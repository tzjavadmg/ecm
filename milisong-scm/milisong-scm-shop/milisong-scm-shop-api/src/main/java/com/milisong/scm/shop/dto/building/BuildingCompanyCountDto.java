package com.milisong.scm.shop.dto.building;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildingCompanyCountDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -301863407967861293L;
	// 楼宇id
	private Long buildingId;
	// 公司数量
	private Integer ct;
}
