package com.milisong.scm.base.mq;

import com.milisong.scm.base.dto.mq.CompanyMqDto;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.milisong.scm.base.dto.mq.OperationLogDto;
import com.milisong.scm.base.dto.mq.ShopCreateMqDto;
import com.milisong.scm.base.dto.mq.ShopMqDto;

import lombok.extern.slf4j.Slf4j;

/**
 * MQ生产者帮助类
 * @author yangzhilong
 *
 */
@Slf4j
public class MqProducerUtil {
	
	private static MessageChannel operationLogChannel;
	
	private static MessageChannel shopChannel;
	
	private static MessageChannel shopCreateChannel;

	private static MessageChannel companyChannel;
		
	public static void setOperationLogChannel (MessageChannel channel){
		MqProducerUtil.operationLogChannel = channel;
	}
	
	public static void setShopChannel(MessageChannel shopChannel) {
		MqProducerUtil.shopChannel = shopChannel;
	}
	
	public static void setShopCreateChannel(MessageChannel shopCreateChannel) {
		MqProducerUtil.shopCreateChannel = shopCreateChannel;
	}
	public static void setCompanyChannel(MessageChannel companyChannel) {
		MqProducerUtil.companyChannel = companyChannel;
	}
	
	public static void sendOperationLog(OperationLogDto msg) {
		log.info("发送操作日志的MQ，消息内容:{}", JSONObject.toJSONString(msg));
		operationLogChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
	}
	
	public static void sendShopInfoMsg(ShopMqDto msg) {
		log.info("发送门店修改信息MQ，消息内容：{}", JSONObject.toJSONString(msg));
		shopChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
	}
	
	public static void sendShopCreateMsg(ShopCreateMqDto msg) {
		log.info("发送门店创建事件MQ，消息内容：{}", JSONObject.toJSONString(msg));
		shopCreateChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
	}

	public static void sendCompanyInfoMsg(CompanyMqDto msg) {
		log.info("发送公司修改信息MQ，消息内容：{}", JSONObject.toJSONString(msg));
		companyChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
	}
}
