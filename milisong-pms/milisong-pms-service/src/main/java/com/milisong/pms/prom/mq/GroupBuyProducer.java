package com.milisong.pms.prom.mq;

import com.alibaba.fastjson.JSONObject;
import com.milisong.pms.prom.dto.groupbuy.GroupBugCancelMessage;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyUpdateUserMessage;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyWechatMsgMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author sailor wang
 * @date 2019/5/22 2:08 PM
 * @description
 */
@Slf4j
@EnableBinding(GroupBuyMessageSource.class)
@Component
public class GroupBuyProducer {

    @Resource
    @Output(GroupBuyMessageSource.CANCEL_GROUPBUY_OUTPUT)
    private MessageChannel cancelChannel;

    @Resource
    @Output(GroupBuyMessageSource.WECHAT_MSG_GROUPBUY_OUTPUT)
    private MessageChannel wechatMsgChannel;

    @Resource
    @Output(GroupBuyMessageSource.UPDATE_USER_GROUPBUY_OUTPUT)
    private MessageChannel userMsgChannel;

    public void cancelGroupBuyMQ(GroupBugCancelMessage message) {
        String jsonString = JSONObject.toJSONString(message);
        log.info("cancelGroupBuyMQ 发送拼团退款消息, 消息内容 -> {}",jsonString);
        cancelChannel.send(MessageBuilder.withPayload(jsonString).build());
    }

    public void wechatMsgNofitMQ(GroupBuyWechatMsgMessage msgMessage){
        String jsonString = JSONObject.toJSONString(msgMessage);
        log.info("发送拼团微信通知消息 -> {}",jsonString);
        wechatMsgChannel.send(MessageBuilder.withPayload(jsonString).build());
    }


    public void updateUserStatusAfterCompleteGroupBuy(GroupBuyUpdateUserMessage userMessage){
        String jsonString = JSONObject.toJSONString(userMessage);
        log.info("拼团成功后，修改'新用户'标识 -> {}",jsonString);
        userMsgChannel.send(MessageBuilder.withPayload(jsonString).build());
    }


}