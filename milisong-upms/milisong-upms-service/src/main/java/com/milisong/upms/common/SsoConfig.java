package com.milisong.upms.common;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * sso配置
 * 
 * Title: SsoConfig<br> 
 * Description: SsoConfig<br> 
 * CreateDate:2017年11月20日 下午3:12:18 
 * @author woody
 */
@Getter
@Setter
public class SsoConfig {
	@NotBlank
	private String appName;
	@NotBlank
	private String appKey;
	@NotBlank
	private String appSercet;
}