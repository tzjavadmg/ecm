package com.milisong.breakfast.scm.orderset.dto.param;

import java.io.Serializable;

import com.farmland.core.api.PageParam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 集单查询的查询参数
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderSetSearchParam extends PageParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3275483883051793189L;
	
	// 查询日期 yyyy-mm-dd格式
	@ApiModelProperty("配送日期 yyyy-mm-dd")
	private String date;
	// 门店id，不传则查所有
	@ApiModelProperty("门店id")
	private Long shopId;
	// 公司id
	@ApiModelProperty("公司id")
	private Long companyId;
	// 公司名称
	@ApiModelProperty("公司名称")
	private String companyName;
	// 楼宇名称
	@ApiModelProperty("楼宇名称")
	private String buildingName;
}
