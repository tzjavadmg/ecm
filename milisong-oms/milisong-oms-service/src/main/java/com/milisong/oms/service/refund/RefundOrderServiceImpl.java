package com.milisong.oms.service.refund;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.exception.BusinessException;
import com.farmland.core.util.BeanMapper;
import com.milisong.oms.api.RefundOrderService;
import com.milisong.oms.api.StockService;
import com.milisong.oms.configruation.SystemProperties;
import com.milisong.oms.constant.*;
import com.milisong.oms.domain.*;
import com.milisong.oms.dto.RefundOrderDetailDto;
import com.milisong.oms.mapper.*;
import com.milisong.oms.mq.MessageProducer;
import com.milisong.oms.mq.RefundGoodsMessage;
import com.milisong.oms.mq.RefundMessage;
import com.milisong.oms.param.RefundOrderParam;
import com.milisong.oms.service.stock.DeliveryCounter;
import com.milisong.oms.service.stock.ShopDailyStockBuilder;
import com.milisong.oms.util.OrderRedisKeyUtils;
import com.milisong.oms.util.XMLUtil;
import com.milisong.pms.prom.api.BreakfastCouponService;
import com.milisong.pms.prom.api.UserRedPacketService;
import com.milisong.pms.prom.dto.UserCouponQueryParam;
import com.milisong.wechat.miniapp.api.MiniAppService;
import com.milisong.wechat.miniapp.dto.MiniPayRefundRequest;
import com.milisong.wechat.miniapp.dto.MiniPayRefundResult;
import com.milisong.wechat.miniapp.dto.MiniRefundQueryRequest;
import com.milisong.wechat.miniapp.dto.MiniRefundQueryResult;
import com.milisong.wechat.miniapp.util.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/10 13:41
 */
@Slf4j
@RestController
@Transactional(rollbackFor = Exception.class)
public class RefundOrderServiceImpl implements RefundOrderService {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderDeliveryMapper orderDeliveryMapper;
    @Resource
    private RefundOrderMapper refundOrderMapper;
    @Resource
    private RefundOrderDetailMapper refundOrderDetailMapper;
    @Resource
    private MiniAppService miniAppService;
    @Resource
    private MessageProducer messageProducer;
    @Resource
    private RefundWaterMapper refundWaterMapper;
    @Resource
    private StockService stockService;
    @Resource
    private OrderDeliveryGoodsMapper orderDeliveryGoodsMapper;
    @Resource
    private DeliveryCounter deliveryCounter;
    @Resource
    private UserRedPacketService userRedPacketService;
    @Resource
    private BreakfastCouponService breakfastCouponService;
    @Resource
    private SystemProperties systemProperties;

