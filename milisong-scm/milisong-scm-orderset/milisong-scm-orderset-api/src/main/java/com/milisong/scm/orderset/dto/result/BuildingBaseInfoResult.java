package com.milisong.scm.orderset.dto.result;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 楼宇的基本信息的返回值
 * 
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class BuildingBaseInfoResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3163365465391078965L;

	private Long buildingId;

	private String buildingAbbreviation;
}
