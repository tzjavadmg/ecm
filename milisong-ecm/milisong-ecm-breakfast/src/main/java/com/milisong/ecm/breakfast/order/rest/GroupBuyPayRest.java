package com.milisong.ecm.breakfast.order.rest;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.milisong.ecm.breakfast.order.service.GroupBuyOrderHelper;
import com.milisong.ecm.breakfast.param.GroupBuyOrderPayParam;
import com.milisong.ecm.common.util.IPUtils;
import com.milisong.oms.api.GroupBuyOrderService;
import com.milisong.oms.dto.WechatMiniPayDto;
import com.milisong.oms.param.GroupBuyOrderParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/21 15:59
 */
@RestController
@Slf4j
public class GroupBuyPayRest {

    @Resource
    private GroupBuyOrderService groupBuyOrderService;
    @Resource
    private GroupBuyOrderHelper groupBuyOrderHelper;

    @PostMapping("/v1/order/group-buy-wechat-pay")
    ResponseResult<?> wechatMiniPay(@RequestBody GroupBuyOrderPayParam orderPaymentParam, HttpServletRequest request) {
        log.info("拼团支付订单入参：{}", JSON.toJSONString(orderPaymentParam));

        if (StringUtils.isEmpty(orderPaymentParam.getRealName())) {
            return ResponseResult.buildFailResponse("", "联系人不能为空！");
        }
        if (StringUtils.isEmpty(orderPaymentParam.getMobileNo())) {
            return ResponseResult.buildFailResponse("", "手机号不能为空！");
        }
        if (StringUtils.isEmpty(orderPaymentParam.getDeliveryCompany()) || orderPaymentParam.getDeliveryCompanyId() == null) {
            return ResponseResult.buildFailResponse("", "收货地址不能为空！");
        }
        if (StringUtils.isEmpty(orderPaymentParam.getTakeMealsAddrName()) || orderPaymentParam.getTakeMealsAddrId() == null) {
            return ResponseResult.buildFailResponse("", "取餐点不能为空！");
        }
        if (StringUtils.isEmpty(orderPaymentParam.getTakeMealsStartTime()) || StringUtils.isEmpty(orderPaymentParam.getTakeMealsEndTime())) {
            return ResponseResult.buildFailResponse("", "取餐时间不能为空！");
        }

        orderPaymentParam.setOrderIp(IPUtils.getIpAddr(request));
        GroupBuyOrderParam groupBuyOrderParam = groupBuyOrderHelper.buildGroupBuyOrderParam(orderPaymentParam);

        log.info("-----------------调用拼团支付订单入参：{}", JSON.toJSONString(groupBuyOrderParam));
        ResponseResult<WechatMiniPayDto> responseResult = groupBuyOrderService.order(groupBuyOrderParam);
        if (responseResult.isSuccess()) {
            WechatMiniPayDto wechatMiniPayDto = responseResult.getData();
            String dataPkg = wechatMiniPayDto.getDataPackage();
            String[] datas = dataPkg.split("=");

//         缓存formId，用于微信消息通知，key:deliveryId value:formId
            cacheFormId(wechatMiniPayDto.getOrderId(), orderPaymentParam.getFormId());
            cachePrepayId(wechatMiniPayDto.getOrderId(), datas[1]);
        }
        return responseResult;
    }

    @PostMapping("/v2/order/group-buy-order-cancel")
    ResponseResult<?> groupBuyOrderCancel(@RequestBody Map<String, String> params) {
        Long orderId = MapUtils.getLong(params, "orderId");
        if (orderId == null) {
            return ResponseResult.buildFailResponse("", "订单ID不能为空！");
        }
        return groupBuyOrderService.cancelOrder(orderId);
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

}