package com.milisong.ecm.breakfast.dto;

import java.math.BigDecimal;
import java.util.List;

import com.milisong.ecm.common.dto.BannerDto;

import lombok.Getter;
import lombok.Setter;

/**
 * 公司及配置信息
 *
 * @author songmulin
 */
@Getter
@Setter
public class CompanyConfigDto {
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 门店code
     */
    private String shopCode;

    /**
     * 默认展示门店的code
     */
    private String defaultShopCode;
    /**
     * 轮播图
     */
    private List<BannerDto> bannerList;

    /**
     * 售卖截止时间("10:30")
     */
    private String interceptingTime;

    /**
     * 配送费
     */
    private BigDecimal deliveryCostAmount;
    /**
     * 配送费优惠后金额
     */
    private BigDecimal deliveryCostDiscountAmount;

    /**
     * 包装费
     */
    private BigDecimal packageAmount;

    /**
     * 包装费优惠价
     */
    private BigDecimal packageDiscountAmount;

    /**
     * 小程序分享图片
     */
    private String pictureUrl;

    /**
     * 小程序分享文案
     */
    private String title;

    /**
     * 开通状态 1开通 0关闭
     */
    private Byte openStatus;
    /**
     * 商品限购次数
     */
    private Integer purchaseLimit;
}
