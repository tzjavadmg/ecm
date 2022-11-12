package com.milisong.breakfast.scm.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "oms")
@Component
public class OmsOrderCheckProperties {
	/**
	 * 检查订单url
	 */
	private String checkUrl;
	/**
	 * 通知url
	 */
	private String notifyUrl;
}
