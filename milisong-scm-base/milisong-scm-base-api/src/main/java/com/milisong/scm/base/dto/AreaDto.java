package com.milisong.scm.base.dto;

import lombok.Data;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author zhaozhonghui
 * @date 2018-09-12
 */
@Data
public class AreaDto implements Serializable {

    private static final long serialVersionUID = -7374972021151337349L;
    @ApiModelProperty("区域编码")
    private String code;
    @ApiModelProperty("区域名称")
    private String name;
}
