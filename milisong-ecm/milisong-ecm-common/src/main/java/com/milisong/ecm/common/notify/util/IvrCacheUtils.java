package com.milisong.ecm.common.notify.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/9/25   9:46
 *    desc    : ivr缓存处理工具类
 *    version : v1.0
 * </pre>
 */

public class IvrCacheUtils {

    private static Integer MAX_RETRY  =3;
    private static Integer MAX_INTERVAL  =2*60;//2分钟
    private static Integer DUPLICATE_INTERVAL  =2;//2小时

    public static void cacheMobileIvrMsg(StringRedisTemplate redisTemplate,Collection<String> collection, String orderNo, int count) {
        //生成key格式：【delivery_ivr:uuid】
        String key = "delivery_ivr:".concat(orderNo);
        Long explireTime = 1L;
        String timeStr = count+"-"+new Date().getTime();
        Map<String,String> map = new HashMap<>();
        collection.stream().forEach(o-> map.put(o,timeStr));
        redisTemplate.opsForHash().putAll(key,map);
        redisTemplate.expire(key,explireTime,TimeUnit.HOURS);
    }

    public static Set<String> getMobileIvrKeys(StringRedisTemplate redisTemplate){
        String key = "delivery_ivr:*";
        Set<String> keys = redisTemplate.keys(key);
        return keys;
    }

    public static String generateUUID(String orderNo,Integer count){
        //uuid格式:【orderNo:count:uuid】,主要出于后续回调我们来便于查找问题
        String uuid = RandomStringUtils.random(10,true,true);
        return orderNo.concat(":").concat(count.toString()).concat(":").concat(uuid);
    }

    public static Map<String,String> getIvrByKey(StringRedisTemplate redisTemplate,String key){
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        Map<String, String> map = operations.entries(key);
        return map;
    }

    public static String checkIfNeedRetry(String value){
        if(StringUtils.isBlank(value)){
            return IVRENUM.DEFAULT.getCode();
        }
        String[] split = value.split("-");
        Integer count = Integer.valueOf(split[0]);
        if(count == 0){
            return IVRENUM.DEFAULT.getCode();
        }
        if(count >=MAX_RETRY){
            return IVRENUM.GO_SMS.getCode();
        }
        long lastDate = Long.valueOf(split[1]);
        long nowDate = new Date().getTime();
        long second = (nowDate - lastDate) / 1000;
        if(second > MAX_INTERVAL){
            return IVRENUM.GO_RETRY.getCode();
        }
        return IVRENUM.DEFAULT.getCode();
    }

    public static boolean deleteIvrByKey(StringRedisTemplate redisTemplate,String key){
        return redisTemplate.delete(key);
    }
    public static Integer getIvrCount(String value){
        if(StringUtils.isBlank(value)){
            return 0;
        }
        String[] split = value.split("-");
        return Integer.valueOf(split[1]);
    }

    public static void updateNotifyIvr(StringRedisTemplate redisTemplate,String mobile, String orderNoKey, String value) {
        // 解析vlaue，提取orderNo ，count
        Integer count = Integer.valueOf(value.split("-")[0]);
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String timeStr = (count+1)+"-"+new Date().getTime();
        operations.put(orderNoKey,mobile,timeStr);
    }

    public static void updateNotifyIvr2Sms(StringRedisTemplate redisTemplate, String mobile, String orderNoKey) {
        // 解析vlaue，提取orderNo ，count
        HashOperations<String, String, String> operations = redisTemplate.opsForHash();
        String timeStr = "0-"+new Date().getTime();
        operations.put(orderNoKey,mobile,timeStr);
    }

    public static boolean checkIfComplate(Map<String, String> map){
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        for (Map.Entry<String,String> entry:entrySet) {
            String value = entry.getValue();
            if(!StringUtils.isBlank(value)){
                if(!"0".equals(value.split("-")[0])){
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkIfDuplicateCall(StringRedisTemplate redisTemplate, String mainUnionOrderNo,String mobileNo){
        String key = "delivery_ivr_duplicate:".concat(mainUnionOrderNo).concat(":").concat(mobileNo);
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        if(operations.get(key)!=null){
            return true;
        }
        //第一次拨打，记录，为后面防重
        operations.set(key,mobileNo,DUPLICATE_INTERVAL,TimeUnit.HOURS);
        return false;
    }

    public static void updateIvrResult(StringRedisTemplate redisTemplate, String uuid,String mobile,boolean success){
        String[] split = uuid.split(":");
        String orderNo = split[0];
        if(success){
            HashOperations<String, String, String> operations = redisTemplate.opsForHash();
            operations.put("delivery_ivr:".concat(orderNo),mobile,"0-"+new Date().getTime());
        }
    }
    public static enum IVRENUM{
        DEFAULT("0","空执行，不做处理"),
        GO_RETRY("1","继续重试"),
        GO_SMS("2","达到重试上限，执行发短信"),
        ;

        private String code;
        private String desc;

        IVRENUM(String code,String desc){
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public IVRENUM getByCode(String code){
            IVRENUM[] values = IVRENUM.values();
            for (IVRENUM ivrenum:values) {
                if(ivrenum.code.equals(code)){
                    return ivrenum;
                }
            }
            return DEFAULT;
        }
    }

}
