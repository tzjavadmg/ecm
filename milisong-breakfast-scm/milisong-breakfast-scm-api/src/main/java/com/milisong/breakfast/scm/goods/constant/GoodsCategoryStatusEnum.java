package com.milisong.breakfast.scm.goods.constant;

/**
 * 商品状态枚举
 * @author benny 2019年1月19日
 */
public enum GoodsCategoryStatusEnum {
	
	/**
	 * 类目状态
	 */
	GOODSCATEGORY_STATUS_USEING 	((byte)1, "使用中"),
	GOODSCATEGORY_STATUS_CLOSE		((byte)0, "已停用"),
	;
	
	private Byte value;
    private String name;

    GoodsCategoryStatusEnum(Byte value, String name){
        this.value = value;
        this.name = name;
    }

    public static String getNameByStatus(Byte value){
        for (GoodsCategoryStatusEnum goodsStatus : GoodsCategoryStatusEnum.values()) {
            if (goodsStatus.value.equals(value)) {
                return goodsStatus.name;
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
