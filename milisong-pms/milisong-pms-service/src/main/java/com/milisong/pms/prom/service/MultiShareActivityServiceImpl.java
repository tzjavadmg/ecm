package com.milisong.pms.prom.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.milisong.oms.api.OrderService;
import com.milisong.oms.constant.OrderType;
import com.milisong.oms.dto.OrderDto;
import com.milisong.pms.prom.annotation.AfterCutRedPacket;
import com.milisong.pms.prom.annotation.AfterReceiveNewRedPacket;
import com.milisong.pms.prom.annotation.BeforeCutRedPacket;
import com.milisong.pms.prom.api.ActivityRedPacketActionService;
import com.milisong.pms.prom.api.ActivityService;
import com.milisong.pms.prom.api.ActivityUserRedPacketService;
import com.milisong.pms.prom.api.MultiShareActivityService;
import com.milisong.pms.prom.domain.ActivityUserRedPacket;
import com.milisong.pms.prom.domain.RedPacketPool;
import com.milisong.pms.prom.domain.UserRedPacket;
import com.milisong.pms.prom.dto.*;
import com.milisong.pms.prom.enums.CouponEnum;
import com.milisong.pms.prom.enums.RedPacketLaunchType;
import com.milisong.pms.prom.mapper.ActivityUserRedPacketMapper;
import com.milisong.pms.prom.mapper.RedPacketPoolMapper;
import com.milisong.pms.prom.mapper.UserRedPacketMapper;
import com.milisong.pms.prom.util.PmsPoolUtil;
import com.milisong.ucs.api.UserService;
import com.milisong.ucs.dto.UserDto;
import com.milisong.ucs.enums.BusinessLineEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.milisong.pms.prom.enums.ActivityUsedStatus.UNUSED;
import static com.milisong.pms.prom.enums.ActivityValid.INVALID;
import static com.milisong.pms.prom.enums.ActivityValid.VALID;
import static com.milisong.pms.prom.enums.PromotionResponseCode.*;
import static com.milisong.pms.prom.enums.RedPacketLaunchType.ORDER_SHARE;
import static com.milisong.pms.prom.enums.RedPacketType.*;

/**
 * @author sailor wang
 * @date 2018/9/13 上午11:55
 * @description
 */
@Slf4j
@Service
@RestController
public class MultiShareActivityServiceImpl implements MultiShareActivityService {

    @Autowired
    ActivityUserRedPacketMapper activityUserRedPacketMapper;

    @Autowired
    ActivityService activityService;

    @Autowired
    ActivityRedPacketActionService activityRedPacketHackService;

    @Autowired
    UserRedPacketMapper userRedPacketMapper;

    @Autowired
    RedPacketPoolMapper redPacketPoolMapper;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    /**
     * 多人分享活动订单入口
     *
     * @param redPacketLaunchQuery
     * @return
     */
    @Override
    @PostMapping(value = "/v1/MultiShareActivityService/multiShareLaunchQuery")
    public ResponseResult<RedPacketLaunchResultDto> multiShareLaunchQuery(@RequestBody RedPacketLaunchQueryDto redPacketLaunchQuery) {
        if (redPacketLaunchQuery == null || CollectionUtils.isEmpty(redPacketLaunchQuery.getRedPacketLaunchList())
                || redPacketLaunchQuery.getBusinessLine() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        try {
            Date startDate = null;
            Date endDate = null;// 活动结束日期
            Integer showDays = null;// 展示天数
            if (BusinessLineEnum.BREAKFAST.getCode().equals(redPacketLaunchQuery.getBusinessLine())){
                // 早餐折扣券
                ActivityQueryParam queryParam = new ActivityQueryParam();
                queryParam.setBusinessLine(redPacketLaunchQuery.getBusinessLine());
                queryParam.setCouponType(CouponEnum.TYPE.DISCOUNT);
                ResponseResult<MultiBreakfastCouponConfig> responseResult = activityService.currentActivityCouponConfig(queryParam);
                if (responseResult.isSuccess() && responseResult.getData() != null) {
                    MultiBreakfastCouponConfig couponConfig = responseResult.getData();
                    startDate = couponConfig.getStartDate();
                    endDate = couponConfig.getEndDate();// 活动结束日期
                    showDays = couponConfig.getShowDays();// 展示天数
                    log.info("couponConfig -> {}", couponConfig);
                }
            }else {
                // 午餐红包
                ActivityQueryParam queryParam = new ActivityQueryParam();
                queryParam.setBusinessLine(redPacketLaunchQuery.getBusinessLine());
                queryParam.setType(SHARE_REDPACKET);
                ResponseResult<ActivityRedPacketDto> responseResult = activityService.currentActivityRedPacket(queryParam);
                if (responseResult.isSuccess() && responseResult.getData() != null) {
                    ActivityRedPacketDto activityRedPacket = responseResult.getData();
                    startDate = activityRedPacket.getStartDate();
                    endDate = activityRedPacket.getEndDate();// 活动结束日期
                    showDays = activityRedPacket.getShowDays();// 展示天数
                    log.info("activityRedPacket -> {}", activityRedPacket);
                }
            }

            if (startDate != null && endDate != null && showDays != null){
                Set<Long> validOrderSet = validMultiShareLaunch(startDate,endDate,showDays,redPacketLaunchQuery.getRedPacketLaunchList());
                if (validOrderSet.size() > 0) {
                    RedPacketLaunchResultDto redPacketLaunchResult = new RedPacketLaunchResultDto();
                    redPacketLaunchResult.setValidOrderSet(validOrderSet);
                    return ResponseResult.buildSuccessResponse(redPacketLaunchResult);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildSuccessResponse();
    }

    /**
     * 校验下单天数是否满足活动分享入口
     *
     * @param startDate
     * @param endDate
     * @param showDays
     * @param redPacketLaunchList
     * @return
     */
    private Set<Long> validMultiShareLaunch(Date startDate,Date endDate,Integer showDays,List<RedPacketLaunchDto> redPacketLaunchList){
        Set<Long> validOrderSet = Sets.newHashSet();

        DateTime now = DateTime.now();
        redPacketLaunchList.stream().forEach(lanuch -> {
            log.info(" lanuch -> {} ", lanuch);
            Long orderId = lanuch.getLaunchId();
            DateTime createDate = new DateTime(lanuch.getLaunchDate());
            if ((createDate.toDate().before(endDate) && createDate.toDate().after(startDate)) || createDate.toDate().compareTo(startDate) == 0) {
                DateTime endDateTime = new DateTime(createDate.getYear(),createDate.getMonthOfYear(),createDate.getDayOfMonth(),0,0).plusDays(showDays);
                if (endDateTime.toDate().before(endDate)) {
                    int intervalDays = Days.daysBetween(now, endDateTime).getDays();
                    if (intervalDays >= 0) {
                        log.info("orderId -> {}",orderId);
                        validOrderSet.add(orderId);
                    }
                } else {
                    int intervalDays = Days.daysBetween(now, new DateTime(endDate)).getDays();
                    if (intervalDays >= 0) {
                        log.info("orderId -> {}",orderId);
                        validOrderSet.add(orderId);
                    }
                }
            }
        });
        return validOrderSet;
    }

}