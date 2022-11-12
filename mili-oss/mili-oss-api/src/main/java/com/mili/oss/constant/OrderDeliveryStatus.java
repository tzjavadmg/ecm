package com.mili.oss.constant;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单配送状态常量类
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/24 15:06
 */
@Getter
@AllArgsConstructor
public enum OrderDeliveryStatus {

    INIT((byte)-1,"未派单"),
    /**
     * 配送派单
     */
    DISPATCHED_ORDER((byte) 0, "等待骑手接单"),
    /**
     * 配送员接单
     */
    RECEIVED_ORDER((byte) 1, "骑手已接单"),
    /**
     * 配送员到店
     */
    ARRIVED_SHOP((byte) 2, "骑手已到店"),
    /**
     * 配送员提货
     */
    RECEIVED_GOODS((byte) 3, "骑手已取餐"),
    /**
     * 配送员送达
     */
    COMPLETED((byte) 4, "已完成"),

    /**
     * 顺丰原因取消订单
     */
    CANCEL((byte) 10, "顺丰取消");

    private Byte value;

    private String name;

    static Map<Byte, String> enumMap = new HashMap<>();

    static {
        for (OrderDeliveryStatus status : OrderDeliveryStatus.values()) {
            enumMap.put(status.getValue(), status.getName());
        }
    }

    public static String getNameByValue(byte value) {
        return enumMap.get(value);
    }

    /** 业务类型 */
    @Getter
    @AllArgsConstructor
    public static enum  BusinessType {

        BREAKFAST ((byte)0,"早餐"),
        LUNCH((byte)1,"午餐");

        private byte value;

        private String name;

        static Map<Byte, String> enumMap = new HashMap<>();

        static {
            for (BusinessType status : BusinessType.values()) {
                enumMap.put(status.getValue(), status.getName());
            }
        }

        public static String getNameByValue(byte value) {
            return enumMap.get(value);
        }
    }
}
