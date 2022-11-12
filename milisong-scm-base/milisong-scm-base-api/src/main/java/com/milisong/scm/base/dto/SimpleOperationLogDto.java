package com.milisong.scm.base.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 简单的操作日志dto
 * @author yangzhilong
 *
 */
@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class SimpleOperationLogDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -144302856636132533L;
	
	private Long id;

    /**
     * 模块 {@link com.tomato.boss.common.constant.BossCommonConstant.OPERATION_MODULAR}
     */
	@ApiModelProperty("模块")
    private String modular;

    /**
     * 业务数据标识id
     */
	@ApiModelProperty("业务数据标识id")
    private String bussinessId;

    /**
     * 操作类型  {@link com.tomato.common.utils.Constant.OPERATION_TYPE}
     */
	@ApiModelProperty("操作类型")
    private String operationType;

    /**
     * 操作人
     */
	@ApiModelProperty("操作人")
    private String operationUser;
	
    /**
     * 操作时间
     */
	@ApiModelProperty("操作时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date operationTime;

    /**
     * 操作简述
     */
	@ApiModelProperty("操作简述")
    private String operationResume;
	
	/**
     * 变动的数据
     */
	@ApiModelProperty("变动的数据")
	private String diffData;
}