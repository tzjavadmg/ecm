package com.mili.oss.algorithm;

import com.mili.oss.algorithm.model.GoodsPackModel;

import java.util.List;

/**
 * @ Description：集单算法接口
 * @ Author     ：zengyuekang.
 * @ Date       ：2019/2/18 16:37
 */
public interface SortingAlgorithm {

    List<GoodsPackModel> calculate(SortingAlgorithmContext refundAlgorithmContext);
}
