package com.tomato.mca.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "sys")
@Setter
@Getter
@Component
@RefreshScope
public class SysProperties {

    private String period;
    private String maxPeriod;
    private String maxAttempts;


}
