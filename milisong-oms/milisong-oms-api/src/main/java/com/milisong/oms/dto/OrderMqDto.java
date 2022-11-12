package com.milisong.oms.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderMqDto {
	// 订单编号
	private String orderNo;
	// 完成日期
	private Date date;
	// 集单状态 1待打包 2已打包 3配送中 4已通知
	private Byte status;
	
	//顺丰状态
	private Byte sfStatus;

	// 是否是补偿完成订单状态 true-补偿过来的消息，false-正常消息
	private Boolean isCompensate;
}
