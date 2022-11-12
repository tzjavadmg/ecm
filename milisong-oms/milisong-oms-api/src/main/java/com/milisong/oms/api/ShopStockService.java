package com.milisong.oms.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.oms.dto.ShopOnSaleGoodsDto;
import com.milisong.oms.param.ShopOnSaleGoodsParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/22 17:57
 */
@FeignClient("milisong-oms-service")
public interface ShopStockService {

    /**
     * 校验需求商品数量库存能否满足
     *
     * @param shopCode          门店编码
     * @param saleDateTimestamp 售卖日期
     * @param goodsCode         商品编码
     * @param count             校验数量
     * @return 校验是否成功
     */
    @PostMapping(value = "/v1/StockService/verifyStock")
    boolean verifyStock(@RequestParam("shopCode") String shopCode, @RequestParam("saleDateTimestamp") long saleDateTimestamp, @RequestParam("goodsCode") String goodsCode, @RequestParam("count") int count);

    /**
     * 获取某个门店某天某个商品的剩余库存
     *
     * @param shopCode
     * @param saleDateTimestamp
     * @param goodsCode
     * @return
     */
    @PostMapping(value = "/v1/StockService/getStock")
    Integer getGoodsStock(@RequestParam("shopCode") String shopCode, @RequestParam("saleDateTimestamp") long saleDateTimestamp, @RequestParam("goodsCode") String goodsCode);

    /**
     * 获取某个门店某天所有商品的月销和剩余库存
     *
     * @param shopOnSaleGoodsParam
     * @return
     */
    @PostMapping(value = "/v1/StockService/getShopGoodsStock")
    Map<String, ShopOnSaleGoodsDto> getShopGoodsStock(@RequestBody ShopOnSaleGoodsParam shopOnSaleGoodsParam);

    /**
     * 配置置门店可售商品库存
     *
     * @param shopDailyStockList 门店各个可售日期的商品库存信息，库存数据量为负值则减少库存，正值增加库存
     * @return 操作是否成功
     */
    @PostMapping(value = "/v1/StockService/configOnSaleStock")
    ResponseResult<?> configOnSaleStock(@RequestBody List<StockService.ShopDailyStock> shopDailyStockList);

    /**
     * 修复门店可售商品库存
     *
     * @param shopDailyStockList 门店各个可售日期的商品库存信息，库存数据量为负值则减少库存，正值增加库存
     * @return 操作是否成功
     */
    @PostMapping(value = "/v1/StockService/repairOnSaleStock")
    ResponseResult<?> repairOnSaleStock(@RequestBody List<StockService.ShopDailyStock> shopDailyStockList);
}
