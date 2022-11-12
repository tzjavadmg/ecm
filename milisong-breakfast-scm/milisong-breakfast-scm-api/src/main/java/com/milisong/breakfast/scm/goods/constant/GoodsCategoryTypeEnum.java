package com.milisong.breakfast.scm.goods.constant;

/**
 * 商品状态枚举
 * @author benny 2019年1月19日
 */
public enum GoodsCategoryTypeEnum {
	
	/**
	 * 类目状态
	 */
	GOODSCATEGORY_TYPE_SINGLE 	((byte)1, "单品"),
	GOODSCATEGORY_TYPE_PACKAGE		((byte)2, "套餐"),
	;
	
	private Byte value;
    private String name;

    GoodsCategoryTypeEnum(Byte value, String name){
        this.value = value;
        this.name = name;
    }

    public static String getNameByStatus(Byte value){
        for (GoodsCategoryTypeEnum goodsStatus : GoodsCategoryTypeEnum.values()) {
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
