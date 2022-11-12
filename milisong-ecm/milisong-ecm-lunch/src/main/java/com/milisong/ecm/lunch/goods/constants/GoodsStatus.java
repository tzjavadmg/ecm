package com.milisong.ecm.lunch.goods.constants;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/9/5   15:06
 *    desc    : 商品状态枚举
 *    version : v1.0
 * </pre>
 */

public enum  GoodsStatus {
    DETAIL_STATUS_COMING_SOON(1,"即将上线"),
    DETAIL_STATUS_USING(2,"使用中"),
    DETAIL_STATUS_TOD_DOWNLINE(3,"今日下线"),
    DETAIL_STATUS_TOM_DOWNLINE(4,"明日下线"),
    DETAIL_STATUS_DOWNLINE(5,"已下线"),
    ;
    private Integer value;
    private String desc;
    GoodsStatus(Integer value,String desc){
        this.desc = desc;
        this.value = value;
    }
    public String getDesc() {
        return desc;
    }

    public Integer getValue() {
        return value;
    }
}
