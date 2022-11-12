package com.mili.oss.algorithm.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;

/**
 * @ Description：商品信息
 * @ Author     ：zengyuekang.
 * @ Date       ：2019/2/18 20:45
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"goodsId"})
public class GoodsModel implements Comparable<GoodsModel> {

    private Long userId;

    private Long goodsId;

    private Long orderId;

    private Integer goodsCount;

    private BigDecimal totalAmount;

    @Override
    public int compareTo(GoodsModel other) {
        if (totalAmount == null) {
            return -1;
        }

        if (other == null || other.getTotalAmount() == null) {
            return 1;
        }

        return other.getTotalAmount().compareTo(totalAmount);
    }
}
