package com.mili.oss.domain;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单明细表-早餐
 */
@Getter
@Setter
public class OrderGoods {
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
     * 商品ID
     */
    private String goodsCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品价格
     */
    private BigDecimal goodsPrice;

    /**
     * 商品数量
     */
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

    /**
     * 是否组合商品 1是 0否
     */
    private Boolean isCombo;

    /**
     * 0=主餐、1=小菜
     */
    private Byte type;
}