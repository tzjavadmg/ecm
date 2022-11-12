package com.milisong.oms.service;

import com.farmland.core.api.ResponseResult;
import com.milisong.oms.domain.Order;
import com.milisong.oms.dto.WechatMiniPayDto;
import com.milisong.oms.param.CancelOrderParam;
import com.milisong.oms.param.OrderPaymentParam;


/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/8 18:10
 */
public interface OrderPayTransactionService {

    ResponseResult<WechatMiniPayDto> payment(OrderPaymentParam orderPaymentParam, Order order);

    ResponseResult<?> cancelOrder(CancelOrderParam cancelOrderParam);

}
