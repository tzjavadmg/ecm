package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/24 15:06
 */
@Getter
@AllArgsConstructor
public enum ShunFengDeliveryStatus {

    /**
     * 配送派单
     */
    DISPATCHED_ORDER((byte) 0, "已派单"),
    /**
     * 配送员接单
     */
    RECEIVED_ORDER((byte) 1, "已接单"),
    /**
     * 配送员到店
     */
    ARRIVED_SHOP((byte) 2, "已到店"),
    /**
     * 配送员提货
     */
    RECEIVED_GOODS((byte) 3, "已提货"),
    /**
     * 配送员送达
     */
    COMPLETED((byte) 4, "已送达");

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
