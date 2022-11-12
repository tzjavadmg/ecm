package com.milisong.scm.shop.constant;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/23   19:04
 *    desc    : 配置枚举定义
 *    version : v1.0
 * </pre>
 */

public enum  ConfigConstant {

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

    public static enum SERVICE_LINE{

        //业务线：1-ecm，2-scm，3-pos，0-common
        COMMON("0","common","各业务线通用配置"),
        ECM("1","ecm","ECM业务线配置"),
        SCM("2","scm","SCM业务线配置"),
        POS("3","pos","POS业务线配置"),
        PMS("4","pms","PMS业务线配置"),
        ;
        private String value;
        private String lineName;
        private String desc;

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

        public String getLineName() {
            return lineName;
        }

        SERVICE_LINE(String value, String lineName, String desc) {
            this.value = value;
            this.lineName = lineName;
            this.desc = desc;
        }

        public static SERVICE_LINE getConstantByValue(String value){
            for (SERVICE_LINE configConstant : SERVICE_LINE.values()) {
                if (configConstant.value.equals(value)) {
                    return configConstant;
                }
            }
            return null;
        }
    }

    public static enum OPERATE{
        ADD("add","新增操作"),
        UPDATE("update","更新操作"),
        DELETE("delete","删除操作"),
        SYNC("sync","全量同步操作"),
        ;
        private String type;
        private String desc;

        OPERATE(String type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        public String getType() {
            return type;
        }

        public String getDesc() {
            return desc;
        }
    }

    public static enum CONFIG_TYPE{

        STRING(1,"字符串"),
        INTEGER(2,"整数"),
        FLOAT(3,"小数"),
        DATE(4,"日期"),
        TIME(5,"时间"),
        DATE_TIME(6,"日期+时间"),
        ;

        CONFIG_TYPE(Integer type, String desc) {
            this.type = type;
            this.desc = desc;
        }

        private Integer type;
        private String desc;

        public Integer getType() {
            return type;
        }

        public String getDesc() {
            return desc;
        }
    }
}
