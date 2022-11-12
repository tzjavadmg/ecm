package com.milisong.pms.prom.mq;

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
    String MESSAGE_PAYMENT_INTPUT = "promotion_payment_input";

    /**
     * 消费午餐配置信息
     */
    String MESSAGE_CONFIG_INPUT = "config_input";

    /**
     * 消费早餐配置信息
     */
    String MESSAGE_CONFIG_BF_INPUT = "config_bf_input";

    /**
     * 消费退款信息
     */
    String MESSAGE_REFUND_INPUT = "refund_input";

    /**
     * 消费订单取消信息
     */
    String MESSAGE_CANCLE_INPUT = "cancle_input";

    /**
     * 订单状态消费，消费完成状态
     */
    String MESSAGE_ORDER_STATUS_INPUT = "order_status_input";

    /**
     * 支付状态消息
     * @return
     */
    @Input(MessageSink.MESSAGE_PAYMENT_INTPUT)
    SubscribableChannel messagePaymentInput();

    /**
     * 消费配置信息通道
     * @return
     */
    @Input(MessageSink.MESSAGE_CONFIG_INPUT)
    SubscribableChannel messageConfigInput();

    /**
     * 消费早餐配置信息通道
     * @return
     */
    @Input(MessageSink.MESSAGE_CONFIG_BF_INPUT)
    SubscribableChannel messageConfigBfInput();

    /**
     * 消费退款信息通道
     * @return
     */
    @Input(MessageSink.MESSAGE_REFUND_INPUT)
    SubscribableChannel messageRefundInput();

    /**
     * 消费订单取消信息通道
     * @return
     */
    @Input(MessageSink.MESSAGE_CANCLE_INPUT)
    SubscribableChannel messageCancleInput();

    /**
     * 消费订单完成状态信息通道
     * @return
     */
    @Input(MessageSink.MESSAGE_ORDER_STATUS_INPUT)
    SubscribableChannel messageOrderStatusInput();
}
