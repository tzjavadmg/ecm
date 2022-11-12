package com.milisong.ecm.lunch.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CompanyDto {
    /**
     * 
     */
    private Long id;

    /**
     * 公司名字
     */
    private String name;

    /**
     * 楼宇名字
     */
    private String buildingName;

    /**
     * 门店id
     */
    private Long shopId;

    /**
     * 门店code
     */
    private String shopCode;

    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 所在城市code
     */
    private String cityCode;

    /**
     * 所在城市名称
     */
    private String cityName;

    /**
     * 所在区code
     */
    private String regionCode;

    /**
     * 所在区名称
     */
    private String regionName;

    /**
     * 不需要包含城市和区县的公司详细地址
     */
    private String detailAddress;

    /**
     * 楼层
     */
    private String floor;

    /**
     * 配送时间begin
     */
    private String deliveryTimeBegin;

    /**
     * 配送时间end
     */
    private String deliveryTimeEnd;

    /**
     * 上班时间
     */
    private String workingHours;

    /**
     * 开通状态 1开通 0关闭
     */
    private Byte openStatus;

    /**
     * 联络人信息
     */
    private String contactPerson;

    /**
     * 取餐点数量
     */
    private Integer mealAddressCount;

    /**
     * 经度-百度
     */
    private BigDecimal lonBaidu;

    /**
     * 纬度-百度
     */
    private BigDecimal latBaidu;

    /**
     * 经度-高德
     */
    private BigDecimal lonGaode;

    /**
     * 纬度-高德
     */
    private BigDecimal latGaode;

    /**
     * 是否删除 1是 0否
     */
    private Boolean isDeleted;

    // 距离，单位：千米
    private Double distance;

}
