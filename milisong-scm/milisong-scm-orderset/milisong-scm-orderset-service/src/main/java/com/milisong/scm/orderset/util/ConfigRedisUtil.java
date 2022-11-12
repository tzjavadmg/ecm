package com.milisong.scm.orderset.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import com.milisong.scm.shop.dto.config.ShopSingleConfigDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.util.List;

/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/30   17:25
 *    desc    : 配置信息读取工具类
 *    version : v1.0
 * </pre>
 */

public class ConfigRedisUtil {
    private static StringRedisTemplate redisTemplate = RedisCache.getRedisTemplate();

    private static final String SHOP_SINGLE_CONFIG_KEY = "config:shop_single_config:scm:%s";
    private static final String ordersetMaxAmount = "ordersetMaxAmount";
    private static final String ordersetMaxMember = "ordersetMaxMember";

    /**
     * 获取集单的最大金额
     * @param shopId
     * @return
     */
    public static BigDecimal getOrdersetMaxAmount(Long shopId){
        String key = String.format(SHOP_SINGLE_CONFIG_KEY, shopId.toString());
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String s = operations.get(key, ordersetMaxAmount);
        if (StringUtils.isBlank(s)){
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(s);
        return new BigDecimal(jsonObject.getString("configValue"));
    }
    
    /**
     * 获取集单的最大顾客数
     * @param shopId
     * @return
     */
    public static Integer getOrdersetMaxMember(Long shopId){
        String key = String.format(SHOP_SINGLE_CONFIG_KEY, shopId.toString());
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String s = operations.get(key, ordersetMaxMember);
        if (StringUtils.isBlank(s)){
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(s);
        return new Integer(jsonObject.getString("configValue"));
    }
    

}
