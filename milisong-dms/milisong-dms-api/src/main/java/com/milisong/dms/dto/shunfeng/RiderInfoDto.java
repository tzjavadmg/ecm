package com.milisong.dms.dto.shunfeng;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 传给c端的顺丰骑手信息 以及顺丰状态封装
 */
@Getter
@Setter
public class RiderInfoDto {
	
	//取餐号
	private String takeNo;
	
	//配送员名称
	private String transporterName;
	
	//配送员电话
	private String transporterPhone;

	// 顺丰状态
	List<ShunFengStatusDto> list;

}
