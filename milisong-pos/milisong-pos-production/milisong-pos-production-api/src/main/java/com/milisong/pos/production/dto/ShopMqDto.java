package com.milisong.pos.production.dto;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年11月12日---下午6:59:54
*
*/
@Data
public class ShopMqDto {

	private Long id;

	private String code;

	private String name;

	//门店状态:1营业中 2停业
	private Byte status;

	private String address;

	private Boolean isDelete;
}
