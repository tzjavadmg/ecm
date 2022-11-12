package com.milisong.pos.production.mq;

import com.milisong.pos.production.util.MqSendUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import javax.annotation.PostConstruct;

@EnableBinding(Source.class)
public class MqMessageProducer {
    /**
     * 集单开始生产
     */
    @Autowired
    @Output(Source.ORDER_SET_START_PRODUCE_OUTPUT)
    private MessageChannel orderSetStartProduceChannel;

    @PostConstruct
    public void postConstruct() {
        MqSendUtil.setOrderSetPrintChannel(orderSetStartProduceChannel);
    }
}
