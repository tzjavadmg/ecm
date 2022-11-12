package com.milisong.oms.service.refund;

import com.alibaba.fastjson.JSON;
import com.milisong.oms.domain.Order;
import com.milisong.oms.domain.OrderDelivery;
import com.milisong.oms.util.SysConfigUtils;
import com.milisong.pms.prom.dto.OrderAmountDto;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @ Description：早餐商品券退款策略
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/25 10:59
 */
@Slf4j
public class BreakfastGoodsCouponAlgorithm implements RefundAlgorithm {
    @Override
    public BigDecimal calc(RefundAlgorithmContext refundAlgorithmContext) {
        OrderAmountDto orderAmountDto = refundAlgorithmContext.getOrderAmountDto();
        OrderDelivery orderDelivery = refundAlgorithmContext.getOrderDelivery();
        Long deliveryId = orderAmountDto.getDeliveryId();
        BigDecimal totalGoodsAmount = orderDelivery.getTotalGoodsActualAmount();
        BigDecimal couponAmount = orderAmountDto.getCouponAmount();
        if (orderDelivery.getId().equals(deliveryId)) {
            return totalGoodsAmount.subtract(couponAmount).add(orderDelivery.getTotalPackageActualAmount().add(orderDelivery.getDeliveryActualPrice()));
        }

        return totalGoodsAmount.add(orderDelivery.getTotalPackageActualAmount().add(orderDelivery.getDeliveryActualPrice()));
    }

    @Override
    public RefundDto calculate(RefundAlgorithmContext refundAlgorithmContext) {
        log.info(" 早餐商品券退款算法入参：{}", JSON.toJSONString(refundAlgorithmContext));
        OrderAmountDto orderAmountDto = refundAlgorithmContext.getOrderAmountDto();
        OrderDelivery orderDelivery = refundAlgorithmContext.getOrderDelivery();
        Order order = refundAlgorithmContext.getOrder();
        Integer deductionPoints = order.getDeductionPoints();
        Integer refundPoints = null;
        BigDecimal pointRatio = new BigDecimal(SysConfigUtils.getPointRatio());

        //总商品价格
        BigDecimal totalActualGoodsAmount = order.getTotalGoodsActualAmount();
        //子单总商品价格
        BigDecimal totalDeliveryActualGoodsAmount = orderDelivery.getTotalGoodsActualAmount();
        //商品券优惠了多少钱
        BigDecimal couponAmount = orderAmountDto.getCouponAmount();
        //本单优惠分摊金额
        BigDecimal sharedCouponAmount = totalDeliveryActualGoodsAmount.multiply(couponAmount).divide(order.getTotalGoodsActualAmount(), 2, BigDecimal.ROUND_HALF_UP);

        //TODO 促销计算中应该返回每天子单促销后的商品实际总价，用于退款计算。而不是将逻辑重新写一遍在这里，这样可能两边的逻辑不一致
        BigDecimal refundAmount = totalDeliveryActualGoodsAmount.subtract(sharedCouponAmount).add(orderDelivery.getTotalPackageActualAmount().add(orderDelivery.getDeliveryActualPrice()));

        if (deductionPoints != null) {
            //退款积分=总抵扣积分*(子单总商品费用/订单总商品费用)
            refundPoints = totalDeliveryActualGoodsAmount.multiply(new BigDecimal(deductionPoints)).divide(totalActualGoodsAmount, 0, BigDecimal.ROUND_HALF_UP).intValue();
            refundAmount = refundAmount.subtract(new BigDecimal(refundPoints).divide(pointRatio, 2, BigDecimal.ROUND_HALF_UP));
        }

        RefundDto refundDto = new RefundDto();
        refundDto.setRefundAmount(refundAmount);
        refundDto.setRefundPoints(refundPoints);
        log.info(" --------------退款计算：{}", JSON.toJSONString(refundDto));
        return refundDto;
    }
}

