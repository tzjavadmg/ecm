package com.milisong.ecm.lunch.goods.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.util.BeanMapper;
import com.google.common.collect.Maps;
import com.milisong.ecm.common.api.ShopConfigService;
import com.milisong.ecm.common.dto.BannerDto;
import com.milisong.ecm.common.dto.ShopConfigDto;
import com.milisong.ecm.common.util.DateUtils;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ecm.lunch.goods.api.ShopService;
import com.milisong.ecm.lunch.goods.constants.GoodsStatus;
import com.milisong.ecm.lunch.goods.constants.ShopReqEnum;
import com.milisong.ecm.lunch.goods.dto.BuildingDto;
import com.milisong.ecm.lunch.goods.dto.GoodsCatalogDto;
import com.milisong.ecm.lunch.goods.dto.GoodsDto;
import com.milisong.ecm.lunch.goods.dto.ShopDisplayDto;
import com.milisong.ecm.lunch.goods.dto.WeekDto;
import com.milisong.ecm.lunch.goods.mapper.DateMapper;
import com.milisong.ecm.lunch.goods.util.RedisShopUtils;
import com.milisong.oms.api.SalesService;
import com.milisong.oms.api.ShopStockService;
import com.milisong.oms.dto.ShopOnSaleGoodsDto;
import com.milisong.oms.param.ShopOnSaleGoodsParam;
import com.milisong.oms.param.ShopOnSaleGoodsParam.OnSaleGoodsParam;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/9/3   11:42
 *    desc    : ?????????????????????
 *    version : v1.0
 * </pre>
 */
@Slf4j
@Service
@RestController
public class ShopServiceImpl implements ShopService {

    @Autowired
    private RestTemplate restTemplate;
    @Resource
    private DateMapper dateMapper;
    @Resource
    private SalesService salesService;
    @Resource
    private ShopConfigService shopConfigService;

    @Resource
    private ShopStockService shopStockService;

    //???SCM????????????????????????
    @Value("${scm.shop.info-url}")
    private String SCM_SHOP_INFO_URL;
    //???SCM????????????????????????
    @Value("${scm.shop.info-byShopCode-url}")
    private String SCM_SHOP_INFO_BYSHOPCODE_URL;

    @Value("${building.default-id}")
    private String DEFAULT_BUILDING_ID;

    @Value("${shop.default-categoryCode}")
    private String DEFAULT_CATEGORY_CODE;

    @Value("${share.pictureUrl}")
    private String PICTURE_URL;

    @Value("${share.title}")
    private String TITLE;


    @Override
    public ResponseResult<ShopDisplayDto> getShopInfo(@RequestParam("buildId") Long buildId) {
        log.info("?????????????????????????????????ID???{}", buildId);
        if (buildId == null) {
            buildId = Long.parseLong(DEFAULT_BUILDING_ID);
        }

        String buildingKey = RedisKeyUtils.getBuildingKey(buildId);
        String buildingResult = RedisCache.get(buildingKey);
        BuildingDto buildingDto = JSONObject.parseObject(buildingResult, BuildingDto.class);
        ShopDisplayDto displayDto = new ShopDisplayDto();
        String shopCode = null;
        Short buildingStatus = null;
        if (buildingDto != null) {
            shopCode = buildingDto.getShopCode();
            buildingStatus = buildingDto.getStatus();//????????????
            displayDto.setName(buildingDto.getName()); //????????????
            displayDto.setCode(shopCode);//??????code
        }
        List<BannerDto> bannerList = shopConfigService.getBanner(shopCode);
        bannerList.sort(Comparator.comparingInt(BannerDto::getWeight).reversed());
        displayDto.setBuildingStatus(buildingStatus);
        ShopConfigDto.DeliveryTimezone  deliveryTimezone = shopConfigService.getLastDeliveryTimezone(shopCode);
        displayDto.setInterceptingTime(deliveryTimezone.getCutoffTime());//????????????
        displayDto.setBannerList(bannerList);//banner?????????????????????URL
        displayDto.setDeliveryCostAmount(shopConfigService.getDeliveryOriginalPrice(shopCode));//?????????
        displayDto.setDeliveryCostDiscountAmount(shopConfigService.getDeliveryDiscountPrice(shopCode));//??????????????????
        displayDto.setPictureUrl(shopConfigService.getSharePicture());//?????????????????????
        displayDto.setTitle(shopConfigService.getShareTitle());//?????????????????????
        displayDto.setPackageAmount(shopConfigService.getPackageOriginalPrice(shopCode));//?????????
        displayDto.setPackageDiscountAmount(shopConfigService.getPackageDiscountPrice(shopCode));//??????????????????
        displayDto.setGoodsPosterUrl(shopConfigService.getGoodsPosterUrl());//????????????
        log.info("?????????????????????{}", JSON.toJSONString(displayDto));
        return ResponseResult.buildSuccessResponse(displayDto);
    }

