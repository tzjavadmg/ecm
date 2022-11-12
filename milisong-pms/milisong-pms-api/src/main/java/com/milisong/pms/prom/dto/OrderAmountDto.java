package com.milisong.pms.prom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 计算支付金额
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/6 17:40
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderAmountDto {
    /**
     * 最终支付总金额
     */
    private BigDecimal totalAmount;
    /**
     * 红包金额
     */
    private BigDecimal redPackAmount;

    /**
     * 满减金额
     */
    private BigDecimal fullReduceAmount;

    /**
     * 券金额(优惠了多少钱)
     */
    private BigDecimal couponAmount;

    /**
     * 券折扣
     */
    private BigDecimal couponDiscount;

    /**
     * 商品券对应的商品编码
     */
    private String couponGoodsCode;

    /**
     * 券类型： 0 新人折扣券 , 1 促活折扣券 , 2 商品券
     */
    private Byte couponType;
    /**
     * 配送单id
     */
    private Long deliveryId;

    /**
     * 积分抵扣金额
     */
    private BigDecimal deductionPointsAmount;

    /**
     * 订单获得积分
     */
    private Integer acquirePoints;

    /**
     * 本次使用多少积分
     */
    private Integer usePoints;


}
