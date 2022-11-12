package com.milisong.ecm.breakfast.web.goods;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.breakfast.goods.api.ShopService;
import com.milisong.ecm.common.api.ShopConfigService;
import com.milisong.oms.api.ShopStockService;
import com.milisong.oms.api.StockService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品对外接口
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/3 20:58
 */
@Slf4j
@RestController
public class GoodsRest {

    @Resource
    private ShopStockService stockService;

    @Resource
    private ShopService shopService;

    @Resource
    private ShopConfigService shopConfigService;

    /**
     * 校验库存
     *
     * @param shopCode  门店编号
     * @param goodsCode 商品编号
     * @param count     校验数量
     * @return 校验结果
     */
    @GetMapping("/v1/goods/verify-stock")
    ResponseResult<Boolean> verifyStock(String shopCode, String goodsCode, int count) {
        boolean hasStock = true;
        try {
            hasStock = stockService.verifyStock(shopCode, System.currentTimeMillis(), goodsCode, count);
        } catch (Exception e) {
            log.error("校验库存出现异常", e);
            return ResponseResult.buildFailResponse();
        }

        return ResponseResult.buildSuccessResponse(hasStock);
    }

    /**
     * 设置在售库存
     *
     * @param shopDailyStock 在售库存信息
     * @return 设置是否成功
     */
    @PostMapping("/v1/goods/set-onsale-stock")
    ResponseResult<?> setOnsaleStock(@RequestBody StockService.ShopDailyStock shopDailyStock) {
        log.info("门店设置库存入参：{}", JSON.toJSONString(shopDailyStock));
        return stockService.configOnSaleStock(Lists.newArrayList(shopDailyStock));
    }

    @PostMapping("/v1/goods/set-onsale-stock-list")
    ResponseResult<?> setOnsaleStockList(@RequestBody List<StockService.ShopDailyStock> shopDailyStockList) {
        log.info("门店批量设置库存入参：{}", JSON.toJSONString(shopDailyStockList));
        return stockService.configOnSaleStock(shopDailyStockList);
    }
}