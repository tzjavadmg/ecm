package com.milisong.scm.printer.request;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年9月16日---下午5:21:09
*
*/
@Data
public class DistributionOrdersetResultDto implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 4328745466318936950L;

	private List<DistributionOrdersetResult> distributionOrdersetResult;
	
	private List<BuildingDto> buildingDto;
}
