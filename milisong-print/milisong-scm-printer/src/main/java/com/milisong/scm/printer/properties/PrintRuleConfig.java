package com.milisong.scm.printer.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="printrule.bf")
public class PrintRuleConfig {
	
	/**
	 *  单品最大值
	 */
	private Integer singleMax;
	/**
	 *  套餐最大值
	 */
	private Integer packageMax;
}
