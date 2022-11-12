package com.milisong.ecm.lunch.goods.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 包含距离定位点距离的楼宇dto
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class BuildingLbsDto extends BuildingDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3224249189169148012L;
	// 距离，单位：千米
	private Double distance;
}
