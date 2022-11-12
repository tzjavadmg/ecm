package com.milisong.pms.prom.util;

import com.farmland.core.cache.RedisCache;
import com.farmland.core.distrib.LockProvider;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * 红包池工具类型
 * @author sailor wang
 * @date 2018/9/22 上午10:36
 * @description
 */
@Slf4j
public class PmsPoolUtil {

    private static final String REDPACKETPOOL_QUEUE = "red_packet_pool_queue:%s";

    private static final String COUPONPOOL_QUEUE = "coupon_pool_queue:%s";


    public static Long createLunchRedPacketQueue(Long userActivityId,List<String> redPacketPool){
        String key = String.format(REDPACKETPOOL_QUEUE,userActivityId);
        return RedisCache.lLeftPushAll(key,redPacketPool);
    }

    public static Long createBreakFastCouponQueue(Long userActivityId,List<String> redPacketPool){
        String key = String.format(COUPONPOOL_QUEUE,userActivityId);
        return RedisCache.lLeftPushAll(key,redPacketPool);
    }

    public static String popLunchRedPacketQueue(Long userActivityId){
        String key = String.format(REDPACKETPOOL_QUEUE,userActivityId);
        String jsonData = RedisCache.lRightPop(key);
        return jsonData;
    }

    public static String popBreakFastCouponQueue(Long userActivityId){
        String key = String.format(COUPONPOOL_QUEUE,userActivityId);
        String jsonData = RedisCache.lRightPop(key);
        return jsonData;
    }

    /**
     * 红包随机算法
     * @param lock
     * @param amount
     * @param min
     * @param max
     * @param personNum
     * @return
     * @throws Exception
     */
    public static List<BigDecimal> random(String lock,BigDecimal amount,int min,int max,int personNum) {

        RLock rLock = LockProvider.getLock(lock);
        rLock.lock();

        List<BigDecimal> list = Lists.newArrayList();
        try {

            int baseAmount = min * personNum;
            BigDecimal leftAmount = amount.subtract(BigDecimal.valueOf(baseAmount));
            if (leftAmount.compareTo(BigDecimal.ZERO)<0){
                throw new Exception("总金额过小");
            }
            if (leftAmount.compareTo(BigDecimal.ZERO) == 0){
                for (int i=0;i<personNum;i++){
                    list.add(BigDecimal.valueOf(0));
                }
                return list;
            }

            int step = max - min;
            Random random = new Random();
            for (int i=0;i<personNum;i++){
                if (leftAmount.equals(BigDecimal.ZERO)){
                    list.add(BigDecimal.valueOf(min));
                    continue;
                }
                int num = random.nextInt(step+1);
                int c = leftAmount.compareTo(BigDecimal.valueOf(num));
                if (c > 0){
                    list.add(BigDecimal.valueOf(num+min));
                    leftAmount = leftAmount.subtract(BigDecimal.valueOf(num));
                }else if (c == 0){
                    list.add(BigDecimal.valueOf(num+min));
                    leftAmount = leftAmount.subtract(BigDecimal.valueOf(num));
                }else {
                    list.add(leftAmount.add(BigDecimal.valueOf(min)));
                    leftAmount = BigDecimal.ZERO;
                }
            }
            return list;
        } catch (Exception e) {
            log.error("",e);
        }finally {
            rLock.unlock();
        }
        return list;
    }
}