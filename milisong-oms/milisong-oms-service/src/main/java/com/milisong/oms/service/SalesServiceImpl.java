package com.milisong.oms.service;

import com.farmland.core.cache.RedisCache;
import com.milisong.oms.api.SalesService;
import com.milisong.oms.mapper.OrderDeliveryGoodsMapper;
import com.milisong.oms.util.OrderRedisKeyUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 销量计算
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/14 9:51
 */
@RestController
public class SalesServiceImpl implements SalesService {

    @Resource
    private OrderDeliveryGoodsMapper orderDeliveryGoodsMapper;

    @Override
    public Integer getLately30DaysSales(String goodsCode) {
        String key = OrderRedisKeyUtils.getLately30DaysSalesKey(goodsCode);
        String dailySalesString = RedisCache.get(key);
        Integer dailySales = 0;
        if (StringUtils.isNotEmpty(dailySalesString)) {
            dailySales = Integer.valueOf(dailySalesString);
        }
        return dailySales;
    }

    @Override
    public void statLately29DaysSales() {
        //5天内所有可售商品，最近29天的销量
        List<Map<String, Object>> salesList = orderDeliveryGoodsMapper.statLately29DaysSales();

        salesList.forEach(map -> {
            String goodsCode = MapUtils.getString(map, "goodsCode");
            Integer salesCount = MapUtils.getInteger(map, "salesCount");

            String key = OrderRedisKeyUtils.getLately30DaysSalesKey(goodsCode);
            if (salesCount != null) {
                RedisCache.set(key, salesCount.toString());
            }
        });
    }
}
