package com.milisong.scm.orderset.result;

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
public class BuildingBaseInfoSqlResult {
	private Long buildingId;

	private String buildingAbbreviation;
}
