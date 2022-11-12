package com.milisong.scm.printer.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MqMessageShopSource {
	// C端发过来的公司信息
	String POS_PRINT_INPUT = "pos_print_input";
	 

	@Input(POS_PRINT_INPUT)
	MessageChannel posPrintInput();
	
	// C端发过来的公司信息
	String POS_PRINT_BREAKFAST_INPUT = "pos_print_breakfast_input";
	 

	@Input(POS_PRINT_BREAKFAST_INPUT)
	MessageChannel posPrintBreakfastInput();	
	 
}
