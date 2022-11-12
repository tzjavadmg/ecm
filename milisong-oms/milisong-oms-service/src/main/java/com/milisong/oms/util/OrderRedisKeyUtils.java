package com.milisong.oms.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/4 18:14
 */
public class OrderRedisKeyUtils {

    static public String CANCEL_VIRTUAL_ORDER_LOCK_PREFIX = "v_order_cancel_lock";
    static public String CANCEL_ORDER_LOCK_PREFIX = "order_cancel_lock";
    static public String GOODS_SALES_PREFIX = "lately_30days_sales";
    static public String RED_PACKET_LOCK_PREFIX = "red_packet_lock";
    static public String COUPON_LOCK_PREFIX = "coupon_lock";
    static public String ORDER_NO_COUNTER_PREFIX = "order_no_counter";
    static public String DELIVERY_COUNTER_PREFIX = "delivery_counter";
    static public String ORDER_DETAIL_PREFIX = "order_detail";
    static public String GLOBAL_CONFIG = "config:global";
    static public String MSG_PREVENT_DUPLICATE="MSG_PREVENT_DUPLICATE:";

    public static String getMsgPreventDuplicateKey(Long messageId){
        return MSG_PREVENT_DUPLICATE+messageId;
    }

    public static String getOrderDetailKey(Long orderId) {
        return ORDER_DETAIL_PREFIX + ":" + orderId;
    }


    public static String getDeliveryCounterKey(String shopCode, Date deliveryTimezoneFrom) {
        String timezone = DateFormatUtils.format(deliveryTimezoneFrom, "yyMMddHHmm");
        return DELIVERY_COUNTER_PREFIX + ":" + shopCode + ":" + timezone;
    }

    /**
     * 取消订单时上锁，对同一订单不能同时执行多次取消操作
     *
     * @param orderId 订单ID
     * @return cacheKey
     */
    public static String getCancelOrderLockKey(long orderId) {
        return CANCEL_ORDER_LOCK_PREFIX + ":" + orderId;
    }

    public static String getCancelVirtualOrderLockKey(long orderId) {
        return CANCEL_VIRTUAL_ORDER_LOCK_PREFIX + ":" + orderId;
    }

    /**
     * 获某个商品最近30天的销量key匹配
     *
     * @param goodsCode 商品编码
     * @return
     */
    public static String getLately30DaysSalesKey(String goodsCode) {
        return GOODS_SALES_PREFIX + ":" + goodsCode;
    }

    /**
     * 获取红包锁key
     *
     * @param redPacketId 红包ID
     * @return String
     */
    public static String getRedPacketLockKey(Long redPacketId) {
        return RED_PACKET_LOCK_PREFIX + ":" + redPacketId;
    }

    /**
     * 优惠券锁key
     *
     * @param couponId
     * @return
     */
    public static String getCouponLockKey(Long couponId) {
        return COUPON_LOCK_PREFIX + ":" + couponId;
    }

    /**
     * 获取订单号计数器key
     *
     * @param orderType 订单类型
     * @param shopCode  门店编码
     * @param datePart  计数日期(yyMMdd)
     * @return String
     */
    public static String getOrderNoCounterKey(String orderType, String shopCode, String datePart) {
        return ORDER_NO_COUNTER_PREFIX + ":" + orderType + shopCode + datePart;
    }

    public static String getShopTimezoneCapacityLockKey(String shopCode, Date timezoneFrom) {
        String dateString = DateFormatUtils.format(timezoneFrom, "yyMMddHHmm");
        return "L_" + shopCode + dateString;
    }

    public static String getConfigKey(Byte businessLine) {
        return "config:" + businessLine;
    }

    public static String getOnsaleGoodsStockLockKey(String shopCode, Date saleDate, String goodsCode) {
        String saleDateString = DateFormatUtils.format(saleDate, "yyMMdd");
        return "L_" + shopCode + saleDateString + ":" + goodsCode;
    }

    public static String getOnsaleGoodsStockKey(String shopCode, Date saleDate, String goodsCode) {
        String saleDateString = DateFormatUtils.format(saleDate, "yyMMdd");
        return "shop_stock:" + shopCode + "_" + saleDateString + ":" + goodsCode;
    }
}
