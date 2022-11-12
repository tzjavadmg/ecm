package com.milisong.oms.rest;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.milisong.oms.api.GroupBuyOrderService;
import com.milisong.oms.api.OrderPayService;
import com.milisong.oms.api.VirtualOrderService;
import com.milisong.oms.constant.CancelReason;
import com.milisong.oms.dto.DelayMessageDto;
import com.milisong.oms.param.CancelOrderParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ Description：订单取消延迟队列回调
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/24 20:27
 */
@Slf4j
@RestController
public class DelayQueueCallbackRest {

    @Resource
    private VirtualOrderService virtualOrderService;
    @Resource
    private OrderPayService orderPayService;

    @PostMapping("/v1/order/expired-virtual-order-cancel-pre")
    ResponseResult<?> expiredVirtualOrderCancelPre(@RequestBody DelayMessageDto delayMessageDto) {
        return expiredVirtualOrderCancel(delayMessageDto);
    }

    @PostMapping("/v1/order/expired-virtual-order-cancel")
    ResponseResult<?> expiredVirtualOrderCancel(@RequestBody DelayMessageDto delayMessageDto) {
        log.info("-----------------延迟队列回调,过期取消虚拟订单-----{}", JSON.toJSONString(delayMessageDto));
        try {
            Long orderId = delayMessageDto.getBizId();
            if (orderId == null) {
                return ResponseResult.buildFailResponse("", "订单ID不能为空！");
            }
            virtualOrderService.expiredCancelVirtualOrder(orderId);
        } catch (Exception e) {
            log.error("虚拟订单取消失败", e);
            throw new RuntimeException("取消虚拟订单异常", e);
        }
        return ResponseResult.buildSuccessResponse();
    }

    @PostMapping("/v1/order/expired-order-cancel-pre")
    ResponseResult<?> expiredOrderCancelPre(@RequestBody DelayMessageDto delayMessageDto) {
        return expiredOrderCancel(delayMessageDto);
    }

    @PostMapping("/v1/order/expired-order-cancel")
    ResponseResult<?> expiredOrderCancel(@RequestBody DelayMessageDto delayMessageDto) {
        log.info("-----------------延迟队列回调,过期取消订单-----------{}", JSON.toJSONString(delayMessageDto));
        try {
            Long orderId = delayMessageDto.getBizId();
            if (orderId == null) {
                return ResponseResult.buildFailResponse("", "订单ID不能为空！");
            }
            CancelOrderParam cancelOrderParam = new CancelOrderParam();
            cancelOrderParam.setOrderId(orderId);
            cancelOrderParam.setCancelReason(CancelReason.UNPAID_EXPIRED.getValue());
            orderPayService.expiredCancelOrder(cancelOrderParam);
        } catch (Exception e) {
            log.error("订单取消失败", e);
            return ResponseResult.buildFailResponse();
        }
        return ResponseResult.buildSuccessResponse();
    }
}
