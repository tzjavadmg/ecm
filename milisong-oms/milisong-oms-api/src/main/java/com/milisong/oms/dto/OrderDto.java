package com.milisong.oms.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/8 15:01
 */
@Getter
@Setter
public class OrderDto {

    private Long id;

    private Date orderDate;

    private String orderNo;

    private Long userId;

    private String realName;

    private Boolean sex;

    private String mobileNo;

    private Byte notifyType;

    private Byte orderType;

    private Byte orderSource;

    private Long deliveryAddressId;

    private Long deliveryBuildingId;

    private String deliveryBuildingName;

    private String deliveryCompany;

    private String deliveryFloor;

    private String shopCode;

    private Long redPacketId;

    private Long couponId;

    /**
     * 抵扣使用积分
     */
    private Long deductionPoints;
    /**
     * 订单获得积分
     */
    private Long acquirePoints;

    private Byte status;

    private Byte orderMode;
    /**
     * 红包金额
     */
    private BigDecimal redPacketAmount;
    /**
     * 满减金额
     */
    private BigDecimal fullReduceAmount;

    private BigDecimal couponAmount;

    private BigDecimal totalGoodsOriginalAmount;

    private BigDecimal totalGoodsActualAmount;

    private BigDecimal totalDeliveryOriginalAmount;

    private BigDecimal totalDeliveryActualAmount;

    private BigDecimal totalPackageOriginalAmount;

    private BigDecimal totalPackageActualAmount;

    private BigDecimal totalPayAmount;

    private Integer totalGoodsCount;

    private Integer totalOrderDays;
    /**
     * 配送开始时间
     */
    private String deliveryStartTime;

    /**
     * 配送结束时间
     */
    private String deliveryEndTime;

    /**
     * 聚餐点ID
     */
    private Long takeMealsAddrId;

    /**
     * 取餐点名称
     */
    private String takeMealsAddrName;

    /**
     * 配送单
     */
    private List<OrderDeliveryDto> deliveries;

    public BigDecimal getTotalGoodsDiscountAmount() {
        return totalGoodsOriginalAmount.subtract(totalGoodsActualAmount);
    }
}
