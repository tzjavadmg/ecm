package com.milisong.scm.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 早餐-公司信息
 */
@Getter
@Setter
public class CompanyDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2365597986511930558L;

	/**
     * 
     */
	@ApiModelProperty("公司id")
    private Long id;

    /**
     * 公司名字
     */
	@ApiModelProperty("公司名字")
    private String name;

    /**
     * 楼宇名字
     */
	@ApiModelProperty("楼宇名字")
    private String buildingName;

    /**
     * 门店id
     */
	@ApiModelProperty("门店id")
    private Long shopId;

    /**
     * 门店code
     */
	@ApiModelProperty("门店code")
    private String shopCode;

    /**
     * 门店名称
     */
	@ApiModelProperty("门店名称")
    private String shopName;

    /**
     * 所在城市code
     */
	@ApiModelProperty("城市code")
    private String cityCode;

    /**
     * 所在城市名称
     */
	@ApiModelProperty("城市名称")
    private String cityName;

    /**
     * 所在区code
     */
	@ApiModelProperty("区code")
    private String regionCode;

    /**
     * 所在区名称
     */
	@ApiModelProperty("区名称")
    private String regionName;

    /**
     * 不需要包含城市和区县的公司详细地址
     */
	@ApiModelProperty("详细地址")
    private String detailAddress;

    /**
     * 楼层
     */
	@ApiModelProperty("楼层")
    private String floor;

    /**
     * 配送时间begin
     */
	@ApiModelProperty("配送开始时间")
    @JsonFormat(pattern="HH:mm",timezone = "GMT+8")
    private Date deliveryTimeBegin;

    /**
     * 配送时间end
     */
	@ApiModelProperty("配送结束时间")
    @JsonFormat(pattern="HH:mm",timezone = "GMT+8")
    private Date deliveryTimeEnd;

    /**
     * 上班时间
     */
	@ApiModelProperty("上班时间")
    @JsonFormat(pattern="HH:mm",timezone = "GMT+8")
    private Date workingHours;
    
    /**
     * 权重（排序号）
     */
	@ApiModelProperty("权重")
    private Integer weight;

    /**
     * 开通状态 1开通 0关闭
     */
	@ApiModelProperty("开通状态 1开通 0关闭")
    private Byte openStatus;

    /**
     * 联络人信息
     */
	@ApiModelProperty("联络人信息")
    private String contactPerson;

    /**
     * 取餐点数量
     */
	@ApiModelProperty("取餐点数量")
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

    /**
     * 创建人
     */
    @ApiModelProperty(hidden=true)
    private String createBy;

    /**
     * 创建时间
     */
//    @ApiModelProperty(hidden=true)
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(hidden=true)
    private String updateBy;

    /**
     * 修改时间
     */
//    @ApiModelProperty(hidden=true)
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    private Date updateTime;

    @ApiModelProperty("早餐配送时间")
    private List<CompanyMealTimeDto> mealTimeList;

    @ApiModelProperty("午餐配送时间")
    private List<CompanyMealTimeDto> lunchMealTimeList;
}