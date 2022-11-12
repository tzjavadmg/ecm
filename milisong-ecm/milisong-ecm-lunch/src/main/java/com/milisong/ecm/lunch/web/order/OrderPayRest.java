package com.milisong.ecm.lunch.web.order;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.milisong.ecm.common.util.IPUtils;
import com.milisong.ecm.lunch.order.service.OrderHelper;
import com.milisong.ecm.lunch.order.service.OrderValidator;
import com.milisong.ecm.lunch.order.service.VirtualOrderValidator;
import com.milisong.oms.api.OrderPayService;
import com.milisong.oms.api.RefundOrderService;
import com.milisong.oms.api.VirtualOrderService;
import com.milisong.oms.constant.OrderType;
import com.milisong.oms.constant.OrderUserSource;
import com.milisong.oms.dto.WechatMiniPayDto;
import com.milisong.oms.param.*;
import com.milisong.ucs.sdk.security.UserContext;
import com.milisong.ucs.sdk.security.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/8 10:50
 */
@Slf4j
@RestController
public class OrderPayRest {

    @Resource
    private VirtualOrderService virtualOrderService;
    @Resource
    private RefundOrderService refundOrderService;
    @Resource
    private OrderPayService orderPayService;
    @Resource
    private OrderHelper orderHelper;
    @Resource
    private VirtualOrderValidator virtualOrderValidator;
    @Resource
    private OrderValidator orderValidator;
    /**
     * 去结算，生成虚拟订单，锁定库存
     *
     * @param virtualOrderParam 虚拟订单对象
     * @return ResponseResult
     */
    @PostMapping("/v2/order/order")
    ResponseResult<?> virtualOrder(@RequestBody VirtualOrderParam virtualOrderParam) {
        UserSession session = UserContext.getCurrentUser();
        if (session != null) {
            virtualOrderParam.setUserId(session.getId());
            virtualOrderParam.setOpenId(session.getOpenId());
        } else {
            log.warn("创建虚拟认单异常：session为空！");
        }

        ResponseResult<?> validateResult = virtualOrderValidator.validate(virtualOrderParam);
        if (!validateResult.isSuccess()) {
            return validateResult;
        }

        log.info("创建虚拟订单参数：{}", JSON.toJSONString(virtualOrderParam));
        orderHelper.fillVirtualOrderParam(virtualOrderParam);
        //--------业务线标识---------------
        virtualOrderParam.setOrderType(OrderType.LUNCH.getValue());
        log.info("创建虚拟订单,重构后参数：{}", JSON.toJSONString(virtualOrderParam));
        return virtualOrderService.createVirtualOrder(virtualOrderParam);
    }

    @PostMapping("/v2/order/virtual-order-cancel")
    ResponseResult<?> virtualOrderCancel(@RequestBody Map<String, String> params) {
        try {
            Long orderId = MapUtils.getLong(params, "orderId");
            if (orderId == null) {
                return ResponseResult.buildFailResponse("", "订单ID不能为空！");
            }
            virtualOrderService.userCancelVirtualOrder(orderId);
        } catch (Exception e) {
            log.error("虚拟订单取消失败", e);
            throw new RuntimeException("取消虚拟订单异常", e);
        }
        return ResponseResult.buildSuccessResponse();
    }


    @PostMapping("/v2/order/order-cancel")
    ResponseResult<?> orderCancel(@RequestBody CancelOrderParam cancelOrderParam) {
        try {
            orderPayService.userCancelOrder(cancelOrderParam);
        } catch (Exception e) {
            log.error("订单取消失败", e);
            return ResponseResult.buildFailResponse();
        }
        return ResponseResult.buildSuccessResponse();
    }


