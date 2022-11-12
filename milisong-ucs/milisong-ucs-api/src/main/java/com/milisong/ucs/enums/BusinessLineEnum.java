package com.milisong.ucs.enums;

/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/12/3   10:55
 *    desc    : 业务线枚举
 *    version : v1.0
 * </pre>
 */

public enum BusinessLineEnum {

    BREAKFAST((byte)0,      "B","早餐"),
    LUNCH((byte)1,          "L","午餐"),
    AFTERNOON_TEA((byte)2,  "A","下午茶"),
    SUPPER((byte)3,         "S","晚餐"),
    MIDNIGHT_SNACK((byte)4, "M","夜宵"),
    ;

    private Byte code;

    private String msg;

    private String desc;

    BusinessLineEnum(Byte code, String msg, String desc) {
        this.code = code;
        this.msg = msg;
        this.desc = desc;
    }

    public Byte getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getDesc() {
        return desc;
    }
}
