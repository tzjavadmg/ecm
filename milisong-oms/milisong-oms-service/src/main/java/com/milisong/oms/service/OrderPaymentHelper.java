package com.milisong.oms.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.util.BeanMapper;
import com.milisong.oms.configruation.SystemProperties;
import com.milisong.oms.constant.*;
import com.milisong.oms.domain.Order;
import com.milisong.oms.dto.OrderDto;
import com.milisong.oms.dto.WechatMiniPayDto;
import com.milisong.oms.mapper.OrderMapper;
import com.milisong.oms.mq.MessageProducer;
import com.milisong.oms.mq.PaymentMessage;
import com.milisong.oms.param.OrderPaymentParam;
import com.milisong.oms.param.PaymentResultParam;
import com.milisong.oms.util.SysConfigUtils;
import com.milisong.oms.util.OrderRedisKeyUtils;
import com.milisong.wechat.miniapp.api.MiniAppService;
import com.milisong.wechat.miniapp.dto.PayUnifiedOrderDto;
import com.milisong.wechat.miniapp.dto.PayUnifiedOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/8 10:51
 */
@Slf4j
@Component
public class OrderPaymentHelper {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private MessageProducer messageProducer;
    @Resource
    private MiniAppService miniAppService;
    @Resource
    private DelayQueueHelper delayQueueHelper;
    @Resource
    private SystemProperties systemProperties;

    public ResponseResult<WechatMiniPayDto> payment(Order order, OrderPaymentParam orderPaymentParam) {
        Long orderId = order.getId();
        BigDecimal actualPayAmount = order.getTotalPayAmount();
        //构建支付参数
        PayUnifiedOrderRequest unifiedOrderRequest = buildPayUnifiedOrderRequest(orderPaymentParam, orderId, actualPayAmount, order.getOrderType());
        log.info("----------主动支付入参：{}", JSON.toJSONString(unifiedOrderRequest));
        //------测试使用代码---start----
//        PayUnifiedOrderDto payUnifiedOrderDto2=new PayUnifiedOrderDto();
//        payUnifiedOrderDto2.setPrepayId("11111");
//        ResponseResult<PayUnifiedOrderDto> responseResult = ResponseResult.buildSuccessResponse(payUnifiedOrderDto2);
        //------测试使用代码---end----
        ResponseResult<PayUnifiedOrderDto> responseResult = miniAppService.unifiedOrder(unifiedOrderRequest);
        PayUnifiedOrderDto payUnifiedOrderDto = responseResult.getData();
        log.info("----------主动支付返回结果：{}", JSON.toJSONString(responseResult));

        //缓存订单信息
        cacheOrderDetail(order);
        if (responseResult.isSuccess()) {
            //构建支付参数
            WechatMiniPayDto wechatMiniPayDto = buildWechatMiniPayDto(payUnifiedOrderDto);
            lockRedPacket(orderPaymentParam.getRedPacketId(), order.getOrderNo());
            lockCoupon(orderPaymentParam.getCouponId(), order.getOrderNo());
            return ResponseResult.buildSuccessResponse(wechatMiniPayDto);
        } else {
            String errorCode = responseResult.getCode();
            String errCodeDes = responseResult.getMessage();
            handlePayFail(order, errorCode, errCodeDes);
            lockRedPacket(orderPaymentParam.getRedPacketId(), order.getOrderNo());
            lockCoupon(orderPaymentParam.getCouponId(), order.getOrderNo());
            return ResponseResult.buildFailResponse(errorCode, errCodeDes);
        }
    }

