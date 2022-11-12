package com.milisong.delay.enums;

public enum StatusEnum {
	SUCCESS((byte)1,"成功"),
	FAIL((byte)2,"失败");
    private byte status;
    private String name;

    StatusEnum(byte status, String name){
        this.status = status;
        this.name = name;
    }

    public static String getNameByStatus(byte status){
        for (StatusEnum commonStatusEnum : StatusEnum.values()) {
            if (commonStatusEnum.status == (byte)status) {
                return commonStatusEnum.name;
            }
        }
        return "";
    }

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
}
