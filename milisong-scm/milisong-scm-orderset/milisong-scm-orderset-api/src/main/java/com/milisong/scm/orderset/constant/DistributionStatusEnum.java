package com.milisong.scm.orderset.constant;

/**
 * 配送单状态枚举
 * 0待接单 1已接单 2配送中 3配送完成
 * @author youxia 2018年9月3日
 */
public enum DistributionStatusEnum {
	WAIT_RECEIPT((byte)0, "待接单"),
	ALREADY_RECEIPT((byte)1, "已接单"),
	IN_DISTRIBUTION((byte)2, "配送中"),
	DELIVERY_COMPLETED((byte)3, "配送完成"),
	;
	
	private Byte value;
    private String name;

    DistributionStatusEnum(Byte value, String name){
        this.value = value;
        this.name = name;
    }

    public static String getNameByStatus(Byte value){
        for (DistributionStatusEnum orderStatusEnum : DistributionStatusEnum.values()) {
            if (orderStatusEnum.value == value) {
                return orderStatusEnum.name;
            }
        }
        return "";
    }

    public Byte getValue() {
        return value;
    }

    public void setValue(Byte value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
}
