package com.milisong.oms.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/3 17:38
 */
public interface MessageSource {

    /**
     * 消息output
     */
    final String ORDER_OUTPUT = "order_output";

    /**
     * 消息output
     */
    final String PAYMENT_OUTPUT = "payment_output";

    /**
     * 退款成功消息
     */
    final String REFUND_OUTPUT = "refund_output";
    /**
     * 取消订单消息
     */
    final String CANCEL_OUTPUT = "cancel_output";


    /**
     * 订单消息
     *
     * @return 消息通道
     */
    @Output(ORDER_OUTPUT)
    MessageChannel orderOutput();

    /**
     * 支付消息
     *
     * @return
     */
    @Output(PAYMENT_OUTPUT)
    MessageChannel paymentOutput();

    /**
     * 退款消息
     *
     * @return
     */
    @Output(REFUND_OUTPUT)
    MessageChannel refundOutput();

    /**
     * 退款消息
     *
     * @return
     */
    @Output(CANCEL_OUTPUT)
    MessageChannel cancelOutput();
}
