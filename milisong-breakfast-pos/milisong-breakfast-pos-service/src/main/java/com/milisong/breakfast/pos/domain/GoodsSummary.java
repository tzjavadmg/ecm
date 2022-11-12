package com.milisong.breakfast.pos.domain;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 商品汇总
 */
@Getter
@Setter
public class GoodsSummary {
    /**
     * 
     */
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 门店id
     */
    private Long shopId;

    /**
     * 门店code
     */
    private String shopCode;

    /**
     * 商品code
     */
    private String goodsCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 预订数量
     */
    private Integer reservedCount;

    /**
     * 配送日期
     */
    private Date deliveryDate;

    /**
     * 预订日期
     */
    private Date reservedDate;

    /**
     * 送达时间
     */
    private Date deliveryTime;

    /**
     * 订单状态 1正常 0退款
     */
    private Byte status;

    /**
     * 是否删除（0否、1是）
     */
    private Byte isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;
}