    @PostMapping("/v2/order/wechat-mini-pay")
    ResponseResult<?> wechatMiniPay(@RequestBody OrderPaymentParam orderPaymentParam, HttpServletRequest request) {
        log.info("支付订单入参：{}", JSON.toJSONString(orderPaymentParam));

        if (orderPaymentParam.getOrderId() == null) {
            return ResponseResult.buildFailResponse("", "订单ID不能为空！");
        }
        if (orderPaymentParam.getDeliveryBuildingId() == null) {
            return ResponseResult.buildFailResponse("", "请先选择收货公司！");
        }
        if (StringUtils.isEmpty(orderPaymentParam.getRealName())) {
            return ResponseResult.buildFailResponse("", "联系人不能为空！");
        }
        if (StringUtils.isEmpty(orderPaymentParam.getMobileNo())) {
            return ResponseResult.buildFailResponse("", "手机号不能为空！");
        }
        if (StringUtils.isEmpty(orderPaymentParam.getDeliveryCompany())) {
            return ResponseResult.buildFailResponse("", "收货公司不能为空！");
        }
        if (StringUtils.isEmpty(orderPaymentParam.getTakeMealsAddrName()) || orderPaymentParam.getTakeMealsAddrId() == null) {
            return ResponseResult.buildFailResponse("", "取餐点不能为空！");
        }

        UserSession session = UserContext.getCurrentUser();
        if (session != null) {
            orderPaymentParam.setOpenId(session.getOpenId());
        }
        orderPaymentParam.setOrderIp(IPUtils.getIpAddr(request));
        //--------业务线标识---------------
        orderPaymentParam.setOrderType(OrderType.LUNCH.getValue());
        orderPaymentParam.setOrderSource(OrderUserSource.WECHAT_MINI_LC.getValue());
        orderHelper.fillOrderPaymentParam(orderPaymentParam);
        log.info("支付参数：{}", JSON.toJSONString(orderPaymentParam));
        orderValidator.validate(orderPaymentParam);

        cacheFormId(orderPaymentParam.getOrderId(), orderPaymentParam.getFormId());
        ResponseResult<WechatMiniPayDto> responseResult= orderPayService.payOrder(orderPaymentParam);
        if(responseResult.isSuccess()){
            WechatMiniPayDto wechatMiniPayDto= responseResult.getData();
            String dataPkg=wechatMiniPayDto.getDataPackage();
            String[] datas=dataPkg.split("=");
            cachePrepayId(orderPaymentParam.getOrderId(), datas[1]);
        }

        return responseResult;
    }

    private void cachePrepayId(long orderId, String prepayId) {
        String key = "wechat_notify_prepay_id:" + orderId;
        RedisCache.set(key, prepayId);
        RedisCache.expire(key, 7, TimeUnit.DAYS);
    }

    private void cacheFormId(long orderId, String formId) {
        // 缓存formId，用于微信消息通知，key:deliveryId value:formId
        if (StringUtils.isNotEmpty(formId)) {
            log.info("缓存formId:{},deliveryId:{}", formId, orderId);
            String key = "wechat_notify_form_id:" + orderId;
            RedisCache.set(key, formId);
            RedisCache.expire(key, 1, TimeUnit.HOURS);
        }
    }

    @PostMapping("/v1/order/order-refund")
    ResponseResult<?> refund(@RequestBody RefundOrderParam refundOrderParam) {
        log.info("订单退款入参：{}", JSON.toJSONString(refundOrderParam));
        return refundOrderService.refund(refundOrderParam);
    }


    @PostMapping("/v2/order/wechat-mini-repay")
    ResponseResult<?> wechatMiniRepay(@RequestBody OrderPaymentParam orderPaymentParam, HttpServletRequest request) {
        log.info("支付订单入参：{}", JSON.toJSONString(orderPaymentParam));
        UserSession session = UserContext.getCurrentUser();
        orderPaymentParam.setOpenId(session.getOpenId());
        orderPaymentParam.setOrderIp(IPUtils.getIpAddr(request));
        //--------业务线标识---------------
        orderPaymentParam.setOrderType(OrderType.LUNCH.getValue());
        orderPaymentParam.setOrderSource(OrderUserSource.WECHAT_MINI_LC.getValue());
        return orderPayService.payOrder(orderPaymentParam);
    }


    @PostMapping("/v1/order/delivery-timezone")
    ResponseResult<?> updateDeliveryTimezone(@RequestBody DeliveryTimezoneParam deliveryTimezoneParam) {
        log.info("订单退款入参：{}", JSON.toJSONString(deliveryTimezoneParam));
        return orderPayService.updateDeliveryTimezone(deliveryTimezoneParam);
    }

    @GetMapping("/v1/order/refundable-deliveries")
    ResponseResult<?> refundable(@RequestParam(value = "orderId", required = true) Long orderId) {
        log.info("查询可退款子单，订单ID：{}", orderId);
        return orderPayService.findDeliveries4RefundByOrderId(orderId);
    }

    @GetMapping("/v2/order/order")
    ResponseResult<?> getOrder(@RequestParam(value = "orderId", required = true) Long orderId) {
        log.info("去支付，查询订单ID:{}", orderId);
        return orderPayService.getOrderById(orderId);
    }
}
