package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/2 18:05
 */
@Getter
@AllArgsConstructor
public enum VirtualOrderStatus {

    /**
     * 初始状态
     */
    UN_PAY((byte) 0, "待生成订单"),
    /**
     * 离开结算页面，或者5分钟内没有点击支付按钮生成订单将自动取消虚拟认单
     */
    CANCELED((byte) 1, "已取消"),
    /**
     * 已生成实际订单
     */
    COMPLETED((byte) 2, "已生成订单");

    private byte value;
    private String name;
}
