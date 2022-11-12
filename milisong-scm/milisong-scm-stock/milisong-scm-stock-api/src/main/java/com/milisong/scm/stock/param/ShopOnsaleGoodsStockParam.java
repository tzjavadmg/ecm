package com.milisong.scm.stock.param;

import com.farmland.core.api.PageParam;

import lombok.Data;

/**
 * @author benny
 *
 */
@Data
public class ShopOnsaleGoodsStockParam extends PageParam{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1426943720831255068L;

	private Long shopId;
	
	private String startTime;
	
	private String endTime;
	
	private String goodsCode;
	
	private Integer availableVolume;
	
	private Long id;
	
	private String operator;
	
	private String saleDate;
	
}
