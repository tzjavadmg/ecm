package com.milisong.pms.prom.enums;

/**
 * @author sailor wang
 * @date 2019/2/18 5:00 PM
 * @description
 */
public enum RedPacketEnum {
    ;

    /**
     * 发布状态：0 下线、1 上线
     */
    public static enum STATUS implements KeyValEnum {
        /**
         * 红包可用
         */
        ONLINE(new Byte("1"), "上线"),
        /**
         * 红包不可用
         */
        OFFLINE(new Byte("0"), "下线"),
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

    public enum SendStatus {
        WAIT_FOR_SEND((byte) 0, "待发送"),
        SEND_SUCCESS((byte) 1, "发送成功"),
        SEND_FAILED((byte) 2, "发送失败"),
        ;

        SendStatus(Byte code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        private Byte code;
        private String desc;

        public Byte getCode() {
            return code;
        }

        public void setCode(Byte code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

    }

}