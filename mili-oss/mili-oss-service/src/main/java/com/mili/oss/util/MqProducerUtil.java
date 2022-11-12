package com.mili.oss.util;

import com.mili.oss.mq.MqMessageSource;
import com.mili.oss.mq.message.WaveOrderMessage;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mili.oss.mq.OrderSetOrderStatusChangeMsg;
import com.mili.oss.mq.OrderSetProductionLunchMsg;
import com.mili.oss.mq.OrderSetProductionMsg;
import com.milisong.scm.base.dto.mq.OperationLogDto;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * MQ生产者帮助类
 *
 * @author yangzhilong
 */
@Slf4j
public class MqProducerUtil {
    private static MessageChannel orderSetStatusChannel;

    private static MessageChannel operationLogChannel;

    private static MessageChannel orderSetOutputChannel;

    private static MessageChannel pushSfOrderOutputChannel;

    private static MessageChannel orderSetOutputChannelBF;

    private static MessageChannel waveOrderOutput;

    public static void setWaveOrderOutput(MessageChannel channel) {
        MqProducerUtil.waveOrderOutput = channel;
    }

    public static void setOrderSetChannel(MessageChannel channel) {
        MqProducerUtil.orderSetStatusChannel = channel;
    }

    public static void setOperationLogChannel(MessageChannel channel) {
        MqProducerUtil.operationLogChannel = channel;
    }

    public static void setOrderSetOutputChannel(MessageChannel channel) {
        MqProducerUtil.orderSetOutputChannel = channel;
    }

    public static void setPushSfOrderOutputChannel(MessageChannel channel) {
        MqProducerUtil.pushSfOrderOutputChannel = channel;
    }

    public static void setOrderSetOutputChannelBF(MessageChannel orderSetOutputChannelBF) {
        MqProducerUtil.orderSetOutputChannelBF = orderSetOutputChannelBF;
    }


    public static void sendOrderSetOrderStatusMsg(OrderSetOrderStatusChangeMsg msg) {
        log.info("发送集单里状态变更的MQ，消息内容：{}", JSONObject.toJSONString(msg));
        orderSetStatusChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
    }

    public static void sendOperationLog(OperationLogDto msg) {
        log.info("发送操作日志的MQ，消息内容:{}", JSONObject.toJSONString(msg));
        operationLogChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
    }

    public static void sendOrderSet(OrderSetProductionLunchMsg msg) {
//        log.info("发送集单的MQ，消息内容:{}", JSONObject.toJSONString(msg));
        orderSetOutputChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
    }

    public static void sendOrderSetBF(OrderSetProductionMsg msg) {
//        log.info("发送【早餐】集单的MQ，消息内容:{}", JSONObject.toJSONString(msg));
        orderSetOutputChannelBF.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
    }

    public static void sendSfOrderSet(OrderSetProductionMsg msg) {
//        log.info("发送派单的MQ，消息内容:{}", JSONObject.toJSONString(msg));
        pushSfOrderOutputChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
    }

    public static void sendWaveOrder(WaveOrderMessage msg) {
//        log.info("发送派单的MQ，消息内容:{}", JSONObject.toJSONString(msg));
        waveOrderOutput.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).setHeader("shopId", msg.getShopId()).build());
    }


}
