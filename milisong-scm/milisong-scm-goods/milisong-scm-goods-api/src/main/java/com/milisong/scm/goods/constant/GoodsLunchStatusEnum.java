package com.milisong.scm.goods.constant;

public enum GoodsLunchStatusEnum {

	/**
	 * 商品状态信息
	 */
	STATUS_USEING					((byte)1, "使用中"),
	STATUS_CLOSE					((byte)2, "已停用"),

	;
	
	private Byte value;
    private String name;

    GoodsLunchStatusEnum(Byte value, String name){
        this.value = value;
        this.name = name;
    }

    public static String getNameByStatus(Byte value){
        for (GoodsLunchStatusEnum goodsStatus : GoodsLunchStatusEnum.values()) {
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
