package com.milisong.oms.service.groupbuy;

import com.alibaba.fastjson.JSON;
import com.farmland.core.util.BeanMapper;
import com.milisong.oms.constant.OrderMode;
import com.milisong.oms.constant.OrderStatus;
import com.milisong.oms.constant.PaymentStatus;
import com.milisong.oms.domain.*;
import com.milisong.oms.mapper.*;
import com.milisong.oms.mq.MessageProducer;
import com.milisong.oms.mq.PaymentMessage;
import com.milisong.oms.service.PaymentMqProcessor;
import com.milisong.oms.service.stock.MonthlySalesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/21 14:21
 */
@Slf4j
@Component
@Transactional(rollbackFor = RuntimeException.class)
public class RealOrderBuilder {

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
    @Resource
    private PaymentMqProcessor paymentMqProcessor;

    public void createRealOrder(Long groupBuyId) {
        List<GroupBuyOrder> groupBuyOrderList = groupBuyOrderMapper.findPaidOrderListByGroupBuyId(groupBuyId);
        log.info("----------------拼团成功，创建真实订单，拼团虚拟订单数据：{}", JSON.toJSONString(groupBuyOrderList));
        groupBuyOrderList.forEach(order -> {
            Order realOrder = BeanMapper.map(order, Order.class);
            realOrder.setOrderMode(OrderMode.GROUP_BUY.getValue());
            List<GroupBuyOrderDelivery> groupBuyOrderDeliveryList = groupBuyOrderDeliveryMapper.findDeliveryListByOrderId(order.getId());
            List<OrderDelivery> deliveryList = BeanMapper.mapList(groupBuyOrderDeliveryList, OrderDelivery.class);

            List<Long> deliveryIdList = deliveryList.stream().map(OrderDelivery::getId).collect(Collectors.toList());
            List<GroupBuyOrderDeliveryGoods> groupBuyOrderDeliveryGoodsList = groupBuyOrderDeliveryGoodsMapper.batchFindByDeliveryIds(deliveryIdList);
            List<OrderDeliveryGoods> orderDeliveryGoodsList = BeanMapper.mapList(groupBuyOrderDeliveryGoodsList, OrderDeliveryGoods.class);

            orderMapper.insert(realOrder);
            log.info("--------------拼团成功，插入真实订单--{}", JSON.toJSONString(realOrder));
            orderDeliveryMapper.batchSave(deliveryList);
            log.info("--------------拼团成功，插入真实配送单--{}", JSON.toJSONString(deliveryList));
            orderDeliveryGoodsMapper.batchSave(orderDeliveryGoodsList);
            log.info("--------------拼团成功，插入真实配送单商品明细--{}", JSON.toJSONString(orderDeliveryGoodsList));

            order.setStatus(OrderStatus.COMPLETED.getValue());
            groupBuyOrderMapper.updateByPrimaryKeySelective(order);

            //记录月售量
            MonthlySalesUtils.monthlySales(orderDeliveryGoodsList);
            //通知供应链集单配送
            paymentMqProcessor.sendMq2Scm(realOrder, deliveryList, orderDeliveryGoodsList);
        });
    }
}
