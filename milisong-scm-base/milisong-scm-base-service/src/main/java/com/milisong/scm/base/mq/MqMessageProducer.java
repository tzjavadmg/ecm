package com.milisong.scm.base.mq;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

@EnableBinding(MqMessageSource.class)
public class MqMessageProducer {
	@Autowired
    @Output(MqMessageSource.OPERATION_LOG_OUTPUT)
    private MessageChannel opLogChannel;
	
	@Autowired
	@Output(MqMessageSource.SHOP_OUTPUT)
	private MessageChannel shopOutput;
	
	@Autowired
	@Output(MqMessageSource.SHOP_CREATE_OUTPUT)
	private MessageChannel shopCreateOutput;

	@Autowired
	@Output(MqMessageSource.COMPANY_OUTPUT)
	private MessageChannel companyChannel;

	@PostConstruct
	public void postConstruct() {
		MqProducerUtil.setOperationLogChannel(opLogChannel);
		MqProducerUtil.setShopChannel(shopOutput);
		MqProducerUtil.setShopCreateChannel(shopCreateOutput);
		MqProducerUtil.setCompanyChannel(companyChannel);
	}
}
