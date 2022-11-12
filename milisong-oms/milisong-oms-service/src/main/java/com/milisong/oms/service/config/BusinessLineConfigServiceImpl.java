package com.milisong.oms.service.config;

import com.farmland.core.cache.RedisCache;
import com.google.common.cache.Cache;
import com.milisong.oms.constant.ConfigItem;
import com.milisong.oms.util.OrderRedisKeyUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @ Description：配置项服务
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/4 13:41
 */
@Slf4j
@Setter
public class BusinessLineConfigServiceImpl implements BusinessLineConfigService {


    Cache<String, BusinessLineConfig> cache;

    public BusinessLineConfig getConfig() {
        return getConfig(null);
    }

    public BusinessLineConfig getConfig(Byte businessLine) {
        String shopConfigCacheKey = null;

        if (businessLine != null) {
            shopConfigCacheKey = OrderRedisKeyUtils.getConfigKey(businessLine);
        } else {
            shopConfigCacheKey = OrderRedisKeyUtils.GLOBAL_CONFIG;
        }
        BusinessLineConfig businessLineConfig = null;
        try {
            businessLineConfig = cache.get(shopConfigCacheKey, new Callable<BusinessLineConfig>() {
                @Override
                public BusinessLineConfig call() throws Exception {
                    Map<Object, Object> globalConfigMap = RedisCache.hGetAll(OrderRedisKeyUtils.GLOBAL_CONFIG);
                    Map<Object, Object> businessLineConfigMap = null;
                    if (businessLine != null) {
                        businessLineConfigMap = RedisCache.hGetAll(OrderRedisKeyUtils.getConfigKey(businessLine));
                    }
                    return getBusinessLineConfig(globalConfigMap, businessLineConfigMap);
                }
            });
        } catch (ExecutionException e) {
            log.error("===========获取缓存配置异常===========", e);
        }
        return businessLineConfig;
    }

    private BusinessLineConfig getBusinessLineConfig(Map<Object, Object> globalConfigMap, Map<Object, Object> businessLineConfigMap) {
        BusinessLineConfig businessLineConfig = new BusinessLineConfig();
        //未支付订单过期时间
        Integer unPayExpiredTime = mergeShopConfigIntegerValue(globalConfigMap, businessLineConfigMap, ConfigItem.UN_PAY_EXPIRED_TIME.getValue());
        businessLineConfig.setUnPayExpiredTime(unPayExpiredTime);

        return businessLineConfig;
    }

    private Integer mergeShopConfigIntegerValue(Map<Object, Object> globalConfig, Map<Object, Object> shopConfig, String value) {
        Integer globalConfigValue = MapUtils.getInteger(globalConfig, value);
        Integer shopConfigValue = MapUtils.getInteger(shopConfig, value);
        return shopConfigValue != null ? shopConfigValue : globalConfigValue;
    }
}
