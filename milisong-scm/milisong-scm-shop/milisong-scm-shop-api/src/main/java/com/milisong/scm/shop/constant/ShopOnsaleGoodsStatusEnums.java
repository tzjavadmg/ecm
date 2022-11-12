package com.milisong.scm.shop.constant;


public enum ShopOnsaleGoodsStatusEnums {

	AVAILABLE(1,"可售"),
	NOTAVAILABLE(2,"不可售");
    private int status;
    private String name;

    ShopOnsaleGoodsStatusEnums(int status, String name){
        this.status = status;
        this.name = name;
    }

    public static String getNameByStatus(int status){
        for (ShopOnsaleGoodsStatusEnums commonStatusEnum : ShopOnsaleGoodsStatusEnums.values()) {
            if (commonStatusEnum.status == status) {
                return commonStatusEnum.name;
            }
        }
        return "";
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
