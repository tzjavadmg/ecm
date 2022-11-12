package com.mili.oss.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MqMessageSource {
	// 订单消费
	String ORDER_INPUT = "order_input";
	// 操作日志消费
	String OPERATION_LOG_INPUT = "operation_log_input";
	// 订单退款消费
	String ORDER_REFUND_INPUT = "order_refund_input";
	// 顺丰状态的变更的消费
	String SF_STATUS_CHANGE_INPUT = "sf_status_change_input";
	// 消费公司信息
	String OSS_COMPANY_INPUT = "oss_company_input";
	// 集单生成完成的MQ生产
	String ORDER_SET_OUTPUT = "order_set_output";
	// 集单关联订单状态变更
	String ORDER_SET_ORDER_STATUS_CHANGE_OUTPUT = "order_set_order_status_change_output";
	// 消费日志生产
	String OPERATION_LOG_OUTPUT = "operation_log_output";
	// 早餐顺丰派单mq
	String PUSH_SF_ORDER_OUTPUT = "sf_order_set_output";
	
	// 早餐集单生成完成的MQ生产
	String ORDER_SET_OUTPUT_BF = "order_set_output_bf";

	@Input(ORDER_INPUT)
	MessageChannel orderMsgInput();
	
	@Input(ORDER_REFUND_INPUT)
	MessageChannel orderRefundMsgInput();
	
	@Input(OPERATION_LOG_INPUT)
	MessageChannel operationLogInput();
	
	@Input(SF_STATUS_CHANGE_INPUT)
	MessageChannel sfStatusChangeInput();
	
	@Input(OSS_COMPANY_INPUT)
	MessageChannel ossCompanyInput();

	@Output(ORDER_SET_OUTPUT)
	MessageChannel orderSetOutput();
	
	@Output(ORDER_SET_ORDER_STATUS_CHANGE_OUTPUT)
    MessageChannel orderSetOrderStatusChangeOutput();
	
	@Output(OPERATION_LOG_OUTPUT)
	MessageChannel operationLogOutput();
	@Output(PUSH_SF_ORDER_OUTPUT)
	MessageChannel pushSfOrderOutput();
	
	@Output(ORDER_SET_OUTPUT_BF)
	MessageChannel orderSetOutputBf();



	String WAVE_ORDER_OUTPUT = "wave_order_output";


	/**
	 * @description: TODO
	 * @author: codyzeng@163.com
	 * @date: 2019/5/17 18:21
	 * @param: []
	 * @return: org.springframework.messaging.MessageChannel
	 */
	@Output(WAVE_ORDER_OUTPUT)
	MessageChannel waveOrderOutput();


}
