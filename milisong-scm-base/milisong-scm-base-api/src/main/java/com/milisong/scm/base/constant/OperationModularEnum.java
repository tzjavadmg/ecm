package com.milisong.scm.base.constant;

/**
 * 操作日志-模块
 * @author yangzhilong
 *
 */
public enum OperationModularEnum {
	ORDER_SET_LUNCH("orderset", "集单-午餐"),
	ORDER_SET_BF("orderset-breakfast", "集单-早餐");

	private String code;
	private String desc;
	OperationModularEnum(String key, String value) {
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
