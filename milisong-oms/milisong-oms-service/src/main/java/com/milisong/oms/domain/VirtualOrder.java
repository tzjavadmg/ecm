package com.milisong.oms.domain;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 虚拟订单表，支付金额相关信息。
 * 1.预计算商品、配送费、打包费等相关金额信息
 * 2.锁定库存
 */
@Getter
@Setter
public class VirtualOrder {
    /**
     * 
     */
    private Long id;

    /**
     * 订单时间
     */
    private Date orderDate;

    /**
     * 联系人姓名
     */
    private Long userId;

    /**
     * 门店编码
     */
    private String shopCode;

    /**
     * 虚拟订单状态：0:待生成订单;1:已取消;2:已生成订单;
     */
    private Byte status;

    /**
     * 微信openid
     */
    private String openId;

    /**
     * 商品原价总金额
     */
    private BigDecimal totalGoodsOriginalAmount;

    /**
     * 商品实价总金额
     */
    private BigDecimal totalGoodsActualAmount;

    /**
     * 配送费原价总金额
     */
    private BigDecimal totalDeliveryOriginalAmount;

    /**
     * 配送费实价总金额
     */
    private BigDecimal totalDeliveryActualAmount;

    /**
     * 包装费原价总金额
     */
    private BigDecimal totalPackageOriginalAmount;

    /**
     * 包装费实价总金额
     */
    private BigDecimal totalPackageActualAmount;

    /**
     * 满减金额
     */
    private BigDecimal fullReduceAmount;

    /**
     * 支付总金额
     */
    private BigDecimal totalPayAmount;

    /**
     * 总商品数量
     */
    private Integer totalGoodsCount;

    /**
     * 总订单天数
     */
    private Integer totalOrderDays;

    /**
     * 订单类型：0:早餐;1:午餐;2:下午茶;3:晚餐;4:夜宵
     */
    private Byte orderType;

    /**
     * 取消时间
     */
    private Date cancelTime;
}