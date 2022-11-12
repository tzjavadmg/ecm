package com.milisong.ecm.lunch.web.goods;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.lunch.service.LunchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/4/12   14:25
 *    desc    : 午餐商品信息入口
 *    version : v1.0
 * </pre>
 */
@RestController
@Slf4j
@RequestMapping("/lc")
public class GoodsRestLC {

    @Autowired
    private LunchService lunchService;

    /**
     * 获取门店商品信息，首页展示(商品类目，列表，库存等) 商品详情也从该接口拿数据
     *
     * @param shopCode
     * @return
     */
    @GetMapping("/v1/goods/goods-catalog")
    public ResponseResult<?> goodsCatalog(
            @RequestParam(value = "shopCode", required = false) String shopCode,
            @RequestParam(value = "setOnTime", required = false) String setOnTime,
            @RequestParam(value = "categoryCode", required = false) String categoryCode) {
        log.info("获取商品信息->>shopCode={},setOnTime={},categoryCode={}", shopCode, setOnTime, categoryCode);
        return lunchService.goodsCatalog(shopCode,setOnTime,categoryCode);
    }

    /**
     * 获取首页商品可售日期
     *
     * @return
     */
    @GetMapping("/v1/goods/get-date")
    ResponseResult<?> getDate(@RequestParam(value = "shopCode", required = false) String shopCode) {
        log.info("获取首页商品可售日期{}",shopCode);
        return lunchService.getDate(shopCode);
    }

    /**
     * 校验库存
     *
     * @param shopCode  门店编号
     * @param goodsCode 商品编号
     * @param count     校验数量
     * @return 校验结果
     */
    @GetMapping("/v1/goods/verify-stock")
    ResponseResult<?> verifyStock(@RequestParam("shopCode") String shopCode,@RequestParam("goodsCode")  String goodsCode, @RequestParam("count") Integer count){
        return lunchService.verifyStock(shopCode,goodsCode,count);
    }

}
