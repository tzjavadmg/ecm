package com.milisong.wechat.miniapp.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.wechat.miniapp.dto.*;

import java.io.File;
import java.util.Map;


/**
 * 小程序交互接口
 *
 * @author sailor wang
 * @date 2018/8/31 下午2:45
 * @description
 */
public interface MiniAppService {

    /**
     * 登录凭证校验
     *
     * @param loginRequest
     * @return
     */
    ResponseResult<MiniSessionDto> login(MiniLoginRequest loginRequest);

    /**
     * 校验签约号
     *
     * @param contractRequest
     * @return
     */
    ResponseResult<MiniContractDto> validateContractNo(MiniContractRequest contractRequest);

    /**
     * 代扣支付
     *
     * @param paymentRequest
     * @return
     */
    ResponseResult<MiniPaymentResult> pappayapply(MiniPaymentRequest paymentRequest);

    /**
     * 获取手机号
     *
     * @param sessionkey
     * @param encryptedData
     * @param iv
     * @return
     */
    ResponseResult<String> getMobile(String sessionkey, String encryptedData, String iv);

    /**
     * 签约
     *
     * @param miniContractRequest
     * @return
     */
    ResponseResult<Map<String, Object>> signContract(MiniContractRequest miniContractRequest);

    /**
     * 查询订单(详见https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_2)
     *
     * @param orderQueryRequest    商户系统内部的订单号，当没提供transactionId时需要传这个。
     */
    ResponseResult<PayOrderQueryResult> queryOrder(MiniOrderQueryRequest orderQueryRequest);

    /**
     * 发送模板消息
     *
     * @param msgTemplate
     * @return
     */
    ResponseResult sendTemplateMsg(MiniMsgTemplateDto msgTemplate);

    /**
     * 刷新access_token
     *
     * @return
     */
    ResponseResult<String> refreshAccessToken(Byte buzLine);

    /**
     * 主动支付接口
     *
     * @param request
     * @return
     */
    ResponseResult<PayUnifiedOrderDto> unifiedOrder(PayUnifiedOrderRequest request);

    /**
     * 小程序申请退款
     *
     * @param request 请求对象
     * @return 退款操作结果 wx pay refund result
     */
    ResponseResult<MiniPayRefundResult> refund(MiniPayRefundRequest request);

    /**
     * 查询退款数据
     *
     * @param refundQueryRequest
     * @return
     */
    ResponseResult<MiniRefundQueryResult> refundQuery(MiniRefundQueryRequest refundQueryRequest);

    /**
     * 关闭订单
     *
     * @param orderCloseRequest
     * @return
     */
    ResponseResult<MiniPayOrderCloseResult> closeOrder(MiniPayOrderCloseRequest orderCloseRequest);

    /**
     * 下载对账单
     *
     * @param billRequest
     * @return
     */
    ResponseResult<MiniPayBillResult> downloadBill(MiniBillRequest billRequest);

    /**
     * 生产小程序二维码
     *
     * @param qrcodeRequest
     * @return
     */
    ResponseResult<File> createWxaCodeUnlimit(MiniQrcodeRequest qrcodeRequest);

	ResponseResult<String> queryAccessToken(Byte buzLine);
}