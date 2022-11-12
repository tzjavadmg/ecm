package com.milisong.breakfast.pos.constant;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/23   19:04
 *    desc    : 配置枚举定义
 *    version : v1.0
 * </pre>
 */

public enum ConfigConstant {

    GLOBAL_CONFIG("global_input","全局配置"),
    BANNER_CONFIG("banner_input","banner图配置"),
    INTERCEPT_CONFIG("intercept_input","截单配置"),
    SHOP_SINGLE_CONFIG("single_input","单个门店配置"),
        ;

    private String value;
    private String desc;

    ConfigConstant(String value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public static ConfigConstant getConstantByValue(String value){
        for (ConfigConstant configConstant : ConfigConstant.values()) {
            if (configConstant.value.equals(value)) {
                return configConstant;
            }
        }
        return GLOBAL_CONFIG;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
