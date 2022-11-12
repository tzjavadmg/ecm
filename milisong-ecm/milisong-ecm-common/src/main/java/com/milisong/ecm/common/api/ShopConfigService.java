package com.milisong.ecm.common.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.milisong.ecm.common.dto.BannerDto;
import com.milisong.ecm.common.dto.ShopConfigDto;

/**
 * 门店配置信息
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/16 15:37
 */
public interface ShopConfigService {


    /**
     * 获取今天的最后一次截单时间
     *
     * @param shopCode 门店编码
     * @return Date
     */
	@PostMapping(value = "/v1/ShopConfigService/getTodayLastCutOffTime")
    Date getTodayLastCutOffTime(@RequestParam("shopCode") String shopCode);

    /**
     * 获取配置费
     *
     * @param shopCode 门店编码
     * @return BigDecimal
     */
	@PostMapping(value = "/v1/ShopConfigService/getDeliveryOriginalPrice")
    BigDecimal getDeliveryOriginalPrice(@RequestParam("shopCode") String shopCode);

    /**
     * 获取配送费优惠价
     *
     * @param shopCode 门店编码
     * @return BigDecimal
     */
	@PostMapping(value = "/v1/ShopConfigService/getDeliveryDiscountPrice")
    BigDecimal getDeliveryDiscountPrice(@RequestParam("shopCode") String shopCode);

    /**
     * 获取打包费
     *
     * @param shopCode 门店编码
     * @return BigDecimal
     */
	@PostMapping(value = "/v1/ShopConfigService/getPackageOriginalPrice")
    BigDecimal getPackageOriginalPrice(@RequestParam("shopCode") String shopCode);

    /**
     * 获取打包费优惠价
     *
     * @param shopCode 门店编码
     * @return BigDecimal
     */
	@PostMapping(value = "/v1/ShopConfigService/getPackageDiscountPrice")
    BigDecimal getPackageDiscountPrice(@RequestParam("shopCode") String shopCode);

    /**
     * 获取同名公司提示次数
     *
     * @return Integer
     */
	@PostMapping(value = "/v1/ShopConfigService/getSameCompanyHintCount")
    Integer getSameCompanyHintCount();

    /**
     * 获取未支付订单过期时间
     *
     * @return Integer
     */
	@PostMapping(value = "/v1/ShopConfigService/getUnPayExpiredTime")
    Integer getUnPayExpiredTime();

    /**
     * 获取小程序分享图片
     *
     * @return
     */
	@PostMapping(value = "/v1/ShopConfigService/getSharePicture")
    String getSharePicture();

    /**
     * 获取小程序分享标题
     *
     * @return
     */
	@PostMapping(value = "/v1/ShopConfigService/getShareTitle")
    String getShareTitle();

    /**
     * 获取banner图
     *
     * @return
     */
	@PostMapping(value = "/v1/ShopConfigService/getBanner")
    List<BannerDto> getBanner(@RequestParam("shopCode") String shopCode);

    /**
     * 获取所有截单时段配置
     *
     * @param shopCode 门店编码
     * @return 截单时段配置
     */
	@PostMapping(value = "/v1/ShopConfigService/getDeliveryTimezones")
    List<ShopConfigDto.DeliveryTimezone> getDeliveryTimezones(@RequestParam("shopCode") String shopCode);


    /**
     * 获取最后一个截单时间
     *
     * @param shopCode 门店编码
     * @return ShopConfigDto.DeliveryTimezone
     */
	@PostMapping(value = "/v1/ShopConfigService/getLastDeliveryTimezone")
    ShopConfigDto.DeliveryTimezone getLastDeliveryTimezone(@RequestParam("shopCode") String shopCode);

    /**
     * @param shopCode 门店编码
     * @return
     */
	@PostMapping(value = "/v1/ShopConfigService/getDeliveryTimezoneMap")
    Map<Long, ShopConfigDto.DeliveryTimezone> getDeliveryTimezoneMap(@RequestParam("shopCode") String shopCode);

    /**
     * 获取新人价格的餐品海报
     *
     * @return
     */
	@PostMapping(value = "/v1/ShopConfigService/getGoodsPosterUrl")
    String getGoodsPosterUrl();

    /**
     * 获取提示用收藏小程序提示配置
     *
     * @return
     */
	@PostMapping(value = "/v1/ShopConfigService/getAddMiniAppTips")
    ShopConfigDto.AddMiniAppTips getAddMiniAppTips();
    
    /**
     * 获取订单完成时间
     * @return
     */
	@PostMapping(value = "/v1/ShopConfigService/getOrderEndTime")
    Integer getOrderEndTime();
	
	/**
	 * 获取档期标识
	 * @return
	 */
	@PostMapping(value = "/v1/ShopConfigService/getMovieFlag")
	Integer getMovieFlag();
	
	/**
	 * 获取库存标识
	 * @return
	 */
    @PostMapping(value = "/v1/ShopConfigService/getStockFlag")
	Integer getStockFlag();

    @PostMapping(value = "/v1/ShopConfigService/getShareFriendBanner")
    String getShareFriendBanner();

    @PostMapping(value = "/v1/ShopConfigService/getPurchaseLimit")
    Integer getPurchaseLimit();
}
