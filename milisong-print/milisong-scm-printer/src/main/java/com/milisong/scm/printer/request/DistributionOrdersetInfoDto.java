package com.milisong.scm.printer.request;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年10月10日---下午4:41:56
*
*/
@Data
public class DistributionOrdersetInfoDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -620055320310034466L;

	private DistributionOrdersetResultDto distributionOrdersetResultDto;
	
	private List<NotifyOrderSetQueryResult> orderSet;
}
