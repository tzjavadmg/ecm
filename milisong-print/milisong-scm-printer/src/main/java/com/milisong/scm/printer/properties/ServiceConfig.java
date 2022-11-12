package com.milisong.scm.printer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="shopservice")
public class ServiceConfig {
	/**
	 * 当前门店ID
	 */
	private String shopId;
	
	/**
	 * 日志上报地址
	 */
	private String logUrl;
	
	 
}
