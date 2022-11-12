package com.milisong.pms.prom.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.OrderAmountDto;
import com.milisong.pms.prom.param.OrderParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/11/6 17:36
 */
@FeignClient("milisong-pms-service")
public interface PromotionCalculationSerivce {

    /**
     * 分析可参与的促销活动，计算订单实际需要支付的金额
     *
     * @param orderParam 订单信息参数
     * @return OrderAmountDto
     */
    @PostMapping(value = "/v1/PromotionCalculationSerivce/calculate")
    ResponseResult<OrderAmountDto> calculate(@RequestBody OrderParam orderParam);

    /**
     * 预计算：分析可参与的促销活动，计算订单实际需要支付的金额
     *
     * @param orderParam 订单信息参数
     * @return OrderAmountDto
     */
    @PostMapping(value = "/v1/PromotionCalculationSerivce/preCalculate")
    ResponseResult<OrderAmountDto> preCalculate(@RequestBody OrderParam orderParam);



}
