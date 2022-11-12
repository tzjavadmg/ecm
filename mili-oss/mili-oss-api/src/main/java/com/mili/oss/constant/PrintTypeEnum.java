package com.mili.oss.constant;

/**
 * @program: mili-oss
 * @description: 打印类型
 * @author: liuyy
 * @date: 2019/5/23 21:04
 */
public enum PrintTypeEnum {
/*    SYSTEMPRINT(0, "自动集单"),
    ALLPRINT(1, "打印全部"),
    DELIVERYPRINT(2, "配送联"),
    CONSUMEPRINT(3, "顾客联");*/
    SYSTEMPRINT(4, "自动集单"),
    ALLPRINT(0, "打印全部"),
    DELIVERYPRINT(1, "配送联"),
    CONSUMEPRINT(2, "顾客联");
    private Integer value;
    private String name;

    PrintTypeEnum(int value, String name){
        this.value = value;
        this.name = name;
    }

    public static String getNameByStatus(Integer value){
        for (PrintTypeEnum printTypeEnum : PrintTypeEnum.values()) {
            if (printTypeEnum.value == value) {
                return printTypeEnum.name;
            }
        }
        return "";
    }

    public Integer getValue() {
        return value;
    }

/*    public void setValue(int value) {
        this.value = value;
    }*/

    public String getName() {
        return name;
    }

 /*   public void setName(String name) {
        this.name = name;
    }*/

}
