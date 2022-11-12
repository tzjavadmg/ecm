package com.milisong.ecm.lunch.utils;

import com.farmland.core.cache.RedisCache;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class GlobalConfigUtil {

    private static final String GLOBAL_CONFIG_KEY = "config:global";

    public static String getOrderWarnEndTime() {
        return getCacheValue("orderWarnEndTime");
    }

    private static String getCacheValue(String key) {
        Object result = RedisCache.hGet(GLOBAL_CONFIG_KEY, key);
        if (result == null) {
            log.error("没有拿到配置信息->{}", key);
            return null;
        } else {
            return result.toString();
        }
    }


}
