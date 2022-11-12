package com.mili.oss.mq;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 集单里订单状态配送完成的消息
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderSetOrderStatusChangeMsg implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6362019290195618400L;
	// 订单编号
	private String orderNo;
	// 完成日期
	private Date date;
	// 集单状态 1待打包 2已打包 3配送中 4已通知
	private Byte status;
	// 顺丰状态 {@link LogisticsDeliveryStatus}
	private Byte sfStatus;
	// 是否是补偿完成订单状态
	private Boolean isCompensate;
}
