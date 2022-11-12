package com.milisong.ucs.enums;

/**
 * @author sailor wang
 * @date 2018/9/27 下午2:10
 * @description
 */
public enum LoginSource implements KeyValEnum {
    INDEX_LOGIN(0,"首页登录"),
    MULTI_SHARE_REDPACKET_LOGIN(1,"多人分享红包登录"),
    ;

    LoginSource(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;

    private String msg;

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}