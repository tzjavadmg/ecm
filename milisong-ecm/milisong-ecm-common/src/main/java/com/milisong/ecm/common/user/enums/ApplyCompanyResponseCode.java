package com.milisong.ecm.common.user.enums;

/**
 * @author tianhaibo
 * @date 2019/1/18 下午3:34
 * @description
 */
public enum ApplyCompanyResponseCode {

    INVALID_ITEM_NAME("60001", "缺少名称"),
    INVALID_TELE("60007", "缺少联系方式"),
    INVALID_BUSINESS_LINE("60008", "缺少业务线"),
    ;


    private final String code;

    private final String message;

    ApplyCompanyResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}
