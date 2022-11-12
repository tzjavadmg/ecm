package com.milisong.breakfast.scm.configuration.constant;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/24   14:22
 *    desc    : 配置业务枚举
 *    version : v1.0
 * </pre>
 */

public enum ConfigEnum {

    GLOBAL_CONFIG_EXITS("20001","全局配置已经存在"),
    GLOBAL_KEY_BLANK("20002","全局配置键不能为空"),
    GLOBAL_VAL_BLANK("20003","全局配置值不能为空"),
    GLOBAL_TYPE_BLANK("20004","全局配置类型不能为空"),
    GLOBAL_DESC_BLANK("20005","全局配置描述不能为空"),
    GLOBAL_SHOP_BLANK("20006","全局配置门店信息不能为空"),
    GLOBAL_KEY_NOT_CHANGE("20007","全局配置键不允许修改"),

    SHOP_INTERCEPT_SHOPID_BLANK("20010","门店配置缺少门店信息"),
    SHOP_INTERCEPT_TIME_BLANK("20011","门店配置缺少截单时间"),
    SHOP_ORDERSET_FIRST_TIME_BLANK("20012","门店配置缺少第一次集单时间"),
    SHOP_ORDERSET_SECOND_TIME_BLANK("20013","门店配置缺少第二次集单时间"),
    SHOP_DELIVERY_TIME_BEGIN_BLANK("20014","门店配置缺少配送开始时间"),
    SHOP_DELIVERY_TIME_END_BLANK("20015","门店配置缺少配送结束时间"),
    SHOP_MAX_OUTPUT_BLANK("20016","门店配置缺少最大生产量"),
    SHOP_MAX_OUTPUT_NOT_ZERO("20016","门店配置最大生产量不能小于零"),
    SHOP_IS_DEFAULT_BLANK("20017","门店配置缺少默认配送设置"),
    SHOP_IS_DEFAULT_EXIST("20018","门店已经存在默认配送，请确认"),
    SHOP_INTERCEPT_FIRST_ORDERSET_ERROR("20019","门店截单时间不能大于集单时间"),
    SHOP_FIRST_SECOND_ORDERSET_ERROR("20020","门店第二次集单不能小于第一次集单时间"),
    SHOP_SECOND_ORDERSET_BEGIN_ERROR("20021","门店配送开始时间不能小于第二次集单时间"),
    SHOP_BEGIN_END_ERROR("20022","门店配送结束时间不能小于开始时间"),
    SHOP_BEGIN_END_CONFLICT("20023","门店已经存在同样的配送时间段"),
    SHOP_INTERCEPT_EXISTS("20024","门店截单时间已经存在"),
    SHOP_INTERCEPT_NOT_DELETE("20025","门店截单时间不允许删除"),
    SHOP_ORDERSET_EXISTS("20026","门店集单时间已经存在"),

    SHOP_BANNER_WEIGHT_EXIST("20030","门店Banner图已经存在这个权重值"),
    SHOP_BANNER_SHOP_MSG_BLANK("20031","门店Banner图缺少门店信息"),
    SHOP_BANNER_PICTURE_BLANK("20032","门店Banner缺少轮播图信息"),
    SHOP_BANNER_LINK_URL_BLANK("20033","门店Banner缺少链接地址信息"),
    SHOP_BANNER_WEIGHT_BLANK("20034","门店Banner图缺少权重信息"),

    SHOP_SINGLE_KEY_EXISTS("20040","门店对应此配置已经存在"),
    SHOP_SINGLE_KEY_BLANK("20041","门店配置键不能为空"),
    SHOP_SINGLE_VAL_BLANK("20042","门店配置值不能为空"),
    SHOP_SINGLE_TYPE_BLANK("20043","门店配置类型不能为空"),
    SHOP_SINGLE_DESC_BLANK("20044","门店配置描述不能为空"),
    SHOP_SINGLE_SHOP_BLANK("20045","门店配置门店信息不能为空"),
    SHOP_SINGLE_KEY_NOT_CHANGE("20046","门店配置键不允许修改"),
    SHOP_SINGLE_KEY_NOT_FOUND("20047","门店配置查不到"),
    SHOP_SINGLE_MAXPRODUCTION_NOT_ZERO("20048","门店最大生产量不能小于0"),

    SHOP_NOT_FOUND("20049","门店没有查询到"),

    SERVICE_LINE_BLANK("20050","业务线属性不能为空"),
    SERVICE_LINE_NOT_FOUND("20051","没有对应的业务线"),
    SHOP_INTERCEPT_DISPATCH_NULL("20052","门店派单时间为空"),
    SHOP_INTERCEPT_DISPATCH_EXISTS("20053","门店已经存在同样的派单时间了"),
    SHOP_DISPATCH_BEFORE_ORDERSET_EXISTS("20054","派单时间不能小于集单时间"),


    ;

    ConfigEnum(String code, String desc){
        this.code =code;
        this.desc = desc;
    }

    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
