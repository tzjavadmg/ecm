package com.milisong.pms.prom.enums;

/**
 * @author sailor wang
 * @date 2019/5/19 7:18 PM
 * @description
 */
public class GrouyBuyEnum {

    public enum GROUP_BUY_STATUS implements KeyValEnum {
        CREATE_UNPAY(Byte.valueOf("0"), "已开团未支付"),
        CREATE_PAY(Byte.valueOf("1"), "开团成功,拼团中"),
        COMPLETED(Byte.valueOf("2"), "完成拼团"),
        TIMEOUT(Byte.valueOf("3"), "拼团超时"),
        REFUND(Byte.valueOf("4"), "拼团失败，已退款"),
        ;
        private Byte val;

        private String desc;

        GROUP_BUY_STATUS(Byte val, String desc) {
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


    public enum USER_GROUP_BUY_STATUS implements KeyValEnum {
        JOINED_UNPAY(Byte.valueOf("0"), "已参团未支付"),
        JOINED_PAY(Byte.valueOf("1"), "已参团并支付成功");
        private Byte val;

        private String desc;

        USER_GROUP_BUY_STATUS(Byte val, String desc) {
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

    public enum GROUP_BUY_LOCK_STATUS implements KeyValEnum {
        UNLOCK(Byte.valueOf("0"), "锁已释放"),
        LOCK(Byte.valueOf("1"), "已加锁");
        private Byte val;

        private String desc;

        GROUP_BUY_LOCK_STATUS(Byte val, String desc) {
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
     * 发送消息场景
     */
    public enum SEND_MSG_SCENE implements KeyValEnum {
        CREATE_GROUPBUY_NOTIFY(Byte.valueOf("1"), "开团成功通知"),
        TIMEOUT_NOTIFY(Byte.valueOf("2"), "拼团超时通知"),
        LEFT_TIME_NOTIFY(Byte.valueOf("3"), "剩余时间提醒"),
        PROCESS_NOTIFY(Byte.valueOf("4"), "拼团进度提醒"),
        COMPLETE_NOTIFY(Byte.valueOf("5"), "成团提醒"),
        ;
        private Byte val;

        private String desc;

        SEND_MSG_SCENE(Byte val, String desc) {
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