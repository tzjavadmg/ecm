package com.milisong.pos.production.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

/**
 * 定义消费队列
 * @author yangzhilong
 *
 */
public interface Sink {
	// 集单单消费
	String ORDER_SET_INPUT = "order_set_input";

	// 配置消费
	String CONFIG_INPUT = "config_input";

	// 配置截单消费
	String CONFIG_INTERCEPT_INPUT = "config_intercept_input";
	
	// 开始生产的集单消费
	String ORDER_SET_START_PRODUCE_INPUT = "order_set_start_produce_input";
	
	String ORDER_INPUT = "order_input";
	
	String ORDER_REFUND_INPUT ="order_refund_input";
	
	String SHOP_INPUT = "shop_input";
	
	@Input(ORDER_SET_INPUT)
	MessageChannel orderSetMsgInput();

	@Input(CONFIG_INPUT)
	MessageChannel configMsgInput();

	@Input(CONFIG_INTERCEPT_INPUT)
	MessageChannel configInterceptInput();
	
	@Input(ORDER_SET_START_PRODUCE_INPUT)
	MessageChannel orderSetStartProduceMsgInput();
	
	@Input(ORDER_INPUT)
	MessageChannel orderInput();
	
	@Input(ORDER_REFUND_INPUT)
	MessageChannel orderRefundInput();
	
	@Input(SHOP_INPUT)
	MessageChannel shopInput();
	
}
