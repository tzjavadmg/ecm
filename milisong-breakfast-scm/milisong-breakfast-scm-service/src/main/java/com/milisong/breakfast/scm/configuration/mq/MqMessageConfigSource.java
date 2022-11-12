package com.milisong.breakfast.scm.configuration.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MqMessageConfigSource {

	String CONFIG_OUTPUT = "config_output";

	String CONFIG_INTERCEPT_OUTPUT = "config_intercept_output";

	String CONFIG_BANNER_OUTPUT = "config_banner_output";

	String CONFIG_INPUT = "config_input";

	String CONFIG_INTERCEPT_INPUT = "config_intercept_input";

	String SHOP_CREATE_INPUT = "shop_create_input";

	@Output(CONFIG_OUTPUT)
	MessageChannel configOutput();

	@Output(CONFIG_INTERCEPT_OUTPUT)
	MessageChannel configInterceptOutput();

	@Output(CONFIG_BANNER_OUTPUT)
	MessageChannel configBannerOutput();

	@Input(CONFIG_INPUT)
	MessageChannel configInput();

	@Input(CONFIG_INTERCEPT_INPUT)
	MessageChannel configInterceptInput();

	@Input(SHOP_CREATE_INPUT)
	MessageChannel shopCreateInput();

}
