package com.milisong.oms.configruation;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.milisong.oms.service.config.BusinessLineConfigService;
import com.milisong.oms.service.config.BusinessLineConfigServiceImpl;
import com.milisong.oms.util.SysConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @ Description：自动装配
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/4 14:27
 */
@Configuration
public class AutoConfigruation {
    @Bean
    SysConfigUtils sysConfigUtils(BusinessLineConfigService businessLineConfigService, SystemProperties systemProperties) {
        SysConfigUtils sysConfigUtils = new SysConfigUtils();
        SysConfigUtils.setBusinessLineConfigService(businessLineConfigService);
        SysConfigUtils.setSystemProperties(systemProperties);
        return sysConfigUtils;
    }

    @Bean
    BusinessLineConfigService businessLineConfigService(SystemProperties systemProperties) {
        BusinessLineConfigServiceImpl businessLineConfigService = new BusinessLineConfigServiceImpl();
        Cache<String, BusinessLineConfigService.BusinessLineConfig> cache = CacheBuilder.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(systemProperties.getLocalCache().getExpireTime(), TimeUnit.SECONDS)
                .build();
        businessLineConfigService.setCache(cache);
        return businessLineConfigService;
    }
}
