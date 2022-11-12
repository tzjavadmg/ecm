package com.milisong.wechat.miniapp.enums;

/**
 * @author sailor wang
 * @date 2018/8/31 下午3:34
 * @description
 */
public enum MiniAppResponseCode {
    SUCCESS("72000", "操作成功", "请求操作成功"),
    REQUEST_PARAM_EMPTY("72001", "请求参数为空", "请求参数为空"),
    QUERY_CONTRACT_EXCEPTION("72002", "签约号查询异常", "签约号查询异常"),
    PAPPAYAPPLY_PARAM_EMPTY("72003", "代扣参数不合法", "代扣参数不合法"),
    PAPPAYAPPLY_EXCEPTION("72004", "代扣失败", "代扣失败"),
    QUERY_MOBILE_FAIL("72005", "获取手机号失败", "获取手机号失败"),
    SIGN_CONTRACT_EXCEPTION("72006", "签约异常", "签约异常"),
    QUERY_ORDER_EXCEPTION("72007", "订单查询异常", "订单查询异常"),
    ACCESSTOKEN_EMPTY("72008", "accesstoken为空", "accesstoken为空"),
    UNIFIEDORDER_EXCEPTION("72009", "主动支付异常", "主动支付异常"),
    REFUND_EXCEPTION("72010", "退款异常", "退款异常"),
    DOWNLOADBILL_EXCEPTION("72011", "对账单下载异常", "对账单下载异常"),
    REFUND_QUERY_EXCEPTION("72012", "退款查询异常", "退款查询异常"),

    ;


    private final String code;

    private final String message;

    private final String detailMessage;

    MiniAppResponseCode(String code, String message, String detailMessage) {
        this.code = code;
        this.message = message;
        this.detailMessage = detailMessage;
    }

    public String code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    public String detailMessage() {
        return this.detailMessage;
    }
}
