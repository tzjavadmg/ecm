package com.milisong.pms.prom.enums;

/**
 * @author sailor wang
 * @date 2018/10/26 2:24 PM
 * @description
 */
public enum QrcodeType implements KeyValEnum {
    PERSONAL((byte) 1, "个人"),
    LEAFLET((byte) 2, "宣传单"),
    PROMOTION((byte) 3, "营销活动"),
    ;

    private Byte code;
    private String msg;

    QrcodeType(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Byte getCode() {
        return code;
    }

    public void setCode(Byte code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static String geneQrcode(Byte type, String count) {
        for (QrcodeType qrcodeType : QrcodeType.values()) {
            if (qrcodeType.getCode().equals(type)) {
                return qrcodeType.getCode() + count;
            }
        }
        return "";
    }
}