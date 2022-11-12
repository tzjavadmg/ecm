package com.milisong.oms.api;

import com.farmland.core.api.PageParam;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.oms.dto.*;
import com.milisong.oms.param.PaymentWaterParam;
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
 * @date 2018/10/1 10:58
 */
@FeignClient("milisong-oms-service")
public interface OrderService {

    /**
     * 记录支付流水
     *
     * @param paymentWaterParam 支付流水
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/OrderService/paymentWater")
    ResponseResult<?> paymentWater(@RequestBody PaymentWaterParam paymentWaterParam);

    /**
     * 根订单ID查询订单详细信息
     *
     * @param orderId 订单ID
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/OrderService/getOrderById")
    ResponseResult<OrderDto> getOrderById(@RequestParam("orderId") Long orderId);

    /**
     * 根订单ID查询订单简单详情
     *
     * @param orderId 订单ID
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/OrderService/getSimpleOrderById")
    ResponseResult<OrderDto> getSimpleOrderById(@RequestParam("orderId") Long orderId);

    /**
     * 根订单ID查询订单
     *
     * @param orderNo 订单No
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/OrderService/getOrderByOrderNo")
    ResponseResult<OrderDto> getOrderByOrderNo(@RequestParam("orderNo") String orderNo);

    /**
     * 获取指定门店，某天需要配送的订单
     *
     * @param shopCode     门店编码
     * @param deliveryDateTimestamp 配送日期
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/OrderService/getOrderListByDeliveryDate")
    ResponseResult<List<OrderDeliveryDto>> getOrderListByDeliveryDate(@RequestParam("businessLine") Byte businessLine,@RequestParam("shopCode") String shopCode, @RequestParam("deliveryDateTimestamp") long deliveryDateTimestamp);

    /**
     * 查询已经完成的子单
     *
     * @param deliveryDateTimestamp 配送日期
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/OrderService/getCompletedDeliveryListByDeliveryDate")
    ResponseResult<List<OrderDeliveryDto>> getCompletedDeliveryListByDeliveryDate(@RequestParam("deliveryDateTimestamp") long deliveryDateTimestamp,@RequestParam("orderType")byte orderType);

    /**
     * 根据配送子单ID查询订单信息
     * 用于用户评价反馈时
     *
     * @param deliveryId 配送记录ID
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/OrderService/getOrderByDeliveryId")
    ResponseResult<OrderDto> getOrderByDeliveryId(@RequestParam("deliveryId") Long deliveryId);


    /**
     * 查询我的订单信息
     *
     * @param pageParam 分页对象
     * @return 定单集合
     */
    @PostMapping(value = "/v1/OrderService/getOrderList")
    ResponseResult<Pagination<MyOrderDto>> getOrderList(@RequestParam("businessLine") Byte businessLine,@RequestBody PageParam pageParam, @RequestParam("userId") Long userId);


    /**
     * 查询我的订单详情
     *
     * @param orderId 订单编号
     * @return ResponseResult
     */
    @PostMapping(value = "/v1/OrderService/getOrderDetailsList")
    ResponseResult<MyOrderDto> getOrderDetailsList(@RequestParam("orderId") Long orderId, @RequestParam("userId") Long userId,@RequestParam("orderEndTime") Integer orderEndTime);


    /**
     * 根据配送单号更新时间及状态
     *
     * @param deliveryMqDto 状态
     */
    @PostMapping(value = "/v1/OrderService/updateDeliveryByOrderNo")
    void updateDeliveryByOrderNo(@RequestBody OrderDeliveryMqDto deliveryMqDto);

    /**
     * 取消待支付订单(定时任务)
     */
    @PostMapping(value = "/v1/OrderService/cancelToPayOrder")
    ResponseResult<?> cancelToPayOrder();

    /**
     * 查询待支付订单(定时任务补偿)
     *
     * @return
     */
    @PostMapping(value = "/v1/OrderService/getUnPayOrder")
    ResponseResult<?> getUnPayOrder();

    /**
     * 我的订单计划
     *
     * @return
     */
    @PostMapping(value = "/v1/OrderService/getOrderPlanList")
    ResponseResult<List<OrderPlanDeliveryDto>> getOrderPlanList(@RequestParam("userId") Long userId,@RequestParam("orderEndTime") Integer orderEndTime);


    /**
     * 我的订单计划
     *
     * @return
     */
    @PostMapping(value = "/v2/OrderService/getOrderPlanList")
    ResponseResult<List<OrderPlanDeliveryDto>> getOrderPlanList(@RequestParam("userId") Long userId,@RequestParam("orderEndTime") Integer orderEndTime,@RequestParam("businessLine") Byte businessLine);

    /**
     * 获取骑手信息及配送状态流水
     *
     * @param deliveryNo
     * @return
     */
    @PostMapping(value = "/v1/OrderService/getRifderInfo")
    ResponseResult<RiderInfoDto> getRifderInfo(@RequestParam("deliveryNo") String deliveryNo, @RequestParam("orderId") Long orderId,@RequestParam("businessLine") Byte businessLine);

    /**
     * 根据业务线及配送时间查询配送单号集合
     * @param businessLine
     * @param deliveryDate
     * @return
     */
    @PostMapping(value = "/v1/OrderService/getDeliveryNos")
    ResponseResult<List<String>> getDeliveryNos(@RequestParam("businessLine") Byte businessLine,@RequestParam("deliveryDate") String deliveryDate);

    
    /**
     * 根据配送单号查询订单及配送单信息
     * @param deliveryNo
     * @return
     */
    @PostMapping(value = "/v1/OrderService/getOrderByDeliveryNo")
    ResponseResult<OrderDto> getOrderByDeliveryNo(@RequestParam("deliveryNo") String deliveryNo);


    /**
     * 根据配送单号查询订单及配送单信息
     * @param deliveryId
     * @return
     */
    @PostMapping(value = "/v1/OrderService/getGoodsByDeliveryId")
    ResponseResult<List<OrderDeliveryGoodsDto>> getGoodsByDeliveryId(@RequestParam("deliveryId") Long deliveryId);

}
