package com.mili.oss.constant;

/**
 * ๆจกๅ
 * @author yangzhilong
 *
 */
public enum ModularEnum {
	ORDER_SET("orderset", "้ๅ");

	private String code;
	private String desc;
	ModularEnum(String key, String value) {
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
