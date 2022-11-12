package com.milisong.breakfast.scm.order.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

public interface MqMessageOrderSource {
	// 订单消费
	String ORDER_INPUT = "order_input";
	// 订单退款消费
	String ORDER_REFUND_INPUT = "order_refund_input";
		
	@Input(ORDER_INPUT)
	MessageChannel orderMsgInput();
	
	@Input(ORDER_REFUND_INPUT)
	MessageChannel orderRefundMsgInput();
}
