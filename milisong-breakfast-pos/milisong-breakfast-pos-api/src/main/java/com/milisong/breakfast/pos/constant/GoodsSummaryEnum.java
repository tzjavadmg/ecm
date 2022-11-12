package com.milisong.breakfast.pos.constant;

/**
 * 门店状态枚举
 * @author youxia 2018年9月4日
 */
public enum GoodsSummaryEnum {

	/**
	 * 订单状态 1正常 0退款
	 */
	NORMAL				((byte)1, "正常"),
	REFUND				((byte)0, "已停业")

	;

	private Byte value;
    private String name;

    GoodsSummaryEnum(Byte value, String name){
        this.value = value;
        this.name = name;
    }

    public static String getNameByStatus(Byte value){
        for (GoodsSummaryEnum shopStatusEnum : GoodsSummaryEnum.values()) {
            if (shopStatusEnum.value.equals(value)) {
                return shopStatusEnum.name;
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
