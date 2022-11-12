package com.milisong.pms.prom.enums;

/**
 * @author sailor wang
 * @date 2018/12/11 1:50 PM
 * @description
 */
public enum SendCouponStatus {
    WAIT_FOR_SEND((byte) 0, "待发送"),
    SEND_SUCCESS((byte) 1, "发送成功"),
    SEND_FAILED((byte) 2, "发送失败"),
    ;

    SendCouponStatus(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Byte code;
    private String desc;

    public Byte getCode() {
        return code;
    }

    public void setCode(Byte code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}