package com.milisong.breakfast.pos.constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RedisKeyConstant {
	// redis key 分隔符
	public static final String SEPARATE = ":";
	
	// 全部的集单list
	private static final String ORDER_SET_ALL_LIST = "order_set_all_list:";
	// 生产中的集单list
	private static final String ORDER_SET_LINE_UP_LIST = "order_set_line_up_list:";
	// 正在生产中的数量
	private static final String ORDER_SET_LINE_UP_COUNT = "order_set_line_up_count:";
	
	// 接受集单的锁
	private static final String ORDER_SET_LOCK = "order_set_lock_";
	// 门店生产某个集单的锁
	private static final String ORDER_SET_PRODUCTION_LOCK = "order_set_production_lock:";
	
	// 门店生产暂停标识
	private static final String PRODUCTION_FLAG = "production_flag:";
	// 门店生产标识变更操作锁
	private static final String PRODUCTION_FLAG_LOCK = "production_flag_lock:";
	
	// 门店倒计时的锁控制
	private static final String SHOP_TIMEOUT_LOCK = "shop_timeout_lock:";
	
	// 门店的集单的超时时间倒计时TTL
	public static final String ORDER_SET_TIMEOUT_TTL = "order_set_timeout_ttl:";
	
	// 门店的销售总量
	private static final String SHOP_GOODS_SALE_COUNT = "shop_goods_sale_count:";
	
	// 门店当前生产完的集单号
	private static final String SHOP_PRODUCED_ORDER_SET_NO = "shop_produced_order_set_no:";
	// 检查门店集单号顺序的锁
	private static final String SHOP_ORDER_SET_SEQUENCE_LOCK = "shop_order_set_sequence_lock:";
		
	/**
	 * 获取门店实时销售量
	 * @param shopId
	 * @return
	 */
	public static final String getShopGoodsSaleCount(Long shopId,String day,String goodsCode) {
		return SHOP_GOODS_SALE_COUNT.concat(String.valueOf(shopId)).concat(":"+day).concat(":"+goodsCode);
	}
	/**
	 * 获取门店push过来的集单的全部临时队列的key
	 * @param shopId
	 * @return
	 */
	public static final String getOrderSetAllListKey(Long shopId) {
		return ORDER_SET_ALL_LIST.concat(String.valueOf(shopId));
	}
	
	/**
	 * 获取门店当前正在生产的集单队列的key
	 * @param shopId
	 * @return
	 */
	public static final String getOrderSetLineUpListKey(Long shopId) {
		return ORDER_SET_LINE_UP_LIST.concat(String.valueOf(shopId));
	}
	
	/**
	 * 获取门店当前正在正产的盒饭的数量
	 * @param shopId
	 * @return
	 */
	public static final String getOrderSetLineUpCountKey(Long shopId) {
		return ORDER_SET_LINE_UP_COUNT.concat(String.valueOf(shopId));
	}
	
	/**
	 * 集单的接收锁，用于防止重复接收
	 * @param orderSetNo
	 * @return
	 */
	public static final String getOrderSetLockKey(String orderSetNo) {
		return ORDER_SET_LOCK.concat(orderSetNo);
	}
	
	/**
	 * 门店生产的锁
	 * @param shopId
	 * @return
	 */
	public static final String getOrderSetProductionLockKey(Long shopId) {
		return ORDER_SET_PRODUCTION_LOCK.concat(String.valueOf(shopId));
	}
	
	/**
	 * 门店的生产标识
	 * @param shopId
	 * @return
	 */
	public static final String getProductionFlagKey(Long shopId) {
		return PRODUCTION_FLAG.concat(String.valueOf(shopId));
	}
	
	/**
	 * 门店生产标识变更操作锁
	 * @param shopId
	 * @return
	 */
	public static final String getProductionFlagLockKey(Long shopId) {
		return PRODUCTION_FLAG_LOCK.concat(String.valueOf(shopId));
	}
			
	/**
	 * 门店超时锁控制
	 * @param shopId
	 * @return
	 */
	public static final String getShopTimeoutLockKey(Long shopId) {
		return SHOP_TIMEOUT_LOCK.concat(String.valueOf(shopId));
	}
	
	/**
	 * 门店的集单的超时时间倒计时TTL
	 * @param shopId
	 * @param orderSetNo
	 * @return
	 */
	public static final String getOrderSetTimeoutTtlKey(Long shopId, String orderSetNo) {
		return ORDER_SET_TIMEOUT_TTL.concat(String.valueOf(shopId)).concat(SEPARATE).concat(orderSetNo);
	}
	
	/**
	 * 门店已经生产的集单号控制
	 * @param shopId
	 * @return
	 */
	public static final String getShopProducedOrderSetKey(Long shopId) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		return SHOP_PRODUCED_ORDER_SET_NO.concat(String.valueOf(shopId)).concat(":").concat(df.format(new Date()));
	}
	
	/**
	 * 门店集单号顺序号控制的锁
	 * @param shopId
	 * @return
	 */
	public static final String getShopOrderSetSequenceLockKey(Long shopId) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		return SHOP_ORDER_SET_SEQUENCE_LOCK.concat(String.valueOf(shopId)).concat(":").concat(df.format(new Date()));
	}
	
}
