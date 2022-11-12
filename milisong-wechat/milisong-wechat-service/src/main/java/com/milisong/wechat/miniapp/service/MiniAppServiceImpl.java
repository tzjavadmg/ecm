package com.milisong.wechat.miniapp.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.JsonMapper;
import com.google.common.collect.Maps;
import com.milisong.wechat.miniapp.api.MiniAppService;
import com.milisong.wechat.miniapp.config.AppProperties;
import com.milisong.wechat.miniapp.constant.MiniPayConstants;
import com.milisong.wechat.miniapp.constant.WXConstants;
import com.milisong.wechat.miniapp.constant.WechatUrl;
import com.milisong.wechat.miniapp.dto.*;
import com.milisong.wechat.miniapp.enums.BusinessLine;
import com.milisong.wechat.miniapp.enums.MiniAppResponseCode;
import com.milisong.wechat.miniapp.enums.TradeTypes;
import com.milisong.wechat.miniapp.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.milisong.wechat.miniapp.constant.WechatUrl.*;
import static com.milisong.wechat.miniapp.enums.MiniAppResponseCode.*;


/**
 * @author sailor wang
 * @date 2018/8/31 下午3:41
 * @description
 */
@Slf4j
@Service
public class MiniAppServiceImpl implements MiniAppService {

    private final static String SUCCESS = "SUCCESS";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppProperties miniappProp;

    private String MINI_APP_ACCESS_TOKEN = "MINI_APP_ACCESS_TOKEN";

