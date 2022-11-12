package com.milisong.oms.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.oms.dto.OrderDto;
import com.milisong.oms.dto.WechatMiniPayDto;
import com.milisong.oms.param.GroupBuyOrderParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description: 虚拟拼团订单服务
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/20 14:19
 */
@FeignClient("milisong-oms-service")
public interface GroupBuyOrderService {

    @PostMapping(value = "/v1/GroupBuyOrderService/order")
    ResponseResult<WechatMiniPayDto> order(@RequestBody GroupBuyOrderParam groupBuyOrderParam);

    @PostMapping(value = "/v1/GroupBuyOrderService/getOrderById")
    ResponseResult<OrderDto> getOrderById(@RequestParam("orderId") Long orderId);


    @PostMapping(value = "/v1/GroupBuyOrderService/cancelOrder")
    ResponseResult<?> cancelOrder(@RequestParam("orderId") Long orderId);

}
