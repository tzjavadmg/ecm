package com.milisong.dms.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.dms.api.OrderSetService;
import com.milisong.dms.constant.OrderDeliveryStatus;
import com.milisong.dms.dto.httpdto.NotifyOrderSetQueryResult;
import com.milisong.dms.dto.httpdto.ScmUrlDto;
import com.milisong.dms.dto.orderset.OrderSetDetailDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhaozhonghui
 * @Description 请求集单信息接口
 * @date 2018-12-01
 */
@Service
@Slf4j
public class OrderSetServiceImpl implements OrderSetService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ScmUrlDto scmUrlDto;

    @Override
    public List<OrderSetDetailDto> getOrderSetByOrderNo(String deliveryNo, Byte businessType) {
        String url = "";
        if (OrderDeliveryStatus.BusinessType.LUNCH.getValue() == businessType) {
            url = scmUrlDto.getQueryLunchByOrderNoUrl();
        } else if (OrderDeliveryStatus.BusinessType.BREAKFAST.getValue() == businessType) {
            url = scmUrlDto.getQueryBfByOrderNoUrl();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("orderNo", deliveryNo);
        log.info("http请求scm集单接口,业务类型:{},请求参数:{},请求路径:{}", OrderDeliveryStatus.BusinessType.getNameByValue(businessType), JSONObject.toJSONString(map), url);
        ResponseResult result = restTemplate.getForObject(url, ResponseResult.class, map);
        log.info("http请求scm集单接口接口返回:{}", JSONObject.toJSONString(result));
        if (result.isSuccess()) {
            List<OrderSetDetailDto> orderSetDetailDtos = JSONArray.parseArray(JSONObject.toJSONString(result.getData()), OrderSetDetailDto.class);
            return orderSetDetailDtos;
        } else {
            log.error(result.getCode() + ":" + result.getMessage());
            return null;
        }
    }

    @Override
    public NotifyOrderSetQueryResult getOrderSetInfoByDetailSetNo(String setDetailNo, Long setDetailId, Byte businessType) {
        String url;
        if (OrderDeliveryStatus.BusinessType.LUNCH.getValue() == businessType) {
            url = scmUrlDto.getQueryLunchBySetIdUrl();
        } else if (OrderDeliveryStatus.BusinessType.BREAKFAST.getValue() == businessType) {
            url = scmUrlDto.getQueryBfBySetIdUrl();
        } else {
            url = scmUrlDto.getQueryLunchBySetIdUrl();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("detailSetNo", setDetailNo);
        map.put("detailSetId", setDetailId);
        log.info("http请求scm集单接口,业务类型:{},请求参数:{},请求路径:{}", OrderDeliveryStatus.BusinessType.getNameByValue(businessType), JSONObject.toJSONString(map), url);
        ResponseResult result = restTemplate.getForObject(url, ResponseResult.class, map);
        log.info("http请求scm集单接口接口返回:{}", JSONObject.toJSONString(result));
        if (result.isSuccess()) {
            NotifyOrderSetQueryResult notifyOrderSetQueryResult = JSONObject.parseObject(JSONObject.toJSONString(result.getData()), NotifyOrderSetQueryResult.class);
            return notifyOrderSetQueryResult;
        } else {
            log.error(result.getCode() + ":" + result.getMessage());
            return null;
        }
    }
}
