package com.mili.oss.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class DistributionOrdersetResult implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1410142857333234888L;

	private Long id;

    private String distributionNo;

    private String distributionDescription;
    
    private Date deliveryDate;

    private Long buildingId;

    private String buildingAbbreviation;

    private String deliveryFloor;

    private String companyAbbreviation;

    private String detailSetNo;

    private String detailSetNoDescription;

    private Integer goodsSum;

    private Boolean isDeleted;

    private Date createTime;

    private Date updateTime;
}