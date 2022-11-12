package com.milisong.scm.shop.constant;

/**
 * 门店配置类型枚举
 * @author youxia 2018年9月4日
 */
public enum ShopSaleFinishConfigTypeEnum {
	
	/**
	 * 门店配置信息
	 */
	SHOP_INTERCEPTINGTIME				(1, "截单时间"),
	SHOP_PREDICTARRIVESTARTTIME			(2, "配送开始时间"),
	SHOP_PREDICTARRIVEENDTIME			(3, "配送截止时间")
	;
	
	private int value;
    private String name;

    ShopSaleFinishConfigTypeEnum(int value, String name){
        this.value = value;
        this.name = name;
    }

    public static String getNameByStatus(int value){
        for (ShopSaleFinishConfigTypeEnum configTypeEnum : ShopSaleFinishConfigTypeEnum.values()) {
            if (configTypeEnum.value == value) {
                return configTypeEnum.name;
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
