package com.mili.oss.algorithm;

import com.mili.oss.algorithm.model.UserOrderModel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


/**
 * @ Description：集单算法上下文
 * @ Author     ：zengyuekang.
 * @ Date       ：2019/2/18 16:37
 */
@Getter
@Setter
public class SortingAlgorithmContext {

    private BigDecimal orderSetMaxAmount;

    private BigDecimal lastOrderSetMinAmount;

    private Integer orderSetMaxUserCount;

    //金额超标的用户
    private List<UserOrderModel> overProofUserOrderModelList;
    //金额未超标的用户
    private List<UserOrderModel> unOverProofUserOrderModelList;
}
