package com.milisong.oms.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.exception.BusinessException;
import com.farmland.core.util.BeanMapper;
import com.milisong.oms.constant.OrderResponseCode;
import com.milisong.oms.constant.OrderStatus;
import com.milisong.oms.constant.VirtualOrderStatus;
import com.milisong.oms.domain.Order;
import com.milisong.oms.domain.VirtualOrder;
import com.milisong.oms.domain.VirtualOrderDelivery;
import com.milisong.oms.dto.TimezoneDto;
import com.milisong.oms.param.DeliveryTimezoneParam;
import com.milisong.oms.util.SysConfigUtils;
import com.milisong.oms.util.DateTimeUtils;
import com.milisong.oms.util.OrderRedisKeyUtils;
import com.milisong.oms.util.WeekDayUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/8 11:05
 */
@Slf4j
@Component
public class OrderValidator {

    public void verifyVirtualOrderStatus(VirtualOrder virtualOrder) {
        Byte orderStatus = virtualOrder.getStatus();
        log.info("虚拟订单状态校验：{}", orderStatus);
        //订单超时未支付会自动取消
        if (VirtualOrderStatus.CANCELED.getValue() == orderStatus) {
            Integer unPayExpiredTime = SysConfigUtils.getUnPayExpiredTime();
            String messageTpl = OrderResponseCode.V_ORDER_CHECK_PAY_TIME_EXPIRED.getMessage();
            throw BusinessException.build(OrderResponseCode.V_ORDER_CHECK_PAY_TIME_EXPIRED.getCode(), String.format(messageTpl, unPayExpiredTime));
        }
    }

    public void verifyOrderStatus(Order order) {
        Byte orderStatus = order.getStatus();
        log.info("订单状态校验：{}", orderStatus);
        //订单超时未支付会自动取消
        if (OrderStatus.CANCELED.getValue() == orderStatus) {
            Integer unPayExpiredTime = SysConfigUtils.getUnPayExpiredTime();
            String messageTpl = OrderResponseCode.ORDER_CANCELED.getMessage();
            throw BusinessException.build(OrderResponseCode.ORDER_CANCELED.getCode(), String.format(messageTpl, unPayExpiredTime));
        }
    }

    public void verifyDeliveryTimezone(final String shopCode, final Date orderDate, final List<VirtualOrderDelivery> virtualOrderDeliveries, final Map<Long, DeliveryTimezoneParam> deliveryTimezoneMap, List<TimezoneDto> timezoneDtos) {
        //校验配送段是否可用
        for (VirtualOrderDelivery orderDelivery : virtualOrderDeliveries) {
            DeliveryTimezoneParam timezoneParam = deliveryTimezoneMap.get(orderDelivery.getId());
            String orderDateString = DateFormatUtils.format(orderDelivery.getDeliveryDate(), "MM月dd日");
            if (timezoneParam == null) {
                String messageTpl = OrderResponseCode.ORDER_CHECK_NO_DELIVERY_ALL_DAY.getMessage();
                throw BusinessException.build(OrderResponseCode.ORDER_CHECK_NO_DELIVERY_ALL_DAY.getCode(), String.format(messageTpl, orderDateString));
            }
            String endTime = timezoneParam.getEndTime();
            boolean available = isAvailable(shopCode, orderDelivery, timezoneParam);
            log.info("产能不足，门店编码：{}，配送子单，{}，配送时段信息：{}", shopCode, JSON.toJSONString(orderDelivery), JSON.toJSONString(timezoneParam));
            if (!available) {
                boolean hasAvailable = false;
                for (TimezoneDto tz : timezoneDtos) {
                    Date cutoffTime = DateTimeUtils.mergeDateAndTimeString(orderDelivery.getDeliveryDate(), tz.getCutoffTime());
                    if (!WeekDayUtils.isToday(orderDelivery.getDeliveryDate()) || orderDate.compareTo(cutoffTime) < 0) {
                        if (isAvailable(shopCode, orderDelivery, BeanMapper.map(tz, DeliveryTimezoneParam.class))) {
                            hasAvailable = true;
                            break;
                        }
                    }
                }
                if (hasAvailable) {
                    String messageTpl = OrderResponseCode.ORDER_CHECK_NO_DELIVERY.getMessage();
                    throw BusinessException.build(OrderResponseCode.ORDER_CHECK_NO_DELIVERY.getCode(), String.format(messageTpl, orderDateString, endTime));
                } else {
                    String messageTpl = OrderResponseCode.ORDER_CHECK_NO_DELIVERY_ALL_DAY.getMessage();
                    throw BusinessException.build(OrderResponseCode.ORDER_CHECK_NO_DELIVERY_ALL_DAY.getCode(), String.format(messageTpl, orderDateString));
                }
            }
        }
    }

    private boolean isAvailable(String shopCode, VirtualOrderDelivery orderDelivery, DeliveryTimezoneParam timezone) {
        int goodsCount = orderDelivery.getTotalGoodsCount();
        int maxCapacity = timezone.getMaxCapacity();
        Date deliveryStartTime = DateTimeUtils.mergeDateAndTimeString(orderDelivery.getDeliveryDate(), timezone.getStartTime());
        String counterKey = OrderRedisKeyUtils.getDeliveryCounterKey(shopCode, deliveryStartTime);
        String counterString = RedisCache.get(counterKey);
        Integer counter = 0;
        if (StringUtils.isNotEmpty(counterString)) {
            counter = new Integer(counterString);
        }
        log.info("校验产量，redisKey：{},counter：{}，deliveryStartTime：{},goodsCount：{},maxCapacity：{}", counterKey, counterString, deliveryStartTime, goodsCount, maxCapacity);
        return counter + goodsCount <= maxCapacity;
    }
}
