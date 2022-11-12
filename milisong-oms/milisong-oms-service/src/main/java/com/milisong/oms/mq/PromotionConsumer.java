package com.milisong.oms.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.milisong.oms.constant.OrderStatus;
import com.milisong.oms.domain.GroupBuyOrder;
import com.milisong.oms.mapper.GroupBuyOrderMapper;
import com.milisong.oms.service.groupbuy.GroupBuyOrderInternalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 拼团状态消息消费
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/20 19:16
 */
@Slf4j
@EnableBinding(MessageSink.class)
public class PromotionConsumer {

    @Resource
    private GroupBuyOrderInternalService groupBuyOrderInternalService;
    @Resource
    private GroupBuyOrderMapper groupBuyOrderMapper;

    @StreamListener(MessageSink.CANCEL_GROUPBUY_INPUT)
    public void accept(Message<String> message) {
        log.info("----------------拼团失败-----------{}", JSON.toJSONString(message));
        String payload = message.getPayload();
        JSONObject jsonObject = JSON.parseObject(payload);
        Long groupBuyId = jsonObject.getLong("userGroupBuyId");

        List<GroupBuyOrder> orderList = groupBuyOrderMapper.findCancelableOrderListByGroupBuyId(groupBuyId);
        log.info("-----------退款订单：{}", JSON.toJSONString(orderList));
        orderList.forEach(order -> {
            if (OrderStatus.PAID.getValue() == order.getStatus()) {
                log.warn("----------拼团失败，已支付订单退款----{}", JSON.toJSONString(order));
                groupBuyOrderInternalService.refundOrder(order);
            } else if (OrderStatus.UN_PAID.getValue() == order.getStatus()) {
                groupBuyOrderInternalService.expiredCancelOrder(order.getId());
                log.warn("----------拼团失败，未支付订单取消----{}", JSON.toJSONString(order));
            } else {
                log.warn("----------拼团失败，订单状态异常----{}", JSON.toJSONString(order));
            }
        });
    }
}
