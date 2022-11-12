package com.milisong.ecm.order;

import com.farmland.core.util.BeanMapper;
import com.milisong.oms.constant.OrderMode;
import com.milisong.oms.constant.OrderStatus;
import com.milisong.oms.constant.PaymentStatus;
import com.milisong.oms.domain.*;
import com.milisong.oms.mapper.*;
import com.milisong.oms.mq.MessageProducer;
import com.milisong.oms.mq.PaymentMessage;
import com.milisong.oms.service.groupbuy.RealOrderBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/23 16:22
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestApplication.class)
public class GroupBuyOrderTest {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OrderDeliveryMapper orderDeliveryMapper;
    @Resource
    private OrderDeliveryGoodsMapper orderDeliveryGoodsMapper;

    @Resource
    private GroupBuyOrderMapper groupBuyOrderMapper;
    @Resource
    private GroupBuyOrderDeliveryMapper groupBuyOrderDeliveryMapper;
    @Resource
    private GroupBuyOrderDeliveryGoodsMapper groupBuyOrderDeliveryGoodsMapper;


    public void createRealOrder(Long groupBuyId) {
        List<GroupBuyOrder> groupBuyOrderList = groupBuyOrderMapper.findPaidOrderListByGroupBuyId(groupBuyId);
        groupBuyOrderList.stream().forEach(order -> {
            Order realOrder = BeanMapper.map(order, Order.class);
            realOrder.setOrderMode(OrderMode.GROUP_BUY.getValue());
            List<GroupBuyOrderDelivery> groupBuyOrderDeliveryList = groupBuyOrderDeliveryMapper.findDeliveryListByOrderId(order.getId());
            List<OrderDelivery> deliveryList = BeanMapper.mapList(groupBuyOrderDeliveryList, OrderDelivery.class);

            List<Long> deliveryIdList = deliveryList.stream().map(OrderDelivery::getId).collect(Collectors.toList());
            List<GroupBuyOrderDeliveryGoods> groupBuyOrderDeliveryGoodsList = groupBuyOrderDeliveryGoodsMapper.batchFindByDeliveryIds(deliveryIdList);
            List<OrderDeliveryGoods> orderDeliveryGoodsList= BeanMapper.mapList(groupBuyOrderDeliveryGoodsList, OrderDeliveryGoods.class);

            orderMapper.insert(realOrder);
            orderDeliveryMapper.batchSave(deliveryList);
            orderDeliveryGoodsMapper.batchSave(orderDeliveryGoodsList);

            order.setStatus(OrderStatus.COMPLETED.getValue());
            groupBuyOrderMapper.updateByPrimaryKeySelective(order);
        });

    }

    @Test
    public void testCreateRealOrder() {
        createRealOrder(2874641419935744L);
    }
}
