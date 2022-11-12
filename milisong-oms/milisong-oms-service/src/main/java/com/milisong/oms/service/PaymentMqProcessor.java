package com.milisong.oms.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.util.BeanMapper;
import com.milisong.oms.domain.Order;
import com.milisong.oms.domain.OrderDelivery;
import com.milisong.oms.domain.OrderDeliveryGoods;
import com.milisong.oms.mapper.OrderDeliveryGoodsMapper;
import com.milisong.oms.mapper.OrderDeliveryMapper;
import com.milisong.oms.mq.MessageSource;
import com.milisong.oms.mq.OrderDeliveryGoodsMessage;
import com.milisong.oms.mq.OrderDeliveryMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @ Description：订单支付MQ消息处理
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/25 15:18
 */
@Slf4j
@Component
public class PaymentMqProcessor {

    @Resource
    @Output(MessageSource.ORDER_OUTPUT)
    private MessageChannel channel;
    @Resource
    private OrderDeliveryMapper orderDeliveryMapper;
    @Resource
    private OrderDeliveryGoodsMapper orderDeliveryGoodsMapper;

    public void sendMq2Scm(Order order) {
        final List<OrderDelivery> deliveries = orderDeliveryMapper.findListByOrderId(order.getId());
        final Set<Long> ids = deliveries.stream().map(OrderDelivery::getId).collect(Collectors.toSet());
        final List<OrderDeliveryGoods> deliveryGoods = orderDeliveryGoodsMapper.batchFindByDeliveryIds(ids);
        sendMq2Scm(order, deliveries, deliveryGoods);
    }

    public void sendMq2Scm(Order order, List<OrderDelivery> deliveries, List<OrderDeliveryGoods> deliveryGoods) {
        log.info("---------发送订单履约中心MQ消息入参,订单：{},配送单：{},配送单商品：{}", JSON.toJSONString(order), JSON.toJSONString(deliveries), JSON.toJSONString(deliveryGoods));
        final Map<Long, List<OrderDeliveryGoods>> goodsByDeliveryIdMap =
                deliveryGoods.stream().collect(groupingBy(OrderDeliveryGoods::getDeliveryId));
        final List<OrderDeliveryMessage> orderList = new ArrayList<>();

        deliveries.forEach(delivery -> {
            OrderDeliveryMessage orderDeliveryMessage = new OrderDeliveryMessage();
            BeanMapper.copy(order, orderDeliveryMessage);

            List<OrderDeliveryGoods> orderDeliveryGoodsList = goodsByDeliveryIdMap.get(delivery.getId());
            Map<String, List<OrderDeliveryGoods>> comboGoodsMap = orderDeliveryGoodsList.stream()
                    //过虑单品
                    .filter(orderDeliveryGoods -> StringUtils.isNotEmpty(orderDeliveryGoods.getComboGoodsCode()))
                    //按组合商品编码分组，转换成map
                    .collect(groupingBy(OrderDeliveryGoods::getComboGoodsCode));
            List<OrderDeliveryGoodsMessage> orderDeliveryGoodsMessageList =
                    orderDeliveryGoodsList.stream()
                            //过虑组合商品
                            .filter(orderDeliveryGoods -> StringUtils.isEmpty(orderDeliveryGoods.getComboGoodsCode())).map(orderDeliveryGoods -> {
                        OrderDeliveryGoodsMessage orderDeliveryGoodsMessage = BeanMapper.map(orderDeliveryGoods, OrderDeliveryGoodsMessage.class);
                        orderDeliveryGoodsMessage.setDeliveryId(orderDeliveryGoods.getDeliveryId());
                        orderDeliveryGoodsMessage.setGoodsActualPrice(orderDeliveryGoods.getGoodsActualPrice());
                        orderDeliveryGoodsMessage.setGoodsOriginalPrice(orderDeliveryGoods.getGoodsOriginalPrice());
                        orderDeliveryGoodsMessage.setPackageOriginalPrice(orderDeliveryGoods.getPackageOriginalPrice());
                        orderDeliveryGoodsMessage.setPackageActualPrice(orderDeliveryGoods.getPackageActualPrice());
                        orderDeliveryGoodsMessage.setIsCombo(orderDeliveryGoods.getIsCombo());
                        orderDeliveryGoodsMessage.setType(orderDeliveryGoods.getType());
                        orderDeliveryGoodsMessage.setComboDetails(BeanMapper.mapList(comboGoodsMap.get(orderDeliveryGoods.getGoodsCode()), OrderDeliveryGoodsMessage.class));
                        return orderDeliveryGoodsMessage;
                    }).collect(Collectors.toList());
            orderDeliveryMessage.setId(delivery.getId());
            orderDeliveryMessage.setDeliveryGoods(orderDeliveryGoodsMessageList);
            orderDeliveryMessage.setOrderActualAmount(order.getTotalPayAmount());
            orderDeliveryMessage.setOrderId(delivery.getOrderId());
            orderDeliveryMessage.setDeliveryNo(delivery.getDeliveryNo());
            orderDeliveryMessage.setDeliveryBuildingId(order.getDeliveryBuildingId());
            orderDeliveryMessage.setDeliveryActualAmount(delivery.getDeliveryActualPrice());
            orderDeliveryMessage.setTotalActualAmount(delivery.getTotalAmount());
            orderDeliveryMessage.setGoodsOriginalAmount(delivery.getTotalGoodsOriginalAmount());
            orderDeliveryMessage.setGoodsActualAmount(delivery.getTotalGoodsActualAmount());
            orderDeliveryMessage.setPackageActualAmount(delivery.getTotalPackageActualAmount());
            orderDeliveryMessage.setShareOrderPayAmount(delivery.getShareOrderPayAmount());
            orderDeliveryMessage.setOrderType(order.getOrderType());
            Date timezoneFrom = delivery.getDeliveryTimezoneFrom();
            Date timezoneTo = delivery.getDeliveryTimezoneTo();
            orderDeliveryMessage.setDeliveryTimezoneFrom(timezoneFrom);
            orderDeliveryMessage.setDeliveryTimezoneTo(timezoneTo);
            orderDeliveryMessage.setDeliveryStatus(delivery.getStatus());
            orderList.add(orderDeliveryMessage);
        });

        String jsonOrderString = JSON.toJSONString(orderList);
        channel.send(MessageBuilder.withPayload(jsonOrderString).build());
        log.info("----------订单履约中心的MQ消息发送成功:{}", jsonOrderString);
    }
}
