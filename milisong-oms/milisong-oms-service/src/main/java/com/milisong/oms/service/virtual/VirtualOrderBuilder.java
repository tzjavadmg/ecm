package com.milisong.oms.service.virtual;

import com.alibaba.fastjson.JSON;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.oms.constant.OrderStatus;
import com.milisong.oms.constant.OrderType;
import com.milisong.oms.domain.VirtualOrder;
import com.milisong.oms.domain.VirtualOrderDelivery;
import com.milisong.oms.domain.VirtualOrderDeliveryGoods;
import com.milisong.oms.dto.VirtualOrderDeliveryDto;
import com.milisong.oms.dto.VirtualOrderDeliveryGoodsDto;
import com.milisong.oms.dto.VirtualOrderDto;
import com.milisong.oms.param.VirtualOrderDeliveryGoodsParam;
import com.milisong.oms.param.VirtualOrderDeliveryParam;
import com.milisong.oms.param.VirtualOrderParam;
import com.milisong.oms.util.WeekDayUtils;
import com.milisong.pms.prom.dto.OrderAmountDto;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/3 14:39
 */
@Slf4j
@Component
public class VirtualOrderBuilder {

    public VirtualOrderModel buildVirtualOrderModel(VirtualOrderParam virtualOrderParam) {
        log.info("-------------构建虚拟订单模型，入参：{}", JSON.toJSONString(virtualOrderParam));
        VirtualOrderModel virtualOrderModel = new VirtualOrderModel();
        VirtualOrder virtualOrder = new VirtualOrder();
        List<VirtualOrderDelivery> deliveryList = new ArrayList<>();
        List<VirtualOrderDeliveryGoods> deliveryGoodsList = new ArrayList<>();

        final Date orderDate = virtualOrderParam.getOrderDate();
        final Long orderId = IdGenerator.nextId();

        BeanMapper.copy(virtualOrderParam, virtualOrder);
        virtualOrder.setId(orderId);
        virtualOrder.setOrderDate(orderDate);
        virtualOrder.setStatus(OrderStatus.UN_PAID.getValue());

        for (VirtualOrderDeliveryParam deliveryParam : virtualOrderParam.getDeliveries()) {
            VirtualOrderDelivery virtualOrderDelivery = buildVirtualOrderDelivery(orderId, deliveryParam);

            BigDecimal deliveryGoodsTotalAmount = BigDecimal.ZERO;
            for (VirtualOrderDeliveryGoodsParam deliveryGoodsParam : deliveryParam.getDeliveryGoods()) {
                VirtualOrderDeliveryGoods virtualOrderDeliveryGoods = buildVirtualOrderDeliveryGoods(virtualOrderDelivery.getId(), deliveryGoodsParam);
                BigDecimal goodsCount = new BigDecimal(virtualOrderDeliveryGoods.getGoodsCount());
                //商品实价金额
                final BigDecimal goodsOriginalAmount = virtualOrderDeliveryGoods.getGoodsOriginalPrice().multiply(goodsCount);
                final BigDecimal goodsDiscountAmount = virtualOrderDeliveryGoods.getGoodsActualPrice().multiply(goodsCount);
                //打包费按商品数量计算
                BigDecimal packageOriginalPrice = virtualOrderDeliveryGoods.getPackageOriginalPrice();
                BigDecimal packageActualPrice = virtualOrderDeliveryGoods.getPackageActualPrice();
                final BigDecimal packageOriginalAmount = packageOriginalPrice.multiply(goodsCount);
                final BigDecimal packageDiscountAmount = packageActualPrice.multiply(goodsCount);
                virtualOrderDeliveryGoods.setTotalAmount(goodsDiscountAmount);
                deliveryGoodsList.add(virtualOrderDeliveryGoods);
                //累加配送单的商品总金额、打包费总金额、商品总数量
                virtualOrderDelivery.setOrderType(virtualOrderParam.getOrderType());
                virtualOrderDelivery.setTotalGoodsOriginalAmount(add(virtualOrderDelivery.getTotalGoodsOriginalAmount(), goodsOriginalAmount));
                virtualOrderDelivery.setTotalGoodsActualAmount(add(virtualOrderDelivery.getTotalGoodsActualAmount(), goodsDiscountAmount));
                if (OrderType.BREAKFAST.getValue() == virtualOrder.getOrderType()) {
                    //早餐打包费一次配送收一次费用。这里会重复设置，为了代码可读性好点，还是写在这里吧
                    virtualOrderDelivery.setTotalPackageOriginalAmount(virtualOrderDeliveryGoods.getPackageOriginalPrice());
                    virtualOrderDelivery.setTotalPackageActualAmount(virtualOrderDeliveryGoods.getPackageActualPrice());
                } else {
                    //午餐打包费用按商品数量
                    virtualOrderDelivery.setTotalPackageOriginalAmount(add(virtualOrderDelivery.getTotalPackageOriginalAmount(), packageOriginalAmount));
                    virtualOrderDelivery.setTotalPackageActualAmount(add(virtualOrderDelivery.getTotalPackageActualAmount(), packageDiscountAmount));

                }
                virtualOrderDelivery.setTotalGoodsCount(add(virtualOrderDelivery.getTotalGoodsCount(), virtualOrderDeliveryGoods.getGoodsCount()));
                //累加SKU总费用
                deliveryGoodsTotalAmount = deliveryGoodsTotalAmount.add(goodsDiscountAmount);

                //组合商品拆分，组合商品中单品不纳入价格计算
                if (BooleanUtils.isTrue(virtualOrderDeliveryGoods.getIsCombo())) {
                    deliveryGoodsParam.getDeliveryGoodsParamList().forEach(comboGoodsDetailParam -> {
                        VirtualOrderDeliveryGoods comboGoodsDetail = buildVirtualOrderDeliveryGoods(virtualOrderDelivery.getId(), comboGoodsDetailParam, deliveryGoodsParam.getGoodsCount());
                        deliveryGoodsList.add(comboGoodsDetail);
                    });
                }
            }
            //计算配送单总金额，每单配送需要在SKU总费用上再增加配送费用
            deliveryGoodsTotalAmount = deliveryGoodsTotalAmount.add(virtualOrderDelivery.getDeliveryActualPrice()).add(virtualOrderDelivery.getTotalPackageActualAmount());
            virtualOrderDelivery.setTotalAmount(deliveryGoodsTotalAmount);
            deliveryList.add(virtualOrderDelivery);

            //累加订单的商品总金额、打包费总金额、商品总数量、配送费总金额
            virtualOrder.setTotalGoodsOriginalAmount(add(virtualOrder.getTotalGoodsOriginalAmount(), virtualOrderDelivery.getTotalGoodsOriginalAmount()));
            virtualOrder.setTotalGoodsActualAmount(add(virtualOrder.getTotalGoodsActualAmount(), virtualOrderDelivery.getTotalGoodsActualAmount()));
            virtualOrder.setTotalPackageOriginalAmount(add(virtualOrder.getTotalPackageOriginalAmount(), virtualOrderDelivery.getTotalPackageOriginalAmount()));
            virtualOrder.setTotalPackageActualAmount(add(virtualOrder.getTotalPackageActualAmount(), virtualOrderDelivery.getTotalPackageActualAmount()));
            virtualOrder.setTotalDeliveryOriginalAmount(add(virtualOrder.getTotalDeliveryOriginalAmount(), virtualOrderDelivery.getDeliveryOriginalPrice()));
            virtualOrder.setTotalDeliveryActualAmount(add(virtualOrder.getTotalDeliveryActualAmount(), virtualOrderDelivery.getDeliveryActualPrice()));
            virtualOrder.setTotalGoodsCount(add(virtualOrder.getTotalGoodsCount(), virtualOrderDelivery.getTotalGoodsCount()));
            virtualOrder.setTotalOrderDays(add(virtualOrder.getTotalOrderDays(), 1));
        }

        BigDecimal orderTotalAmount = BigDecimal.ZERO;
        orderTotalAmount = orderTotalAmount.add(virtualOrder.getTotalGoodsActualAmount());
        orderTotalAmount = orderTotalAmount.add(virtualOrder.getTotalPackageActualAmount());
        orderTotalAmount = orderTotalAmount.add(virtualOrder.getTotalDeliveryActualAmount());
        virtualOrder.setTotalPayAmount(orderTotalAmount);

        virtualOrderModel.setVirtualOrder(virtualOrder);
        virtualOrderModel.setDeliveryList(deliveryList);
        virtualOrderModel.setDeliveryGoodsList(deliveryGoodsList);
        return virtualOrderModel;
    }