    @Override
    public ResponseResult<?> refund(@RequestBody RefundOrderParam refundOrderParam) {
        log.info("------------申请退款，入参：{}", JSON.toJSONString(refundOrderParam));
        Long orderId = refundOrderParam.getOrderId();
        Order order = orderMapper.selectByPrimaryKey(orderId);

        if (OrderMode.GROUP_BUY.getValue() == order.getOrderMode()) {
            throw BusinessException.build("", "已拼团成功的订单不能退款！订单ID:" + orderId);
        }
        String shopCode = order.getShopCode();
        //本次要退款的配送记录
        Set<Long> deliveryIds = refundOrderParam.getDeliveryIds();
        String cancelReason = refundOrderParam.getCancelReason();
        //校验，如果有过截单时间的不能退款
        List<OrderDelivery> orderDeliveries = orderDeliveryMapper.batchFind(deliveryIds);
        Date now = new Date();
        for (OrderDelivery orderDelivery : orderDeliveries) {
            Date deliveryDate = orderDelivery.getDeliveryDate();
            if (OrderType.BREAKFAST.getValue() == order.getOrderType()) {
                log.info("----------今天零点--------{}", DateUtils.truncate(now, Calendar.DATE));
                log.info("----------比较值--------{}", deliveryDate.compareTo(DateUtils.truncate(now, Calendar.DATE)) < 0);
                if (deliveryDate.compareTo(DateUtils.truncate(now, Calendar.DATE)) <= 0) {
                    return ResponseResult.buildFailResponse(OrderResponseCode.REFUND_CHECK_CUTOFF_TIME_EXPIRED_B.getCode(), OrderResponseCode.REFUND_CHECK_CUTOFF_TIME_EXPIRED_B.getMessage());
                }
            } else {
                log.info("-------------截单时间-----{}", orderDelivery.getDeliveryTimezoneCutoffTime());
                if (now.compareTo(orderDelivery.getDeliveryTimezoneCutoffTime()) >= 0) {
                    return ResponseResult.buildFailResponse(OrderResponseCode.REFUND_CHECK_CUTOFF_TIME_EXPIRED.getCode(), OrderResponseCode.REFUND_CHECK_CUTOFF_TIME_EXPIRED.getMessage());
                }
            }
        }
        Long refundId = IdGenerator.nextId();
        RefundOrder refundOrder = buildBaseRefundOrder(order, refundId);
        BigDecimal totalPayAmount = order.getTotalPayAmount();

        int goodsCount = 0;
        int deliveryCount = orderDeliveries.size();

        BigDecimal refundTotalPackageAmount = BigDecimal.ZERO;
        BigDecimal refundTotalDeliveryAmount = BigDecimal.ZERO;
        BigDecimal refundTotalGoodsAmount = BigDecimal.ZERO;
        BigDecimal refundTotalRedPacketAmount = BigDecimal.ZERO;
        BigDecimal refundTotalAmount = BigDecimal.ZERO;
        Integer refundTotalPoints = 0;

        List<RefundOrderDetail> refundOrderDetails = new ArrayList<>();
        List<DeliveryCounter.DateCounter> dateCounterList = new ArrayList<>();
        for (OrderDelivery orderDelivery : orderDeliveries) {
            Integer deliveryGoodsCount = orderDelivery.getTotalGoodsCount();
            RefundOrderDetail refundOrderDetail = buildRefundOrderDetail(order, refundId, orderDelivery, deliveryGoodsCount);

            goodsCount += deliveryGoodsCount;

            refundTotalPackageAmount = refundTotalPackageAmount.add(orderDelivery.getTotalPackageActualAmount());
            refundTotalDeliveryAmount = refundTotalDeliveryAmount.add(orderDelivery.getDeliveryActualPrice());
            refundTotalGoodsAmount = refundTotalGoodsAmount.add(orderDelivery.getTotalGoodsActualAmount());
            refundTotalAmount = refundTotalAmount.add(refundOrderDetail.getTotalRefundAmount());
            if (refundOrderDetail.getTotalRefundPoints() != null) {
                refundTotalPoints = refundTotalPoints + refundOrderDetail.getTotalRefundPoints();
            }
            refundOrderDetails.add(refundOrderDetail);
            dateCounterList.add(new DeliveryCounter.DateCounter(orderDelivery.getDeliveryTimezoneFrom(), deliveryGoodsCount));
        }

        refundOrder.setGoodsCount(goodsCount);
        refundOrder.setDeliveryCount(deliveryCount);
        refundOrder.setTotalRefundAmount(refundTotalAmount);
        refundOrder.setGoodsAmount(refundTotalGoodsAmount);
        refundOrder.setDeliveryAmount(refundTotalDeliveryAmount);
        refundOrder.setPackageAmount(refundTotalPackageAmount);
        refundOrder.setRedPacketAmount(refundTotalRedPacketAmount);
        refundOrder.setTotalRefundPoints(refundTotalPoints);
        refundOrder.setApplyTime(new Date());
        refundOrder.setCancelReason(cancelReason);

        ResponseResult<?> responseResult = wechatMiniAppRefund(order, refundId, totalPayAmount, refundTotalAmount);
        if (!responseResult.isSuccess()) {
            return responseResult;
        }
        refundOrderMapper.insert(refundOrder);
        refundOrderDetailMapper.batchSave(refundOrderDetails);
        orderDeliveries.forEach(this::updateOrderDeliveryStatus);
        List<OrderDeliveryGoods> orderDeliveryGoods = orderDeliveryGoodsMapper.batchFindByDeliveryIds(deliveryIds);
        sendMq(order.getUserId(), shopCode, refundOrderDetails, orderDeliveryGoods);

        updatePoints(order);
        //申请退款成功后，回滚库存
        rollbackStock(refundOrder);
        //回滚生产量
        deliveryCounter.decrement(shopCode, dateCounterList);
        //回滚月销量
        rollbackMonthlySales(orderDeliveryGoods);
        return ResponseResult.buildSuccessResponse();
    }

