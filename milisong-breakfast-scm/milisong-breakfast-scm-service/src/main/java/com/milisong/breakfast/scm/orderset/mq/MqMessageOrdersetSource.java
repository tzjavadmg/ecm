package com.milisong.breakfast.scm.orderset.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MqMessageOrdersetSource {
	// 顺丰状态的变更的消费
//	String SF_STATUS_CHANGE_INPUT = "sf_status_change_input";
	
	// 集单生成完成的MQ生产
	String ORDER_SET_OUTPUT = "order_set_output";
	// 集单关联订单状态变更
	String ORDER_SET_ORDER_STATUS_CHANGE_OUTPUT = "order_set_order_status_change_output";

	String PUSH_SF_ORDER_OUTPUT = "sf_order_set_output";
	
//	@Input(SF_STATUS_CHANGE_INPUT)
//	MessageChannel sfStatusChangeInput();
	
	@Output(ORDER_SET_OUTPUT)
	MessageChannel orderSetOutput();
	
	@Output(ORDER_SET_ORDER_STATUS_CHANGE_OUTPUT)
    MessageChannel orderSetOrderStatusChangeOutput();

	@Output(PUSH_SF_ORDER_OUTPUT)
	MessageChannel pushSfOrderOutput();
}
