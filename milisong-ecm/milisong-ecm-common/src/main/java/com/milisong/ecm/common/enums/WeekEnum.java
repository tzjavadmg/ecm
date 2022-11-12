package com.milisong.ecm.common.enums;


public enum WeekEnum {
	/**
	 * 订单状态信息
	 */
	Monday 						(1, "周一"),
	Tuesday 					(2, "周二"),
	Wednesday					(3, "周三"),
	Thursday					(4, "周四"),
	Friday						(5, "周五"),
	Saturday					(6, "周六"),
	Sunday						(7, "周日");
	
	private int value;
    private String name;

    WeekEnum(int value, String name){
        this.value = value;
        this.name = name;
    }

    public static String getNameByStatus(Integer value){
        for (WeekEnum orderStatusEnum : WeekEnum.values()) {
            if (orderStatusEnum.value == value) {
                return orderStatusEnum.name;
            }
        }
        return "";
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
