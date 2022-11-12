package com.milisong.dms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.AsyncRestTemplate;

/**
 * @author zhaozhonghui
 * @Description
 * @date 2019-02-15
 */
@Configuration
public class AsyncRestTemplateAutoConfig {

    @Bean
    public AsyncRestTemplate asyncRestTemplate() {
        return new AsyncRestTemplate();
    }
}
