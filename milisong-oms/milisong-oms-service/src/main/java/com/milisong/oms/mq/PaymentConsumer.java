package com.milisong.oms.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.oms.constant.OrderMode;
import com.milisong.oms.constant.PaymentStatus;
import com.milisong.oms.domain.*;
import com.milisong.oms.mapper.*;
import com.milisong.oms.service.PaymentMqProcessor;
import com.milisong.oms.service.stock.MonthlySalesUtils;
import com.milisong.oms.util.OrderRedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/20 13:48
 */
@Slf4j
@EnableBinding(MessageSink.class)
@MessageEndpoint(value = "orderPaymentConsumer")
public class PaymentConsumer {
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderDeliveryMapper orderDeliveryMapper;
    @Resource
    private OrderDeliveryGoodsMapper orderDeliveryGoodsMapper;
    @Resource
    private OrderPaymentWaterMapper orderPaymentWaterMapper;
    @Resource
    private PaymentMqProcessor paymentMqProcessor;
    @Resource
    private GroupBuyOrderMapper groupBuyOrderMapper;


    @StreamListener(MessageSink.MESSAGE_PAYMENT_INTPUT)
    public void accept(Message<String> message) {
        log.info("订单系统==========接收支付MQ消息，参数={}", JSONObject.toJSONString(message));
        String jsonString = message.getPayload();
        PaymentMessage paymentMessage = JSON.parseObject(jsonString, PaymentMessage.class);
        Long orderId = paymentMessage.getOrderId();


        Order order = orderMapper.selectByPrimaryKey(orderId);
        //FIXME 支付成功！=订单生效。将支付成功消息和订单生效消息分开处理
        if (order != null) {
            createPaymentWater(order, paymentMessage);
            log.info("========支付结果：{},订单：{}", paymentMessage.getStatus(), JSON.toJSONString(order));
            if (paymentMessage.getStatus() == PaymentStatus.SUCCEED.getValue()) {
                final List<OrderDelivery> deliveries = orderDeliveryMapper.findListByOrderId(orderId);
                final Set<Long> ids = deliveries.stream().map(OrderDelivery::getId).collect(Collectors.toSet());
                final List<OrderDeliveryGoods> deliveryGoods = orderDeliveryGoodsMapper.batchFindByDeliveryIds(ids);
                //记录月售量
                MonthlySalesUtils.monthlySales(deliveryGoods);
                //通知供应链集单配送
                paymentMqProcessor.sendMq2Scm(order, deliveries, deliveryGoods);
            }
        } else {
            GroupBuyOrder groupBuyOrder = groupBuyOrderMapper.selectByPrimaryKey(orderId);
            if (groupBuyOrder != null) {
                order = BeanMapper.map(groupBuyOrder, Order.class);
                order.setOrderMode(OrderMode.GROUP_BUY.getValue());
                createPaymentWater(order, paymentMessage);
                log.info("========拼团支付结果：{},订单：{}", paymentMessage.getStatus(), JSON.toJSONString(order));
            }
        }
    }


    private void createPaymentWater(Order order, PaymentMessage paymentMessage) {
        try {
            String key = OrderRedisKeyUtils.getMsgPreventDuplicateKey(paymentMessage.getMessageId());
            //消息幂等性
            if (BooleanUtils.isTrue(RedisCache.setNx(key, ""))) {
                RedisCache.expire(key, 30, TimeUnit.SECONDS);
                OrderPaymentWater paymentWater = new OrderPaymentWater();
                paymentWater.setRealName(order.getRealName());
                paymentWater.setSex(order.getSex());
                paymentWater.setMobileNo(order.getMobileNo());
                BeanMapper.copy(paymentMessage, paymentWater);
                paymentWater.setId(IdGenerator.nextId());
                log.info("============保存支付流水：{}", JSON.toJSONString(paymentWater));
                orderPaymentWaterMapper.insert(paymentWater);
            } else {
                log.warn("消息重复消费:{}", JSON.toJSONString(paymentMessage));
            }
        } catch (Exception e) {
            log.error("记录支付流水失败", e);
        }
    }
}
