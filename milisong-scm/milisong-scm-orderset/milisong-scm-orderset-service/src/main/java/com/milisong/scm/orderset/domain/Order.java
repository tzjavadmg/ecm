package com.milisong.scm.orderset.domain;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单表
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
     * 配送公司id
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
     * 房间号
     */
    private String deliveryRoom;

    /**
     * 取餐点id
     */
    private Long takeMealsAddrId;

    /**
     * 取餐名字
     */
    private String takeMealsAddrName;

    /**
     * 订单中商品总数量
     */
    private Integer goodsSum;

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
     * 0:配送中 1:已打包-待配送 2:已支付-备餐中 3:已申请退款 4:已退款 9:已完成
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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 订单类型0:普通;1:预定
     */
    private Byte orderType;
}