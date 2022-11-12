package com.milisong.oms.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.exception.BusinessException;
import com.milisong.oms.api.StockService;
import com.milisong.oms.constant.GoodsStockSource;
import com.milisong.oms.constant.OrderResponseCode;
import com.milisong.oms.constant.OrderStatus;
import com.milisong.oms.domain.*;
import com.milisong.oms.dto.WechatMiniPayDto;
import com.milisong.oms.mapper.*;
import com.milisong.oms.mq.CancelMessage;
import com.milisong.oms.mq.MessageProducer;
import com.milisong.oms.param.CancelOrderParam;
import com.milisong.oms.param.DeliveryTimezoneParam;
import com.milisong.oms.param.OrderPaymentParam;
import com.milisong.oms.service.stock.DeliveryCounter;
import com.milisong.oms.service.stock.ShopDailyStockBuilder;
import com.milisong.oms.util.DateTimeUtils;
import com.milisong.wechat.miniapp.api.MiniAppService;
import com.milisong.wechat.miniapp.dto.MiniPayOrderCloseRequest;
import com.milisong.wechat.miniapp.dto.MiniPayOrderCloseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/8 18:22
 */
@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class OrderPayTransactionServiceImpl implements OrderPayTransactionService {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderDeliveryMapper orderDeliveryMapper;
    @Resource
    private OrderDeliveryGoodsMapper orderDeliveryGoodsMapper;
    @Resource
    private VirtualOrderMapper virtualOrderMapper;
    @Resource
    private VirtualOrderDeliveryMapper virtualOrderDeliveryMapper;
    @Resource
    private VirtualOrderDeliveryGoodsMapper virtualOrderDeliveryGoodsMapper;
    @Resource
    private OrderBuilder orderBuilder;
    @Resource
    private OrderPaymentHelper orderPaymentHelper;
    @Resource
    private OrderValidator orderValidator;
    @Resource
    private DeliveryCounter deliveryCounter;
    @Resource
    private StockService stockService;
    @Resource
    private MiniAppService miniAppService;
    @Resource
    private MessageProducer messageProducer;

    @Override
    public ResponseResult<WechatMiniPayDto> payment(OrderPaymentParam orderPaymentParam, Order order) {
        if (order == null) {
            long orderId = orderPaymentParam.getOrderId();
            final VirtualOrder virtualOrder = virtualOrderMapper.selectByPrimaryKey(orderId);
            //校验虚拟订单状态
            orderValidator.verifyVirtualOrderStatus(virtualOrder);
            //定义下单时间
            Date orderDate = orderPaymentParam.getOrderDate();
            final List<VirtualOrderDelivery> virtualOrderDeliveries = virtualOrderDeliveryMapper.findListByOrderId(orderId);
            final Set<Long> ids = virtualOrderDeliveries.stream().map(VirtualOrderDelivery::getId).collect(Collectors.toSet());
            final List<VirtualOrderDeliveryGoods> virtualOrderDeliveryGoods = virtualOrderDeliveryGoodsMapper.batchFindByDeliveryIds(ids);
            order = orderBuilder.createOrder(orderDate, orderPaymentParam, null, virtualOrder, virtualOrderDeliveries, virtualOrderDeliveryGoods);
        } else {
            //校验订单状态
            orderValidator.verifyOrderStatus(order);
        }
        return orderPaymentHelper.payment(order, orderPaymentParam);
    }

    private List<Date> buildDateList(List<VirtualOrderDelivery> virtualOrderDeliveries, Map<Long, DeliveryTimezoneParam> deliveryTimezoneMap) {
        List<Date> dateList = new ArrayList<>();
        virtualOrderDeliveries.forEach(orderDelivery -> {
            DeliveryTimezoneParam timezoneParam = deliveryTimezoneMap.get(orderDelivery.getId());
            String orderDateString = DateFormatUtils.format(orderDelivery.getDeliveryDate(), "MM月dd日");
            if (timezoneParam == null) {
                String messageTpl = OrderResponseCode.ORDER_CHECK_NO_DELIVERY_ALL_DAY.getMessage();
                throw BusinessException.build(OrderResponseCode.ORDER_CHECK_NO_DELIVERY_ALL_DAY.getCode(), String.format(messageTpl, orderDateString));
            }
            Date startDateTime = DateTimeUtils.mergeDateAndTimeString(orderDelivery.getDeliveryDate(), timezoneParam.getStartTime());
            dateList.add(startDateTime);
        });
        return dateList;
    }

    private Map<Long, DeliveryTimezoneParam> getDeliveryTimezoneParamMap(OrderPaymentParam orderPaymentParam, String shopCode, Order order) {
        Map<Long, DeliveryTimezoneParam> deliveryTimezoneMap = null;
        if (order == null) {
            List<DeliveryTimezoneParam> deliveryTimezones = orderPaymentParam.getDeliveryTimezones();
            log.info("================配送时段配置:{}", JSON.toJSONString(deliveryTimezones));
            deliveryTimezoneMap = deliveryTimezones.stream().collect(Collectors.toMap(DeliveryTimezoneParam::getDeliveryId, deliveryTimezone -> deliveryTimezone));
        } else {
            List<OrderDelivery> orderDeliveries = orderDeliveryMapper.findListByOrderId(order.getId());
            deliveryTimezoneMap = orderDeliveries.stream().collect(Collectors.toMap(OrderDelivery::getId, delivery -> {
                DeliveryTimezoneParam timezoneParam = new DeliveryTimezoneParam();
                timezoneParam.setDeliveryId(delivery.getId());
                timezoneParam.setId(delivery.getDeliveryTimezoneId());
                timezoneParam.setShopCode(shopCode);
                return timezoneParam;
            }));
        }
        log.info("==============组装配送段列表：{}", JSON.toJSONString(deliveryTimezoneMap.values()));
        return deliveryTimezoneMap;
    }

    @Override
    public ResponseResult<?> cancelOrder(CancelOrderParam cancelOrderParam) {
        log.info("取消未支付订单，入参：{}", JSON.toJSONString(cancelOrderParam));
        String cancelReason = cancelOrderParam.getCancelReason();
        if (cancelReason != null && cancelReason.length() > 300) {
            throw BusinessException.build("", "订单取消原因长度不能超过300");
        }
        Long orderId = cancelOrderParam.getOrderId();
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order != null) {
            byte orderStatus = order.getStatus();
            log.info("---------取消真实订单------订单状态：{}", orderStatus);
            if (OrderStatus.PAID.getValue() == orderStatus) {
                return ResponseResult.buildFailResponse(OrderResponseCode.ORDER_PAID.getCode(), OrderResponseCode.ORDER_PAID.getMessage());
            }
            if (OrderStatus.CANCELED.getValue() == orderStatus) {
                return ResponseResult.buildFailResponse(OrderResponseCode.ORDER_CANCELED.getCode(), OrderResponseCode.ORDER_CANCELED.getMessage());
            }
            if (OrderStatus.COMPLETED.getValue() == orderStatus) {
                return ResponseResult.buildFailResponse(OrderResponseCode.ORDER_COMPLETED.getCode(), OrderResponseCode.ORDER_COMPLETED.getMessage());
            }
            String outTradeNo = String.valueOf(orderId);
            //通知微信关闭订单，防止用户支付该订单
            MiniPayOrderCloseRequest orderCloseRequest = new MiniPayOrderCloseRequest();
            orderCloseRequest.setOutTradeNo(outTradeNo);
            orderCloseRequest.setBusinessLine(order.getOrderType());
            ResponseResult<MiniPayOrderCloseResult> closeOrderResponseResult = miniAppService.closeOrder(orderCloseRequest);
            log.info("----------取消真实订单，回滚库存后，关闭订单，微信返回结果：{}", JSON.toJSONString(closeOrderResponseResult));
            if (!closeOrderResponseResult.isSuccess()) {
                log.info("----------取消真实订单，微信订单已支付，无法回滚");
                return ResponseResult.buildFailResponse(OrderResponseCode.ORDER_PAID.getCode(), OrderResponseCode.ORDER_PAID.getMessage());
            }
            //FIXME 库存&产能合并控制并发锁
            orderPaymentHelper.unlockRedPacket(order.getRedPacketId());
            orderPaymentHelper.unlockCoupon(order.getCouponId());
            rollbackStock(order, cancelOrderParam.getCancelReason());
            rollbackDeliveryCounter(order);
            //发送订单取消MQ
            sendCancelOrderMessage(order);
        }
        return ResponseResult.buildSuccessResponse();
    }

    private void sendCancelOrderMessage(Order order) {
        CancelMessage cancelMessage = new CancelMessage();
        cancelMessage.setOrderId(order.getId());
        cancelMessage.setShopCode(order.getShopCode());
        cancelMessage.setOrderType(order.getOrderType());
        cancelMessage.setUserId(order.getUserId());
        cancelMessage.setDeductionPoints(order.getDeductionPoints());
        messageProducer.send(cancelMessage);
        log.info("-------------发送取消订单MQ消息：{}", cancelMessage);
    }

    private void rollbackDeliveryCounter(Order order) {
        List<OrderDelivery> orderDeliveryList = orderDeliveryMapper.findListByOrderId(order.getId());
        log.info("回滚产量计数器：{}", JSON.toJSONString(orderDeliveryList));
        deliveryCounter.decrement(order.getShopCode(), orderDeliveryList.stream().map(orderDelivery -> new DeliveryCounter.DateCounter(orderDelivery.getDeliveryTimezoneFrom(), orderDelivery.getTotalGoodsCount())).collect(Collectors.toList()));
    }

    private void rollbackStock(Order order, String cancelReason) {
        log.info("取消未支付订单，释放库存：{}", JSON.toJSONString(order));
        long orderId = order.getId();
        String shopCode = order.getShopCode();

        final List<OrderDelivery> deliveryList = orderDeliveryMapper.findDeliveryDateListByOrderId(orderId);
        final Set<Long> ids = deliveryList.stream().map(OrderDelivery::getId).collect(Collectors.toSet());
        final List<OrderDeliveryGoods> deliveryGoodsList = orderDeliveryGoodsMapper.batchFindByDeliveryIds(ids);

        List<StockService.ShopDailyStock> dailyGoodsStockList = ShopDailyStockBuilder.buildIncrementDailyGoodsStocks(GoodsStockSource.CANCEL_ORDER.getValue(), shopCode, deliveryList, deliveryGoodsList);
        log.info("========释放库存.......订单集合：{}，订单明细集合：{}", JSON.toJSONString(deliveryList), JSON.toJSONString(deliveryGoodsList));
        stockService.lockMultiDayStock(dailyGoodsStockList);
        try {
            order.setCancelReason(cancelReason);
            order.setStatus(OrderStatus.CANCELED.getValue());
            order.setCancelTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
            //回滚相关商品库存
            stockService.incrementMultiDayStock(dailyGoodsStockList);
            log.info("==========订单取消成功==============订单ID：{},回滚库存.......门店编码：{}，库存信息：{}", orderId, shopCode, JSON.toJSONString(dailyGoodsStockList));
        } finally {
            //解锁相关商品库存
            stockService.unlockMultiDayStock(dailyGoodsStockList);
        }
    }
}
