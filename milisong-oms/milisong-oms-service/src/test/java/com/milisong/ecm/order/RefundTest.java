package com.milisong.ecm.order;

import com.alibaba.fastjson.JSON;
import com.milisong.oms.domain.Order;
import com.milisong.oms.domain.OrderDelivery;
import com.milisong.oms.service.refund.LunchDefaultAlgorithm;
import com.milisong.oms.service.refund.RefundAlgorithm;
import com.milisong.oms.service.refund.RefundAlgorithmContext;
import com.milisong.pms.prom.dto.OrderAmountDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

/**
 * @ Description：退款算法测试
 * @ Author     ：zengyuekang.
 * @ Date       ：2019/4/26 10:55
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = TestApplication.class)
@Slf4j
public class RefundTest {

    @Test
    public void lunchRullReduce() throws Exception {

        LunchDefaultAlgorithm algt = new LunchDefaultAlgorithm();

        RefundAlgorithmContext refundAlgorithmContext = new RefundAlgorithmContext(null, null, null);

        Order order = new Order();
        order.setTotalPayAmount(new BigDecimal(50.80));
        order.setTotalGoodsActualAmount(new BigDecimal(73.80));
        order.setTotalPackageActualAmount(new BigDecimal(12.00));
        order.setTotalDeliveryActualAmount(new BigDecimal(16.00));
        order.setRedPacketAmount(new BigDecimal(6.00));
        refundAlgorithmContext.setOrder(order);


        OrderAmountDto orderAmountDto = new OrderAmountDto();
        orderAmountDto.setFullReduceAmount(new BigDecimal(45.00));
        orderAmountDto.setRedPackAmount(new BigDecimal(6.00));
        refundAlgorithmContext.setOrderAmountDto(orderAmountDto);

        OrderDelivery orderDelivery = new OrderDelivery();
        orderDelivery.setTotalAmount(new BigDecimal(28.00));
        orderDelivery.setTotalPackageActualAmount(new BigDecimal(4.00));
        orderDelivery.setDeliveryActualPrice(new BigDecimal(4.00));
        orderDelivery.setTotalGoodsActualAmount(new BigDecimal(20.00));
        refundAlgorithmContext.setOrderDelivery(orderDelivery);

        RefundAlgorithm.RefundDto refundDto1 = algt.calculate(refundAlgorithmContext);
        System.out.println(JSON.toJSONString(refundDto1));
//        log.info(JSON.toJSONString(refundDto));

        orderDelivery.setTotalAmount(new BigDecimal(30.00));
        orderDelivery.setTotalGoodsActualAmount(new BigDecimal(22.00));
        orderDelivery.setTotalPackageActualAmount(new BigDecimal(4.00));
        orderDelivery.setDeliveryActualPrice(new BigDecimal(4.00));
        refundAlgorithmContext.setOrderDelivery(orderDelivery);

        RefundAlgorithm.RefundDto refundDto2 = algt.calculate(refundAlgorithmContext);
        System.out.println(JSON.toJSONString(refundDto2));


        orderDelivery.setTotalAmount(new BigDecimal(23.00));
        orderDelivery.setTotalGoodsActualAmount(new BigDecimal(17.00));
        orderDelivery.setTotalPackageActualAmount(new BigDecimal(2.00));
        orderDelivery.setDeliveryActualPrice(new BigDecimal(4.00));
        refundAlgorithmContext.setOrderDelivery(orderDelivery);


        RefundAlgorithm.RefundDto refundDto3 = algt.calculate(refundAlgorithmContext);
        System.out.println(JSON.toJSONString(refundDto3));

        orderDelivery.setTotalAmount(new BigDecimal(20.80));
        orderDelivery.setTotalGoodsActualAmount(new BigDecimal(14.80));
        orderDelivery.setTotalPackageActualAmount(new BigDecimal(2.00));
        orderDelivery.setDeliveryActualPrice(new BigDecimal(4.00));
        refundAlgorithmContext.setOrderDelivery(orderDelivery);

        RefundAlgorithm.RefundDto refundDto4 = algt.calculate(refundAlgorithmContext);
        System.out.println(JSON.toJSONString(refundDto4));

        System.out.println(JSON.toJSONString(order.getTotalPayAmount().subtract(refundDto1.getRefundAmount()).subtract(refundDto2.getRefundAmount()).subtract(refundDto3.getRefundAmount())));
    }

}
