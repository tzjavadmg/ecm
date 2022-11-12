package com.milisong.oms.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.oms.dto.OrderDeliveryDto;
import com.milisong.oms.dto.OrderDto;
import com.milisong.oms.dto.WechatMiniPayDto;
import com.milisong.oms.param.CancelOrderParam;
import com.milisong.oms.param.DeliveryTimezoneParam;
import com.milisong.oms.param.OrderPaymentParam;
import com.milisong.oms.param.PaymentResultParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/7 16:44
 */
@FeignClient("milisong-oms-service")
public interface OrderPayService {

    /**
     * 创建并支付订单
     *
     * @param orderPaymentParam 支付参数
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/OrderPayService/payOrder")
    ResponseResult<WechatMiniPayDto> payOrder(@RequestBody OrderPaymentParam orderPaymentParam);

    /**
     * 支付结果更新
     *
     * @param paymentResultParam 支付结果
     */
    @PostMapping(value = "/v1/OrderPayService/paymentResult")
    ResponseResult<?> paymentResult(@RequestBody PaymentResultParam paymentResultParam);

    /**
     * 用户主动取消订单，释放锁定库存
     *
     * @param cancelOrderParam 订单ID
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/OrderPayService/userCancelOrder")
    ResponseResult<?> userCancelOrder(@RequestBody CancelOrderParam cancelOrderParam);

    /**
     * 取消订单，释放锁定库存
     * 5分钟未支付超时自动取消
     *
     * @param cancelOrderParam 订单ID
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/OrderPayService/expiredCancelOrder")
    ResponseResult<?> expiredCancelOrder(@RequestBody CancelOrderParam cancelOrderParam);

    /**
     * 更新配送表的配送时间
     *
     * @param deliveryTimezoneParam 配送时区参数
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/OrderPayService/updateDeliveryTimezone")
    ResponseResult<?> updateDeliveryTimezone(@RequestBody DeliveryTimezoneParam deliveryTimezoneParam);

    /**
     * 查找可退款的配送子单
     *
     * @param orderId 订单ID
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/OrderPayService/findDeliveries4RefundByOrderId")
    ResponseResult<List<OrderDeliveryDto>> findDeliveries4RefundByOrderId(@RequestParam("orderId") Long orderId);

    /**
     * 根订单ID查询订单详细信息
     *
     * @param orderId 订单ID
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/OrderPayService/getOrderById")
    ResponseResult<OrderDto> getOrderById(@RequestParam("orderId") Long orderId);
}
