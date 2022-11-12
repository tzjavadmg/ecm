package com.milisong.scm.base.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * 操作日志查询dto
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OperationLogQueryDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1966170772869774004L;

	/**
	 * 模块
	 */
	@NotNull
	private String modular;
	
	/**
     * 业务数据标识id
     */
	@NotNull
	@Size(min=1)
    private List<String> bussinessIds;
	
	/**
	 * 操作类型
	 */
	private String operationType;
}
