package com.milisong.scm.orderset.dto.mq;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * 退款信息mq
 * @author yangzhilong
 *
 */
@Getter
@Setter
public class OrderRefundDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7955005218463006209L;

	private Long deliveryId;

    private String deliveryNo;

    private Date deliveryDate;
    
    // 订单类型0:早餐;1:午餐
    private Short orderType;
}
