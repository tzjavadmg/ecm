package com.milisong.oms.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.exception.BusinessException;
import com.farmland.core.util.BeanMapper;
import com.milisong.oms.constant.*;
import com.milisong.oms.domain.*;
import com.milisong.oms.mapper.OrderDeliveryGoodsMapper;
import com.milisong.oms.mapper.OrderDeliveryMapper;
import com.milisong.oms.mapper.OrderMapper;
import com.milisong.oms.mapper.VirtualOrderMapper;
import com.milisong.oms.param.DeliveryTimezoneParam;
import com.milisong.oms.param.OrderPaymentParam;
import com.milisong.oms.service.promotion.PromotionHelper;
import com.milisong.oms.service.refund.RefundAlgorithm;
import com.milisong.oms.service.refund.RefundAlgorithmExecutor;
import com.milisong.oms.service.stock.DeliveryCounter;
import com.milisong.oms.util.DateTimeUtils;
import com.milisong.oms.util.OrderDtoBuilder;
import com.milisong.oms.util.OrderNoBuilder;
import com.milisong.oms.util.OrderRedisKeyUtils;
import com.milisong.pms.prom.dto.OrderAmountDto;
import com.milisong.pms.prom.param.OrderParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/8 10:16
 */
@Slf4j
@Component
public class OrderBuilder {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderDeliveryMapper orderDeliveryMapper;
    @Resource
    private OrderDeliveryGoodsMapper orderDeliveryGoodsExtMapper;
    @Resource
    private VirtualOrderMapper virtualOrderMapper;
    @Resource
    private DeliveryCounter deliveryCounter;
    @Resource
    private DelayQueueHelper delayQueueHelper;
    @Resource
    private PromotionHelper promotionHelper;

    private Order buildOrder(Date orderDate, OrderPaymentParam orderPaymentParam, VirtualOrder virtualOrder) {
        checkRedPacketIsValid(orderPaymentParam.getRedPacketId());
        checkCouponIsValid(orderPaymentParam.getCouponId());

        String orderNo = OrderNoBuilder.build(virtualOrder.getOrderDate(), virtualOrder.getOrderType(), virtualOrder.getShopCode());
        Order order = new Order();
        BeanMapper.copy(virtualOrder, order);
        BeanMapper.copy(orderPaymentParam, order);
        order.setPayMode(PayMode.WECHAT_PAY.getValue());
        if (order.getOrderSource() == null) {
            order.setOrderSource(OrderUserSource.WECHAT_MINI_LC.getValue());
        }
        order.setOrderMode(OrderMode.COMMON.getValue());
        order.setOrderDate(orderDate);
        order.setOrderNo(orderNo);
        return order;
    }

    private void checkRedPacketIsValid(Long redPackedId) {
        if (redPackedId == null) {
            return;
        }
        String redPacketLockKey = OrderRedisKeyUtils.getRedPacketLockKey(redPackedId);
        String existsOrderNo = RedisCache.get(redPacketLockKey);
        if (StringUtils.isNotEmpty(existsOrderNo)) {
            throw BusinessException.build("", "红包已经被订单号为" + existsOrderNo + "的订单锁定");
        }
    }

    private void checkCouponIsValid(Long couponId) {
        if (couponId == null) {
            return;
        }
        String couponLockKey = OrderRedisKeyUtils.getCouponLockKey(couponId);
        String existsOrderNo = RedisCache.get(couponLockKey);
        if (StringUtils.isNotEmpty(existsOrderNo)) {
            throw BusinessException.build("", "优惠券已经被订单号为" + existsOrderNo + "的订单锁定");
        }
    }

