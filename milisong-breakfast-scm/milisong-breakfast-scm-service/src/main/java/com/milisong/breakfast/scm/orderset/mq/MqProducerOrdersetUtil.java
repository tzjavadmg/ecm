package com.milisong.breakfast.scm.orderset.mq;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * MQ生产者帮助类
 * @author yangzhilong
 *
 */
@Slf4j
public class MqProducerOrdersetUtil {
	private static MessageChannel orderSetStatusChannel;
		
	private static MessageChannel orderSetOutputChannel;

	private static MessageChannel pushSfOrderOutputChannel;
	
	public static void setOrderSetChannel (MessageChannel channel){
		MqProducerOrdersetUtil.orderSetStatusChannel = channel;
	}
	
	public static void setOrderSetOutputChannel (MessageChannel channel){
		MqProducerOrdersetUtil.orderSetOutputChannel = channel;
	}

	public static void setPushSfOrderOutputChannel(MessageChannel channel){
		MqProducerOrdersetUtil.pushSfOrderOutputChannel = channel;
	}
	
	public static void sendOrderSetOrderStatusMsg(OrderSetOrderStatusChangeMsg msg) {
		log.info("发送集单里状态变更的MQ，消息内容：{}", JSONObject.toJSONString(msg));
		orderSetStatusChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
	}
	
	public static void sendOrderSet(OrderSetProductionMsg msg) {
		log.info("发送集单的MQ，消息内容:{}", JSONObject.toJSONString(msg));
		orderSetOutputChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
	}

	public static void sendSfOrderSet(OrderSetProductionMsg msg){
		log.info("发送派单的MQ，消息内容:{}", JSONObject.toJSONString(msg));
		pushSfOrderOutputChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
	}
}
