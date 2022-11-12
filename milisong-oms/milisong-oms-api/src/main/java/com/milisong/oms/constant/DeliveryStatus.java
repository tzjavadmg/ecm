package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 配送状态
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/2 16:48
 */
@Getter
@AllArgsConstructor
public enum DeliveryStatus {
    /**
     * 配送员提货
     */
    DELIVERING((byte) 0, "配送中"),
    /**
     * 已打包待配送
     */
    PACKAGED((byte) 1, "待配送"),
    /**
     * 初始状态
     */
    GETTING_READY((byte) 2, "备餐中"),
    /**
     * 已取消申请退款
     */
    CANCELED((byte) 3, "已取消"),
    /**
     * 申请退款的配送单
     */
    REFUNDED((byte) 4, "已退款"),
    /**
     * 配送派单
     */
    DISPATCHED_ORDER((byte) 5, "已派单"),
    /**
     * 配送员接单
     */
    RECEIVED_ORDER((byte) 6, "已接单"),
    /**
     * 配送员到店
     */
    ARRIVED_SHOP((byte) 7, "已到店"),
    /**
     * 配送员提货
     */
    RECEIVED_GOODS((byte) 8, "已提货"),
    /**
     * 配送员送达
     */
    COMPLETED((byte) 9, "已完成");

    private byte value;

    private String name;

    static Map<Byte, String> enumMap = new HashMap<>();

    static {
        for (DeliveryStatus status : DeliveryStatus.values()) {
            enumMap.put(status.getValue(), status.getName());
        }
    }

    public static String getNameByValue(byte value) {
        return enumMap.get(value);
    }
}
