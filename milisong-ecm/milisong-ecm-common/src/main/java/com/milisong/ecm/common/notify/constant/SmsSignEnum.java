package com.milisong.ecm.common.notify.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/12/3   17:22
 *    desc    : 业务线枚举
 *    version : v1.0
 * </pre>
 */
@Getter
@AllArgsConstructor
public enum SmsSignEnum {

    LUNCH((byte)2, "L", "午餐"),
    BREAKFAST((byte)3, "B", "早餐"),
    ;

    private byte val;

    private String code;

    private String desc;

    public static SmsSignEnum getEnum(byte val){
        SmsSignEnum[] enums = SmsSignEnum.values();
        for (SmsSignEnum e:enums) {
            if(Byte.toString(e.getVal()).equals(Byte.toString(val))){
                return e;
            }
        }
        return null;
    }
}
