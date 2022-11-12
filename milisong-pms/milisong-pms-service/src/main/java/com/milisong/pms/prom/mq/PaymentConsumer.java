package com.milisong.pms.prom.mq;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.oms.api.GroupBuyOrderService;
import com.milisong.oms.api.OrderService;
import com.milisong.oms.constant.OrderMode;
import com.milisong.oms.constant.OrderStatus;
import com.milisong.oms.constant.PaymentStatus;
import com.milisong.oms.dto.OrderDto;
import com.milisong.pms.prom.annotation.RecordMQ;
import com.milisong.pms.prom.api.BreakfastCouponService;
import com.milisong.pms.prom.api.UserRedPacketService;
import com.milisong.pms.prom.domain.ActivityUserInvite;
import com.milisong.pms.prom.domain.UserInviteRecord;
import com.milisong.pms.prom.dto.UserCouponQueryParam;
import com.milisong.pms.prom.enums.MQRecordType;
import com.milisong.pms.prom.enums.UserInviteEnum;
import com.milisong.pms.prom.mapper.ActivityUserInviteMapper;
import com.milisong.pms.prom.mapper.UserInviteRecordMapper;
import com.milisong.ucs.enums.BusinessLineEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

import javax.annotation.Resource;
import java.util.Date;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/25 14:18
 */
@Slf4j
@EnableBinding(MessageSink.class)
@MessageEndpoint(value = "promotionPaymentConsumer")
public class PaymentConsumer {
    @Resource
    private UserRedPacketService userRedPacketService;

    @Autowired
    BreakfastCouponService breakfastCouponService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserInviteRecordMapper recordMapper;


    @RecordMQ(MQRecordType.PAY_MENT)
    @StreamListener(MessageSink.MESSAGE_PAYMENT_INTPUT)
    public void accept(Message<String> message) {
        log.info("促销系统==========接收支付MQ消息，参数={}", JSONObject.toJSONString(message));
        String jsonString = message.getPayload();
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        Byte buzLine = jsonObject.getByte("orderType");
        Long orderId = jsonObject.getLong("orderId");
        Byte orderMode = jsonObject.getByte("orderMode");

        log.info("MQ消息 buzLine -> {}, orderId -> {}",buzLine,orderId);


        if (BusinessLineEnum.BREAKFAST.getCode().equals(buzLine)){
            Long couponId = jsonObject.getLong("couponId");
            log.info("MQ消息couponId -> {}",couponId);
            if (orderId != null && couponId != null){
                UserCouponQueryParam queryParam = new UserCouponQueryParam();
                queryParam.setOrderId(orderId);
                queryParam.setUserCouponId(couponId);
                breakfastCouponService.useUserCoupon(queryParam);
            }
            if(orderId!= null){
                //更新邀新记录表信息-早餐
                afterPayUpdateInviteRecord(orderId,orderMode);
            }
        }else{
            Long redPacketId = jsonObject.getLong("redPacketId");
            log.info("MQ消息redPacketId -> {}",redPacketId);
            if (redPacketId != null && orderId != null){
                userRedPacketService.useUserRedPacket(redPacketId, orderId);
            }
        }

    }

    /**
     * 判断是否需要是被邀请者,如果是，检查邀新记录状态，如果是未下单状态，就更新为已下单已支付
     * @param orderId
     */
    private void afterPayUpdateInviteRecord(Long orderId,Byte orderMode){
        //拼团订单不发通知
        if (orderMode != null && OrderMode.GROUP_BUY.getValue() == orderMode) {
            return;
        }
        //TODO
        //1.查询订单详情
        ResponseResult<OrderDto> dtoResponseResult = orderService.getOrderById(orderId);

        if(!dtoResponseResult.isSuccess()|| dtoResponseResult.getData() == null){
            return;
        }
        OrderDto orderDto = dtoResponseResult.getData();
        //2.校验支付状态
        Byte status = orderDto.getStatus();
        if(status == null ||!status.equals(OrderStatus.PAID.getValue())){
           return;
        }
        //3.查询此用户的邀新记录信息
        Long userId = orderDto.getUserId();
        UserInviteRecord userInviteRecord = recordMapper.queryByReceiveUserId(userId);
        if(userInviteRecord == null){
            return;
        }
        //4.判断用户的邀新信息状态
        Byte inviteStatus = userInviteRecord.getStatus();
        //只有记录信息是未下单状态时，才执行更新，其他状态都不处理
        if(inviteStatus.equals(UserInviteEnum.INVITE_RECORD_STATUS.NOT_PLACE_ORDER.getValue())){
            //5.查询邀新者主体信息是否已失效,暂时不做

            //6.更新状态
            UserInviteRecord updateDto = new UserInviteRecord();
            updateDto.setId(userInviteRecord.getId());
            updateDto.setStatus(UserInviteEnum.INVITE_RECORD_STATUS.ON_DELIVERY.getValue());
            updateDto.setRemark(UserInviteEnum.INVITE_RECORD_REMARK.ON_DELIVERY.getDesc());
            recordMapper.updateByPrimaryKeySelective(updateDto);
            log.info("---更新邀新记录表数据状态成功old-{},new-{}",inviteStatus,updateDto.getStatus());
        }
    }

}
