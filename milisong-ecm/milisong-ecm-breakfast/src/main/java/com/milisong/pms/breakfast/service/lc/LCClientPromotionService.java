package com.milisong.pms.breakfast.service.lc;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.api.ActivityUserRedPacketService;
import com.milisong.pms.prom.api.PromotionCalculationSerivce;
import com.milisong.pms.prom.api.UserRedPacketService;
import com.milisong.pms.prom.dto.OrderAmountDto;
import com.milisong.pms.prom.dto.RedPacketLaunchQueryDto;
import com.milisong.pms.prom.dto.RedPacketLaunchResultDto;
import com.milisong.pms.prom.param.OrderParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sailor wang
 * @date 2018/9/14 下午7:47
 * @description
 */
@Slf4j
@Service
public class LCClientPromotionService {

    @Autowired
    ActivityUserRedPacketService activityUserRedPacketService;

    @Autowired
    UserRedPacketService userRedPacketService;

    @Autowired
    PromotionCalculationSerivce calculationSerivce;


    /**
     * 发起红包入口
     *
     * @param redPacketLaunchQuery
     * @return
     */
    public ResponseResult<RedPacketLaunchResultDto> multiShareRedPacketLaunchQuery(RedPacketLaunchQueryDto redPacketLaunchQuery) {
        log.info("发起红包入口 redPacketLaunchQuery -> {}",redPacketLaunchQuery);
        return activityUserRedPacketService.multiShareRedPacketLaunchQuery(redPacketLaunchQuery);
    }

    /**
     * 红包状态置为可用
     *
     * @param userRedPacketId
     * @return
     */
    public ResponseResult<Boolean> updateUserRedPacketUseful(Long userRedPacketId) {
        log.info("红包状态置为可用 userRedPacketId -> {}",userRedPacketId);
        return userRedPacketService.updateUserRedPacketUseful(userRedPacketId);
    }

    /**
     * 分析可参与的促销活动，计算订单实际需要支付的金额
     *
     * @param orderParam
     * @return
     */
    public ResponseResult<OrderAmountDto> actualPay(OrderParam orderParam) {
        return calculationSerivce.calculate(orderParam);
    }

    public ResponseResult receiveNewRedpacket(Long userId) {
        ResponseResult result = activityUserRedPacketService.receiveNewRedPacket(userId,null);
        if (result.isSuccess()){
            return ResponseResult.buildSuccessResponse();
        }
        return ResponseResult.buildFailResponse();
    }
}