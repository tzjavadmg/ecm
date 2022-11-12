package com.milisong.breakfast.scm.common.constant;

/**
 * 错误的提示信息
 * @author yangzhilong
 *
 */
public enum ErrorEnum {
	PARAMETER_CHECK_FAIL("400", "入参校验失败："),
	NOT_FOND_RESULT("401", "没有找到相关数据");

	private String code;
	private String desc;
	ErrorEnum(String key, String value) {
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
