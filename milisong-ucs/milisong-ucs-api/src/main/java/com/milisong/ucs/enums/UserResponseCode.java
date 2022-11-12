package com.milisong.ucs.enums;

/**
 * @author sailor wang
 * @date 2018/8/31 下午3:34
 * @description
 */
public enum UserResponseCode {
    SUCCESS("60000", "操作成功", "请求操作成功"),
    REQUEST_PARAM_EMPTY("60001", "请求参数为空", "请求参数为空"),
    USER_LOGIN_EXCEPTION("60002", "用户jscode鉴权失败", "用户jscode鉴权失败"),
    QUERY_USER_INFO_EXCEPTION("60003", "获取用户信息异常", "获取用户信息异常"),
    SAVE_USER_ADDR_EXCEPTION("60004", "用户地址保存失败", "用户地址保存失败"),
    UPDATE_USER_ADDR_EXCEPTION("60005", "修改用户地址失败", "修改用户地址失败"),
    QUERY_USER_ADDR_EXCEPTION("60006", "查询用户地址异常", "查询用户地址异常"),
    REDIS_CONFIG_DATA_NOT_EXISTS("60009", "REDIS配置数据不存在", "REDIS配置数据不存在"),
    ALREADY_EXECUTE("60010", "已执行过", "已执行过"),
    ADD_MINIAPP_TIPS_EXCEPTION("60012", "引导添加小程序异常", "引导添加小程序异常"),
    BUSINESS_LINE_IS_EMPTY("60013", "业务线参数为空", "业务线参数为空"),

    SAVE_PARAM_INVALID("60014", "保存数据不合法，请检查", "业务参数为空"),

    ;


    private final String code;

    private final String message;

    private final String detailMessage;

    UserResponseCode(String code, String message, String detailMessage) {
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
