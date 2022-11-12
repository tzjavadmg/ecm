package com.milisong.oms.service.refund;

import com.milisong.oms.domain.Order;
import com.milisong.oms.domain.OrderDelivery;
import com.milisong.pms.prom.dto.OrderAmountDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @ Description：退款算法上下文
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/25 11:36
 */
@Getter
@Setter
@AllArgsConstructor
public
class RefundAlgorithmContext {

    private OrderAmountDto orderAmountDto;

    private Order order;

    private OrderDelivery orderDelivery;
}
