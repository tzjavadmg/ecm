package com.milisong.breakfast.pos.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年10月24日---下午3:58:59
*
*/
@Data
public class PreProductionDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1762607240876246531L;
	private Long id;
	// 商品code
	private String goodsCode;
	
	// 商品Name
	private String goodsName;
	
	//门店ID
	private Long shopId;
	
	//销售平均值
	private BigDecimal salesAverageCount;
	
	//预生产日
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date preProductionDate;
	
	//预生产值
	private BigDecimal preProductionCount;
	
	//实际生产值
	private Integer actualProductionCount;
	
	
	//实际销售值
	private Integer actualSaleCount;
	List<SaleVolumes> list;
}
