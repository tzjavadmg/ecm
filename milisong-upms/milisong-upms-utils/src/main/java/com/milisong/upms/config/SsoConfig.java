package com.milisong.upms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * sso配置
 * <p>
 * Title: SsoConfig<br>
 * Description: SsoConfig<br>
 * CreateDate:2017年11月20日 下午3:12:18
 *
 * @author woody
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "upms.config")
public class SsoConfig implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // 应用名称
    private String appName;
    // upms获取用户信息的url
    private String upmsUserInfoUrl;
    // 获取token url
    private String upmsTokenUrl;
    // 检查登录 url
    private String upmsCheckLoginUrl;
    // 校验权限url
    private String upmsCheckPermissionUrl;
    // 获取用户菜单url
    private String upmsGetMenuListUrl;
    // 获取用户数据权限
    private String upmsGetDataPermissionUrl;

}