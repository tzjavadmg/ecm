package com.milisong.scm.base.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhaozhonghui
 * @Description 公司取餐时间
 * @date 2019-02-11
 */
@Data
public class CompanyMealTimeDto implements Serializable {

    private static final long serialVersionUID = 893352694260032942L;

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("编码")
    private Integer code;
    @ApiModelProperty("配送时间begin")
    @JsonFormat(pattern="HH:mm",timezone = "GMT+8")
    private Date deliveryTimeBegin;

    @ApiModelProperty("配送时间end")
    @JsonFormat(pattern="HH:mm",timezone = "GMT+8")
    private Date deliveryTimeEnd;

    @ApiModelProperty("是否删除 1是 0否")
    private Boolean isDeleted;
}
