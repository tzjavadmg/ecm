package com.milisong.ecm.common.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import com.google.common.cache.Cache;
import com.milisong.ecm.common.api.ShopConfigService;
import com.milisong.ecm.common.dto.BannerDto;
import com.milisong.ecm.common.dto.ShopConfigDto;
import com.milisong.ecm.common.enums.ConfigItemEnum;
import com.milisong.ecm.common.util.DateTimeUtils;
import com.milisong.ecm.common.util.RedisKeyUtils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@Component
@Configuration
@RestController
public class ShopConfigServiceImpl implements ShopConfigService{
    @Resource
    Cache<String, ShopConfigDto> cache;

    private ShopConfigDto getShopConfig(String shopCode) {
        String shopConfigCacheKey = null;

        if (StringUtils.isNotEmpty(shopCode)) {
            shopConfigCacheKey = RedisKeyUtils.getShopConfigKey(shopCode);
        } else {
            shopConfigCacheKey = RedisKeyUtils.getGlobalConfigKey();
        }
        ShopConfigDto shopConfigDto = null;
        try {
            shopConfigDto = cache.get(shopConfigCacheKey, new Callable<ShopConfigDto>() {
                @Override
                public ShopConfigDto call() throws Exception {
                    Map<Object, Object> globalConfig = RedisCache.hGetAll(RedisKeyUtils.getGlobalConfigKey());
                    Map<Object, Object> shopConfig = null;
                    if (StringUtils.isNotEmpty(shopCode)) {
                        shopConfig = RedisCache.hGetAll(RedisKeyUtils.getShopConfigKey(shopCode));
                    }
                    return getShopConfigDto(globalConfig, shopConfig);
                }
            });
        } catch (ExecutionException e) {
            log.error("===========获取缓存配置异常===========", e);
        }
        return shopConfigDto;
    }

    private ShopConfigDto getShopConfigDto(Map<Object, Object> globalConfig, Map<Object, Object> shopConfig) {
        if (MapUtils.isEmpty(globalConfig)) {
            return null;
        }
        ShopConfigDto shopConfigDto = new ShopConfigDto();

        //配送时段 JSON
        String deliveryTimezoneString = mergeShopConfigStringValue(globalConfig, shopConfig, ConfigItemEnum.DELIVERY_TIMEZONE.getValue());
        //配送费原价
        String deliveryCostAmount = mergeShopConfigStringValue(globalConfig, shopConfig, ConfigItemEnum.DELIVERY_ORIGINAL_AMOUNT.getValue());
        //配送费优惠价
        String deliveryCostDiscountAmount = mergeShopConfigStringValue(globalConfig, shopConfig, ConfigItemEnum.DELIVERY_DISCOUNT_AMOUNT.getValue());
        //打包费原价
        String packageAmount = mergeShopConfigStringValue(globalConfig, shopConfig, ConfigItemEnum.PACKAGE_ORIGINAL_AMOUNT.getValue());
        //打包费优惠价
        String packageDiscountAmount = mergeShopConfigStringValue(globalConfig, shopConfig, ConfigItemEnum.PACKAGE_DISCOUNT_AMOUNT.getValue());
        //同名公司首页提示次数
        Integer sameCompanyHintCount = mergeShopConfigIntegerValue(globalConfig, shopConfig, ConfigItemEnum.SAME_COMPANY_HINT_COUNT.getValue());
        //未支付订单过期时间
        Integer unPayExpiredTime = mergeShopConfigIntegerValue(globalConfig, shopConfig, ConfigItemEnum.UN_PAY_EXPIRED_TIME.getValue());
        //小程序分享图片地址
        String sharePictureUrl = MapUtils.getString(globalConfig, ConfigItemEnum.SHARE_PICTURE_URL.getValue());
        //小程序分享标题
        String shareTitle = MapUtils.getString(globalConfig, ConfigItemEnum.SHARE_TITLE.getValue());
        //banner图及链接
        String banner = mergeShopConfigStringValue(globalConfig, shopConfig, ConfigItemEnum.BANNER.getValue());
        //餐盘海报图
        String goodsPosterUrl = MapUtils.getString(globalConfig, ConfigItemEnum.GOODS_POSTER_URL.getValue());
        //提示用收藏小程序提示配置
        String addMiniAppTipsString = MapUtils.getString(globalConfig, ConfigItemEnum.ADD_MINI_APP_TIPS.getValue());
        //订单完成时间(我的午餐计划是否展示已完成订单)
        Integer orderEndTime = mergeShopConfigIntegerValue(globalConfig, shopConfig, ConfigItemEnum.ORDER_END_TIME.getValue());
        //档期标识
        Integer movieFlag = MapUtils.getInteger(globalConfig, ConfigItemEnum.MOVIE_FLAG.getValue());
        //库存标识
        Integer stockFlag = MapUtils.getInteger(globalConfig, ConfigItemEnum.STOCK_FLAG.getValue());//库存标识
        Integer purchaseLimit = MapUtils.getInteger(globalConfig, ConfigItemEnum.PURCHASE_LIMIT.getValue());
        //提示用收藏小程序提示配置
        String shareFriendBanner = MapUtils.getString(globalConfig, ConfigItemEnum.SHARE_FRIEND_BANNER.getValue());


        shopConfigDto.setDeliveryOriginalAmount(convertToBigDecimal(deliveryCostAmount));
        shopConfigDto.setDeliveryDiscountAmount(convertToBigDecimal(deliveryCostDiscountAmount));
        shopConfigDto.setPackageOriginalAmount(convertToBigDecimal(packageAmount));
        shopConfigDto.setPackageDiscountAmount(convertToBigDecimal(packageDiscountAmount));
        shopConfigDto.setSameCompanyHintCount(sameCompanyHintCount);
        shopConfigDto.setUnPayExpiredTime(unPayExpiredTime);
        shopConfigDto.setSharePictureUrl(sharePictureUrl);
        shopConfigDto.setShareTitle(shareTitle);
        shopConfigDto.setBannerList(buildBannerList(banner));
        shopConfigDto.setGoodsPosterUrl(goodsPosterUrl);
        shopConfigDto.setAddMiniAppTips(JSON.parseObject(addMiniAppTipsString, ShopConfigDto.AddMiniAppTips.class));
        shopConfigDto.setOrderEndTime(orderEndTime);
        shopConfigDto.setMovieFlag(movieFlag);
        shopConfigDto.setStockFlag(stockFlag);
        shopConfigDto.setShareFriendBanner(shareFriendBanner);
        shopConfigDto.setPurchaseLimit(purchaseLimit);

        List<ShopConfigDto.DeliveryTimezone> deliveryTimezones = JSONArray.parseArray(deliveryTimezoneString, ShopConfigDto.DeliveryTimezone.class);
        shopConfigDto.setDeliveryTimezones(deliveryTimezones);
        return shopConfigDto;
    }

