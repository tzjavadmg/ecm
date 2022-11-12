package com.milisong.scm.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 全量的操作日志dto
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OperationLogDto extends SimpleOperationLogDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2723128302709454658L;

	/**
	 * 操作前数据
	 */
	@ApiModelProperty("操作前数据")
	private String beforeData;

    /**
     * 操作后数据
     */
	@ApiModelProperty("操作后数据")
    private String afterData;
}
