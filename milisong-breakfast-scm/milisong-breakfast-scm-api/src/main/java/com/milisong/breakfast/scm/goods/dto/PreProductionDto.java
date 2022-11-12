package com.milisong.breakfast.scm.goods.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年10月24日---下午3:58:59
*
*/
@Data
public class PreProductionDto {

	// 商品code
	private String goodsCode;
	
	// 商品Name
	private String goodsName;
	
	//门店ID
	private Long shopId;
	
	//销售平均值
	private BigDecimal salesAverageCount;
	
	//预生产日
	private Date preProductionDate;
	
	//预生产值
	private BigDecimal preProductionCount;
	
	
	
}
