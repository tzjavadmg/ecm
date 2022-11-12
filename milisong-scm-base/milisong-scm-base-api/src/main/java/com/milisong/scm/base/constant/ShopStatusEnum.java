package com.milisong.scm.base.constant;

/**
 * 门店状态枚举
 * @author youxia 2018年9月4日
 */
public enum ShopStatusEnum {

	/**
	 * 门店状态信息
	 */
	SHOP_STATUS_OPEN				(1, "营业中"),
	SHOP_STATUS_CLOSE				(2, "已停业")

	;
	
	private Integer value;
    private String name;

    ShopStatusEnum(Integer value, String name){
        this.value = value;
        this.name = name;
    }

    public static String getNameByStatus(Integer value){
        for (ShopStatusEnum shopStatusEnum : ShopStatusEnum.values()) {
            if (shopStatusEnum.value == value) {
                return shopStatusEnum.name;
            }
        }
        return "";
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
}
