package com.milisong.scm.orderset.dto.result;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年9月16日---上午11:05:40
*
*/
@Data
public class PickOrderPrintQueryResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5212118109697898864L;

	List<DistributionOrdersetResult> setNoList;
	
	List<PickOrderGoodsSumResult> goodsList;
}
