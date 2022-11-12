package com.milisong.oms.service.refund;

import com.alibaba.fastjson.JSON;
import com.milisong.oms.domain.Order;
import com.milisong.oms.domain.OrderDelivery;
import com.milisong.oms.util.SysConfigUtils;
import com.milisong.pms.prom.dto.OrderAmountDto;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @ Description：早餐折扣券退款策略
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/25 10:59
 */
@Slf4j
public class BreakfastDiscountCouponAlgorithm implements RefundAlgorithm {
    @Override
    public BigDecimal calc(RefundAlgorithmContext refundAlgorithmContext) {
        OrderAmountDto orderAmountDto = refundAlgorithmContext.getOrderAmountDto();
        OrderDelivery orderDelivery = refundAlgorithmContext.getOrderDelivery();
        BigDecimal discount = orderAmountDto.getCouponDiscount();
        BigDecimal discountRate = BigDecimal.ONE;
        if (discount != null) discountRate = discount.divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal totalGoodsAmount = orderDelivery.getTotalGoodsActualAmount();
        return totalGoodsAmount.multiply(discountRate).setScale(2, BigDecimal.ROUND_HALF_UP).add(orderDelivery.getTotalPackageActualAmount().add(orderDelivery.getDeliveryActualPrice()));
    }

    @Override
    public RefundDto calculate(RefundAlgorithmContext refundAlgorithmContext) {
        Order order = refundAlgorithmContext.getOrder();
        //订单抵扣积分
        Integer deductionPoints = order.getDeductionPoints();
        Integer refundPoints = null;
        OrderAmountDto orderAmountDto = refundAlgorithmContext.getOrderAmountDto();
        OrderDelivery orderDelivery = refundAlgorithmContext.getOrderDelivery();
        BigDecimal totalDeliveryGoodsAmount = orderDelivery.getTotalGoodsActualAmount();
        BigDecimal pointRatio = new BigDecimal(SysConfigUtils.getPointRatio());

        BigDecimal discount = orderAmountDto.getCouponDiscount();
        BigDecimal discountRate = BigDecimal.ONE;
        if (discount != null) {
            discountRate = discount.divide(BigDecimal.TEN, 2, BigDecimal.ROUND_HALF_UP);
        }

        //子单商品实际金额，相乘折扣率
        BigDecimal totalActualGoodsAmount = totalDeliveryGoodsAmount.multiply(discountRate).setScale(2, BigDecimal.ROUND_HALF_UP);
        //分摊退款金额
        BigDecimal refundAmount = totalActualGoodsAmount.add(orderDelivery.getTotalPackageActualAmount().add(orderDelivery.getDeliveryActualPrice()));
        //分摊积分
        if (deductionPoints != null) {
            refundPoints = totalDeliveryGoodsAmount.multiply(new BigDecimal(deductionPoints)).divide(order.getTotalGoodsActualAmount(), 0, BigDecimal.ROUND_HALF_UP).intValue();
            refundAmount = refundAmount.subtract(new BigDecimal(refundPoints).divide(pointRatio, 2, BigDecimal.ROUND_HALF_UP));
        }

        RefundDto refundDto = new RefundDto();
        refundDto.setRefundAmount(refundAmount);
        refundDto.setRefundPoints(refundPoints);
        log.info(" --------------退款计算：{}", JSON.toJSONString(refundDto));
        return refundDto;
    }
}