    /**
     * 登录凭证校验
     *
     * @param loginRequest
     * @return
     */
    @Override
    public ResponseResult<MiniSessionDto> login(MiniLoginRequest loginRequest) {
        String validMsg = BeanFieldsUtils.checkRequiredFields(loginRequest);
        if (StringUtils.isNotEmpty(validMsg)) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), validMsg);
        }
        String appid = miniappProp.getMiniAppid(loginRequest.getBusinessLine());
        String secret = miniappProp.getMiniSecret(loginRequest.getBusinessLine());
        String loginUrl = WechatUrl.LOGIN_URL(appid, secret, loginRequest.getJsCode());
        log.info("日志打印， miniappid -> {}, secret -> {}, jscode ->{}, buzLine -> {}", appid, secret, loginRequest.getJsCode(),loginRequest.getBusinessLine());
        String jsonStr = restTemplate.getForObject(loginUrl, String.class);
        log.info("jsonStr -> {}", jsonStr);
        MiniSessionDto wechatSession = JSONObject.parseObject(jsonStr, MiniSessionDto.class);
        if (wechatSession.getErrcode() != null) {
            return ResponseResult.buildFailResponse(String.valueOf(wechatSession.getErrorcode()), wechatSession.getErrormsg());
        }
        return ResponseResult.buildSuccessResponse(wechatSession);
    }

    /**
     * 校验签约号
     *
     * @param contractRequest
     * @return
     */
    @Override
    public ResponseResult<MiniContractDto> validateContractNo(MiniContractRequest contractRequest) {
        String validMsg = BeanFieldsUtils.checkRequiredFields(contractRequest);
        if (StringUtils.isNotEmpty(validMsg)) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), validMsg);
        }
        try {
            Byte buzLine = contractRequest.getBusinessLine();
            SortedMap<String, String> signParams = new TreeMap<String, String>();
            signParams.put("mch_id", miniappProp.getMchid(buzLine).trim()); // 商户id
            signParams.put("appid", miniappProp.getMiniAppid(buzLine).trim());// appid
            signParams.put("contract_id", String.valueOf(contractRequest.getContractNo()));
            signParams.put("version", "1.0");
            String signValue = SHA1.createSHA1Sign(signParams, miniappProp.getApikey(buzLine).trim());
            signParams.put("sign", signValue);
            Map<String, String> contractMap = queryContractNo(signParams, signValue);
            log.info(" 校验签约号 contractMap —> {}", JSONObject.toJSONString(contractMap));
            if (contractMap != null && contractMap.get("contractState") != null && contractMap.get("contractState").equals("0")) {
                MiniContractDto wechatContract = new MiniContractDto();
                wechatContract.setWechatContractNo(contractMap.get("contractId"));
                return ResponseResult.buildSuccessResponse(wechatContract);
            }
            return ResponseResult.buildSuccessResponse(contractMap.get("returnCode"), contractMap.get("returnMsg"));
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildFailResponse(QUERY_CONTRACT_EXCEPTION.code(), QUERY_CONTRACT_EXCEPTION.message());
    }

    /**
     * 小程序代扣(该接口调用成功只是成功发送了代扣指令，并不代表代扣成功)
     *
     * @param paymentRequest
     * @return
     */
    @Override
    public ResponseResult<MiniPaymentResult> pappayapply(MiniPaymentRequest paymentRequest) {
        String validMsg = BeanFieldsUtils.checkRequiredFields(paymentRequest);
        if (StringUtils.isNotEmpty(validMsg)) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), validMsg);
        }
        try {
            Byte buzLine = paymentRequest.getBusinessLine();
            SortedMap<String, String> signParams = new TreeMap<String, String>();
            signParams.put("mch_id", miniappProp.getMchid(buzLine).trim()); // 商户id
            signParams.put("appid", miniappProp.getMiniAppid(buzLine).trim());// 小程序 appid

            signParams.put("nonce_str", SHA1.getNonceStr());
            signParams.put("body", "米立送-购买商品");
            signParams.put("out_trade_no", paymentRequest.getOrderNo());
            signParams.put("total_fee", XMLUtil.convertToWxAmt(paymentRequest.getTotalAmount()));
            signParams.put("spbill_create_ip", paymentRequest.getIp());
            signParams.put("notify_url", miniappProp.getPappayapplyCallbackUrl());
            signParams.put("trade_type", "PAP");
            signParams.put("contract_id", paymentRequest.getContractNo());
            String signValue = SHA1.createSHA1Sign(signParams, miniappProp.getApikey(buzLine).trim());
            String xmlParam = XMLUtil.StringToXml(signParams, signValue);
            String url = WechatUrl.PAPPAYAPPLY_URL();
            log.info("代扣参数 xmlParam -> {}", xmlParam);
            log.info("代扣 url -> {}", url);
            String result = restTemplate().postForObject(url, xmlParam, String.class);
            log.info("代扣 pappayapply -> {}", result);
            Map<String, String> mapResult = parseResult(result);

            MiniPaymentResult paymentResult = new MiniPaymentResult();
            if (mapResult != null && mapResult.get("resultCode") != null && mapResult.get("resultCode").equals(SUCCESS)) {
                paymentResult.setCode(mapResult.get("resultCode"));
                return ResponseResult.buildSuccessResponse(paymentResult);
            } else if (mapResult != null && mapResult.get("resultCode") != null && mapResult.get("resultCode").equals("FAIL")) {
                paymentResult.setCode(mapResult.get("resultCode"));
                paymentResult.setErrorcode(mapResult.get("errCode"));
                paymentResult.setErrormsg(mapResult.get("errCodeDes"));
                return ResponseResult.buildSuccessResponse(paymentResult);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildSuccessResponse(PAPPAYAPPLY_EXCEPTION.code(), PAPPAYAPPLY_EXCEPTION.message());
    }

    /**
     * 获取用户手机号
     *
     * @param sessionkey
     * @param encryptedData
     * @param iv
     * @return
     */
    @Override
    public ResponseResult<String> getMobile(String sessionkey, String encryptedData, String iv) {
        try {
            if (StringUtils.isBlank(sessionkey) || StringUtils.isBlank(encryptedData) || StringUtils.isBlank(iv)) {
                return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
            }
            String data = AESUtils.decryptForMini(encryptedData, sessionkey, iv);
            JSONObject jsonObj = JSONObject.parseObject(data);
            log.info("获取手机号 jsonObj -> {}", jsonObj);
            if (jsonObj != null && jsonObj.containsKey("phoneNumber")) {
                return ResponseResult.buildSuccessResponse(MiniAppResponseCode.SUCCESS.code(), MiniAppResponseCode.SUCCESS.message(), "", jsonObj.getString("phoneNumber"));
            }
        } catch (Exception e) {
            log.error("解密用户手机号异常", e);
        }
        return ResponseResult.buildFailResponse(QUERY_MOBILE_FAIL.code(), QUERY_MOBILE_FAIL.message());
    }

    /**
     * 代扣签约
     *
     * @param miniContractRequest
     * @return
     */
    @Override
    public ResponseResult<Map<String, Object>> signContract(MiniContractRequest miniContractRequest) {
        String validMsg = BeanFieldsUtils.checkRequiredFields(miniContractRequest);
        if (StringUtils.isNotEmpty(validMsg)) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), validMsg);
        }
        try {
            Byte buzLine = miniContractRequest.getBusinessLine();
            Map<String, Object> params = new TreeMap<>();
            params.put("appid", miniappProp.getMiniAppid(buzLine));
            params.put("contract_code", IdGenerator.nextId());
            params.put("contract_display_account", miniContractRequest.getMobileNo());
            params.put("mch_id", miniappProp.getMchid(buzLine));
            params.put("notify_url", miniappProp.getContractCallbackUrl(buzLine));
            params.put("plan_id", miniappProp.getPlanId());
            long now = System.currentTimeMillis() / 1000;
            params.put("request_serial", now);
            params.put("timestamp", now);
            String sign = MD5.getSign(params, miniappProp.getApikey(buzLine));
            params.put("sign", sign);
            log.info("signContract -> {} ", JSONObject.toJSONString(params));
            return ResponseResult.buildSuccessResponse(params);
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildFailResponse(SIGN_CONTRACT_EXCEPTION.code(), SIGN_CONTRACT_EXCEPTION.message());
    }

    /**
     * 查询订单
     *
     * @param orderQueryRequest    商户系统内部的订单号，当没提供transactionId时需要传这个。
     * @return
     */
    @Override
    public ResponseResult<PayOrderQueryResult> queryOrder(MiniOrderQueryRequest orderQueryRequest) {
        String validMsg = BeanFieldsUtils.checkRequiredFields(orderQueryRequest);
        if (StringUtils.isNotEmpty(validMsg)) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), validMsg);
        }

        try {
            Map<String, String> reqData = Maps.newHashMap();
            reqData.put("out_trade_no", orderQueryRequest.getOutTradeNo().trim());

            // 填装请求参数
            WXPayUtil.fillRequestData(reqData,orderQueryRequest.getBusinessLine());
            String orderQueryUrl = WechatUrl.QUERYORDER_URL();
            // 请求微信订单查询接口
            String xmlParam = XMLUtil.StringToXml(reqData);
            String rstStr = restTemplate().postForObject(orderQueryUrl, xmlParam, String.class);

            PayOrderQueryResult payOrderQueryDto = XMLUtil.convertToObject(rstStr, PayOrderQueryResult.class);
            if (null != payOrderQueryDto && SUCCESS.equalsIgnoreCase(payOrderQueryDto.getResultCode())) {
                return ResponseResult.buildSuccessResponse(payOrderQueryDto);
            }
            return ResponseResult.buildFailResponse(payOrderQueryDto.getErrCode(),payOrderQueryDto.getReturnMsg());
        } catch (Exception e) {
            log.error("", e);
            return ResponseResult.buildFailResponse(QUERY_ORDER_EXCEPTION.code(), QUERY_ORDER_EXCEPTION.message());
        }
    }

    /**
     * 发送模板消息
     *
     * @param msgTemplate
     * @return
     */
    @Override
    public ResponseResult sendTemplateMsg(MiniMsgTemplateDto msgTemplate) {
        String accessToken = getAccessToken(msgTemplate.getBusinessLine());
        if (StringUtils.isBlank(accessToken)) {
            ResponseResult<String> result = refreshAccessToken(msgTemplate.getBusinessLine());
            if (result.isSuccess()){
                accessToken = result.getData();
            }
            if (StringUtils.isBlank(accessToken)){
                log.info("微信小程序 accessToken 为空");
                return ResponseResult.buildFailResponse(ACCESSTOKEN_EMPTY.code(), ACCESSTOKEN_EMPTY.message());
            }
        }
        if (msgTemplate == null || StringUtils.isBlank(msgTemplate.getOpenId()) || StringUtils.isBlank(msgTemplate.getFormId())
                || CollectionUtils.isEmpty(msgTemplate.getData())) {
            log.info("发送模板消息参数为空 -> {}", JSONObject.toJSONString(msgTemplate));
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }

        String url = WechatUrl.TEMPLATE_MSG_URL() + accessToken;
        Map<String, Object> params = new HashMap<>();
        params.put("touser", msgTemplate.getOpenId());
        params.put("template_id", msgTemplate.getMsgId());
        params.put("form_id", msgTemplate.getFormId());
        params.put("appid", miniappProp.getMiniAppid(msgTemplate.getBusinessLine()));
        params.put("page", StringUtils.isEmpty(msgTemplate.getTransferPage()) ? "pages/transfer/index" : msgTemplate.getTransferPage());
        Map<String, Object> dataMap = new HashMap<>();
        for (int i = 0; i < msgTemplate.getData().size(); i++) {
            Map<String, Object> valMap = new HashMap<>();
            valMap.put("value", msgTemplate.getData().get(i));
            dataMap.put("keyword" + (i + 1), valMap);
        }
        params.put("data", dataMap);
        log.info("调用微信小程序发送模板，params：{}", JSONObject.toJSONString(params));
        String jsonString = restTemplate.postForObject(url, params, String.class);
        log.info("调用微信小程序发送模板，返回json字符串：" + jsonString);
        return ResponseResult.buildSuccessResponse();
    }

    @Override
    public ResponseResult<String> refreshAccessToken(Byte buzLine) {
        String url = String.format(WechatUrl.ACCESSTOKEN_URL(miniappProp.getMiniAppid(buzLine),miniappProp.getMiniSecret(buzLine)));

        String jsonStr = restTemplate.getForObject(url, String.class);
        log.info("调用微信获取access token接口，返回json字符串：" + jsonStr);
        JsonMapper mapper = JsonMapper.nonEmptyMapper();
        MiniAccessTokenDto accessTokenDto = mapper.fromJson(jsonStr, MiniAccessTokenDto.class);

        if (accessTokenDto == null || StringUtils.isBlank(accessTokenDto.getAccessToken())) {
            log.error("刷新access_token失败， jsonStr -> {}", jsonStr);
            return ResponseResult.buildFailResponse();
        }

        String accessToken = accessTokenDto.getAccessToken();
        int expiresIn = accessTokenDto.getExpiresIn();
        log.info("超时时间：" + expiresIn);
        String key = MINI_APP_ACCESS_TOKEN;
        if (buzLine != null){
            key = MINI_APP_ACCESS_TOKEN+":"+buzLine.intValue();
        }
        RedisCache.setEx(key, accessToken, expiresIn - 60, TimeUnit.SECONDS);
        return ResponseResult.buildSuccessResponse(accessToken);
    }

    @Override
    public ResponseResult<PayUnifiedOrderDto> unifiedOrder(PayUnifiedOrderRequest request) {
        String validMsg = BeanFieldsUtils.checkRequiredFields(request);
        if (StringUtils.isNotEmpty(validMsg)) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), validMsg);
        }
        try {
            log.info("主动下单接口参数 -> {}", request);
            Byte buzLine = request.getBusinessLine();
            // 默认小程序主动支付
            request.setSceneInfo("小程序支付");// 支付场景
            request.setTradeType(TradeTypes.JSAPI.name());

            //拼团接口回调url使用参数值,兼容老接口
            if(StringUtils.isNotEmpty(request.getNotifyURL())){
                log.info("使用参数回调notifyURL：{}",request.getNotifyURL());
                request.setNotifyURL(request.getNotifyURL());
            }else {
                request.setNotifyURL(miniappProp.getUnifiedOrderCallbackUrl(buzLine));
            }

            Map<String, String> reqData = request.toMap();
            // 填装请求参数
            WXPayUtil.fillRequestData(reqData,request.getBusinessLine());

            String unifiedOrderUrl = PAY_UNIFIEDORDER_URL();
            String xmlParam = XMLUtil.StringToXml(reqData);
            log.info("xml参数：{}", xmlParam);

            // 请求统一下单接口
            String rstStr = restTemplate().postForObject(unifiedOrderUrl, xmlParam, String.class);
            log.info("统一下单返回结果 -> {}", rstStr);
            PayUnifiedOrderDto payUnifiedOrderDto = XMLUtil.convertToObject(rstStr, PayUnifiedOrderDto.class);
            if (null != payUnifiedOrderDto && SUCCESS.equalsIgnoreCase(payUnifiedOrderDto.getResultCode())) {
                Map<String, String> map = Maps.newHashMap();
                String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
                String nonceStr = WXPayUtil.generateUUID();
                String packageStr = "prepay_id=" + payUnifiedOrderDto.getPrepayId();

                map.put("appId", miniappProp.getMiniAppid(buzLine));
                map.put("timeStamp", timeStamp);
                map.put("nonceStr", nonceStr);
                map.put("package", packageStr);
                map.put("signType", WXConstants.SignType.MD5.name());
                WXConstants.SignType.convert2Signtype("");
                String sign = WXPayUtil.generateSignature(map, miniappProp.getApikey(buzLine).trim(), WXConstants.SignType.convert2Signtype(miniappProp.getSigntype(buzLine).trim()));

                log.info(" args -> {}, sign -> {}", JSON.toJSONString(map), sign);
                payUnifiedOrderDto.setTimeStamp(timeStamp);
                payUnifiedOrderDto.setNonceStr(nonceStr);
                payUnifiedOrderDto.setSign(sign);
                // 支付成功
                return ResponseResult.buildSuccessResponse(payUnifiedOrderDto);
            }
            return ResponseResult.buildFailResponse(payUnifiedOrderDto.getErrCode(), payUnifiedOrderDto.getReturnMsg());
        } catch (Exception e) {
            log.error("统一下单失败", e);
            return ResponseResult.buildFailResponse(UNIFIEDORDER_EXCEPTION.code(), UNIFIEDORDER_EXCEPTION.message());
        }
    }

    /**
     * 小程序申请退款
     *
     * @param request 请求对象
     * @return
     */
    @Override
    public ResponseResult<MiniPayRefundResult> refund(MiniPayRefundRequest request) {
        String validMsg = BeanFieldsUtils.checkRequiredFields(request);
        if (StringUtils.isNotEmpty(validMsg)) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), validMsg);
        }

        try {
            String refundUrl = REFUND_URL();
            Byte buzline = request.getBusinessLine();
            // 填装请求参数
            WXPayUtil.fillRequestData(request);

            String mchId = miniappProp.getMchid(buzline);
            log.info(" buzline -> {}, 退款 mchId -> {}, request -> {}",buzline,mchId,JSONObject.toJSONString(request));
            String rstStr = "";
            if (BusinessLine.BREAKFAST.getCode().equals(buzline)){
                log.info("BREAKFAST...");
                rstStr = CertHttpUtil.postData(refundUrl, request.toXML(), mchId, buzline);
            }else {
                log.info("LUNCH...");
                rstStr = CertHttpUtil.postData(refundUrl, request.toXML(), mchId, buzline);
            }
            log.info("退款返回数据 rstStr -> {}", rstStr);
            MiniPayRefundResult result = BasicPayDto.fromXML(rstStr, MiniPayRefundResult.class);
            if (null != result && SUCCESS.equalsIgnoreCase(result.getResultCode())) {
                return ResponseResult.buildSuccessResponse(result);
            }
            return ResponseResult.buildFailResponse("", result);
        } catch (Exception e) {
            log.error("", e);
            return ResponseResult.buildFailResponse(REFUND_EXCEPTION.code(), REFUND_EXCEPTION.message());
        }
    }

    /**
     * 查询退款数据
     *
     * @param refundQueryRequest
     * @return
     */
    @Override
    public ResponseResult<MiniRefundQueryResult> refundQuery(MiniRefundQueryRequest refundQueryRequest) {
        String validMsg = refundQueryRequest.checkConstraints();
        if (StringUtils.isNotEmpty(validMsg)) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), validMsg);
        }
        try {
            String refundQueryUrl = REFUND_QUERY_URL();
            // 填装请求参数
            WXPayUtil.fillRequestData(refundQueryRequest);

            // 查询退款数据
            String rstStr = restTemplate().postForObject(refundQueryUrl, refundQueryRequest.toXML(), String.class);
            log.info("查询退款数据 -> {}",rstStr);
            MiniRefundQueryResult result = XMLUtil.convertToObject(rstStr, MiniRefundQueryResult.class);
            result.setXmlString(rstStr);
            result.composeRefundRecords();
            if (null != result && SUCCESS.equalsIgnoreCase(result.getResultCode())) {
                return ResponseResult.buildSuccessResponse(result);
            }
            return ResponseResult.buildFailResponse(result.getErrCode(), result.getReturnMsg());
        } catch (Exception e) {
            log.error("查询退款数据", e);
        }
        return ResponseResult.buildFailResponse(REFUND_QUERY_EXCEPTION.code(), REFUND_QUERY_EXCEPTION.message());
    }

    /**
     * 订单关闭
     *
     * @param orderCloseRequest
     * @return
     */
    @Override
    public ResponseResult<MiniPayOrderCloseResult> closeOrder(MiniPayOrderCloseRequest orderCloseRequest) {
        String validMsg = BeanFieldsUtils.checkRequiredFields(orderCloseRequest);
        if (StringUtils.isNotEmpty(validMsg)) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), validMsg);
        }

        try {
            String closeOrderUrl = CLOSE_ORDER_URL();
            Map<String, String> reqData = Maps.newHashMap();
            reqData.put("out_trade_no", orderCloseRequest.getOutTradeNo().trim());
            // 填装请求参数
            WXPayUtil.fillRequestData(reqData,orderCloseRequest.getBusinessLine());

            // 关闭订单
            String rstStr = restTemplate().postForObject(closeOrderUrl, XMLUtil.StringToXml(reqData), String.class);
            log.info("订单关闭返回数据 -> {}",rstStr);
            MiniPayOrderCloseResult result = XMLUtil.convertToObject(rstStr, MiniPayOrderCloseResult.class);
            if (null != result && SUCCESS.equalsIgnoreCase(result.getResultCode())) {
                return ResponseResult.buildSuccessResponse(result);
            }
            return ResponseResult.buildFailResponse(result.getErrCode(), result.getResultMsg());
        } catch (Exception e) {
            log.error("关闭订单", e);
        }
        return ResponseResult.buildFailResponse(REFUND_QUERY_EXCEPTION.code(), REFUND_QUERY_EXCEPTION.message());
    }

    /**
     * 下载对账单
     *
     * @param billRequest
     * @return
     */
    @Override
    public ResponseResult<MiniPayBillResult> downloadBill(MiniBillRequest billRequest) {
        if (StringUtils.isEmpty(billRequest.getBillType())) {
            billRequest.setBillType(MiniPayConstants.BillType.ALL);//默认所有
        }
        String validMsg = BeanFieldsUtils.checkRequiredFields(billRequest);
        if (StringUtils.isNotEmpty(validMsg)) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), validMsg);
        }

        try {
            String downloadbillUrl = DOWNLOADBILL_URL();
            // 填装请求参数
            WXPayUtil.fillRequestData(billRequest);

            // 请求统一下单接口
            String rstStr = restTemplate().postForObject(downloadbillUrl, billRequest.toXML(), String.class);
            log.info("下载对账单 -> {}", rstStr);
            // 请求下载对账单接口
            MiniPayBillResult payBillResult = WXPayUtil.parseBillText(rstStr, MiniPayConstants.BillType.ALL);
            return ResponseResult.buildSuccessResponse(payBillResult);
        } catch (Exception e) {
            log.error("", e);
            return ResponseResult.buildFailResponse(DOWNLOADBILL_EXCEPTION.code(), DOWNLOADBILL_EXCEPTION.message());
        }
    }

    /**
     * 生产小程序二维码
     *
     * @param qrcodeRequest
     * @return
     */
    @Override
    public ResponseResult<File> createWxaCodeUnlimit(MiniQrcodeRequest qrcodeRequest) {
        String accessToken = getAccessToken(qrcodeRequest.getBusinessLine());
        log.info("生成小程序二维码 accessToken -> {}",accessToken);
        if (StringUtils.isBlank(accessToken)) {
            ResponseResult<String> result = refreshAccessToken(qrcodeRequest.getBusinessLine());
            if (result.isSuccess()){
                accessToken = result.getData();
            }
            if (StringUtils.isBlank(accessToken)){
                log.info("微信小程序 accessToken 为空");
                return ResponseResult.buildFailResponse(ACCESSTOKEN_EMPTY.code(), ACCESSTOKEN_EMPTY.message());
            }
        }
        try {
            String validMsg = BeanFieldsUtils.checkRequiredFields(qrcodeRequest);
            if (StringUtils.isNotEmpty(validMsg)) {
                return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), validMsg);
            }
            String createWxaCodeUrl = GETWXACODEUNLIMIT_URL();
            createWxaCodeUrl = String.format(createWxaCodeUrl, accessToken);
            log.info("createWxaCodeUrl -> {}",createWxaCodeUrl);

