package com.milisong.oms.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.oms.param.VirtualOrderParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 虚拟订单服务
 * 虚拟订单用来锁库存
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/1 10:57
 */
@FeignClient("milisong-oms-service")
public interface VirtualOrderService {

    /**
     * 创建虚拟订单
     *
     * @param virtualOrderParam 虚拟订单信息
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/VirtualOrderService/createVirtualOrder")
    ResponseResult<?> createVirtualOrder(@RequestBody VirtualOrderParam virtualOrderParam);

    /**
     * 用户离开结算页面时取消
     *
     * @param orderId 订单ID
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/VirtualOrderService/userCancelVirtualOrder")
    ResponseResult<?> userCancelVirtualOrder(@RequestParam("orderId") Long orderId);

    /**
     * 取消虚拟订单，释放锁定库存
     * 5分钟未支付超时自动取消
     *
     * @param orderId 订单ID
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/VirtualOrderService/expiredCancelVirtualOrder")
    ResponseResult<?> expiredCancelVirtualOrder(@RequestParam("orderId") Long orderId);

    /**
     * 取消待生成虚拟订单(定时任务)
     *
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/VirtualOrderService/cancelUnPayVirtualOrder")
    ResponseResult<?> cancelUnPayVirtualOrder();
}
