package com.milisong.ecm.lunch.order.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.exception.BusinessException;

import com.farmland.core.util.BeanMapper;
import com.milisong.ecm.common.api.ShopConfigService;
import com.milisong.ecm.common.dto.ShopConfigDto;
import com.milisong.ecm.common.enums.GoodsType;
import com.milisong.ecm.common.util.WeekDayUtils;
import com.milisong.ecm.lunch.goods.api.ShopService;
import com.milisong.ecm.lunch.goods.dto.GoodsDto;
import com.milisong.oms.dto.TimezoneDto;
import com.milisong.oms.param.OrderPaymentParam;
import com.milisong.oms.param.VirtualOrderDeliveryParam;
import com.milisong.oms.param.VirtualOrderParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ Description：订单帮助服务
 * @ Author     ：zengyuekang.
 * @ Date       ：2018/12/3 11:18
 */
@Slf4j
@Component
public class OrderHelper {

    @Resource
    private ShopConfigService shopConfigService;
    @Resource
    private ShopService shopService;

    public VirtualOrderParam fillVirtualOrderParam(VirtualOrderParam virtualOrderParam) {
        Date orderDate = new Date();
        virtualOrderParam.setOrderDate(orderDate);
        String shopCode = virtualOrderParam.getShopCode();
        BigDecimal deliveryOriginalPrice = shopConfigService.getDeliveryOriginalPrice(shopCode);
        BigDecimal deliveryActualPrice = shopConfigService.getDeliveryDiscountPrice(shopCode);
        BigDecimal packageOriginalPrice = shopConfigService.getPackageOriginalPrice(shopCode);
        BigDecimal packageActualPrice = shopConfigService.getPackageDiscountPrice(shopCode);
        Date cutOffOrderTime = shopConfigService.getTodayLastCutOffTime(shopCode);

        /**
         * 如果有当天的订单，然而已经超过截单时间，同时用户确认跳过预订今天的订单，则将当天的订单从子单中移除
         */
        List<VirtualOrderDeliveryParam> deliveryParams = virtualOrderParam.getDeliveries().stream().filter(
                deliveryParam -> !(WeekDayUtils.isToday(deliveryParam.getDeliveryDate()) && orderDate.compareTo(cutOffOrderTime) > 0)
        ).collect(Collectors.toList());
        virtualOrderParam.setDeliveries(deliveryParams);

        /**
         * 拼装详细信息
         * 配送表 配送费原价，实价
         * 配送商品表 打包费原价，实价，商品名称，图片
         */
        virtualOrderParam.getDeliveries().forEach(delivery -> {
            delivery.setDeliveryOriginalPrice(deliveryOriginalPrice);
            delivery.setDeliveryActualPrice(deliveryActualPrice);
            delivery.getDeliveryGoods().forEach(goods -> {
                ResponseResult<GoodsDto> result = shopService.getGoodsInfo(virtualOrderParam.getShopCode(), delivery.getDeliveryDate().getTime(), goods.getGoodsCode());
                GoodsDto goodsDto = result.getData();
                if (goodsDto == null) {
                    throw BusinessException.build("", "无法获取商品信息价格信息");
                }
                log.info("获取商品信息返回结果：商品信息：{}", JSON.toJSONString(goodsDto));
                if (goodsDto.getType() == null || GoodsType.MAIN_MEAL.getValue() == goodsDto.getType()) {
                    goods.setPackageOriginalPrice(packageOriginalPrice);
                    goods.setPackageActualPrice(packageActualPrice);
                }

                goods.setGoodsName(goodsDto.getName());
                goods.setGoodsImgUrl(goodsDto.getPicture());
                goods.setGoodsOriginalPrice(goodsDto.getAdvisePrice());
                goods.setGoodsActualPrice(goodsDto.getPreferentialPrice() != null ? goodsDto.getPreferentialPrice() : goodsDto.getAdvisePrice());
                goods.setType(goodsDto.getType());
            });
        });
        return virtualOrderParam;
    }

    public OrderPaymentParam fillOrderPaymentParam(OrderPaymentParam orderPaymentParam) {
        orderPaymentParam.setOrderDate(new Date());
        List<ShopConfigDto.DeliveryTimezone> timezones = shopConfigService.getDeliveryTimezones(orderPaymentParam.getShopCode());
        ShopConfigDto.DeliveryTimezone timezone = timezones.get(0);
        orderPaymentParam.setCutoffTime(timezone.getCutoffTime());
//        log.info("--------填充配送时段配置：{}", JSON.toJSONString(timezones));
//        Map<Long, ShopConfigDto.DeliveryTimezone> timezoneMap = shopConfigService.getDeliveryTimezoneMap(orderPaymentParam.getShopCode());
//        log.info("------------timezoneMap:{}", JSON.toJSONString(timezoneMap));
//        orderPaymentParam.setTimezoneDtos(BeanMapper.mapList(timezones, TimezoneDto.class));
//        orderPaymentParam.getDeliveryTimezones().forEach(deliveryTimezoneParam -> {
//            ShopConfigDto.DeliveryTimezone timezone = timezoneMap.get(deliveryTimezoneParam.getId());
//            log.info("----timezone:{}", JSON.toJSONString(timezone));
//            BeanMapper.copy(timezone, deliveryTimezoneParam);
//            log.info("----deliveryTimezoneParam:{}", JSON.toJSONString(deliveryTimezoneParam));
//        });
        return orderPaymentParam;
    }
}
