package com.milisong.scm.orderset.dto.result;

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
	private static final long serialVersionUID = -6459856121430876289L;

	private String goodsName;
	
	private Integer goodsCount;
}
