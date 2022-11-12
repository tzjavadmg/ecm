package com.milisong.breakfast.pos.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.milisong.breakfast.pos.api.OrderService;
import com.milisong.breakfast.pos.api.PosProductionService;
import com.milisong.breakfast.pos.dto.ConfigDto;
import com.milisong.breakfast.pos.util.ConfigRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.CollectionUtils;

@Slf4j
@EnableBinding(Sink.class)
public class MqMessageConsumer {

    @Autowired
    private PosProductionService posProductionService;

    @Autowired
    private OrderService orderService;

    /**
     * 集单单消费
     *
     * @param message
     */
    @StreamListener(Sink.ORDER_SET_INPUT)
    public void order(Message<String> message) {
        log.info("开始消费集单信息，参数：{}", JSONObject.toJSONString(message));
        String payload = message.getPayload();
        if (StringUtils.isNotBlank(payload)) {
            // 先入库入队列
            Long shopId = null;
            try {
                shopId = posProductionService.saveOrderSetInfo(payload);
            } catch (Exception e) {
                log.error("集单信息处理异常", e);
                throw e;
            }
            
            // 检查顺序
            boolean flag = posProductionService.checkOrderSetSequence(payload);
            if(!flag) {
            	return;
            }
                        
            // 再校验是否需要进行生产,此处忽略异常
            try {
                posProductionService.notifyProduction(shopId);
            } catch (Exception e) {
                log.error("通知是否需要生产时异常", e);
            }
        } else {
            log.warn("收到集单的空消息！！！");
        }
    }

    /**
     * 配置信息消费
     *
     * @param message
     */
    @StreamListener(Sink.CONFIG_INPUT)
    public void configConsume(@Payload String message) {
        log.info("开始消费属性配置信息，参数：{}", message);
        ConfigDto configDto = JSON.parseObject(message, ConfigDto.class);
        boolean result = ConfigRedisUtil.updateCache(configDto);
        if (!result) {
            log.error("---消费配置信息失败---".concat(message));
        }
    }

    /**
     * 截单配置信息消费
     *
     * @param message
     */
    @StreamListener(Sink.CONFIG_INTERCEPT_INPUT)
    public void configInterceptConsume(@Payload String message) {
        log.info("开始消费截单配置信息，参数：{}", message);
        ConfigDto configDto = JSON.parseObject(message, ConfigDto.class);
        boolean result = ConfigRedisUtil.updateCache(configDto);
        if (!result) {
            log.error("---消费截单配置信息失败---".concat(message));
        }
    }

    /**
     * 集单正式开发生产消费
     *
     * @param message
     */
    @StreamListener(Sink.ORDER_SET_START_PRODUCE_INPUT)
    public void orderSetStartProduce(Message<String> message) {
        log.info("开始集单的正式生产的信息，参数：{}", message);
        String payload = message.getPayload();
        if (StringUtils.isNotBlank(payload)) {
            try {
                this.posProductionService.countdown(payload);
            } catch (Exception e) {
                log.error("集单的正式生产信息处理异常", e);
                throw e;
            }
        } else {
            log.warn("收到集单的正式生产的空消息！！！");
        }
    }

    /**
     * 订单消费
     *
     * @param message
     * @throws Exception
     */
    @StreamListener(Sink.ORDER_INPUT)
    public void orderInfo(Message<String> message) throws Exception {
        log.info("开始消费订单信息，参数：{}", JSONObject.toJSONString(message));
        String payload = message.getPayload();
        if (StringUtils.isNotBlank(payload)) {
            orderInfoV2(payload);
        } else {
            log.warn("收到订单的空消息！！！");
        }
    }

    private void orderInfoV2(String payload) throws Exception {
        try {
            //	List<OrderDto> list = JSONObject.parseArray(payload, OrderDto.class);
            JSONArray list = JSONObject.parseArray(payload);
            if (CollectionUtils.isEmpty(list)) {
                log.warn("订单为空数组！！！");
            } else {
                for (int i = 0; i < list.size(); i++) {
                    JSONObject jsonObject = list.getJSONObject(i);
                    JSONArray jsonArray = jsonObject.getJSONArray("deliveryGoods");
                    if (null == jsonArray || jsonArray.isEmpty()) {
                        log.error("订单：{}未包含明细数据", jsonObject.get("deliveryNo"));
                        throw new RuntimeException("订单数据没有商品数据");
                    }
                }
                this.orderService.saveOrder(list);
            }
        } catch (Exception e) {
            log.error("订单数据转换成list异常", e);
            throw e;
        }
    }

    /**
     * 订单退款消费
     *
     * @param message
     */
    @StreamListener(Sink.ORDER_REFUND_INPUT)
    public void orderRefund(Message<String> message) {
        log.info("开始消费订单退款信息，参数：{}", JSONObject.toJSONString(message));
        String payload = message.getPayload();
        if (StringUtils.isNotBlank(payload)) {
            JSONArray list = JSONObject.parseArray(payload);
            if (CollectionUtils.isEmpty(list)) {
                log.warn("退款信息为空数组！！！");
            } else {
                this.orderService.refoundOrder(list);
            }
        } else {
            log.warn("收到订单退款的空消息！！！");
        }
    }

    /**
     * 订单状态变更消费
     */
//    @StreamListener(Sink.ORDER_STATUS_CHANGE_INPUT)
    public void orderStatusChange(Message<String> message) {
        log.info("开始消费订单状态变更信息。参数：{}", JSON.toJSONString(message));
        String payload = message.getPayload();
        try {
//            orderService.orderStatusChange(payload);
        } catch (Exception e) {
            log.error("更新商品汇总表订单完成时间失败！", e);
        }
    }


}
