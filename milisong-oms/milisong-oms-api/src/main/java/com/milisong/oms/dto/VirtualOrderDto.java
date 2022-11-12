package com.milisong.oms.dto;

import com.milisong.oms.constant.GoodsType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/3 15:24
 */
@Getter
@Setter
public class VirtualOrderDto {

    private Long id;

    private String orderDate;

    private String shopCode;

    private BigDecimal totalGoodsOriginalAmount;

    private BigDecimal totalGoodsActualAmount;

    private BigDecimal totalDeliveryOriginalAmount;

    private BigDecimal totalDeliveryActualAmount;

    private BigDecimal totalPackageOriginalAmount;

    private BigDecimal totalPackageActualAmount;

    private BigDecimal totalPayAmount;

    private BigDecimal fullReduceAmount;

    private Integer totalGoodsCount;

    private Integer totalOrderDays;

    private Integer usePoints;
    /**
     * 订单获得积分
     */
    private Integer acquirePoints;

    /**
     * 配送单
     */
    private List<VirtualOrderDeliveryDto> deliveries;

    public BigDecimal getTotalGoodsDiscountAmount() {
        return totalGoodsOriginalAmount.subtract(totalGoodsActualAmount);
    }

    public Integer getPackageCount() {
        return deliveries.stream().flatMap(delivery -> delivery.getDeliveryGoods().stream())
                .filter(g -> g.getType() == null || GoodsType.MAIN_MEAL.getValue() == g.getType())
                .mapToInt(VirtualOrderDeliveryGoodsDto::getGoodsCount).sum();
    }
}
