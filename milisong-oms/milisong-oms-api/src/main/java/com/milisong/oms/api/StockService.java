package com.milisong.oms.api;

import com.farmland.core.api.ResponseResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 在售库存校验，库存锁和库存加减操作
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/2 16:34
 */
public interface StockService {

    /**
     * 校验需求商品数量库存能否满足
     *
     * @param shopCode  门店编码
     * @param saleDate  售卖日期
     * @param goodsCode 商品编码
     * @param count     校验数量
     * @return 校验是否成功
     */
    boolean verifyStock(@RequestParam("shopCode") String shopCode, @RequestParam("saleDate") Date saleDate, @RequestParam("goodsCode") String goodsCode, @RequestParam("count") int count);

    /**
     * 验证多天库存
     *
     * @param shopDailyStockList 门店每日商品库存信息
     * @return 按时间顺序返回每天的库存不足提示信息，如果为空表示库存充足
     */
    Map<String, List<String>> verifyMultiDayStock(@RequestBody List<ShopDailyStock> shopDailyStockList);

    /**
     * 锁定库存
     *
     * @param shopDailyStockList 门店每日商品库存信息
     */
    void lockMultiDayStock(List<ShopDailyStock> shopDailyStockList);

    /**
     * 释放库存
     *
     * @param shopDailyStockList 门店每日商品库存信息
     */
    void unlockMultiDayStock(List<ShopDailyStock> shopDailyStockList);

    /**
     * 配置置门店可售商品库存
     *
     * @param shopDailyStockList 门店各个可售日期的商品库存信息，库存数据量为负值则减少库存，正值增加库存
     * @return 操作是否成功
     */
    ResponseResult<?> configOnSaleStock(List<ShopDailyStock> shopDailyStockList);

    /**
     * 添加多天商品库存.
     * 注：使用该方法增加库存前，务必同时使用lockMultiDayGoodsStock和unlockMultiDayGoodsStock来锁定和解锁库存
     *
     * @param shopDailyStockList 门店每日商品库存信息
     */
    void incrementMultiDayStock(List<ShopDailyStock> shopDailyStockList);

    /**
     * 扣减多天商品库存
     * 注：使用该方法扣减库存前，务必同时使用lockMultiDayGoodsStock和unlockMultiDayGoodsStock来锁定和解锁库存，
     * 同时先使用verifyMultiDayStock来校验库存是否满足需求
     *
     * @param shopDailyStockList 门店每日商品库存信息
     */
    void decrementMultiDayStock(List<ShopDailyStock> shopDailyStockList);

    void updateGoodsStock(List<ShopDailyStock> shopDailyStockList);

    ResponseResult<?> changeGoodsStock(List<ShopDailyStock> shopDailyStockList);

    @Getter
    @Setter
    class ShopDailyStock {
        private Byte source;
        private Long orderId;
        private Long deliveryId;
        private String shopCode;
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
        private Date saleDate;
        private List<GoodsStock> goodsStocks;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    class GoodsStock {
        private String goodsCode;
        private Integer goodsCount;
//        /**
//         * 是否组合商品
//         */
//        private Boolean isCombo;
//
//        private List<GoodsStock> goodsStockList;
    }
}
