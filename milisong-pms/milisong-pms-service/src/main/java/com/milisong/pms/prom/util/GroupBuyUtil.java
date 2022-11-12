package com.milisong.pms.prom.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.distrib.LockProvider;
import com.milisong.pms.prom.domain.ActivityGroupBuy;
import com.milisong.pms.prom.dto.groupbuy.ActivityGroupBuyDto;
import com.milisong.pms.prom.dto.groupbuy.JoinUser;
import com.milisong.pms.prom.dto.groupbuy.UserGroupBuyDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RCountDownLatch;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 拼团活动工具类
 *
 * @author sailor wang
 * @date 2019/5/17 5:30 PM
 * @description
 */
@Slf4j
public class GroupBuyUtil {
    private static final String CURRENT_GROUP_BUY_KEY = "group_buy:active";//当前生效的拼团活动
    private static final String GROUP_BUY_KEY = "group_buy:activity:%s";//拼团活动
    private static final String GROUP_BUY_JOINED_USER_KEY = "group_buy:user_list:%s";//参加拼团的用户列表
    private static final String GROUP_BUY_USER_JOINED_KEY = "group_buy:self_joined:%s";//自己参团的拼团

    private static final String PAY_CALLBACK_SYNC = "pay_callback_sync_orderid:%s";//支付成功回调，释放锁


    /**
     * 获取当前生效的拼团活动
     *
     * @return
     */
    public static ActivityGroupBuyDto currentActivityGroupBuy() {
        String json = RedisCache.get(CURRENT_GROUP_BUY_KEY);
        if (StringUtils.isNotBlank(json)){
            return JSONObject.parseObject(json,ActivityGroupBuyDto.class);
        }
        return null;
    }

    /**
     * 设置当前进行的拼团活动
     *
     * @param activityGroupBuy
     * @param timeout
     * @param unit
     */
    public static void setCurrentActivityGroupBuy(ActivityGroupBuyDto activityGroupBuy,long timeout, TimeUnit unit){
        RedisCache.setEx(CURRENT_GROUP_BUY_KEY,JSONObject.toJSONString(activityGroupBuy),timeout,unit);
    }

    public static void setActivityGroupBuy(Long groupBuyId, ActivityGroupBuy activityGroupBuy, long timeout, TimeUnit unit) {
        String key = String.format(GROUP_BUY_KEY,groupBuyId);
        RedisCache.setEx(key,JSONObject.toJSONString(activityGroupBuy),timeout,unit);
    }

    public static ActivityGroupBuy getActivityGroupBuy(Long groupBuyId) {
        String key = String.format(GROUP_BUY_KEY,groupBuyId);
        String json = RedisCache.get(key);
        if (StringUtils.isNotBlank(json)){
            return JSONObject.parseObject(json,ActivityGroupBuy.class);
        }
        return null;
    }

    /**
     * 查询拼团实例参加的用户列表
     *
     * @param userGroupBuyId
     * @return
     */
    public static List<JoinUser> fetchGroupBuyJoinedUsers(Long userGroupBuyId) {
        String key = String.format(GROUP_BUY_JOINED_USER_KEY,userGroupBuyId);
        String json = RedisCache.get(key);
        if (StringUtils.isNotBlank(json)){
            return JSONArray.parseArray(json,JoinUser.class);
        }
        return null;
    }

    /**
     * 缓存拼团实例参加的用户列表
     *
     * @param userGroupBuyId
     * @param joinUserList
     */
    public static void cacheGroupBuyJoinedUsers(Long userGroupBuyId, List<JoinUser> joinUserList,long timeout, TimeUnit unit) {
        String key = String.format(GROUP_BUY_JOINED_USER_KEY,userGroupBuyId);
        if (CollectionUtils.isNotEmpty(joinUserList)){
            RedisCache.setEx(key,JSONObject.toJSONString(joinUserList),timeout,unit);
        }
    }

    /**
     * 每次有用户成功参团后，清空该缓存
     * @param userGroupBuyId
     */
    public static void clearGroupBuyJoinedUsers(Long userGroupBuyId){
        String key = String.format(GROUP_BUY_JOINED_USER_KEY,userGroupBuyId);
        RedisCache.delete(key);
    }

    public static UserGroupBuyDto userJoinedGroupBuy(Long groupBuyId, Long userId) {
        String key = String.format(GROUP_BUY_USER_JOINED_KEY,groupBuyId+":"+userId);
        String json = RedisCache.get(key);
        if (StringUtils.isNotBlank(json)){
            return JSONObject.parseObject(json,UserGroupBuyDto.class);
        }
        return null;
    }

    public static void cacheUserJoinedGroupBuy(UserGroupBuyDto userGroupBuy,Long groupBuyId, Long userId, long timeout, TimeUnit seconds) {
        String key = String.format(GROUP_BUY_USER_JOINED_KEY,groupBuyId+":"+userId);
        RedisCache.setEx(key,JSONObject.toJSONString(userGroupBuy),timeout,seconds);
    }

    public static void clearUserJoinedGroupBuy(Long groupBuyId, Long userId) {
        String key = String.format(GROUP_BUY_USER_JOINED_KEY,groupBuyId+":"+userId);
        RedisCache.delete(key);
    }

    public static void lockBeforePayCallback(Long orderId){
        try {
            log.info("count_down_latch -> lock 加锁，orderId -> {}",orderId);
            String key = String.format(PAY_CALLBACK_SYNC,orderId);
            RCountDownLatch latch = LockProvider.getCountDownLatch(key);
            long count = latch.getCount();
            if (count==0){
                latch.trySetCount(1);
            }
        } catch (Exception e) {
            log.error("",e);
        }
    }

    public static void waitBeforePayCallback(Long orderId){
        String key = String.format(PAY_CALLBACK_SYNC,orderId);
        try {
            RCountDownLatch latch = LockProvider.getCountDownLatch(key);
            long count = latch.getCount();
            if (count > 0){
                log.info("count_down_latch -> wait，等待等待等待等待5s，count_down_latch，orderId -> {}",orderId);
                latch.await(5,TimeUnit.SECONDS);//锁5s
            }else {
                log.info("count_down_latch count 已释放，orderId -> {}",orderId);
            }
        } catch (InterruptedException e) {
            log.error("",e);
        }
    }


    public static void unlockAfterPayCallback(Long orderId){
        try {
            String key = String.format(PAY_CALLBACK_SYNC,orderId);
            RCountDownLatch latch = LockProvider.getCountDownLatch(key);
            long count = latch.getCount();
            if (count > 0){
                log.info("count_down_latch -> unlock 支付回调成功，count_down_latch 释放，orderId -> {}",orderId);
                latch.countDown();
            }
        } catch (Exception e) {
            log.error("",e);
        }
    }

}