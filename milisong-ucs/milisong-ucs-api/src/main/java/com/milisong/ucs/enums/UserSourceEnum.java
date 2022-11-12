package com.milisong.ucs.enums;

/**
 * @author sailor wang
 * @date 2018/8/31 下午10:07
 * @description
 */
public enum UserSourceEnum implements KeyValEnum {
    MINI_APP(0,"微信小程序"),
    ;

    private Integer code;
    private String msg;

    UserSourceEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}