    private BigDecimal convertToBigDecimal(String stringValue) {
        BigDecimal value = null;
        if (StringUtils.isNotEmpty(stringValue)) {
            value = new BigDecimal(stringValue);
        }
        return value;
    }

    private String mergeShopConfigStringValue(Map<Object, Object> globalConfig, Map<Object, Object> shopConfig, String value) {
        String globalConfigValue = MapUtils.getString(globalConfig, value);
        String shopConfigValue = MapUtils.getString(shopConfig, value);
        return StringUtils.isNotEmpty(shopConfigValue) ? shopConfigValue : globalConfigValue;
    }

    private Integer mergeShopConfigIntegerValue(Map<Object, Object> globalConfig, Map<Object, Object> shopConfig, String value) {
        Integer globalConfigValue = MapUtils.getInteger(globalConfig, value);
        Integer shopConfigValue = MapUtils.getInteger(shopConfig, value);
        return shopConfigValue != null ? shopConfigValue : globalConfigValue;
    }

    private List<BannerDto> buildBannerList(String banner) {
        List<BannerDto> bannerList = JSONObject.parseArray(banner, BannerDto.class);
        return bannerList;
    }
    
    @Override
    public BigDecimal getDeliveryOriginalPrice(String shopCode) {
        ShopConfigDto shopConfigDto = getShopConfig(shopCode);
        BigDecimal deliveryCostAmount = shopConfigDto.getDeliveryOriginalAmount();
        if (deliveryCostAmount == null) {
            deliveryCostAmount = BigDecimal.valueOf(4);
        }
        return deliveryCostAmount;
    }

    @Override
    public BigDecimal getDeliveryDiscountPrice(String shopCode) {
        ShopConfigDto shopConfigDto = getShopConfig(shopCode);
        BigDecimal deliveryCostDiscountAmount = shopConfigDto.getDeliveryDiscountAmount();
        if (deliveryCostDiscountAmount == null) {
            deliveryCostDiscountAmount = BigDecimal.valueOf(0);
        }
        return deliveryCostDiscountAmount;
    }

    @Override
    public BigDecimal getPackageOriginalPrice(String shopCode) {
        ShopConfigDto shopConfigDto = getShopConfig(shopCode);
        BigDecimal packageAmount = shopConfigDto.getPackageOriginalAmount();
        if (packageAmount == null) {
            packageAmount = BigDecimal.valueOf(4);
        }
        return packageAmount;
    }

    @Override
    public BigDecimal getPackageDiscountPrice(String shopCode) {
        ShopConfigDto shopConfigDto = getShopConfig(shopCode);
        BigDecimal packageDiscountAmount = shopConfigDto.getPackageDiscountAmount();
        if (packageDiscountAmount == null) {
            packageDiscountAmount = BigDecimal.valueOf(0);
        }
        return packageDiscountAmount;
    }

