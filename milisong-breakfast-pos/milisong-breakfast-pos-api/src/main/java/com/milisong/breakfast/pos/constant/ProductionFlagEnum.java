package com.milisong.breakfast.pos.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 生产的标识
 * @author yangzhilong
 *
 */
public enum ProductionFlagEnum {

    RUN("1", "生产中"),
    PAUSE("0", "已暂停"),
    ;

	private String value;

    private String name;

    ProductionFlagEnum(String value,String name){
    	this.value = value;
    	this.name = name;
    }
    
    public String getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	static Map<String, String> enumMap = new HashMap<>();

    static {
        for (ProductionFlagEnum status : ProductionFlagEnum.values()) {
            enumMap.put(status.getValue(), status.getName());
        }
    }

    public static String getNameByValue(String value) {
        return enumMap.get(value);
    }
}