    private void updatePoints(Order order) {
        List<RefundOrder> refundOrders = refundOrderMapper.getRefundOrderByOrderId(order.getId());
        double totalRefundGoodsAmountDouble = refundOrders.stream().map(refundOrder ->
                refundOrder.getTotalRefundAmount().subtract(refundOrder.getDeliveryAmount()).subtract(refundOrder.getPackageAmount())
        ).collect(Collectors.summarizingDouble(BigDecimal::doubleValue)).getSum();

        BigDecimal totalRefundGoodsAmount = new BigDecimal(totalRefundGoodsAmountDouble).setScale(2, RoundingMode.HALF_UP);
        log.info("--------------退款商品总金额-----{}", totalRefundGoodsAmount);
        BigDecimal totalGoodsAmount = order.getTotalPayAmount().subtract(order.getTotalDeliveryActualAmount()).subtract(order.getTotalPackageActualAmount());
        log.info("--------------商品实付总金额-----{}", totalGoodsAmount);
        log.info("----不要再赠送积分----");
        BigDecimal acquirePoints = new BigDecimal("0");//totalGoodsAmount.subtract(totalRefundGoodsAmount).setScale(0, RoundingMode.CEILING);
        log.info("--------------商品余留实付总金额-----{}", acquirePoints);
        order.setAcquirePoints(acquirePoints.intValue());
        orderMapper.updateByPrimaryKeySelective(order);
    }

    private void sendMq(Long userId, String shopCode, List<RefundOrderDetail> refundOrderDetails, List<OrderDeliveryGoods> orderDeliveryGoods) {
        Map<Long, List<OrderDeliveryGoods>> deliveryGoodsMap = new HashMap<>();
        for (OrderDeliveryGoods goods : orderDeliveryGoods) {
            Long deliveryId = goods.getDeliveryId();
            List<OrderDeliveryGoods> goodsList = deliveryGoodsMap.get(deliveryId);
            if (goodsList == null) {
                goodsList = new ArrayList<>();
            }
            goodsList.add(goods);
            deliveryGoodsMap.putIfAbsent(deliveryId, goodsList);
        }
        List<RefundMessage> refundMessages = BeanMapper.mapList(refundOrderDetails, RefundMessage.class);
        refundMessages.forEach(refundMessage -> {
            refundMessage.setShopCode(shopCode);
            refundMessage.setUserId(userId);
            refundMessage.setRefundGoods(BeanMapper.mapList(deliveryGoodsMap.get(refundMessage.getDeliveryId()), RefundGoodsMessage.class));
        });
        log.info("-----------发送退款MQ消息---------{}", JSON.toJSONString(refundMessages));
        messageProducer.send(refundMessages);
    }

    private void rollbackMonthlySales(List<OrderDeliveryGoods> deliveryGoods) {
        //记录商品月销量
        if (deliveryGoods != null) {
            deliveryGoods.forEach(goods -> {
                String goodsCode = goods.getGoodsCode();
                Integer goodsCount = goods.getGoodsCount();
                String lately30DaysSalesKey = OrderRedisKeyUtils.getLately30DaysSalesKey(goodsCode);
                RedisCache.incrBy(lately30DaysSalesKey, -new Long(goodsCount));
            });
        }
        log.info("==============记录月销：{}", JSON.toJSONString(deliveryGoods));
    }

