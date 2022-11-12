package com.milisong.scm.orderset.dto.result;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailResult implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6705662274146881947L;

	private Long id;

    private Long orderId;

    private String orderNo;

    private String goodsCode;

    private String goodsName;

    private BigDecimal goodsPrice;

    private Integer goodsCount;
    
    /**
     * 商品折扣后价格
     */
    private BigDecimal goodsDiscountPrice;

    /**
     * 商品实际支付总金额
     */
    private BigDecimal actualPayAmount;

    /**
     * 平摊商品配送费
     */
    private BigDecimal deliveryCostAmount;

    /**
     * 平摊打包费
     */
    private BigDecimal packageAmount;

    /**
     * 平摊红包金额
     */
    private BigDecimal redPacketAmount;

    private Date createTime;

    private Date updateTime;
    /**
     * 手机号
     */
    private String mobileNo;
}