package com.milisong.breakfast.scm.orderset.constant;

/**
 * 单号的前缀的枚举
 * @author yangzhilong
 *
 */
public enum NoPrefixEnum {
	ORDER("D", "集单中订单联编号前缀"),
	ORDERSET("F", "集单编号前缀");

	private String code;
	private String desc;
	NoPrefixEnum(String key, String value) {
		this.code = key;
		this.desc = value;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