    private VirtualOrderDelivery buildVirtualOrderDelivery(Long orderId, VirtualOrderDeliveryParam deliveryParam) {
        final long deliveryId = IdGenerator.nextId();

        BigDecimal deliveryOriginalPrice = deliveryParam.getDeliveryOriginalPrice();
        BigDecimal deliveryActualPrice = deliveryParam.getDeliveryActualPrice();

        VirtualOrderDelivery virtualOrderDelivery = new VirtualOrderDelivery();
        virtualOrderDelivery.setId(deliveryId);
        virtualOrderDelivery.setOrderId(orderId);
        virtualOrderDelivery.setDeliveryDate(deliveryParam.getDeliveryDate());
        virtualOrderDelivery.setDeliveryOriginalPrice(getNotNullValue(deliveryOriginalPrice));
        virtualOrderDelivery.setDeliveryActualPrice(getNotNullValue(deliveryActualPrice));
        return virtualOrderDelivery;
    }

    private BigDecimal add(BigDecimal one, BigDecimal other) {
        if (one == null) {
            one = BigDecimal.ZERO;
        }
        if (other == null) {
            other = BigDecimal.ZERO;
        }

        return one.add(other);
    }

    private Integer add(Integer one, Integer other) {
        if (one == null) {
            one = 0;
        }
        if (other == null) {
            other = 0;
        }
        return one + other;
    }