//            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
//            HttpEntity requestEntity = new HttpEntity(qrcodeRequest.toMap(), headers);
//            ResponseEntity<byte[]> entity = restTemplate.exchange(createWxaCodeUrl, HttpMethod.POST, requestEntity, byte[].class, new Object[0]);
//            byte[] result = entity.getBody();
//            InputStream inputStream = new ByteArrayInputStream(result);
//            log.info("result -> {}",result);
//            File qrcodeFile = File.createTempFile(UUID.randomUUID().toString(), ".jpg");
//            inputstreamToFile(inputStream,qrcodeFile);
//            log.info("file path -> {}",qrcodeFile.getAbsolutePath());

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="+accessToken);  // 接口
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
            String body = JSON.toJSONString(qrcodeRequest.toMap());           //必须是json模式的 post
            StringEntity entity = new StringEntity(body);
            entity.setContentType("image/png");

            httpPost.setEntity(entity);
            HttpResponse response;
            response = httpClient.execute(httpPost);
            InputStream inputStream = response.getEntity().getContent();
            File qrcodeFile = File.createTempFile(UUID.randomUUID().toString(), ".png");
            inputstreamToFile(inputStream,qrcodeFile);
            log.info("file path -> {}",qrcodeFile.getAbsolutePath());
            return ResponseResult.buildSuccessResponse(qrcodeFile);
