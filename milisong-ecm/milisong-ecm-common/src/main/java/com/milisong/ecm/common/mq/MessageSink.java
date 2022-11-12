package com.milisong.ecm.common.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/25 14:11
 */
public interface MessageSink {


    /**
     * 支付消息input
     */
    String MESSAGE_PAYMENT_INTPUT = "user_payment_input";

    /**
     * 支付状态消息
     *
     * @return
     */
    @Input(MessageSink.MESSAGE_PAYMENT_INTPUT)
    SubscribableChannel messagePaymentInput();

}
