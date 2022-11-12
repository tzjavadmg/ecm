package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单状态
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/2 16:48
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {

    /**
     * 初始状态
     */
    UN_PAID((byte) 0, "待支付"),
    /**
     * 下单开始计算时间，5分钟内未支付成功，订单将自动取消
     */
    CANCELED((byte) 1, "已取消"),

    PAID((byte) 2, "已支付"),
    /**
     * 已取消申请退款
     */
    REFUNDING((byte) 3, "退款中"),
    /**
     * 申请退款的配送单
     */
    REFUNDED((byte) 4, "已退款"),
    /**
     * 全部配送完成
     */
    COMPLETED((byte) 9, "已完成");

    private final byte value;
    private final String name;


    static Map<Byte, String> enumMap = new HashMap<>();

    static {
        for (OrderStatus status : OrderStatus.values()) {
            enumMap.put(status.getValue(), status.getName());
        }
    }

    public static String getNameByValue(byte value) {
        return enumMap.get(value);
    }
}
