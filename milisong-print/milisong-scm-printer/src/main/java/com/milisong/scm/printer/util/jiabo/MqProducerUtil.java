package com.milisong.scm.printer.util.jiabo;

import org.springframework.messaging.MessageChannel;

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

	public static void setConfigChannel(MessageChannel channel) {
		MqProducerUtil.configChannel = channel;
	}

	public static void setCompanyB2cChannel (MessageChannel channel){
		MqProducerUtil.companyB2cChannel = channel;
	}
	public static void setBuildingChannel (MessageChannel channel){
		MqProducerUtil.buildingChannel = channel;
	}
	
}
