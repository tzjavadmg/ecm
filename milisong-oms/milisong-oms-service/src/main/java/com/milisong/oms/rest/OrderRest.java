package com.milisong.oms.rest;

import com.alibaba.fastjson.JSON;
import com.milisong.oms.configruation.SystemProperties;
import com.milisong.oms.domain.Order;
import com.milisong.oms.mapper.OrderMapper;
import com.milisong.oms.service.PaymentMqProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ Description：订单对外接口
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/25 15:01
 */
@Slf4j
@RestController
public class OrderRest {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private PaymentMqProcessor paymentMqProcessor;
    @Resource
    private SystemProperties systemProperties;

    @RequestMapping(value = "/v1/order/repair-order-mq", method = RequestMethod.POST)
    public void repairOrderMq(@RequestBody List<String> orderNoList) {
        log.info("------------MQ补偿入参：{}", JSON.toJSONString(orderNoList));
        List<Order> orderList = orderMapper.selectByOrderNoList(orderNoList);
        log.info("------------需要补偿的订单，{}", JSON.toJSONString(orderNoList));
        if (CollectionUtils.isNotEmpty(orderList)) {
            orderList.forEach(order -> paymentMqProcessor.sendMq2Scm(order));
        }
    }

    @RequestMapping(value = "/v1/order/sys-prop", method = RequestMethod.GET)
    public void sysProp() {
       log.info("---------------本地缓存刷新时间：{}",systemProperties.getLocalCache().getExpireTime());
       log.info("---------------延迟队列系统url：{}",systemProperties.getDelayQueue().getBaseUrl());
    }
}
