package com.mili.oss.constant;

/**
 * 订单类型枚举
 *  0:早餐;1:午餐
 * @author yangzhilong
 *
 */
public enum OrderTypeEnum {
	BREAKFAST((byte)0, "早餐"),
	LUNCH((byte)1, "午餐");

	private Byte code;
	private String desc;
	OrderTypeEnum(Byte key, String value) {
		this.code = key;
		this.desc = value;
	}

	public Byte getCode() {
		return code;
	}
	public static String getCodeByDesc(Byte code) {
		for (OrderTypeEnum ordertype : OrderTypeEnum.values()) {
			if(ordertype.code == code) {
				return ordertype.desc;
			}
		}
		return "";
	}

	public String getDesc() {
		return desc;
	}
}