//            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.add("Content-Disposition", "attachment;filename=" + new String((qrcodeRequest.getScene() + ".jpg").getBytes()));
//            ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(entity.getBody(), httpHeaders, HttpStatus.OK);
            //return ResponseResult.buildSuccessResponse(responseEntity);
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildFailResponse();
    }

    @Override
    public ResponseResult<String> queryAccessToken(Byte buzLine) {
        String accessToken = getAccessToken(buzLine);
        if (StringUtils.isNotBlank(accessToken)){
            return ResponseResult.buildSuccessResponse(accessToken);
        }
        return ResponseResult.buildFailResponse();
    }

    private String getAccessToken(Byte buzLine){
        String accessToken = null;
        if (buzLine != null){
            accessToken = RedisCache.get(MINI_APP_ACCESS_TOKEN+":"+buzLine.intValue());
            if (StringUtils.isBlank(accessToken) && BusinessLine.LUNCH.getCode().equals(buzLine)){//午餐
                //兼容午餐之前服务
                accessToken = RedisCache.get(MINI_APP_ACCESS_TOKEN);
            }
        }else {
            accessToken = RedisCache.get(MINI_APP_ACCESS_TOKEN);
        }
        if (StringUtils.isBlank(accessToken)){//补偿刷新accesstoken
            ResponseResult<String> result = refreshAccessToken(buzLine);
            accessToken = result.getData();
        }
        return accessToken;
    }

    private RestTemplate restTemplate() {
        // 添加内容转换器
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }


    private Map<String, String> queryContractNo(SortedMap<String, String> signParams, String signValue) {
        String url = WechatUrl.CONTRACT_URL();
        String xmlParam = XMLUtil.StringToXml(signParams, signValue);
        String result = restTemplate.postForObject(url, xmlParam, String.class);
        return parseResult(result);
    }

    private Map<String, String> parseResult(String result) {
        Map<String, String> map = XMLUtil.doXMLParse(result);
        if (!StringUtils.equals(SUCCESS, map.get("result_code"))) {
            String errorCode = map.get("err_code");
            String errorMsg = map.get("err_code_des");
            if (StringUtils.isBlank(errorCode)) {
                String returnCode = map.get("return_code");
                String returnMsg = map.get("return_msg");
                map.put("returnCode", returnCode);
                map.put("returnMsg", returnMsg);
                return map;
            } else {
                map.put("returnCode", errorCode);
                map.put("returnMsg", errorMsg);
                return map;
            }
        }
        return map;
    }

    private static void inputstreamToFile(InputStream ins, File file) throws IOException {
        if (file == null) {
            file = new File("", "");
        }
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        while ((bytesRead = ins.read(buffer, 0, 1024)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.close();
        ins.close();
    }

}