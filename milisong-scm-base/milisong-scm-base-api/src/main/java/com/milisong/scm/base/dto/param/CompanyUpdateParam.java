package com.milisong.scm.base.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
*@author    created by benny
*@date  2018年10月9日---下午4:52:55
*
*/
@Data
public class CompanyUpdateParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2115022457856452207L;
	@ApiModelProperty("公司id")
	private Long id;
	@ApiModelProperty("排序号")
	private Integer weight;
}
