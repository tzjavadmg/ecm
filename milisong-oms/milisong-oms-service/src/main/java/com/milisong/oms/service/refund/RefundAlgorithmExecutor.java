package com.milisong.oms.service.refund;

import com.milisong.oms.domain.Order;
import com.milisong.oms.domain.OrderDelivery;
import com.milisong.pms.prom.dto.OrderAmountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @ Description：退款算法执行器
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/25 10:56
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefundAlgorithmExecutor {

    private RefundAlgorithm refundStrategy;

    private RefundAlgorithmContext refundAlgorithmContext;

    public RefundAlgorithmExecutor(OrderAmountDto orderAmountDto, Order order, OrderDelivery orderDelivery) {
        refundAlgorithmContext=new RefundAlgorithmContext(orderAmountDto,order,orderDelivery);
        refundStrategy = RefundAlgorithmFactory.createRefundStrategy(orderAmountDto, order);
    }


    public RefundAlgorithm.RefundDto calculate() {
        return refundStrategy.calculate(refundAlgorithmContext);
    }


}
