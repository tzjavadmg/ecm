package com.milisong.scm.goods.param;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsSalableQuantityLunchParam {
	
	@ApiModelProperty("门店id")
	private Long shopId;
	
	private String startTime;
	
	private String endTime;
	
	@ApiModelProperty("商品code")
	private String goodsCode;
	
	@ApiModelProperty("可售量")
	private Integer availableVolume;
	
	private Long id;
	
	private String operator;
	
	@ApiModelProperty("可售日期")
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private String saleDate;
}
