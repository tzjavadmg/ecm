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
 * @date 2018/10/7 10:00
 */
@Getter
@AllArgsConstructor
public enum OrderType {

    /**
     * 早餐
     */
    BREAKFAST((byte) 0, "B"),
    /**
     * 午餐
     */
    LUNCH((byte) 1, "L"),
    /**
     * 下午茶
     */
    AFTERNOON_TEA((byte) 2, "A"),
    /**
     * 晚餐
     */
    SUPPER((byte) 3, "S"),
    /**
     * 夜宵
     */
    MIDNIGHT_SNACK((byte) 4, "M");

    private final byte value;
    private final String abbr;

    static Map<Byte, String> enumMap = new HashMap<>();

    static {
        for (OrderType type : OrderType.values()) {
            enumMap.put(type.getValue(), type.getAbbr());
        }
    }

    public static String getAbbrByValue(byte value) {
        return enumMap.get(value);
    }
}
