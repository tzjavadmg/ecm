package com.milisong.scm.stock.dto;

import lombok.Data;


/**
 * @author benny
 *
 */
@Data
public class ShopOnsaleGoodsStockDto {

	private Long id;
	
	private String goodsName;
	
	private String goodsCode;
	
	private String saleDate;
	
	private String availableVolume;
	
	private Integer salesVolume;
	
	
}
