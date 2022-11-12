package com.milisong.pms.prom.enums;

/**
 * 扫码活动
 *
 * @author sailor wang
 * @date 2019/5/10 2:37 PM
 * @description
 */
public class ScanQrcodeActivityEnum {

    public enum GIFT_TYPE {
        BF_COUPON(Byte.valueOf("0"), "早餐券"),
        ;
        private Byte value;
        private String desc;

        GIFT_TYPE(Byte value, String desc) {
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

}
