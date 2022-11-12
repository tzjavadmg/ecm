package com.milisong.oms.util;

import com.farmland.core.cache.RedisCache;
import com.milisong.oms.constant.OrderType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/3 13:31
 */
@Slf4j
public class OrderNoBuilder {

    public static String build(Date orderDate, byte orderType, String shopCode) {
        String datePart = DateFormatUtils.format(orderDate, "yyMMdd");
        String orderTypePart = OrderType.getAbbrByValue(orderType);

        String key = OrderRedisKeyUtils.getOrderNoCounterKey(orderTypePart, shopCode, datePart);
        long flowNo = RedisCache.incrBy(key, 1);
        RedisCache.expire(key, 3, TimeUnit.DAYS);
        String flowNoPart = String.format("%05d", flowNo);
        log.info("流水号：{}", flowNo);
        String orderNo = orderTypePart + shopCode + datePart + flowNoPart;
        log.info("订单号：{}", orderNo);
        return orderNo;
    }
}
