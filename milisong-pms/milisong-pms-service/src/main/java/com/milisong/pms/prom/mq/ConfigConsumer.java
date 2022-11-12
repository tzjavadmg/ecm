package com.milisong.pms.prom.mq;

import com.alibaba.fastjson.JSONObject;
import com.milisong.pms.prom.util.ConfigRedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

/**
 * 消费配置信息
 *
 * @author tianhaibo
 * @version 1.0.0
 * @date 2018/12/19 14:18
 */
@Slf4j
@EnableBinding(MessageSink.class)
@MessageEndpoint(value = "promotionConfigConsumer")
public class ConfigConsumer {

    @StreamListener(MessageSink.MESSAGE_CONFIG_INPUT)
    public void accept(Message<String> message) {
        log.info("促销系统==========接收午餐配置MQ消息，参数={}", JSONObject.toJSONString(message));
        String msg = message.getPayload();
        ConfigRedisUtils.updateConfigMsg(msg);
    }

    @StreamListener(MessageSink.MESSAGE_CONFIG_BF_INPUT)
    public void acceptBfConfig(Message<String> message) {
        log.info("促销系统==========接收早餐配置MQ消息，参数={}", JSONObject.toJSONString(message));
        String msg = message.getPayload();
        ConfigRedisUtils.updateConfigBfMsg(msg);
    }
}
