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
 * @date 2018/10/10 17:13
 */
@Getter
@AllArgsConstructor
public enum RefundOrderStatus {

    /**
     * 初始状态
     */
    RDFUNDING((byte) 0, "退款中"),
    /**
     * 下单开始计算时间，5分钟内未支付成功，订单将自动取消
     */
    SUCCEED((byte) 1, "退款成功"),
    FAILED((byte) 2, "退款失败");

    private final byte value;
    private final String name;


    static Map<Byte, String> enumMap = new HashMap<>();

    static {
        for (RefundOrderStatus status : RefundOrderStatus.values()) {
            enumMap.put(status.getValue(), status.getName());
        }
    }

    public static String getNameByValue(byte value) {
        return enumMap.get(value);
    }
}
