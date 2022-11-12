package com.milisong.breakfast.scm.goods.param;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
*@author    created by benny
*@date  2018年12月5日---上午10:54:40
*
*/
@Getter
@Setter
public class ShopGoodsSalableQuantityParam {

	private Long shopId;
	
	private String startTime;
	
	private String endTime;
	
	private String goodsCode;
	
	private Integer availableVolume;
	
	private Long id;
	
	private String operator;
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private String saleDate;
}
