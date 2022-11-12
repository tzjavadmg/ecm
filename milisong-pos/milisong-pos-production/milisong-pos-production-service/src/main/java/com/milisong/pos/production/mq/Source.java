package com.milisong.pos.production.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 定义消息生产者队列
 *
 * @author yangzhilong
 */
public interface Source {
    // 集单开始生产
    String ORDER_SET_START_PRODUCE_OUTPUT = "order_set_start_produce_output";

    @Output(ORDER_SET_START_PRODUCE_OUTPUT)
    MessageChannel orderSetStartProduceMsgOutput();
}
