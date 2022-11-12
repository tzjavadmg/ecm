package com.milisong.pms.prom.enums;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/10   16:17
 *    desc    : MQ记录 类型枚举
 *    version : v1.0
 * </pre>
 */

public enum MQRecordType implements KeyValEnum{
    POINT(Byte.valueOf("1"),"积分"),
    RED_PACKET(Byte.valueOf("2"),"红包"),
    TICKET(Byte.valueOf("3"),"券"),
    PAY_MENT(Byte.valueOf("4"),"订单支付"),
    CANCEL_GROUPBUY(Byte.valueOf("5"),"取消拼团"),
    ;

    private Byte code;
    private String msg;

    MQRecordType(Byte code, String msg) {
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
