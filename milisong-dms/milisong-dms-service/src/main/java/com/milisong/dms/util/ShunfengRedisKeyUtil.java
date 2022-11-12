package com.milisong.dms.util;

import java.util.Date;

/**
 * @author zhaozhonghui
 * @date 2018-10-23
 */
public class ShunfengRedisKeyUtil {

    public static String SF_HASH_KEY = "sf_info";

    // redis 缓存队列key的前缀
    public static final String DELAY_LIST_KEY_PREFIX = "delay_orderset:";
    // redis 缓存队列key的生成时间
    public static final String DELAY_LIST_TIME_KEY_PREFIX = "delay_orderset_time:";

    /** 根据子集单id获取顺丰id */
    public static String getSfOrderIdKey(String detailSetId) {
        return "sf:detail_set_id:" + detailSetId;
    }

    public static String getSfOrderStatusLockKey(Long sfOrderId,Byte status){
        return "lock_sf_order_id:" + sfOrderId + ":" + status;
    }

    public static String getSfOrderKey(String detailSetId){
        return "sf_info:" + detailSetId;
    }

    public static String getShopKey(){
        return "shop_info";
    }

    public static String getBuildingKey(){
        return "building_info";
    }

    public static String getCompanyKey(){
        return "company_info";
    }

    public static String getSfTaskLock() {
    	return "sf_task_lock";
    }

    /**
     * 获取延迟队列的key
     * @param mq
     * @return
     */
    public static String getDelayKey(Date deliveryDate, Date deliveryEndTime) {
    	return DELAY_LIST_KEY_PREFIX.concat(DateUtils.format(deliveryDate,"yyyyMMdd"))
    		.concat(":").concat(DateUtils.format(deliveryEndTime, "HHmmss"));
    }

    /**
     * 获取延迟队列的时间的key
     * @param shopId
     * @param deliveryDate
     * @param deliveryEndTime
     * @return
     */
    public static String getDelayTimeKey(Date deliveryDate, Date deliveryEndTime) {
    	return DELAY_LIST_TIME_KEY_PREFIX.concat(DateUtils.format(deliveryDate,"yyyyMMdd"))
        		.concat(":").concat(DateUtils.format(deliveryEndTime, "HHmmss"));
    }
}
