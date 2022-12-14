package com.milisong.oms.service.groupbuy;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.exception.BusinessException;
import com.farmland.core.util.BeanMapper;
import com.google.common.collect.Lists;
import com.milisong.oms.api.GroupBuyOrderService;
import com.milisong.oms.api.StockService;
import com.milisong.oms.configruation.SystemProperties;
import com.milisong.oms.constant.DeliveryStatus;
import com.milisong.oms.constant.OrderResponseCode;
import com.milisong.oms.constant.OrderStatus;
import com.milisong.oms.domain.*;
import com.milisong.oms.dto.OrderDto;
import com.milisong.oms.dto.WechatMiniPayDto;
import com.milisong.oms.mapper.GroupBuyOrderDeliveryGoodsMapper;
import com.milisong.oms.mapper.GroupBuyOrderDeliveryMapper;
import com.milisong.oms.mapper.GroupBuyOrderMapper;
import com.milisong.oms.param.GroupBuyOrderDeliveryGoodsParam;
import com.milisong.oms.param.GroupBuyOrderDeliveryParam;
import com.milisong.oms.param.GroupBuyOrderParam;
import com.milisong.oms.service.DelayQueueHelper;
import com.milisong.oms.service.OrderPaymentHelper;
import com.milisong.oms.util.DateTimeUtils;
import com.milisong.oms.util.OrderDtoBuilder;
import com.milisong.oms.util.OrderNoBuilder;
import com.milisong.pms.prom.api.GroupBuyService;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyRequest;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyResponse;
import com.milisong.wechat.miniapp.api.MiniAppService;
import com.milisong.wechat.miniapp.dto.PayUnifiedOrderDto;
import com.milisong.wechat.miniapp.dto.PayUnifiedOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description: ??????????????????
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/20 14:28
 */
@Slf4j
@RestController
public class GroupBuyOrderServiceImpl implements GroupBuyOrderService {

    @Resource
    private StockService stockService;
    @Resource
    private GroupBuyOrderMapper groupBuyOrderMapper;
    @Resource
    private GroupBuyOrderDeliveryMapper groupBuyOrderDeliveryMapper;
    @Resource
    private GroupBuyOrderDeliveryGoodsMapper groupBuyOrderDeliveryGoodsMapper;
    @Resource
    private MiniAppService miniAppService;
    @Resource
    private DelayQueueHelper delayQueueHelper;
    @Resource
    private OrderPaymentHelper orderPaymentHelper;
    @Resource
    private SystemProperties systemProperties;
    @Resource
    private GroupBuyService groupBuyService;
    @Resource
    private GroupBuyOrderInternalService groupBuyOrderInternalService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public ResponseResult<WechatMiniPayDto> order(@RequestBody GroupBuyOrderParam groupBuyOrderParam) {
        log.info("--------------??????????????????????????????{}", JSON.toJSONString(groupBuyOrderParam));
        String shopCode = groupBuyOrderParam.getShopCode();
        //????????????????????????
        GroupBuyOrderDeliveryParam groupBuyOrderDeliveryParam = groupBuyOrderParam.getDeliveries().get(0);
        GroupBuyOrderDeliveryGoodsParam groupBuyOrderDeliveryGoodsParam = groupBuyOrderDeliveryParam.getDeliveryGoods().get(0);
        BigDecimal totalPayAmount = groupBuyOrderDeliveryGoodsParam.getGoodsActualPrice();
        //??????????????????
        List<StockService.ShopDailyStock> shopDailyStockList = DailyStocksBuilder.buildShopDailyStocks(shopCode, groupBuyOrderDeliveryParam, groupBuyOrderDeliveryGoodsParam);

        stockService.lockMultiDayStock(shopDailyStockList);
        try {
            //????????????
            verifyStock(shopDailyStockList);
            final Long orderId = IdGenerator.nextId();
            //???????????????????????????????????????????????????
            PayUnifiedOrderDto payUnifiedOrderDto = invokeWechatPayUnified(groupBuyOrderParam, totalPayAmount, orderId);
            //????????????
            GroupBuyOrder groupBuyOrder = buildGroupBuyOrder(groupBuyOrderParam, groupBuyOrderDeliveryParam, groupBuyOrderDeliveryGoodsParam, totalPayAmount, orderId);
            final Long deliveryId = IdGenerator.nextId();
            //???????????????
            final GroupBuyOrderDelivery groupBuyOrderDelivery = buildGroupBuyOrderDelivery(groupBuyOrderParam.getOrderType(), groupBuyOrderDeliveryParam, groupBuyOrderDeliveryGoodsParam, totalPayAmount, orderId, groupBuyOrder, deliveryId);
            //????????????????????????
            List<GroupBuyOrderDeliveryGoods> groupBuyOrderDeliveryGoodsList = buildGroupBuyOrderDeliveryGoods(groupBuyOrderDeliveryGoodsParam);
            //??????PMS????????????
            Long userGroupBuyId = invokePmsService(groupBuyOrderParam, orderId);
            //??????????????????
            persistentOrder(groupBuyOrder, groupBuyOrderDelivery, groupBuyOrderDeliveryGoodsList, userGroupBuyId);
            //????????????????????????
            delayQueueHelper.pushGroupBuy(orderId, groupBuyOrder.getOrderDate());
            //????????????
            deductionStock(shopDailyStockList, orderId, deliveryId);
            //????????????????????????????????????
            return ResponseResult.buildSuccessResponse(buildWechatMiniPayDto(payUnifiedOrderDto, orderId));
        } finally {
            stockService.unlockMultiDayStock(shopDailyStockList);
        }
    }

