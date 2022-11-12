package com.mili.oss.algorithm;

import com.google.common.collect.Maps;
import com.mili.oss.algorithm.model.GoodsModel;
import com.mili.oss.algorithm.model.UserOrderModel;
import org.assertj.core.util.Lists;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ Description：构建UserOrderModel模型
 * @ Author     ：zengyuekang.
 * @ Date       ：2019/2/28 15:06
 */
public class UserOrderModelBuilder {


    public static SortingAlgorithmContext buildSortingAlgorithmContext(List<GoodsModel> goodsModelList, BigDecimal ordersetMaxAmount,BigDecimal lastOrderSetMinAmount, Integer ordersetMaxMember) {
        Map<Long, UserOrderModel> userOrderModelMap = Maps.newHashMap();
        goodsModelList.forEach(goodsModel -> {
            Long userId = goodsModel.getUserId();
            UserOrderModel userOrderModel = userOrderModelMap.get(userId);
            if (userOrderModel == null) {
                userOrderModel = new UserOrderModel();
                userOrderModelMap.put(userId, userOrderModel);
            }
            userOrderModel.setUserId(userId);
            userOrderModel.addGoodsModel(goodsModel);
        });

        List<UserOrderModel> overProofUserOrderModelList = Lists.newArrayList();
        List<UserOrderModel> unOverProofUserOrderModelList = Lists.newArrayList();

        userOrderModelMap.values().forEach(userOrderModel -> {
            if(userOrderModel.getTotalAmount().compareTo(ordersetMaxAmount)>0){
                overProofUserOrderModelList.add(userOrderModel);
            }else{
                unOverProofUserOrderModelList.add(userOrderModel);
            }
        });

        SortingAlgorithmContext sortingAlgorithmContext = new SortingAlgorithmContext();
        sortingAlgorithmContext.setOrderSetMaxAmount(ordersetMaxAmount);
        sortingAlgorithmContext.setLastOrderSetMinAmount(lastOrderSetMinAmount);
        sortingAlgorithmContext.setOrderSetMaxUserCount(ordersetMaxMember);
        sortingAlgorithmContext.setOverProofUserOrderModelList(overProofUserOrderModelList);
        sortingAlgorithmContext.setUnOverProofUserOrderModelList(unOverProofUserOrderModelList);
        return sortingAlgorithmContext;
    }
}
