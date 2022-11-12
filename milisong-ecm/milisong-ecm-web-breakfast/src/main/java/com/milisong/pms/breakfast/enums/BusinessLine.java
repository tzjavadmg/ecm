package com.milisong.pms.breakfast.enums;

/**
 * @author sailor wang
 * @date 2018/12/3 3:04 PM
 * @description
 */
public enum BusinessLine {
    BREAKFAST((byte) 0, "B"),
    LUNCH((byte) 1, "L"),
    AFTERNOON_TEA((byte) 2, "A"),
    SUPPER((byte) 3, "S"),
    MIDNIGHT_SNACK((byte) 4, "M")
    ;

    BusinessLine(Byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private Byte code;
    private String desc;

    public Byte getCode() {
        return code;
    }

    public void setCode(Byte code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}