package com.milisong.oms.service.groupbuy;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.milisong.oms.api.StockService;
import com.milisong.oms.configruation.SystemProperties;
import com.milisong.oms.constant.*;
import com.milisong.oms.domain.*;
import com.milisong.oms.mapper.*;
import com.milisong.oms.mq.CancelMessage;
import com.milisong.oms.mq.MessageProducer;
import com.milisong.oms.mq.PaymentMessage;
import com.milisong.oms.param.PaymentResultParam;
import com.milisong.oms.service.DelayQueueHelper;
import com.milisong.oms.util.OrderRedisKeyUtils;
import com.milisong.oms.util.XMLUtil;
import com.milisong.pms.prom.api.GroupBuyService;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyPayRequest;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyRequest;
import com.milisong.wechat.miniapp.api.MiniAppService;
import com.milisong.wechat.miniapp.dto.MiniPayOrderCloseRequest;
import com.milisong.wechat.miniapp.dto.MiniPayOrderCloseResult;
import com.milisong.wechat.miniapp.dto.MiniPayRefundRequest;
import com.milisong.wechat.miniapp.dto.MiniPayRefundResult;
import com.milisong.wechat.miniapp.util.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.concurrent.locks.Lock;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/20 19:42
 */
@Slf4j
@Component
@Transactional(rollbackFor = RuntimeException.class)
public class GroupBuyOrderInternalServiceImpl implements GroupBuyOrderInternalService {

    @Resource
    private StockService stockService;
    @Resource
    private MiniAppService miniAppService;
    @Resource
    private GroupBuyOrderMapper groupBuyOrderMapper;
    @Resource
    private GroupBuyOrderDeliveryMapper groupBuyOrderDeliveryMapper;
    @Resource
    private GroupBuyOrderDeliveryGoodsMapper groupBuyOrderDeliveryGoodsMapper;
    @Resource
    private MessageProducer messageProducer;
    @Resource
    private RefundOrderMapper refundOrderMapper;
    @Resource
    private RefundOrderDetailMapper refundOrderDetailMapper;
    @Resource
    private RefundWaterMapper refundWaterMapper;
    @Resource
    private DelayQueueHelper delayQueueHelper;
    @Resource
    private GroupBuyService groupBuyService;
    @Resource
    private RealOrderBuilder realOrderBuilder;
    @Resource
    private SystemProperties systemProperties;


    @Override
    public void refundOrder(GroupBuyOrder order) {
        log.info("------------????????????????????????{}", JSON.toJSONString(order));
        //????????????????????????????????????????????????
        Long refundId = IdGenerator.nextId();
        RefundOrder refundOrder = buildBaseRefundOrder(order, refundId);
        BigDecimal totalPayAmount = order.getTotalPayAmount();

        int goodsCount = 0;

        List<GroupBuyOrderDelivery> deliveryList = groupBuyOrderDeliveryMapper.findDeliveryListByOrderId(order.getId());
        int deliveryCount = deliveryList.size();

        BigDecimal refundTotalAmount = order.getTotalPayAmount();

        List<RefundOrderDetail> refundOrderDetails = new ArrayList<>();
        for (GroupBuyOrderDelivery orderDelivery : deliveryList) {
            Integer deliveryGoodsCount = orderDelivery.getTotalGoodsCount();
            RefundOrderDetail refundOrderDetail = buildRefundOrderDetail(order, refundId, orderDelivery, deliveryGoodsCount);
            goodsCount += deliveryGoodsCount;

            refundOrderDetails.add(refundOrderDetail);
        }

        refundOrder.setGoodsCount(goodsCount);
        refundOrder.setDeliveryCount(deliveryCount);
        refundOrder.setTotalRefundAmount(refundTotalAmount);
        refundOrder.setApplyTime(new Date());
        refundOrderMapper.insert(refundOrder);
        refundOrderDetailMapper.batchSave(refundOrderDetails);
        deliveryList.forEach(this::updateOrderDeliveryStatus);
        order.setStatus(OrderStatus.REFUNDING.getValue());
        groupBuyOrderMapper.updateByPrimaryKeySelective(order);

        ResponseResult<?> responseResult = wechatMiniAppRefund(order, refundId, totalPayAmount, refundTotalAmount);
        if (!responseResult.isSuccess()) {
            return;
        }
        log.info("========================???????????????{}", JSON.toJSONString(order));

        //????????????????????????????????????
        rollbackStock(refundOrder);
        //?????????????????????
//        GroupBuyOrderDelivery groupBuyOrderDelivery = deliveryList.get(0);
//        List<GroupBuyOrderDeliveryGoods> orderDeliveryGoods = groupBuyOrderDeliveryGoodsMapper.findGoodsListByDeliveryId(groupBuyOrderDelivery.getId());
//        rollbackMonthlySales(orderDeliveryGoods);
    }

//    private void rollbackMonthlySales(List<GroupBuyOrderDeliveryGoods> deliveryGoods) {
//        //?????????????????????
//        if (deliveryGoods != null) {
//            deliveryGoods.forEach(goods -> {
//                String goodsCode = goods.getGoodsCode();
//                Integer goodsCount = goods.getGoodsCount();
//                String lately30DaysSalesKey = OrderRedisKeyUtils.getLately30DaysSalesKey(goodsCode);
//                RedisCache.incrBy(lately30DaysSalesKey, -new Long(goodsCount));
//            });
//        }
//        log.info("-------------------????????????????????????????????????{}", JSON.toJSONString(deliveryGoods));
//    }

