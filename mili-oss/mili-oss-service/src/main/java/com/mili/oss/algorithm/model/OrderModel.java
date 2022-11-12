package com.mili.oss.algorithm.model;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Sets;

import java.math.BigDecimal;
import java.util.Set;

/**
 * @ Description：用户订单集
 * @ Author     ：zengyuekang.
 * @ Date       ：2019/2/18 16:44
 */
@Getter
@Setter
@Slf4j
public class OrderModel {

    private Long userId;

    private Long orderId;

    private BigDecimal totalAmount;

    private Set<GoodsModel> goodsModelSet;

    private BigDecimal addTotalAmount(BigDecimal totalGoodsAmount) {
        if (totalAmount == null) {
            totalAmount = totalGoodsAmount;
        } else {
            totalAmount = totalAmount.add(totalGoodsAmount);
        }
        return totalAmount;
    }

    public Integer addGoodsModel(GoodsModel goodsModel) {
        if (CollectionUtils.isEmpty(goodsModelSet)) {
            goodsModelSet = Sets.newHashSet();
        }
        GoodsModel sameIdGoodsModel = goodsModelSet.stream().filter(goodsModel1 -> goodsModel1.equals(goodsModel)).findFirst().orElse(null);

        if (sameIdGoodsModel == null) {
            this.goodsModelSet.add(goodsModel);
        } else {
            sameIdGoodsModel.setGoodsCount(sameIdGoodsModel.getGoodsCount() + 1);
            sameIdGoodsModel.setTotalAmount(sameIdGoodsModel.getTotalAmount().add(goodsModel.getTotalAmount()));
        }
        this.addTotalAmount(goodsModel.getTotalAmount());
        return this.goodsModelSet.size();
    }
}
