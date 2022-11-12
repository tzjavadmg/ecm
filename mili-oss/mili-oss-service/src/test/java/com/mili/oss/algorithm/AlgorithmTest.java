//package com.mili.oss.algorithm;
//
//import com.google.common.collect.Lists;
//import com.mili.oss.algorithm.model.GoodsModel;
//import com.mili.oss.algorithm.model.GoodsPackModel;
//import com.mili.oss.algorithm.model.OrderModel;
//import com.mili.oss.algorithm.model.UserOrderModel;
//import org.junit.Test;
//
//import java.math.BigDecimal;
//import java.util.List;
//
///**
// * @ Description：集单算法测试
// * @ Author     ：zengyuekang.
// * @ Date       ：2019/2/19 17:23
// */
//public class AlgorithmTest {
//
//    @Test
//    public void testDefaultAlgt() {
//        DefalutSortingAlgorithm algorithm = new DefalutSortingAlgorithm();
//        SortingAlgorithmContext ctx = new SortingAlgorithmContext();
//        ctx.setOrderSetMaxAmount(new BigDecimal(10));
//        ctx.setOrderSetMaxUserCount(2);
//
////----------------------------------------------------------------
//        UserOrderModel u1 = buildUserOrderModel(1L, "1L", 1L, new BigDecimal(1));
//        UserOrderModel u2 = buildUserOrderModel(2L, "2L", 2L, new BigDecimal(2));
//        UserOrderModel u3 = buildUserOrderModel(3L, "3L", 3L, new BigDecimal(3));
//        UserOrderModel u4 = buildUserOrderModel(4L, "4L", 4L, new BigDecimal(4));
//        UserOrderModel u5 = buildUserOrderModel(5L, "5L", 5L, new BigDecimal(8));
//        List<UserOrderModel> unOverProofUserOrderModelList = Lists.newArrayList(u1, u2, u3, u4, u5);
////----------------------------------------------------------------
//        UserOrderModel u8 = buildUserOrderModel(8L, 8L, "81L", new BigDecimal(2), "82L", new BigDecimal(7), "83L", new BigDecimal(1));
//        UserOrderModel u9 = buildUserOrderModel(9L, 9L, "91L", new BigDecimal(5), "92L", new BigDecimal(7), "93L", new BigDecimal(3));
//        UserOrderModel u10 = buildUserOrderModel(10L, 10L, "101L", new BigDecimal(7), "102L", new BigDecimal(8), "103L", new BigDecimal(2));
//        List<UserOrderModel> overProofUserOrderModelList = Lists.newArrayList(u8, u9, u10);
//
//        ctx.setOverProofUserOrderModelList(overProofUserOrderModelList);
//        ctx.setUnOverProofUserOrderModelList(unOverProofUserOrderModelList);
//
//        List<GoodsPackModel> goodsPackModelList = algorithm.calculate(ctx);
//
//        goodsPackModelList.forEach(goodsPackModel -> {
//            System.out.println("---------------------------------------------------");
//            System.out.println("---集单金额--------：" + goodsPackModel.getTotalGoodsAmount());
//            System.out.println("---集单关联用户--------：" + goodsPackModel.getUserIdSet());
//            System.out.println("---集单关联订单--------：" + goodsPackModel.getOrderIdSet());
//            System.out.println("---集单关联商品--------：" + goodsPackModel.getOrderGoodsIdSet());
//        });
//
//    }
//
//    private UserOrderModel buildUserOrderModel(Long userId, String goodsId, Long orderId, BigDecimal totalAmount) {
//        GoodsModel goodsModel = new GoodsModel();
//        goodsModel.setGoodsId(goodsId);
//        goodsModel.setOrderId(orderId);
//        goodsModel.setTotalAmount(totalAmount);
//
//        OrderModel orderModel = new OrderModel();
//        orderModel.setOrderId(orderId);
//        orderModel.setUserId(userId);
//        orderModel.addGoodsModel(goodsModel);
//
//        UserOrderModel userOrderModel = new UserOrderModel();
//        userOrderModel.setUserId(userId);
//        userOrderModel.addGoodsModel(goodsModel);
//
//        return userOrderModel;
//    }
//
//    private UserOrderModel buildUserOrderModel(Long userId, Long orderId, String goodsId1, BigDecimal totalAmount1, String goodsId2, BigDecimal totalAmount2, String goodsId3, BigDecimal totalAmount3) {
//        GoodsModel g1 = new GoodsModel();
//        g1.setGoodsId(goodsId1);
//        g1.setOrderId(orderId);
//        g1.setTotalAmount(totalAmount1);
//
//        GoodsModel g2 = new GoodsModel();
//        g2.setGoodsId(goodsId2);
//        g2.setOrderId(orderId);
//        g2.setTotalAmount(totalAmount2);
//
//        GoodsModel g3 = new GoodsModel();
//        g3.setGoodsId(goodsId3);
//        g3.setOrderId(orderId);
//        g3.setTotalAmount(totalAmount3);
//
//
//        OrderModel orderModel = new OrderModel();
//        orderModel.setOrderId(orderId);
//        orderModel.setUserId(userId);
////        orderModel.setGoodsModelList(Sets.newHashSet(g1, g2, g3));
//        orderModel.addGoodsModel(g1);
//        orderModel.addGoodsModel(g2);
//        orderModel.addGoodsModel(g3);
//
//        UserOrderModel userOrderModel = new UserOrderModel();
//        userOrderModel.setUserId(userId);
//        userOrderModel.addGoodsModel(g1);
//        userOrderModel.addGoodsModel(g2);
//        userOrderModel.addGoodsModel(g3);
//
//        return userOrderModel;
//    }
//}
