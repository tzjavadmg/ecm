package com.milisong.scm.orderset.util;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.milisong.scm.orderset.mq.OrderSetOrderStatusChangeMsg;
import com.milisong.scm.orderset.mq.OrderSetProductionMsg;

import lombok.extern.slf4j.Slf4j;

/**
 * MQ生产者帮助类
 * @author yangzhilong
 *
 */
@Slf4j
public class MqProducerUtil {
	private static MessageChannel orderSetStatusChannel;
	
	private static MessageChannel orderSetOutputChannel;
	
	public static void setOrderSetChannel (MessageChannel channel){
		MqProducerUtil.orderSetStatusChannel = channel;
	}
	
	public static void setOrderSetOutputChannel (MessageChannel channel){
		MqProducerUtil.orderSetOutputChannel = channel;
	}
	
	public static void sendOrderSetOrderStatusMsg(OrderSetOrderStatusChangeMsg msg) {
		log.info("发送集单里状态变更的MQ，消息内容：{}", JSONObject.toJSONString(msg));
		orderSetStatusChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
	}
	
	public static void sendOrderSet(OrderSetProductionMsg msg) {
		log.info("发送集单的MQ，消息内容:{}", JSONObject.toJSONString(msg));
		orderSetOutputChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
	}
}
