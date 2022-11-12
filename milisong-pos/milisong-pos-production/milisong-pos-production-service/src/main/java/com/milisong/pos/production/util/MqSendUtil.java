package com.milisong.pos.production.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.milisong.pos.production.mq.dto.OrderSetProductionMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

@Slf4j
public class MqSendUtil {
    private static MessageChannel orderSetStartProduceChannel;
    private static MessageChannel sfOrderCreateProduceChannel;

    public static void setOrderSetPrintChannel(MessageChannel channel) {
        orderSetStartProduceChannel = channel;
    }

    public static void setSfOrderCreateChannel(MessageChannel channel) {
        sfOrderCreateProduceChannel = channel;
    }

    public static void sendOrderSetPrintMsg(OrderSetProductionMsg msg) {
        // add kv pair - routingkeyexpression (which matches 'type') will then evaluate
        // and add the value as routing key
        log.info("发送开始生产的MQ：{}", JSONObject.toJSONString(msg));
        orderSetStartProduceChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).setHeader("shopId", msg.getOrderSet().getShopId()).build());
        //orderSetStartProduceChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
    }

}
