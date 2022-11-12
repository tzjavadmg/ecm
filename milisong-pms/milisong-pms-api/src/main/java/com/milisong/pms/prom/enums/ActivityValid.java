package com.milisong.pms.prom.enums;

/**
 * 活动使用状态
 *
 * @author sailor wang
 * @date 2018/9/13 下午1:49
 * @description
 */
public enum ActivityValid implements KeyValEnum {
    INVALID((byte) 0, "无效"), VALID((byte)1, "有效");

    private Byte code;
    private String msg;

    ActivityValid(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Byte getCode() {
        return this.code;
    }

    @Override
    public Object getMsg() {
        return this.msg;
    }
}