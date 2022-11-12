package com.milisong.oms.domain;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 退款单。记录本次退款金额信息
 */
@Getter
@Setter
public class RefundOrder {
    /**
     * 主键
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 下单时间
     */
    private Date orderDate;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 姓名
     */
    private String realName;

    /**
     * 性别(0:女;1:男)
     */
    private Byte sex;

    /**
     * 联系人手机号码
     */
    private String mobileNo;

    /**
     * 门店编码
     */
    private String shopCode;

    /**
     * 退款打包费
     */
    private BigDecimal packageAmount;

    /**
     * 退款配送费
     */
    private BigDecimal deliveryAmount;

    /**
     * 退款红包金额
     */
    private BigDecimal redPacketAmount;

    /**
     * 退款商品金额
     */
    private BigDecimal goodsAmount;

    /**
     * 退款总金额
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
     * 配送次数
     */
    private Integer deliveryCount;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 订单类型：0:早餐;1:午餐;2:下午茶;3:晚餐;4:夜宵
     */
    private Byte orderType;

    /**
     * 下单模式：0:普通下单;1:拼团下单
     */
    private Byte orderMode;

    /**
     * 退款状态：0:退款中;1:退款成功;2:退款失败;
     */
    private Byte status;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 退款时间
     */
    private Date refundTime;
}