    @Override
    public ResponseResult<ShopDisplayDto> getShopInfoByCode(@RequestParam("code") String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        JSONObject jsonObject = restTemplate.getForObject(SCM_SHOP_INFO_BYSHOPCODE_URL, JSONObject.class, params);
        Boolean isSuccess = jsonObject.getBoolean("success");
        JSONObject data = jsonObject.getJSONObject("data");
        if (!isSuccess || data == null) {
            return ResponseResult.buildFailResponse(ShopReqEnum.SHOP_GETINFO_FROM_SCM_ERROR.getCode(), ShopReqEnum.SHOP_GETINFO_FROM_SCM_ERROR.getDesc(), jsonObject.getString("message"));
        }
        ShopDisplayDto displayDto = BeanMapper.map(data, ShopDisplayDto.class);
        displayDto.setInterceptingTime(formatTime(displayDto.getInterceptingTime()));
        displayDto.setPredictArriveStartTime(formatTime(displayDto.getPredictArriveStartTime()));
        displayDto.setPredictArriveEndTime(formatTime(displayDto.getPredictArriveEndTime()));
        return ResponseResult.buildSuccessResponse(displayDto);
    }

    @Override
    public ResponseResult<List<GoodsCatalogDto>> getShopSaleGoodsDetail(String shopCode, String setOnTime, String categoryCode) {
        List<GoodsCatalogDto> shopGoodsMsg = RedisShopUtils.getShopGoodsMsg(shopCode, categoryCode, setOnTime);
        if (shopGoodsMsg == null || shopGoodsMsg.size() == 0) {
            return ResponseResult.buildSuccessResponse(shopGoodsMsg);
        }
        // ???????????????????????????,???????????????0,????????????
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date setOnTimeConver = null;
        try {
            setOnTimeConver = format.parse(setOnTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dealShopStock(shopCode, setOnTimeConver, shopGoodsMsg);
        //????????????????????????????????????
        //        filterShopGoods(shopGoodsMsg);
        //???????????????
        sortShopGoods(shopGoodsMsg);
        return ResponseResult.buildSuccessResponse(shopGoodsMsg);
    }

    @Override
    public ResponseResult<Boolean> receiveShopSaleGoodsDetail(@RequestBody Map<String, List<GoodsCatalogDto>> dto) {
        List<Date> weekListDate = new ArrayList<>();
        List<WeekDto> weekList = dateMapper.getDate();
        for (WeekDto week : weekList) {
            weekListDate.add(week.getDate());
        }
        for (Map.Entry<String, List<GoodsCatalogDto>> entry : dto.entrySet()) {
            String shopCode = entry.getKey(); //????????????
            List<GoodsCatalogDto> cataList = entry.getValue(); //??????list
            if (CollectionUtils.isNotEmpty(cataList) && cataList.size() > 0) {
                for (GoodsCatalogDto cata : cataList) {
                    List<GoodsDto> goodsList = cata.getGoods();
                    Map<String, List<GoodsDto>> map = Maps.newHashMap();
                    for (GoodsDto goods : goodsList) { //??????List
                        Date beginDate = goods.getBeginDate();
                        Date endDate = goods.getEndDate();
                        int detailStatus = goods.getDetailStatus();
                        if (GoodsStatus.DETAIL_STATUS_USING.getValue() == detailStatus) {
                            List<Date> dateList = DateUtils.getDatesBetweenTwoDate(beginDate, endDate); //???????????????????????????????????????????????????
                            for (Date date : dateList) {
                                if (weekListDate.contains(date)) { //??????????????????????????????????????????
                                    String stringDate = DateUtils.formateDateToString(date);
                                    List<GoodsDto> goodscataList = map.get(stringDate);
                                    if (CollectionUtils.isEmpty(goodscataList)) {
                                        goodscataList = new ArrayList<>();
                                    }
                                    goodscataList.add(goods);
                                    map.put(stringDate, goodscataList);
                                }
                            }
                        }
                    }
                    //??????????????????
                    Map<String, List<GoodsCatalogDto>> categoryMap = Maps.newHashMap();
                    for (Map.Entry<String, List<GoodsDto>> entryMap : map.entrySet()) {
                        List<GoodsCatalogDto> categoryList = new ArrayList<>();
                        GoodsCatalogDto categoryDto = new GoodsCatalogDto();
                        categoryDto.setGoods(entryMap.getValue());
                        categoryDto.setCategoryCode(cata.getCategoryCode());
                        categoryDto.setCategoryName(cata.getCategoryName());
                        categoryDto.setChildren(cata.getChildren());
                        categoryDto.setHasChild(cata.isHasChild());
                        categoryDto.setWeight(cata.getWeight());
                        categoryList.add(categoryDto);
                        categoryMap.put(entryMap.getKey(), categoryList);
                    }
                    RedisShopUtils.cacheShopGoodsForDay(cata.getCategoryCode(), shopCode, categoryMap, cata.getGoods());
                }
            }
        }
        return ResponseResult.buildSuccessResponse(true);
    }

    @Override
    public ResponseResult<GoodsDto> getGoodsInfo(@RequestParam("shopCode") String shopCode, @RequestParam("setOnTime") Long setOnTime, @RequestParam("goodsCode") String goodsCode) {
        log.info("??????????????????????????????,shopCode={},setOnTime={},goodsCode={}",shopCode,setOnTime,goodsCode);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String setOnTimeConver = format.format(new Date(setOnTime));
        List<GoodsCatalogDto> shopGoodsMsg = RedisShopUtils.getShopGoodsMsg(shopCode, DEFAULT_CATEGORY_CODE, setOnTimeConver);
        if (shopGoodsMsg == null || shopGoodsMsg.size() == 0) {
            return ResponseResult.buildFailResponse(ShopReqEnum.SHOP_GOODS_BYCODE_INFO_NULL.getCode(), ShopReqEnum.SHOP_GOODS_BYCODE_INFO_NULL.getDesc());
        }
        GoodsCatalogDto goodsCatalogDto = shopGoodsMsg.get(0);
        List<GoodsDto> goodsList = goodsCatalogDto.getGoods();
        if (goodsList == null || goodsList.size() == 0) {
            return ResponseResult.buildFailResponse(ShopReqEnum.SHOP_GOODS_BYCODE_INFO_NULL.getCode(), ShopReqEnum.SHOP_GOODS_BYCODE_INFO_NULL.getDesc());
        }
        GoodsDto goods = null;
        for (GoodsDto g : goodsList) {
            if (goodsCode.equals(g.getCode())) {
                goods = g;
                break;
            }
        }
        if (goods == null) {
            return ResponseResult.buildFailResponse(ShopReqEnum.SHOP_GOODS_BYCODE_INFO_NULL.getCode(), ShopReqEnum.SHOP_GOODS_BYCODE_INFO_NULL.getDesc());
        }
        return ResponseResult.buildSuccessResponse(goods);
    }

    //@Override
    //public Date getCutOffOrderTime(String shopCode) {
    //    Calendar cutOffOrderCal = Calendar.getInstance();
    //    try {
    //        String cutOffOrderTimeStr = RedisCache.get("config:cutoffTime");
    //        log.info("=============?????????????????????{}", cutOffOrderTimeStr);
    //        String[] datePart = cutOffOrderTimeStr.split(":");
    //        cutOffOrderCal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(datePart[0]));
    //        cutOffOrderCal.set(Calendar.MINUTE, Integer.valueOf(datePart[1]));
    //    } catch (Exception e) {
    //        log.error("?????????????????????????????????????????????????????????10.30");
    //        cutOffOrderCal.set(Calendar.HOUR_OF_DAY, 10);
    //        cutOffOrderCal.set(Calendar.MINUTE, 30);
    //    }
    //    cutOffOrderCal.set(Calendar.SECOND, 0);
    //    cutOffOrderCal.set(Calendar.MILLISECOND, 0);
    //    log.info("=============?????????????????????{}", cutOffOrderCal.getTime());
    //    return cutOffOrderCal.getTime();
    //}

    private void dealShopStock(String shopCode, Date setOnTime, List<GoodsCatalogDto> shopGoodsMsg) {
        for (GoodsCatalogDto category : shopGoodsMsg) {
            List<GoodsDto> goodsList = category.getGoods();
            ShopOnSaleGoodsParam  shopOnSaleGoodsParam = new ShopOnSaleGoodsParam();
            shopOnSaleGoodsParam.setShopCode(shopCode);
            shopOnSaleGoodsParam.setSaleDate(setOnTime);
            List<String> goodsCodes=goodsList.stream().map(GoodsDto::getCode).collect(Collectors.toList());
            List<OnSaleGoodsParam> goodsCodeList = Lists.newArrayList();
            for (String goodsCode : goodsCodes) {
                OnSaleGoodsParam saleGoodsParam = new OnSaleGoodsParam();
                saleGoodsParam.setGoodsCode(goodsCode);
                saleGoodsParam.setIsCombo(false);
                saleGoodsParam.setComboInfos(new ArrayList<>());
                goodsCodeList.add(saleGoodsParam);
            }
            shopOnSaleGoodsParam.setGoodsCodes(goodsCodeList);
            log.info("?????????????????????????????????{}",JSON.toJSONString(shopOnSaleGoodsParam));
            Map<String, ShopOnSaleGoodsDto> resultMap = shopStockService.getShopGoodsStock(shopOnSaleGoodsParam);
            log.info("???????????????????????????????????????{}",JSON.toJSONString(resultMap));
            for (GoodsDto goods : goodsList) {
                ShopOnSaleGoodsDto shopOnSaleGoods = resultMap.get(goods.getCode());
                goods.setMonthSale(shopOnSaleGoods.getMonthlySales());
                goods.setStock(shopOnSaleGoods.getStock());

            }
        }
    }

    /**
     * ??????????????????????????????,?????????????????????id???????????????
     *
     * @param shopGoodsMsg
     */
    private static void sortShopGoods(List<GoodsCatalogDto> shopGoodsMsg) {
        List<GoodsDto> goodsList = shopGoodsMsg.get(0).getGoods();
        //?????????0?????????
        List<GoodsDto> stockZero = goodsList.stream().filter(stock -> stock.getStock() == 0).distinct().collect(Collectors.toList());
        //????????????0?????????
        List<GoodsDto> stockUnZero = goodsList.stream().filter(stcok -> stcok.getStock() != 0).distinct().collect(Collectors.toList());
        //?????????????????????
        stockUnZero.sort(Comparator.comparingInt(GoodsDto::getWeight).reversed());
        stockUnZero.addAll(stockZero);
        shopGoodsMsg.get(0).setGoods(stockUnZero);
    }


    private static boolean checkIsNum(String val) {
        String regex = "^\\d+$";
        return val.matches(regex);
    }

    private static String formatTime(String data) {
        Date date = new Date(Long.parseLong(data));
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    @Override
    public ResponseResult<Boolean> setBuildings(List<BuildingDto> buildingDtos) {
        log.info("?????????????????????{}", buildingDtos);
        if (buildingDtos != null) {
            for (BuildingDto buildingDto : buildingDtos) {
                String key = RedisKeyUtils.getBuildingKey(buildingDto.getId());
                if (buildingDto.getStatus().equals((short) 1)) {
                    String json = JSON.toJSONString(buildingDto);
                    //??????????????????
                    log.info("??????????????????:{}", json);
                    RedisCache.set(key, json);
                } else {
                    log.info("??????????????????:{}", key);
                    RedisCache.delete(key);
                }
            }
        }
        return ResponseResult.buildSuccessResponse();
    }
}