    private void deductionStock(List<StockService.ShopDailyStock> shopDailyStockList, Long orderId, Long deliveryId) {
        StockService.ShopDailyStock shopDailyStock=shopDailyStockList.get(0);
        shopDailyStock.setOrderId(orderId);
        shopDailyStock.setDeliveryId(deliveryId);
        stockService.decrementMultiDayStock(shopDailyStockList);
    }

    @Override
    public ResponseResult<OrderDto> getOrderById(Long orderId) {

        GroupBuyOrder groupBuyOrder = groupBuyOrderMapper.selectByPrimaryKey(orderId);
        List<GroupBuyOrderDelivery> groupBuyOrderDeliveryList = groupBuyOrderDeliveryMapper.findDeliveryListByOrderId(groupBuyOrder.getId());
        final Set<Long> ids = groupBuyOrderDeliveryList.stream().map(GroupBuyOrderDelivery::getId).collect(Collectors.toSet());
        List<GroupBuyOrderDeliveryGoods> groupBuyOrderDeliveryGoodsList = groupBuyOrderDeliveryGoodsMapper.batchFindByDeliveryIds(ids);

        Order order = BeanMapper.map(groupBuyOrder, Order.class);
        List<OrderDelivery> deliveries = BeanMapper.mapList(groupBuyOrderDeliveryList, OrderDelivery.class);
        List<OrderDeliveryGoods> deliveryGoods = BeanMapper.mapList(groupBuyOrderDeliveryGoodsList, OrderDeliveryGoods.class);

        return ResponseResult.buildSuccessResponse(OrderDtoBuilder.buildOrderDto(order, deliveries, deliveryGoods));

    }

    @Override
    public ResponseResult<?> cancelOrder(Long orderId) {
        groupBuyOrderInternalService.expiredCancelOrder(orderId);
        return ResponseResult.buildSuccessResponse();
    }

    private void verifyStock(List<StockService.ShopDailyStock> shopDailyStockList) {
        Map<String, List<String>> verifyResult = stockService.verifyMultiDayStock(shopDailyStockList);
        log.info("------????????????--------?????????????????????{}", JSON.toJSONString(verifyResult));
        if (MapUtils.isNotEmpty(verifyResult)) {
            throw BusinessException.build(OrderResponseCode.V_ORDER_CHECK_NO_STOCK.getCode(), OrderResponseCode.V_ORDER_CHECK_NO_STOCK.getMessage());
        }
    }

    private PayUnifiedOrderDto invokeWechatPayUnified(@RequestBody GroupBuyOrderParam groupBuyOrderParam, BigDecimal totalPayAmount, Long orderId) {
        //??????????????????
        PayUnifiedOrderRequest unifiedOrderRequest = orderPaymentHelper.buildPayUnifiedOrderRequest(groupBuyOrderParam.getOpenId(), groupBuyOrderParam.getOrderIp(), orderId, totalPayAmount, groupBuyOrderParam.getOrderType(), groupBuyOrderParam.getOrderSource());
        //FIXME ??????
        unifiedOrderRequest.setNotifyURL(systemProperties.getWechatPay().getGroupBuyPayCallbackUrl());
        log.info("----------?????????????????????{}", JSON.toJSONString(unifiedOrderRequest));
        ResponseResult<PayUnifiedOrderDto> responseResult = miniAppService.unifiedOrder(unifiedOrderRequest);

        log.info("----------???????????????????????????{}", JSON.toJSONString(responseResult));
        if (!responseResult.isSuccess()) {
            String errorCode = responseResult.getCode();
            String errCodeDes = responseResult.getMessage();
            throw BusinessException.build(errorCode, errCodeDes);
        }
        return responseResult.getData();
    }

