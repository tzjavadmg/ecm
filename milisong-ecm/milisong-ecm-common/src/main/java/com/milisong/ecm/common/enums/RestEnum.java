package com.milisong.ecm.common.enums;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/9/4   11:36
 *    desc    : 响应层枚举
 *    version : v1.0
 * </pre>
 */

public enum RestEnum {
    //-------------30500-30599----------
    SYS_ERROR("30500","服务器繁忙...");
    ;

    private String code;
    private String desc;
    RestEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }
    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
