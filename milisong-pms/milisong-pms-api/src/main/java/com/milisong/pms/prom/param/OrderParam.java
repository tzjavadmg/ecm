package com.milisong.pms.prom.param;

import com.milisong.pms.prom.dto.BaseDto;
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
public class OrderParam extends BaseDto {

    private Long id;

    private Date orderDate;

    private String orderNo;

    private Long userId;

    private String realName;

    private Boolean sex;

    private String mobileNo;

    private String shopCode;

    private Long redPacketId;

    private Long couponId;

    private Byte status;

    /**
     * 红包金额
     */
    private BigDecimal redPacketAmount;

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
     * 抵扣使用积分
     */
    private Integer deductionPoints;


    /**
     * 配送单
     */
    private List<OrderDeliveryParam> deliveries;

    public BigDecimal getTotalGoodsDiscountAmount() {
        return totalGoodsOriginalAmount.subtract(totalGoodsActualAmount);
    }

    @Override
    public String toString() {
        return "OrderParam{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", orderNo='" + orderNo + '\'' +
                ", userId=" + userId +
                ", realName='" + realName + '\'' +
                ", sex=" + sex +
                ", mobileNo='" + mobileNo + '\'' +
                ", shopCode='" + shopCode + '\'' +
                ", redPacketId=" + redPacketId +
                ", couponId=" + couponId +
                ", status=" + status +
                ", redPacketAmount=" + redPacketAmount +
                ", totalGoodsOriginalAmount=" + totalGoodsOriginalAmount +
                ", totalGoodsActualAmount=" + totalGoodsActualAmount +
                ", totalDeliveryOriginalAmount=" + totalDeliveryOriginalAmount +
                ", totalDeliveryActualAmount=" + totalDeliveryActualAmount +
                ", totalPackageOriginalAmount=" + totalPackageOriginalAmount +
                ", totalPackageActualAmount=" + totalPackageActualAmount +
                ", totalPayAmount=" + totalPayAmount +
                ", totalGoodsCount=" + totalGoodsCount +
                ", totalOrderDays=" + totalOrderDays +
                ", deductionPoints=" + deductionPoints +
                ", deliveries=" + deliveries +
                '}';
    }
}
