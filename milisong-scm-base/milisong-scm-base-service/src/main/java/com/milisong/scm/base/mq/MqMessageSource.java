package com.milisong.scm.base.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MqMessageSource {
	// 操作日志消费
	String OPERATION_LOG_INPUT = "operation_log_input";
	
	// 消费日志生产
	String OPERATION_LOG_OUTPUT = "operation_log_output";
	
	// 发送给POS的门店信息
	String SHOP_OUTPUT = "shop_info_output";
	// 门店创建的事件
	String SHOP_CREATE_OUTPUT = "shop_create_output";
	// 公司信息的MQ生产
	String COMPANY_OUTPUT = "company_output";


		
	@Input(OPERATION_LOG_INPUT)
	MessageChannel operationLogInput();
	
	@Output(OPERATION_LOG_OUTPUT)
	MessageChannel operationLogOutput();
	
	@Output(SHOP_OUTPUT)
	MessageChannel shopOutput();
	
	@Output(SHOP_CREATE_OUTPUT)
	MessageChannel shopCreateOutput();

	@Output(COMPANY_OUTPUT)
	MessageChannel companyOutput();
}
