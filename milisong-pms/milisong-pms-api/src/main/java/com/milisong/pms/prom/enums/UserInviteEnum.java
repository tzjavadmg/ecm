package com.milisong.pms.prom.enums;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/3/25   17:10
 *    desc    : 用户邀请新相关枚举
 *    version : v1.0
 * </pre>
 */

public class UserInviteEnum {

    public static enum INVITE_RECORD_STATUS{
        NOT_PLACE_ORDER(Byte.valueOf("0"),"未下单"),
        ON_DELIVERY(Byte.valueOf("1"),"已下单待配送"),
        SUCCESS(Byte.valueOf("2"),"送券成功（配送完成，完成一天即可）"),
        FAILD(Byte.valueOf("3"),"送送券失败（活动结束，超限制）"),

        C_FAILD(Byte.valueOf("0"),"C端显示 邀新未完成"),
        C_SUCCESS(Byte.valueOf("1"),"C端显示 邀新完成"),
        C_ON_DELIVERY(Byte.valueOf("2"),"C端显示 已下单-等待配送"),
        ;
        private Byte value;
        private String desc;

        INVITE_RECORD_STATUS(Byte value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public Byte getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    public static enum INVITE_TYPE{
        OLD_PULL_NEW(Byte.valueOf("1"),"老拉新"),
        ;
        private Byte value;
        private String desc;

        INVITE_TYPE(Byte value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public Byte getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }
    }

    public static enum INVITE_RECORD_REMARK{
        NOT_PLACE_ORDER("未下单"),
        ON_DELIVERY("已支付下单"),
        SUCCESS("送券成功"),
        USER_REFUND("用户退单"),
        INVITE_OVER("活动结束"),
        MAX_LIMIT("超多最大邀请限制"),
        ;
        private String desc;

        INVITE_RECORD_REMARK(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }
}
