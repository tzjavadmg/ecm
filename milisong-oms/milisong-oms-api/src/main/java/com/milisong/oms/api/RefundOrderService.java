package com.milisong.oms.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.oms.dto.RefundOrderDetailDto;
import com.milisong.oms.param.RefundOrderParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.SortedMap;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/8 16:51
 */
@FeignClient("milisong-oms-service")
public interface RefundOrderService {
    /**
     * 退款
     *
     * @param refundOrderParam
     * @return
     */
    @PostMapping(value = "/v1/RefundOrderService/refund")
    ResponseResult<?> refund(@RequestBody RefundOrderParam refundOrderParam);

    /**
     * 查询退款进度
     * @param deliveryId
     * @return
     */
    @PostMapping(value = "/v1/RefundOrderService/getRefundOrderDetail")
    ResponseResult<RefundOrderDetailDto> getRefundOrderDetail(@RequestParam("deliveryId") Long deliveryId);

    /**
     * 微信退款回调
     *
     * @param map
     */
    @PostMapping(value = "/v1/RefundOrderService/refundCallback")
    void refundCallback(@RequestBody SortedMap<String, String> map);

    /**
     * 查询退款中订单(定时任务补偿)
     * @return
     */
    @PostMapping(value = "/v1/RefundOrderService/getRefundingOrder")
    ResponseResult<?> getRefundingOrder();
}
