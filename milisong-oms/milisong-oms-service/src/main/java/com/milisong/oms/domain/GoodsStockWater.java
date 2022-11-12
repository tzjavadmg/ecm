package com.milisong.oms.domain;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 记录商品库存流水，用于以下两点：
 * 1.每日在售库存控制
 * 2.每日多配送段产量控制
 */
@Getter
@Setter
public class GoodsStockWater {
    /**
     * 
     */
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 配送ID
     */
    private Long deliveryId;

    /**
     * 门店编号
     */
    private String shopCode;

    /**
     * 库存来源 0:虚拟订单;1:取消虚拟订单;2:取消订单;3:退款;4:设置在售库存;
     */
    private Byte source;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 商品数量，可为负值。负值表示扣减
     */
    private Integer goodsCount;

    /**
     * 库存原始数量
     */
    private Integer oldStockCount;

    /**
     * 
     */
    private Integer newStockCount;

    /**
     * 配送日期
     */
    private Date deliveryDate;
}