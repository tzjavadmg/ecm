package com.milisong.breakfast.scm.configuration.constant;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/12/18   17:08
 *    desc    : 全局配置类型枚举定义
 *    配置值的类型 1字符串 2整数 3小数 4日期 5时间 6日期+时间,7图片
 *    version : v1.0
 * </pre>
 */

public enum GlobalConfigTypeEnum {

    STRING(new Byte("1"),"字符串"),
    INTEGER(new Byte("2"),"整数"),
    FLOAT(new Byte("3"),"小数"),
    DATE(new Byte("4"),"日期"),
    TIME(new Byte("5"),"时间"),
    DATE_AND_TIME(new Byte("6"),"时间和日期"),
    PIC(new Byte("7")," 图片"),
    ;

    private Byte val;

    private String desc;

    public Byte getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }

    GlobalConfigTypeEnum(Byte val, String desc) {
        this.val = val;
        this.desc = desc;
    }
}
