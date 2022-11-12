package com.milisong.scm.orderset.constant;
/**
*@author    created by benny
*@date  2018年9月16日---下午7:23:06
* 打印类型
*/
public enum PrintTypeEnum {

	PICKORDER(1, "打印生产联"),
	DISTRIBUTION(2, "打印配送单/集单/顾客联"),
	COLLECTION(3, "打印集单"),
	ALONEDISTRIBUTION(4, "单独打印配送单"),
	ALONECOLLECTION(5, "单独打印集单"),
	ALONECUSTOMER(6, "单独打印顾客单");
	private Integer code;
	private String desc;
	PrintTypeEnum(Integer key, String value) {
		this.code = key;
		this.desc = value;
	}
	 public static String getDescByCode(Integer code) {
		 for (PrintTypeEnum printTypeEnum : PrintTypeEnum.values()) {
			if(printTypeEnum.code == code) {
				return printTypeEnum.desc;
			}
		}
		 return "";
	 }

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
