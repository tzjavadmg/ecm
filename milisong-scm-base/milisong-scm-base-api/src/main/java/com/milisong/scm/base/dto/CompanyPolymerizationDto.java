package com.milisong.scm.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 公司聚合信息的dto（包含公司和取餐点）
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class CompanyPolymerizationDto extends CompanyDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3234084381957436644L;
	
	@ApiModelProperty("取餐点信息List")
	public List<CompanyMealAddressDto> mealAddressList;
}
