package com.milisong.oms.service.stock;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.milisong.oms.api.ShopStockService;
import com.milisong.oms.api.StockService;
import com.milisong.oms.dto.ShopOnSaleGoodsDto;
import com.milisong.oms.param.ShopOnSaleGoodsParam;
import com.milisong.oms.util.OrderRedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/22 18:21
 */
@Slf4j
@RestController
public class ShopStockServiceImpl implements ShopStockService {
    @Resource
    private StockService stockService;

    @Override
    public boolean verifyStock(String shopCode, long deliveryDateTimestamp, String goodsCode, int count) {
        return stockService.verifyStock(shopCode, new Date(deliveryDateTimestamp), goodsCode, count);
    }

    @Override
    public Integer getGoodsStock(String shopCode, long saleDateTimestamp, String goodsCode) {
        String stockKey = OrderRedisKeyUtils.getOnsaleGoodsStockKey(shopCode, new Date(saleDateTimestamp), goodsCode);
        String stockValue = RedisCache.get(stockKey);
        return stockValue != null ? Integer.valueOf(stockValue) : 0;
    }

    @Override
    public Map<String, ShopOnSaleGoodsDto> getShopGoodsStock(@RequestBody ShopOnSaleGoodsParam shopOnSaleGoodsParam) {
        log.info("-----------获取商品库存和销量入参：" + JSON.toJSONString(shopOnSaleGoodsParam));
        List<ShopOnSaleGoodsParam.OnSaleGoodsParam> goodsCodes = shopOnSaleGoodsParam.getGoodsCodes();
        List<String> salesKeys = goodsCodes.stream().map(goodsParam -> OrderRedisKeyUtils.getLately30DaysSalesKey(goodsParam.getGoodsCode())).collect(Collectors.toList());
        List<Integer> salesValues = RedisCache.batchGet(salesKeys).stream().map(s -> s != null ? Integer.valueOf(s) : 0).collect(Collectors.toList());
        log.info("销量数据：{}", salesValues);

        //将商品列表中的组合商品编码取出扁平化
        List<String> stockKeys = goodsCodes.stream().flatMap(goodsParam -> {
            if (BooleanUtils.isNotTrue(goodsParam.getIsCombo())) {
                return Stream.of(OrderRedisKeyUtils.getOnsaleGoodsStockKey(shopOnSaleGoodsParam.getShopCode(), shopOnSaleGoodsParam.getSaleDate(), goodsParam.getGoodsCode()));
            }
            return goodsParam.getComboInfos().stream().map(comboInfo -> {
                return OrderRedisKeyUtils.getOnsaleGoodsStockKey(shopOnSaleGoodsParam.getShopCode(), shopOnSaleGoodsParam.getSaleDate(), comboInfo.getGoodsCode());
            });
        }).collect(Collectors.toList());
        log.info("-------------库存keys:{}", stockKeys);
        //一次将所有的单品库存值取出
        LinkedList<Integer> stockQueue = RedisCache.batchGet(stockKeys).stream().map(s -> s != null ? Integer.valueOf(s) : 0).collect(Collectors.toCollection(LinkedList::new));
        log.info("-------------库存keys:{},全销量值队列：{}", stockKeys, stockQueue);
        //计算采购商品的库存
        List<Integer> stockValues = goodsCodes.stream().map(goodsParam -> {
            //单品计算逻辑
            if (BooleanUtils.isNotTrue(goodsParam.getIsCombo())) {
                return stockQueue.poll();
            }
            //组合商品计算逻辑
            /**
             * 假设组合商品由x个M和y个N组成。
             * 单品M的库存:Ms,N的库存:Ns
             * 计算公式：min(Ms/x,Ns/y)
             */
            return goodsParam.getComboInfos().stream().map(comboInfo -> {
                Integer stockValue = stockQueue.poll();
                Integer goodsCount = comboInfo.getGoodsCount();
                log.info("------组合商品---------商品编码:{},库存值：{},可制作组合商品份数：{}", comboInfo.getGoodsCode(), stockValue, stockValue / goodsCount);
                return stockValue / goodsCount;
            }).min(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            }).get();
        }).collect(Collectors.toList());
        log.info("---------------库存：{}，销量：{}", JSON.toJSONString(stockValues), JSON.toJSONString(salesValues));
        return IntStream.range(0, goodsCodes.size())
                .mapToObj(i -> new ShopOnSaleGoodsDto(goodsCodes.get(i).getGoodsCode(), salesValues.get(i), stockValues.get(i) < 0 ? 0 : stockValues.get(i))).collect(Collectors.toMap(ShopOnSaleGoodsDto::getGoodsCode, s -> s, (k1, k2) -> k1));
    }

    @Override
    public ResponseResult<?> configOnSaleStock(@RequestBody List<StockService.ShopDailyStock> shopDailyStockList) {
        return stockService.configOnSaleStock(shopDailyStockList);
    }

    @Override
    public ResponseResult<?> repairOnSaleStock(@RequestBody List<StockService.ShopDailyStock> shopDailyStockList) {
        log.info("-------------修复库存数据：{}", JSON.toJSONString(shopDailyStockList));
//        return stockService.changeGoodsStock(shopDailyStockList);
        stockService.updateGoodsStock(shopDailyStockList);
        return ResponseResult.buildSuccessResponse();
    }


}
