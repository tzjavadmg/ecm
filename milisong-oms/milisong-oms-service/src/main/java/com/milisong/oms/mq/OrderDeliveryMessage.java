package com.milisong.oms.mq;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 发送给SCM的MQ消息类：配送表信息
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/12 15:37
 */
@Getter
@Setter
public class OrderDeliveryMessage {

    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 订单编号
     */
    private String deliveryNo;
    /**
     * 门店编码
     */
    private String shopCode;
    /**
     * 用户名称
     */
    private String realName;
    /**
     * 用户手机号
     */
    private String mobileNo;
    /**
     * 配送公司上班时间
     */
    private Date deliveryCompanyWorkingTime;
    /**
     * 配送单配送时段开始时间
     */
    private Date deliveryTimezoneFrom;
    /**
     * 配送单配送时段结束时间
     */
    private Date deliveryTimezoneTo;
    /**
     * 配送楼宇ID
     */
    private Long deliveryBuildingId;
    /**
     * 配送公司名称
     */
    private String deliveryCompany;
    /**
     * 配送楼层
     */
    private String deliveryFloor;
    /**
     * 订单实际支付金额
     */
    private BigDecimal orderActualAmount;
    /**
     * 配送单实际总金额= 商品实际金额+配送费实际金额+打包费实际金额
     */
    private BigDecimal totalActualAmount;
    /**
     * 分摊订单实际支付金额
     */
    private BigDecimal shareOrderPayAmount;
    /**
     * 商品原价金额
     */
    private BigDecimal goodsOriginalAmount;
    /**
     * 商品实际金额
     */
    private BigDecimal goodsActualAmount;
    /**
     * 配送费实际金额
     */
    private BigDecimal deliveryActualAmount;
    /**
     * 打包费实际金额
     */
    private BigDecimal packageActualAmount;
    /**
     * 取餐点ID
     */
    private Long takeMealsAddrId;
    /**
     * 取餐点名称
     */
    private String takeMealsAddrName;
    /**
     * 订单类型
     */
    private Byte orderType;
    /**
     * 下单日期
     */
    private Date orderDate;
    /**
     * 配送状态
     */
    private Byte deliveryStatus;
    /**
     * 配送单商品明细
     */
    private List<OrderDeliveryGoodsMessage> deliveryGoods;
}
