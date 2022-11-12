package com.milisong.scm.shop.constant;

/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/11/7   13:54
 *    desc    : 门店业务响应枚举
 *    version : v1.0
 * </pre>
 */

public enum ComponyResponseEnum {

    COMPONY_NEED_ID("20001","缺少公司id信息"),
    COMPONY_NOT_FOUND("20002","没有查询到此公司信息"),
    ;
    private String code;

    private String desc;

    ComponyResponseEnum(String code, String desc){
        this.code =code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
