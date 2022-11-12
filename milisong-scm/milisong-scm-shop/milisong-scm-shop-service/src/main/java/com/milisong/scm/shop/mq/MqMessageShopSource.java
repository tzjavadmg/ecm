package com.milisong.scm.shop.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MqMessageShopSource {
	// C端发过来的公司信息
	String COMPANY_C_2_B = "company_c_2_b";
	// 发送给C端的公司信息变更
	String COMPANY_B_2_C = "company_b_2_c";
	// 发送给C端的楼宇信息
	String BUILDING_OUTPUT = "building_output";
	// 楼宇申请记录
	String BUILDING_APPLY = "building_apply";

	String CONFIG_OUTPUT = "config_output";

	String CONFIG_INTERCEPT_OUTPUT = "config_intercept_output";

	String CONFIG_BANNER_OUTPUT = "config_banner_output";

	String COMPANY_MODIFY_OUTPUT = "company_modify_output";

	String CONFIG_INPUT = "config_input";

	String CONFIG_INTERCEPT_INPUT = "config_intercept_input";

	String SHOP_CREATE_INPUT = "shop_create_input";

	@Input(COMPANY_C_2_B)
	MessageChannel companyMsgInput();
	
	@Output(COMPANY_B_2_C)
    MessageChannel companyMsgOutput();
	
	@Output(BUILDING_OUTPUT)
	MessageChannel buildingMsgOutput();
	
	@Input(BUILDING_APPLY)
	MessageChannel buildingApplyMsgInput();

	@Output(CONFIG_OUTPUT)
	MessageChannel configMsgOutput();

	@Output(CONFIG_INTERCEPT_OUTPUT)
	MessageChannel configInterceptOutput();

	@Output(CONFIG_BANNER_OUTPUT)
	MessageChannel configBannerOutput();

	@Output(COMPANY_MODIFY_OUTPUT)
	MessageChannel companyModifyOutput();

	@Input(CONFIG_INPUT)
	MessageChannel configInput();

	@Input(CONFIG_INTERCEPT_INPUT)
	MessageChannel configInterceptInput();

	@Input(SHOP_CREATE_INPUT)
	MessageChannel shopCreateInput();

}
