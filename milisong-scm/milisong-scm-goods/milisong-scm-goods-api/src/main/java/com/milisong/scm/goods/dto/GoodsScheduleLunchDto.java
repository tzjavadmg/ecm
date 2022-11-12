package com.milisong.scm.goods.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsScheduleLunchDto {

	 /**
     * 
     */
    private Long id;

    @ApiModelProperty("门店id")
    private Long shopId;

    @ApiModelProperty("门店code")
    private String shopCode;

    @ApiModelProperty("门店name")
    private String shopName;

    @ApiModelProperty("年份")
    private Integer year;

    @ApiModelProperty("W多少周")
    private Integer weekOfYear;

    @ApiModelProperty("档期日期")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date scheduleDate;

    @ApiModelProperty("周几")
    private Integer week;

    @ApiModelProperty("是否可售 1可售 0不可售")
    private Boolean status;

    @ApiModelProperty("发布时间(生效时间)")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date publishTime;

    @ApiModelProperty("是否删除 1是 0否")
    private Boolean isDeleted;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;
}
