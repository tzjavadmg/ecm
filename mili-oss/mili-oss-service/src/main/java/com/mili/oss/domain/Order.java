package com.mili.oss.domain;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单表-早餐
 */
@Getter
@Setter
public class Order {
    /**
     * 主键
     */
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 门店id
     */
    private Long shopId;

    /**
     * 门店编码
     */
    private String shopCode;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String mobileNo;

    /**
     * 配送起始时间:预定的第二天配送
     */
    private Date deliveryStartDate;

    /**
     * 配送结束时间
     */
    private Date deliveryEndDate;

    /**
     * 配送楼宇:写字楼名称ID
     */
    private Long deliveryOfficeBuildingId;

    /**
     * 配送公司：ID
     */
    private Long deliveryCompanyId;

    /**
     * 配送公司:公司名称
     */
    private String deliveryCompany;

    /**
     * 配送地址
     */
    private String deliveryAddress;

    /**
     * 楼层
     */
    private String deliveryFloor;

    /**
     * 房间号（取餐点）
     */
    private String deliveryRoom;

    /**
     * 聚餐点ID
     */
    private Long takeMealsAddrId;

    /**
     * 取餐点名称
     */
    private String takeMealsAddrName;

    /**
     * 订单中商品总数量(累计套餐内商品数)
     */
    private Integer goodsSum;

    /**
     * SKU商品总数量(套餐记一个)
     */
    private Integer skuSum;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 实际支付金额
     */
    private BigDecimal actualPayAmount;

    /**
     * 配送费金额
     */
    private BigDecimal deliveryCostAmount;

    /**
     * 折扣后金额
     */
    private BigDecimal discountAmount;

    /**
     * 打包费
     */
    private BigDecimal packageAmount;

    /**
     * 红包金额
     */
    private BigDecimal redPacketAmount;

    /**
     * 配送完成时间
     */
    private Date completeTime;

    /**
     * 2:已支付 3:已申请退款 4:已退款
     */
    private Byte orderStatus;

    /**
     * -1备餐中 0 已派单 1已接单 2已到店  3已取餐 4已完成
     */
    private Byte deliveryStatus;

    /**
     * 集单处理状态 1已处理 0未处理
     */
    private Boolean ordersetProcessStatus;

    /**
     * 订单类型0:早餐;1:中餐
     */
    private Byte orderType;

    /**
     * 库存状态 0正常 1缺货
     */
    private Byte stockStatus;
}