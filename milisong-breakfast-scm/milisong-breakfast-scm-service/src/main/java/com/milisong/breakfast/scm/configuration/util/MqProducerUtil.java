package com.milisong.breakfast.scm.configuration.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.milisong.breakfast.scm.configuration.dto.ConfigDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

/**
 * MQ生产者帮助类
 * @author yangzhilong
 *
 */
@Slf4j
public class MqProducerUtil {
	private static MessageChannel configChannel;
	private static MessageChannel configInterceptChannel;
	private static MessageChannel configBannerChannel;

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

	public static void sendConfig(ConfigDto<?> msg) {
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

}
