package com.milisong.oms.service.refund;

import com.milisong.oms.constant.OrderType;
import com.milisong.oms.domain.Order;
import com.milisong.pms.prom.dto.OrderAmountDto;
import com.milisong.pms.prom.enums.CouponEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * @ Description：退款策略工厂
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/25 11:26
 */
@Slf4j
class RefundAlgorithmFactory {

    static RefundAlgorithm createRefundStrategy(OrderAmountDto orderAmountDto, Order order) {
        if (OrderType.BREAKFAST.getValue() == order.getOrderType()) {
            if (CouponEnum.TYPE.GOODS.getCode().equals(orderAmountDto.getCouponType())) {
                log.info("--------------早餐商品券退款算法------------");
                return new BreakfastGoodsCouponAlgorithm();
            } else if (CouponEnum.TYPE.NEW_DISCOUNT.getCode().equals(orderAmountDto.getCouponType()) || CouponEnum.TYPE.DISCOUNT.getCode().equals(orderAmountDto.getCouponType())) {
                log.info("--------------早餐折扣券退款算法------------");
                return new BreakfastDiscountCouponAlgorithm();
            } else {
                log.info("--------------早餐默认退款算法------------");
                return new DefaultAlgorithm();
            }
        } else if (OrderType.LUNCH.getValue() == order.getOrderType()) {
            log.info("--------------午餐退款算法------------");
            return new LunchDefaultAlgorithm();
        }
        log.info("--------------全局默认退款算法------------");
        return new DefaultAlgorithm();
    }
}
