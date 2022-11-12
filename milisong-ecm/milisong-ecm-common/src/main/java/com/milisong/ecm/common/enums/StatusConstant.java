package com.milisong.ecm.common.enums;

/**
 * 门店状态枚举
 * @author youxia 2018年9月4日
 */
public class StatusConstant {

    public enum BuildingStatusEnum {
//    楼宇状态0未计划 2待开通 3关闭 9已开通
        /**
         * 门店状态信息
         */
        INIT				(0, "未计划"),
        PREPARE_OPEN        (2, "待开通"),
        CLOSED              (3,"关闭"),
    	OPENED				(9, "已开通"),
    	;

        private int value;
        private String name;

        BuildingStatusEnum(int value, String name){
            this.value = value;
            this.name = name;
        }

        public static String getNameByStatus(int value){
            for (BuildingStatusEnum shopStatusEnum : BuildingStatusEnum.values()) {
                if (shopStatusEnum.value == value) {
                    return shopStatusEnum.name;
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

    public enum CompanyStatusEnum {
//    开通状态 1开通 0关闭
        /**
         * 公司状态信息
         */
        CLOSED              (0,"关闭"),
        OPENED				(1, "已开通"),
        ;

        private int value;
        private String name;

        CompanyStatusEnum(int value, String name){
            this.value = value;
            this.name = name;
        }

        public static String getNameByStatus(int value){
            for (CompanyStatusEnum shopStatusEnum : CompanyStatusEnum.values()) {
                if (shopStatusEnum.value == value) {
                    return shopStatusEnum.name;
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
}
