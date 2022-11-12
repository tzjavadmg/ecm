package com.milisong.ecm.common.notify.mq;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.common.notify.api.NotifyService;
import com.milisong.ecm.common.notify.dto.NotifyArriveDto;
import com.milisong.ecm.common.notify.dto.NotifyIvrResponseDto;
import com.milisong.ecm.common.notify.dto.WechatPayNotifyDto;
import com.milisong.oms.constant.OrderMode;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyWechatMsgMessage;
import com.milisong.pms.prom.enums.GrouyBuyEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.handler.annotation.Payload;

@Slf4j
@EnableBinding(MessageProcessor.class)
@MessageEndpoint(value = "notifyMessageConsumer")
public class MessageConsumer {

    @Autowired
    private NotifyService notifyService;


    @StreamListener(MessageProcessor.MESSAGE_IVR_INTPUT)
    public void acceptIvr(@Payload String message) {
        log.info("通知系统===========接收IVR的MQ消息，参数={}", JSONObject.toJSONString(message));
        try {
            JSONObject jsonObject = JSONObject.parseObject(message);
            //reason,success,mobile,data
            Boolean success = jsonObject.getBoolean("success");
            String mobile = jsonObject.getString("mobile");
            String uuid = jsonObject.getString("uuid");
            NotifyIvrResponseDto dto = new NotifyIvrResponseDto();
            dto.setUuid(uuid);
            dto.setMobile(mobile);
            dto.setSuccess(success);
            notifyService.updateIvrResult(dto);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("接收IVR的消息出现异常->" + message, e);
        }
    }

    @StreamListener(MessageProcessor.MESSAGE_NOTIFY_PAYMENT_INPUT)
    public void acceptPayment(@Payload String message) {
        log.info("通知系统===========接收支付的回调MQ消息，参数={}", JSONObject.toJSONString(message));
        try {
            JSONObject jsonObject = JSONObject.parseObject(message);
            Long orderId = jsonObject.getLong("orderId");
            String shopCode = jsonObject.getString("shopCode");
            String openId = jsonObject.getString("openId");
            Byte status = jsonObject.getByte("status");

            //拼团订单不发通知
            Byte orderMode = jsonObject.getByte("orderMode");
            if (OrderMode.GROUP_BUY.getValue() == orderMode) {
                return;
            }

            WechatPayNotifyDto notifyDto = new WechatPayNotifyDto();
            notifyDto.setOpenId(openId);
            notifyDto.setOrderId(orderId);
            notifyDto.setShopCode(shopCode);
            notifyDto.setStatus(status);
            notifyService.sendWeiXinPayMsg(notifyDto);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("接收支付的回调MQ消息用于发通知异常->" + message, e);
        }
    }

    /**
     * @param message
     */
    @StreamListener(MessageProcessor.MESSAGE_NOTIFY_ARRIVE_INPUT)
    public void acceptNotifyArrive(@Payload String message) {
        log.info("通知系统===========接收外卖送达MQ消息，参数={}", JSONObject.toJSONString(message));
        try {
            NotifyArriveDto dto = JSONObject.parseObject(message, NotifyArriveDto.class);
            if (dto.getOrderType() == null) {
                log.error("----外卖送达MQ缺少订单类型字段--{}", message);
                return;
            }
            ResponseResult<Boolean> result = notifyService.notifyCustomer(dto);
            if (!result.isSuccess()) {
                log.info("------通知用户取餐失败----{}", JSON.toJSONString(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("接收外卖送达MQ消息用于发通知异常->" + message, e);
        }
    }

    /**
     * 拼团通知
     *
     * @param message
     */
    @StreamListener(MessageProcessor.WECHATMSG_GROUPBUY_INPUT)
    public void wechatmsgGroupbuy(@Payload String message) {
        log.info("拼团通知 入参：{}", JSONObject.toJSONString(message));
        try {
            GroupBuyWechatMsgMessage dto = JSONObject.parseObject(message, GroupBuyWechatMsgMessage.class);
            //根据场景类型选择对应的通知
            //【发团 & 参团成功】：拼团进度通知
            if (GrouyBuyEnum.SEND_MSG_SCENE.CREATE_GROUPBUY_NOTIFY == dto.getScene()) {
                notifyService.sendWeiXinJoinGroupBuy(dto);
            } else if (GrouyBuyEnum.SEND_MSG_SCENE.TIMEOUT_NOTIFY == dto.getScene()) {
                notifyService.sendWeiXinJoinGroupBuyFailed(dto);
            } else if (GrouyBuyEnum.SEND_MSG_SCENE.LEFT_TIME_NOTIFY == dto.getScene()) {
                notifyService.sendWeiXinJoinGroupBuyTimeRemind(dto);
            } else if (GrouyBuyEnum.SEND_MSG_SCENE.PROCESS_NOTIFY == dto.getScene()) {
                notifyService.sendWeiXinJoinGroupBuySchedule(dto);
            } else if (GrouyBuyEnum.SEND_MSG_SCENE.COMPLETE_NOTIFY == dto.getScene()) {
                notifyService.sendWeiXinJoinGroupBuySuccess(dto);
            }
        } catch (Exception e) {
            log.error("拼团通知异常->", e);
        }
    }

}
