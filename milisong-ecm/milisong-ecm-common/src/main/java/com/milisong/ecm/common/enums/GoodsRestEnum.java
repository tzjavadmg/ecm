package com.milisong.ecm.common.enums;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/9/4   11:36
 *    desc    : 商品信息响应层枚举
 *    version : v1.0
 * </pre>
 */

public enum GoodsRestEnum {
    //-------------30300-30499----------
    SHOP_INFO_ERROR("30300","门店信息异常");
    ;

    private String code;
    private String desc;
    GoodsRestEnum(String code,String desc){
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
