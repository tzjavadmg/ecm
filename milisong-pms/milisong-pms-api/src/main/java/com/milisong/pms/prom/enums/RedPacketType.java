package com.milisong.pms.prom.enums;

/**
 * @author sailor wang
 * @date 2018/9/21 下午2:38
 * @description
 */
public enum RedPacketType implements KeyValEnum {
    SHARE_REDPACKET((byte) 1, "分享红包"),
    NEW_REDPACKET((byte) 2, "新人红包"),
    CUT_REDPACKET((byte) 3, "砍红包"),
    ACTIVIE_REDPACKET((byte) 4, "促活红包"),
    ;

    private Byte code;

    private String msg;

    RedPacketType(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Byte getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}