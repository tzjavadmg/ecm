package com.milisong.pms.prom.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.oms.api.OrderService;
import com.milisong.oms.constant.DeliveryStatus;
import com.milisong.oms.dto.OrderDeliveryDto;
import com.milisong.oms.dto.OrderDto;
import com.milisong.pms.prom.annotation.RecordMQ;
import com.milisong.pms.prom.api.UserPointService;
import com.milisong.pms.prom.dto.UserPointWaterDto;
import com.milisong.pms.prom.enums.MQRecordType;
import com.milisong.pms.prom.enums.UserPointEnum;
import com.milisong.pms.prom.properties.PmsProperties;
import com.milisong.pms.prom.util.UserPointUtil;
import com.milisong.ucs.enums.BusinessLineEnum;
import com.sun.javafx.binding.StringFormatter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.messaging.Message;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 消费配置信息
 *
 * @author tianhaibo
 * @version 1.0.0
 * @date 2018/12/19 14:18
 */
@Slf4j
@EnableBinding(MessageSink.class)
@MessageEndpoint(value = "promotionRefundOrderConsumer")
public class RefundOrderConsumer {

    @Autowired
    private UserPointService userPointService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PmsProperties pmsProperties;

    @RecordMQ(MQRecordType.POINT)
    @StreamListener(MessageSink.MESSAGE_REFUND_INPUT)
    public void accept(Message<String> message) {
        log.info("促销系统==========接收退款MQ消息，参数={}", JSONObject.toJSONString(message));
        String msg = message.getPayload();
        List<RefundMessage> refundMessages = JSON.parseArray(msg, RefundMessage.class);
        if(refundMessages == null || refundMessages.size() == 0){
            return;
        }
        if(!refundMessages.get(0).getOrderType().equals(BusinessLineEnum.BREAKFAST.getCode())){
            log.info("---不是早餐，不处理积分，不处理邀新业务---");
            return;
        }
        ResponseResult<OrderDto> responseResult = orderService.getOrderByDeliveryNo(refundMessages.get(0).getDeliveryNo());
        OrderDto data = responseResult.getData();
        //处理积分业务
        afterRefundSendPoint(refundMessages,data);
    }

    /**
     * 退款后，处理积分业务数据
     * @param refundMessages
     * @param data
     */
    private void afterRefundSendPoint(List<RefundMessage> refundMessages,OrderDto data) {
        for (RefundMessage refundMessage:refundMessages) {
            UserPointWaterDto userPointWaterDto = UserPointWaterDto.builder()
                    .action(UserPointEnum.Action.REFUND_ORDER.getCode())
                    .businessLine(refundMessage.getOrderType())
                    .incomeType(UserPointEnum.IncomeType.IN.getCode())
                    .point(refundMessage.getTotalRefundPoints())
                    .refId(refundMessage.getDeliveryId())
                    .refType(UserPointEnum.RefType.SUB_ORDER.getCode())
                    .userId(refundMessage.getUserId())
                    .remark(UserPointUtil.POINT_REFUND_OR_CANCLE)
                    .build();
            userPointService.saveUserPointWater(userPointWaterDto);
        }
        //检查订单是否全部完成，来进行送积分
        if(data == null ){
            log.error("----接收订单状态变更MQ消息--没有查询到订单详细信息--");
            return;
        }
        dealSendPoint(data,refundMessages);
    }

    private void dealSendPoint(OrderDto data, List<RefundMessage> refundMessages) {
        //关闭赠送积分
        Boolean userPointOpenStatus = pmsProperties.getUserPointSwitch();
        if(!userPointOpenStatus){
            return;
        }
        log.info("----查询订单数据---{}", JSONObject.toJSONString(data));
        List<OrderDeliveryDto> deliveries = data.getDeliveries();
        if(deliveries == null || deliveries.size() == 0){
            return;
        }
        List<String> list = new ArrayList<>();
        refundMessages.stream().forEach(o->list.add(o.getDeliveryNo()));
        for (OrderDeliveryDto o:deliveries) {
            if(list.contains(o.getDeliveryNo())){
                continue;
            }
            if(!o.getStatus().equals(DeliveryStatus.CANCELED.getValue())&&
                    !o.getStatus().equals(DeliveryStatus.REFUNDED.getValue())&&
                    !o.getStatus().equals(DeliveryStatus.COMPLETED.getValue())){
                return;
            }
        }
        //计算应得积分
        BigDecimal sumPointAmount = BigDecimal.ZERO;
        for (OrderDeliveryDto o:deliveries){
            if(o.getStatus().equals(DeliveryStatus.COMPLETED.getValue())){
                sumPointAmount = sumPointAmount.add(o.getShareOrderPayAmount());
                if(o.getDeliveryActualPrice()!=null){
                    sumPointAmount = sumPointAmount.subtract(o.getDeliveryActualPrice());
                }
                if(o.getTotalPackageActualAmount()!=null){
                    sumPointAmount = sumPointAmount.subtract(o.getTotalPackageActualAmount());
                }
            }
        }
        log.info("----计算总金额---{}",sumPointAmount);
        BigDecimal amount = data.getTotalPayAmount();
        if(sumPointAmount.compareTo(amount)>0){
            log.error("计算订单积分异常，子单金额总和大于主单金额-主单-"+data.getId()+",主单金额-"+amount+",子单总金额-"+sumPointAmount);
            return;
        }
        UserPointWaterDto userPointWaterDto = UserPointWaterDto.builder()
                .action(UserPointEnum.Action.FINISH_ORDER.getCode())
                .businessLine(data.getOrderType())
                .incomeType(UserPointEnum.IncomeType.IN.getCode())
                .point(UserPointUtil.money2Point(sumPointAmount))
                .refId(data.getId())
                .refType(UserPointEnum.RefType.ORDER.getCode())
                .userId(data.getUserId())
                .remark(StringFormatter.format(UserPointUtil.POINT_COMPLATE,sumPointAmount).getValue())
                .build();
        userPointService.saveUserPointWater(userPointWaterDto);
        return;
    }

    @Data
    public static class RefundMessage {

        private Long deliveryId;

        private String deliveryNo;

        private Date deliveryDate;

        private String shopCode;

        private Byte orderType;

        private Long userId;

        private Integer totalRefundPoints;

    }
}

