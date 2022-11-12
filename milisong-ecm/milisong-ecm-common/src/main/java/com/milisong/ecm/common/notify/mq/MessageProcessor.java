package com.milisong.ecm.common.notify.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.SubscribableChannel;

/**
 *  消息input
 * @author User
 *
 */
public interface MessageProcessor extends Sink,Source {

    /**
     * ivr消息input
     */
    String MESSAGE_IVR_INTPUT  = "ivr_input";

    /**
     * 支付消息input
     */
    String MESSAGE_NOTIFY_PAYMENT_INPUT = "notify_payment_input";

    /**
     * 送达消息input
     */
    String MESSAGE_NOTIFY_ARRIVE_INPUT = "notify_arrive_input";

    /**
     *拼团通知
     */
    String WECHATMSG_GROUPBUY_INPUT = "wechatmsg_groupbuy_input";


    /**
     * ivr消息消费通道
     * @return
     */
    @Input(MessageProcessor.MESSAGE_IVR_INTPUT)
    SubscribableChannel messageIvrInput();

    /**
     * 支付消息消费通道
     * @return
     */
    @Input(MessageProcessor.MESSAGE_NOTIFY_PAYMENT_INPUT)
    SubscribableChannel messagePaymentInput();

    /**
     * 通知消息消费通道
     * @return
     */
    @Input(MessageProcessor.MESSAGE_NOTIFY_ARRIVE_INPUT)
    SubscribableChannel messageNotifyInput();

    /**
     * 拼团通知
     * @return
     */
    @Input(MessageProcessor.WECHATMSG_GROUPBUY_INPUT)
    SubscribableChannel messageNotifyArriveInput();

}
