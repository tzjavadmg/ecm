package com.milisong.breakfast.scm.configuration.mq;

import com.milisong.breakfast.scm.configuration.util.MqProducerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import javax.annotation.PostConstruct;

@EnableBinding(MqMessageConfigSource.class)
public class MqMessageConfigProducer {
	@Autowired
	@Output(MqMessageConfigSource.CONFIG_OUTPUT)
	private MessageChannel configChannel;
	@Autowired
	@Output(MqMessageConfigSource.CONFIG_INTERCEPT_OUTPUT)
	private MessageChannel configInterceptChannel;
	@Autowired
	@Output(MqMessageConfigSource.CONFIG_BANNER_OUTPUT)
	private MessageChannel configBannerChannel;
	@Autowired

	@PostConstruct
	public void postConstruct() {
		MqProducerUtil.setConfigChannel(configChannel);
		MqProducerUtil.setConfigBannerChannel(configBannerChannel);
		MqProducerUtil.setConfigInterceptChannel(configInterceptChannel);
	}
}
