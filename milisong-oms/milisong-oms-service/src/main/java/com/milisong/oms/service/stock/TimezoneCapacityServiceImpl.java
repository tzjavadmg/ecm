package com.milisong.oms.service.stock;

import com.farmland.core.cache.RedisCache;
import com.farmland.core.distrib.LockProvider;
import com.milisong.oms.api.TimezoneCapacityService;
import com.milisong.oms.util.OrderRedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/12 10:53
 */
@Slf4j
@Service
public class TimezoneCapacityServiceImpl implements TimezoneCapacityService {

    @Override
    public void lockTimezoneCapacity(List<ShopTimezoneCapacity> shopTimezoneCapacityList) {
        List<String> lockKeys = getLockKeys(shopTimezoneCapacityList);
        Lock lock = LockProvider.getMultiLock(lockKeys);
        lock.lock();
    }

    @Override
    public void unlockTimezoneCapacity(List<ShopTimezoneCapacity> shopTimezoneCapacityList) {
        List<String> lockKeys = getLockKeys(shopTimezoneCapacityList);
        Lock lock = LockProvider.getMultiLock(lockKeys);
        lock.unlock();
    }

    private List<String> getLockKeys(List<ShopTimezoneCapacity> shopTimezoneCapacityList) {
        return shopTimezoneCapacityList.stream().map(shopTimezoneCapacity -> {
            return OrderRedisKeyUtils.getShopTimezoneCapacityLockKey(shopTimezoneCapacity.getShopCode(), shopTimezoneCapacity.getSaleDate());
        }).collect(Collectors.toList());
    }

    @Override
    public void incrementTimezoneCapacity(List<ShopTimezoneCapacity> shopTimezoneCapacityList) {
        shopTimezoneCapacityList.forEach(shopTimezoneCapacity -> {
            String counterKey = OrderRedisKeyUtils.getDeliveryCounterKey(shopTimezoneCapacity.getShopCode(), shopTimezoneCapacity.getSaleDate());
            RedisCache.incrBy(counterKey, shopTimezoneCapacity.getGoodsCount());
        });
    }

    @Override
    public void decrementTimezoneCapacity(List<ShopTimezoneCapacity> shopTimezoneCapacityList) {
        shopTimezoneCapacityList.forEach(shopTimezoneCapacity -> {
            String counterKey = OrderRedisKeyUtils.getDeliveryCounterKey(shopTimezoneCapacity.getShopCode(), shopTimezoneCapacity.getSaleDate());
            RedisCache.incrBy(counterKey, -shopTimezoneCapacity.getGoodsCount());
        });
    }
}