    private ResponseResult<?> wechatMiniAppRefund(Order order, Long refundId, BigDecimal totalPayAmount, BigDecimal refundTotalAmount) {
        MiniPayRefundRequest miniPayRefundRequest = new MiniPayRefundRequest();
        //outTradeNo 订单号、outRefundNo 退款单号、totalAmount 支付总金额、refundAmount 退款金额
        miniPayRefundRequest.setOutTradeNo(String.valueOf(order.getId()));
        miniPayRefundRequest.setOutRefundNo(String.valueOf(refundId));
        miniPayRefundRequest.setTotalAmount(totalPayAmount);
        miniPayRefundRequest.setRefundAmount(refundTotalAmount);
        miniPayRefundRequest.setBusinessLine(order.getOrderType());
        miniPayRefundRequest.setNotifyUrl(systemProperties.getWechatPay().getDefaultRefundCallbackUrl());
        log.info("调用微信退款接口，入参：{}", JSON.toJSONString(miniPayRefundRequest));
        ResponseResult<MiniPayRefundResult> responseResult = miniAppService.refund(miniPayRefundRequest);
        log.info("调用微信退款接口,返回参数：{}", JSON.toJSONString(responseResult));
        if (!responseResult.isSuccess()) {
            MiniPayRefundResult result = responseResult.getData();
            saveRefundWaterAndOrderStatus(order, refundTotalAmount, result);
            return ResponseResult.buildFailResponse(result.getErrCode(), result.getErrCodeDes());
        }
        return responseResult;
    }

    private void saveRefundWaterAndOrderStatus(Order order, BigDecimal refundTotalAmount, MiniPayRefundResult result) {
        RefundWater refundWater = buildRefundWater(order, refundTotalAmount);
        if (result != null) {
            refundWater.setErrorCode(result.getErrCode());
            refundWater.setErrorDescription(result.getErrCodeDes());
        }
        refundWater.setStatus(RefundStatus.FAILED.getValue());
        refundWaterMapper.insert(refundWater);
    }

    private RefundWater buildRefundWater(Order order, BigDecimal refundTotalAmount) {
        RefundWater refundWater = new RefundWater();
        refundWater.setId(IdGenerator.nextId());
        refundWater.setOrderId(order.getId());
        refundWater.setUserId(order.getUserId());
        refundWater.setOrderType(order.getOrderType());
        refundWater.setOrderMode(order.getOrderMode());
        refundWater.setAmount(refundTotalAmount);
        refundWater.setOpenId(order.getOpenId());
        refundWater.setRealName(order.getRealName());
        refundWater.setShopCode(order.getShopCode());
        refundWater.setSex(order.getSex());
        refundWater.setMobileNo(order.getMobileNo());
        return refundWater;
    }

    private RefundOrderDetail buildRefundOrderDetail(Order order, Long refundId, OrderDelivery orderDelivery, Integer deliveryGoodsCount) {
        RefundOrderDetail refundOrderDetail = new RefundOrderDetail();
        refundOrderDetail.setId(IdGenerator.nextId());
        refundOrderDetail.setRefundId(refundId);
        refundOrderDetail.setGoodsCount(deliveryGoodsCount);
        refundOrderDetail.setOrderId(order.getId());
        refundOrderDetail.setOrderNo(order.getOrderNo());
        refundOrderDetail.setDeliveryId(orderDelivery.getId());
        refundOrderDetail.setDeliveryNo(orderDelivery.getDeliveryNo());
        refundOrderDetail.setDeliveryDate(orderDelivery.getDeliveryDate());
        refundOrderDetail.setDeliveryAmount(orderDelivery.getDeliveryActualPrice());
        refundOrderDetail.setPackageAmount(orderDelivery.getTotalPackageActualAmount());
        refundOrderDetail.setGoodsAmount(orderDelivery.getTotalGoodsActualAmount());
        refundOrderDetail.setTotalRefundAmount(orderDelivery.getShareOrderPayAmount());
        refundOrderDetail.setTotalRefundPoints(orderDelivery.getShareOrderDeductionPoints());
        refundOrderDetail.setOrderType(orderDelivery.getOrderType());
        return refundOrderDetail;
    }

