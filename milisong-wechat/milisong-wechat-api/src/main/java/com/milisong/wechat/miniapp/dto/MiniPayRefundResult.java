package com.milisong.wechat.miniapp.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 申请退款返回结果
 *
 * @author sailor wang
 * @date 2018/10/9 下午5:01
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@XStreamAlias("xml")
public class MiniPayRefundResult extends BasicPayDto implements Serializable {
    private static final long serialVersionUID = -3392333879907788033L;
    /**
     * 微信订单号.
     */
    @XStreamAlias("transaction_id")
    private String transactionId;

    /**
     * 商户订单号.
     */
    @XStreamAlias("out_trade_no")
    private String outTradeNo;

    /**
     * 商户退款单号.
     */
    @XStreamAlias("out_refund_no")
    private String outRefundNo;

    /**
     * 微信退款单号.
     */
    @XStreamAlias("refund_id")
    private String refundId;

    /**
     * 退款金额.
     */
    @XStreamAlias("refund_fee")
    private Integer refundFee;

    /**
     * 应结退款金额.
     */
    @XStreamAlias("settlement_refund_fee")
    private Integer settlementRefundFee;

    /**
     * 标价金额.
     */
    @XStreamAlias("total_fee")
    private Integer totalFee;

    /**
     * 应结订单金额.
     */
    @XStreamAlias("settlement_total_fee")
    private Integer settlementTotalFee;

    /**
     * 标价币种.
     */
    @XStreamAlias("fee_type")
    private String feeType;

    /**
     * 现金支付金额.
     */
    @XStreamAlias("cash_fee")
    private Integer cashFee;

    /**
     * 现金支付币种.
     */
    @XStreamAlias("cash_fee_type")
    private String cashFeeType;

    /**
     * 现金退款金额.
     */
    @XStreamAlias("cash_refund_fee")
    private String cashRefundFee;

    /**
     * 退款代金券使用数量.
     */
    @XStreamAlias("coupon_refund_count")
    private Integer couponRefundCount;

    /**
     * <pre>
     * 字段名：代金券退款总金额.
     * 变量名：coupon_refund_fee
     * 是否必填：否
     * 类型：Int
     * 示例值：100
     * 描述：代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
     * </pre>
     */
    @XStreamAlias("coupon_refund_fee")
    private Integer couponRefundFee;
}
