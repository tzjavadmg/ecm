package com.milisong.dms.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.milisong.dms.api.DaDaOrderService;
import com.milisong.dms.constant.DaDaStatusConstant;
import com.milisong.dms.constant.OrderDeliveryStatus;
import com.milisong.dms.dto.dada.DaDaBaseDto;
import com.milisong.dms.dto.dada.DaDaOrderReqDto;
import com.milisong.dms.dto.dada.DaDaOrderStatusBackReqDto;
import com.milisong.dms.dto.shunfeng.*;
import com.milisong.dms.util.JsonUtil;
import com.milisong.dms.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author zhaozhonghui
 * @date 2018-11-26
 */
@Service
@Slf4j
public class DaDaOrderServiceImpl implements DaDaOrderService {
    @Value("${dada.app-key}")
    private String appKey;
    @Value("${dada.app-secret}")
    private String appSecret;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseResult createOrder(SfOrderReqDto dto) {
        DaDaBaseDto dadaDto = new DaDaBaseDto();
        dadaDto.setAppKey(appKey);
        dadaDto.setFormat("json");
        dadaDto.setTimestamp(String.valueOf(System.currentTimeMillis() / 1000));
        dadaDto.setSourceId("73753");   // 测试环境写死
        dadaDto.setV("1.0");
        DaDaOrderReqDto daDaOrderReqDto = convertSf2DaDa(dto);
        try {
            String bodyStr = JsonUtil.toUnderlineJSONString(daDaOrderReqDto);
            dadaDto.setBody(bodyStr);
            dadaDto.setSignature(getDaDaSign(dadaDto));
            String requestParam = JsonUtil.toUnderlineJSONString(dadaDto);
            String url = "http://newopen.qa.imdada.cn" + "/api/order/addOrder";
            postSendMsg(url,requestParam);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, Object> sendShunfengBackMsg(String msg) {
        try {
            DaDaOrderStatusBackReqDto backReqDto = JsonUtil.toSnakeObject(msg, DaDaOrderStatusBackReqDto.class);
            Byte orderStatus = backReqDto.getOrderStatus();
            Byte status = convertStatus(orderStatus);
            if (null != status) {
                DeliveryOrderMqDto orderMqDto = new DeliveryOrderMqDto();
                orderMqDto.setDetailSetId(backReqDto.getOrderId());
                orderMqDto.setStatus(status);

            }

        } catch (IOException e) {
            log.error("达达订单转换失败！",e);
        }
        return null;
    }

    @Override
    public ResponseResult testCrestOrder(SfOrderReqDto dto) {
        if (dto == null) {
            dto = new SfOrderReqDto();
        }
        SfOrderReqDto sfOrderReqDto = testCreateSfOrder(dto);
        ResponseResult result = createOrder(sfOrderReqDto);
        return result;
    }

    @Override
    public JSONObject postSendMsg(String url, String param) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<Object> entity = new HttpEntity<>(param, headers);
        JSONObject result = restTemplate.postForObject(url, entity, JSONObject.class);
        log.info("达达订单接口返回:{}", result);
        return result;
    }

    @Override
    public void sendSmsMsg(String detailSetId, String mobiles) {

    }

    @Override
    public String getDetailSetIdBySfOrderId(String sfOrderId) {
        return null;
    }

    @Override
    public ResponseResult<RiderInfoDto> getSfOrderDeliveryStatusByDeliveryNo(String deliveryNo) {
        return null;
    }

    @Override
    public ResponseResult<RiderInfoDto> getSfOrderDeliveryStatusBySetDetailId(Long setDetailId) {
        return null;
    }

    private DaDaOrderReqDto convertSf2DaDa(SfOrderReqDto sfDto){
        DaDaOrderReqDto dadaDto = new DaDaOrderReqDto();
        SfOrderReceiveDto receive = sfDto.getReceive();
        SfOrderDetail orderDetail = sfDto.getOrderDetail();
        dadaDto.setOriginId(sfDto.getShopOrderId());
        dadaDto.setShopNo(sfDto.getShopId());
        dadaDto.setCargoPrice(new BigDecimal(orderDetail.getTotalPrice() / 100));
        dadaDto.setReceiverAddress(receive.getUserAddress());
        // TODO: 经纬度要调用高德API 将百度坐标进行转换
        dadaDto.setReceiverLat(Double.valueOf(receive.getUserLat()));
        dadaDto.setReceiverLng(Double.valueOf(receive.getUserLng()));
        dadaDto.setReceiverPhone(receive.getUserPhone());
        dadaDto.setReceiverName(receive.getUserName());

        dadaDto.setIsPrepay(DaDaStatusConstant.IsPrepay.NOT_NEED);
        dadaDto.setCityCode("310100");
        dadaDto.setCargoType(DaDaStatusConstant.CargoType.FOOD);
        dadaDto.setCallback("www.baidu.com");
        return dadaDto;
    }

    private String getDaDaSign(DaDaBaseDto dto){
        Map<String, Object> map = new HashMap<>();
        map.put("app_key",dto.getAppKey());
        map.put("body",dto.getBody());
        map.put("format",dto.getFormat());
        map.put("source_id",dto.getSourceId());
        map.put("v",dto.getV());
        map.put("timestamp",dto.getTimestamp());
        Set<String> keys = map.keySet();
        List<String> keyList = new ArrayList<>();
        keyList.addAll(keys);
        keyList.sort(Comparator.naturalOrder());
        StringBuffer signStr = new StringBuffer();
        for(int i=0; i<keyList.size(); i++){
            String key = keyList.get(i);
            signStr.append(key + map.get(key));
        }
        String sign = appSecret + signStr.toString() + appSecret;
        sign = MD5Util.MD5(sign);
        return sign;
    }

    private SfOrderReqDto testCreateSfOrder(SfOrderReqDto dto) {
        long minute = System.currentTimeMillis() / 1000;
        dto.setShopId("11047059");
        dto.setShopOrderId("01" + minute);
        dto.setPayType((byte) 1);
        dto.setOrderTime(minute - 2 * 60);
        dto.setPushTime(minute);
        dto.setExpectTime(minute + 2 * 60 * 60);
        dto.setIsAppoint((byte) 0);
        dto.setOrderSource("米立送");

        SfOrderReceiveDto sfOrderReceiveDto = new SfOrderReceiveDto();
        sfOrderReceiveDto.setUserName("老王");
        sfOrderReceiveDto.setUserPhone("15921557120");
        sfOrderReceiveDto.setUserAddress("王府井B12");
        sfOrderReceiveDto.setUserLat("40.002349");
        sfOrderReceiveDto.setUserLng("116.339392");
        dto.setReceive(sfOrderReceiveDto);

        SfOrderDetail sfOrderDetail = new SfOrderDetail();
        sfOrderDetail.setTotalPrice(1000L);
        sfOrderDetail.setWeightGram(420);
        sfOrderDetail.setProductType((byte) 1);
        sfOrderDetail.setProductNum(1);
        sfOrderDetail.setProductTypeNum(1);

        SfORderProductDetail sfORderProductDetail = new SfORderProductDetail();
        sfORderProductDetail.setProductName("午餐");
        sfORderProductDetail.setProductNum(1);
        sfOrderDetail.setProductDetail(Arrays.asList(sfORderProductDetail));
        dto.setOrderDetail(sfOrderDetail);
        dto.setOrderSequence(dto.getShopOrderId());
        dto.setDetailId("1234567");
        return dto;
    }

    private Byte convertStatus(Byte dadaStatus) {
        Byte status = null;
        if (DaDaStatusConstant.DaDaDeliveryStatus.WAIT_CONFIRM.equals(dadaStatus)) {
            status = OrderDeliveryStatus.DISPATCHED_ORDER.getValue();
        } else if (DaDaStatusConstant.DaDaDeliveryStatus.WAIT_DISPATCHING.equals(dadaStatus)) {
            status = OrderDeliveryStatus.RECEIVED_ORDER.getValue();
        } else if (DaDaStatusConstant.DaDaDeliveryStatus.DISPATCHING.equals(dadaStatus)){
            status = OrderDeliveryStatus.RECEIVED_GOODS.getValue();
        } else if (DaDaStatusConstant.DaDaDeliveryStatus.FINISH.equals(dadaStatus)){
            status = OrderDeliveryStatus.COMPLETED.getValue();
        }

        return status;
    }
}
