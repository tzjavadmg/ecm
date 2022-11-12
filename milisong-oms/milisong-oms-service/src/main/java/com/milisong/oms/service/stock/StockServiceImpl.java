package com.milisong.oms.service.stock;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.exception.BusinessException;
import com.milisong.oms.api.StockService;
import com.milisong.oms.domain.GoodsStockWater;
import com.milisong.oms.mapper.GoodsStockWaterMapper;
import com.milisong.oms.util.OrderRedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;


/**
 * 商品库存相关服务<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/2 17:16
 */
@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class StockServiceImpl implements StockService {

    /**
     * 库存过期时间
     */
    private final int STOCK_EXPIRED_TIME = 30;
    @Resource
    private GoodsStockWaterMapper goodsStockWaterMapper;

    @Override
    public boolean verifyStock(String shopCode, Date saleDate, String goodsCode, int count) {
        String onSaleGoodsStockKey = OrderRedisKeyUtils.getOnsaleGoodsStockKey(shopCode, saleDate, goodsCode);
        String countStr = RedisCache.get(onSaleGoodsStockKey);
        log.info("库存校验  key：{},库存量：{}", onSaleGoodsStockKey, countStr);
        boolean hasStock = false;
        if (StringUtils.isNotEmpty(countStr)) {
            hasStock = Integer.valueOf(countStr) >= count;
        }
        log.info("##############库存校验结果：{},库存值：{},校验值：{}", hasStock, countStr, count);
        return hasStock;
    }


    @Override
    public Map<String, List<String>> verifyMultiDayStock(List<ShopDailyStock> shopDailyStockList) {
        log.info("-------------批量校验库存，库存数据：{}", JSON.toJSONString(shopDailyStockList));
        Map<String, List<String>> resultMap = new HashMap<>();
        shopDailyStockList.stream().filter(shopDailyStock -> CollectionUtils.isNotEmpty(shopDailyStock.getGoodsStocks())).forEach(shopDailyStock -> {
            String shopCode = shopDailyStock.getShopCode();
            Date saleDate = shopDailyStock.getSaleDate();
            String dateString = DateFormatUtils.format(saleDate, "yyyy-MM-dd");
            shopDailyStock.getGoodsStocks().stream().filter(goodsStock -> goodsStock.getGoodsCount() < 0).forEach(goodsStock -> {
                int stockCount = goodsStock.getGoodsCount();
                log.info("-------------本次需要扣减库存数量：{}", stockCount);
                if (!verifyStock(shopCode, saleDate, goodsStock.getGoodsCode(), Math.abs(stockCount))) {
                    List<String> list = resultMap.computeIfAbsent(dateString, k -> new ArrayList<>());
                    list.add(goodsStock.getGoodsCode());
                    log.info("-------------多天批量校验库存失败，商品代码：{},数量{}", goodsStock.getGoodsCode(), stockCount);
                }
            });
        });
        log.info("-------------库存校验结果：{}", JSON.toJSONString(resultMap));
        return resultMap;
    }

    @Override
    public void lockMultiDayStock(List<ShopDailyStock> shopDailyStockList) {
        List<String> lockKeys = getMultiDayLockKeys(shopDailyStockList);
        log.info("组合锁，keys:{}", JSON.toJSONString(lockKeys));
        Lock lock = LockProvider.getMultiLock(lockKeys);
        lock.lock();//FIXME tryLock
    }

    private List<String> getMultiDayLockKeys(List<ShopDailyStock> shopDailyStockList) {
        List<String> lockKeys = new ArrayList<>();
        if (CollectionUtils.isEmpty(shopDailyStockList)) {
            throw BusinessException.build("", "拼装组合锁key,参数为空！");
        }

        shopDailyStockList.stream().filter(shopDailyStock -> CollectionUtils.isNotEmpty(shopDailyStock.getGoodsStocks())).forEach(shopDailyStock -> {
            String shopCode = shopDailyStock.getShopCode();
            Date saleDate = shopDailyStock.getSaleDate();
            shopDailyStock.getGoodsStocks().forEach(goodsStock -> {
                lockKeys.add(OrderRedisKeyUtils.getOnsaleGoodsStockLockKey(shopCode, saleDate, goodsStock.getGoodsCode()));
            });
        });
        return lockKeys;
    }

    @Override
    public void unlockMultiDayStock(List<ShopDailyStock> shopDailyStockList) {
        List<String> lockKeys = getMultiDayLockKeys(shopDailyStockList);
        Lock lock = LockProvider.getMultiLock(lockKeys);
        lock.unlock();
    }

    private void saveStockWater(ShopDailyStock shopDailyStock, String goodsCode, int goodsCount, int curStockCount) {
        Long waterId = IdGenerator.nextId();
        log.info("~~~~~~库存流水~~~~~~，订单iD：{}，配送ID：{}，门店编号：{}，销售日期：{}，库存量：{},商品编码：{},商品数量：{}，修改后库存：{}",
                shopDailyStock.getOrderId(), shopDailyStock.getDeliveryId(), shopDailyStock.getShopCode(), DateFormatUtils.format(shopDailyStock.getSaleDate(), "yyyy-MM-dd"),
                curStockCount, goodsCode, goodsCount, curStockCount + goodsCount);
        //TODO 非核心业务，异步化
        GoodsStockWater goodsStockWater = new GoodsStockWater();
        goodsStockWater.setId(waterId);
        goodsStockWater.setSource(shopDailyStock.getSource());
        goodsStockWater.setGoodsCode(goodsCode);
        goodsStockWater.setGoodsCount(goodsCount);
        goodsStockWater.setOldStockCount(curStockCount);
        goodsStockWater.setNewStockCount(curStockCount + goodsCount);
        goodsStockWater.setShopCode(shopDailyStock.getShopCode());
        goodsStockWater.setDeliveryDate(shopDailyStock.getSaleDate());
        goodsStockWater.setOrderId(shopDailyStock.getOrderId());
        goodsStockWater.setDeliveryId(shopDailyStock.getDeliveryId());
        goodsStockWaterMapper.insertSelective(goodsStockWater);
    }


    @Override
    public void incrementMultiDayStock(List<ShopDailyStock> shopDailyStockList) {
        updateGoodsStock(shopDailyStockList);
    }

    @Override
    public void decrementMultiDayStock(List<ShopDailyStock> shopDailyStockList) {
        updateGoodsStock(shopDailyStockList);
    }

    @Override
    public ResponseResult<?> configOnSaleStock(List<ShopDailyStock> shopDailyStockList) {
        log.info("--------@@@@--------设置门店在售库存入参：{}", JSON.toJSONString(shopDailyStockList));
        lockMultiDayStock(shopDailyStockList);
        log.info("--------@@@@@@@@----设置在售库存----------获取联合锁");
        try {
            Map<String, List<String>> resultMap = verifyMultiDayStock(shopDailyStockList);
            if (MapUtils.isNotEmpty(resultMap)) {
                log.warn("------设置在售库存  库存校验失败：校验结果：{}", JSON.toJSONString(resultMap));
                return ResponseResult.buildFailResponse("", resultMap);
            }
            updateGoodsStock(shopDailyStockList);
            return ResponseResult.buildSuccessResponse();
        } finally {
            unlockMultiDayStock(shopDailyStockList);
            log.info("--------@@@@@@@@----设置在售库存------------离开联合锁");
        }
    }

    @Override
    public void updateGoodsStock(List<ShopDailyStock> shopDailyStockList) {
        shopDailyStockList.stream().filter(shopDailyStock -> CollectionUtils.isNotEmpty(shopDailyStock.getGoodsStocks())).forEach(shopDailyStock -> {
            String shopCode = shopDailyStock.getShopCode();
            Date saleDate = shopDailyStock.getSaleDate();
            shopDailyStock.getGoodsStocks().forEach(goodsStock -> {
                String goodsCode = goodsStock.getGoodsCode();
                int goodsCount = goodsStock.getGoodsCount();
                String stockKey = OrderRedisKeyUtils.getOnsaleGoodsStockKey(shopCode, saleDate, goodsCode);
                String curStockStr = RedisCache.get(stockKey);
                int curStockCount = 0;
                if (StringUtils.isNotEmpty(curStockStr)) {
                    curStockCount = Integer.valueOf(curStockStr);
                }
                int modifiedStockCount = curStockCount + goodsCount;
                saveStockWater(shopDailyStock, goodsCode, goodsCount, curStockCount);
                RedisCache.setEx(stockKey, String.valueOf(modifiedStockCount), STOCK_EXPIRED_TIME, TimeUnit.DAYS);
            });
        });
    }

    @Override
    public ResponseResult<?> changeGoodsStock(List<ShopDailyStock> shopDailyStockList) {
        log.info("------------更新商品库存，入参：{}", JSON.toJSONString(shopDailyStockList));
        for (ShopDailyStock shopDailyStock : shopDailyStockList) {
            String shopCode = shopDailyStock.getShopCode();
            Date saleDate = shopDailyStock.getSaleDate();
            for (GoodsStock goodsStock : shopDailyStock.getGoodsStocks()) {
                String goodsCode = goodsStock.getGoodsCode();
                int goodsCount = goodsStock.getGoodsCount();
                String stockKey = OrderRedisKeyUtils.getOnsaleGoodsStockKey(shopCode, saleDate, goodsCode);
                String curStockStr = RedisCache.get(stockKey);
                if (StringUtils.isEmpty(curStockStr)) {
                    return ResponseResult.buildFailResponse("", "【" + stockKey + "】不存在！");
                }
            }
        }
        updateGoodsStock(shopDailyStockList);
        return ResponseResult.buildSuccessResponse();
    }
}
