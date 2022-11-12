package com.milisong.oms.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 *  消息input
 * @author User
 *
 */
public interface MessageSink {
    /**
     * order消息input
     */
    String MESSAGE_ORDER_INTPUT  = "order_status_input";

    /**
     * 支付消息input
     */
    String MESSAGE_PAYMENT_INTPUT = "order_payment_input";

    String CANCEL_GROUPBUY_INPUT = "cancel_groupbuy_input";

    /**
     * 订单状态消息
     *
     * @return
     */
    @Input(MessageSink.MESSAGE_ORDER_INTPUT)
    SubscribableChannel messageOrderInput();

    /**
     * 支付状态消息
     * @return
     */
    @Input(MessageSink.MESSAGE_PAYMENT_INTPUT)
    SubscribableChannel messagePaymentInput();

    /**
     * 支付状态消息
     * @return
     */
    @Input(MessageSink.CANCEL_GROUPBUY_INPUT)
    SubscribableChannel cancelGroupbuyInput();
}
