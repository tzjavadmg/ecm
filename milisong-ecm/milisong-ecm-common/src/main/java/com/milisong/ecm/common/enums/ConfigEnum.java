package com.milisong.ecm.common.enums;


public enum ConfigEnum {
	GLOBAL_INPUT 					("global_input","全局配置"),
	BANNER_INPUT 					("banner_input","banner门店配置"),
	INTERCEPT_INPUT					("intercept_input","门店截单、配送时间配置"),
	SINGLE_INPUT					("single_input","门店信息配置(如配送费，包装费等)");
	
	private String value;
    private String name;

    ConfigEnum(String value, String name){
        this.value = value;
        this.name = name;
    }

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

 
}
