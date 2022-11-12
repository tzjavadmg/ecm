package com.milisong.pos.production.dto.result;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年10月25日---下午9:12:28
*
*/
@Data
public class OrderSetInfoResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4841199691644706683L;

	private String detailSetNo;

	private String detailSetNoDescription;
	  
	private String coupletNo;
	
	private String goodsName;
	
	private String goodsCode;
	
	private Integer goodsNumber;
	
	private Long outTime;
	
	private List<OrderSetGoodsInfoResult> list;	
}
