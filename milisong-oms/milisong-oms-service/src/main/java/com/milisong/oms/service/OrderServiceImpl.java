package com.milisong.oms.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.PageParam;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.google.common.collect.Lists;
import com.milisong.oms.api.OrderPayService;
import com.milisong.oms.api.OrderService;
import com.milisong.oms.configruation.SystemProperties;
import com.milisong.oms.constant.*;
import com.milisong.oms.domain.Order;
import com.milisong.oms.domain.OrderDelivery;
import com.milisong.oms.domain.OrderDeliveryGoods;
import com.milisong.oms.domain.OrderPaymentWater;
import com.milisong.oms.dto.*;
import com.milisong.oms.mapper.OrderDeliveryGoodsMapper;
import com.milisong.oms.mapper.OrderDeliveryMapper;
import com.milisong.oms.mapper.OrderMapper;
import com.milisong.oms.mapper.OrderPaymentWaterMapper;
import com.milisong.oms.param.CancelOrderParam;
import com.milisong.oms.param.PaymentWaterParam;
import com.milisong.oms.service.promotion.PromotionHelper;
import com.milisong.oms.util.*;
import com.milisong.pms.prom.dto.RedPacketLaunchDto;
import com.milisong.pms.prom.dto.RedPacketLaunchQueryDto;
import com.milisong.pms.prom.dto.RedPacketLaunchResultDto;
import com.milisong.pms.prom.enums.RedPacketLaunchType;
import com.milisong.wechat.miniapp.api.MiniAppService;
import com.milisong.wechat.miniapp.dto.MiniOrderQueryRequest;
import com.milisong.wechat.miniapp.dto.PayOrderQueryResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/2 19:14
 */