    private void updateOrderDeliveryStatus(OrderDelivery orderDelivery) {
        orderDelivery.setStatus(DeliveryStatus.CANCELED.getValue());
        orderDelivery.setCancelTime(new Date());
        orderDeliveryMapper.updateByPrimaryKeySelective(orderDelivery);
    }

    private RefundOrder buildBaseRefundOrder(Order order, Long refundId) {
        RefundOrder refundOrder = new RefundOrder();
        refundOrder.setId(refundId);
        refundOrder.setOrderId(order.getId());
        refundOrder.setOrderNo(order.getOrderNo());
        refundOrder.setUserId(order.getUserId());
        refundOrder.setRealName(order.getRealName());
        refundOrder.setMobileNo(order.getMobileNo());
        refundOrder.setSex(order.getSex());
        refundOrder.setShopCode(order.getShopCode());
        refundOrder.setOrderDate(order.getOrderDate());
        refundOrder.setStatus(RefundOrderStatus.RDFUNDING.getValue());
        refundOrder.setOrderType(order.getOrderType());
        refundOrder.setOrderMode(order.getOrderMode());
        return refundOrder;
    }

    @Override
    public ResponseResult<RefundOrderDetailDto> getRefundOrderDetail(Long deliveryId) {
        RefundOrderDetailDto refundOrderDetailDto = refundOrderDetailMapper.getRefundOrderDetail(deliveryId);
        //退款中时间与申请时间一致
        refundOrderDetailDto.setRefundingTime(refundOrderDetailDto.getApplyTime());
        log.info("退款详情返回结果{}", JSON.toJSONString(refundOrderDetailDto));
        return ResponseResult.buildSuccessResponse(refundOrderDetailDto);
    }

