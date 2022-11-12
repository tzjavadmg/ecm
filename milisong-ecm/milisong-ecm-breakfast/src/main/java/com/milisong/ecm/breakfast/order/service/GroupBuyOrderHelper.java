package com.milisong.ecm.breakfast.order.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.exception.BusinessException;
import com.farmland.core.util.BeanMapper;
import com.google.common.collect.Lists;
import com.milisong.ecm.breakfast.dto.GoodsDto;
import com.milisong.ecm.breakfast.goods.api.ShopService;
import com.milisong.ecm.breakfast.param.GroupBuyOrderPayParam;
import com.milisong.ecm.common.api.ShopConfigService;
import com.milisong.oms.constant.OrderType;
import com.milisong.oms.constant.OrderUserSource;
import com.milisong.oms.param.GroupBuyOrderDeliveryGoodsParam;
import com.milisong.oms.param.GroupBuyOrderDeliveryParam;
import com.milisong.oms.param.GroupBuyOrderParam;
import com.milisong.pms.prom.api.GroupBuyService;
import com.milisong.pms.prom.dto.groupbuy.ActivityGroupBuyDto;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyRequest;
import com.milisong.ucs.sdk.security.UserContext;
import com.milisong.ucs.sdk.security.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/21 16:29
 */
@Slf4j
@Component
public class GroupBuyOrderHelper {

    @Resource
    private ShopConfigService shopConfigService;
    @Resource
    private ShopService shopService;
    @Resource
    private GroupBuyService groupBuyService;

    public GroupBuyOrderParam buildGroupBuyOrderParam(GroupBuyOrderPayParam orderPaymentParam) {

        GroupBuyOrderParam groupBuyOrderParam = BeanMapper.map(orderPaymentParam, GroupBuyOrderParam.class);
        Date orderDate = new Date();
        groupBuyOrderParam.setOrderDate(orderDate);
        groupBuyOrderParam.setOrderSource(OrderUserSource.WECHAT_MINI_BF.getValue());
        groupBuyOrderParam.setOrderType(OrderType.BREAKFAST.getValue());
        groupBuyOrderParam.setDeliveryBuildingId(orderPaymentParam.getDeliveryCompanyId());
        groupBuyOrderParam.setGroupBuyActivityId(orderPaymentParam.getActivityGroupBuyId());
        groupBuyOrderParam.setGroupBuyId(orderPaymentParam.getUserGroupBuyId());
        groupBuyOrderParam.setDeliveryBuildingId(orderPaymentParam.getDeliveryCompanyId());
        UserSession session = UserContext.getCurrentUser();
        if (session != null) {
            groupBuyOrderParam.setOpenId(session.getOpenId());
            groupBuyOrderParam.setNickName(session.getNickName());
            groupBuyOrderParam.setHeadPortraitUrl(session.getHeadPortraitUrl());
            groupBuyOrderParam.setUserId(session.getId());
        }

        String shopCode = orderPaymentParam.getShopCode();
        BigDecimal deliveryOriginalPrice = shopConfigService.getDeliveryOriginalPrice(shopCode);
        BigDecimal deliveryActualPrice = BigDecimal.ZERO;
        BigDecimal packageOriginalPrice = shopConfigService.getPackageOriginalPrice(shopCode);
        BigDecimal packageActualPrice = BigDecimal.ZERO;

        /**
         * 拼装详细信息
         * 配送表 配送费原价，实价
         * 配送商品表 打包费原价，实价，商品名称，图片
         */
        ResponseResult<ActivityGroupBuyDto> responseResult=groupBuyService.queryActivityGroupBuyInfo(GroupBuyRequest.builder().activityGroupBuyId(orderPaymentParam.getActivityGroupBuyId()).build());
        if(!responseResult.isSuccess()){
            throw BusinessException.build("", "无法从促销系统获取拼团活动信息:"+responseResult.getMessage());
        }

        ActivityGroupBuyDto activityGroupBuyDto= responseResult.getData();
        String goodsCode=activityGroupBuyDto.getGoodsCode();
        ResponseResult<GoodsDto> result = shopService.getGoodsInfo(activityGroupBuyDto.getGoodsCode());
        GoodsDto goodsDto = result.getData();
        if (goodsDto == null) {
            throw BusinessException.build("", "无法获取商品信息,商品编码："+activityGroupBuyDto.getGoodsCode());
        }
        log.info("获取商品信息返回结果：商品信息：{}", JSON.toJSONString(goodsDto));

        GroupBuyOrderDeliveryParam groupBuyOrderDeliveryParam = new GroupBuyOrderDeliveryParam();
        groupBuyOrderDeliveryParam.setDeliveryDate(activityGroupBuyDto.getDeliveryDate());
        groupBuyOrderDeliveryParam.setStartTime(orderPaymentParam.getTakeMealsStartTime());
        groupBuyOrderDeliveryParam.setEndTime(orderPaymentParam.getTakeMealsEndTime());
        groupBuyOrderDeliveryParam.setDeliveryActualPrice(deliveryActualPrice);
        groupBuyOrderDeliveryParam.setDeliveryOriginalPrice(deliveryOriginalPrice);

        GroupBuyOrderDeliveryGoodsParam goods=new GroupBuyOrderDeliveryGoodsParam();
        goods.setGoodsCode(goodsCode);
        goods.setGoodsCount(1);
        goods.setPackageOriginalPrice(packageOriginalPrice);
        goods.setPackageActualPrice(packageActualPrice);
        goods.setGoodsName(goodsDto.getName());
        goods.setGoodsImgUrl(goodsDto.getPicture());
        goods.setGoodsOriginalPrice(goodsDto.getAdvisePrice());
        goods.setGoodsActualPrice(activityGroupBuyDto.getBuyPrice());


        //处理组合商品
        if (CollectionUtils.isNotEmpty(goodsDto.getListGoodsCombinationRefMqDto())) {
            List<GroupBuyOrderDeliveryGoodsParam> deliveryGoodsParamList = goodsDto.getListGoodsCombinationRefMqDto().stream().map(comboGoods -> {
                GroupBuyOrderDeliveryGoodsParam goodsParam = new GroupBuyOrderDeliveryGoodsParam();
                goodsParam.setIsCombo(false);
                goodsParam.setGoodsCode(comboGoods.getSingleCode());
                goodsParam.setComboGoodsCode(comboGoods.getCombinationCode());
                goodsParam.setGoodsCount(comboGoods.getGoodsCount());
                goodsParam.setGoodsName(comboGoods.getGoodsName());
                goodsParam.setGoodsOriginalPrice(comboGoods.getAdvisePrice());
                goodsParam.setGoodsActualPrice(comboGoods.getAdvisePrice());
                goodsParam.setPackageOriginalPrice(packageOriginalPrice);
                goodsParam.setPackageActualPrice(packageActualPrice);
                return goodsParam;
            }).collect(Collectors.toList());
            goods.setDeliveryGoodsParamList(deliveryGoodsParamList);
            goods.setIsCombo(true);
        } else {
            goods.setIsCombo(false);
        }

        groupBuyOrderDeliveryParam.setDeliveryGoods(Lists.newArrayList(goods));
        groupBuyOrderParam.setDeliveries(Lists.newArrayList(groupBuyOrderDeliveryParam));
        log.info("--------------------构建支付参数：{}",JSON.toJSONString(groupBuyOrderParam));
        return groupBuyOrderParam;
    }
}
