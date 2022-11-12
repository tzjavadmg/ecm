package com.milisong.breakfast.scm.orderset.param;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSetBuildingSqlResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4111236310215597457L;
	// 楼宇id
	private Long buildingId;
	// 楼宇简称
	private String buildingAbbreviation;
}
