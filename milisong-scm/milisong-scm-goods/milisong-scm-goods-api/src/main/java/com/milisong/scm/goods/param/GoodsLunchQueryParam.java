package com.milisong.scm.goods.param;

import java.io.Serializable;

import com.farmland.core.api.PageParam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsLunchQueryParam extends PageParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3071936984345468909L;

	@ApiModelProperty("商品名称")
	private String name; 
	
	@ApiModelProperty("商品编码")
	private String code; 
	
	@ApiModelProperty("商品类目")
	private String categoryCode; 
	
	@ApiModelProperty("状态")
	private Byte status; 
}
