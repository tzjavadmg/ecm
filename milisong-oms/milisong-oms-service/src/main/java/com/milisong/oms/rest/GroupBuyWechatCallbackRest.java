package com.milisong.oms.rest;

import com.milisong.oms.api.RefundOrderService;
import com.milisong.oms.param.PaymentResultParam;
import com.milisong.oms.service.OrderPaymentHelper;
import com.milisong.oms.service.groupbuy.GroupBuyOrderInternalService;
import com.milisong.oms.util.XMLUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.SortedMap;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/20 20:48
 */
@Slf4j
@RestController
public class GroupBuyWechatCallbackRest {

    private final String CALLBACK_SUCCESS = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    @Resource
    private RefundOrderService refundOrderService;
    @Resource
    private GroupBuyOrderInternalService groupBuyOrderInternalService;
    /**
     * 灰度使用
     *
     * @param request
     * @param response
     */
    @Deprecated
    @RequestMapping(value = "/v2/order/groupbuyorder-callback-pre")
    public void payCallbackPre(HttpServletRequest request, HttpServletResponse response) {
        payCallback(request, response);
    }

    @RequestMapping(value = "/v2/order/groupbuyorder-callback")
    public void payCallback(HttpServletRequest request, HttpServletResponse response) {
        try {
            String notityXml = getXml(request);
            log.info("拼团支付回调 notityXml -> {}", notityXml);

            SortedMap<String, String> map = XMLUtil.doXMLParse(notityXml);
            log.info("支付回调====================参数：{}", map);
            // 回调操作
            callback(notityXml);
            response.getWriter().write(CALLBACK_SUCCESS);
        } catch (Exception e) {
            log.error("主动支付回调", e);
        }
    }

    private String getXml(HttpServletRequest request) throws IOException {
        String inputLine;
        String notityXml = "";
        while ((inputLine = request.getReader().readLine()) != null) {
            notityXml += inputLine;
        }
        request.getReader().close();
        return notityXml;
    }

    private void callback(String notifyXml) {
        SortedMap<String, String> map = XMLUtil.doXMLParse(notifyXml);
        log.info("------------拼团支付回调，参数：{}", map);
        //订单号
        String orderIdString = map.get("outTradeNo");
        // 支付结果
        String resultCode = map.get("resultCode");
        String errorCode = map.get("errorCode");
        String errCodeDes = map.get("errCodeDes");
        if (StringUtils.isNotEmpty(orderIdString)) {
            Long orderId = Long.valueOf(orderIdString);
            boolean isSuccess = "SUCCESS".equals(resultCode);
            PaymentResultParam paymentResultParam = new PaymentResultParam();
            paymentResultParam.setOrderId(orderId);
            paymentResultParam.setIsSuccess(isSuccess);
            paymentResultParam.setErrorCode(errorCode);
            paymentResultParam.setErrCodeDes(errCodeDes);
            groupBuyOrderInternalService.payCallback(paymentResultParam);
        } else {
            log.error("------------订单回调异常,订单ID为空！");
        }
    }

    @Deprecated
    @RequestMapping(value = "/v2/order/groupbuy-refund-callback-pre")
    void refundCallbackPre(HttpServletRequest request, HttpServletResponse response) {
        refundCallback(request, response);
    }

    @RequestMapping(value = "/v2/order/groupbuy-refund-callback")
    void refundCallback(HttpServletRequest request, HttpServletResponse response) {
        try {
            String notityXml = getXml(request);
            log.info("退款回调 notityXml -> {}", notityXml);
            SortedMap<String, String> map = XMLUtil.doXMLParse(notityXml);
            // 回调操作
            groupBuyOrderInternalService.refundCallback(map);
            response.getWriter().write(CALLBACK_SUCCESS);
        } catch (Exception e) {
            log.error("退款回调", e);
        }
    }
}
