package com.milisong.scm.printer.request;

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
	private static final long serialVersionUID = -4548674269275649852L;

	private List<DistributionOrdersetResult> setNoList;
	
	private List<PickOrderGoodsSumResult> goodsList;
}
