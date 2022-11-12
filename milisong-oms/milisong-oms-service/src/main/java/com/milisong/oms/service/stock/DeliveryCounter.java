package com.milisong.oms.service.stock;

import com.farmland.core.cache.RedisCache;
import com.farmland.core.distrib.LockProvider;
import com.milisong.oms.util.OrderRedisKeyUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/25 14:32
 */
@Component
public class DeliveryCounter {

    public void increment(String shopCode, List<DateCounter> dateList) {
        dateList.forEach(dateCounter -> {
            String counterKey = OrderRedisKeyUtils.getDeliveryCounterKey(shopCode, dateCounter.getDate());
            RedisCache.incrBy(counterKey, dateCounter.getCount());
        });
    }

    public void decrement(String shopCode, List<DateCounter> dateList) {
        dateList.forEach(dateCounter -> {
            String counterKey = OrderRedisKeyUtils.getDeliveryCounterKey(shopCode, dateCounter.getDate());
            RedisCache.incrBy(counterKey, -dateCounter.getCount());
        });
    }

    public void lockShopTimezoneCapacity(String shopCode, List<Date> dateList) {
        String[] lockKeys = getLockKeys(shopCode, dateList);
        Lock lock = LockProvider.getMultiLock(lockKeys);
        lock.lock();
    }

    private String[] getLockKeys(String shopCode, List<Date> dateList) {
        List<String> lockKeyList = new ArrayList<>();
        dateList.forEach(date -> {
            lockKeyList.add(OrderRedisKeyUtils.getShopTimezoneCapacityLockKey(shopCode, date));
        });
        return lockKeyList.toArray(new String[lockKeyList.size()]);
    }

    public void unlockShopTimezoneCapacity(String shopCode, List<Date> dateList) {
        String[] lockKeys = getLockKeys(shopCode, dateList);
        Lock lock = LockProvider.getMultiLock(lockKeys);
        lock.unlock();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DateCounter {
        private Date date;
        private int count;
    }
}
