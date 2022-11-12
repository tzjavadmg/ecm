package com.milisong.pos.production.constant;

/**
 * @author zhaozhonghui
 * @date 2018-10-26
 */
public class ShunfengStatusConstant {

    /** 店铺类型 0顺丰 1第三方 */
    public static class ShopType {
        public final static byte SHUNFENG = 1;
        public final static byte ORTHER = 2;
    }
    /** 1、美团；2、饿了么；3、百度；4、口碑；其他：直接字符串 */
    public static class OrderSource {
        public final static String MILISONG = "米立送";
    }

    /** 支付方式 1、在线支付 0、货到付款 */
    public static class PayType {
        public final static byte ARRIVED_PAY = 0;
        public final static byte ONLINE = 1;
    }

    /** 0、非预约单；1、预约单 */
    public static class AppointStatus {
        public final static byte NORMAL = 0;
        public final static byte PREPRODUCTION = 1;
    }

    /** 1送餐; 2送药; 3百货;
     4脏衣收; 5干净衣派;
     6生鲜; 7保单;8饮品；
     9现场勘查；99其他*/
    public static class ProductType {
        public final static byte FOOD = 1;
    }

    /** 商品重量(克) */
    public static class GoodsWeightGram {
        public final static int FOOD = 420;
    }

    /** 坐标系 */
    public static class LbsType {
        public final static byte BAIDU = 1;
        public final static byte GAODE = 2;
    }
}
