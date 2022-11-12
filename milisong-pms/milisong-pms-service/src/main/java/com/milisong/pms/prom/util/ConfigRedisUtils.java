package com.milisong.pms.prom.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/12/19   11:24
 *    desc    : 配置信息redis操作工具类
 * 【P1】米立送红包转发图配置 sharePic
 *
 * 【P1】米立送红包转发头图配置 masterPic
 *
 * 【P1】米立送红包转发文案配置 shareTitle
 *
 * 【P1】米立送小程序转发图配置
 *    version : v1.0
 * </pre>
 */
@Slf4j
public class ConfigRedisUtils {


    private static final String configType = "global_input";
    private static final String TYPE = "type";
    private static final String DATA = "data";
    //米立送午餐专享红包
    private static final String redPacketKey = "activity_red_packet:1";
    //分享图
    private static final String sharePic = "sharePic";
    private static final String sharePicConfig = "redPacket-sharePic";
    //主图
    private static final String masterPic = "masterPic";
    private static final String masterPicConfig = "redPacket-masterPic";
    //分享文案
    private static final String shareTitle = "shareTitle";
    private static final String shareTitleConfig = "redPacket-shareTitle";

    //午餐支付订单弹层
    private static final String shareToastPic = "shareToastPic";
    private static final String shareToastPicConfig = "redPacket-shareToastPic";

    //积分规则图片(早餐使用)
    private static final String pointRulePic = "pointRulePic";
    //结算页，不可用券标识语
    private static final String UNUSABLE_COUPON_LABEL = "unusableCouponLabel";
    private static final String GLOBAL_BF_CONFIG = "config_bf:global";


    private static final String BF_COUPON_MULTI = "breakfast_coupon:multi";

    private static final String bfMasterPic = "masterPic";
    private static final String bfMasterPicConfig = "coupon-masterPic";
    private static final String bfSharePic = "sharePic";
    private static final String bfSharePicConfig = "coupon-sharePic";
    private static final String bfShareToastPic = "shareToastPic";
    private static final String bfShareToastPicConfig = "coupon-shareToastPic";
    private static final String bfShareTitle = "shareTitle";
    private static final String bfShareTitleConfig = "coupon-shareTitle";



    private static StringRedisTemplate redisTemplate = RedisCache.getRedisTemplate();

    public static void updateConfigMsg(String msg){
        JSONObject jsonObject = JSONObject.parseObject(msg);
        if(!configType.equals(jsonObject.getString(TYPE))){
            return;
        }
        JSONObject data = jsonObject.getJSONObject(DATA);
        HashMap<String,String> hashMap = JSON.parseObject(data.toJSONString(),HashMap.class);
        if(hashMap == null || hashMap.size() == 0){
            return;
        }
        String result = redisTemplate.opsForValue().get(redPacketKey);
        HashMap valMap = JSON.parseObject(result, HashMap.class);
        Set<Map.Entry<String, String>> entries = hashMap.entrySet();
        for (Map.Entry<String, String> e:entries) {
            if(e.getKey().equals(sharePicConfig)){
                valMap.put(sharePic,e.getValue());
            }else if(e.getKey().equals(masterPicConfig)){
                valMap.put(masterPic,e.getValue());
            }else if(e.getKey().equals(shareTitleConfig)){
                valMap.put(shareTitle,e.getValue());
            }else if (e.getKey().equals(shareToastPicConfig)){
                valMap.put(shareToastPic,e.getValue());
            }
        }
        redisTemplate.opsForValue().set(redPacketKey,JSON.toJSONString(valMap));
    }

    public static void updateConfigBfMsg(String msg) {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        if(!configType.equals(jsonObject.getString(TYPE))){
            return;
        }
        JSONObject data = jsonObject.getJSONObject(DATA);
        HashMap<String,String> hashMap = JSON.parseObject(data.toJSONString(),HashMap.class);
        if(hashMap == null || hashMap.size() == 0){
            return;
        }
        //更新指定key信息到redis中的指定key
        String result = redisTemplate.opsForValue().get(BF_COUPON_MULTI);
        HashMap valMap = JSON.parseObject(result, HashMap.class);
        Set<Map.Entry<String, String>> entries = hashMap.entrySet();
        for (Map.Entry<String, String> e:entries) {
            if(e.getKey().equals(bfMasterPicConfig)){
                valMap.put(bfMasterPic,e.getValue());
            }else if(e.getKey().equals(bfSharePicConfig)){
                valMap.put(bfSharePic,e.getValue());
            }else if(e.getKey().equals(bfShareToastPicConfig)){
                valMap.put(bfShareToastPic,e.getValue());
            }else if(e.getKey().equals(bfShareTitleConfig)){
                valMap.put(bfShareTitle,e.getValue());
            }
        }
        redisTemplate.opsForValue().set(BF_COUPON_MULTI,JSON.toJSONString(valMap));
        updateGlobalBfConfig(hashMap);
    }

    private static void updateGlobalBfConfig(Map<String,String> map){
        redisTemplate.opsForHash().putAll(GLOBAL_BF_CONFIG,map);
    }

    public static String getPointRulePic(){
        Object o = redisTemplate.opsForHash().get(GLOBAL_BF_CONFIG, pointRulePic);
        if(o == null ){
            return null;
        }else{
            return o.toString();
        }
    }

    public static String getUnusableCouponLabel(){
        try {
            Object o = redisTemplate.opsForHash().get(GLOBAL_BF_CONFIG, UNUSABLE_COUPON_LABEL);
            if(o != null ){
                return o.toString();
            }
        } catch (Exception e) {
            log.error("",e);
        }
        return "不可用";
    }
}
