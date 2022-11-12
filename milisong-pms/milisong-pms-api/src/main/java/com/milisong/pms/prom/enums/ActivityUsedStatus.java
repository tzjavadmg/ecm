package com.milisong.pms.prom.enums;

/**
 * 活动使用状态
 *
 * @author sailor wang
 * @date 2018/9/13 下午1:49
 * @description
 */
public enum ActivityUsedStatus implements KeyValEnum {
    UNUSED((byte) 0, "未使用"), USED((byte)1, "已使用");

    private Byte code;
    private String msg;

    ActivityUsedStatus(Byte code, String msg) {
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