    private void persistentOrder(GroupBuyOrder groupBuyOrder, GroupBuyOrderDelivery groupBuyOrderDelivery, List<GroupBuyOrderDeliveryGoods> groupBuyOrderDeliveryGoodsList, Long userGroupBuyId) {
        groupBuyOrder.setGroupBuyId(userGroupBuyId);
        groupBuyOrderMapper.insert(groupBuyOrder);
        log.info("------??????------?????????????????????{}", JSON.toJSONString(groupBuyOrder));
        groupBuyOrderDeliveryMapper.insert(groupBuyOrderDelivery);
        log.info("------??????------????????????????????????{}", JSON.toJSONString(groupBuyOrderDelivery));
        if (CollectionUtils.isNotEmpty(groupBuyOrderDeliveryGoodsList)) {
            groupBuyOrderDeliveryGoodsList.forEach(goods -> {
                goods.setId(IdGenerator.nextId());
                goods.setDeliveryId(groupBuyOrderDelivery.getId());
                groupBuyOrderDeliveryGoodsMapper.insert(goods);
            });
        }
        log.info("------??????------??????????????????????????????{}", JSON.toJSONString(groupBuyOrderDeliveryGoodsList));
    }

    private List<GroupBuyOrderDeliveryGoods> buildGroupBuyOrderDeliveryGoods(GroupBuyOrderDeliveryGoodsParam groupBuyOrderDeliveryGoodsParam) {
        List<GroupBuyOrderDeliveryGoods> groupBuyOrderDeliveryGoodsList = Lists.newArrayList();
        GroupBuyOrderDeliveryGoods groupBuyOrderDeliveryGoods = BeanMapper.map(groupBuyOrderDeliveryGoodsParam, GroupBuyOrderDeliveryGoods.class);
        groupBuyOrderDeliveryGoods.setTotalAmount(groupBuyOrderDeliveryGoodsParam.getGoodsActualPrice());
        groupBuyOrderDeliveryGoodsList.add(groupBuyOrderDeliveryGoods);
        if (BooleanUtils.isTrue(groupBuyOrderDeliveryGoodsParam.getIsCombo())) {
            groupBuyOrderDeliveryGoodsList.addAll(BeanMapper.mapList(groupBuyOrderDeliveryGoodsParam.getDeliveryGoodsParamList(), GroupBuyOrderDeliveryGoods.class));
        }
        return groupBuyOrderDeliveryGoodsList;
    }

    private GroupBuyOrderDelivery buildGroupBuyOrderDelivery(Byte orderType, GroupBuyOrderDeliveryParam groupBuyOrderDeliveryParam, GroupBuyOrderDeliveryGoodsParam groupBuyOrderDeliveryGoodsParam, BigDecimal totalPayAmount, Long orderId, GroupBuyOrder groupBuyOrder, Long deliveryId) {
        final GroupBuyOrderDelivery groupBuyOrderDelivery = BeanMapper.map(groupBuyOrderDeliveryParam, GroupBuyOrderDelivery.class);
        groupBuyOrderDelivery.setId(deliveryId);
        groupBuyOrderDelivery.setOrderId(orderId);
        groupBuyOrderDelivery.setDeliveryNo(groupBuyOrder.getOrderNo() + "_" + String.format("%02d", 1));
        groupBuyOrderDelivery.setStatus(DeliveryStatus.GETTING_READY.getValue());
        groupBuyOrderDelivery.setShareOrderPayAmount(totalPayAmount);
        groupBuyOrderDelivery.setDeliveryTimezoneFrom(DateTimeUtils.mergeDateAndTimeString(groupBuyOrderDelivery.getDeliveryDate(), groupBuyOrderDeliveryParam.getStartTime()));
        groupBuyOrderDelivery.setDeliveryTimezoneTo(DateTimeUtils.mergeDateAndTimeString(groupBuyOrderDelivery.getDeliveryDate(), groupBuyOrderDeliveryParam.getEndTime()));
        groupBuyOrderDelivery.setTotalPackageActualAmount(groupBuyOrderDeliveryGoodsParam.getPackageActualPrice());
        groupBuyOrderDelivery.setTotalPackageOriginalAmount(groupBuyOrderDeliveryGoodsParam.getPackageOriginalPrice());
        groupBuyOrderDelivery.setTotalGoodsCount(1);
        groupBuyOrderDelivery.setTotalGoodsOriginalAmount(groupBuyOrderDeliveryGoodsParam.getGoodsOriginalPrice());
        groupBuyOrderDelivery.setTotalGoodsActualAmount(totalPayAmount);
        groupBuyOrderDelivery.setTotalAmount(totalPayAmount);
        groupBuyOrderDelivery.setOrderType(orderType);
        return groupBuyOrderDelivery;
    }

