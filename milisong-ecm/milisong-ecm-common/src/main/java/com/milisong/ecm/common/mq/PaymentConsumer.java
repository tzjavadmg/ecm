package com.milisong.ecm.common.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.milisong.ecm.common.notify.properties.NotifyProperties;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ucs.api.UserDeliveryAddressService;
import com.milisong.ucs.api.UserService;
import com.milisong.ucs.dto.UserDeliveryAddressDto;

import com.milisong.ucs.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
    private UserService userService;
    @Resource
    private UserDeliveryAddressService userDeliveryAddressService;

    @Autowired
    private NotifyProperties notifyProperties;

    @StreamListener(MessageSink.MESSAGE_PAYMENT_INTPUT)
    public void accept(Message<String> message) {
        log.info("通知系统==========接收支付MQ消息，参数={}", JSONObject.toJSONString(message));
        String jsonString = message.getPayload();
        HashMap messageMap = JSONObject.parseObject(jsonString, HashMap.class);
        Byte orderMode = MapUtils.getByte(messageMap, "orderMode");
        Long userId = MapUtils.getLong(messageMap, "userId");
        Long deliveryAddressId = MapUtils.getLong(messageMap, "deliveryAddressId");
        cacheLatestDeliveryAddress(userId, deliveryAddressId);

    }

    private void cacheLatestDeliveryAddress(Long userId, Long deliveryAddressId) {

        if (deliveryAddressId != null) {
            ResponseResult<UserDeliveryAddressDto> responseResult = userDeliveryAddressService.queryDeliveryById(deliveryAddressId);
            if (responseResult.isSuccess()) {
                UserDeliveryAddressDto userDeliveryAddressDto = responseResult.getData();
                String latestDeliveryAddressKey = RedisKeyUtils.getLatestDeliveryAddressKey(userId);
                if (!checkUserWithBusinessLine(userId, notifyProperties.getBusinessLine())) {
                    log.info("用户来源鉴定不通过，不记录下单信息缓存");
                    return;
                }
                RedisCache.setEx(latestDeliveryAddressKey, JSON.toJSONString(userDeliveryAddressDto), 90, TimeUnit.DAYS);
            }
        }

    }

    private boolean checkUserWithBusinessLine(Long userId, Byte businessLine) {
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        Byte usertype = null;
        ResponseResult<UserDto> userInfo = userService.getUserInfo(userDto);
        if (userInfo != null) {
            UserDto data = userInfo.getData();
            if (data != null) {
                usertype = data.getBusinessLine();
            }
        }
        boolean b = businessLine.equals(usertype) ? true : false;
        log.info("---result={},businessLine={},userType={}", b, businessLine, usertype);
        return b;
    }
}
