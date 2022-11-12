package com.mili.oss.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 订单明细表-组合明细-早餐
 */
@Getter
@Setter
public class OrderGoodsCombo {
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
     * 商品code
     */
    private String goodsCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品数量
     */
    private Integer goodsCount;

    /**
     * 组合商品code
     */
    private String comboGoodsCode;

    /**
     * 组合商品名称
     */
    private String comboGoodsName;

    /**
     * 组合商品数量
     */
    private Integer comboGoodsCount;
}