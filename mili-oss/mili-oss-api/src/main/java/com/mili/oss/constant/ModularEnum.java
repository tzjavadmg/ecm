package com.mili.oss.constant;

/**
 * 模块
 * @author yangzhilong
 *
 */
public enum ModularEnum {
	ORDER_SET("orderset", "集单");

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