    public Order createOrder(Date orderDate, OrderPaymentParam orderPaymentParam, Map<Long, DeliveryTimezoneParam> deliveryTimezoneMap, VirtualOrder virtualOrder, List<VirtualOrderDelivery> virtualOrderDeliveries, List<VirtualOrderDeliveryGoods> virtualOrderDeliveryGoods) {
        Order order = buildOrder(orderDate, orderPaymentParam, virtualOrder);
        String shopCode = order.getShopCode();
        List<OrderDelivery> orderDeliveries = BeanMapper.mapList(virtualOrderDeliveries, OrderDelivery.class);
        List<OrderDeliveryGoods> orderDeliveryGoods = BeanMapper.mapList(virtualOrderDeliveryGoods, OrderDeliveryGoods.class);
        //计算促销活动后订单金额
        OrderAmountDto orderAmountDto = setOrderAmount(order, orderDeliveries, orderDeliveryGoods);

        int deliveryIndex = 1;
        int lastIndex = orderDeliveries.size();
        BigDecimal totalPayAmount = order.getTotalPayAmount();

        //抵扣积分
        Integer totalUsedPoints = orderPaymentParam.getDeductionPoints();
        BigDecimal totalShareOrderPayAmount = BigDecimal.ZERO;
        Integer totalShareOrderPayPoints = 0;
        boolean hasTimezoneConfig = MapUtils.isNotEmpty(deliveryTimezoneMap);
        for (OrderDelivery delivery : orderDeliveries) {
            delivery.setDeliveryNo(order.getOrderNo() + "_" + String.format("%02d", deliveryIndex));
            delivery.setStatus(DeliveryStatus.GETTING_READY.getValue());

            if (hasTimezoneConfig) {
                //午餐配送时间跟着业务线的全局或门店配置走
                DeliveryTimezoneParam deliveryTimezoneParam = deliveryTimezoneMap.get(delivery.getId());
                delivery.setDeliveryTimezoneId(deliveryTimezoneParam.getId());
                delivery.setDeliveryTimezoneCutoffTime(DateTimeUtils.mergeDateAndTimeString(delivery.getDeliveryDate(), deliveryTimezoneParam.getCutoffTime()));
                delivery.setDeliveryTimezoneFrom(DateTimeUtils.mergeDateAndTimeString(delivery.getDeliveryDate(), deliveryTimezoneParam.getStartTime()));
                delivery.setDeliveryTimezoneTo(DateTimeUtils.mergeDateAndTimeString(delivery.getDeliveryDate(), deliveryTimezoneParam.getEndTime()));
            } else {
                //上面的if判断代码可以不需要了 FIXME
                if (StringUtils.isEmpty(orderPaymentParam.getCutoffTime())) {
                    orderPaymentParam.setCutoffTime("00:00");
                }
                delivery.setDeliveryTimezoneCutoffTime(DateTimeUtils.mergeDateAndTimeString(delivery.getDeliveryDate(), orderPaymentParam.getCutoffTime()));
                delivery.setDeliveryTimezoneFrom(DateTimeUtils.mergeDateAndTimeString(delivery.getDeliveryDate(), orderPaymentParam.getDeliveryTimezoneFrom()));
                delivery.setDeliveryTimezoneTo(DateTimeUtils.mergeDateAndTimeString(delivery.getDeliveryDate(), orderPaymentParam.getDeliveryTimezoneTo()));
            }

            /**
             * 计算退款分摊金额
             */
            if (deliveryIndex == lastIndex) {
                BigDecimal lastShareAmount = totalPayAmount.subtract(totalShareOrderPayAmount);
                if (lastShareAmount.doubleValue() < 0) {
                    throw BusinessException.build("",String.format("分摊金额s%为负数！", lastShareAmount));
                }
                delivery.setShareOrderPayAmount(lastShareAmount);
                if (totalUsedPoints != null) {
                    Integer lastSharePoints = totalUsedPoints - totalShareOrderPayPoints;
                    if (lastSharePoints < 0) {
                        throw BusinessException.build("",String.format("分摊积分s%为负数！", lastSharePoints));
                    }
                    delivery.setShareOrderDeductionPoints(lastSharePoints);
                    log.info("------------------最后一笔：总支付金额：{}，总分摊金额：{},分摊积分：{}", totalPayAmount, totalShareOrderPayAmount, lastSharePoints);
                }
            } else {
                RefundAlgorithm.RefundDto refundDto = new RefundAlgorithmExecutor(orderAmountDto, order, delivery).calculate();
                BigDecimal shareOrderPayAmount = refundDto.getRefundAmount();
                //设置分摊金额
                delivery.setShareOrderPayAmount(shareOrderPayAmount);
                totalShareOrderPayAmount = totalShareOrderPayAmount.add(shareOrderPayAmount);

                //设置分摊积分 总积分*分摊金额/总支付金额
                if (refundDto.getRefundPoints() != null) {
                    delivery.setShareOrderDeductionPoints(refundDto.getRefundPoints());
                    totalShareOrderPayPoints = totalShareOrderPayPoints + refundDto.getRefundPoints();
                }
                log.info("商品打包费：{},配送费：{}，分摊金额 ：{}, 累计分摊金额：{}", delivery.getTotalPackageActualAmount(), delivery.getDeliveryActualPrice(), shareOrderPayAmount, totalShareOrderPayAmount);
            }
            deliveryIndex++;
        }
        log.info("----------插入真实订单信息，订单：{},配送单：{},商品：{}", JSON.toJSONString(order), JSON.toJSONString(orderDeliveries), JSON.toJSONString(orderDeliveryGoods));
        orderMapper.insert(order);
        orderDeliveryMapper.batchSave(orderDeliveries);
        orderDeliveryGoodsExtMapper.batchSave(orderDeliveryGoods);
        //更新虚拟订单状态为已完成
        completeVirtualOrder(virtualOrder);
        //更新生产量计数器
        updateDeliveryCounter(shopCode, orderDeliveries);
        //设置未支付过期key
        cacheUnpaidOrder(order.getId(), order.getOrderDate());
        return order;
    }

