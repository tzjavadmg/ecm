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
public enum  BusinessLineEnum {

    BREAKFAST((byte)0, "B", "早餐"),
    LUNCH((byte)1, "L", "午餐"),
    AFTERNOON_TEA((byte)2, "A", "下午茶"),
    SUPPER((byte)3, "S", "晚餐"),
    MIDNIGHT_SNACK((byte)4, "M", "夜宵"),
    ;

    private byte val;

    private String code;

    private String desc;

    public static BusinessLineEnum getEnum(byte val){
        BusinessLineEnum[] enums = BusinessLineEnum.values();
        for (BusinessLineEnum e:enums) {
            if(Byte.toString(e.getVal()).equals(Byte.toString(val))){
                return e;
            }
        }
        return null;
    }
}
