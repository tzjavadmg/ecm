package com.milisong.scm.base.dto.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 公司配送顺序保存DTO
 * @author yangzhilong
 *
 */
@Data
public class CompanyUpdateListParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7453990337650261321L;
	/**
	 * 排序结果
	 */
	@ApiModelProperty("排序结果")
	List<CompanyUpdateParam> list;
	
	/**
	 * 操作人
	 */
	@ApiModelProperty(value="操作人", hidden=true)
	private String updateBy;
}
