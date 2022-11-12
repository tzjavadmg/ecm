package com.milisong.scm.shop.util;

import com.alibaba.fastjson.JSON;
import com.farmland.core.cache.RedisCache;
import com.milisong.scm.shop.dto.config.ShopBannerDto;
import com.milisong.scm.shop.dto.config.ShopInterceptConfigDto;
import com.milisong.scm.shop.dto.config.ShopSingleConfigDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/29   12:52
 *    desc    : 配置相关redis工具类
 *    version : v1.0
 * </pre>
 */

public class RedisConfigUtil{

    private static StringRedisTemplate redisTemplate = RedisCache.getRedisTemplate();

    private static final String SHOP_INTERCEPT_CONFIG_KEY = "config:intercept_config:scm:%s";
    private static final String SHOP_BANNER_CONFIG_KEY = "config:shop_banner_config:scm:%s";
    private static final String SHOP_SINGLE_CONFIG_KEY = "config:shop_single_config:scm:%s";
    private static final String GLOBAL_CONFIG_KEY = "config:global_config:scm";
    private static final String OPERATE_ADD = "add";
    private static final String OPERATE_UPDATE = "update";
    private static final String OPERATE_DELETE = "delete";
    private static final String OPERATE_SYNC = "sync";

    public static void cacheShopIntercept(Long shopId,List<ShopInterceptConfigDto> list){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(String.format(SHOP_INTERCEPT_CONFIG_KEY,shopId.toString()), JSON.toJSONString(list));
    }

    public static void cacheGlobalConfig(Map<String,String> map,String operation){
        if(map ==null || map.size() == 0){
            return;
        }
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        switch (operation){
            case OPERATE_ADD:
                operations.putAll(GLOBAL_CONFIG_KEY,map);
                break;
            case OPERATE_UPDATE:
                operations.putAll(GLOBAL_CONFIG_KEY,map);
                break;
            case OPERATE_DELETE:
                operations.delete(GLOBAL_CONFIG_KEY,map.keySet().toArray());
                break;
            case OPERATE_SYNC:
                redisTemplate.delete(GLOBAL_CONFIG_KEY);
                operations.putAll(GLOBAL_CONFIG_KEY,map);
                break;
        }
    }

    public static void cacheShopSingleConfig(List<ShopSingleConfigDto> list,String operation){
        if(list == null || list.size() == 0){
            return;
        }
        Long shopId = list.get(0).getShopId();
        String key = String.format(SHOP_SINGLE_CONFIG_KEY, shopId.toString());
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        HashMap<String, String> map = new HashMap<>();
        list.stream().forEach(o-> map.put(o.getConfigKey(),JSON.toJSONString(o)));
        switch (operation){
            case OPERATE_ADD:
                operations.putAll(key,map);
                break;
            case OPERATE_UPDATE:
                operations.putAll(key,map);
                break;
            case OPERATE_DELETE:
                operations.delete(key,map.keySet().toArray());
                break;
            case OPERATE_SYNC:
                redisTemplate.delete(key);
                operations.putAll(key,map);
                break;
        }

    }

    public static List<ShopInterceptConfigDto> getShopIncercept(Long shopId){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String s = operations.get(String.format(SHOP_INTERCEPT_CONFIG_KEY, shopId.toString()));
        if(StringUtils.isBlank(s)){
            return null;
        }
        List<ShopInterceptConfigDto> list = JSON.parseArray(s, ShopInterceptConfigDto.class);
        return list;
    }

    public static Object getGlobalConfig(String mapKey){
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        return operations.get(GLOBAL_CONFIG_KEY,mapKey);
    }

    public static List<ShopBannerDto> getShopBanner(Long shopId){
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        String s = operations.get(String.format(SHOP_BANNER_CONFIG_KEY, shopId.toString()));
        if(StringUtils.isBlank(s)){
            return null;
        }
        List<ShopBannerDto> list = JSON.parseArray(s, ShopBannerDto.class);
        return list;
    }

    public static List<ShopSingleConfigDto> getShopSingleConfig(Long shopId){
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String key = String.format(SHOP_SINGLE_CONFIG_KEY, shopId.toString());
        //防止key事故
        if(StringUtils.isBlank(key)){
            return null;
        }
        Set<String> keys = operations.keys(key);
        List<String> strings = operations.multiGet(key, keys);
        List<ShopSingleConfigDto> list = new ArrayList<>();
        strings.stream().forEach(o-> list.add(JSON.parseObject(o,ShopSingleConfigDto.class)));
        return list;
    }

   /* public static void main(String[] args) {
        MybatisGenerateUtil.setDbUrl("127.0.0.1:3306");
        MybatisGenerateUtil.setUserId("root");
        MybatisGenerateUtil.setPassword("123456");
        MybatisGenerateUtil.setDbName("t_bracelet");
        MybatisGenerateUtil.setTableName("t_global_config,t_shop_single_config");
        MybatisGenerateUtil.setEntity("GlobalConfig,ShopSingleConfig");
        MybatisGenerateUtil.setJavaRootPath("com.milisong.scm.shop");
        MybatisGenerateUtil.setAuthor("田海波");
        MybatisGenerateUtil.setVersion("1.1");
        MybatisGenerateUtil.setGenerateSetGet(false);
        MybatisGenerateUtil.doGenerateCode();
    }*/

}
