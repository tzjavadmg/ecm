package com.milisong.ecm.lunch.web.order;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.PageParam;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.common.api.ShopConfigService;
import com.milisong.ecm.common.notify.api.NotifyService;
import com.milisong.ecm.common.notify.constant.BusinessLineEnum;
import com.milisong.ecm.common.notify.dto.WechatPayNotifyDto;
import com.milisong.ecm.lunch.service.RestOrderServiceLC;
import com.milisong.oms.api.OrderService;
import com.milisong.oms.api.RefundOrderService;
import com.milisong.oms.constant.OrderType;
import com.milisong.oms.dto.*;
import com.milisong.oms.param.PaymentWaterParam;
import com.milisong.ucs.sdk.security.UserContext;
import com.milisong.ucs.sdk.security.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/2 16:42
 */
@Slf4j
@RestController
@RequestMapping("lc")
public class OrderRestLC {
    @Resource
    private OrderService orderService;
    @Resource
    private RefundOrderService refundOrderService;
    @Resource
    private RestOrderServiceLC restOrderServiceLC;
    @Resource
    private NotifyService notifyService;
    @Resource
    private ShopConfigService shopConfigService;



    @GetMapping("/v1/order/simple-order")
    ResponseResult<OrderDto> getSimpleOrder(@RequestParam(value = "orderId", required = true) Long orderId) {
        return orderService.getSimpleOrderById(orderId);
    }

    @PostMapping("/v2/order/order-list")
    ResponseResult<Pagination<MyOrderDto>> orderList(@RequestBody PageParam pageParam) {
        try {
        	UserSession session = UserContext.getCurrentUser();
        	long userId = session.getId();
            Byte businessLine = OrderType.LUNCH.getValue();
            return orderService.getOrderList(businessLine,pageParam,userId);
        } catch (Exception e) {
            log.error("查询订单失败", e);
            throw new RuntimeException("我的订单异常", e);
        }
    }

    @GetMapping("/v2/order/order-details-list")
    ResponseResult<MyOrderDto> orderDetaisList(@RequestParam(value = "orderId", required = true) Long orderId) {
        try {
        	UserSession session = UserContext.getCurrentUser();
        	long userId = session.getId();
        	Integer orderEndTime = shopConfigService.getOrderEndTime();
            return orderService.getOrderDetailsList(orderId,userId,orderEndTime);
        } catch (Exception e) {
            log.error("查询订单详情失败", e);
            throw new RuntimeException("我的订单详情异常", e);
        }
    }

    @PostMapping(value = "/v2/order/unpay-notify")
    public ResponseResult<Boolean> notifyWaitPay(@RequestBody Map<String, String> params) {
        log.info("取消支付订单入参：{}", JSON.toJSONString(params));
        Long orderId = MapUtils.getLong(params, "orderId");
        WechatPayNotifyDto dto = new WechatPayNotifyDto();
        dto.setOrderId(orderId);
        dto.setBusinessLine(BusinessLineEnum.LUNCH.getVal());
        notifyService.notifyWaitPay(dto);
        return ResponseResult.buildSuccessResponse();
    }

    @PostMapping(value = "/v1/order/payment-water")
    public ResponseResult<?> paymentWater(@RequestBody PaymentWaterParam paymentWaterParam) {
        log.info("取消支付订单入参：{}", JSON.toJSONString(paymentWaterParam));
        return orderService.paymentWater(paymentWaterParam);
    }

    @GetMapping("/v2/order/refundorder-details")
    ResponseResult<RefundOrderDetailDto> refundOrderDetails(@RequestParam(value = "deliveryId", required = true) Long deliveryId) {
        log.info("查询退款单详情，配送单ID：{}", deliveryId);
        return refundOrderService.getRefundOrderDetail(deliveryId);
    }

    @GetMapping("/v2/order/download-bill/{env}/{billDate}")
    ResponseEntity<byte[]> downloadBill(@PathVariable("env") String env, @PathVariable("billDate") String billDate) {
        return restOrderServiceLC.downloadBill(env, billDate);
    }
    
    @GetMapping(value = "/v1/order/order-plan-list")
    ResponseResult<List<OrderPlanDeliveryDto>> orderPlanList() {
    	log.info("查询我的订单计划");
    	UserSession session = UserContext.getCurrentUser();
    	long userId = session.getId();
    	Integer orderEndTime = shopConfigService.getOrderEndTime();
    	return orderService.getOrderPlanList(userId,orderEndTime,OrderType.LUNCH.getValue());
    }
    
    
    @GetMapping(value = "/v1/order/rider-info")
    ResponseResult<RiderInfoDto> riderInfo(@RequestParam(value = "deliveryNo" ,required = true) String deliveryNo,
                                           @RequestParam(value = "orderId" ,required = true) Long orderId) {
    	log.info("查询配送信息,配送单号={},订单id={}",deliveryNo,orderId);
    	Byte businessLine = OrderType.LUNCH.getValue();
    	return orderService.getRifderInfo(deliveryNo,orderId,businessLine);
    }
}