    /**
     * 退款回调
     *
     * @param map
     */
    @Override
    public void refundCallback(@RequestBody SortedMap<String, String> map) {
        try {
            // 支付状态
            String resultCode = map.get("returnCode");
            String returnMsg = map.get("returnMsg");
            // 加密数据
            String reqInfo = map.get("reqInfo");
            String decryptReqInfo = AESUtil.decryptData(reqInfo);
            log.info("退款回调，返回数据解密后 -> {}", decryptReqInfo);
            SortedMap<String, String> reqInfoObj = XMLUtil.doXMLParse(decryptReqInfo);

            String outTradeNo = reqInfoObj.get("outTradeNo");
            String outRefundNo = reqInfoObj.get("outRefundNo");
            String refundFee = reqInfoObj.get("refundFee");
            BigDecimal refundAmount = new BigDecimal(refundFee);
            refundAmount = refundAmount.divide(BigDecimal.valueOf(100));

            Long orderId = Long.valueOf(outTradeNo);
            Long refundId = Long.valueOf(outRefundNo);

            Order order = orderMapper.selectByPrimaryKey(orderId);
            RefundOrder refundOrder = refundOrderMapper.selectByPrimaryKey(refundId);
            if (order == null) {
                log.error("根据订单ID:{}，查询不到订单数据", orderId);
                return;
            }
            if (refundOrder == null) {
                log.error("根据退款单ID:{}，查询不到退款单数据", refundId);
                return;
            }

            RefundWater refundWater = buildRefundWater(order, refundAmount);
            refundWater.setRefundId(refundId);
            if ("SUCCESS".equals(resultCode)) { //成功
                refundWater.setStatus(RefundStatus.SUCCEED.getValue());
                refundOrder.setStatus(RefundOrderStatus.SUCCEED.getValue());
                refundOrder.setRefundTime(new Date());

                log.info("outTradeNo -> {}, outRefundNo -> {}", outTradeNo, outRefundNo);
                List<RefundOrderDetail> refundOrderDetailList = refundOrderDetailMapper.getListByRefundId(refundId);
                log.info("-------退款回调----退款明细：{}", JSON.toJSONString(refundOrderDetailList));
                refundOrderDetailList.forEach(refundOrderDetail -> {
                    OrderDelivery orderDelivery = new OrderDelivery();
                    orderDelivery.setId(refundOrderDetail.getDeliveryId());
                    orderDelivery.setStatus(DeliveryStatus.REFUNDED.getValue());
                    orderDelivery.setRefundTime(new Date());
                    orderDeliveryMapper.updateByPrimaryKeySelective(orderDelivery);
                    log.info("更新子单配送状态：{}", JSON.toJSONString(orderDelivery));
                });

                refundWaterMapper.insert(refundWater);
                refundOrderMapper.updateByPrimaryKeySelective(refundOrder);
                //更新订单状态，是否完成
                updateOrderStatus(order, refundOrder);
                //如果全额退款，则返回红包
                updateRedPacketStatus(order);
                //如果全额退款，则返回优惠券
                updateCouponStatus(order);
            } else { //失败
                String errorCode = map.get("errorCode");
                String errCodeDes = map.get("errCodeDes");
                refundWater.setErrorCode(errorCode);
                refundWater.setErrorDescription(errCodeDes);
                refundWater.setStatus(RefundStatus.FAILED.getValue());

                refundOrder.setStatus(RefundOrderStatus.FAILED.getValue());
                refundOrder.setRefundTime(new Date());

                refundWaterMapper.insert(refundWater);
                refundOrderMapper.updateByPrimaryKeySelective(refundOrder);
                log.info("退款失败 原因 -> {}", returnMsg);
            }
            log.info("--------------退款回调流水：{}", JSON.toJSONString(refundWater));
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public ResponseResult<?> getRefundingOrder() {
        List<RefundOrder> refundOrderList = refundOrderMapper.getRefundingOrder(RefundOrderStatus.RDFUNDING.getValue());
        log.info("查询退款中订单数据结果{}", JSON.toJSONString(refundOrderList));
        try {
            if (CollectionUtils.isNotEmpty(refundOrderList)) {
                for (RefundOrder refundOrder : refundOrderList) {
                    MiniRefundQueryRequest refundQueryRequest = new MiniRefundQueryRequest();
                    refundQueryRequest.setOutRefundNo(String.valueOf(refundOrder.getId()));
                    ResponseResult<MiniRefundQueryResult> responseResult = miniAppService.refundQuery(refundQueryRequest);
                    log.info("查询退款入参,退款单id={},返回结果={}", refundOrder.getId(), JSON.toJSONString(responseResult));
                    if (responseResult.isSuccess()) {
                        MiniRefundQueryResult refundQueryResult = responseResult.getData();
                        if (refundQueryResult != null) {
                            //调用查询微信退款返回退款成功
                            String refundStatus = refundQueryResult.getRefundRecords().get(0).getRefundStatus();
                            if ("SUCCESS".equalsIgnoreCase(refundStatus)) {
                                //更新退款单位退款成功
                                RefundOrder rdOrder = new RefundOrder();
                                rdOrder.setId(refundOrder.getId());
                                rdOrder.setStatus(RefundOrderStatus.SUCCEED.getValue());
                                refundOrderMapper.updateByPrimaryKeySelective(rdOrder);
                                //更新订单状态为已结束
                                Order order = new Order();
                                order.setId(refundOrder.getOrderId());
                                order.setStatus(OrderStatus.COMPLETED.getValue());
                                orderMapper.updateByPrimaryKeySelective(order);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("查询退款异常", e);
        }
        return ResponseResult.buildSuccessResponse();
    }

    private void rollbackStock(RefundOrder refundOrder) {
        log.info("取消未支付订单，释放库存：{}", JSON.toJSONString(refundOrder));
        String shopCode = refundOrder.getShopCode();

        List<RefundOrderDetail> refundOrderDetails = refundOrderDetailMapper.getListByRefundId(refundOrder.getId());
        log.info("------------退款明细：{}", JSON.toJSONString(refundOrderDetails));
        final Set<Long> detailIds = refundOrderDetails.stream().map(RefundOrderDetail::getDeliveryId).collect(Collectors.toSet());
        final List<OrderDelivery> deliveryList = orderDeliveryMapper.batchFind(detailIds);
        final Set<Long> ids = deliveryList.stream().map(OrderDelivery::getId).collect(Collectors.toSet());
        final List<OrderDeliveryGoods> deliveryGoodsList = orderDeliveryGoodsMapper.batchFindByDeliveryIds(ids);

        List<StockService.ShopDailyStock> dailyGoodsStockList = ShopDailyStockBuilder.buildIncrementDailyGoodsStocks(GoodsStockSource.REFUND_ORDER.getValue(), shopCode, deliveryList, deliveryGoodsList);
        stockService.lockMultiDayStock(dailyGoodsStockList);
        try {
            //回滚相关商品库存
            stockService.incrementMultiDayStock(dailyGoodsStockList);
            log.info("~~~~~~库存流水-退款回滚=订单ID：{},门店编码：{}，库存信息：{}", refundOrder.getId(), shopCode, JSON.toJSONString(dailyGoodsStockList));
        } finally {
            //解锁相关商品库存
            stockService.unlockMultiDayStock(dailyGoodsStockList);
        }
    }

    private void updateRedPacketStatus(Order order) {
        if (order.getRedPacketId() == null) {
            log.info("----退款红包校验，订单未使用红包，订单ID:{}", order.getId());
            return;
        }
        BigDecimal totalRefundAmount = refundOrderMapper.getOrderTotalRefundAmount(order.getId());
        log.info("----退款红包校验，订单ID：{}，红包ID：{}，退款金额：{}，订单金额：{}", order.getId(), order.getRedPacketId(), totalRefundAmount, order.getTotalPayAmount());
        if (order.getTotalPayAmount().equals(totalRefundAmount)) {
            log.info("----退款红包校验，将红包释放，红包ID：{}", order.getRedPacketId());
            userRedPacketService.updateUserRedPacketUseful(order.getRedPacketId());
        }
    }

    private void updateCouponStatus(Order order) {
        if (order.getCouponId() == null) {
            log.info("----退款优惠券校验，订单未使用优惠券，订单ID:{}", order.getId());
            return;
        }
        BigDecimal totalRefundAmount = refundOrderMapper.getOrderTotalRefundAmount(order.getId());
        log.info("----退款优惠券校验，订单ID：{}，优惠券ID：{}，退款金额：{}，订单金额：{}", order.getId(), order.getCouponId(), totalRefundAmount, order.getTotalPayAmount());
        if (order.getTotalPayAmount().equals(totalRefundAmount)) {
            log.info("----退款优惠券校验，将优惠券释放，优惠券ID：{}", order.getCouponId());
            UserCouponQueryParam couponQueryParam = new UserCouponQueryParam();
            couponQueryParam.setUserCouponId(order.getCouponId());
            breakfastCouponService.updateUserCouponUseful(couponQueryParam);
        }
    }

    private void updateOrderStatus(Order order, RefundOrder refundOrder) {
        List<OrderDelivery> orderDeliveries = orderDeliveryMapper.findUncompletedDeliveriesByOrderId(order.getId());
        if (orderDeliveries.size() == 0) {
            order.setStatus(OrderStatus.COMPLETED.getValue());
            orderMapper.updateByPrimaryKeySelective(order);
        }


    }
}
