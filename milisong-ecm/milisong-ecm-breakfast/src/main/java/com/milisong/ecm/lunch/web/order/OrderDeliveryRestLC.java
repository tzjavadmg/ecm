package com.milisong.ecm.lunch.web.order;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.lunch.order.service.DeliveryTimezoneService;
import com.milisong.oms.constant.OrderType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/23 20:01
 */
@RestController
@Slf4j
@RequestMapping("lc")
public class OrderDeliveryRestLC {

    @Resource
    private DeliveryTimezoneService deliveryTimezoneService;

    @GetMapping("/v1/delivery/delivery-timezone")
    ResponseResult<?> getAllDeliveryTimezones(@RequestParam(value = "shopCode", required = true) String shopCode) {
        log.info("获取所有配送时间段，入参：{}", shopCode);
        return deliveryTimezoneService.getAllDeliveryTimezones(OrderType.LUNCH.getValue(),shopCode);
    }

    @GetMapping("/v1/delivery/available-delivery-timezone")
    ResponseResult<?> getAllAvailableDeliveryTimezones(@RequestParam(value = "shopCode", required = true) String shopCode, @DateTimeFormat(pattern = "yyyy-MM-dd") Date deliveryDate) {
        log.info("获取所有可用配送时间段，入参：{}", shopCode);
        return deliveryTimezoneService.getAvailableTimezones(OrderType.LUNCH.getValue(),shopCode, deliveryDate);
    }

}
