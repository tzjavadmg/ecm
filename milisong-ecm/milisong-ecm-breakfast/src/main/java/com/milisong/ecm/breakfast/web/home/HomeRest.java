package com.milisong.ecm.breakfast.web.home;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.farmland.core.util.BeanMapper;
import com.milisong.ecm.breakfast.dto.*;
import com.milisong.ecm.breakfast.service.RestUserService;
import com.milisong.ecm.common.enums.StatusConstant;
import com.milisong.pms.prom.api.BreakfastCouponService;
import com.milisong.ucs.sdk.security.UserContext;
import com.milisong.wechat.miniapp.enums.BusinessLine;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.DateUtil;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.google.common.collect.Maps;
import com.milisong.ecm.common.api.ShopConfigService;
import com.milisong.ecm.common.dto.BannerDto;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ecm.common.util.WeekDayUtils;
import com.milisong.oms.api.SalesService;
import com.milisong.oms.api.ShopStockService;
import com.milisong.oms.dto.ShopOnSaleGoodsDto;
import com.milisong.oms.param.ShopOnSaleGoodsParam;
import com.milisong.oms.param.ShopOnSaleGoodsParam.GoodsComboInfo;
import com.milisong.oms.param.ShopOnSaleGoodsParam.OnSaleGoodsParam;

import lombok.extern.slf4j.Slf4j;
import net.logstash.logback.encoder.org.apache.commons.lang.BooleanUtils;

/**
 * 小程序首页接口
 *
 * @author songmulin
 */

@Slf4j
@RestController
public class HomeRest {

    @Autowired
    private ShopConfigService shopConfigService;

    @Resource
    private ShopStockService shopStockService;

    @Autowired
    private BreakfastCouponService breakfastCouponService;

    @Autowired
    private RestUserService restUserService;

    /**
     * 获取档期及类目信息
     *
     * @return
     */
    @GetMapping(value = "/v1/home/movie-catalog")
    ResponseResult<MovieCatalogDto> movieCatalog(@RequestParam(value = "shopCode", required = false) String shopCode) {
        log.info("获取档期及类目信息,shopCode={}", shopCode);
        ResponseResult<MovieCatalogDto> responseResult = ResponseResult.buildSuccessResponse();
        MovieCatalogDto moviceCatalogDto = new MovieCatalogDto();
        List<MovieDto> movieList = Lists.newArrayList();
        //获取档期key
        String shopMovieKey = RedisKeyUtils.getShopScheduleDateKey(shopCode);
        Map<Object, Object> movieMap = RedisCache.hGetAll(shopMovieKey);
        if (movieMap != null && movieMap.size() > 0) {
            for (Map.Entry<Object, Object> shopMovie : movieMap.entrySet()) {
                Object value = shopMovie.getValue();
                MovieDto movieDto = JSONObject.parseObject(value.toString(), MovieDto.class);
                if (movieDto.getStatus()) {
                    movieList.add(movieDto);
                }

            }
        }
        movieList.sort(Comparator.comparing(MovieDto::getScheduleDate));
        LinkedHashMap<Integer, List<MovieDto>> dataMap = new LinkedHashMap<>();
        long currentTime = System.currentTimeMillis();
        if (CollectionUtils.isNotEmpty(movieList)) {
            for (MovieDto movie : movieList) {
                movie.setDateMesc(movie.getScheduleDate().getTime()); //日期毫秒
                Integer movieFlag = shopConfigService.getMovieFlag();
                if (movieFlag == 0) {
                    if (movie.getDateMesc() <= currentTime && !WeekDayUtils.isThisTime(movie.getDateMesc(), "yyyy-MM-dd")) {
                        continue;
                    }
                } else {
                    if (movie.getDateMesc() <= currentTime) {
                        continue;
                    }
                }
                movie.setDay(WeekDayUtils.getWeekDayString(movie.getScheduleDate())); //周几
                List<MovieDto> weekList = dataMap.get(movie.getWeekOfYear());
                if (CollectionUtils.isEmpty(weekList)) {
                    weekList = new ArrayList<>();
                }
                weekList.add(movie);
                dataMap.put(movie.getWeekOfYear(), weekList);
                if (dataMap.size() == 3) {
                    break;
                }
            }
        }
        log.info("过滤后档期数据{}", dataMap);
        AtomicInteger index = new AtomicInteger(0);
        for (Map.Entry<Integer, List<MovieDto>> movieResultMap : dataMap.entrySet()) {
            if (index.getAndIncrement() > 2) {
                break;
            }
            index.incrementAndGet();
            Integer week = WeekDayUtils.getWeekOfYear(new Date());
            if (null == dataMap.get(week)) {
                //当本周已无可售档期时，取下周数据 (实际取到的是下周和下下周)
                List<MovieDto> currentWeekList = movieResultMap.getValue();
                currentWeekList.sort(Comparator.comparing(MovieDto::getScheduleDate));
                moviceCatalogDto.setNextWeek(currentWeekList);
                break;
            } else {
                //如果当前时间在档期两周数据周期内，表明当前周还有可售日期，则展示本周和下周数据给前端
                //当前周数据
                if (movieResultMap.getKey() == week) {
                    List<MovieDto> currentWeekList = movieResultMap.getValue();
                    currentWeekList.sort(Comparator.comparing(MovieDto::getScheduleDate));
                    moviceCatalogDto.setCurrentWeek(currentWeekList);
                } else {
                    //下周数据
                    List<MovieDto> nextWeekList = movieResultMap.getValue();
                    nextWeekList.sort(Comparator.comparing(MovieDto::getScheduleDate));
                    moviceCatalogDto.setNextWeek(nextWeekList);
                }

            }
        }
        //分类结果
        moviceCatalogDto.setCatalogList(getCatalogList());
        responseResult.setData(moviceCatalogDto);
        return responseResult;

    }

