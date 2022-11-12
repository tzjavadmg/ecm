package com.milisong.oms.domain;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单配送商品表。每次配送需要配送的商品明细，及相关的费用。
 */
@Getter
@Setter
public class GroupBuyOrderDeliveryGoods {
    /**
     * 主键
     */
    private Long id;

    /**
     * 订单子单ID
     */
    private Long deliveryId;

    /**
     * 组合商品编码
     */
    private String comboGoodsCode;

    /**
     * 商品ID
     */
    private String goodsCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 图片路径
     */
    private String goodsImgUrl;

    /**
     * 商品数量
     */
    private Integer goodsCount;

    /**
     * 商品原价
     */
    private BigDecimal goodsOriginalPrice;

    /**
     * 商品实价
     */
    private BigDecimal goodsActualPrice;

    /**
     * 包装费原价
     */
    private BigDecimal packageOriginalPrice;

    /**
     * 包装费实价
     */
    private BigDecimal packageActualPrice;

    /**
     * 总费用
     */
    private BigDecimal totalAmount;

    /**
     * 是否组合商品
     */
    private Boolean isCombo;

    /**
     * 0:主餐、1:小菜
     */
    private Byte type;
}