package com.milisong.oms.mq;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/25 14:20
 */
@Slf4j
@EnableBinding(MessageSource.class)
@Component
public class MessageProducer {
    @Resource
    @Output(MessageSource.PAYMENT_OUTPUT)
    private MessageChannel companyChannel;

    @Resource
    @Output(MessageSource.REFUND_OUTPUT)
    private MessageChannel refundChannel;

    @Resource
    @Output(MessageSource.CANCEL_OUTPUT)
    private MessageChannel cancelChannel;
//
//    @Resource
//    private MqProductionEventMapper mqProductionEventMapper;

    public void send(PaymentMessage message) {
        String jsonString = JSONObject.toJSONString(message);
        Message<String> mqMessage = MessageBuilder.withPayload(jsonString).setHeader("businessLine", message.getOrderType()).build();
        //UUID messageId = mqMessage.getHeaders().getId();
        //MqProductionEvent mqProductionEvent = new MqProductionEvent();
        //mqProductionEvent.setId(IdGenerator.nextId());
        //if (messageId != null) {
        //    mqProductionEvent.setMessageId(messageId.toString());
        //}
        //mqProductionEvent.setMessageTopic("ECM_PAYMENT");
        //mqProductionEvent.setMessageBody(jsonString);
        //mqProductionEvent.setProducer("milisong-ecm-order");
        //mqProductionEvent.setStatus(ProductionEventStatus.SEND.getValue());
        //mqProductionEventMapper.insert(mqProductionEvent);
        companyChannel.send(mqMessage);
        log.info("------------发送支付成功MQ消息：{}", jsonString);
    }


    public void send(List<RefundMessage> messages) {
        String jsonString = JSONObject.toJSONString(messages);
        refundChannel.send(MessageBuilder.withPayload(jsonString).build());
        log.info("------------发送退款申请成功MQ消息：{}", jsonString);
    }

    public void send(CancelMessage message) {
        String jsonString = JSONObject.toJSONString(message);
        cancelChannel.send(MessageBuilder.withPayload(jsonString).build());
        log.info("------------发送支付成功MQ消息：{}", jsonString);
    }

}
