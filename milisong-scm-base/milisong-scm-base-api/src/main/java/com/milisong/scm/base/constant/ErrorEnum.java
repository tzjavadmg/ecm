package com.milisong.scm.base.constant;

/**
 * 错误的提示信息
 * @author yangzhilong
 *
 */
public enum ErrorEnum {
	PARAMETER_CHECK_FAIL("400", "入参校验失败："),
	NOT_FOND_RESULT("401", "没有找到相关数据"),
	COMPANY_UPDATE_FAIL("500","门店在售商品不一致，修改失败"),
	COMPANY_UPDATE_ERROR("501","修改失败，请稍候再试"),
	;
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
