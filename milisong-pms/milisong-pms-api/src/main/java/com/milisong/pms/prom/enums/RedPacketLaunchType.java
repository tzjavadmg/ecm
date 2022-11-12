package com.milisong.pms.prom.enums;

/**
 * 红包和积分发起类型
 * @author sailor wang
 * @date 2018/9/21 下午6:58
 * @description
 */
public enum RedPacketLaunchType implements KeyValEnum {
    ORDER_SHARE((byte)1,"订单分享红包"),// 午餐
    BREAKFAST_COUPON_ORDER_SHARE((byte)2,"早餐下单分享券"),
    COMPLETE_GROUPBUY_ORDER_SHARE((byte)3,"拼团成功分享券"),
    ;

    private Byte code;
    private String msg;

    RedPacketLaunchType(Byte code, String msg) {
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