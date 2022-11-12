package com.milisong.pos.production.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 延迟队列的配置信息
 * @author yangzhilong
 *
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="dq")
public class DqProperties {
	// 域名
	private String host;
	// 设置延迟
	private String delayUrl;
	// 获取剩余延迟
	private String expireTimeUrl;
	// 暂停计时
	private String pauseUrl;
	// 取消计时
	private String cancelUrl;
	
	// 超时回调地址
	private String callbackUrl;
}
