package com.mili.oss.algorithm.model;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @ Description：用户订单模型
 * @ Author     ：zengyuekang.
 * @ Date       ：2019/2/19 15:44
 */
@Getter
@Setter
public class UserOrderModel implements Comparable<UserOrderModel> {

    private Long userId;

    private BigDecimal totalAmount;

    //key为订单ID
    private Map<Long, OrderModel> orderModelMap;

    private BigDecimal addTotalAmount(BigDecimal totalGoodsAmount) {
        if (totalAmount == null) {
            totalAmount = totalGoodsAmount;
        } else {
            totalAmount = totalAmount.add(totalGoodsAmount);
        }
        return totalAmount;
    }

    public BigDecimal addGoodsModel(GoodsModel goodsModel) {
        Long orderId = goodsModel.getOrderId();
        BigDecimal totalAmount = goodsModel.getTotalAmount();

        if (orderModelMap == null) {
            orderModelMap = Maps.newHashMap();
        }
        OrderModel orderModel = orderModelMap.get(orderId);

        if (orderModel == null) {
            orderModel = new OrderModel();
            orderModel.setUserId(this.getUserId());
            orderModel.setOrderId(orderId);
        }

        orderModel.addGoodsModel(goodsModel);
        orderModelMap.put(orderId, orderModel);
        addTotalAmount(totalAmount);
        return totalAmount;
    }

    @Override
    public int compareTo(UserOrderModel other) {
        if (totalAmount == null) {
            return -1;
        }

        if (other == null || other.getTotalAmount() == null) {
            return 1;
        }

        return other.getTotalAmount().compareTo(totalAmount);
    }
}
