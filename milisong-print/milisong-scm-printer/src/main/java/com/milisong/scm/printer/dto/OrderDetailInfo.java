package com.milisong.scm.printer.dto;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年9月7日---下午6:58:59
*
*/
@Data
public class OrderDetailInfo {

	private String name;
	private String phone;
	private String goodsName;
	private Integer goodsCount;
}
