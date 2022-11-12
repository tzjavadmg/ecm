package com.milisong.scm.orderset.constant;

/**
 * 包装类型
 * 1楼宇包装 2公司包装 3配送单
 * @author yangzhilong
 *
 */
public enum PackageTypeEnum {
	BUILDING((byte)1, "楼宇包装"),
	COMPANY((byte)2, "公司包装"),
	DISTRIBUTION((byte)3, "配送单包装");

	private Byte code;
	private String desc;
	PackageTypeEnum(Byte key, String value) {
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
