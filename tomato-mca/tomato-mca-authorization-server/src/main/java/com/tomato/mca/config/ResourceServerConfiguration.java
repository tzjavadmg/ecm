package com.tomato.mca.config;


import feign.Retryer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResourceServerConfiguration {

    @Autowired
    SysProperties sysProperties;

    //开启重试
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(Long.parseLong(sysProperties.getPeriod()), Long.parseLong(sysProperties.getMaxPeriod()), Integer.parseInt(sysProperties.getMaxAttempts()));
    }

}
