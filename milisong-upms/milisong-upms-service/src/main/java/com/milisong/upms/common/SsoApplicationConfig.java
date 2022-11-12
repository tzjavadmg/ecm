package com.milisong.upms.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年11月8日---下午4:10:45
*
*/
@Data
@Configuration
@ConfigurationProperties(prefix = "sso.config")
public class SsoApplicationConfig {
	
	private Map<String, SsoConfig> app = new HashMap<>();
	
	// 缓存失效时间（单位：分钟）
	private Integer cacheTimeOutWithMinute;
}
