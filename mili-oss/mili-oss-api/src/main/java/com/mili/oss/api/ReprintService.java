package com.mili.oss.api;

import com.mili.oss.dto.NotifyOrderSetQueryResult;
import com.mili.oss.dto.OrderSetProductionMsgByPrint;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @program: mili-oss
 * @description: TODO
 * @author: liuyy
 * @date: 2019/5/28 19:35
 */
@FeignClient("mili-oss-service")
public interface ReprintService {

    /**
     * 午餐
     * */
    @GetMapping("/reprint/set-reprint-info")
    NotifyOrderSetQueryResult reprintSetMqBySetNo(String setNo,String coupletNo,Integer printType);

    /**
     * 早餐
     * */
    @GetMapping("/reprint/set-bf-reprint-info")
    OrderSetProductionMsgByPrint reprintBfSetMqBySetNo(String setNo, String coupletNo, Integer printType);

}
