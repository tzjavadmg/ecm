package com.milisong.oms.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/14 9:50
 */
@FeignClient("milisong-oms-service")
public interface SalesService {

    /**
     * 获取指定商品近30天总销量
     *
     * @param goodsCode 商品编码
     * @return 销量数据
     */
    @PostMapping(value = "/v1/SalesService/getLately30DaysSales")
    Integer getLately30DaysSales(@RequestParam("goodsCode") String goodsCode);

    /**
     * 统计最近29天各类商品销量，该方法用于定时任务，每晚0点执行
     */
    @PostMapping(value = "/v1/SalesService/statLately29DaysSales")
    void statLately29DaysSales();
}
