package com.milisong.ecm.breakfast.order.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.exception.BusinessException;
import com.milisong.ecm.common.api.ShopConfigService;
import com.milisong.ecm.common.util.DateTimeUtils;
import com.milisong.ecm.common.util.WeekDayUtils;
import com.milisong.oms.constant.OrderResponseCode;
import com.milisong.oms.param.DeliveryTimezoneParam;
import com.milisong.oms.param.OrderPaymentParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Resource
    private ShopConfigService shopConfigService;

    public void validate(OrderPaymentParam orderPaymentParam) {
        Date orderDate = orderPaymentParam.getOrderDate();
        String orderDateString = DateFormatUtils.format(orderDate, "MM月dd日");
        List<DeliveryTimezoneParam> deliveryTimezones = orderPaymentParam.getDeliveryTimezones();
        log.info("================配送时段配置:{}", JSON.toJSONString(deliveryTimezones));
        Map<Long, DeliveryTimezoneParam> deliveryTimezoneMap = deliveryTimezones.stream().collect(Collectors.toMap(DeliveryTimezoneParam::getDeliveryId, deliveryTimezone -> deliveryTimezone));

        DeliveryTimezoneParam firstDeliveryTimezoneParam=deliveryTimezones.get(0);
        Date firstDeliveryDate = firstDeliveryTimezoneParam.getDeliveryDate();
        Long firstDeliveryId = firstDeliveryTimezoneParam.getDeliveryId();

        log.info("--------------获取配送时间：Map:{},deliveryId:{}", deliveryTimezoneMap, firstDeliveryId);
        DeliveryTimezoneParam deliveryTimezoneParam = deliveryTimezoneMap.get(firstDeliveryId);


        if (deliveryTimezoneParam == null) {
            String messageTpl = OrderResponseCode.ORDER_CHECK_NO_DELIVERY_ALL_DAY.getMessage();
            throw BusinessException.build(OrderResponseCode.ORDER_CHECK_NO_DELIVERY_ALL_DAY.getCode(), String.format(messageTpl, orderDateString));
        }
        Date currentCutOffTime = DateTimeUtils.mergeDateAndTimeString(orderDate, deliveryTimezoneParam.getCutoffTime());
        Date lastCutOffTime = shopConfigService.getTodayLastCutOffTime(orderPaymentParam.getShopCode());

        if (WeekDayUtils.isToday(firstDeliveryDate)) {
            //1.如果有今天的配送信息，当前时间超过当天最后一个截单时间，则不允许下单
            //2.如果有今天的配送信息，当前时间没有超过最后一个截单时间，则提示可以选择其它配送时段
            if (orderDate.getTime() > lastCutOffTime.getTime()) {
                //如果是今天的订单，并且超过截单时间.
                String messageTpl = OrderResponseCode.ORDER_CHECK_CUTOFF_TIME_EXPIRED.getMessage();
                throw BusinessException.build(OrderResponseCode.ORDER_CHECK_CUTOFF_TIME_EXPIRED.getCode(), String.format(messageTpl, orderDateString));
            }

            if (orderDate.getTime() < lastCutOffTime.getTime() && orderDate.getTime() > currentCutOffTime.getTime()) {
                //如果是今天的订单，并且超过截单时间.
                String messageTpl = OrderResponseCode.ORDER_CHECK_CURRENT_CUTOFF_TIME_EXPIRED.getMessage();
                throw BusinessException.build(OrderResponseCode.ORDER_CHECK_CURRENT_CUTOFF_TIME_EXPIRED.getCode(), String.format(messageTpl, orderDateString));
            }
        }
    }
}