    /**
     * 获取商品信息
     *
     * @param shopCode
     * @param setOnTime
     * @return
     */
    @GetMapping(value = "/v1/home/goods-catalog")
    ResponseResult<List<GoodsCatalogDto>> goodsCatalog(
            @RequestParam(value = "shopCode", required = true) String shopCode,
            @RequestParam(value = "setOnTime", required = true) String setOnTime) {
        log.info("获取商品信息shopCode={},setOnTime={}", shopCode, setOnTime);
        List<GoodsCatalogDto> goodsCatalogResultList = Lists.newArrayList();

        //获取分类集合数据
        String catalogBasicKey = RedisKeyUtils.getCatglogBasicKey();
        Map<Object, Object> catalogMapRedis = RedisCache.hGetAll(catalogBasicKey);
        log.info("分类信息结果{}", catalogMapRedis);
        Map<String, CatalogMqDto> catalogMapResult = Maps.newHashMap();
        if (catalogMapRedis != null && catalogMapRedis.size() > 0) {
            for (Map.Entry<Object, Object> catalogMap : catalogMapRedis.entrySet()) {
                String catagoryCode = catalogMap.getKey() == null ? "" : catalogMap.getKey().toString();
                CatalogMqDto catalogDto = JSONObject.parseObject(catalogMap.getValue() == null ? "" : catalogMap.getValue().toString(), CatalogMqDto.class);
                if (catalogDto.getStatus() == 1) {
                    catalogMapResult.put(catagoryCode, catalogDto);
                    log.info("-----------类目：{}", JSON.toJSONString(catalogDto));
                }
            }
        }

        String dailyShopGoodsKey = RedisKeyUtils.getDailyShopGoodsKey(shopCode, DateUtil.parse(setOnTime));
        String dailyShopGoods = RedisCache.get(dailyShopGoodsKey);
        Map<String, List<GoodsDto>> catalogMap = Maps.newHashMap();
        if (StringUtils.isNotBlank(dailyShopGoods)) {
            BigDecimal bestDiscountPrice = bestDiscountCouponPrice();
            //商品code集合
            List<String> goodsCodeList = JSONObject.parseArray(dailyShopGoods, String.class);
            //商品分类集合
            for (String goodsCode : goodsCodeList) {
                String goodsBasicKey = RedisKeyUtils.getGoodsBasicKey(goodsCode);
                String goodsBasic = RedisCache.get(goodsBasicKey);
                if (StringUtils.isNoneBlank(goodsBasic)) {
                    GoodsDto goodsDto = JSONObject.parseObject(goodsBasic, GoodsDto.class);
                    String categoryCode = goodsDto.getCategoryCode();
                    CatalogMqDto catalogDto = catalogMapResult.get(categoryCode);
                    if (catalogDto != null) {
                        List<GoodsDto> goodsList = catalogMap.get(categoryCode);
                        if (CollectionUtils.isEmpty(goodsList)) {
                            goodsList = new ArrayList<>();
                        }
                        //新人，计算商品折扣后的价格
                        calcNewUserPrice(goodsDto,bestDiscountPrice);
                        goodsList.add(goodsDto);
                        catalogMap.put(categoryCode, goodsList);
                    }
                }
            }
        }
        log.info("获取商品分类后数据{}", JSON.toJSONString(catalogMap));
        if (catalogMap != null && catalogMap.size() > 0) {
            for (Map.Entry<String, List<GoodsDto>> resultMap : catalogMap.entrySet()) {
                GoodsCatalogDto goodsCatalog = new GoodsCatalogDto();
                //类目code
                String categoryCode = resultMap.getKey();
                List<GoodsDto> goodsList = resultMap.getValue();
                CatalogMqDto catalogDto = catalogMapResult.get(categoryCode);
                goodsCatalog.setCategoryCode(catalogDto.getCode());
                goodsCatalog.setCategoryName(catalogDto.getName());
                goodsCatalog.setPicture(catalogDto.getPicture());
                goodsCatalog.setWeight(Integer.parseInt(catalogDto.getWeight().toString()));
                goodsCatalog.setGoods(goodsList);
                goodsCatalogResultList.add(goodsCatalog);
            }
            goodsCatalogResultList.sort(Comparator.comparing(GoodsCatalogDto::getWeight).reversed());
            log.info("可售商品返回结果{}", JSON.toJSONString(goodsCatalogResultList));
            dealShopStock(shopCode, DateUtil.parse(setOnTime), goodsCatalogResultList);
            sortShopGoods(goodsCatalogResultList);
        }
        return ResponseResult.buildSuccessResponse(goodsCatalogResultList);

    }

