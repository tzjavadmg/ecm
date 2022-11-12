package com.milisong.oms.domain;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单配送表。
 * 1.记录配送时间信息
 * 2.记录本次配送相关的商品费用、打包费用、配送费用
 * 3.记录红包、促销等分摊费用，用于退款计算。
 */
@Getter
@Setter
public class OrderDelivery {
    /**
     *  
     */
    private Long id;

    /**
     * 主单ID
     */
    private Long orderId;

    /**
     * 配送单编号
     */
    private String deliveryNo;

    /**
     * 收货公司上班时间
     */
    private Date deliveryCompanyWorkingTime;

    /**
     * 
     */
    private Long deliveryTimezoneId;

    /**
     * 订单：截单时间
     */
    private Date deliveryTimezoneCutoffTime;

    /**
     * 配送时间段-开始时间
     */
    private Date deliveryTimezoneFrom;

    /**
     * 配送时间段-结束时间
     */
    private Date deliveryTimezoneTo;

    /**
     * 配送日期
     */
    private Date deliveryDate;

    /**
     * 配送开始时间
     */
    private Date deliveryStartTime;

    /**
     * 配送到达时间
     */
    private Date deliveryEndTime;

    /**
     * 配送费原价
     */
    private BigDecimal deliveryOriginalPrice;

    /**
     * 配送费实价
     */
    private BigDecimal deliveryActualPrice;

    /**
     * 打包时间
     */
    private Date packageTime;

    /**
     * 包装费原价总金额
     */
    private BigDecimal totalPackageOriginalAmount;

    /**
     * 包装费实价总金额
     */
    private BigDecimal totalPackageActualAmount;

    /**
     * 总商品数量
     */
    private Integer totalGoodsCount;

    /**
     * 商品原价总金额
     */
    private BigDecimal totalGoodsOriginalAmount;

    /**
     * 商品实价总金额
     */
    private BigDecimal totalGoodsActualAmount;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 分摊订单实际支付金额
     */
    private BigDecimal shareOrderPayAmount;

    /**
     * 分摊抵扣积分
     */
    private Integer shareOrderDeductionPoints;

    /**
     * 订单类型：0:早餐;1:午餐;2:下午茶;3:晚餐;4:夜宵
     */
    private Byte orderType;

    /**
     * 配送状态：0:配送中;1:待配送;2:备餐中;3:已取消;4:已退款;9:已完成;
     */
    private Byte status;

    /**
     * 顺送配送状态：0:已派单; 1: 已接单;2: 已到店;3: 已提货;4: 已送达
     */
    private Byte sfStatus;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 退款时间
     */
    private Date refundTime;
}