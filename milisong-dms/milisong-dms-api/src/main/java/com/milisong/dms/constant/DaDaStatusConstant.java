package com.milisong.dms.constant;

import lombok.Getter;

/**
 * 顺丰状态枚举
 * @author zhaozhonghui 2018年10月29日
 */
@Getter
public class DaDaStatusConstant {


    /**
        达达订单配送状态
        待接单＝1 待取货＝2 配送中＝3 已完成＝4 已取消＝5
        已过期＝7 指派单=8 妥投异常之物品返回中=9 妥投异常之物品返回完成=10 创建达达运单失败=1000
     */
    public static class DaDaDeliveryStatus {
        public static final Byte WAIT_CONFIRM = 1;
        public static final Byte WAIT_DISPATCHING = 2;
        public static final Byte DISPATCHING = 3;
        public static final Byte FINISH = 4;
        public static final Byte CANCEL = 5;
        public static final Byte OVERTIME = 7;
        public static final Byte ASSIGN = 8;
        public static final Byte DISPATCHING_FAIL = 8;

//        public static final Byte DISPATCHING = 15;
    }

    public static class IsPrepay {
        public static final Byte NOT_NEED = 0;
        public static final Byte NEED = 1;
    }

    public static class CargoType {
        public final static byte FOOD = 1;
    }
}
