package com.mili.oss.algorithm;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mili.oss.algorithm.model.GoodsModel;
import com.mili.oss.algorithm.model.GoodsPackModel;
import com.mili.oss.algorithm.model.OrderModel;
import com.mili.oss.algorithm.model.UserOrderModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * @ Description：默认算法（贪婪算法）
 * @ Author     ：zengyuekang.
 * @ Date       ：2019/2/18 17:03
 */
@Slf4j
public class DefalutSortingAlgorithm implements SortingAlgorithm {

    @Override
    public List<GoodsPackModel> calculate(SortingAlgorithmContext sortingAlgorithmContext) {
        BigDecimal maxAmount = sortingAlgorithmContext.getOrderSetMaxAmount();
        BigDecimal lastOrderSetMinAmount = sortingAlgorithmContext.getLastOrderSetMinAmount();
        Integer maxUserCount = sortingAlgorithmContext.getOrderSetMaxUserCount();
        List<UserOrderModel> overProofUserOrderModelList = sortingAlgorithmContext.getOverProofUserOrderModelList();
        List<UserOrderModel> unOverProofUserOrderModelList = sortingAlgorithmContext.getUnOverProofUserOrderModelList();

        log.info("@@@@@@@@@@----------最大金额：{}----最大人数：{}", maxAmount, maxUserCount);
        log.info("@@@@@@@@@@----------集单计算----超标用户订单数：{}", overProofUserOrderModelList.size());
        log.info("@@@@@@@@@@----------集单计算----未超标用户订单数：{}", unOverProofUserOrderModelList.size());
        if (CollectionUtils.isNotEmpty(overProofUserOrderModelList)) {
            //搜集所有超标用户产生的集单
            List<UserOrderModel> userOrderModelList = overProofUserOrderModelList.stream().flatMap(userOrderModel -> {
                //将单个用户的商品扁平化成一个集合
                List<GoodsModel> goodsModelList = userOrderModel.getOrderModelMap().values().stream().flatMap(orderModel -> {
                    return orderModel.getGoodsModelSet().stream();
                }).collect(toList());

                return splitOrder(userOrderModel.getUserId(), goodsModelList, maxAmount).stream();
            }).collect(toList());
            unOverProofUserOrderModelList.addAll(userOrderModelList);
        }

        return sorting(unOverProofUserOrderModelList, maxAmount, lastOrderSetMinAmount, maxUserCount);
    }

    /**
     * 处理未超标的用户,未超标用户不允许拆单
     *
     * @param userOrderModelList
     * @param maxGoodsAmount
     * @param maxUserCount
     * @return
     */
    private List<GoodsPackModel> sorting(List<UserOrderModel> userOrderModelList, BigDecimal maxGoodsAmount, BigDecimal lastOrderSetMinAmount, Integer maxUserCount) {
        userOrderModelList = userOrderModelList.stream().sorted().collect(toList());

        List<GoodsPackModel> goodsPackList = Lists.newArrayList();
        int packId = 0;

        List<GoodsPackModel> tmpGoodsPackModelList = Lists.newArrayList();
        for (UserOrderModel userOrderModel : userOrderModelList) {
            GoodsPackModel curGoodsPackModel = tmpGoodsPackModelList.stream().filter(goodsPackModel -> {
                        //集单余额
                        BigDecimal packageBalance = maxGoodsAmount.subtract(goodsPackModel.getTotalGoodsAmount());
                        //找出当前商品支付金额小于余额的集单
                        return userOrderModel.getTotalAmount().compareTo(packageBalance) <= 0;
                    }
            ).findFirst().orElse(null);

            //如果找不到符合条件的集单，则新开一个集单
            if (curGoodsPackModel == null) {
                curGoodsPackModel = new GoodsPackModel();
                curGoodsPackModel.setId(++packId);
                tmpGoodsPackModelList.add(curGoodsPackModel);
            }
            curGoodsPackModel.addTotalGoodsAmount(userOrderModel.getTotalAmount());
            curGoodsPackModel.addUserId(userOrderModel.getUserId());

            curGoodsPackModel.addOrderIdSet(userOrderModel.getOrderModelMap().values().stream().map(OrderModel::getOrderId).collect(toSet()));
            curGoodsPackModel.addGoodsModelSet(userOrderModel.getOrderModelMap().values().stream().flatMap(orderModel ->
                    orderModel.getGoodsModelSet().stream()
            ).collect(toSet()));
            if (curGoodsPackModel.getUserIdSet().size() == maxUserCount) {
                goodsPackList.add(curGoodsPackModel);
                log.info("=================#############==============新完成集单ID:{}，金额：{}，人数：{}，订单数：{}", curGoodsPackModel.getId(), curGoodsPackModel.getTotalGoodsAmount(), curGoodsPackModel.getUserIdSet().size(), curGoodsPackModel.getOrderIdSet().size());
                tmpGoodsPackModelList.remove(curGoodsPackModel);
            }
        }

        goodsPackList.addAll(tmpGoodsPackModelList);

        mergeSmallestGoodsPack(goodsPackList, lastOrderSetMinAmount);

        return goodsPackList;
    }

