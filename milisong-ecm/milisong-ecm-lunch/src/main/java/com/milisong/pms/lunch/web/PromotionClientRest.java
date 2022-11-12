package com.milisong.pms.lunch.web;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.pms.lunch.service.ClientPromotionService;
import com.milisong.pms.prom.dto.OrderAmountDto;
import com.milisong.pms.prom.dto.RedPacketLaunchQueryDto;
import com.milisong.pms.prom.dto.RedPacketLaunchResultDto;
import com.milisong.pms.prom.param.OrderParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author sailor wang
 * @date 2018/9/14 下午7:32
 * @description
 */
@Slf4j
@RestController
@RequestMapping("/v1/promotion/client/")
public class PromotionClientRest {

    @Autowired
    private ClientPromotionService promotionService;

    /**
     * 发起红包入口
     *
     * @return
     */
    @PostMapping("multishareredpacket-launch-query")
    ResponseResult<RedPacketLaunchResultDto> multiShareRedPacketLaunchQuery(@RequestBody RedPacketLaunchQueryDto redPacketLaunchQuery) {
        return promotionService.multiShareRedPacketLaunchQuery(redPacketLaunchQuery);
    }

    /**
     * 红包状态置为可用
     *
     * @param param
     * @return
     */
    @PostMapping("update-redpacket-useful")
    ResponseResult<Boolean> updateUserRedPacketUseful(@RequestBody JSONObject param){
        Long userRedPacketId = param.getLong("userRedPacketId");
        return promotionService.updateUserRedPacketUseful(userRedPacketId);
    }

    /**
     * 促销计算实际支付金额
     *
     * @param orderParam
     * @return
     */
    @PostMapping("actual-pay")
    ResponseResult<OrderAmountDto> actualPay(@RequestBody OrderParam orderParam){
        return promotionService.actualPay(orderParam);
    }

    /**
     * 领取新人红包
     *
     * @param userId
     * @return
     */
    @GetMapping("receive-new-redpacket")
    ResponseResult receiveNewRedpacket(@RequestParam("userId")Long userId){
        return promotionService.receiveNewRedpacket(userId);
    }


}