package com.milisong.oms.service.refund;

import com.alibaba.fastjson.JSON;
import com.milisong.oms.domain.Order;
import com.milisong.oms.domain.OrderDelivery;
import com.milisong.pms.prom.dto.OrderAmountDto;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @ Description：午餐默认退款策略
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/25 10:54
 */
@Slf4j
public class LunchDefaultAlgorithm implements RefundAlgorithm {

    @Override
    public BigDecimal calc(RefundAlgorithmContext refundAlgorithmContext) {
        Order order = refundAlgorithmContext.getOrder();
        OrderDelivery orderDelivery = refundAlgorithmContext.getOrderDelivery();
        BigDecimal totalPayAmount = order.getTotalPayAmount();
        BigDecimal totalOrderAmount = order.getTotalGoodsActualAmount().add(order.getTotalPackageActualAmount());

        BigDecimal totalDeliveryAmount = orderDelivery.getTotalGoodsActualAmount().add(orderDelivery.getTotalPackageActualAmount());

        return totalDeliveryAmount.multiply(totalPayAmount).divide(totalOrderAmount, 2, RoundingMode.HALF_UP);
    }

    @Override
    public RefundDto calculate(RefundAlgorithmContext refundAlgorithmContext) {
        log.info("----------------午餐退款计算----------------------{}", JSON.toJSONString(refundAlgorithmContext));
        Order order = refundAlgorithmContext.getOrder();
        OrderDelivery orderDelivery = refundAlgorithmContext.getOrderDelivery();
        OrderAmountDto orderAmountDto = refundAlgorithmContext.getOrderAmountDto();
        //红包金额
        BigDecimal redPackAmount = orderAmountDto.getRedPackAmount();
        //满减金额
        BigDecimal fullReduceAmount = orderAmountDto.getFullReduceAmount();
        //子单总金额
        BigDecimal deliveryTotalAmount = orderDelivery.getTotalAmount();
        //子单商品金额
        BigDecimal deliveryTotalGoodsActualAmount = orderDelivery.getTotalGoodsActualAmount();
        //子单包装费
        BigDecimal deliveryTotalPackageActualAmount = orderDelivery.getTotalPackageActualAmount();
        //主单商品金额
        BigDecimal orderTotalGoodsActualAmount = order.getTotalGoodsActualAmount();
        //主单包装费
        BigDecimal orderTotalPackageActualAmount = order.getTotalPackageActualAmount();

        BigDecimal refundAmount = deliveryTotalGoodsActualAmount.add(deliveryTotalPackageActualAmount).add(orderDelivery.getDeliveryActualPrice());

//        if (redPackAmount != null) {
//            BigDecimal shareRedPackAmount = redPackAmount.multiply(deliveryTotalGoodsActualAmount).divide(orderTotalGoodsActualAmount, 2, RoundingMode.HALF_UP);
//            log.info("----分摊红包金额---{}", shareRedPackAmount);
//            refundAmount = refundAmount.subtract(shareRedPackAmount);
//        }
//
//        if (fullReduceAmount != null) {
//            BigDecimal shareReduceAmount = fullReduceAmount.multiply(deliveryTotalGoodsActualAmount.add(deliveryActualPrice)).divide(orderTotalGoodsActualAmount.add(orderTotalDeliveryActualAmount), 2, RoundingMode.HALF_UP);
//            log.info("----分摊满减金额---{}", shareReduceAmount);
//            refundAmount = refundAmount.subtract(shareReduceAmount);
//        }

        //FIXME 每种促销方式统一退款分摊算法
        if (fullReduceAmount != null) {
            BigDecimal shareReduceAmount = fullReduceAmount.multiply(deliveryTotalGoodsActualAmount.add(deliveryTotalPackageActualAmount)).divide(orderTotalGoodsActualAmount.add(orderTotalPackageActualAmount), 2, RoundingMode.HALF_UP);
            log.info("----分摊满减金额---{}", shareReduceAmount);
            refundAmount = refundAmount.subtract(shareReduceAmount);
            if (redPackAmount != null) {
                BigDecimal shareRedPackAmount = redPackAmount.multiply(deliveryTotalGoodsActualAmount.add(deliveryTotalPackageActualAmount)).divide(orderTotalGoodsActualAmount.add(orderTotalPackageActualAmount), 2, RoundingMode.HALF_UP);
                log.info("----分摊红包金额---{}", shareRedPackAmount);
                refundAmount = refundAmount.subtract(shareRedPackAmount);
            }
        } else {
            if (redPackAmount != null) {
                BigDecimal shareRedPackAmount = redPackAmount.multiply(deliveryTotalGoodsActualAmount).divide(orderTotalGoodsActualAmount, 2, RoundingMode.HALF_UP);
                log.info("----分摊红包金额---{}", shareRedPackAmount);
                refundAmount = refundAmount.subtract(shareRedPackAmount);
            }
        }
        refundAmount=refundAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
        RefundDto refundDto = new RefundDto();
        refundDto.setRefundAmount(refundAmount);
        return refundDto;
    }
}
