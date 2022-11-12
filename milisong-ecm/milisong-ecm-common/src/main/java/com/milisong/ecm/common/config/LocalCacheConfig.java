package com.milisong.ecm.common.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.milisong.ecm.common.dto.ShopConfigDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @ Description：
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/13 14:46
 */
@Configuration
public class LocalCacheConfig {

    @Value("${local-cache.expired-time}")
    private long expireTime = 60 * 5;

    @Bean
    public Cache<String, ShopConfigDto> localCache(){
        return CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(expireTime, TimeUnit.SECONDS)
                .build();
    }
}
