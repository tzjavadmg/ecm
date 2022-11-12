package com.milisong.scm.goods.constant;

/**
 * 商品状态枚举
 * @author youxia 2018年9月3日
 */
public enum GoodsStatusEnum {
	
	/**
	 * 商品状态信息
	 */
	STATUS_USEING					("S1", "使用中"),
	STATUS_CLOSE					("S2", "已停用"),

	/**
	 * 商品子状态信息
	 */
	DETAIL_STATUS_FORTHCOMING		("DS1", "即将上线"),
	DETAIL_STATUS_USEING			("DS2", "使用中"),
	DETAIL_STATUS_TOD_DOWNLINE		("DS3", "今日下线"),
	DETAIL_STATUS_TOM_DOWNLINE		("DS4", "明日下线"),
	DETAIL_STATUS_DOWNLINE			("DS5", "已下线"),
	;
	
	private String value;
    private String name;

    GoodsStatusEnum(String value, String name){
        this.value = value;
        this.name = name;
    }

    public static String getNameByStatus(String value){
        for (GoodsStatusEnum goodsStatus : GoodsStatusEnum.values()) {
            if (goodsStatus.value.equals(value)) {
                return goodsStatus.name;
            }
        }
        return "";
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
