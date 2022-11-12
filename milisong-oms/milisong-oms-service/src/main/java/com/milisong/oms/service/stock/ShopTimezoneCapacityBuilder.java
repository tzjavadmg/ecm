package com.milisong.oms.service.stock;

import com.milisong.oms.api.TimezoneCapacityService;
import com.milisong.oms.domain.VirtualOrderDelivery;
import com.milisong.oms.param.DeliveryTimezoneParam;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/13 11:14
 */
@Slf4j
public class ShopTimezoneCapacityBuilder {

    static List<TimezoneCapacityService.ShopTimezoneCapacity> buildIncrementTimezoneCapacity(Byte source, String shopCode, List<VirtualOrderDelivery> deliveryList, Map<Long, DeliveryTimezoneParam> deliveryTimezoneMap) {
        return buildTimezoneCapacity(true, source, shopCode, deliveryList, deliveryTimezoneMap);
    }

    static List<TimezoneCapacityService.ShopTimezoneCapacity> buildDecrementTimezoneCapacity(Byte source, String shopCode, List<VirtualOrderDelivery> deliveryList,Map<Long, DeliveryTimezoneParam> deliveryTimezoneMap) {
        return buildTimezoneCapacity(false, source, shopCode, deliveryList, deliveryTimezoneMap);
    }

    private static List<TimezoneCapacityService.ShopTimezoneCapacity> buildTimezoneCapacity(boolean isIncrement, Byte source, String shopCode, List<VirtualOrderDelivery> deliveryList,Map<Long, DeliveryTimezoneParam> deliveryTimezoneMap) {
        return deliveryList.stream().map(orderDelivery -> {
            TimezoneCapacityService.ShopTimezoneCapacity shopDailyStock = new TimezoneCapacityService.ShopTimezoneCapacity();
            shopDailyStock.setSource(source);
            shopDailyStock.setOrderId(orderDelivery.getOrderId());
            shopDailyStock.setDeliveryId(orderDelivery.getId());
            shopDailyStock.setShopCode(shopCode);
            return shopDailyStock;
        }).collect(Collectors.toList());
    }
}
