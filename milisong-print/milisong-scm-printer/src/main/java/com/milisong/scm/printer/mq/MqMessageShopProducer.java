package com.milisong.scm.printer.mq;

import javax.annotation.PostConstruct;

import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(MqMessageShopSource.class)
public class MqMessageShopProducer {
	
	@PostConstruct
	public void postConstruct() {
//		MqProducerUtil.setCompanyB2cChannel(companyB2cChannel);
//		MqProducerUtil.setBuildingChannel(buildingChannel);
//		MqProducerUtil.setConfigChannel(configChannel);
	}
}
