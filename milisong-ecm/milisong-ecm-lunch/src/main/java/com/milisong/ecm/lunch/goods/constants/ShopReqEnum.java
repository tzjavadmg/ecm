package com.milisong.ecm.lunch.goods.constants;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/9/4   11:20
 *    desc    : 门店信息枚举
 *    version : v1.0
 * </pre>
 */

public enum ShopReqEnum {
    //------------范围30200-30299---------------
    SHOP_GETINFO_FROM_SCM_ERROR("30200","从SCM获取门店信息失败"),
    SHOP_GOODS_INFO_NULL("30201","拿取门店商品信息为空"),
    SHOP_GOODS_BYCODE_INFO_NULL("30202","没有查询到拿取门店中指定商品"),
    ;

    private String code;
    private String desc;
    ShopReqEnum(String code,String desc){
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
