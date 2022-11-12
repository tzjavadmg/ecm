package com.milisong.ecm.common.notify.util;

import com.farmland.core.cache.RedisCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/29   15:06
 *    desc    : 通知业务工具类
 *    version : v1.0
 * </pre>
 */

public class NotifyCacheUtil {

    private static final String NOTIFY_CHECK_KEY = "notify_check_key:%s";
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final Integer DEFAULTLIVETIME = 30;
    private static StringRedisTemplate redisTemplate = RedisCache.getRedisTemplate();

    public static boolean checkIfSendNotify(Long orderSetId){
        String key = String.format(NOTIFY_CHECK_KEY, orderSetId.toString());
        String result = redisTemplate.opsForValue().get(key);
        if(StringUtils.isBlank(result)){
            return false;
        }else{
            return true;
        }
    }
    public static void cacheSendNotify(Long orderSetId){
        String key = String.format(NOTIFY_CHECK_KEY, orderSetId.toString());
        redisTemplate.opsForValue().set(key,format.format(new Date()),DEFAULTLIVETIME, TimeUnit.MINUTES);
    }
}
