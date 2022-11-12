package com.milisong.scm.base.constant;

/**
 * <pre>
 *    author  : Administrator
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/11/7   13:54
 *    desc    : 门店业务响应枚举
 *    version : v1.0
 * </pre>
 */

public enum  ShopResponseEnum {

    SHOP_NAME_BLANK("20001","门店名称为空"),
    SHOP_ADDRESS_BLANK("20002","门店地址为空"),
    SHOP_STATUS_BLANK("20003","请选择门店状态"),
    SHOP_NAME_EXISTS("20004","此门店名称已存在"),
    ;
    private String code;

    private String desc;

    ShopResponseEnum(String code,String desc){
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
