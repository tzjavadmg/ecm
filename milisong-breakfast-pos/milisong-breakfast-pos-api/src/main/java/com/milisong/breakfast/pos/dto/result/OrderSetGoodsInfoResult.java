package com.milisong.breakfast.pos.dto.result;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
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
	@ApiModelProperty("商品code")
	private String goodsCode;
	@ApiModelProperty("商品名字")
	private String goodsName;
	@ApiModelProperty("商品数量")
	private Integer goodsSum;
	@ApiModelProperty("顾客联编号")
	private String coupletNo;
	
}
