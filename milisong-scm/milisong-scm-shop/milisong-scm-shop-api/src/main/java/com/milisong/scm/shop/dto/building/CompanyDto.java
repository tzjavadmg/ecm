package com.milisong.scm.shop.dto.building;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

/**
 * 公司信息dto
 * @author yangzhilong
 *
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class CompanyDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5811890157440202463L;

	private Long id;

    private String name;

    private String abbreviation;

    private Long buildingId;

    private String buildingAbbreviation;

    private String floor;

    private String roomNumber;

    private Integer employeesNumber;

    private Boolean isCertification;

    private Boolean isSameName;

    private String sameName;

    private Boolean isDeleted;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
}