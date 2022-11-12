package com.milisong.pms.prom.enums;

/**
 * 券类型
 *
 * @author tianhaibo
 * @date 2019/1/11 下午1:49
 * @description
 */
public enum CouponEnum {
    ;

    /**
     * 券类型：1 折扣券 2 商品券
     */
    public static enum TYPE implements KeyValEnum{

        NEW_DISCOUNT(Byte.valueOf("0"), "新人折扣券"),
        DISCOUNT(Byte.valueOf("1"), "普通折扣券"),
        GOODS(Byte.valueOf("2"), "商品券"),
//        COMBO(Byte.valueOf("3"), "套餐券"),
        ;
        private Byte val;

        private String desc;

        TYPE(Byte val, String desc) {
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
     * 商品券的套餐类型：0 单品 1 套餐
     */
    public static enum LABEL implements KeyValEnum{

        SINGLE(Byte.valueOf("0"), "单品"),
        PACKAGE(Byte.valueOf("1"), "套餐"),
        ;
        private Byte val;

        private String desc;

        LABEL(Byte val, String desc) {
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
     * 发布状态：0 下线、1 上线
     */
    public static enum STATUS implements KeyValEnum{
        ONLINE(new Byte("1"),"上线"),
        OFFLINE(new Byte("0"),"下线"),
        ;
        private Byte val;

        private String desc;

        STATUS(Byte val, String desc) {
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
     * 否与其它活动共享，0 不同享 1 同享
     */
    public static enum IS_SHARE implements KeyValEnum{
        YES(new Byte("1"),"同享"),
        NO(new Byte("0"),"不同享"),
        ;
        private Byte val;

        private String desc;

        IS_SHARE(Byte val, String desc) {
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