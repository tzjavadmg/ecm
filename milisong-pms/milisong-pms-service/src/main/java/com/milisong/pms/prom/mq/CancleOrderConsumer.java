package com.milisong.pms.prom.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.milisong.pms.prom.annotation.RecordMQ;
import com.milisong.pms.prom.api.UserPointService;
import com.milisong.pms.prom.dto.UserPointWaterDto;
import com.milisong.pms.prom.enums.MQRecordType;
import com.milisong.pms.prom.enums.UserPointEnum;
import com.milisong.pms.prom.util.UserPointUtil;
import com.milisong.ucs.enums.BusinessLineEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@MessageEndpoint(value = "promotionCancleOrderConsumer")
public class CancleOrderConsumer {

    @Autowired
    private UserPointService userPointService;

    @RecordMQ(MQRecordType.POINT)
    @StreamListener(MessageSink.MESSAGE_CANCLE_INPUT)
    public void accept(Message<String> message) {
        log.info("促销系统==========接收取消订单MQ消息 ，处理积分，参数={}", JSONObject.toJSONString(message));
        String msg = message.getPayload();
        CancelMessage cancelMessage = JSON.parseObject(msg, CancelMessage.class);
        if(!cancelMessage.getOrderType().equals(BusinessLineEnum.BREAKFAST.getCode())){
            log.info("---不是早餐，不处理积分---");
            return;
        }
        UserPointWaterDto userPointWaterDto = UserPointWaterDto.builder()
                .action(UserPointEnum.Action.CANCLE_ORDER.getCode())
                .businessLine(cancelMessage.getOrderType())
                .incomeType(UserPointEnum.IncomeType.IN.getCode())
                .point(cancelMessage.getDeductionPoints())
                .refId(cancelMessage.getOrderId())
                .refType(UserPointEnum.RefType.ORDER.getCode())
                .userId(cancelMessage.getUserId())
                .remark(UserPointUtil.POINT_REFUND_OR_CANCLE)
                .build();
        userPointService.saveUserPointWater(userPointWaterDto);
    }

    @Getter
    @Setter
    public static class CancelMessage {

        /**
         * 订单ID
         */
        private Long orderId;

        /**
         * 门店编码
         */
        private String shopCode;

        /**
         * 订单类型
         */
        private Byte orderType;

        /**
         * 订单使用抵扣积分
         */
        private Integer deductionPoints;

        private Long userId;
    }
}
