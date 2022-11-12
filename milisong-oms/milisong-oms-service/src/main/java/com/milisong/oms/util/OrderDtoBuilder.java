package com.milisong.oms.util;

import com.alibaba.fastjson.JSON;
import com.farmland.core.util.BeanMapper;
import com.milisong.oms.domain.*;
import com.milisong.oms.dto.OrderDeliveryDto;
import com.milisong.oms.dto.OrderDeliveryGoodsDto;
import com.milisong.oms.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/8 11:00
 */
@Slf4j
public class OrderDtoBuilder {

    public static OrderDto buildOrderDto(Order order, List<OrderDelivery> deliveries, List<OrderDeliveryGoods> deliveryGoods) {
        log.info("构建订单视图信息-------------主订单信息：{},订单配送集信息：{}，配送商品集信息：{}", JSON.toJSONString(order), JSON.toJSONString(deliveries), JSON.toJSONString(deliveryGoods));
        final OrderDto orderDto = BeanMapper.map(order, OrderDto.class);

        final Map<Long, List<OrderDeliveryGoods>> goodsByDeliveryIdMap =
                deliveryGoods.stream().collect(groupingBy(OrderDeliveryGoods::getDeliveryId));

        final List<OrderDeliveryDto> deliveryDtos =
                deliveries.stream().filter(delivery -> CollectionUtils.isNotEmpty(goodsByDeliveryIdMap.get(delivery.getId())))
                        .map(delivery -> {
                            OrderDeliveryDto deliveryDto = BeanMapper.map(delivery, OrderDeliveryDto.class);
                            deliveryDto.setDeliveryDate(DateFormatUtils.format(delivery.getDeliveryDate(), "yyyy-MM-dd"));
                            String dayDesc = WeekDayUtils.getWeekDayString(delivery.getDeliveryDate());
                            deliveryDto.setDayDesc(dayDesc);

                            final List<OrderDeliveryGoodsDto> orderGoods = goodsByDeliveryIdMap.get(delivery.getId()).stream()
                                    .filter(goods -> StringUtils.isEmpty(goods.getComboGoodsCode()))
                                    .map(goods -> {
                                        return BeanMapper.map(goods, OrderDeliveryGoodsDto.class);
                                    }).collect(Collectors.toList());
                            deliveryDto.setDeliveryGoods(orderGoods);
                            return deliveryDto;
                        }).collect(Collectors.toList());

        orderDto.setDeliveries(deliveryDtos);
        log.info("-----------------构建订单视图信息,orderDto：{}", JSON.toJSONString(orderDto));
        return orderDto;
    }

    public static OrderDto buildOrderDto(VirtualOrder order, List<VirtualOrderDelivery> deliveries, List<VirtualOrderDeliveryGoods> deliveryGoods) {
        return buildOrderDto(BeanMapper.map(order, Order.class), BeanMapper.mapList(deliveries, OrderDelivery.class), BeanMapper.mapList(deliveryGoods, OrderDeliveryGoods.class));
    }
}
