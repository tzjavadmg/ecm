package com.milisong.oms.domain;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 订单表。
 * 1.金额相关信息，包括基础的商品费用、打包费、配送费等，同时记录商品优惠、活动促销、红包、券等金额数据。
 * 2.配送信息。一个订单只能有一个配送地址，但可以分多次配送。
 */
@Getter
@Setter
public class GroupBuyOrder {
    /**
     * 
     */
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单时间
     */
    private Date orderDate;

    /**
     * 联系人姓名
     */
    private String realName;

    /**
     * 性别：0:女;1:男
     */
    private Byte sex;

    /**
     * 联系手机号
     */
    private String mobileNo;

    /**
     * 配送地址ID
     */
    private Long deliveryAddressId;

    /**
     * 通知类型 0:电话,1:短信
     */
    private Byte notifyType;

    /**
     * 门店编码
     */
    private String shopCode;

    /**
     * 配送楼宇ID
     */
    private Long deliveryBuildingId;

    /**
     * 配送楼宇名称
     */
    private String deliveryBuildingName;

    /**
     * 配送公司名称
     */
    private String deliveryCompany;

    /**
     * 配送楼层
     */
    private String deliveryFloor;

    /**
     * 聚餐点ID
     */
    private Long takeMealsAddrId;

    /**
     * 取餐点名称
     */
    private String takeMealsAddrName;

    /**
     * 支付方式：0:微信代扣;1:微信支付
     */
    private Byte payMode;

    /**
     * 订单来源：1:微信小程序
     */
    private Byte orderSource;

    /**
     * 微信openid
     */
    private String openId;

    /**
     * 券ID
     */
    private Long couponId;

    /**
     * 红包ID
     */
    private Long redPacketId;

    /**
     * 团购ID
     */
    private Long groupBuyId;

    /**
     * 红包金额
     */
    private BigDecimal redPacketAmount;

    /**
     * 满减金额
     */
    private BigDecimal fullReduceAmount;

    /**
     * 券优惠金额
     */
    private BigDecimal couponAmount;

    /**
     * 积分抵扣金额
     */
    private BigDecimal deductionPointsAmount;

    /**
     * 抵扣使用积分
     */
    private Integer deductionPoints;

    /**
     * 订单获得积分
     */
    private Integer acquirePoints;

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
     * 订单状态：1:已支付;2:已成团;3:已取消;4:已退款;
     */
    private Byte status;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 取消原因
     */
    private String cancelReason;
}