    private GroupBuyOrder buildGroupBuyOrder(@RequestBody GroupBuyOrderParam groupBuyOrderParam, GroupBuyOrderDeliveryParam groupBuyOrderDeliveryParam, GroupBuyOrderDeliveryGoodsParam groupBuyOrderDeliveryGoodsParam, BigDecimal totalPayAmount, Long orderId) {
        String orderNo = OrderNoBuilder.build(groupBuyOrderParam.getOrderDate(), groupBuyOrderParam.getOrderType(), groupBuyOrderParam.getShopCode());
        GroupBuyOrder groupBuyOrder = BeanMapper.map(groupBuyOrderParam, GroupBuyOrder.class);
        groupBuyOrder.setId(orderId);
        groupBuyOrder.setOrderNo(orderNo);
        groupBuyOrder.setStatus(OrderStatus.UN_PAID.getValue());
        groupBuyOrder.setTotalPayAmount(totalPayAmount);
        groupBuyOrder.setTotalDeliveryActualAmount(groupBuyOrderDeliveryParam.getDeliveryActualPrice());
        groupBuyOrder.setTotalDeliveryOriginalAmount(groupBuyOrderDeliveryParam.getDeliveryOriginalPrice());
        groupBuyOrder.setTotalPackageActualAmount(groupBuyOrderDeliveryGoodsParam.getPackageActualPrice());
        groupBuyOrder.setTotalPackageOriginalAmount(groupBuyOrderDeliveryGoodsParam.getPackageOriginalPrice());
        groupBuyOrder.setTotalGoodsOriginalAmount(groupBuyOrderDeliveryGoodsParam.getGoodsOriginalPrice());
        groupBuyOrder.setTotalGoodsActualAmount(totalPayAmount);
        groupBuyOrder.setTotalGoodsCount(1);
        groupBuyOrder.setTotalOrderDays(1);
        return groupBuyOrder;
    }

    private Long invokePmsService(@RequestBody GroupBuyOrderParam groupBuyOrderParam, long orderId) {
        GroupBuyRequest groupBuyRequest = buildGroupBuyRequest(groupBuyOrderParam, orderId);
        ResponseResult<GroupBuyResponse> result = null;
        if (groupBuyOrderParam.getGroupBuyId() != null) {
            groupBuyRequest.setUserGroupBuyId(groupBuyOrderParam.getGroupBuyId());
            log.info("-------------????????????----{}", JSON.toJSONString(groupBuyRequest));
            result = groupBuyService.joinGroupBuyForPay(groupBuyRequest);
            log.info("-------------??????????????????----{}", JSON.toJSONString(result));

        } else {
            log.info("-------------??????????????????----{}", JSON.toJSONString(groupBuyRequest));
            result = groupBuyService.createGroupBuyForPay(groupBuyRequest);
            log.info("-------------????????????????????????----{}", JSON.toJSONString(result));
        }
        if (!result.isSuccess()) {
            throw BusinessException.build(result.getCode(), result.getMessage());
        }
        if (result.getData() == null || result.getData().getUserGroupBuyId() == null) {
            throw BusinessException.build("", "????????????????????????????????????????????????");
        }
        return result.getData().getUserGroupBuyId();
    }

    private GroupBuyRequest buildGroupBuyRequest(@RequestBody GroupBuyOrderParam groupBuyOrderParam, Long orderId) {
        GroupBuyRequest groupBuyRequest = new GroupBuyRequest();
        groupBuyRequest.setCompanyId(groupBuyOrderParam.getDeliveryBuildingId());
        groupBuyRequest.setNickName(groupBuyOrderParam.getNickName());
        groupBuyRequest.setHeadPortraitUrl(groupBuyOrderParam.getHeadPortraitUrl());
        groupBuyRequest.setOpenId(groupBuyOrderParam.getOpenId());
        groupBuyRequest.setShopCode(groupBuyOrderParam.getShopCode());
        groupBuyRequest.setUserId(groupBuyOrderParam.getUserId());
        groupBuyRequest.setOrderId(orderId);
        groupBuyRequest.setCompanyId(groupBuyOrderParam.getDeliveryBuildingId());
        groupBuyRequest.setActivityGroupBuyId(groupBuyOrderParam.getGroupBuyActivityId());
        return groupBuyRequest;
    }

    private WechatMiniPayDto buildWechatMiniPayDto(PayUnifiedOrderDto payUnifiedOrderDto, Long orderId) {
        WechatMiniPayDto wechatMiniPayDto = new WechatMiniPayDto();
        wechatMiniPayDto.setTimeStamp(payUnifiedOrderDto.getTimeStamp());
        wechatMiniPayDto.setSignType("MD5");
        wechatMiniPayDto.setNonceStr(payUnifiedOrderDto.getNonceStr());
        wechatMiniPayDto.setDataPackage("prepay_id=" + payUnifiedOrderDto.getPrepayId());
        wechatMiniPayDto.setPaySign(payUnifiedOrderDto.getSign());
        wechatMiniPayDto.setOrderId(orderId);
        return wechatMiniPayDto;
    }


}
