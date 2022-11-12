package com.milisong.breakfast.pos.dto;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年10月30日---下午2:46:27
*
*/
@Data
public class RefundGoodsMessage {

	private String goodsCode;
	
	private String goodsCount;
}
