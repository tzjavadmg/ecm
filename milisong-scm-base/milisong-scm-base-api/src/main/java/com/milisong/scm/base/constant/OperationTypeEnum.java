package com.milisong.scm.base.constant;

/**
 * 操作日志的类型模块
 * @author yangzhilong
 *
 */
public enum OperationTypeEnum {
	ADD("add", "新增"),
	UPDATE("update", "修改"),
	DELETE("delete", "删除")
	;
	
	private String code;
	private String desc;
	private OperationTypeEnum(String key, String value) {
		this.code = key;
		this.desc = value;
	}

	public String getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}
}