    public ResponseResult<?> paymentResult(PaymentResultParam paymentResultParam) {
        Order order = orderMapper.selectByPrimaryKey(paymentResultParam.getOrderId());
        if (OrderStatus.PAID.getValue() == order.getStatus()) {
            log.warn("~~~~~~~订单已经支付~~~~~~订单ID:{}~~~~~~~", order.getId());
            return ResponseResult.buildFailResponse(OrderResponseCode.ORDER_PAID.getCode(), OrderResponseCode.ORDER_PAID.getMessage());
        }

        if (OrderStatus.CANCELED.getValue() == order.getStatus()) {
            log.warn("~~~~~~~订单已经取消~~~~~~订单ID:{}~~~~~~~", order.getId());
            return ResponseResult.buildFailResponse(OrderResponseCode.ORDER_CANCELED.getCode(), OrderResponseCode.ORDER_CANCELED.getMessage());
        }

        if (OrderStatus.COMPLETED.getValue() == order.getStatus()) {
            log.warn("~~~~~~~订单已经完成~~~~~~订单ID:{}~~~~~~~", order.getId());
            return ResponseResult.buildFailResponse(OrderResponseCode.ORDER_COMPLETED.getCode(), OrderResponseCode.ORDER_COMPLETED.getMessage());
        }

        if (BooleanUtils.isTrue(paymentResultParam.getIsSuccess())) {
            handlePaySuccess(order);
        } else {
            handlePayFail(order, paymentResultParam.getErrorCode(), paymentResultParam.getErrCodeDes());
        }

        return ResponseResult.buildSuccessResponse();
    }

