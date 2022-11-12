package com.milisong.oms.service.stock;

import com.farmland.core.util.BeanMapper;
import com.milisong.oms.api.StockService;
import com.milisong.oms.domain.OrderDelivery;
import com.milisong.oms.domain.OrderDeliveryGoods;
import com.milisong.oms.domain.VirtualOrderDelivery;
import com.milisong.oms.domain.VirtualOrderDeliveryGoods;
import org.apache.commons.lang.BooleanUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/11 8:51
 */
public class ShopDailyStockBuilder {

    public static List<StockService.ShopDailyStock> buildIncrementDailyGoodsStocks(Byte source, String shopCode, List<OrderDelivery> deliveryList, List<OrderDeliveryGoods> deliveryGoodsList) {
        return buildIncrementVirtualDailyGoodsStocks(source, shopCode, BeanMapper.mapList(deliveryList, VirtualOrderDelivery.class), BeanMapper.mapList(deliveryGoodsList, VirtualOrderDeliveryGoods.class));
    }

    public static List<StockService.ShopDailyStock> buildIncrementVirtualDailyGoodsStocks(Byte source, String shopCode, List<VirtualOrderDelivery> deliveryList, List<VirtualOrderDeliveryGoods> deliveryGoodsList) {
        return buildVirtualDailyGoodsStocks(true, source, shopCode, deliveryList, deliveryGoodsList);
    }

    public static List<StockService.ShopDailyStock> buildDecrementVirtualDailyGoodsStocks(Byte source, String shopCode, List<VirtualOrderDelivery> deliveryList, List<VirtualOrderDeliveryGoods> deliveryGoodsList) {
        return buildVirtualDailyGoodsStocks(false, source, shopCode, deliveryList, deliveryGoodsList);
    }

    private static List<StockService.ShopDailyStock> buildVirtualDailyGoodsStocks(boolean isIncrement, Byte source, String shopCode, List<VirtualOrderDelivery> deliveryList, List<VirtualOrderDeliveryGoods> deliveryGoodsList) {

        final Map<Long, List<VirtualOrderDeliveryGoods>> goodsByDeliveryIdMap =
                deliveryGoodsList.stream()
                        //过虑组合商品
                        .filter(deliveryGoods -> BooleanUtils.isNotTrue(deliveryGoods.getIsCombo()))
                        //按配送子单ID分组，转换成Map
                        .collect(groupingBy(VirtualOrderDeliveryGoods::getDeliveryId));


        return deliveryList.stream().map(orderDelivery -> {
            Map<String, Integer> goodsStocks = goodsByDeliveryIdMap.get(orderDelivery.getId()).stream()
                    .collect(groupingBy(VirtualOrderDeliveryGoods::getGoodsCode, summingInt(VirtualOrderDeliveryGoods::getGoodsCount)));

            StockService.ShopDailyStock shopDailyStock = new StockService.ShopDailyStock();
            shopDailyStock.setSource(source);
            shopDailyStock.setOrderId(orderDelivery.getOrderId());
            shopDailyStock.setDeliveryId(orderDelivery.getId());
            shopDailyStock.setShopCode(shopCode);
            shopDailyStock.setSaleDate(orderDelivery.getDeliveryDate());
            shopDailyStock.setGoodsStocks(goodsStocks.entrySet().stream()
                    .map(goodsStock -> {
                        if (isIncrement) {
                            return new StockService.GoodsStock(goodsStock.getKey(), goodsStock.getValue());
                        } else {
                            return new StockService.GoodsStock(goodsStock.getKey(), -goodsStock.getValue());
                        }
                    }).collect(Collectors.toList()));
            return shopDailyStock;
        }).collect(Collectors.toList());
    }
}
