package com.milisong.oms.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefundOrderDetailDto {
	
	//申请时间
    private String applyTime;

    //退款中时间
    private String refundingTime;
    
    //退款时间
    private String refundTime;
    
    //退款金额
    private BigDecimal totalRefundAmount;
    
    //预计配送时间
    private Long deliveryDate;
    
    //订单号
    private String orderNo;
    
    //订单时间
    private String orderDate;

}
