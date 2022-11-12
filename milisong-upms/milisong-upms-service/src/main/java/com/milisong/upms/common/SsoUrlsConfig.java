package com.milisong.upms.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="sso.url")
@Getter
@Setter
public class SsoUrlsConfig {
	// 验证ticket
	private String validateTicket;
	// 菜单树
	private String menuTree;
	// 获取登陆用户
	private String loginUserInfo;
	// 用户信息
	private String userInfo;
	// 用户权限
	private String userPermission;
	// 用户数据权限
	private String userDataPermission;
}
