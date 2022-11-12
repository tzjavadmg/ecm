package com.mili.oss.constant;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 物流配送状态
 * @author yangzhilong
 *
 */
@Getter
@AllArgsConstructor
public enum LogisticsDeliveryStatus {
	
	/**
	 * 集单的初始化状态
	 */
	INIT((byte) -1, "备餐中"),

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

    private Byte value;

    private String name;

    static Map<Byte, String> enumMap = new HashMap<>();

    static {
        for (LogisticsDeliveryStatus status : LogisticsDeliveryStatus.values()) {
            enumMap.put(status.getValue(), status.getName());
        }
    }

    public static String getNameByValue(byte value) {
        return enumMap.get(value);
    }
}
