package com.milisong.oms.service.stock;

import com.alibaba.fastjson.JSON;
import com.farmland.core.cache.RedisCache;
import com.milisong.oms.domain.OrderDeliveryGoods;
import com.milisong.oms.util.OrderRedisKeyUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/24 21:17
 */
@Slf4j
public class MonthlySalesUtils {

    static public void monthlySales(List<OrderDeliveryGoods> deliveryGoods) {
        //记录商品月销量
        if (deliveryGoods != null) {
            deliveryGoods.forEach(goods -> {
                String goodsCode = goods.getGoodsCode();
                Integer goodsCount = goods.getGoodsCount();
                String lately30DaysSalesKey = OrderRedisKeyUtils.getLately30DaysSalesKey(goodsCode);
                RedisCache.incrBy(lately30DaysSalesKey, new Long(goodsCount));
            });
        }
        log.info("==============记录月销：{}", JSON.toJSONString(deliveryGoods));
    }

}
