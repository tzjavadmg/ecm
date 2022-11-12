package com.milisong.ecm.common.notify.constant;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/9/4   13:59
 *    desc    : 订单响应层枚举
 *    version : v1.0
 * </pre>
 */

public enum OrderRestEnum {
    //----------------30600-30699
    ORDER_NOT_EXISTS("30600","订单信息不存在"),
    ORDER_SMS_NOTIFY_FAILED("30601","订单送达信息通知失败"),
    ORDER_UNION_ORDER_GET_FAILED("30602","集单信息明细获取失败"),
    ORDER_UNION_ORDER_NULL("30603","没有查询到集单号信息"),
    ORDER_UNION_ORDER_HAS_NOTICED("30604","订单送达通知已经下发，请勿重复操作"),
    ORDER_UNION_ORDER_STATUS_ERROR("30605","订单状态不合法，请检查确认"),
    ORDER_IVR_NOTIFY_FAILED("30606","拨打IVR电话失败，请检查确认"),
    ORDER_IVR_NOTIFY_RETRY_FAILED("30607","拨打IVR电话失败,再次发送短信仍然失败，请检查确认"),

    ORDER_NOTIFY_STATUS_ILLEGAL("30608","集单状态非法，不发送送达通知，请检查确认"),
    ;

    private String code;
    private String desc;

    OrderRestEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
