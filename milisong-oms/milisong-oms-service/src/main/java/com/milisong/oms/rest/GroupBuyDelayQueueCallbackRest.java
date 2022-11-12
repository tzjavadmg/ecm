package com.milisong.oms.rest;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.milisong.oms.api.GroupBuyOrderService;
import com.milisong.oms.dto.DelayMessageDto;
import com.milisong.oms.service.groupbuy.GroupBuyOrderInternalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/21 10:19
 */
@Slf4j
@RestController
public class GroupBuyDelayQueueCallbackRest {

    @Resource
    private GroupBuyOrderInternalService groupBuyOrderInternalService;

    @PostMapping("/v1/order/expired-group-buy-order-pre")
    ResponseResult<?> expiredGroupBuyPre(@RequestBody DelayMessageDto delayMessageDto) {
        return expiredGroupBuyOrder(delayMessageDto);
    }

    @PostMapping("/v1/order/expired-group-buy-order")
    ResponseResult<?> expiredGroupBuyOrder(@RequestBody DelayMessageDto delayMessageDto) {
        log.info("-----------------延迟队列回调,过期取消未支付拼团订单-----{}", JSON.toJSONString(delayMessageDto));
        try {
            Long orderId = delayMessageDto.getBizId();
            if (orderId == null) {
                return ResponseResult.buildFailResponse("", "拼团订单ID不能为空！");
            }
            groupBuyOrderInternalService.expiredCancelOrder(orderId);
        } catch (Exception e) {
            log.error("虚拟订单取消失败", e);
            throw new RuntimeException("取消虚拟订单异常", e);
        }
        return ResponseResult.buildSuccessResponse();
    }

}