    private OrderAmountDto setOrderAmount(Order order, List<OrderDelivery> orderDeliveries, List<OrderDeliveryGoods> orderDeliveryGoods) {
        OrderParam orderParam = BeanMapper.map(OrderDtoBuilder.buildOrderDto(order, orderDeliveries, orderDeliveryGoods), OrderParam.class);
        orderParam.setBusinessLine(order.getOrderType());
        log.info("-----促销计算接口参数--------：{}", JSON.toJSONString(orderParam));
        OrderAmountDto orderAmountDto = promotionHelper.calculate(orderParam);
        log.info("-----促销计算接口返回结果--------：{}", JSON.toJSONString(orderAmountDto));
        BigDecimal actualPayAmount = orderAmountDto.getTotalAmount();
        BigDecimal redPacketPayAmount = orderAmountDto.getRedPackAmount();
        BigDecimal fullReduceAmount = orderAmountDto.getFullReduceAmount();
        Integer acquirePoints = orderAmountDto.getAcquirePoints();
        order.setFullReduceAmount(fullReduceAmount);
        order.setTotalPayAmount(order.getTotalDeliveryActualAmount().add(actualPayAmount));
        order.setRedPacketAmount(redPacketPayAmount);
        order.setCouponAmount(orderAmountDto.getCouponAmount());
        order.setAcquirePoints(acquirePoints);
        return orderAmountDto;
    }

    private void completeVirtualOrder(VirtualOrder virtualOrder) {
        virtualOrder.setStatus(VirtualOrderStatus.COMPLETED.getValue());
        virtualOrderMapper.updateByPrimaryKeySelective(virtualOrder);
    }

    private void updateDeliveryCounter(String shopCode, List<OrderDelivery> orderDeliveries) {
        deliveryCounter.increment(shopCode, orderDeliveries.stream().map(orderDelivery -> new DeliveryCounter.DateCounter(orderDelivery.getDeliveryTimezoneFrom(), orderDelivery.getTotalGoodsCount())).collect(Collectors.toList()));
    }

    private void cacheUnpaidOrder(Long orderId, Date orderDate) {
        delayQueueHelper.push(orderId, orderDate);
    }
}
