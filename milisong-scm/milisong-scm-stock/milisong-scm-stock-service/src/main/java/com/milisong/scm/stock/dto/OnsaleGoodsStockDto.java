package com.milisong.scm.stock.dto;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年10月24日---下午4:54:04
*
*/
@Data
public class OnsaleGoodsStockDto {

	private Long id;
	
	private String goodsName;
	
	private String goodsCode;
	
	private Long shopId;
}
