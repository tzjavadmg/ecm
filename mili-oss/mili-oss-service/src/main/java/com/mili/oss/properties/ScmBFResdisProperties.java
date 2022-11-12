package com.mili.oss.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 集单服务接口地址
 */
@Data
@ConfigurationProperties(prefix = "scm.sys")
@Component
public class ScmBFResdisProperties {
	/**
	 * redis -get
	 */
	private String scmBfRedisUrl;
	/**
	 * redis -set
	 */
	private String scmBfRedisUrlset;
	/**
	 * redis -incr
	 */
	private String scmBfRedisUrlincr;
}
