package com.milisong.pms.prom.enums;

/**
 * @author sailor wang
 * @date 2019/5/22 10:57 AM
 * @description
 */
public enum SystemModule {
    SYS_PMS("pms"),
    MOD_GROUPBUY("groupbuy");

    private String value;

    public String getValue() {
        return this.value;
    }

    private SystemModule(final String value) {
        this.value = value;
    }
}