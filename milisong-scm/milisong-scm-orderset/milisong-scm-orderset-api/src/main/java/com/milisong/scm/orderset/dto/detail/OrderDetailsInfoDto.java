package com.milisong.scm.orderset.dto.detail;

import java.io.Serializable;

import lombok.Data;

/**
 * 订单明细信息dto
 * @author youxia 2018年9月3日
 */
@Data
public class OrderDetailsInfoDto implements Serializable {
	
	private static final long serialVersionUID = -2404185665112517573L;
	
	private String orderNo; // 订单编号
	
	private String deliveryAddress; // 配送地址
	
	private String goodsName; // 商品名称
	
	private int goodsQuantity; // 订购数量
	
	private String mobileNo; // 手机号
					
	private String orderDateStr; // 订单日期	
			
	private Byte orderStatus; // 订单状态
	
	private String orderStatusStr; // 订单状态描述
					
	private String userName; // 用户名称

}
