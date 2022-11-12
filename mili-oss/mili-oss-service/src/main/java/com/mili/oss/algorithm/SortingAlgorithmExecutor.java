package com.mili.oss.algorithm;

import com.mili.oss.algorithm.model.GoodsModel;
import com.mili.oss.algorithm.model.GoodsPackModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ Description：算法执行器
 * @ Author     ：zengyuekang.
 * @ Date       ：2019/2/28 15:10
 */
public class SortingAlgorithmExecutor {

    private SortingAlgorithm sortingAlgorithm;

    private SortingAlgorithmContext sortingAlgorithmContext;

    public SortingAlgorithmExecutor(List<GoodsModel> goodsModelList, BigDecimal ordersetMaxAmount, BigDecimal lastOrderSetMinAmount, Integer ordersetMaxMember) {
        sortingAlgorithm = new DefalutSortingAlgorithm();
        sortingAlgorithmContext = UserOrderModelBuilder.buildSortingAlgorithmContext(goodsModelList, ordersetMaxAmount, lastOrderSetMinAmount, ordersetMaxMember);
    }


    public List<GoodsPackModel> calculate() {
        return sortingAlgorithm.calculate(sortingAlgorithmContext);
    }
}
