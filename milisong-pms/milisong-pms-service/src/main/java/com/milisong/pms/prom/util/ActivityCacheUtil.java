package com.milisong.pms.prom.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import com.milisong.pms.prom.dto.ActivityRedPacketDto;
import com.milisong.pms.prom.dto.BreakfastCouponConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Seconds;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author sailor wang
 * @date 2018/9/13 下午5:41
 * @description
 */
@Slf4j
public class ActivityCacheUtil {
    private static final String CURRENT_REDPACKET_ACTIVITY = "current_redpacket_activity";

    private static final String CURRENT_BREAKFAST_COUPON = "breakfast_coupon:new";

    private static final String COMPANY_BREAKFAST_COUPON = "breakfast_coupon:company";

    private static final String DEFAULT_BREAKFAST_COUPON = "breakfast_coupon:default";

    private static final String DEFAULT_BREAKFAST_GOODS_COUPON = "breakfast_coupon:goods";

    public static void cacheCurrentRedPacket(ActivityRedPacketDto activityRedPacket) {
        DateTime expireDate = new DateTime(activityRedPacket.getEndDate());
        int seconds = Seconds.secondsBetween(DateTime.now(), expireDate).getSeconds();
        RedisCache.setEx(CURRENT_REDPACKET_ACTIVITY, JSON.toJSONString(activityRedPacket), seconds, TimeUnit.SECONDS);
    }

    public static ActivityRedPacketDto getCurrentRedPacket() {
        String jsonStr = RedisCache.get(CURRENT_REDPACKET_ACTIVITY);
        if (StringUtils.isNotBlank(jsonStr)) {
            return JSONObject.parseObject(jsonStr, ActivityRedPacketDto.class);
        }
        return null;
    }

    public static BreakfastCouponConfig getCurrentBreakfastCoupon() {
        String jsonStr = RedisCache.get(CURRENT_BREAKFAST_COUPON);
        log.info("新人早餐券 jsonStr -> {}",jsonStr);
        if (StringUtils.isNotBlank(jsonStr)) {
            return JSONObject.parseObject(jsonStr, BreakfastCouponConfig.class);
        }
        return null;
    }

    public static BreakfastCouponConfig getBreafastCouponByCompanyId(Long companyId) {
        String jsonStr = RedisCache.get(COMPANY_BREAKFAST_COUPON);
        log.info("公司早餐券 jsonStr -> {}",jsonStr);
        if (StringUtils.isNotBlank(jsonStr)) {
            Map<String, BreakfastCouponConfig> configMap = JSONObject.parseObject(jsonStr, HashMap.class);
            return JSONObject.parseObject(JSONObject.toJSONString(configMap.get(companyId.toString())),BreakfastCouponConfig.class);
        }
        return null;
    }

    public static BreakfastCouponConfig getDefaultBreakfastConfig() {
        String jsonStr = RedisCache.get(DEFAULT_BREAKFAST_COUPON);
        log.info("默认早餐券 jsonStr -> {}",jsonStr);
        if (StringUtils.isNotBlank(jsonStr)) {
            return JSONObject.parseObject(jsonStr, BreakfastCouponConfig.class);
        }
        return null;
    }

    public static BreakfastCouponConfig getBreakfastGoodsCouponConfig() {
        String jsonStr = RedisCache.get(DEFAULT_BREAKFAST_GOODS_COUPON);
        log.info("商品券 jsonStr -> {}",jsonStr);
        if (StringUtils.isNotBlank(jsonStr)) {
            return JSONObject.parseObject(jsonStr, BreakfastCouponConfig.class);
        }
        return null;
    }


}