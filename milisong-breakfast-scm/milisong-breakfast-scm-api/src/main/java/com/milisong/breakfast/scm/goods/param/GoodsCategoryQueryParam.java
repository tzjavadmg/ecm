package com.milisong.breakfast.scm.goods.param;

import java.io.Serializable;

import com.farmland.core.api.PageParam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsCategoryQueryParam extends PageParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8391174930948157955L;

	@ApiModelProperty("商品名称")
	private String name; 
	
	@ApiModelProperty("状态")
	private Byte status; 
}
