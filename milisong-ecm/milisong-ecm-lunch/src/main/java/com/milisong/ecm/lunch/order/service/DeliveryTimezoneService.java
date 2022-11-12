package com.milisong.ecm.lunch.order.service;

import com.farmland.core.api.ResponseResult;
import com.milisong.oms.dto.TimezoneDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * 配送时段服务
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/23 18:36
 */
public interface DeliveryTimezoneService {
    /**
     * 获取所有的配送区间
     *
     * @param shopCode 门店编码
     * @return ResponseResult
     */
    ResponseResult<List<TimezoneDto>> getAllDeliveryTimezones(@RequestParam("businessLine") Byte businessLine, @RequestParam("shopCode") String shopCode);


    /**
     * 获取可用的配送区间
     *
     * @param shopCode 门店编码
     * @return ResponseResult
     */
    ResponseResult<List<TimezoneDto>> getAvailableTimezones(@RequestParam("businessLine") Byte businessLine, @RequestParam("shopCode") String shopCode, @RequestParam("deliveryDate") Date deliveryDate);
}