    @Override
    public Integer getSameCompanyHintCount() {
        ShopConfigDto shopConfigDto = getShopConfig(null);
        Integer sameCompanyHintCount = shopConfigDto.getSameCompanyHintCount();
        if (sameCompanyHintCount == null) {
            sameCompanyHintCount = 2;
        }
        return sameCompanyHintCount;
    }

    @Override
    public Integer getUnPayExpiredTime() {
        ShopConfigDto shopConfigDto = getShopConfig(null);
        Integer unPayExpiredTime = shopConfigDto.getUnPayExpiredTime();
        if (unPayExpiredTime == null) {
            unPayExpiredTime = 5;
        }
        return unPayExpiredTime;
    }

    @Override
    public String getSharePicture() {
        ShopConfigDto shopConfigDto = getShopConfig(null);
        String sharePictureUrl = shopConfigDto.getSharePictureUrl();
        return sharePictureUrl;
    }

    @Override
    public String getShareTitle() {
        ShopConfigDto shopConfigDto = getShopConfig(null);
        String shareTitle = shopConfigDto.getShareTitle();
        return shareTitle;
    }

    @Override
    public List<BannerDto> getBanner(String shopCode) {
        ShopConfigDto shopConfigDto = getShopConfig(shopCode);
        List<BannerDto> bannerList = shopConfigDto.getBannerList();
        return bannerList;
    }

    @Override
    public Date getTodayLastCutOffTime(String shopCode) {
        ShopConfigDto shopConfigDto = getShopConfig(shopCode);
        int size = shopConfigDto.getDeliveryTimezones().size();
        ShopConfigDto.DeliveryTimezone deliveryTimezone = shopConfigDto.getDeliveryTimezones().get(size - 1);
        return DateTimeUtils.mergeDateAndTimeString(new Date(), deliveryTimezone.getCutoffTime());
    }

    @Override
    public List<ShopConfigDto.DeliveryTimezone> getDeliveryTimezones(String shopCode) {
        ShopConfigDto shopConfigDto = getShopConfig(shopCode);
        return shopConfigDto.getDeliveryTimezones();
    }

    @Override
    public ShopConfigDto.DeliveryTimezone getLastDeliveryTimezone(String shopCode) {
        List<ShopConfigDto.DeliveryTimezone> deliveryTimezones = getDeliveryTimezones(shopCode);
        return deliveryTimezones.get(deliveryTimezones.size() - 1);
    }

    @Override
    public Map<Long, ShopConfigDto.DeliveryTimezone> getDeliveryTimezoneMap(String shopCode) {
        ShopConfigDto shopConfigDto = getShopConfig(shopCode);
        return shopConfigDto.getDeliveryTimezones().stream().collect(Collectors.toMap(ShopConfigDto.DeliveryTimezone::getId, deliveryTimezone -> deliveryTimezone));
    }

    @Override
    public String getGoodsPosterUrl() {
        ShopConfigDto shopConfigDto = getShopConfig(null);
        String goodsPosterUrl = shopConfigDto.getGoodsPosterUrl();
        return goodsPosterUrl;
    }

    @Override
    public ShopConfigDto.AddMiniAppTips getAddMiniAppTips() {
        ShopConfigDto shopConfigDto = getShopConfig(null);
        return shopConfigDto.getAddMiniAppTips();
    }

    @Override
    public Integer getOrderEndTime() {
        ShopConfigDto shopConfigDto = getShopConfig(null);
        Integer orderEndTime = shopConfigDto.getOrderEndTime();
        if (orderEndTime == null) {
        	orderEndTime = 60;
        }
        return orderEndTime;
    }
    
    @Override
    public Integer getMovieFlag() {
        ShopConfigDto shopConfigDto = getShopConfig(null);
        Integer movieFlag = shopConfigDto.getMovieFlag();
        if (movieFlag == null) {
        	movieFlag = 1;
        }
        return movieFlag;
    }
    
    @Override
    public Integer getStockFlag() {
        ShopConfigDto shopConfigDto = getShopConfig(null);
        Integer stockValue = shopConfigDto.getStockFlag();
        log.info("获取库存配置阈值{}",stockValue);
        if (stockValue == null) {
        	stockValue = 20;
        }
        return stockValue;
    }

    @Override
    public String getShareFriendBanner() {
        ShopConfigDto shopConfigDto = getShopConfig(null);
        return shopConfigDto.getShareFriendBanner();
    }

    @Override
    public Integer getPurchaseLimit() {
        ShopConfigDto shopConfigDto = getShopConfig(null);
        return shopConfigDto.getPurchaseLimit();
    }

}
