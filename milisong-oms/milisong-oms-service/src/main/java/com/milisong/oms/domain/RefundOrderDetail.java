package com.milisong.oms.domain;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 退款单明细表，记录退款明细金额及相应的配送记录
 */
@Getter
@Setter
public class RefundOrderDetail {
    /**
     *  
     */
    private Long id;

    /**
     * 退款单ID
     */
    private Long refundId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 配送ID
     */
    private Long deliveryId;

    /**
     * 配送编号
     */
    private String deliveryNo;

    /**
     * 配送日期
     */
    private Date deliveryDate;

    /**
     * 配送费
     */
    private BigDecimal deliveryAmount;

    /**
     * 包装费
     */
    private BigDecimal packageAmount;

    /**
     * 商品金额
     */
    private BigDecimal goodsAmount;

    /**
     * 总退款金额
     */
    private BigDecimal totalRefundAmount;

    /**
     * 退还积分
     */
    private Integer totalRefundPoints;

    /**
     * 商品数量
     */
    private Integer goodsCount;

    /**
     * 订单类型：0:早餐;1:午餐;2:下午茶;3:晚餐;4:夜宵
     */
    private Byte orderType;
}