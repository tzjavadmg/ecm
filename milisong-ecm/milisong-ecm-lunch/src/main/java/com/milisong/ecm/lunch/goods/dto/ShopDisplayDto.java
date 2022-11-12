package com.milisong.ecm.lunch.goods.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.milisong.ecm.common.dto.BannerDto;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/9/3   16:00
 *    desc    : 门店展示dto
 *    version : v1.0
 * </pre>
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopDisplayDto implements Serializable {
    private static final long serialVersionUID = -845642773007418559L;
    /**
     * C端不展示ID
     */
    private Long id;
    /**
     * 楼宇名称
     */
    private String name;
    /**
     * 门店code
     */
    private String code;
    /**
     * 轮播图
     */
    private List<BannerDto> bannerList;
    /**
     * 预计到达开始时间("11:00")
     */
    private String predictArriveStartTime;
    /**
     * 预计到达结束时间("12:00")
     */
    private String predictArriveEndTime;
    /**
     * 售卖截止时间("10:30")
     */
    private String interceptingTime;
    //标识是否要显示为明天配送
    private boolean displayTomorrow;
    
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
    
    private String pictureUrl;
    
    private String title;
    
    // 楼宇状态 0未计划  1已开通   2待开通
    private Short buildingStatus;
    
    //餐品海报图地址
    private String goodsPosterUrl;

    private String defaultShopCode;
}
