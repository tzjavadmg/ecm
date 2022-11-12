package com.milisong.ecm.breakfast.order.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.exception.BusinessException;
import com.milisong.ecm.breakfast.dto.GoodsDto;
import com.milisong.ecm.breakfast.goods.api.ShopService;
import com.milisong.ecm.common.api.ShopConfigService;
import com.milisong.oms.param.OrderPaymentParam;
import com.milisong.oms.param.VirtualOrderDeliveryGoodsParam;
import com.milisong.oms.param.VirtualOrderParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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

        /**
         * 拼装详细信息
         * 配送表 配送费原价，实价
         * 配送商品表 打包费原价，实价，商品名称，图片
         */
        virtualOrderParam.getDeliveries().forEach(delivery -> {
            delivery.setDeliveryOriginalPrice(deliveryOriginalPrice);
            delivery.setDeliveryActualPrice(deliveryActualPrice);
            delivery.getDeliveryGoods().forEach(goods -> {
                ResponseResult<GoodsDto> result = shopService.getGoodsInfo(goods.getGoodsCode());
                GoodsDto goodsDto = result.getData();
                if (goodsDto == null) {
                    throw BusinessException.build("", "无法获取商品信息价格信息");
                }
                log.info("获取商品信息返回结果：商品信息：{}", JSON.toJSONString(goodsDto));
                goods.setPackageOriginalPrice(packageOriginalPrice);
                goods.setPackageActualPrice(packageActualPrice);
                goods.setGoodsName(goodsDto.getName());
                goods.setGoodsImgUrl(goodsDto.getPicture());
                goods.setGoodsOriginalPrice(goodsDto.getAdvisePrice());
                goods.setGoodsActualPrice(goodsDto.getPreferentialPrice() != null ? goodsDto.getPreferentialPrice() : goodsDto.getAdvisePrice());

                //处理组合商品
                if (CollectionUtils.isNotEmpty(goodsDto.getListGoodsCombinationRefMqDto())) {
                    List<VirtualOrderDeliveryGoodsParam> deliveryGoodsParamList = goodsDto.getListGoodsCombinationRefMqDto().stream().map(comboGoods -> {
                        VirtualOrderDeliveryGoodsParam goodsParam = new VirtualOrderDeliveryGoodsParam();
                        goodsParam.setIsCombo(false);
                        goodsParam.setGoodsCode(comboGoods.getSingleCode());
                        goodsParam.setComboGoodsCode(comboGoods.getCombinationCode());
                        goodsParam.setGoodsCount(comboGoods.getGoodsCount());
                        goodsParam.setGoodsName(comboGoods.getGoodsName());
                        goodsParam.setGoodsOriginalPrice(comboGoods.getAdvisePrice());
                        return goodsParam;
                    }).collect(Collectors.toList());
                    goods.setDeliveryGoodsParamList(deliveryGoodsParamList);
                    goods.setIsCombo(true);
                }else{
                    goods.setIsCombo(false);
                }
            });
        });
        return virtualOrderParam;
    }

    public OrderPaymentParam fillOrderPaymentParam(OrderPaymentParam orderPaymentParam) {
        orderPaymentParam.setOrderDate(new Date());
//        List<ShopConfigDto.DeliveryTimezone> timezones= shopConfigService.getDeliveryTimezones(orderPaymentParam.getShopCode());
//        Map<Long, ShopConfigDto.DeliveryTimezone> timezoneMap= shopConfigService.getDeliveryTimezoneMap(orderPaymentParam.getShopCode());
//        orderPaymentParam.setTimezoneDtos(BeanMapper.mapList(timezones, TimezoneDto.class));
//        orderPaymentParam.getDeliveryTimezones().forEach(deliveryTimezoneParam->{
//            ShopConfigDto.DeliveryTimezone timezone=timezoneMap.get(deliveryTimezoneParam.getId());
//            BeanMapper.copy(timezone,deliveryTimezoneParam);
//        });
        return orderPaymentParam;
    }
}
