package com.milisong.scm.orderset.constant;

/**
 * 集单状态枚举
 * 1待打包 2已打包 3配送中 4已通知
 * @author yangzhilong
 *
 */
public enum OrderSetStatusEnum {
	WAITING_FOR_PACKAGING((byte)1, "待打包"),
	ALREADY_PACKED((byte)2, "已打包"),
	IN_DISTRIBUTION((byte)3, "配送中"),
	NOTIFIED((byte)4, "已通知"),
	;

	private Byte code;
	private String desc;
	OrderSetStatusEnum(Byte key, String value) {
		this.code = key;
		this.desc = value;
	}

	public Byte getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