    private void mergeSmallestGoodsPack(final List<GoodsPackModel> goodsPackList, BigDecimal lastOrderSetMinAmount) {
        if (CollectionUtils.isEmpty(goodsPackList)) {
            return;
        }
        log.info("-------------集单结果：{}", JSON.toJSONString(goodsPackList));

        int goodsPackCount = goodsPackList.size();
        GoodsPackModel lastGoodsPackModel = goodsPackList.get(goodsPackCount - 1);
        BigDecimal lastOrderSetAmount = lastGoodsPackModel.getTotalGoodsAmount();
        if (lastOrderSetAmount.compareTo(lastOrderSetMinAmount) <= 0 && goodsPackList.size() > 1) {
            //如果最后一个集单金额小于阀值，则从集合中删除最后一个集单，将最后一 个集单与剩余集单中金额最小的合并
            goodsPackList.remove(goodsPackCount - 1);
            GoodsPackModel minGoodsPackModel = goodsPackList.stream().min(new Comparator<GoodsPackModel>() {
                @Override
                public int compare(GoodsPackModel o1, GoodsPackModel o2) {
                    return o1.getTotalGoodsAmount().compareTo(o2.getTotalGoodsAmount());
                }
            }).orElse(null);
            log.info("-------------------需要合并的最小集单为：{}", JSON.toJSONString(minGoodsPackModel));
            if (minGoodsPackModel != null) {
                minGoodsPackModel.addGoodsModelSet(lastGoodsPackModel.getGoodsModelSet());
                minGoodsPackModel.addOrderIdSet(lastGoodsPackModel.getOrderIdSet());
                minGoodsPackModel.addUserIdSet(lastGoodsPackModel.getUserIdSet());
                minGoodsPackModel.addTotalGoodsAmount(lastGoodsPackModel.getTotalGoodsAmount());
            }
            log.info("-------------最后一笔集单合并后结果：{}", JSON.toJSONString(goodsPackList));
        }
    }

    /**
     * 处理单个用户订单总额超标的
     *
     * @param goodsModelList
     * @param maxGoodsAmount
     */
    private List<UserOrderModel> splitOrder(Long userId, List<GoodsModel> goodsModelList, BigDecimal maxGoodsAmount) {
        //开启的商品包(集单)
        List<UserOrderModel> userOrderModelList = new ArrayList<>();

        if (CollectionUtils.isEmpty(goodsModelList)) {
            log.warn("---------------商品集为空！");
            return userOrderModelList;
        }

        //将所有商品按金额降序排列
        List<GoodsModel> sortedGoodsModelList = goodsModelList.stream().flatMap(goodsModel -> {
            BigDecimal totalAmount = goodsModel.getTotalAmount();
            Integer goodsCount = goodsModel.getGoodsCount();
            if (goodsCount == null || goodsCount <= 1) {
                return Stream.of(goodsModel);
            }
            final List<GoodsModel> newGoodsModelList = Lists.newArrayList();
            for (int i = 0; i < goodsCount; i++) {
                GoodsModel newGoodsModel = new GoodsModel();
                newGoodsModel.setUserId(userId);
                newGoodsModel.setOrderId(goodsModel.getOrderId());
                newGoodsModel.setGoodsId(goodsModel.getGoodsId());
                newGoodsModel.setTotalAmount(totalAmount.divide(new BigDecimal(goodsCount), 2, BigDecimal.ROUND_HALF_UP));
                newGoodsModel.setGoodsCount(1);
                newGoodsModelList.add(newGoodsModel);
            }
            log.info("--------------------拆商品结果：{}", JSON.toJSONString(newGoodsModelList));
            return newGoodsModelList.stream();
        }).sorted().collect(toList());

        //遍历每个商品
        for (GoodsModel goodsModel : sortedGoodsModelList) {
            //获取第一个可以装下当前商品的集单
            UserOrderModel curUserOrderModel = userOrderModelList.stream().filter(userOrderModel -> {
                        //集单余额
                        BigDecimal packageBalance = maxGoodsAmount.subtract(userOrderModel.getTotalAmount());
                        //找出当前商品支付金额小于余额的集单
                        return goodsModel.getTotalAmount().compareTo(packageBalance) <= 0;
                    }
            ).findFirst().orElse(null);
            //如果找不到符合条件的集单，则新开一个集单
            if (curUserOrderModel == null) {
                curUserOrderModel = new UserOrderModel();
                curUserOrderModel.setUserId(userId);
                userOrderModelList.add(curUserOrderModel);
            }
            curUserOrderModel.addGoodsModel(goodsModel);
        }

        log.info("-------------------------------超标用户{}拆单", userId);
        userOrderModelList.forEach(userOrderModel -> {
            log.info("--------------总金额：{}", userOrderModel.getTotalAmount());
            userOrderModel.getOrderModelMap().values().forEach(orderModel -> {
                log.info("--------------订单ID：{}", orderModel.getOrderId());
                orderModel.getGoodsModelSet().forEach(goodsModel -> {
                    log.info("--------------===商品ID：{},商品数量：{}", goodsModel.getGoodsId(), goodsModel.getGoodsCount());
                });
            });

        });

        return userOrderModelList;
    }
}
