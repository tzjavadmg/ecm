package com.milisong.scm.shop.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.milisong.scm.shop.dto.building.BuildingDto;
import com.milisong.scm.shop.dto.building.CompanyDto;
import com.milisong.scm.shop.dto.building.CompanyModifyDto;
import com.milisong.scm.shop.dto.config.ConfigDto;

import lombok.extern.slf4j.Slf4j;

/**
 * MQ生产者帮助类
 * @author yangzhilong
 *
 */
@Slf4j
public class MqProducerUtil {
	private static MessageChannel companyB2cChannel;
	private static MessageChannel buildingChannel;
	private static MessageChannel configChannel;
	private static MessageChannel configInterceptChannel;
	private static MessageChannel configBannerChannel;
	private static MessageChannel companyModifyChannel;

	private static final String POINT = ".";
	private static final String CONFIG_ROUTING_KEY = "key";


	public static void setConfigChannel(MessageChannel channel) {
		MqProducerUtil.configChannel = channel;
	}

	public static void setConfigInterceptChannel(MessageChannel configInterceptChannel) {
		MqProducerUtil.configInterceptChannel = configInterceptChannel;
	}

	public static void setConfigBannerChannel(MessageChannel configBannerChannel) {
		MqProducerUtil.configBannerChannel = configBannerChannel;
	}

	public static void setCompanyB2cChannel (MessageChannel channel){
		MqProducerUtil.companyB2cChannel = channel;
	}
	public static void setBuildingChannel (MessageChannel channel){
		MqProducerUtil.buildingChannel = channel;
	}
	public static void setCompanyModifyChannel(MessageChannel channel) {
		MqProducerUtil.companyModifyChannel = channel;
	}

	public static void sendCompanyMsg(CompanyDto msg) {
		log.info("发送公司信息的MQ，消息内容：{}", JSONObject.toJSONString(msg));
		companyB2cChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
	}
	
	public static void sendBuildingMsg(BuildingDto msg) {
		log.info("发送楼宇信息的MQ，消息内容：{}", JSONObject.toJSONString(msg));
		buildingChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
	}

	public static void sendConfigMsg(ConfigDto<?> msg) {
		log.info("发送配置信息的MQ，消息内容：{}", JSONObject.toJSONString(msg));
        String lineName = StringUtils.join(msg.getServiceLine(),POINT);
        msg.setServiceLine(null);
        Message<String> message = MessageBuilder.withPayload(JSON.toJSONString(msg)).setHeader(CONFIG_ROUTING_KEY,lineName).build();
        log.debug("查看发送配置MQ详情---{}",JSON.toJSONString(message));
		configChannel.send(message);
	}

	public static void sendConfigInterceptMsg(ConfigDto<?> msg) {
		log.info("发送配置截单时间信息MQ，消息内容：{}", JSONObject.toJSONString(msg));
		configInterceptChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
	}

	public static void sendConfigBannerMsg(ConfigDto<?> msg) {
		log.info("发送配置Banner图信息MQ，消息内容：{}", JSONObject.toJSONString(msg));
		configBannerChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
	}

	public static void sendCompanyModifyMsg(CompanyModifyDto msg) {
		log.info("发送公司楼层修改信息MQ，消息内容：{}", JSONObject.toJSONString(msg));
		companyModifyChannel.send(MessageBuilder.withPayload(JSON.toJSONString(msg)).build());
	}
}