    public void handlePaySuccess(Order order) {
        PaymentMessage paymentMessage = buildPaymentMessage(order);
        paymentMessage.setStatus(PaymentStatus.SUCCEED.getValue());
        paymentMessage.setPaySuccess(true);
        if (OrderStatus.CANCELED.getValue() == order.getStatus()) {
            log.warn("~~~~~~~支付回调异常,订单已经取消~~~~~~订单ID:{}~~~~~~~", order.getId());
        }
        //更新订单支付状态
        order.setStatus(OrderStatus.PAID.getValue());
        order.setPayTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);
        messageProducer.send(paymentMessage);
        unlockRedPacket(order.getRedPacketId());
        unlockCoupon(order.getCouponId());
        delayQueueHelper.remove(order.getId());
        log.info("--------------------支付成功MQ消息发送成功!");
    }

    private void cacheOrderDetail(Order order) {
        OrderDto orderDto = BeanMapper.map(order, OrderDto.class);
        String cacheKey = OrderRedisKeyUtils.getOrderDetailKey(order.getId());
        log.info("缓存订单详情：key:{},data:{}", cacheKey, JSON.toJSONString(orderDto));
        RedisCache.setEx(cacheKey, JSON.toJSONString(orderDto), 30, TimeUnit.DAYS);
    }

    public void handlePayFail(Order order, String errorCode, String errCodeDes) {
        PaymentMessage paymentMessage = buildPaymentMessage(order);
        paymentMessage.setErrorCode(errorCode);
        paymentMessage.setErrorDescription(errCodeDes);
        paymentMessage.setStatus(PaymentStatus.FAILED.getValue());
        paymentMessage.setPaySuccess(false);
        //发送支付失败消息
        messageProducer.send(paymentMessage);
        log.info("--------------------支付失败MQ消息发送成功!");
    }

    private PaymentMessage buildPaymentMessage(Order order) {
        PaymentMessage paymentMessage = new PaymentMessage();
        paymentMessage.setAmount(order.getTotalPayAmount());
        paymentMessage.setOrderId(order.getId());
        paymentMessage.setUserId(order.getUserId());
        paymentMessage.setOpenId(order.getOpenId());
        paymentMessage.setRedPacketId(order.getRedPacketId());
        paymentMessage.setCouponId(order.getCouponId());
        paymentMessage.setShopCode(order.getShopCode());
        paymentMessage.setDeliveryAddressId(order.getDeliveryAddressId());
        paymentMessage.setOrderType(order.getOrderType());
        paymentMessage.setOrderSource(order.getOrderSource());
        paymentMessage.setOrderMode(order.getOrderMode());
        return paymentMessage;
    }

    private void lockRedPacket(Long redPacketId, String orderNo) {
        if (redPacketId != null) {
            log.info("-----------锁定红包：{}，订单编号：{}", redPacketId, orderNo);
            String redPacketLockKey = OrderRedisKeyUtils.getRedPacketLockKey(redPacketId);
            RedisCache.setEx(redPacketLockKey, orderNo, SysConfigUtils.getUnPayExpiredTime(), TimeUnit.MINUTES);
        }
    }

    public void unlockRedPacket(Long redPacketId) {
        if (redPacketId != null) {
            log.info("-----------解锁红包：{}", redPacketId);
            String redPacketLockKey = OrderRedisKeyUtils.getRedPacketLockKey(redPacketId);
            RedisCache.delete(redPacketLockKey);
        }
    }

    private void lockCoupon(Long couponId, String orderNo) {
        if (couponId != null) {
            log.info("-----------锁定优惠券：{}，订单编号：{}", couponId, orderNo);
            String couponLockKey = OrderRedisKeyUtils.getCouponLockKey(couponId);
            RedisCache.setEx(couponLockKey, orderNo, SysConfigUtils.getUnPayExpiredTime(), TimeUnit.MINUTES);
        }
    }

    public void unlockCoupon(Long couponId) {
        if (couponId != null) {
            log.info("-----------解锁优惠券：{}", couponId);
            String couponLockKey = OrderRedisKeyUtils.getCouponLockKey(couponId);
            RedisCache.delete(couponLockKey);
        }
    }

    private WechatMiniPayDto buildWechatMiniPayDto(PayUnifiedOrderDto payUnifiedOrderDto) {
        WechatMiniPayDto wechatMiniPayDto = new WechatMiniPayDto();
        wechatMiniPayDto.setTimeStamp(payUnifiedOrderDto.getTimeStamp());
        wechatMiniPayDto.setSignType("MD5");
        wechatMiniPayDto.setNonceStr(payUnifiedOrderDto.getNonceStr());
        wechatMiniPayDto.setDataPackage("prepay_id=" + payUnifiedOrderDto.getPrepayId());
        wechatMiniPayDto.setPaySign(payUnifiedOrderDto.getSign());
        return wechatMiniPayDto;
    }


    private PayUnifiedOrderRequest buildPayUnifiedOrderRequest(OrderPaymentParam orderPaymentParam, long orderId, BigDecimal totalAmount, byte orderType) {
        return buildPayUnifiedOrderRequest(orderPaymentParam.getOpenId(), orderPaymentParam.getOrderIp(), orderId, totalAmount, orderType, orderPaymentParam.getOrderSource());
    }

    public PayUnifiedOrderRequest buildPayUnifiedOrderRequest(String openId, String orderIp, long orderId, BigDecimal totalAmount, byte orderType, Byte orderSource) {
        PayUnifiedOrderRequest unifiedOrderRequest = new PayUnifiedOrderRequest();
        unifiedOrderRequest.setOpenid(openId);
        unifiedOrderRequest.setOutTradeNo(String.valueOf(orderId));
        unifiedOrderRequest.setTotalFee(totalAmount);
        unifiedOrderRequest.setSpbillCreateIp(orderIp);
        unifiedOrderRequest.setNotifyURL(systemProperties.getWechatPay().getDefaultPayCallbackUrl());
        if (OrderType.LUNCH.getValue() == orderType) {
            unifiedOrderRequest.setBody("米立送精选午餐");
        } else {
            unifiedOrderRequest.setBody("米立送精选早餐");
        }

        if (orderSource != null) {
            if (OrderUserSource.WECHAT_MINI_BF.getValue() == orderSource) {
                unifiedOrderRequest.setBusinessLine(OrderType.BREAKFAST.getValue());
            } else {
                unifiedOrderRequest.setBusinessLine(OrderType.LUNCH.getValue());
            }
        } else {
            unifiedOrderRequest.setBusinessLine(orderType);
        }

        return unifiedOrderRequest;
    }
}
