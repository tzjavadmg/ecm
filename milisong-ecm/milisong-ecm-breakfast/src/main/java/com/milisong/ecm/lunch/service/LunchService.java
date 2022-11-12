package com.milisong.ecm.lunch.service;

import com.farmland.core.api.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/4/12   17:01
 *    desc    : 调用午餐的接口
 *    version : v1.0
 * </pre>
 */

@FeignClient("milisong-ecm-lunch")
public interface LunchService {

    @GetMapping("/v1/goods/goods-catalog")
    ResponseResult<?> goodsCatalog(
            @RequestParam(value = "shopCode", required = false) String shopCode,
            @RequestParam(value = "setOnTime", required = false) String setOnTime,
            @RequestParam(value = "categoryCode", required = false) String categoryCode);

    @GetMapping("/v1/goods/get-date")
    ResponseResult<?> getDate(@RequestParam(value = "shopCode", required = false) String shopCode);

    @GetMapping("/v1/goods/shop-info")
    ResponseResult<?> shopInfo(@RequestParam(value = "buildId", required = false) Long buildId);

    @GetMapping("/v1/goods/verify-stock")
    ResponseResult<?> verifyStock(@RequestParam("shopCode") String shopCode,@RequestParam("goodsCode")  String goodsCode, @RequestParam("count") Integer count);

    @GetMapping("/v1/user/is-point")
    ResponseResult<?> isPoint();
}