    private void calcNewUserPrice(GoodsDto goodsDto,BigDecimal bestDiscountPrice){
        try {
            Byte isNew = UserContext.getCurrentUser().getIsNew();
            if ((isNew != null && isNew.intValue() == 0) && (goodsDto.getPreferentialPrice() != null || goodsDto.getAdvisePrice() != null)
                    && bestDiscountPrice != null){
                BigDecimal price = goodsDto.getPreferentialPrice();
                if (price == null){
                    price = goodsDto.getAdvisePrice();
                }
                //计算出商品的新人价格
                goodsDto.setNewUserPrice(price.multiply(bestDiscountPrice).divide(BigDecimal.TEN).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
            }
        } catch (Exception e) {
            log.error("",e);
        }
    }

    private BigDecimal bestDiscountCouponPrice(){
        try {
            ResponseResult<BigDecimal> result = breakfastCouponService.bestDiscount();
            if (result.isSuccess()){
                return result.getData();
            }
        } catch (Exception e) {
            log.error("",e);
        }
        return null;
    }


    /**
     * 获取公司及配置信息
     *
     * @param companyId
     * @return
     */
    @GetMapping(value = "/v1/home/basic-company")
    ResponseResult<CompanyConfigDto> companyConfig(@RequestParam(value = "companyId", required = false) Long companyId) {
        log.info("获取公司及配置信息,companyId={}", companyId);
        CompanyConfigDto companyConfigDto = new CompanyConfigDto();
        String shopCode = null;
        if (companyId != null) {
            String companyKey = RedisKeyUtils.getCompanyKey(companyId);
            String companyStr = RedisCache.get(companyKey);
            CompanyDto companyDto = JSONObject.parseObject(companyStr, CompanyDto.class);
            if (companyDto != null) {
                shopCode = companyDto.getShopCode();
                companyDto.setShopCode(shopCode);
                companyConfigDto.setCompanyName(companyDto.getName());
                companyConfigDto.setShopCode(shopCode);
                companyConfigDto.setOpenStatus(companyDto.getOpenStatus());
            }
        }else{
            shopCode = RedisKeyUtils.getDefaultShopCode();
            companyConfigDto.setDefaultShopCode(shopCode);
            companyConfigDto.setOpenStatus(Byte.parseByte(Integer.toString(StatusConstant.CompanyStatusEnum.OPENED.getValue())));
        }
        List<BannerDto> bannerDtos = shopConfigService.getBanner(shopCode);
        if (CollectionUtils.isNotEmpty(bannerDtos)) {
            List<BannerDto> bannerList = BeanMapper.mapList(bannerDtos, BannerDto.class);
            bannerList.sort(Comparator.comparingInt(BannerDto::getWeight).reversed());
            //banner图片地址，链接URL
            companyConfigDto.setBannerList(bannerList);
        }
        //配送费
        companyConfigDto.setDeliveryCostAmount(shopConfigService.getDeliveryOriginalPrice(shopCode));
        //配送费优惠价
        companyConfigDto.setDeliveryCostDiscountAmount(shopConfigService.getDeliveryDiscountPrice(shopCode));
        //小程序分享图片
        companyConfigDto.setPictureUrl(shopConfigService.getSharePicture());
        //小程序分享文案
        companyConfigDto.setTitle(shopConfigService.getShareTitle());
        //包装费
        companyConfigDto.setPackageAmount(shopConfigService.getPackageOriginalPrice(shopCode));
        //包装费优惠价
        companyConfigDto.setPackageDiscountAmount(shopConfigService.getPackageDiscountPrice(shopCode));
        //限购数量
        companyConfigDto.setPurchaseLimit(shopConfigService.getPurchaseLimit());
        return ResponseResult.buildSuccessResponse(companyConfigDto);
    }


    /**
     * 获取申请开通公司入口的图片
     * @return
     */
    @GetMapping("/v1/home/display-pic")
    ResponseResult<DisplayPicDto> displayPic(){
        DisplayPicDto dto = new DisplayPicDto();
        String appluCompanyPic = RedisKeyUtils.getApplyCompanyEnterPic();
        dto.setApplyCompanyPic(appluCompanyPic);
        return ResponseResult.buildSuccessResponse(dto);
    }



    /**
     * 排序，权重大的排前面,如果权重一样，id大的排前面
     *
     * @param goodsCatalogList
     */
    private static void sortShopGoods(List<GoodsCatalogDto> goodsCatalogList) {
        for (GoodsCatalogDto goodsCatalog : goodsCatalogList) {
            List<GoodsDto> goodsList = goodsCatalog.getGoods();

            if (CollectionUtils.isEmpty(goodsList)) {
                log.warn("----------------商品列表为空！");
                continue;
            }
            //库存为0的集合
            List<GoodsDto> stockZero = goodsList.stream().filter(stock -> stock == null || (stock.getStock()!=null && stock.getStock() == 0)).distinct().collect(Collectors.toList());
            //库存不为0的集合
            List<GoodsDto> stockUnZero = goodsList.stream().filter(stock -> stock == null || (stock.getStock()!=null && stock.getStock() != 0)).distinct().collect(Collectors.toList());
            //按权重降序排序
            stockUnZero.sort(Comparator.comparingInt(GoodsDto::getWeight).reversed());
            stockUnZero.addAll(stockZero);
            goodsCatalog.setGoods(stockUnZero);
        }
    }

    /**
     * 设置库存及销量
     *
     * @param shopCode
     * @param setOnTime
     * @param goodsCatalogList
     */
    private void dealShopStock(String shopCode, Date setOnTime, List<GoodsCatalogDto> goodsCatalogList) {
        //循环类目数据
        for (GoodsCatalogDto category : goodsCatalogList) {
            List<OnSaleGoodsParam> goodsCodeList = Lists.newArrayList();
            //获取类目下商品code
            List<GoodsDto> goodsList = category.getGoods();
            ShopOnSaleGoodsParam shopOnSaleGoodsParam = new ShopOnSaleGoodsParam();
            shopOnSaleGoodsParam.setShopCode(shopCode);
            shopOnSaleGoodsParam.setSaleDate(setOnTime);
            List<String> goodsCodes = goodsList.stream().map(GoodsDto::getCode).collect(Collectors.toList());
            //循环商品code
            for (String goodsCode : goodsCodes) {
                OnSaleGoodsParam saleGoodsParam = new OnSaleGoodsParam();
                String goodsBasiKey = RedisKeyUtils.getGoodsBasicKey(goodsCode);
                String goodsBasic = RedisCache.get(goodsBasiKey);
                //获取商品基础信息
                GoodsMqDto goodsMqDto = JSONObject.parseObject(goodsBasic, GoodsMqDto.class);
                List<GoodsComboInfo> comboInfos = Lists.newArrayList();
                //如果是组合商品
                if (BooleanUtils.isTrue(goodsMqDto.getIsCombo())) {
                    //获取商品下的组合商品数据集合
                    List<GoodsCombinationRefDto> goodsCombination = goodsMqDto.getListGoodsCombinationRefMqDto();
                    log.info("组合商品：{}", goodsCombination);
                    if (goodsCombination == null) {
                        log.warn("-----------组合商品下无明细商品信息！商品信息：", goodsMqDto);
                        continue;
                    }
                    for (GoodsCombinationRefDto combination : goodsCombination) {
                        //组装组合商品对象数据
                        GoodsComboInfo goodsComboInfo = new GoodsComboInfo();
                        goodsComboInfo.setGoodsCode(combination.getSingleCode());
                        goodsComboInfo.setGoodsCount(combination.getGoodsCount());
                        comboInfos.add(goodsComboInfo);
                    }

                }
                saleGoodsParam.setGoodsCode(goodsCode);
                saleGoodsParam.setIsCombo(goodsMqDto.getIsCombo());
                saleGoodsParam.setComboInfos(comboInfos);
                goodsCodeList.add(saleGoodsParam);
            }
            shopOnSaleGoodsParam.setGoodsCodes(goodsCodeList);
            log.info("调用查询库存及销量参数{}", JSON.toJSONString(shopOnSaleGoodsParam));
            Map<String, ShopOnSaleGoodsDto> resultMap = shopStockService.getShopGoodsStock(shopOnSaleGoodsParam);
            log.info("调用查询库存及销量返回结果{}", JSON.toJSONString(resultMap));
            Integer stockValue = shopConfigService.getStockFlag();
            for (GoodsDto goods : goodsList) {
                log.info("---------------组装商品信息：{}", goods);
                if (resultMap == null) {
                    log.warn("---------------查询库存及销量结果为空");
                    continue;
                }
                ShopOnSaleGoodsDto shopOnSaleGoods = resultMap.get(goods.getCode());
                if (shopOnSaleGoods == null) {
                    log.warn("---------------shopOnSaleGoods 查询库存及销量结果为空");
                    continue;
                }
                goods.setMonthSale(shopOnSaleGoods.getMonthlySales());
                Integer saleStock = shopOnSaleGoods.getStock();
                if (saleStock <= stockValue) {
                    goods.setStockFlag(1);
                } else {
                    goods.setStockFlag(0);
                }
                goods.setStock(saleStock);

            }
        }
    }

    /**
     * 获取分类结果
     *
     * @return
     */
    private List<CatalogDto> getCatalogList() {
        List<CatalogDto> catalogResultList = Lists.newArrayList();
        //获取类目信息key
        String catalogBasicKey = RedisKeyUtils.getCatglogBasicKey();
        Map<Object, Object> catalogBasicMap = RedisCache.hGetAll(catalogBasicKey);
        if (catalogBasicMap != null && catalogBasicMap.size() > 0) {
            for (Map.Entry<Object, Object> catalogBasic : catalogBasicMap.entrySet()) {
                Object value = catalogBasic.getValue();
                CatalogMqDto catalogMqDto = JSONObject.parseObject(value.toString(), CatalogMqDto.class);
                if (catalogMqDto.getStatus() == (byte) 1) {
                    CatalogDto catalogDto = new CatalogDto();
                    catalogDto.setCategoryCode(catalogMqDto.getCode());
                    catalogDto.setCategoryName(catalogMqDto.getName());
                    catalogDto.setWeight(catalogMqDto.getWeight());
                    catalogDto.setPicture(catalogMqDto.getPicture());
                    catalogResultList.add(catalogDto);
                }
            }
            catalogResultList.sort(Comparator.comparingInt(CatalogDto::getWeight).reversed());
        }
        return catalogResultList;
    }

    /**
     * 获取用户最后一次下单公司，用在用户进首页时需要的数据
     *
     * @return
     */
    @GetMapping("/v1/home/get-last-addr")
    ResponseResult<BfUserAddressDto> getDeliveryAddr(@RequestParam(value = "companyId", required = false) Long companyId ,@RequestParam(value = "businessLine",required = false)Byte businessLine) {
        log.info("获取用户外卖地址:companyId={},businessLine={}", companyId,businessLine);
        if(businessLine == null){
            businessLine = BusinessLine.BREAKFAST.getCode();
        }
        return restUserService.getDeliveryAddr(companyId,businessLine);
    }
}
