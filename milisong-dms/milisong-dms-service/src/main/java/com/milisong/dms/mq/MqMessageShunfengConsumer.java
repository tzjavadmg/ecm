package com.milisong.dms.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.milisong.dms.api.BuildingService;
import com.milisong.dms.api.ShopService;
import com.milisong.dms.api.ShunfengOrderService;
import com.milisong.dms.constant.OrderDeliveryStatus;
import com.milisong.dms.dto.httpdto.BuildingDto;
import com.milisong.dms.dto.httpdto.ShopDto;
import com.milisong.dms.dto.orderset.OrderSetDetailGoodsMqDto;
import com.milisong.dms.dto.orderset.OrderSetDetailMqDto;
import com.milisong.dms.dto.orderset.OrderSetProductionMqMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import java.util.List;

@Slf4j
@EnableBinding(MqMessageShunfengSource.class)
public class MqMessageShunfengConsumer {
    @Autowired
    private ShunfengOrderService shunfengOrderService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private BuildingService buildingService;

    @StreamListener(MqMessageShunfengSource.BUILDING_INFO_INPUT)
    public void getBuildingInfoMessage(Message<String> message) {
        log.info("开始消费楼宇信息，message：{}", JSONObject.toJSONString(message));
        String payload = message.getPayload();
        if (StringUtils.isNotBlank(payload)) {
            try {
                BuildingDto buildingDto = JSONObject.parseObject(payload, BuildingDto.class);
                if (null == buildingDto) {
                    log.warn("楼宇信息为空！！！");
                } else {
                    buildingService.syncBuildingInfo(buildingDto);
                }
            } catch (Exception e) {
                log.error("楼宇信息转换异常", e);
                throw e;
            }
        } else {
            log.warn("收到楼宇信息的空消息！！！");
        }
    }

    @StreamListener(MqMessageShunfengSource.SHOP_INFO_INPUT)
    public void getShopInfoMessage(Message<String> message) {
        log.info("开始消费门店信息，message：{}", JSONObject.toJSONString(message));
        String payload = message.getPayload();
        if (StringUtils.isNotBlank(payload)) {
            try {
                ShopDto shopDto = JSONObject.parseObject(payload, ShopDto.class);
                if (null == shopDto) {
                    log.warn("门店信息为空！！！");
                } else {
                    shopService.syncShopInfo(shopDto);
                }
            } catch (Exception e) {
                log.error("门店信息转换异常", e);
                throw e;
            }
        } else {
            log.warn("收到门店信息的空消息！！！");
        }
    }

    @StreamListener(MqMessageShunfengSource.DMS_ORDER_SET_INPUT)
    public void getOrderSetMessage(Message<String> message) {
        log.info("废弃，统一由一个MQ通道来消费，开始消费午餐集单信息，message：{}", JSONObject.toJSONString(message));
//        String payload = message.getPayload();
//        if (StringUtils.isNotBlank(payload)) {
//            try {
//                OrderSetProductionMqMsg orderSetProductionMqMsg = JSONObject.parseObject(payload, OrderSetProductionMqMsg.class);
//                if (null == orderSetProductionMqMsg) {
//                    log.warn("午餐集单信息为空！！！");
//                } else {
//                    this.shunfengOrderService.convertOrderSetMsg(orderSetProductionMqMsg, OrderDeliveryStatus.BusinessType.LUNCH.getValue());
//                }
//            } catch (Exception e) {
//                log.error("午餐集单信息转换异常", e);
//                throw e;
//            }
//        } else {
//            log.warn("收到午餐集单信息的空消息！！！");
//        }
    }

    @StreamListener(MqMessageShunfengSource.DMS_BF_ORDER_SET_INPUT)
    public void getBfOrderSetMessage(Message<String> message) {
        log.info("开始消费派单信息，message：{}", JSONObject.toJSONString(message));
        String payload = message.getPayload();
        if (StringUtils.isNotBlank(payload)) {
            try {
                OrderSetProductionMqMsg orderSetProductionMqMsg = JSONObject.parseObject(payload, OrderSetProductionMqMsg.class);
                if (null == orderSetProductionMqMsg) {
                    log.warn("派单信息为空！！！");
                } else {
                    Byte orderType = getOrderTypeByte(orderSetProductionMqMsg);
                    if(orderType == null ){
                        log.error("派单信息缺少订单类型信息，msg={}",message.getPayload());
                    }
                    this.shunfengOrderService.convertOrderSetMsg(orderSetProductionMqMsg, orderType);
                }
            } catch (Exception e) {
                log.error("派单信息转换异常", e);
                throw e;
            }
        } else {
            log.warn("收到派单信息的空消息！！！");
        }
    }

    private Byte getOrderTypeByte( OrderSetProductionMqMsg orderSetProductionMqMsg) {
        OrderSetDetailMqDto orderSet = orderSetProductionMqMsg.getOrderSet();
        Byte orderType = null;
        if(orderSet!=null && orderSet.getOrderType() != null){
            orderType = orderSet.getOrderType();
        }else{
            List<OrderSetDetailGoodsMqDto> goods = orderSetProductionMqMsg.getGoods();
            if(goods!= null && goods.size() > 0){
                OrderSetDetailGoodsMqDto orderSetDetailGoodsMqDto = goods.get(0);
                String orderNo = orderSetDetailGoodsMqDto.getOrderNo();
                if(StringUtils.isNotBlank(orderNo)){
                    if(orderNo.startsWith("B")){
                        orderType = OrderDeliveryStatus.BusinessType.BREAKFAST.getValue();
                    }else if(orderNo.startsWith("L")){
                        orderType = OrderDeliveryStatus.BusinessType.LUNCH.getValue();
                    }
                }
            }
        }
        return orderType;
    }
}
