package com.milisong.pms.prom.enums;

/**
 * 活动类型
 *
 * @author sailor wang
 * @date 2018/9/13 下午1:49
 * @description
 */
public enum UserActivityType implements KeyValEnum {
    MULTI_SHARE_COUPON((byte)1, "多人分享券活动"),
    OLD_PULL_NEW((byte)2, "老拉新邀请活动"),
    SCAN_QRCODE((byte)3, "扫二维码活动"),
    GROUP_BUY((byte)4, "拼团活动"),
    ;

    private Byte code;
    private String msg;

    UserActivityType(Byte code, String msg) {
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