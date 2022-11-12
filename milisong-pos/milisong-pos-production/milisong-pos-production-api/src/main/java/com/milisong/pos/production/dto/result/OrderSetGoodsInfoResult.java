package com.milisong.pos.production.dto.result;

import java.io.Serializable;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年10月25日---下午9:14:06
*
*/
@Data
public class OrderSetGoodsInfoResult implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1720052598209136249L;

	private String goodsCode;
	
	private String goodsName;
	
	private Integer goodsSum;
	
	private String coupletNo;
	
}
