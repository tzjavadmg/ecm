package com.milisong.oms.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/2 17:52
 */
@Getter
@AllArgsConstructor
public enum OrderResponseCode {

    /**
     * 虚拟订单
     */
    V_ORDER_CANCEL_ERROR_CANCELED("10000", "虚拟订单已取消"),
    V_ORDER_CANCEL_ERROR_COMPLETED("10001", "真实订单已生成"),

    V_ORDER_CHECK_NO_GOODS("10010", "需要先往购物车中添加商品才能去结算哦"),
    V_ORDER_CHECK_SKIP_TODAY("10011", "今天已过截单时间，不可配送，是否确认继续预订其它时间的商品"),
    V_ORDER_CHECK_CUTOFF_TIME_EXPIRED("10012", "需要在今天以外的时间往购物车中添加的商品才能去结算哦"),
    V_ORDER_CHECK_NO_STOCK("10013", "商品库存不足"),
    V_ORDER_CHECK_PAY_TIME_EXPIRED("10014", "超过%s分钟未支付，需要重新下单哦"),

    /**
     * 实际订单
     */
    ORDER_CANCELED("10020", "超过%s分钟未支付，需要重新下单哦"),
    ORDER_PAID("10021", "订单已支付"),
    ORDER_COMPLETED("10022", "订单已完成"),

    ORDER_CHECK_CUTOFF_TIME_EXPIRED("10030", "%s（今天）无法配送，需要重新下单哦"),
    ORDER_CHECK_NO_GOODS("10031", "需要先往购物车中添加商品才能去结算哦"),
    ORDER_CHECK_CURRENT_CUTOFF_TIME_EXPIRED("10032", "配送时间已过期，需要重新选择%s（今天）配送时间哦"),
    ORDER_CHECK_NO_DELIVERY("10033", "%s %s 前已约满，需要重新选择配送时间哦~"),
    ORDER_CHECK_NO_DELIVERY_ALL_DAY("10034", "%s 已售完"),

    /**
     * 退款单
     */
    REFUND_CHECK_CUTOFF_TIME_EXPIRED("10051", "今天已过截单时间的子订单不能取消哦"),
    REFUND_CHECK_CUTOFF_TIME_EXPIRED_B("10053", "此天不可取消哦~"),
    REFUND_ALL_DELIVERY_CANCELED("10052", "配送订单已经全部取消");

    private final String code;
    private final String message;
}
