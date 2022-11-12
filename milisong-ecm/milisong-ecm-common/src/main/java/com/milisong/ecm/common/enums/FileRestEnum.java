package com.milisong.ecm.common.enums;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/9/4   14:50
 *    desc    : 文件上传响应层枚举
 *    version : v1.0
 * </pre>
 */

public enum FileRestEnum {
    //----------30700-30755
    FILE_FORMART_ERROR("30700","文件格式错误"),
    FILE_SIZE_ERROR("30701","文件大小超出限制范围"),
    FILE_OSS_ERROR("30702","文件上传OSS失败"),

    ;
    private String code;
    private String desc;
    FileRestEnum(String code,String desc){
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
