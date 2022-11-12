package com.milisong.pms.prom.enums;

/**
 * 活动状态
 *
 * @author sailor wang
 * @date 2018/9/13 下午1:49
 * @description
 */
public enum ActivityStatus implements KeyValEnum {
    DRAFT(0, "保存-草稿"), NO_EFFECTIVE(1, "保存并发布-未生效"), EFFECTIVE(2, "上线-生效"), END(3, "下线-已结束");

    private Integer code;
    private String msg;

    ActivityStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public Object getMsg() {
        return this.msg;
    }
}