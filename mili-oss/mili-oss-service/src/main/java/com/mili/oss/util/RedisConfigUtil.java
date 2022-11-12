package com.mili.oss.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.alibaba.fastjson.JSON;
import com.farmland.core.cache.RedisCache;
import com.mili.oss.dto.config.ShopInterceptConfigDto;
import com.mili.oss.dto.config.ShopSingleConfigDto;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/29   12:52
 *    desc    : 配置相关redis工具类
 *    version : v1.0
 * </pre>
 */

public class RedisConfigUtil {

    private static StringRedisTemplate redisTemplate = RedisCache.getRedisTemplate();

    private static final String SHOP_INTERCEPT_CONFIG_KEY = "config:intercept_config:scm:%s";
    private static final String SHOP_SINGLE_CONFIG_KEY = "config:shop_single_config:scm:%s";
    private static final String GLOBAL_CONFIG_KEY = "config:global_config:scm";
    private static final String OPERATE_ADD = "add";
    private static final String OPERATE_UPDATE = "update";
    private static final String OPERATE_DELETE = "delete";
    private static final String OPERATE_SYNC = "sync";

    private static final String SHOP_PRINTER = "shop_printer:%s";

    public static Map<String, String> getShopPrinter(Long shopId) {
        String key = String.format(SHOP_PRINTER, shopId);
        Map<Object, Object> printerMap = RedisCache.hGetAll(key);

        Map<String, String> printerConfigMap = Maps.newHashMap();
        printerMap.entrySet().stream().forEach(entry -> {
            printerConfigMap.put((String) entry.getKey(), (String) entry.getValue());
        });
        return printerConfigMap;
    }

    public static void cacheShopIntercept(Long shopId, List<ShopInterceptConfigDto> list) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(String.format(SHOP_INTERCEPT_CONFIG_KEY, shopId.toString()), JSON.toJSONString(list));
    }

    public static void cacheGlobalConfig(Map<String, String> map, String operation) {
        if (map == null || map.size() == 0) {
            return;
        }
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        switch (operation) {
            case OPERATE_ADD:
                operations.putAll(GLOBAL_CONFIG_KEY, map);
                break;
            case OPERATE_UPDATE:
                operations.putAll(GLOBAL_CONFIG_KEY, map);
                break;
            case OPERATE_DELETE:
                operations.delete(GLOBAL_CONFIG_KEY, map.keySet().toArray());
                break;
            case OPERATE_SYNC:
                redisTemplate.delete(GLOBAL_CONFIG_KEY);
                operations.putAll(GLOBAL_CONFIG_KEY, map);
                break;
        }
    }

    public static void cacheShopSingleConfig(List<ShopSingleConfigDto> list, String operation) {
        if (list == null || list.size() == 0) {
            return;
        }
        Long shopId = list.get(0).getShopId();
        String key = String.format(SHOP_SINGLE_CONFIG_KEY, shopId.toString());
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        HashMap<String, String> map = new HashMap<>();
        list.stream().forEach(o -> map.put(o.getConfigKey(), JSON.toJSONString(o)));
        switch (operation) {
            case OPERATE_ADD:
                operations.putAll(key, map);
                break;
            case OPERATE_UPDATE:
                operations.putAll(key, map);
                break;
            case OPERATE_DELETE:
                operations.delete(key, map.keySet().toArray());
                break;
            case OPERATE_SYNC:
                redisTemplate.delete(key);
                operations.putAll(key, map);
                break;
        }

    }

    public static String getShopSingleConfigByKey(Long shopId, String hashKey) {
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String key = String.format(SHOP_SINGLE_CONFIG_KEY, shopId.toString());
        String result = operations.get(key, hashKey);
        if (StringUtils.isBlank(result)) {
            return null;
        }
        ShopSingleConfigDto dto = JSON.parseObject(result, ShopSingleConfigDto.class);
        return dto.getConfigValue();
    }

    public static List<ShopInterceptConfigDto> getShopIncercept(Long shopId) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String s = operations.get(String.format(SHOP_INTERCEPT_CONFIG_KEY, shopId.toString()));
        if (StringUtils.isBlank(s)) {
            return null;
        }
        List<ShopInterceptConfigDto> list = JSON.parseArray(s, ShopInterceptConfigDto.class);
        return list;
    }

    public static String findShopInterceptConfigKey(Long shopId) {
        return String.format(SHOP_INTERCEPT_CONFIG_KEY, shopId.toString());
    }


}
