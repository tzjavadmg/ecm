package com.milisong.scm.base.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 门店信息dto
 * @author youxia 2018年9月2日
 */
@Data
public class ShopDto implements Serializable {
	
	private static final long serialVersionUID = -1563375216090449268L;
	@ApiModelProperty("门店id")
	public Long id; // 门店id
	@ApiModelProperty("门店编码")
	private String code; // 门店编码
	@ApiModelProperty("门店名称")
	private String name; // 门店名称
	@ApiModelProperty("门店状态 1营业中 2停业")
	private Byte status; // 状态
	@ApiModelProperty("门店地址")
	private String address; // 门店地址

}
