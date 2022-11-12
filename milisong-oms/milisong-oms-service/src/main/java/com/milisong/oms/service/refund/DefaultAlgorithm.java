package com.milisong.oms.service.refund;

import com.alibaba.fastjson.JSON;
import com.milisong.oms.domain.Order;
import com.milisong.oms.domain.OrderDelivery;
import com.milisong.oms.util.SysConfigUtils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @ Description：
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/25 11:49
 */
@Slf4j
public class DefaultAlgorithm implements RefundAlgorithm {
    @Override
    public BigDecimal calc(RefundAlgorithmContext refundAlgorithmContext) {
        OrderDelivery orderDelivery = refundAlgorithmContext.getOrderDelivery();
        BigDecimal totalGoodsAmount = orderDelivery.getTotalGoodsActualAmount();
        return totalGoodsAmount.add(orderDelivery.getTotalPackageActualAmount().add(orderDelivery.getDeliveryActualPrice()));
    }

    @Override
    public RefundDto calculate(RefundAlgorithmContext refundAlgorithmContext) {
        OrderDelivery orderDelivery = refundAlgorithmContext.getOrderDelivery();
        BigDecimal totalDeliveryGoodsAmount = orderDelivery.getTotalGoodsActualAmount();
        BigDecimal refundAmount = totalDeliveryGoodsAmount.add(orderDelivery.getTotalPackageActualAmount().add(orderDelivery.getDeliveryActualPrice()));
        Order order = refundAlgorithmContext.getOrder();
        Integer deductionPoints = order.getDeductionPoints();
        BigDecimal pointRatio = new BigDecimal(SysConfigUtils.getPointRatio());
        Integer refundPoints = null;

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
