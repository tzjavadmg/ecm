package com.milisong.breakfast.pos.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSON;
import com.farmland.core.cache.RedisCache;
import com.milisong.breakfast.pos.constant.ConfigConstant;
import com.milisong.breakfast.pos.dto.ConfigDto;

import org.springframework.data.redis.core.ValueOperations;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/26   16:32
 *    desc    : 配置服务redis相关工具类
 *    version : v1.0
 * </pre>
 */

public class ConfigRedisUtil {

    private static StringRedisTemplate redisTemplate = RedisCache.getRedisTemplate();
    private static final int EXPIRE_TIME = 60*60*24*30;//默认失效时间，30天

    private static final String GLOBAL_CONFIG_KEY = "config:global_config:pos";
    private static final String KEY_CONFIG_INTERCEPT = "config:intercept_config:pos:%s";
    private static final String KEY_CONFIG_SINGLE = "config:shop_single_config:pos:%s";
    private static final String OPERATE_ADD = "add";
    private static final String OPERATE_UPDATE = "update";
    private static final String OPERATE_DELETE = "delete";
    private static final String OPERATE_SYNC = "sync";

    private static final String SHOP_ID = "shopId";
    private static final String CONFIG_KEY = "configKey";
    private static final String CONFIG_VALUE = "configValue";
    private static final String MAX_PRODUCTION = "maxProduction";

    private static final SimpleDateFormat format = new SimpleDateFormat("HH:mm");

    public static boolean updateCache(ConfigDto dto){
        Object data = dto.getData();
        String type = dto.getType();
        //单个门店配置
        if(type.equals(ConfigConstant.SHOP_SINGLE_CONFIG.getValue())){
            return updateShopSingleConfig(data,dto.getOperation());
        }
        //全局配置
        if(type.equals(ConfigConstant.GLOBAL_CONFIG.getValue())){
            HashMap<String,String> hashMap = JSONObject.parseObject(JSON.toJSONString(data),HashMap.class);
            return cacheGlobalConfig(hashMap,dto.getOperation());
        }
        //截单配置
        if(type.equals(ConfigConstant.INTERCEPT_CONFIG.getValue())){
            return updateShopInterceptConfig(data);
        }
        return true;
    }

    public static boolean cacheGlobalConfig(Map<String,String> map, String operation){
        if(map ==null || map.size() == 0){
            return true;
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
        return true;
    }

    /**
     * 更新单个门店配置信息,目前缓存最大生产量
     * @param data
     */
    private static boolean updateShopSingleConfig(Object data,String operation){
        List<HashMap> list = JSON.parseArray(JSON.toJSONString(data),HashMap.class);
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        HashMap<String, Object> map = list.get(0);
        String key = String.format(KEY_CONFIG_SINGLE,map.get(SHOP_ID).toString());
        HashMap<String, String> hashValues = new HashMap<>();
        list.stream().forEach(o-> hashValues.put(o.get(CONFIG_KEY).toString(),o.get(CONFIG_VALUE).toString()));
        switch (operation){
            case OPERATE_ADD:
                operations.putAll(key,hashValues);
                break;
            case OPERATE_UPDATE:
                operations.putAll(key,hashValues);
                break;
            case OPERATE_DELETE:
                operations.delete(key,hashValues.keySet().toArray());
                break;
            case OPERATE_SYNC:
                redisTemplate.delete(key);
                operations.putAll(key,hashValues);
                break;
        }
        return true;
    }

    /**
     * 更新截单配置信息,目前缓存最大生产量 key->config:intercept:{shopCode}
     * @param data
     */
    private static boolean updateShopInterceptConfig(Object data){
        List<HashMap> list = JSON.parseArray(JSON.toJSONString(data),HashMap.class);
        HashMap<String, Object> map = list.get(0);
        String key = String.format(KEY_CONFIG_INTERCEPT,map.get(SHOP_ID).toString());
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(key, JSON.toJSONString(list));
        return true;
    }

    public static Integer getCacheMaxOutputByTime(Long shopId,Date beginTime, Date endTime){
        String key = String.format(KEY_CONFIG_INTERCEPT,shopId.toString());
        String result = redisTemplate.opsForValue().get(key);
        List<HashMap> list = JSON.parseArray(result,HashMap.class);
        try {
            for (HashMap<String,Object> map:list) {
                Date deliveryTimeBegin = format.parse(map.get("startTime").toString());
                Date deliveryTimeEnd = format.parse(map.get("endTime").toString());
                if(deliveryTimeBegin.equals(beginTime) && deliveryTimeEnd.equals(endTime)){
                    return Integer.parseInt(map.get("maxCapacity").toString());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Integer getMaxOutput(Long shopId){
        Object maxProduction = redisTemplate.opsForHash().get(String.format(KEY_CONFIG_SINGLE, shopId.toString()), MAX_PRODUCTION);
        if(maxProduction == null){
            return null;
        }
        return Integer.parseInt(maxProduction.toString());
    }

    public static Date getInterceptTime(Long shopId,Date beginTime,Date endTime){
        String key = String.format(KEY_CONFIG_INTERCEPT,shopId.toString());
        String result = redisTemplate.opsForValue().get(key);
        List<HashMap> list = JSON.parseArray(result,HashMap.class);
        try {
            for (HashMap<String,Object> map:list) {
                Date deliveryTimeBegin = format.parse(map.get("startTime").toString());
                Date deliveryTimeEnd = format.parse(map.get("endTime").toString());
                if(deliveryTimeBegin.equals(beginTime) && deliveryTimeEnd.equals(endTime)){
                    return format.parse(map.get("cutOffTime").toString());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String  getShopSingleConfigByKey(Long shopId,String hashKey){
        String key = String.format(KEY_CONFIG_SINGLE,shopId.toString());
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        Object o = operations.get(key, hashKey);
        if(o == null){
            return null;
        }else{
            return o.toString();
        }
    }
}
