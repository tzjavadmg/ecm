package com.milisong.breakfast.pos.mq;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import com.milisong.breakfast.pos.util.MqSendUtil;

@EnableBinding(Source.class)
public class MqMessageProducer {
	/**
	 * 集单开始生产
	 */
	@Autowired
    @Output(Source.ORDER_SET_START_PRODUCE_OUTPUT)
    private MessageChannel orderSetStartProduceChannel;

	
	@PostConstruct
	public void postConstruct() {
		MqSendUtil.setOrderSetPrintChannel(orderSetStartProduceChannel);
	}
}
