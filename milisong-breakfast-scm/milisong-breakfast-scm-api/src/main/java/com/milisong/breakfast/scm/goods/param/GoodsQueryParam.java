package com.milisong.breakfast.scm.goods.param;

import java.io.Serializable;

import com.farmland.core.api.PageParam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 商品列表查询条件
 * @author youxia 2018年9月6日
 */
@Getter
@Setter
public class GoodsQueryParam extends PageParam implements Serializable {

	private static final long serialVersionUID = 8242214387139874476L;
	
	@ApiModelProperty("商品名称")
	private String name; 
	
	@ApiModelProperty("商品编码")
	private String code; 
	
	@ApiModelProperty("商品类目")
	private String categoryCode; 
	
	@ApiModelProperty("状态")
	private Byte status; 
	
	@ApiModelProperty("是否为组合")
	private Boolean isCombo; 

}
