package com.milisong.scm.base.dto.mq;

import com.milisong.scm.base.dto.CompanyMealAddressDto;
import com.milisong.scm.base.dto.CompanyMealTimeDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 公司聚合信息的dto（包含公司和取餐点）
 *
 * @author yangzhilong
 */
@Getter
@Setter
public class CompanyMqDto implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1916533004359090056L;

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
     * 权重（排序号）
     */
    private Integer weight;

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
    /** 取餐点 */
    public List<CompanyMealAddressDto> mealAddressList;
    /** 早餐配送时间 */
    public List<CompanyMealTimeDto> mealTimeList;
    /** 午餐配送时间 */
    public List<CompanyMealTimeDto> lunchMealTimeList;
    /** 是否改变绑定的门店 */
    public Boolean transformShop;
    /** 旧的门店Code */
    public String oldShopCode;
}