    private VirtualOrderDeliveryGoods buildVirtualOrderDeliveryGoods(Long deliveryId, VirtualOrderDeliveryGoodsParam deliveryGoodsParam) {
        return buildVirtualOrderDeliveryGoods(deliveryId, deliveryGoodsParam, null);
    }

    private VirtualOrderDeliveryGoods buildVirtualOrderDeliveryGoods(Long deliveryId, VirtualOrderDeliveryGoodsParam deliveryGoodsParam, Integer comboGoodsCount) {
        String goodsCode = deliveryGoodsParam.getGoodsCode();
        Integer goodsCount = deliveryGoodsParam.getGoodsCount();

        //商品实际价格
        final BigDecimal packageOriginalPrice = deliveryGoodsParam.getPackageOriginalPrice();
        final BigDecimal packageActualPrice = deliveryGoodsParam.getPackageActualPrice();
        final BigDecimal goodsOriginalPrice = deliveryGoodsParam.getGoodsOriginalPrice();
        final BigDecimal goodsDiscountPrice = deliveryGoodsParam.getGoodsActualPrice();
        final BigDecimal goodsActualPrice = (goodsDiscountPrice != null && goodsDiscountPrice.doubleValue() > 0 ? goodsDiscountPrice : goodsOriginalPrice);

        VirtualOrderDeliveryGoods virtualOrderDeliveryGoods = BeanMapper.map(deliveryGoodsParam, VirtualOrderDeliveryGoods.class);
        virtualOrderDeliveryGoods.setId(IdGenerator.nextId());
        virtualOrderDeliveryGoods.setDeliveryId(deliveryId);
        virtualOrderDeliveryGoods.setGoodsCode(goodsCode);
        if (comboGoodsCount != null) {
            virtualOrderDeliveryGoods.setGoodsCount(goodsCount * comboGoodsCount);
        } else {
            virtualOrderDeliveryGoods.setGoodsCount(goodsCount);
        }
        virtualOrderDeliveryGoods.setGoodsActualPrice(getNotNullValue(goodsActualPrice));
        virtualOrderDeliveryGoods.setPackageActualPrice(getNotNullValue(packageActualPrice));
        virtualOrderDeliveryGoods.setPackageOriginalPrice(getNotNullValue(packageOriginalPrice));

        return virtualOrderDeliveryGoods;
    }

    private BigDecimal getNotNullValue(BigDecimal goodsActualPrice) {
        return goodsActualPrice == null ? BigDecimal.ZERO : goodsActualPrice;
    }

    public VirtualOrderDto buildVirtualOrderDto(VirtualOrderModel virtualOrderModel, OrderAmountDto orderAmountDto) {
        log.info("构建虚拟订单，入参：{}", JSON.toJSONString(virtualOrderModel));
        final VirtualOrder virtualOrder = virtualOrderModel.getVirtualOrder();
        final List<VirtualOrderDelivery> deliveryList = virtualOrderModel.getDeliveryList();
        final List<VirtualOrderDeliveryGoods> deliveryGoodsList = virtualOrderModel.getDeliveryGoodsList();
        final VirtualOrderDto virtualOrderDto = BeanMapper.map(virtualOrder, VirtualOrderDto.class);
        final Map<Long, List<VirtualOrderDeliveryGoods>> goodsByDeliveryIdMap =
                deliveryGoodsList.stream().collect(groupingBy(VirtualOrderDeliveryGoods::getDeliveryId));

        virtualOrderDto.setOrderDate(DateFormatUtils.format(virtualOrder.getOrderDate(), "yyyy-MM-dd"));

        final List<VirtualOrderDeliveryDto> deliveries =
                deliveryList.stream().map(delivery -> {
                    Date deliveryDate = delivery.getDeliveryDate();
                    VirtualOrderDeliveryDto virtualOrderDeliveryDto = BeanMapper.map(delivery, VirtualOrderDeliveryDto.class);
                    virtualOrderDeliveryDto.setDeliveryDate(DateFormatUtils.format(deliveryDate, "yyyy-MM-dd"));
                    String dayDesc = WeekDayUtils.getWeekDayString(delivery.getDeliveryDate());
                    virtualOrderDeliveryDto.setDayDesc(dayDesc);
                    List<VirtualOrderDeliveryGoods> deliveryGoods = goodsByDeliveryIdMap.get(delivery.getId());
                    deliveryGoods = deliveryGoods.stream().filter(goods -> StringUtils.isEmpty(goods.getComboGoodsCode())).collect(Collectors.toList());
                    virtualOrderDeliveryDto.setDeliveryGoods(BeanMapper.mapList(deliveryGoods, VirtualOrderDeliveryGoodsDto.class));
                    return virtualOrderDeliveryDto;
                }).collect(Collectors.toList());

        virtualOrderDto.setDeliveries(deliveries);
        virtualOrderDto.setUsePoints(orderAmountDto.getUsePoints());
        return virtualOrderDto;
    }

    @Getter
    @Setter
    class VirtualOrderModel {
        private VirtualOrder virtualOrder;
        private List<VirtualOrderDelivery> deliveryList;
        private List<VirtualOrderDeliveryGoods> deliveryGoodsList;
    }
}
