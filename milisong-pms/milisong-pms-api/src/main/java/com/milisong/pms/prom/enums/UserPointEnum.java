package com.milisong.pms.prom.enums;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/4   15:20
 *    desc    : 用户积分枚举类型类
 *    version : v1.0
 * </pre>
 */

public class  UserPointEnum {

    public static final Double POINT_RATE = 50.0;

    /**
     * 流水产生类型
     */
    public static enum Action implements KeyValEnum{
        PAY_ORDER(new Byte("1"),"支付订单"),
        FINISH_ORDER(new Byte("2"),"订单完成"),
        REFUND_ORDER(new Byte("3"),"订单退款"),
        CANCLE_ORDER(new Byte("4"),"取消订单"),
        ;
        private Byte val;

        private String desc;

        Action(Byte val, String desc) {
            this.val = val;
            this.desc = desc;
        }

        @Override
        public Byte getCode() {
            return this.val;
        }

        @Override
        public String getMsg() {
            return this.desc;
        }
    }

    /**
     * 收支类型
     */
    public static enum IncomeType implements KeyValEnum{
        IN(new Byte("1"),"收入"),
        OUT(new Byte("0"),"支出"),
        ;
        private Byte val;

        private String desc;

        IncomeType(Byte val, String desc) {
            this.val = val;
            this.desc = desc;
        }

        @Override
        public Byte getCode() {
            return this.val;
        }

        @Override
        public String getMsg() {
            return this.desc;
        }
    }

    /**
     * 相关业务类型
     */
    public static enum RefType implements KeyValEnum{
        ORDER(new Byte("1"),"订单"),
        SUB_ORDER(new Byte("2"),"子订单"),
        ;
        private Byte val;

        private String desc;

        RefType(Byte val, String desc) {
            this.val = val;
            this.desc = desc;
        }

        @Override
        public Byte getCode() {
            return this.val;
        }

        @Override
        public String getMsg() {
            return this.desc;
        }
    }
}
