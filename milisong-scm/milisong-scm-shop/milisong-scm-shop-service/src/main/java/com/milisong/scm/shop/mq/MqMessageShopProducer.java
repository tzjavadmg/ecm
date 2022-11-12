package com.milisong.scm.shop.mq;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import com.milisong.scm.shop.util.MqProducerUtil;

@EnableBinding(MqMessageShopSource.class)
public class MqMessageShopProducer {
	@Autowired
    @Output(MqMessageShopSource.COMPANY_B_2_C)
    private MessageChannel companyB2cChannel;
	@Autowired
	@Output(MqMessageShopSource.BUILDING_OUTPUT)
	private MessageChannel buildingChannel;
	@Autowired
	@Output(MqMessageShopSource.CONFIG_OUTPUT)
	private MessageChannel configChannel;
	@Autowired
	@Output(MqMessageShopSource.CONFIG_INTERCEPT_OUTPUT)
	private MessageChannel configInterceptChannel;
	@Autowired
	@Output(MqMessageShopSource.CONFIG_BANNER_OUTPUT)
	private MessageChannel configBannerChannel;
	@Autowired
	@Output(MqMessageShopSource.COMPANY_MODIFY_OUTPUT)
	private MessageChannel companyModifyOutput;
	
	@PostConstruct
	public void postConstruct() {
		MqProducerUtil.setCompanyB2cChannel(companyB2cChannel);
		MqProducerUtil.setBuildingChannel(buildingChannel);
		MqProducerUtil.setConfigChannel(configChannel);
		MqProducerUtil.setConfigBannerChannel(configBannerChannel);
		MqProducerUtil.setConfigInterceptChannel(configInterceptChannel);
		MqProducerUtil.setCompanyModifyChannel(companyModifyOutput);
	}
}
