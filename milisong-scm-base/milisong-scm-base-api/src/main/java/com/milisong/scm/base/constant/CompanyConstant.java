package com.milisong.scm.base.constant;

/**
 * 门店状态枚举
 * @author youxia 2018年9月4日
 */
public class CompanyConstant {

    public enum BusinessLine {
//    楼宇状态0未计划 2待开通 3关闭 9已开通
        /**
         * 门店状态信息
         */
        BREAKFAST				((byte)0, "早餐"),
    	LUNCH				((byte)1, "午餐")
    	;

        private Byte value;
        private String name;

        BusinessLine(Byte value, String name){
            this.value = value;
            this.name = name;
        }

        public static String getNameByStatus(Byte value){
            for (BusinessLine businessLine : BusinessLine.values()) {
                if (businessLine.value.equals(value)) {
                    return businessLine.name;
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
}