    @Override
    public void refundCallback(SortedMap<String, String> map) {
        try {
            // ????????????
            String resultCode = map.get("returnCode");
            String returnMsg = map.get("returnMsg");
            // ????????????
            String reqInfo = map.get("reqInfo");
            String decryptReqInfo = AESUtil.decryptData(reqInfo);
            log.info("???????????????????????????????????????????????? -> {}", decryptReqInfo);
            SortedMap<String, String> reqInfoObj = XMLUtil.doXMLParse(decryptReqInfo);

            String outTradeNo = reqInfoObj.get("outTradeNo");
            String outRefundNo = reqInfoObj.get("outRefundNo");
            String refundFee = reqInfoObj.get("refundFee");
            BigDecimal refundAmount = new BigDecimal(refundFee);
            refundAmount = refundAmount.divide(BigDecimal.valueOf(100));

            Long orderId = Long.valueOf(outTradeNo);
            Long refundId = Long.valueOf(outRefundNo);

            GroupBuyOrder order = groupBuyOrderMapper.selectByPrimaryKey(orderId);
            RefundOrder refundOrder = refundOrderMapper.selectByPrimaryKey(refundId);
            if (order == null) {
                log.error("????????????ID:{}?????????????????????????????????", orderId);
                return;
            }
            if (refundOrder == null) {
                log.error("???????????????ID:{}??????????????????????????????", refundId);
                return;
            }

            RefundWater refundWater = buildRefundWater(order, refundAmount);
            refundWater.setRefundId(refundId);
            if ("SUCCESS".equals(resultCode)) { //??????
                refundWater.setStatus(RefundStatus.SUCCEED.getValue());
                refundOrder.setStatus(RefundOrderStatus.SUCCEED.getValue());
                refundOrder.setRefundTime(new Date());
                log.info("outTradeNo -> {}, outRefundNo -> {}", outTradeNo, outRefundNo);
                List<RefundOrderDetail> refundOrderDetailList = refundOrderDetailMapper.getListByRefundId(refundId);
                log.info("-------????????????----???????????????{}", JSON.toJSONString(refundOrderDetailList));
                refundOrderDetailList.forEach(refundOrderDetail -> {
                    GroupBuyOrderDelivery orderDelivery = new GroupBuyOrderDelivery();
                    orderDelivery.setId(refundOrderDetail.getDeliveryId());
                    orderDelivery.setStatus(DeliveryStatus.REFUNDED.getValue());
                    orderDelivery.setRefundTime(new Date());
                    groupBuyOrderDeliveryMapper.updateByPrimaryKeySelective(orderDelivery);
                });

                refundWaterMapper.insert(refundWater);
                refundOrderMapper.updateByPrimaryKeySelective(refundOrder);
                //?????????????????????????????????
                order.setStatus(OrderStatus.REFUNDED.getValue());
                order.setCancelTime(new Date());
                groupBuyOrderMapper.updateByPrimaryKeySelective(order);

            } else { //??????
                String errorCode = map.get("errorCode");
                String errCodeDes = map.get("errCodeDes");
                refundWater.setErrorCode(errorCode);
                refundWater.setErrorDescription(errCodeDes);
                refundWater.setStatus(RefundStatus.FAILED.getValue());

                refundOrder.setStatus(RefundOrderStatus.FAILED.getValue());
                refundOrder.setRefundTime(new Date());

                refundWaterMapper.insert(refundWater);
                refundOrderMapper.updateByPrimaryKeySelective(refundOrder);
                log.error("????????????,?????????ID:{}??? ?????? -> {}", refundId, returnMsg);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void payCallback(PaymentResultParam paymentResultParam) {
        GroupBuyOrder order = groupBuyOrderMapper.selectByPrimaryKey(paymentResultParam.getOrderId());

        if (order == null) {
            log.warn("~~~~~~~?????????????????????~~~~~??????ID:{}~~~~~~~", paymentResultParam.getOrderId());
            return;
        }

        if (OrderStatus.PAID.getValue() == order.getStatus()) {
            log.warn("~~~~~~~????????????????????????~~~~~~??????ID:{}~~~~~~~", order.getId());
            return;
        }

        if (OrderStatus.CANCELED.getValue() == order.getStatus()) {
            log.warn("~~~~~~~????????????????????????~~~~~~??????ID:{}~~~~~~~", order.getId());
            return;
        }

        if (OrderStatus.COMPLETED.getValue() == order.getStatus()) {
            log.warn("~~~~~~~????????????????????????~~~~~~??????ID:{}~~~~~~~", order.getId());
            return;
        }

        PaymentMessage paymentMessage = BeanMapper.map(order, PaymentMessage.class);
        paymentMessage.setOrderMode(OrderMode.GROUP_BUY.getValue());
        paymentMessage.setOrderId(order.getId());
        paymentMessage.setAmount(order.getTotalPayAmount());
        paymentMessage.setMessageId(IdGenerator.nextId());
        if (BooleanUtils.isTrue(paymentResultParam.getIsSuccess())) {
            paymentMessage.setStatus(PaymentStatus.SUCCEED.getValue());
            paymentMessage.setPaySuccess(true);
            //????????????????????????
            order.setStatus(OrderStatus.PAID.getValue());
            order.setPayTime(new Date());
            groupBuyOrderMapper.updateByPrimaryKeySelective(order);
            delayQueueHelper.removeGroupBuy(order.getId());

            GroupBuyPayRequest groupBuyPayRequest = new GroupBuyPayRequest();
            groupBuyPayRequest.setOrderId(order.getId());
            ResponseResult<Boolean> result = groupBuyService.groupBuyPaySuccess(groupBuyPayRequest);
            log.info("----------?????????????????????????????????????????????????????????????????????{}", JSON.toJSONString(result));
            if (result.isSuccess() && BooleanUtils.isTrue(result.getData())) {
                //?????????????????????????????????
                realOrderBuilder.createRealOrder(order.getGroupBuyId());
            }
        } else {
            paymentMessage.setErrorCode(paymentResultParam.getErrorCode());
            paymentMessage.setErrorDescription(paymentResultParam.getErrCodeDes());
            paymentMessage.setStatus(PaymentStatus.FAILED.getValue());
            paymentMessage.setPaySuccess(false);
        }
        messageProducer.send(paymentMessage);
        log.info("--------------------??????MQ??????????????????!");
    }


    private void rollbackStock(RefundOrder refundOrder) {
        log.info("????????????????????????????????????{}", JSON.toJSONString(refundOrder));
        long groupBuyOrderId = refundOrder.getOrderId();
        GroupBuyOrder groupBuyOrder = groupBuyOrderMapper.selectByPrimaryKey(groupBuyOrderId);
        rollbackStock(groupBuyOrder);
    }

    private void updateOrderDeliveryStatus(GroupBuyOrderDelivery orderDelivery) {
        orderDelivery.setStatus(DeliveryStatus.CANCELED.getValue());
        orderDelivery.setCancelTime(new Date());
        groupBuyOrderDeliveryMapper.updateByPrimaryKeySelective(orderDelivery);
    }

    private RefundOrderDetail buildRefundOrderDetail(GroupBuyOrder order, Long refundId, GroupBuyOrderDelivery orderDelivery, Integer deliveryGoodsCount) {
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

    private RefundOrder buildBaseRefundOrder(GroupBuyOrder order, Long refundId) {
        RefundOrder refundOrder = BeanMapper.map(order, RefundOrder.class);
        refundOrder.setId(refundId);
        refundOrder.setStatus(RefundOrderStatus.RDFUNDING.getValue());
        refundOrder.setOrderId(order.getId());
        refundOrder.setOrderMode(OrderMode.GROUP_BUY.getValue());
        return refundOrder;
    }

    private ResponseResult<?> wechatMiniAppRefund(GroupBuyOrder order, Long refundId, BigDecimal totalPayAmount, BigDecimal refundTotalAmount) {
        MiniPayRefundRequest miniPayRefundRequest = new MiniPayRefundRequest();
        //outTradeNo ????????????outRefundNo ???????????????totalAmount ??????????????????refundAmount ????????????
        miniPayRefundRequest.setOutTradeNo(String.valueOf(order.getId()));
        miniPayRefundRequest.setOutRefundNo(String.valueOf(refundId));
        miniPayRefundRequest.setTotalAmount(totalPayAmount);
        miniPayRefundRequest.setRefundAmount(refundTotalAmount);
        miniPayRefundRequest.setBusinessLine(order.getOrderType());
        miniPayRefundRequest.setNotifyUrl(systemProperties.getWechatPay().getGroupBuyRefundCallbackUrl());
        log.info("????????????????????????????????????{}", JSON.toJSONString(miniPayRefundRequest));
        ResponseResult<MiniPayRefundResult> responseResult = miniAppService.refund(miniPayRefundRequest);
        log.info("????????????????????????,???????????????{}", JSON.toJSONString(responseResult));
        if (!responseResult.isSuccess()) {
            MiniPayRefundResult result = responseResult.getData();
            saveRefundWaterAndOrderStatus(order, refundTotalAmount, result);
            return ResponseResult.buildFailResponse(result.getErrCode(), result.getErrCodeDes());
        }
        return responseResult;
    }

    private void saveRefundWaterAndOrderStatus(GroupBuyOrder order, BigDecimal refundTotalAmount, MiniPayRefundResult result) {
        RefundWater refundWater = buildRefundWater(order, refundTotalAmount);
        if (result != null) {
            refundWater.setErrorCode(result.getErrCode());
            refundWater.setErrorDescription(result.getErrCodeDes());
        }
        refundWater.setStatus(RefundStatus.FAILED.getValue());
        refundWaterMapper.insert(refundWater);
    }

    private RefundWater buildRefundWater(GroupBuyOrder order, BigDecimal refundTotalAmount) {
        RefundWater refundWater =  BeanMapper.map(order,RefundWater.class);;
        refundWater.setId(IdGenerator.nextId());
        refundWater.setOrderId(order.getId());
        refundWater.setOrderMode(OrderMode.GROUP_BUY.getValue());
        refundWater.setUserId(order.getUserId());
        refundWater.setOrderType(order.getOrderType());
        refundWater.setAmount(refundTotalAmount);
        return refundWater;
    }


    private void sendCancelOrderMessage(GroupBuyOrder order) {
        CancelMessage cancelMessage = new CancelMessage();
        cancelMessage.setOrderId(order.getId());
        cancelMessage.setShopCode(order.getShopCode());
        cancelMessage.setOrderType(order.getOrderType());
        cancelMessage.setUserId(order.getUserId());
        cancelMessage.setDeductionPoints(order.getDeductionPoints());
        messageProducer.send(cancelMessage);
        log.info("-------------??????????????????MQ?????????{}", cancelMessage);
    }

    private void rollbackStock(GroupBuyOrder order) {
        log.info("????????????????????????????????????????????????{}", JSON.toJSONString(order));
        long orderId = order.getId();
        String shopCode = order.getShopCode();

        List<GroupBuyOrderDelivery> deliveryList = groupBuyOrderDeliveryMapper.findDeliveryListByOrderId(orderId);
        if (CollectionUtils.isEmpty(deliveryList)) {
            log.warn("---------------????????????ID???{}???????????????????????????", orderId);
            return;
        }
        GroupBuyOrderDelivery groupBuyOrderDelivery = deliveryList.get(0);
        List<GroupBuyOrderDeliveryGoods> goodsList = groupBuyOrderDeliveryGoodsMapper.findGoodsListByDeliveryId(groupBuyOrderDelivery.getId());
        if (CollectionUtils.isEmpty(goodsList)) {
            log.warn("---------------???????????????ID???{}????????????????????????", groupBuyOrderDelivery.getId());
            return;
        }
        List<StockService.ShopDailyStock> shopDailyStockList = DailyStocksBuilder.buildShopDailyStocks(shopCode, groupBuyOrderDelivery, goodsList);
        stockService.lockMultiDayStock(shopDailyStockList);
        try {
            order.setStatus(OrderStatus.CANCELED.getValue());
            order.setCancelTime(new Date());
            groupBuyOrderMapper.updateByPrimaryKeySelective(order);
            //????????????????????????
            stockService.incrementMultiDayStock(shopDailyStockList);
            log.info("==========??????????????????==============??????ID???{},????????????.......???????????????{}??????????????????{}", orderId, shopCode, JSON.toJSONString(shopDailyStockList));
        } finally {
            //????????????????????????
            stockService.unlockMultiDayStock(shopDailyStockList);
        }
    }

    @Override
    public void expiredCancelOrder(Long orderId) {
        String lockKey = OrderRedisKeyUtils.getCancelVirtualOrderLockKey(orderId);
        Lock lock = LockProvider.getLock(lockKey);
        lock.lock();
        log.info("---------????????????????????????------????????????????????????{}???????????????????????????????????????????????????????????????????????????????????????", orderId);
        try {
            cancelOrder(orderId);
        } finally {
            lock.unlock();
            log.info("---------????????????????????????------????????????????????????{}???????????????????????????????????????????????????????????????????????????????????????", orderId);
        }
    }

    private void cancelOrder(Long orderId) {
        log.info("????????????????????????????????????????????????????????????????????????????????????????????????????????????{}", JSON.toJSONString(orderId));
        GroupBuyOrder order = groupBuyOrderMapper.selectByPrimaryKey(orderId);
        if (order != null) {
            byte orderStatus = order.getStatus();
            log.info("---------??????????????????------???????????????{}", orderStatus);
            if (OrderStatus.PAID.getValue() == orderStatus) {
                log.info("----------?????????????????????????????????????????????????????????");
                return;
            }
            if (OrderStatus.CANCELED.getValue() == orderStatus) {
                log.info("----------?????????????????????????????????????????????????????????");
                return;
            }
            String outTradeNo = String.valueOf(orderId);
            //??????????????????????????????????????????????????????
            MiniPayOrderCloseRequest orderCloseRequest = new MiniPayOrderCloseRequest();
            orderCloseRequest.setOutTradeNo(outTradeNo);
            orderCloseRequest.setBusinessLine(order.getOrderType());
            ResponseResult<MiniPayOrderCloseResult> closeOrderResponseResult = miniAppService.closeOrder(orderCloseRequest);
            log.info("----------?????????????????????????????????????????????????????????????????????{}", JSON.toJSONString(closeOrderResponseResult));
            if (!closeOrderResponseResult.isSuccess()) {
                log.info("----------?????????????????????????????????????????????????????????");
                return;
            }
            order.setStatus(OrderStatus.CANCELED.getValue());
            groupBuyOrderMapper.updateByPrimaryKeySelective(order);

            rollbackStock(order);
            log.info("---------------????????????--????????????????????????------------------");
            groupBuyService.releaseGroupBuyLock(GroupBuyRequest.builder().orderId(order.getId()).build());
            log.info("---------------????????????--??????????????????------------------");

            //??????????????????MQ
            sendCancelOrderMessage(order);
            log.info("---------------????????????--??????????????????????????????------------------");
        }
    }
}
