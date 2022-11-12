package com.milisong.breakfast.pos.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 退款信息mq
 * @author yangzhilong
 *
 */
@Data
public class OrderRefundDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7955005218463006209L;

	private Long deliveryId;

    private String deliveryNo;

    private Date deliveryDate;
    
    private String shopCode;
    
    private List<RefundGoodsMessage> refundGoods;
}
