package com.milisong.scm.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 早餐-公司取餐点信息
 */
@Getter
@Setter
public class CompanyMealAddressDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2381470216080552377L;

	/**
     * 
     */
	@ApiModelProperty("取餐点id")
    private Long id;

    /**
     * 公司id
     */
	@ApiModelProperty("公司id")
    private Long companyId;

    /**
     * 取餐点名称
     */
	@ApiModelProperty("取餐点名称")
    private String mealAddress;

    /**
     * 取餐点图片
     */
	@ApiModelProperty("取餐点照片")
    private String picture;
	
	/**
	 * 取餐点图片
	 */
	@ApiModelProperty("取餐点图片完整地址")
	private String completePicture;

    /**
     * 是否删除 1是 0否
     */
	@ApiModelProperty(hidden=true)
    private Boolean isDeleted;

    /**
     * 创建人
     */
	@ApiModelProperty(hidden=true)
    private String createBy;

    /**
     * 创建时间
     */
//	@ApiModelProperty(hidden=true)
//	 @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    private Date createTime;

    /**
     * 修改人
     */
	@ApiModelProperty(hidden=true)
    private String updateBy;

    /**
     * 修改时间
     */
//	@ApiModelProperty(hidden=true)
//	 @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//    private Date updateTime;

}