package com.milisong.oms.service.groupbuy;

import com.alibaba.fastjson.JSON;
import com.farmland.core.util.BeanMapper;
import com.google.common.collect.Lists;
import com.milisong.oms.api.StockService;
import com.milisong.oms.constant.GoodsStockSource;
import com.milisong.oms.domain.GroupBuyOrderDelivery;
import com.milisong.oms.domain.GroupBuyOrderDeliveryGoods;
import com.milisong.oms.param.GroupBuyOrderDeliveryGoodsParam;
import com.milisong.oms.param.GroupBuyOrderDeliveryParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/20 19:48
 */
@Slf4j
public class DailyStocksBuilder {

    public static List<StockService.ShopDailyStock> buildShopDailyStocks(String shopCode, GroupBuyOrderDelivery groupBuyOrderDelivery, List<GroupBuyOrderDeliveryGoods> groupBuyOrderDeliveryGoodsList) {
        GroupBuyOrderDeliveryParam groupBuyOrderDeliveryParam = BeanMapper.map(groupBuyOrderDelivery, GroupBuyOrderDeliveryParam.class);
        groupBuyOrderDeliveryParam.setDeliveryId(groupBuyOrderDelivery.getId());

        GroupBuyOrderDeliveryGoodsParam mainGoods = null;
        List<GroupBuyOrderDeliveryGoodsParam> subGoodsList = Lists.newArrayList();
        for (GroupBuyOrderDeliveryGoods goods : groupBuyOrderDeliveryGoodsList) {
            if (BooleanUtils.isTrue(goods.getIsCombo()) || (BooleanUtils.isNotTrue(goods.getIsCombo()) && StringUtils.isEmpty(goods.getComboGoodsCode()))) {
                mainGoods = BeanMapper.map(goods, GroupBuyOrderDeliveryGoodsParam.class);
            } else {
                subGoodsList.add(BeanMapper.map(goods, GroupBuyOrderDeliveryGoodsParam.class));
            }
        }
        if (CollectionUtils.isNotEmpty(subGoodsList)) {
            mainGoods.setDeliveryGoodsParamList(subGoodsList);
        }
        return buildShopDailyStocks(true, shopCode, GoodsStockSource.CANCEL_GROUP_BUY_ORDER.getValue(), groupBuyOrderDeliveryParam, mainGoods);
    }

    public static List<StockService.ShopDailyStock> buildShopDailyStocks(String shopCode, GroupBuyOrderDeliveryParam groupBuyOrderDeliveryParam, GroupBuyOrderDeliveryGoodsParam groupBuyOrderDeliveryGoodsParam) {
        return buildShopDailyStocks(false, shopCode, GoodsStockSource.CREATE_GROUP_BUY_ORDER.getValue(), groupBuyOrderDeliveryParam, groupBuyOrderDeliveryGoodsParam);
    }

    public static List<StockService.ShopDailyStock> buildShopDailyStocks(boolean isIncrement, String shopCode, Byte source, GroupBuyOrderDeliveryParam groupBuyOrderDeliveryParam, GroupBuyOrderDeliveryGoodsParam groupBuyOrderDeliveryGoodsParam) {
        List<StockService.GoodsStock> goodsStockList = new ArrayList<>();
        log.info("---------------商品入参信息-------------{}", JSON.toJSONString(groupBuyOrderDeliveryGoodsParam));
        if (groupBuyOrderDeliveryGoodsParam.getIsCombo()) {
            groupBuyOrderDeliveryGoodsParam.getDeliveryGoodsParamList().forEach(goods -> {
                int goodsCount = isIncrement ? goods.getGoodsCount() : -goods.getGoodsCount();
                goodsStockList.add(new StockService.GoodsStock(goods.getGoodsCode(), goodsCount));
            });
        } else {
            int goodsCount = isIncrement ? groupBuyOrderDeliveryGoodsParam.getGoodsCount() : -groupBuyOrderDeliveryGoodsParam.getGoodsCount();
            goodsStockList.add(new StockService.GoodsStock(groupBuyOrderDeliveryGoodsParam.getGoodsCode(), goodsCount));
        }
        log.info("---------------商品信息-------------{}", JSON.toJSONString(goodsStockList.size()));
        StockService.ShopDailyStock shopDailyStock = new StockService.ShopDailyStock();
        shopDailyStock.setGoodsStocks(goodsStockList);
        shopDailyStock.setShopCode(shopCode);
        shopDailyStock.setSaleDate(groupBuyOrderDeliveryParam.getDeliveryDate());
        shopDailyStock.setSource(source);
        shopDailyStock.setOrderId(groupBuyOrderDeliveryParam.getOrderId());
        shopDailyStock.setDeliveryId(groupBuyOrderDeliveryParam.getDeliveryId());
        log.info("---------------锁库存信息----------------{}", JSON.toJSONString(shopDailyStock));
        return Lists.newArrayList(shopDailyStock);
    }
}
