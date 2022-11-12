package com.milisong.ucs.mq;

import com.alibaba.fastjson.JSONObject;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyUpdateUserMessage;
import com.milisong.ucs.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sailor wang
 * @date 2019/5/27 2:03 AM
 * @description
 */
@Slf4j
@EnableBinding(MessageSink.class)
@MessageEndpoint(value = "groupBuyConsumer")
public class GroupBuyConsumer {

    @Resource
    UserMapper userMapper;

    @StreamListener(MessageSink.MESSAGE_GROUPBUY_INTPUT)
    public void accept(Message<String> message) {
        log.info("拼团完成后修改用户状态 message -> {}", JSONObject.toJSONString(message));
        String jsonString = message.getPayload();

        try {
            GroupBuyUpdateUserMessage userMessage = JSONObject.parseObject(jsonString,GroupBuyUpdateUserMessage.class);
            if (CollectionUtils.isNotEmpty(userMessage.getUserIdList())){
                List<Long> userIdList = userMessage.getUserIdList();
                userMapper.batchUpdateUserStatus(userIdList);
            }
        } catch (Exception e) {
            log.error("",e);
        }

    }

}
