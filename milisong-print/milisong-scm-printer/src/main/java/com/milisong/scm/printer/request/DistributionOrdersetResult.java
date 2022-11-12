package com.milisong.scm.printer.request;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

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
    
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date detailDeliveryDate;

    private String detailSetNo;

    private String detailSetNoDescription;

    private Integer goodsSum;

    private Boolean isDeleted;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date updateTime;
}