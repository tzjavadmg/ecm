package com.milisong.oms.domain;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 虚拟订单配送表。记录每次配送各项金额费用和配送时间
 */
@Getter
@Setter
public class VirtualOrderDelivery {
    /**
     *  
     */
    private Long id;

    /**
     * 主单ID
     */
    private Long orderId;

    /**
     * 配送日期
     */
    private Date deliveryDate;

    /**
     * 配送费原价
     */
    private BigDecimal deliveryOriginalPrice;

    /**
     * 配送费实价
     */
    private BigDecimal deliveryActualPrice;

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
     * 订单类型：0:早餐;1:午餐;2:下午茶;3:晚餐;4:夜宵
     */
    private Byte orderType;
}