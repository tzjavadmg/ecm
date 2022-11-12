package com.milisong.scm.orderset.dto.result;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

/**
 * 配送单查询的结果
 * @author yangzhilong
 *
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class DistributionQueryResult implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4250823007553894573L;

	private Long id;

    private Long shopId;

    private String distributionNo;

    private String distributionDescription;
    
    private Integer ordersetSum;
    /** 顾客联数量 */
    private Integer customerSum;

    private Integer goodsSum;

    private String distributorAccount;

    private String distributorName;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date deliveryDate;

    private Date theoryDeliveryStartDate;

    private Date theoryDeliveryEndDate;

    private Date actualDeliveryStartDate;

    private Date actualDeliveryEndDate;
    
    private Boolean isPrintPickList;

    private Boolean isPrintDistribution;

    private Byte status;
    
    private List<BuildingBaseInfoResult> buildingList;
    
    private OrdersetNoResult ordersetNoResult;
}