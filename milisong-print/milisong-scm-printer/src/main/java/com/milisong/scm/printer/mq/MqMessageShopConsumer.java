package com.milisong.scm.printer.mq;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import com.alibaba.fastjson.JSONObject;
import com.milisong.scm.printer.api.PosPrintService;
import com.milisong.scm.printer.properties.ServiceConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableBinding(MqMessageShopSource.class)
public class MqMessageShopConsumer {
	
	@Autowired
	ServiceConfig serviceConfig;
	
	@Autowired
	PosPrintService posPrintServiceImpl;
	/**
	 * 消费C端配送信息中的公司
	 * @param message
	 */
	@StreamListener(MqMessageShopSource.POS_PRINT_INPUT)
	public void company(Message<String> message) {
		log.info("开始消费POS端的 集单打印信息POS_PRINT_INPUT，参数：{}", JSONObject.toJSONString(message));
		MessageHeaders head = message.getHeaders();
		String shopId = head.get("shopId").toString();
		log.info("shopId:{}",shopId);
		log.info("当前门店ID:{}",serviceConfig.getShopId());
		String payload = message.getPayload();
		if(StringUtils.isNotBlank(payload)) {
			 if(serviceConfig.getShopId().equals(shopId)) {
				 posPrintServiceImpl.printOrdeSet(payload,-1);
			 }
		} else {
			log.warn("收到POS端的 集单打印信！！！");
		}
	}
	
	@StreamListener(MqMessageShopSource.POS_PRINT_BREAKFAST_INPUT)
	public void printBreakfast(Message<String> message) {
		log.info("开始消费POS端的 集单打印信息POS_PRINT_BREAKFAST_INPUT，参数：{}", JSONObject.toJSONString(message));
		MessageHeaders head = message.getHeaders();
		String shopId = head.get("shopId").toString();
		log.info("shopId:{}",shopId);
		log.info("当前门店ID:{}",serviceConfig.getShopId());
		String payload = message.getPayload();
		if(StringUtils.isNotBlank(payload)) {
			 if(serviceConfig.getShopId().equals(shopId)) {
				 // 打印类型 -1 顾客、配送联都要打印
				 posPrintServiceImpl.printOrdeBreakfastSet(payload,-1);
			 }
		} else {
			log.warn("收到POS端的 集单打印信！！！");
		}
	}
	
}
