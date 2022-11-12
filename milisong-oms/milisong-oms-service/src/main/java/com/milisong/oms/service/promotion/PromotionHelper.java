package com.milisong.oms.service.promotion;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.exception.BusinessException;
import com.farmland.core.util.BeanMapper;
import com.milisong.pms.prom.api.ActivityUserRedPacketService;
import com.milisong.pms.prom.api.MultiShareActivityService;
import com.milisong.pms.prom.api.PromotionCalculationSerivce;
import com.milisong.pms.prom.dto.OrderAmountDto;
import com.milisong.pms.prom.dto.RedPacketLaunchQueryDto;
import com.milisong.pms.prom.dto.RedPacketLaunchResultDto;
import com.milisong.pms.prom.param.OrderParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class PromotionHelper {

    @Resource
    private PromotionCalculationSerivce promotionCalculationSerivce;

    @Resource
    private ActivityUserRedPacketService activityUserRedPacketService;
    
    @Resource
    private MultiShareActivityService multiShareActivityService;

    /**
     * 我的订单多人红包分享
     *
     * @param redPacketLaunchQuery
     * @return
     */
    public RedPacketLaunchResultDto multiShareRedPacketLaunchQuery(RedPacketLaunchQueryDto redPacketLaunchQuery) {
        ResponseResult<RedPacketLaunchResultDto> responseResult = activityUserRedPacketService.multiShareRedPacketLaunchQuery(redPacketLaunchQuery);
        return responseResult.getData();
    }
    
    /**
     * 我的订单午餐红包分享、早餐折扣券
     * @param redPacketLaunchQuery
     * @return
     */
    public RedPacketLaunchResultDto multiShareLaunchQuery(RedPacketLaunchQueryDto redPacketLaunchQuery) {
    	log.info("调用红包分享,早餐折扣券入参{}",JSON.toJSONString(redPacketLaunchQuery));
    	ResponseResult<RedPacketLaunchResultDto> responseResult = multiShareActivityService.multiShareLaunchQuery(redPacketLaunchQuery);
    	return responseResult.getData();
    }

    public OrderAmountDto calculate(OrderParam orderParam) {
        ResponseResult<OrderAmountDto> result = promotionCalculationSerivce.calculate(orderParam);
        if (result.isSuccess()) {
            return BeanMapper.map(result.getData(), OrderAmountDto.class);
        }
        throw BusinessException.build(result.getCode(), result.getMessage());
    }

    public OrderAmountDto preCalculate(OrderParam orderParam) {
        ResponseResult<OrderAmountDto> result = promotionCalculationSerivce.preCalculate(orderParam);
        if (result.isSuccess()) {
            return BeanMapper.map(result.getData(), OrderAmountDto.class);
        }
        throw BusinessException.build(result.getCode(), result.getMessage());
    }

}
