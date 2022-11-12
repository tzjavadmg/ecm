package com.milisong.ecm.lunch.web.goods;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.github.jsonzou.jmockdata.JMockData;
import com.github.jsonzou.jmockdata.TypeReference;
import com.milisong.ecm.common.api.ShopConfigService;
import com.milisong.ecm.common.enums.RestEnum;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ecm.common.util.WeekDayUtils;
import com.milisong.ecm.lunch.goods.api.GoodsService;
import com.milisong.ecm.lunch.goods.api.ShopService;
import com.milisong.ecm.lunch.goods.dto.*;
import com.milisong.oms.api.ShopStockService;
import com.milisong.oms.api.StockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

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
    private GoodsService goodsService;

    // @Resource
    // private DeprecatedOrderService orderService;

    @Resource
    private ShopConfigService shopConfigService;

    @Value("${building.default-id}")
    private String DEFAULT_BUILDING_ID;

    @Value("${shop.default-code}")
    private String SHOP_DEFAULT_CODE;

    @Value("${shop.default-categoryCode}")
    private String DEFAULT_CATEGORY_CODE;

    @Autowired
    private RestTemplate restTemplate;
    @Value("${lbs.address-service-url}")
    private String addressServiceUrl;
    @Value("${lbs.convert-service-url}")
    private String convertServiceUrl;


    @PostMapping("/v1/goods/building-list")
    @Deprecated
    ResponseResult<?> setBuildingList(@RequestBody List<BuildingDto> buildingDtoList) {
        return shopService.setBuildings(buildingDtoList);
    }

    /**
     * 获取门店商品信息，首页展示(商品类目，列表，库存等) 商品详情也从该接口拿数据
     *
     * @param shopCode
     * @return
     */
    @GetMapping("/v1/goods/goods-catalog")
    ResponseResult<List<GoodsCatalogDto>> goodsCatalog(
            @RequestParam(value = "shopCode", required = false) String shopCode,
            @RequestParam(value = "setOnTime", required = false) String setOnTime,
            @RequestParam(value = "categoryCode", required = false) String categoryCode) {
        log.info("获取商品信息->>shopCode{},setOnTime{},categoryCode{}", shopCode, setOnTime, categoryCode);
        if (StringUtils.isEmpty(shopCode) || "null".equals(shopCode)) {
            // shopCode = SHOP_DEFAULT_CODE;
            return ResponseResult.buildSuccessResponse(new ArrayList<GoodsCatalogDto>());
        }
        if (StringUtils.isBlank(categoryCode)) {
            categoryCode = DEFAULT_CATEGORY_CODE;
        }

        if (StringUtils.isBlank(setOnTime)) {
            final String tomorrow = "1_0";
            final String today = "0_0";
            String shopOnsaleDateKey = RedisKeyUtils.getShopOnsaleDate(shopCode);
            Set<String> result = RedisCache.setMembers(shopOnsaleDateKey);
            Map<String, String> map = new HashMap<>();
            for (String str : result) {
                int[] array = WeekDayUtils.getTodayOrTomorrow(str); // arr[0] = 天 arr[1]=周
                if (array[0] == -1) {
                    continue;
                }
                map.put(array[0] + "_" + array[1], str);

            }
            Date current = new Date();
            Date cutOffOrderTime = shopConfigService.getTodayLastCutOffTime(shopCode);
            boolean flag = current.compareTo(cutOffOrderTime) > 0;
            if (flag) {
                setOnTime = map.get(tomorrow); // 明天

            } else {
                setOnTime = map.get(today); // 今天
            }
            if (StringUtils.isEmpty(setOnTime)) {
                // 往后推算非工作日取第一天
                List<String> dateList = Lists.newArrayList();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (entry.getKey().equals(today) || entry.getKey().equals(tomorrow)) {
                        continue;
                    }
                    dateList.add(entry.getValue());
                }
                if (CollectionUtils.isNotEmpty(dateList)) {
                    setOnTime = Collections.min(dateList);
                }
            }
        }
        ResponseResult<List<GoodsCatalogDto>> goodsInfo = null;
        try {
            if (StringUtils.isBlank(setOnTime)) {
                goodsInfo = ResponseResult.buildSuccessResponse(new ArrayList<>());
                return goodsInfo;
            }
            goodsInfo = shopService.getShopSaleGoodsDetail(shopCode, setOnTime, categoryCode);
            log.info("获取商品返回结果{}:", JSON.toJSONString(goodsInfo));
            if (!goodsInfo.isSuccess()) {
                log.error("---system error->GoodsRest.goodsCatalog" + JSON.toJSONString(goodsInfo));
                return ResponseResult.buildFailResponse(RestEnum.SYS_ERROR.getCode(), RestEnum.SYS_ERROR.getDesc());
            }
            return goodsInfo;
        } catch (Exception e) {
            log.error("---system error->GoodsRest.goodsInfo,{}", e);
            return ResponseResult.buildFailResponse(RestEnum.SYS_ERROR.getCode(), RestEnum.SYS_ERROR.getDesc());
        }
    }

    /**
     * 获取门店商品信息，通知，首页展示(商品类目，列表，库存等)
     *
     * @param shopCode
     * @return
     */
    @GetMapping("/v2/goods/goods-catalog")
    @Deprecated
    ResponseResult<List<GoodsForDayDto>> goodsCatalogV2(
            @RequestParam(value = "shopCode", required = false) String shopCode,
            @RequestParam(value = "setOnTime", required = false) String setOnTime,
            @RequestParam(value = "categoryCode", required = false) String categoryCode) {
        log.info("获取商品信息->>shopCode{},setOnTime{},categoryCode{}", shopCode, setOnTime, categoryCode);
        List<GoodsForDayDto> list = JMockData.mock(new TypeReference<List<GoodsForDayDto>>() {
        });
        ResponseResult<List<GoodsForDayDto>> result = ResponseResult.buildSuccessResponse();
        result.setData(list);
        return result;

    }

    /**
     * 获取商品详情
     *
     * @param goodsCode
     * @return
     */
    @GetMapping("/v1/goods/goods-details")
    @Deprecated
    ResponseResult<GoodsDetailDto> goodsDetails(@RequestParam(value = "goodsCode", required = true) String goodsCode) {
        GoodsDetailDto mockData = JMockData.mock(GoodsDetailDto.class);
        ResponseResult<GoodsDetailDto> result = ResponseResult.buildSuccessResponse(mockData);

        System.out.println(JSON.toJSONString(result));
        return null;
    }

    /**
     * 接收SCM通知的门店商品信息
     *
     * @param dto
     * @return
     */
    @PostMapping("/v1/goods/set-onsale-goods")
    ResponseResult<Boolean> setOnsaleGoods(@RequestBody Map<String, List<GoodsCatalogDto>> dto) {
        log.info("接收scm商品信息{}", JSON.toJSONString(dto));
        ResponseResult<Boolean> recShogGoods = null;
        try {
            recShogGoods = shopService.receiveShopSaleGoodsDetail(dto);
            if (!recShogGoods.isSuccess()) {
                log.error("---system error->GoodsRest.setOnsaleGoods" + JSON.toJSONString(recShogGoods));
            }
            return recShogGoods;
        } catch (Exception e) {
            log.error("---system error->GoodsRest.setOnsaleGoods,{}", e);
            return ResponseResult.buildFailResponse(RestEnum.SYS_ERROR.getCode(), RestEnum.SYS_ERROR.getDesc());
        }
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

    /**
     * 获取首页商品可售日期
     *
     * @return
     */
    @GetMapping("/v1/goods/get-date")
    ResponseResult<List<WeekDto>> getDate(@RequestParam(value = "shopCode", required = false) String shopCode) {
    	log.info("获取首页商品可售日期{}",shopCode);
    	return goodsService.getDate(shopCode);
    }

}