package com.milisong.scm.orderset.mq;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import com.milisong.scm.orderset.util.MqProducerUtil;

@EnableBinding(MqMessageSource.class)
public class MqMessageProducer {
	@Autowired
    @Output(MqMessageSource.ORDER_SET_ORDER_STATUS_CHANGE_OUTPUT)
    private MessageChannel orderSetchannel;
	@Autowired
	@Output(MqMessageSource.ORDER_SET_OUTPUT)
	private MessageChannel orderSetOutputChannel;;
	
	@PostConstruct
	public void postConstruct() {
		MqProducerUtil.setOrderSetChannel(orderSetchannel);
		MqProducerUtil.setOrderSetOutputChannel(orderSetOutputChannel);
	}
}
