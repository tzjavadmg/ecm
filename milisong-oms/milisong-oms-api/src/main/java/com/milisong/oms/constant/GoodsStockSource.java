package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/8 9:29
 */
@Getter
@AllArgsConstructor
public enum GoodsStockSource {

    /**
     * 创建虚拟订单
     */
    CREATE_VIRTUAL_ORDER((byte) 0),
    /**
     * 取消虚拟订单
     */
    CANCEL_VIRTUAL_ORDER((byte) 1),
    /**
     * 取消真实订单
     */
    CANCEL_ORDER((byte) 2),
    /**
     * 订单退款
     */
    REFUND_ORDER((byte) 3),
    /**
     * 设置在售库存
     */
    SET_STOCK((byte) 4),

    /**
     * 门店变更导致的设置在售库存
     */
    SHOP_CHANGE_SET_STOCK((byte) 5),

    /**
     * 创建团购订单
     */
    CREATE_GROUP_BUY_ORDER((byte) 0),

    /**
     * 取消团购订单(拼团失败)
     */
    CANCEL_GROUP_BUY_ORDER((byte) 0);

    private final byte value;
}
