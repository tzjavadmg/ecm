package com.milisong.pos.production.constant;

import java.util.HashMap;
import java.util.Map;

/**
*@author    created by benny
*@date  2018年10月25日---下午8:33:05
*
*/
public enum OrderSetStatusEnum {

	 /**
     * 待生产
     */
    WATTING_ORDER_1((byte) 1, "待生产"),
    /**
     * 生产中
     */
    PRODUCTION_ORDER_2((byte) 2, "生产中"),
    /**
     * 已完成
     */
    FINISH_ORDER_3((byte) 3, "已完成");

	private Byte value;

    private String name;

    OrderSetStatusEnum(byte value,String name){
    	this.value = value;
    	this.name = name;
    }
    
    public Byte getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	static Map<Byte, String> enumMap = new HashMap<>();

    static {
        for (OrderSetStatusEnum status : OrderSetStatusEnum.values()) {
            enumMap.put(status.getValue(), status.getName());
        }
    }

    public static String getNameByValue(byte value) {
        return enumMap.get(value);
    }
}
