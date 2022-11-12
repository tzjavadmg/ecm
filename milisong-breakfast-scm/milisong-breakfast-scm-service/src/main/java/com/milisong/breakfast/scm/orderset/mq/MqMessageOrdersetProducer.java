package com.milisong.breakfast.scm.orderset.mq;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

@EnableBinding(MqMessageOrdersetSource.class)
public class MqMessageOrdersetProducer {
	@Autowired
    @Output(MqMessageOrdersetSource.ORDER_SET_ORDER_STATUS_CHANGE_OUTPUT)
    private MessageChannel orderSetchannel;
	@Autowired
	@Output(MqMessageOrdersetSource.ORDER_SET_OUTPUT)
	private MessageChannel orderSetOutputChannel;
	@Autowired
	@Output(MqMessageOrdersetSource.PUSH_SF_ORDER_OUTPUT)
	private MessageChannel pushSfORderOutputChannel;
	
	@PostConstruct
	public void postConstruct() {
		MqProducerOrdersetUtil.setOrderSetChannel(orderSetchannel);
		MqProducerOrdersetUtil.setOrderSetOutputChannel(orderSetOutputChannel);
		MqProducerOrdersetUtil.setPushSfOrderOutputChannel(pushSfORderOutputChannel);
	}
}
