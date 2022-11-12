package com.milisong.scm.orderset.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MqMessageSource {
	// 订单消费
	String ORDER_INPUT = "order_input";
	// 订单退款消费
	String ORDER_REFUND_INPUT = "order_refund_input";
//	// 顺丰状态的变更的消费
//	String SF_STATUS_CHANGE_INPUT = "sf_status_change_input";
	
	// 集单生成完成的MQ生产
	String ORDER_SET_OUTPUT = "order_set_output";
	// 集单关联订单状态变更
	String ORDER_SET_ORDER_STATUS_CHANGE_OUTPUT = "order_set_order_status_change_output";
	
	@Input(ORDER_INPUT)
	MessageChannel orderMsgInput();
	
	@Input(ORDER_REFUND_INPUT)
	MessageChannel orderRefundMsgInput();
	
//	@Input(SF_STATUS_CHANGE_INPUT)
//	MessageChannel sfStatusChangeInput();
	
	
	@Output(ORDER_SET_OUTPUT)
	MessageChannel orderSetOutput();
	
	@Output(ORDER_SET_ORDER_STATUS_CHANGE_OUTPUT)
    MessageChannel orderSetOrderStatusChangeOutput();
}
