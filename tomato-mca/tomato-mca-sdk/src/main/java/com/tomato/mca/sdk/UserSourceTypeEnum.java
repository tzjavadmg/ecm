package com.tomato.mca.sdk;

/**
 * 用户-消息用户来源（0微信 1 方塘 2支付宝）
 * 注意和mms-api保持一致，后续会改造
 */
public enum UserSourceTypeEnum {
    WECHAT(0, "WX"),
    FOUNTOWN(1, "FTOWN"),
    ALIPAY(2, "ALIPAY"),
    WXM(3,"WXM"),
    DINGTALK(4,"DINGTALK"),
    ;

    private Integer code;
    private String desc;

    private UserSourceTypeEnum(Integer key, String value) {
        this.code = key;
        this.desc = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static UserSourceTypeEnum getEnumByDesc(String targetDesc) {
        for (UserSourceTypeEnum productEnum : UserSourceTypeEnum.values()) {
            if (productEnum.desc.equalsIgnoreCase(targetDesc)) {
                return productEnum;
            }
        }
        return null;
    }


}
