package com.milisong.wechat.miniapp.constant;

/**
 * 接口地址
 * @author sailor wang
 * @date 2018/8/31 下午3:34
 * @description
 */
public class WechatUrl {

    // 登录凭证校验请求地址
    private final static String MINIAPP_LOGIN_URL="https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    // 签约查询
    private final static String MINIAPP_CONTRACT_URL = "https://api.mch.weixin.qq.com/papay/querycontract";

    //代扣地址
    private final static String PAPPAYAPPLY_URL = "https://api.mch.weixin.qq.com/pay/pappayapply";

    //订单查询
    private final static String QUERY_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

    //刷新access_token
    private final static String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    //发送模板消息
    private final static String SEND_TEMPLATE_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=";

    //主动支付下单接口
    private final static String PAY_UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    //退款接口
    private final static String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    //下载对账单
    private final static String DOWNLOADBILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";

    //下载二维码
    private final static String GETWXACODEUNLIMIT_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";

    //退款查询
    private final static String REFUND_QUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";

    //关闭订单
    private final static String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";

    public static String LOGIN_URL(String appid,String secret,String jsCode){
        return String.format(MINIAPP_LOGIN_URL,appid,secret,jsCode);
    }

    public static String CONTRACT_URL(){
        return MINIAPP_CONTRACT_URL;
    }

    public static String PAPPAYAPPLY_URL(){
        return PAPPAYAPPLY_URL;
    }

    public static String QUERYORDER_URL() {
        return QUERY_ORDER_URL;
    }

    public static String ACCESSTOKEN_URL(String appid,String secret) {
        return String.format(GET_ACCESS_TOKEN_URL,appid,secret);
    }

    public static String TEMPLATE_MSG_URL(){
        return SEND_TEMPLATE_MSG_URL;
    }

    public static String PAY_UNIFIEDORDER_URL(){
        return PAY_UNIFIEDORDER_URL;
    }

    public static String REFUND_URL(){
        return REFUND_URL;
    }

    public static String REFUND_QUERY_URL(){
        return REFUND_QUERY_URL;
    }

    public static String CLOSE_ORDER_URL(){
        return CLOSE_ORDER_URL;
    }

    public static String DOWNLOADBILL_URL(){
        return DOWNLOADBILL_URL;
    }

    public static String GETWXACODEUNLIMIT_URL(){
        return GETWXACODEUNLIMIT_URL;
    }
}