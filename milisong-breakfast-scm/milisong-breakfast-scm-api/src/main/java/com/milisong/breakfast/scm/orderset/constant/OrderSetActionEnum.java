package com.milisong.breakfast.scm.orderset.constant;

/**
 * 集单的操作类型
 * 1打包 2交配送 3通知客户
 * @author yangzhilong
 *
 */
public enum OrderSetActionEnum {
	BALE(1, "打包"),
	DELIVERY(2, "交配送"),
	NOTIFY(3, "通知客户");

	private Integer code;
	private String desc;
	OrderSetActionEnum(Integer key, String value) {
		this.code = key;
		this.desc = value;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
