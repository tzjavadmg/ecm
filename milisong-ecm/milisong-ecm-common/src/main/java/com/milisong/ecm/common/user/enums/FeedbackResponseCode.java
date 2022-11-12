package com.milisong.ecm.common.user.enums;

/**
 * @author sailor wang
 * @date 2018/8/31 下午3:34
 * @description
 */
public enum FeedbackResponseCode {
    REQUEST_PARAM_EMPTY("60001", "请求参数为空", "请求参数为空"),
    QUERY_FEEDBACK_TYPE_EXCEPTION("60007", "查询反馈类型异常", "查询反馈类型异常"),
    SAVE_FEEDBACK_TYPE_EXCEPTION("60008", "保存反馈类型异常", "保存反馈类型异常"),
    ;


    private final String code;

    private final String message;

    private final String detailMessage;

    FeedbackResponseCode(String code, String message, String detailMessage) {
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
