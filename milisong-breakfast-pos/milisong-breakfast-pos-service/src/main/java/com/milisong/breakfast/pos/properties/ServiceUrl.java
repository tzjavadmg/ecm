package com.milisong.breakfast.pos.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix="scm.init")
public class ServiceUrl {
	/**
	 * 初始化预生产量
	 */
	private String preProductionUrl;
	
	/**
	 * 集单的task 里的线程池销毁
	 */
	private String dayPreproductionUrl;

	/**
	 * 楼宇信息
	 */
	private String dayBuildingUrl;

	/**
	 * 公司信息
	 */
	private String queryCompanyUrl;
	 
}
