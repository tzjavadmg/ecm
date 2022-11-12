package com.milisong.oms.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.milisong.oms.api.OrderPayService;
import com.milisong.oms.constant.DeliveryStatus;
import com.milisong.oms.constant.OrderResponseCode;
import com.milisong.oms.domain.Order;
import com.milisong.oms.domain.OrderDelivery;
import com.milisong.oms.domain.OrderDeliveryGoods;
import com.milisong.oms.dto.OrderDeliveryDto;
import com.milisong.oms.dto.OrderDeliveryGoodsDto;
import com.milisong.oms.dto.OrderDto;
import com.milisong.oms.dto.WechatMiniPayDto;
import com.milisong.oms.mapper.OrderDeliveryGoodsMapper;
import com.milisong.oms.mapper.OrderDeliveryMapper;
import com.milisong.oms.mapper.OrderMapper;
import com.milisong.oms.param.CancelOrderParam;
import com.milisong.oms.param.DeliveryTimezoneParam;
import com.milisong.oms.param.OrderPaymentParam;
import com.milisong.oms.param.PaymentResultParam;
import com.milisong.oms.util.DateTimeUtils;
import com.milisong.oms.util.OrderDtoBuilder;
import com.milisong.oms.util.OrderRedisKeyUtils;
import com.milisong.oms.util.WeekDayUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/7 16:50
 */
@Slf4j
@RestController
public class OrderPayServiceImpl implements OrderPayService {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderDeliveryMapper orderDeliveryMapper;
    @Resource
    private OrderDeliveryGoodsMapper orderDeliveryGoodsMapper;
    @Resource
    private OrderPayTransactionService orderPayTransactionService;
    @Resource
    private OrderPaymentHelper orderPaymentHelper;
    @Resource
    private DelayQueueHelper delayQueueHelper;

    @Override
    public ResponseResult<WechatMiniPayDto> payOrder(@RequestBody OrderPaymentParam orderPaymentParam) {
        log.info("--------------小程序微信支付，入参：{}", JSON.toJSONString(orderPaymentParam));
        //虚拟订单ID,创建真实订单时ID同虚拟订单一致
        long orderId = orderPaymentParam.getOrderId();
        Order order = orderMapper.selectByPrimaryKey(orderId);
        String lockKey = null;
        if (order == null) {
            lockKey = OrderRedisKeyUtils.getCancelVirtualOrderLockKey(orderId);
        } else {
            lockKey = OrderRedisKeyUtils.getCancelOrderLockKey(orderId);
        }
        Lock lock = LockProvider.getLock(lockKey);
        lock.lock();
        log.info("---------支付------进入订单锁【{}】，防止并发情况：用户支付的同时，虚拟订单或真实订单过期回滚库存", orderId);
        try {
            return orderPayTransactionService.payment(orderPaymentParam, order);
        } finally {
            lock.unlock();
            log.info("---------支付------离开订单锁【{}】，防止并发情况：用户支付的同时，虚拟订单或真实订单过期回滚库存", orderId);
        }
    }

    @Override
    public ResponseResult<?> paymentResult(@RequestBody PaymentResultParam paymentResultParam) {
        return orderPaymentHelper.paymentResult(paymentResultParam);
    }

    @Override
    public ResponseResult<?> userCancelOrder(@RequestBody CancelOrderParam cancelOrderParam) {
        log.info("---------------用户取消真实订单，入参：{}", JSON.toJSONString(cancelOrderParam));
        Long orderId = cancelOrderParam.getOrderId();
        String lockKey = OrderRedisKeyUtils.getCancelOrderLockKey(orderId);
        Lock lock = LockProvider.getLock(lockKey);
        lock.lock();
        log.info("---------用户取消真实订单------进入订单锁【{}】，防止并发情况：真实订单过期回滚库存时，用户进行支付操作", orderId);
        try {
            ResponseResult<?> responseResult = orderPayTransactionService.cancelOrder(cancelOrderParam);
            if (!responseResult.isSuccess()) {
                return responseResult;
            }
            //String unpaidOrderKey = OrderRedisKeyUtils.getUnpaidOrderKey(cancelOrderParam.getOrderId());
            //RedisCache.delete(unpaidOrderKey);
            delayQueueHelper.remove(orderId);
            return ResponseResult.buildSuccessResponse();
        } finally {
            lock.unlock();
            log.info("---------用户取消真实订单------离开订单锁【{}】，防止并发情况：真实订单过期回滚库存时，用户进行支付操作", orderId);
        }
    }

    @Override
    public ResponseResult<?> expiredCancelOrder(@RequestBody CancelOrderParam cancelOrderParam) {
        Long orderId = cancelOrderParam.getOrderId();
        String lockKey = OrderRedisKeyUtils.getCancelOrderLockKey(orderId);
        Lock lock = LockProvider.getLock(lockKey);
        lock.lock();
        log.info("---------过期取消真实订单------进入订单锁【{}】，防止并发情况：真实订单过期回滚库存时，用户进行支付操作", orderId);
        try {
            return orderPayTransactionService.cancelOrder(cancelOrderParam);
        } finally {
            lock.unlock();
            log.info("---------过期取消真实订单------离开订单锁【{}】，防止并发情况：真实订单过期回滚库存时，用户进行支付操作", orderId);
        }
    }

