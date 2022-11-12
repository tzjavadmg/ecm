package com.milisong.scm.printer.request;

import java.io.Serializable;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年9月16日---上午11:06:48
*
*/
@Data
public class PickOrderGoodsSumResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7257764140012664641L;

	private String goodsName;
	
	private Integer goodsCount;
}
