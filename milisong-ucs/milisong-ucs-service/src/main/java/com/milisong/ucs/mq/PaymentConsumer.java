package com.milisong.ucs.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.milisong.ucs.api.UserService;
import com.milisong.ucs.dto.UserDto;
import com.milisong.ucs.enums.BusinessLineEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/25 14:18
 */
@Slf4j
@EnableBinding(MessageSink.class)
@MessageEndpoint(value = "userPaymentConsumer")
public class PaymentConsumer {

    @Resource
    UserService userService;

    @StreamListener(MessageSink.MESSAGE_PAYMENT_INTPUT)
    public void accept(Message<String> message) {
        log.info("会员系统==========接收支付MQ消息，参数={}", JSONObject.toJSONString(message));
        String jsonString = message.getPayload();
        HashMap messageMap = JSONObject.parseObject(jsonString, HashMap.class);
        String openId = MapUtils.getString(messageMap, "openId");
        Byte buzLine = MapUtils.getByte(messageMap, "orderType");
        Byte orderSource = MapUtils.getByte(messageMap, "orderSource");
        if (buzLine == null){
            buzLine = BusinessLineEnum.LUNCH.getCode();
        }
        if (orderSource != null ){
            //orderSource：早午餐标识
            buzLine = orderSource;
        }

        boolean paySuccess = MapUtils.getBoolean(messageMap, "paySuccess");
        Byte orderMode = MapUtils.getByte(messageMap, "orderMode");
        log.info("buzLine -> {}, openId -> {}, paySuccess -> {}",buzLine,openId,paySuccess);

        if (paySuccess && (orderMode != null && orderMode.intValue() == 0)) {
            updateUserInfo(openId,buzLine);
        }
    }

    private void updateUserInfo(String openId,Byte buzLine) {
        UserDto userDto = new UserDto();
        userDto.setOpenId(openId);
        userDto.setBusinessLine(buzLine);
        userDto.setIsNew((byte) 1);
        log.info("用户信息============={}", JSON.toJSONString(userDto));
        userService.updateByOpenid(userDto);
        log.info("用户用户信息成功=============");
    }

}
