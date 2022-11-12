package com.milisong.scm.orderset.constant;

/**
 * 订单状态枚举（和ECM并不保持一致）
 * 0:配送中 1:已打包-待配送 2:已支付-备餐中 3:已申请退款 4:已退款 9:已完成
 * @author youxia 2018年9月3日
 */
public enum OrderStatusEnum {
	
	DELIVERYING(0, "配送中"),
	WAITDELIVERY(1, "待配送"),
	WAITPACK(2, "待打包"),
	REFUND(3, "申请退款"),
	REFUNDED(4, "已退款"),
	COMPLETE(9, "完成"),
	;
	
	private Integer value;
    private String name;

    OrderStatusEnum(int value, String name){
        this.value = value;
        this.name = name;
    }

    public static String getNameByStatus(Integer value){
        for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
            if (orderStatusEnum.value == value) {
                return orderStatusEnum.name;
            }
        }
        return "";
    }

    public Integer getValue() {
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