@Slf4j
@RestController
@Transactional(rollbackFor = RuntimeException.class)
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderDeliveryMapper orderDeliveryMapper;
    @Resource
    private OrderDeliveryGoodsMapper orderDeliveryGoodsMapper;
    @Resource
    private OrderPaymentWaterMapper orderPaymentWaterMapper;
    @Resource
    private OrderPayService orderPayService;
    @Resource
    private DelayQueueHelper delayQueueHelper;
    @Resource
    private PromotionHelper promotionHelper;
    @Resource
    private MiniAppService miniAppService;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private SystemProperties systemProperties;
    @Resource
    private OrderPaymentHelper orderPaymentHelper;

    @Override
    public ResponseResult<OrderDto> getSimpleOrderById(Long orderId) {
        String cacheKey = OrderRedisKeyUtils.getOrderDetailKey(orderId);
        String jsonString = RedisCache.get(cacheKey);
        log.info("获取缓存订单详情：key:{},data:{}", cacheKey, jsonString);
        return ResponseResult.buildSuccessResponse(JSONObject.parseObject(jsonString, OrderDto.class));
    }

    @Override
    public ResponseResult<OrderDto> getOrderByOrderNo(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        List<OrderDelivery> deliveries = orderDeliveryMapper.findDeliveryDateListByOrderId(order.getId());
        final Set<Long> ids = deliveries.stream().map(OrderDelivery::getId).collect(Collectors.toSet());
        List<OrderDeliveryGoods> deliveryGoods = orderDeliveryGoodsMapper.batchFindByDeliveryIds(ids);
        return ResponseResult.buildSuccessResponse(OrderDtoBuilder.buildOrderDto(order, deliveries, deliveryGoods));
    }


    @Override
    public ResponseResult<?> paymentWater(@RequestBody PaymentWaterParam paymentWaterParam) {
        Long orderId = paymentWaterParam.getOrderId();
        Order order = orderMapper.selectByPrimaryKey(orderId);
        OrderPaymentWater paymentWater = new OrderPaymentWater();
        paymentWater.setId(IdGenerator.nextId());
        paymentWater.setOrderId(orderId);
        paymentWater.setMobileNo(order.getMobileNo());
        paymentWater.setSex(order.getSex());
        paymentWater.setRealName(order.getRealName());
        paymentWater.setOpenId(order.getOpenId());
        paymentWater.setAmount(order.getTotalPayAmount());
        paymentWater.setShopCode(order.getShopCode());
        paymentWater.setUserId(order.getUserId());
        paymentWater.setErrorCode(paymentWaterParam.getErrorCode());
        paymentWater.setErrorDescription(paymentWaterParam.getErrorDescription());
        paymentWater.setStatus(PaymentStatus.FAILED.getValue());
        orderPaymentWaterMapper.insert(paymentWater);
        return ResponseResult.buildSuccessResponse();
    }


    @Override
    public ResponseResult<List<OrderDeliveryDto>> getOrderListByDeliveryDate(Byte businessLine, String shopCode, long deliveryDateTimestamp) {
        log.info("---入参--shopCode：{}-时间戳：{}", shopCode, deliveryDateTimestamp);
        Date deliveryDate = DateUtils.truncate(new Date(deliveryDateTimestamp), Calendar.DATE);
        List<OrderDeliveryDto> orderDeliveryList = orderDeliveryMapper.getOrderListByDeliveryDate(businessLine, shopCode, deliveryDate);
        return ResponseResult.buildSuccessResponse(orderDeliveryList);
    }

    @Override
    public ResponseResult<List<OrderDeliveryDto>> getCompletedDeliveryListByDeliveryDate(long deliveryDateTimestamp, byte orderType) {
        log.info("---入参---{}", deliveryDateTimestamp);
        Date deliveryDate = DateUtils.truncate(new Date(deliveryDateTimestamp), Calendar.DATE);
        List<OrderDeliveryDto> orderDeliveryList = orderDeliveryMapper.getDeliveryListByDeliveryDateAndStatus(deliveryDate, DeliveryStatus.COMPLETED.getValue(), orderType);
        return ResponseResult.buildSuccessResponse(orderDeliveryList);
    }

    @Override
    public ResponseResult<OrderDto> getOrderByDeliveryId(Long deliveryId) {
        OrderDelivery orderDelivery = orderDeliveryMapper.selectByPrimaryKey(deliveryId);
        if (orderDelivery == null) {
            return ResponseResult.buildFailResponse();
        }
        Order order = orderMapper.selectByPrimaryKey(orderDelivery.getOrderId());
        if (order == null) {
            return ResponseResult.buildFailResponse();
        }
        List<OrderDelivery> deliveries = orderDeliveryMapper.findDeliveryDateListByOrderId(order.getId());
        final Set<Long> ids = deliveries.stream().map(OrderDelivery::getId).collect(Collectors.toSet());
        List<OrderDeliveryGoods> deliveryGoods = orderDeliveryGoodsMapper.batchFindByDeliveryIds(ids);
        return ResponseResult.buildSuccessResponse(OrderDtoBuilder.buildOrderDto(order, deliveries, deliveryGoods));
    }

    @Override
    public ResponseResult<Pagination<MyOrderDto>> getOrderList(Byte businessLine, @RequestBody PageParam pageParam, Long userId) {
        Pagination<MyOrderDto> pagination = new Pagination<>();
        try {
            //统计订单总数
            int count = orderMapper.selectOrderForCount(userId, businessLine);
            pagination.setPageNo(pageParam.getPageNo());
            pagination.setPageSize(pageParam.getPageSize());
            pagination.setTotalCount(count);
            if (count > 0) {
                //查询订单主体信息
                List<MyOrderDto> orderList = orderMapper.selectOrderDetail(userId, businessLine, pageParam.getStartRow(), pageParam.getPageSize());
                if (CollectionUtils.isNotEmpty(orderList)) {
                    //红包入参 调用http
                    RedPacketLaunchQueryDto redPacketLaunchQuery = new RedPacketLaunchQueryDto();
                    List<RedPacketLaunchDto> redPacketLaunchList = Lists.newArrayList();
                    for (MyOrderDto myOrder : orderList) {
                        //主订单状态为已支付、已完成
                        if (myOrder.getStatus() == OrderStatus.PAID.getValue() ||
                                myOrder.getStatus() == OrderStatus.COMPLETED.getValue()) {
                            //获取配送单信息
                            List<MyOrderDeliveryDto> orderDeliveryList = myOrder.getOrderDlivery();
                            for (MyOrderDeliveryDto myOrderDelivery : orderDeliveryList) {
                                //配送单状态为备餐中、待配送、配送中、已完成
                                if (myOrderDelivery.getStatus() == DeliveryStatus.DELIVERING.getValue() ||
                                        myOrderDelivery.getStatus() == DeliveryStatus.PACKAGED.getValue() ||
                                        myOrderDelivery.getStatus() == DeliveryStatus.GETTING_READY.getValue() ||
                                        myOrderDelivery.getStatus() == DeliveryStatus.COMPLETED.getValue()) {
                                    RedPacketLaunchDto redPacketLaunchDto = new RedPacketLaunchDto();
                                    redPacketLaunchDto.setLaunchId(myOrder.getId());
                                    if (businessLine != null && businessLine == OrderType.BREAKFAST.getValue()) {
                                        redPacketLaunchDto.setLaunchType(RedPacketLaunchType.BREAKFAST_COUPON_ORDER_SHARE);
                                    } else {
                                        redPacketLaunchDto.setLaunchType(RedPacketLaunchType.ORDER_SHARE);
                                    }
                                    redPacketLaunchDto.setLaunchDate(myOrder.getOrderDate());
                                    redPacketLaunchList.add(redPacketLaunchDto);
                                    continue;
                                }
                            }
                        }
                    }
                    redPacketLaunchQuery.setRedPacketLaunchList(redPacketLaunchList);
                    redPacketLaunchQuery.setBusinessLine(businessLine == null ? OrderType.LUNCH.getValue() : businessLine);
                    //调用多人红包分享
//                    RedPacketLaunchResultDto redPacketResultDto = promotionHelper.multiShareRedPacketLaunchQuery(redPacketLaunchQuery);
                    RedPacketLaunchResultDto redPacketResultDto = promotionHelper.multiShareLaunchQuery(redPacketLaunchQuery);
                    Set<Long> redPacketOrderList = null;
                    if (redPacketResultDto != null) {
                        redPacketOrderList = redPacketResultDto.getValidOrderSet();
                    }
                    //组装订单数据
                    for (MyOrderDto myOrder : orderList) {
                        myOrder.setRedPacketFlag(0);
                        if (redPacketOrderList != null) {
                            if (redPacketOrderList.contains(myOrder.getId())) {
                                myOrder.setRedPacketFlag(1);
                            }
                        }
                        myOrder.setStatusName(OrderStatus.getNameByValue(myOrder.getStatus()));
                        //获取配送单信息
                        List<MyOrderDeliveryDto> orderDeliveryList = myOrder.getOrderDlivery();
                        for (MyOrderDeliveryDto myOrderDelivery : orderDeliveryList) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(myOrderDelivery.getDeliveryDate());
                            //预计配送开始时间转换成毫秒
                            myOrderDelivery.setDeliveryStartTimeMsec(calendar.getTimeInMillis());
                            //转换状态值名称
                            myOrderDelivery.setStatusName(DeliveryStatus.getNameByValue(myOrderDelivery.getStatus()));
                            //根据配送开始时间设置周几
                            myOrderDelivery.setWeekDay(WeekDayUtils.getWeekDayString(myOrderDelivery.getDeliveryDate()));

                        }
                    }
                    pagination.setDataList(orderList);
                }
            } else {
                pagination.setDataList(new ArrayList<>());
            }

        } catch (Exception e) {
            log.error("查询我的订单异常", e);
        }
        return ResponseResult.buildSuccessResponse(pagination);
    }

    @Override
    public ResponseResult<MyOrderDto> getOrderDetailsList(Long orderId, Long userId, Integer orderEndTime) {
        log.info("查询订单详情orderId={},userId={}", orderId, userId);
        ResponseResult<MyOrderDto> responseResult = ResponseResult.buildSuccessResponse();
        try {
            MyOrderDto myOrderDto = orderMapper.selectOrderDetailById(userId, orderId); //查询订单详情
            if (myOrderDto != null && myOrderDto.getId() != null) {
                if (myOrderDto.getStatus() == OrderStatus.PAID.getValue()) {
                    myOrderDto.setStatusName("进行中");
                } else {
                    myOrderDto.setStatusName(OrderStatus.getNameByValue(myOrderDto.getStatus()));
                }
                if (myOrderDto.getStatus() == OrderStatus.UN_PAID.getValue()) {
                    Long expireTime = delayQueueHelper.getOrderExpireTime(myOrderDto.getId());
                    myOrderDto.setCountDown(expireTime * 1000); //获取待支付倒计时
                }
                List<MyOrderDeliveryDto> orderDeliveryList = myOrderDto.getOrderDlivery(); //获取配送单信息
                if (CollectionUtils.isNotEmpty(orderDeliveryList)) {
                    //循环子单按天数据
                    for (MyOrderDeliveryDto myOrderDelivery : orderDeliveryList) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(myOrderDelivery.getDeliveryDate());
                        myOrderDelivery.setDeliveryStartTimeMsec(calendar.getTimeInMillis()); //预计配送开始时间转换成毫秒
                        myOrderDelivery.setStatusName(DeliveryStatus.getNameByValue(myOrderDelivery.getStatus()));
                        myOrderDelivery.setWeekDay(WeekDayUtils.getWeekDayString(myOrderDelivery.getDeliveryDate())); //设置周几
                        if (myOrderDelivery.getStatus() == DeliveryStatus.COMPLETED.getValue()) {
                            Date deliverEndTime = myOrderDelivery.getDeliveryEndTime();//配送完成时间
                            if (deliverEndTime != null) {
                                boolean result = DateUtil.computeEndTime(deliverEndTime, new Date(), orderEndTime);
                                log.info("已完成订单结果标识{}", result);
                                if (result) {
                                    myOrderDelivery.setEngFlag(1); //已经超过已完成配置时间范围
                                } else {
                                    myOrderDelivery.setEngFlag(0);
                                }
                            }
                        }
                    }

                }
            }
            responseResult.setData(myOrderDto);
            log.info("订单详情返回结果{}", JSON.toJSONString(responseResult));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询订单详情异常", e);
        }
        return responseResult;
    }

    @Override
    public void updateDeliveryByOrderNo(@RequestBody OrderDeliveryMqDto deliveryMqDto) {
        orderDeliveryMapper.updateDeliveryByOrderNo(deliveryMqDto);
    }

    @Override
    public ResponseResult<?> cancelToPayOrder() {
        try {
            Integer unPayExpiredTime = SysConfigUtils.getUnPayExpiredTime();
            if (unPayExpiredTime == null) {
                unPayExpiredTime = 300;
            } else {
                unPayExpiredTime = unPayExpiredTime * 60;
            }
            List<Order> orderList = orderMapper.selectOrderByStatus(OrderStatus.UN_PAID.getValue(), unPayExpiredTime);
            for (Order order : orderList) {
                Date orderDate = order.getOrderDate();
                Date currentDate = new Date();
                if (DateUtil.computeTime(orderDate, currentDate, unPayExpiredTime)) {
                    CancelOrderParam orderParam = new CancelOrderParam();
                    orderParam.setOrderId(order.getId());
                    orderParam.setCancelReason(CancelReason.UNPAID_EXPIRED.getValue());
                    orderPayService.expiredCancelOrder(orderParam);
                }
            }
            return ResponseResult.buildSuccessResponse();
        } catch (Exception e) {
            log.error("取消待支付订单异常", e);
            return ResponseResult.buildFailResponse();
        }
    }

    @Override
    public ResponseResult<OrderDto> getOrderById(Long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        List<OrderDelivery> deliveries = orderDeliveryMapper.findDeliveryDateListByOrderId(orderId);
        final Set<Long> ids = deliveries.stream().map(OrderDelivery::getId).collect(Collectors.toSet());
        List<OrderDeliveryGoods> deliveryGoods = orderDeliveryGoodsMapper.batchFindByDeliveryIds(ids);
        return ResponseResult.buildSuccessResponse(OrderDtoBuilder.buildOrderDto(order, deliveries, deliveryGoods));
    }

    @Override
    public ResponseResult<?> getUnPayOrder() {
        List<Order> orderList = orderMapper.selectOrderByStatus(OrderStatus.UN_PAID.getValue(), 0);
        log.info("查询待支付订单数据结果{}", JSON.toJSONString(orderList));
        try {
            if (CollectionUtils.isNotEmpty(orderList)) {
                for (Order order : orderList) {
                    MiniOrderQueryRequest orderQueryRequest = new MiniOrderQueryRequest();
                    orderQueryRequest.setOutTradeNo(String.valueOf(order.getId()));
                    orderQueryRequest.setBusinessLine(order.getOrderType());
                    ResponseResult<PayOrderQueryResult> responseResult = miniAppService.queryOrder(orderQueryRequest);
                    if (responseResult.isSuccess()) {
                        PayOrderQueryResult orderQueryResult = responseResult.getData();
                        if (orderQueryResult != null) {
                            if ("SUCCESS".equalsIgnoreCase(orderQueryResult.getTradeState())) {
//                                Order record = new Order();
//                                record.setId(order.getId());
//                                record.setStatus(OrderStatus.PAID.getValue());
//                                orderMapper.updateByPrimaryKeySelective(record);
                                orderPaymentHelper.handlePaySuccess(order);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("查询待支付订单异常", e);
        }
        return ResponseResult.buildSuccessResponse();
    }

    @Override
    public ResponseResult<List<OrderPlanDeliveryDto>> getOrderPlanList(Long userId, Integer orderEndTime) {
        ResponseResult<List<OrderPlanDeliveryDto>> responseResult = ResponseResult.buildSuccessResponse();
        try {
            //订单已支付、已结束
            List<Byte> orderStatusList = Lists.newArrayList();
            orderStatusList.add(OrderStatus.PAID.getValue());
            orderStatusList.add(OrderStatus.COMPLETED.getValue());

            //配送单配送中、待配送、备餐中、已完成
            List<Byte> deliveryStatusList = Lists.newArrayList();
            deliveryStatusList.add(DeliveryStatus.DELIVERING.getValue());
            deliveryStatusList.add(DeliveryStatus.PACKAGED.getValue());
            deliveryStatusList.add(DeliveryStatus.GETTING_READY.getValue());
            deliveryStatusList.add(DeliveryStatus.COMPLETED.getValue());
            List<OrderPlanDeliveryDto> orderPlanResultList = Lists.newArrayList();
            List<OrderPlanDeliveryDto> orderPlanList = orderMapper.selectOrderPlan(userId, orderStatusList, deliveryStatusList);
            log.info("-----------------{}", JSON.toJSONString(orderPlanList));
            if (CollectionUtils.isNotEmpty(orderPlanList)) {
                for (OrderPlanDeliveryDto planDelivery : orderPlanList) {
                    if (planDelivery.getStatus() == DeliveryStatus.COMPLETED.getValue()) {
                        Date deliverEndTime = planDelivery.getDeliveryEndTime();//配送完成时间
                        if (deliverEndTime != null) {
                            boolean result = DateUtil.computeEndTime(deliverEndTime, new Date(), orderEndTime);
                            if (result) {
                                continue;
                            }
                        }
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(planDelivery.getDeliveryDate());
                    //预计配送开始时间转换成毫秒
                    planDelivery.setDeliveryStartTimeMsec(calendar.getTimeInMillis());
                    //转换状态值名称
                    planDelivery.setStatusName(DeliveryStatus.getNameByValue(planDelivery.getStatus()));
                    //根据配送开始时间设置周几
                    planDelivery.setWeekDay(WeekDayUtils.getWeekDayString(planDelivery.getDeliveryDate()));
                    orderPlanResultList.add(planDelivery);
                }

            }
            responseResult.setData(orderPlanResultList);
        } catch (Exception e) {
            log.error("我的订单计划异常", e);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<List<OrderPlanDeliveryDto>> getOrderPlanList(Long userId, Integer orderEndTime, Byte businessLine) {
        log.info("----------------业务线----{}", businessLine);
        if (businessLine == null) {
            businessLine = OrderType.BREAKFAST.getValue();
        }

        ResponseResult<List<OrderPlanDeliveryDto>> responseResult = ResponseResult.buildSuccessResponse();
        try {
            //订单已支付、已结束
            List<Byte> orderStatusList = Lists.newArrayList();
            orderStatusList.add(OrderStatus.PAID.getValue());
            orderStatusList.add(OrderStatus.COMPLETED.getValue());

            //配送单配送中、待配送、备餐中、已完成
            List<Byte> deliveryStatusList = Lists.newArrayList();
            deliveryStatusList.add(DeliveryStatus.DELIVERING.getValue());
            deliveryStatusList.add(DeliveryStatus.PACKAGED.getValue());
            deliveryStatusList.add(DeliveryStatus.GETTING_READY.getValue());
            deliveryStatusList.add(DeliveryStatus.COMPLETED.getValue());
            List<OrderPlanDeliveryDto> orderPlanResultList = Lists.newArrayList();
            List<OrderPlanDeliveryDto> orderPlanList = orderMapper.selectOrderPlan2(userId, orderStatusList, deliveryStatusList, businessLine);
            if (CollectionUtils.isNotEmpty(orderPlanList)) {
                for (OrderPlanDeliveryDto planDelivery : orderPlanList) {
                    if (planDelivery.getStatus() == DeliveryStatus.COMPLETED.getValue()) {
                        Date deliverEndTime = planDelivery.getDeliveryEndTime();//配送完成时间
                        if (deliverEndTime != null) {
                            boolean result = DateUtil.computeEndTime(deliverEndTime, new Date(), orderEndTime);
                            if (result) {
                                continue;
                            }
                        }
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(planDelivery.getDeliveryDate());
                    //预计配送开始时间转换成毫秒
                    planDelivery.setDeliveryStartTimeMsec(calendar.getTimeInMillis());
                    //转换状态值名称
                    planDelivery.setStatusName(DeliveryStatus.getNameByValue(planDelivery.getStatus()));
                    //根据配送开始时间设置周几
                    planDelivery.setWeekDay(WeekDayUtils.getWeekDayString(planDelivery.getDeliveryDate()));
                    orderPlanResultList.add(planDelivery);
                }

            }
            responseResult.setData(orderPlanResultList);
        } catch (Exception e) {
            log.error("我的订单计划异常", e);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<RiderInfoDto> getRifderInfo(String deliveryNo, Long orderId, Byte businessLine) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        RiderInfoDto riderInfo = getRiderInfoForScm(deliveryNo, businessLine);
        List<ShungFengStatusDto> shunfengStatusList = riderInfo.getList();
        if (CollectionUtils.isNotEmpty(shunfengStatusList)) {
            for (ShungFengStatusDto shunfeng : shunfengStatusList) {
                if (shunfeng.getStatus() == ShunFengDeliveryStatus.COMPLETED.getValue()
                        && StringUtils.isEmpty(shunfeng.getCompleteTime())) {
                    shunfeng.setStatusName("骑手送货中");
                    shunfeng.setStatus((byte) 9);
                }
            }

        }
        Order order = orderMapper.selectByPrimaryKey(orderId);
        ShungFengStatusDto shunfengStatus = new ShungFengStatusDto();
        if (order != null) {
            Date date = order.getOrderDate();
            shunfengStatus.setCompleteTime(format.format(date));
        }
        shunfengStatus.setStatus((byte) -1);
        shunfengStatus.setStatusName("门店备餐中");
        shunfengStatusList.add(shunfengStatus);
        shunfengStatusList.sort(Comparator.comparing(ShungFengStatusDto::getStatus));
        return ResponseResult.buildSuccessResponse(riderInfo);
    }

    @Override
    public ResponseResult<List<String>> getDeliveryNos(Byte businessLine, String deliveryDate) {
        log.info("订单补偿接口查询businessLine={},deliveryDate={}", businessLine, deliveryDate);
        if (null == businessLine || StringUtils.isBlank(deliveryDate)) {
            return ResponseResult.buildFailResponse("", "参数不能为空");
        }
        List<String> deliveryNoList = orderDeliveryMapper.getDeliveryNos(businessLine, deliveryDate);
        return ResponseResult.buildSuccessResponse(deliveryNoList);
    }

    @Override
    public ResponseResult<OrderDto> getOrderByDeliveryNo(String deliveryNo) {
        log.info("根据配送单号查询订单信息{}", deliveryNo);
        ResponseResult<OrderDto> responseResult = ResponseResult.buildSuccessResponse();
        if (StringUtils.isBlank(deliveryNo)) {
            return ResponseResult.buildFailResponse("", "参数不能为空");
        }
        OrderDelivery orderDelivery = orderDeliveryMapper.findDeliveryByNo(deliveryNo);
        if (orderDelivery != null) {
            OrderDto orderDto = orderMapper.selectOrderPoints(orderDelivery.getOrderId());
            responseResult.setData(orderDto);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<List<OrderDeliveryGoodsDto>> getGoodsByDeliveryId(Long deliveryId) {
        List<OrderDeliveryGoods> goodsList = orderDeliveryGoodsMapper.findAllByDeliveryId(deliveryId);
        return ResponseResult.buildSuccessResponse(BeanMapper.mapList(goodsList, OrderDeliveryGoodsDto.class));
    }

    private RiderInfoDto getRiderInfoForScm(String deliveryNo, Byte businessLine) {
        RiderInfoDto riInfoDto = new RiderInfoDto();
        ResponseEntity<ResponseResult> responseEntity = restTemplate.getForEntity(systemProperties.getScmBaseUrl() + "/shunfeng/query-rider-info?deliveryNo={1}&businessType={2}", ResponseResult.class, deliveryNo, businessLine);
        ResponseResult result = responseEntity.getBody();
        log.info("调用scm查询配送信息返回结果{}", JSON.toJSONString(result));
        if (result.isSuccess()) {
            Object objData = result.getData();
            if (null == objData) {
                return riInfoDto;
            }
            String data = JSON.toJSONString(result.getData());
            return JSONObject.parseObject(data, RiderInfoDto.class);
        }
        return riInfoDto;
    }
}