    @Override
    public ResponseResult<List<OrderDeliveryDto>> findDeliveries4RefundByOrderId(Long orderId) {
        Date now = new Date();
        List<OrderDelivery> deliveries = orderDeliveryMapper.findListByOrderId(orderId);
        List<OrderDeliveryDto> refundableDeliveryDtoList = new ArrayList<>();
        List<OrderDeliveryDto> unRefundableDeliveryDtoList = new ArrayList<>();
        List<OrderDeliveryDto> completedDeliveryDtoList = new ArrayList<>();
        for (OrderDelivery delivery : deliveries) {
            OrderDeliveryDto orderDeliveryDto = BeanMapper.map(delivery, OrderDeliveryDto.class);
            orderDeliveryDto.setDeliveryDate(DateFormatUtils.format(delivery.getDeliveryDate(), "yyyy-MM-dd"));
            String dayDesc = WeekDayUtils.getWeekDayString(delivery.getDeliveryDate());
            orderDeliveryDto.setDayDesc(dayDesc);

            List<OrderDeliveryGoods> orderDeliveryGoods = orderDeliveryGoodsMapper.findByDeliveryId(delivery.getId());
            List<OrderDeliveryGoodsDto> orderDeliveryGoodsDtos = BeanMapper.mapList(orderDeliveryGoods, OrderDeliveryGoodsDto.class);
            orderDeliveryDto.setDeliveryGoods(orderDeliveryGoodsDtos);

            Date cutoffTime = delivery.getDeliveryTimezoneCutoffTime();
            boolean isExpiredCutOffTime = delivery.getDeliveryDate().compareTo(DateUtils.truncate(now, Calendar.DATE)) < 0 || WeekDayUtils.isToday(delivery.getDeliveryDate()) && (cutoffTime == null || now.compareTo(cutoffTime) > 0);
            if (isExpiredCutOffTime || DeliveryStatus.GETTING_READY.getValue() != delivery.getStatus()) {
                orderDeliveryDto.setRefundable(Boolean.FALSE);
                orderDeliveryDto.setStatusName(DeliveryStatus.getNameByValue(delivery.getStatus()));
                if (DeliveryStatus.COMPLETED.getValue() == delivery.getStatus()) {
                    completedDeliveryDtoList.add(orderDeliveryDto);
                } else {
                    unRefundableDeliveryDtoList.add(orderDeliveryDto);
                }
            } else {
                orderDeliveryDto.setRefundable(Boolean.TRUE);
                orderDeliveryDto.setStatusName(DeliveryStatus.getNameByValue(delivery.getStatus()));
                refundableDeliveryDtoList.add(orderDeliveryDto);
            }
        }
        log.info("----------------过虑后可退款的订单------：{}", JSON.toJSONString(refundableDeliveryDtoList));
        boolean isAllCanceled = refundableDeliveryDtoList.size() == 0;
        //将可退款的配送记录放至列表头部
        refundableDeliveryDtoList.addAll(unRefundableDeliveryDtoList);
        refundableDeliveryDtoList.addAll(completedDeliveryDtoList);
        log.info("----------------重排组合后的订单列表------：{}", JSON.toJSONString(refundableDeliveryDtoList));
        if (isAllCanceled) {
            return ResponseResult.buildSuccessResponse(OrderResponseCode.REFUND_ALL_DELIVERY_CANCELED.getCode(), OrderResponseCode.REFUND_ALL_DELIVERY_CANCELED.getMessage(), refundableDeliveryDtoList);
        }
        return ResponseResult.buildSuccessResponse(refundableDeliveryDtoList);
    }

    @Override
    public ResponseResult<?> updateDeliveryTimezone(@RequestBody DeliveryTimezoneParam deliveryTimezoneParam) {
        Long deliveryId = deliveryTimezoneParam.getDeliveryId();
        OrderDelivery orderDelivery = orderDeliveryMapper.selectByPrimaryKey(deliveryId);
        orderDelivery.setDeliveryTimezoneFrom(DateTimeUtils.mergeDateAndTimeString(orderDelivery.getDeliveryDate(), deliveryTimezoneParam.getStartTime()));
        orderDelivery.setDeliveryTimezoneTo(DateTimeUtils.mergeDateAndTimeString(orderDelivery.getDeliveryDate(), deliveryTimezoneParam.getEndTime()));
        orderDeliveryMapper.updateByPrimaryKeySelective(orderDelivery);
        return ResponseResult.buildSuccessResponse();
    }

    @Override
    public ResponseResult<OrderDto> getOrderById(Long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        List<OrderDelivery> deliveries = orderDeliveryMapper.findDeliveryDateListByOrderId(orderId);
        final Set<Long> ids = deliveries.stream().map(OrderDelivery::getId).collect(Collectors.toSet());
        List<OrderDeliveryGoods> deliveryGoods = orderDeliveryGoodsMapper.batchFindByDeliveryIds(ids);
        return ResponseResult.buildSuccessResponse(OrderDtoBuilder.buildOrderDto(order, deliveries, deliveryGoods));
    }
}
