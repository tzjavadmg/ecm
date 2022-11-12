package com.mili.oss.dto.param;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class OrderUpdateStatusParam implements Serializable{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -3327856743891955806L;

	private String orderNo;
    // 0:待支付;2:已支付，待打包;4:打包完成，待配送;6:配送中;8:定单取消;9:配送完成，订单结束;
    private Byte orderStatus;
	// 0 待接单 1已接单 2已到店  3已取餐 4已完成
    private Byte deliveryStatus;
	// 是否是补偿完成订单状态
	private Boolean isCompensate;
}