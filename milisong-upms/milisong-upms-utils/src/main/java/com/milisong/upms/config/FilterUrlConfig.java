package com.milisong.upms.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author zhaozhonghui
 * @date 2018-11-08
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "upms.filter-url")
public class FilterUrlConfig {

    // 非在http head里传递token的urls
    private List<String> notHeadTokenUrls;
    private String tokenUrl;
    private List<String> whitelistUrls;

}
