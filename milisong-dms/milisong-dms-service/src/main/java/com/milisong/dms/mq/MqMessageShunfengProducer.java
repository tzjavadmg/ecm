package com.milisong.dms.mq;

import com.alibaba.fastjson.JSON;
import com.milisong.dms.dto.shunfeng.DeliveryOrderMqDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

@Slf4j
@EnableBinding(MqMessageShunfengSource.class)
public class MqMessageShunfengProducer {
    @Resource
    @Output(MqMessageShunfengSource.SF_STATUS_CHANGE_OUTPUT)
    private MessageChannel sfStatusChangeOutput;

    public void sendShunfengBackMsg(DeliveryOrderMqDto dto) {
        String jsonMsg = JSON.toJSONString(dto);
        try {
            log.info("sendShunfengBackMsg() mq发送消息:{}", jsonMsg);
            sfStatusChangeOutput.send(MessageBuilder.withPayload(jsonMsg).build());
            log.info("发送完毕");
        } catch (Exception e) {
            log.error("mq发送失败:{}", e);
        }
    }
}
