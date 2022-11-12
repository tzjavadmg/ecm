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
import com.milisong.pms.prom.api.BreakfastCouponService;
import com.milisong.pms.prom.domain.ActivityUserRedPacket;
import com.milisong.pms.prom.domain.RedPacketPool;
import com.milisong.pms.prom.domain.UserRedPacket;
import com.milisong.pms.prom.dto.*;
import com.milisong.pms.prom.enums.RedPacketLaunchType;
import com.milisong.pms.prom.mapper.ActivityUserRedPacketMapper;
import com.milisong.pms.prom.mapper.RedPacketPoolMapper;
import com.milisong.pms.prom.mapper.UserRedPacketMapper;
import com.milisong.pms.prom.util.PmsPoolUtil;
import com.milisong.ucs.api.UserService;
import com.milisong.ucs.dto.UserDto;
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
import static com.milisong.ucs.enums.BusinessLineEnum.BREAKFAST;

/**
 * @author sailor wang
 * @date 2018/9/13 上午11:55
 * @description
 */
@Slf4j
@Service
@RestController
public class ActivityUserRedPacketServiceImpl implements ActivityUserRedPacketService {

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

    @Autowired
    BreakfastCouponService breakfastCouponService;


    /**
     * 发起红包活动
     *
     * @param redPacketRequest
     * @return
     */
    @Override
    @BeforeCutRedPacket
    @AfterCutRedPacket
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/v1/ActivityUserRedPacketService/createUserCutRedPacket")
    public ResponseResult<UserRedPacketResponse> createUserCutRedPacket(@RequestBody UserRedPacketRequest redPacketRequest) {
        if (redPacketRequest == null || redPacketRequest.getUserId() == null || StringUtils.isBlank(redPacketRequest.getOpenid())) {
            log.info("发起红包活动参数为空 redPacketRequest -> {}", redPacketRequest);
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        ActivityUserRedPacketDto activityUserRedPacket = redPacketRequest.getActivityUserRedPacket();
        if (activityUserRedPacket == null) {
            // 发起红包活动
            activityUserRedPacket = doCreateUserCutRedPacket(redPacketRequest);
            if (activityUserRedPacket == null) {
                return ResponseResult.buildFailResponse(NOT_EXIST_ACTIVITY_RED_PACKET.code(), NOT_EXIST_ACTIVITY_RED_PACKET.message());
            }
            // 砍一刀
            hackRedPacket(activityUserRedPacket, redPacketRequest.getHackTimes());
        } else {
            // 红包活动已完成
            Boolean isSuccess = activityUserRedPacket.getIsSuccess().equals((byte) 1);
            if (isSuccess) {
                return ResponseResult.buildSuccessResponse(UserRedPacketResponse.builder().currentUserRedPacket(activityUserRedPacket).build());
            }
            // 砍过该红包
            Boolean hackSelf = redPacketRequest.getHackCurrentRedPacket();
            if (hackSelf) {
                return ResponseResult.buildSuccessResponse(UserRedPacketResponse.builder().currentUserRedPacket(activityUserRedPacket).build());
            }
            // 砍一刀
            ActivityRedPacketActionDto hackRedPacket = hackRedPacket(activityUserRedPacket, redPacketRequest.getHackTimes());
            if (hackRedPacket != null) {
                // 需要几人砍
                Integer players = activityUserRedPacket.getPlayers().intValue();
                // 已砍几次
                Integer hackCount = activityUserRedPacket.getClickCount().intValue();
                if (hackCount + 1 >= players) {
                    //砍包完成
                    finishRedPacket(activityUserRedPacket);
                }
            }
        }
        Boolean isMy = redPacketRequest.getUserId().equals(activityUserRedPacket.getUserId());
        return ResponseResult.buildSuccessResponse(UserRedPacketResponse.builder().currentUserRedPacket(activityUserRedPacket).isMy(isMy).build());
    }

    /**
     * 砍红包活动
     *
     * @param redPacketRequest
     * @return
     */
    @Override
    @BeforeCutRedPacket
    @AfterCutRedPacket
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/v1/ActivityUserRedPacketService/hackUserCutRedPacket")
    public ResponseResult<UserRedPacketResponse> hackUserCutRedPacket(@RequestBody UserRedPacketRequest redPacketRequest) {
        if (redPacketRequest == null || redPacketRequest.getActivityUserRedPacketId() == null || redPacketRequest.getUserId() == null || StringUtils.isBlank(redPacketRequest.getOpenid())) {
            log.info("砍红包活动参数为空 redPacketRequest -> {}", redPacketRequest);
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        ActivityUserRedPacketDto activityUserRedPacket = redPacketRequest.getActivityUserRedPacket();
        if (activityUserRedPacket == null) {
            return ResponseResult.buildFailResponse(QUERY_USER_RED_PACKET_EXCEPTION.code(), QUERY_USER_RED_PACKET_EXCEPTION.message());
        } else {
            // 红包活动已完成
            Boolean isSuccess = activityUserRedPacket.getIsSuccess().equals((byte) 1);
            if (isSuccess) {
                return ResponseResult.buildSuccessResponse(UserRedPacketResponse.builder().currentUserRedPacket(activityUserRedPacket).build());
            }
            // 砍过该红包
            Boolean hackCurrentRedPacket = redPacketRequest.getHackCurrentRedPacket();
            if (hackCurrentRedPacket) {
                return ResponseResult.buildSuccessResponse(UserRedPacketResponse.builder().currentUserRedPacket(activityUserRedPacket).build());
            }
            // 砍一刀
            ActivityRedPacketActionDto hackRedPacket = hackRedPacket(activityUserRedPacket, redPacketRequest.getHackTimes());
            if (hackRedPacket != null) {
                // 需要几人砍
                Integer players = activityUserRedPacket.getPlayers().intValue();
                // 已砍几次
                Integer hackCount = activityUserRedPacket.getClickCount().intValue();
                if (hackCount + 1 >= players) {
                    //砍包完成
                    finishRedPacket(activityUserRedPacket);
                }
            }
        }
        Boolean isMy = redPacketRequest.getUserId().equals(activityUserRedPacket.getUserId());
        return ResponseResult.buildSuccessResponse(UserRedPacketResponse.builder().currentUserRedPacket(activityUserRedPacket).isMy(isMy).build());
    }

    @Override
    @PostMapping(value = "/v1/ActivityUserRedPacketService/queryUserRedPacketById")
    public ResponseResult<ActivityUserRedPacketDto> queryUserRedPacketById(@RequestParam Long activityUserRedPacketId) {
        if (activityUserRedPacketId == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        try {
            ActivityUserRedPacket activityUserRedPacket = activityUserRedPacketMapper.selectByPrimaryKey(activityUserRedPacketId);
            if (activityUserRedPacket != null) {
                return ResponseResult.buildSuccessResponse(BeanMapper.map(activityUserRedPacket, ActivityUserRedPacketDto.class));
            }
            return ResponseResult.buildSuccessResponse();
        } catch (Exception e) {
            log.error("", e);
            return ResponseResult.buildFailResponse(QUERY_USER_RED_PACKET_EXCEPTION.code(), QUERY_USER_RED_PACKET_EXCEPTION.message());
        }
    }

    /**
     * 领取新人红包
     *
     * @param userId
     * @return
     */
    @Override
    @PostMapping(value = "/v1/ActivityUserRedPacketService/receiveNewRedPacket")
    public ResponseResult<UserRedPacketDto> receiveNewRedPacket(@RequestParam Long userId,@RequestParam(required = false) Long userActivityId) {
        log.info("-----------------新人领取红包：userId -> {}，userActivityId -> {}", userId, userActivityId);
        if (userId == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        try {
            String RECEIVE_NEW_REDPACKET_KEY = "receive_new_redpacket:" + userId;
            String redPacketJson = RedisCache.get(RECEIVE_NEW_REDPACKET_KEY);
            if (StringUtils.isNotBlank(redPacketJson)) {
                return ResponseResult.buildSuccessResponse(JSON.parseObject(redPacketJson, UserRedPacketDto.class));
            }
            ActivityQueryParam queryParam = new ActivityQueryParam();
            queryParam.setType(NEW_REDPACKET);
            //queryParam.setBusinessLine(); FIXME
            ResponseResult<ActivityRedPacketDto> activityRedPacket = activityService.currentActivityRedPacket(queryParam);
            List<UserRedPacketDto> redPacketDtoList = userRedPacketMapper.queryUserRedPacket(userId, NEW_REDPACKET.getCode(), true);
            log.info("----------------领取新人红包activityRedPacket -> {}，redPacketDtoList -> {}", JSON.toJSONString(activityRedPacket), JSON.toJSONString(redPacketDtoList));
            if (CollectionUtils.isNotEmpty(redPacketDtoList)) {
                UserRedPacketDto userRedPacket = redPacketDtoList.get(0);
                if (activityRedPacket.getData() != null) {
                    userRedPacket.setName(activityRedPacket.getData().getName());
                    userRedPacket.setBgImage(activityRedPacket.getData().getBgImage());
                }
                // 已领过
                return ResponseResult.buildSuccessResponse(userRedPacket);
            } else {
                if (activityRedPacket.isSuccess()) {
                    ActivityRedPacketDto currentRedpacket = activityRedPacket.getData();
                    String key = "receive_new_red_packet_concurrent:" + userId;
                    // 领取
                    boolean flag = RedisCache.setIfAbsent(key, "1");
                    DateTime now = DateTime.now();
                    if (currentRedpacket != null && flag) {
                        DateTime validDate = now.plusDays(currentRedpacket.getValid().intValue());
                        Date expireTime = new DateTime(validDate.getYear(), validDate.getMonthOfYear(), validDate.getDayOfMonth(), 23, 59, 59).toDate();
                        List<UserRedPacket> list = Lists.newArrayList();
                        UserRedPacket maxUserRedPacket = null;
                        for (BigDecimal amount : currentRedpacket.getAmounts()){
                            UserRedPacket record = new UserRedPacket();
                            record.setId(IdGenerator.nextId());
                            record.setAcitivtyId(currentRedpacket.getActivityId());
                            record.setUserActivityId(userActivityId);
                            record.setName(currentRedpacket.getName());
                            record.setType(currentRedpacket.getType());
                            record.setUserId(userId);
                            record.setAmount(amount);
                            record.setExpireTime(expireTime);
                            record.setIsShare(currentRedpacket.getIsShare());
                            record.setIsValid((byte) 1);
                            record.setIsUsed((byte) 0);
                            record.setCreateTime(now.toDate());
                            record.setUpdateTime(now.toDate());
                            if (maxUserRedPacket != null){
                                if (amount.compareTo(maxUserRedPacket.getAmount()) > 0){
                                    maxUserRedPacket = record;
                                }
                            }else {
                                maxUserRedPacket = record;
                            }
                            list.add(record);
                        }
                        int row = userRedPacketMapper.insertBatch(list);
                        log.info("row -> {}, maxUserRedPacket -> {}",row,maxUserRedPacket);
                        RedisCache.expire(key, 1, TimeUnit.SECONDS);
                        if (row > 0) {
                            UserRedPacketDto userRedPacket = BeanMapper.map(maxUserRedPacket, UserRedPacketDto.class);
                            userRedPacket.setName(currentRedpacket.getName());
                            userRedPacket.setBgImage(currentRedpacket.getBgImage());
                            int seconds = Seconds.secondsBetween(DateTime.now(), new DateTime(expireTime)).getSeconds();
                            RedisCache.setEx(RECEIVE_NEW_REDPACKET_KEY, JSON.toJSONString(userRedPacket), Long.valueOf(seconds), TimeUnit.SECONDS);
                            return ResponseResult.buildSuccessResponse(userRedPacket);
                        }
                        return ResponseResult.buildFailResponse(RECEIVE_RED_PACKET_FAIL.code(), RECEIVE_RED_PACKET_FAIL.message());
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildFailResponse(QUERY_USER_RED_PACKET_EXCEPTION.code(), QUERY_USER_RED_PACKET_EXCEPTION.message());
    }

    /**
     * 多人分享红包实例
     *
     * @param redPacketRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    @PostMapping(value = "/v1/ActivityUserRedPacketService/createMultiShareRedPacket")
    public ResponseResult<UserRedPacketResponse> createMultiShareRedPacket(@RequestBody UserRedPacketRequest redPacketRequest) throws Exception {
        if (redPacketRequest == null || redPacketRequest.getLaunchType() == null || redPacketRequest.getLaunchId() == null
                || redPacketRequest.getOpenid() == null || redPacketRequest.getUserId() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }


        RedPacketLaunchType launchType = redPacketRequest.getLaunchType();
        Long launchId = redPacketRequest.getLaunchId();
        // 多人分享红包
        ActivityUserRedPacketDto activityUserRedPacket = activityUserRedPacketMapper.queryUserRedPacketByLaunch(launchType.getCode(), launchId);
        log.info("创建多人分享红包 activityUserRedPacket -> {}", activityUserRedPacket);
        if (activityUserRedPacket == null) {
            ActivityQueryParam queryParam = new ActivityQueryParam();
            queryParam.setBusinessLine(redPacketRequest.getBusinessLine());
            queryParam.setType(SHARE_REDPACKET);
            ResponseResult<ActivityRedPacketDto> responseResult = activityService.currentActivityRedPacket(queryParam);
            log.info(" 分享红包 responseResult -> {}", responseResult.getData());
            if (!responseResult.isSuccess()) {
                log.info("获取分享红包失败");
                return ResponseResult.buildFailResponse(responseResult.getCode(), responseResult.getMessage());
            }

            ActivityRedPacketDto multiShareRedPacket = responseResult.getData();
            // 预算最大、最小金额
            BigDecimal minBudgetAmount = multiShareRedPacket.getMinBudgetAmount();
            BigDecimal maxBudgetAmount = multiShareRedPacket.getMaxBudgetAmount();
            if (minBudgetAmount.intValue() >= maxBudgetAmount.intValue()) {
                log.info(INVALID_BUDGET_RED_PACKET_AMOUT.message());
                return ResponseResult.buildFailResponse(INVALID_BUDGET_RED_PACKET_AMOUT.code(), INVALID_BUDGET_RED_PACKET_AMOUT.message());
            }

            // 领取最大、最小金额
            BigDecimal minAmount = multiShareRedPacket.getMinAmount();
            BigDecimal maxAmount = multiShareRedPacket.getMaxAmount();
            int personNum = multiShareRedPacket.getPlayers();
            if (minAmount.intValue() > maxAmount.intValue()) {
                log.info(INVALID_RECIVED_RED_PACKET_AMOUT.message());
                return ResponseResult.buildFailResponse(INVALID_RECIVED_RED_PACKET_AMOUT.code(), INVALID_RECIVED_RED_PACKET_AMOUT.message());
            }

            BigDecimal amount = BigDecimal.valueOf((maxBudgetAmount.intValue() - minBudgetAmount.intValue()) / 2 + minBudgetAmount.intValue());
            log.info("红包金额1 amount -> {}", amount);
            Random random = new Random();
            int r = random.nextInt(maxBudgetAmount.subtract(amount).intValue());
            amount = amount.add(BigDecimal.valueOf(r));
            log.info("红包金额2 amount -> {}", amount);
            //用户发起红包活动
            Long userActivityId = insertActivityUserRedPacket(redPacketRequest, multiShareRedPacket, amount);
            Long activityId = multiShareRedPacket.getActivityId();
            if (userActivityId > 0) {
                String lockKey = "red_packet_pool_lock:" + launchId;
                List<BigDecimal> redPacketList = PmsPoolUtil.random(lockKey, amount, minAmount.intValue(), maxAmount.intValue(), personNum);
                if (CollectionUtils.isEmpty(redPacketList)) {
                    throw new Exception("生产红包池异常");
                }
                // 生成红包池
                List<String> strList = batchInsertRedPacketPool(redPacketList, activityId, userActivityId);
                if (strList.size() > 0) {
                    // 红包数据入队列
                    Long row = PmsPoolUtil.createLunchRedPacketQueue(userActivityId, strList);
                    if (row <= 0) {
                        log.info("红包入队列失败");
                    }
                }
            }
            return ResponseResult.buildSuccessResponse();
        }
        return ResponseResult.buildFailResponse(USER_RED_PACKET_EXISTED.code(), USER_RED_PACKET_EXISTED.message());
    }

    /**
     * 领取多人分享红包
     *
     * @param redPacketRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @AfterReceiveNewRedPacket
    @Override
    @PostMapping(value = "/v1/ActivityUserRedPacketService/recevieMultiShareRedPacket")
    public ResponseResult<UserRedPacketResponse> recevieMultiShareRedPacket(@RequestBody UserRedPacketRequest redPacketRequest) {
        if (redPacketRequest.getLaunchType() == null || redPacketRequest.getLaunchId() == null || redPacketRequest.getIsNewUser() == null
                || redPacketRequest.getOpenid() == null || redPacketRequest.getUserId() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }

        UserRedPacketResponse userRedPacketResponse = new UserRedPacketResponse();
        Long userId = redPacketRequest.getUserId();

        Long launchId = redPacketRequest.getLaunchId();
        Byte launchType = redPacketRequest.getLaunchType().getCode();

        String nickName = redPacketRequest.getNickName();
        String headPortraitUrl = redPacketRequest.getHeadPortraitUrl();

        // 查询多人分享红包
        ActivityQueryParam queryParam = new ActivityQueryParam();
        queryParam.setBusinessLine(redPacketRequest.getBusinessLine());
        queryParam.setType(SHARE_REDPACKET);
        ResponseResult<ActivityRedPacketDto> responseResult = activityService.currentActivityRedPacket(queryParam);
        ActivityRedPacketDto activityRedPacket = responseResult.getData();
        userRedPacketResponse.setDescript(activityRedPacket.getDescript());
        userRedPacketResponse.setMasterPic(activityRedPacket.getMasterPic());
        String redPacketName = activityRedPacket.getName();
        Long activityId = activityRedPacket.getActivityId();
        Integer clickLimit = activityRedPacket.getClickLimit().intValue();//每人每天点击次数限制

        if (responseResult.isSuccess()) { // 活动进行中
            // 是否领取完成
            ActivityUserRedPacketDto activityUserRedPacket = activityUserRedPacketMapper.queryUserRedPacketByLaunch(launchType, launchId);
            if (activityUserRedPacket == null) {
                //补偿创建红包
                compensateCreateMultiShareRedPacket(launchId);

                activityUserRedPacket = activityUserRedPacketMapper.queryUserRedPacketByLaunch(launchType, launchId);
                if (activityUserRedPacket == null){
                    return ResponseResult.buildFailResponse(ACTIVITY_USER_RED_PACKET_NOT_EXIST.code(), ACTIVITY_USER_RED_PACKET_NOT_EXIST.message());
                }
            }
            if (activityUserRedPacket != null){
                Date now = new Date();
                if (now.after(activityUserRedPacket.getEndDate())){
                    // 活动已结束，查询历史数据
                    return fetchHistoryRecord(userRedPacketResponse,launchType,launchId,userId,activityId,redPacketName,clickLimit);
                }
            }
            Long userActivityId = activityUserRedPacket.getId();
            userRedPacketResponse.setUserActivityId(userActivityId);
            if (activityUserRedPacket.getIsSuccess().intValue() == 1) { //已领完
                log.info("红包已领完。。。。");
                return redPacketCompleted(userRedPacketResponse,launchType,launchId,userId,activityId,userActivityId,redPacketName,clickLimit);
            }
            // 判断用户是否授权
            if (StringUtils.isEmpty(nickName) && StringUtils.isEmpty(headPortraitUrl)) {
                log.info("用户未授权。。。。");
                return ResponseResult.buildFailResponse(USER_NOT_AUTHORIZATION.code(), USER_NOT_AUTHORIZATION.message());
            }

            Byte clickCount = activityUserRedPacket.getClickCount() == null ? 0 : activityUserRedPacket.getClickCount();// 点击次数
            Byte players = activityUserRedPacket.getPlayers();
            Byte isShare = activityRedPacket.getIsShare();
            Byte valid = activityRedPacket.getValid();
            //每天领取次数是否超限
            int count = redPacketPoolMapper.receiveCountToday(activityId, redPacketRequest.getUserId());
            if (count >= clickLimit) {
                log.info("领取次数超限。。。。");
                userRedPacketResponse.setOverLimit(true);
                // 查询该活动领取记录
                List<ActivityUserRedPacketDto> userRedPacketRecordList = multiShareRedPacketRecord(launchType, launchId);
                // 用户是否领取
                isReceivedRedPacket(userRedPacketResponse,userId,userActivityId,redPacketName,true);

                userRedPacketResponse.setMultiShareRedPacketRecord(userRedPacketRecordList);
                return ResponseResult.buildSuccessResponse(userRedPacketResponse);
            }

            // 是否领取过该红包
            int received = redPacketPoolMapper.receiveCount(null, redPacketRequest.getUserId(), userActivityId);
            if (received > 0) {
                // 用户是否领取
                isReceivedRedPacket(userRedPacketResponse,userId,userActivityId,redPacketName,true);

                // 查询该活动领取记录
                List<ActivityUserRedPacketDto> userRedPacketRecordList = multiShareRedPacketRecord(launchType, launchId);
                userRedPacketResponse.setMultiShareRedPacketRecord(userRedPacketRecordList);
                return ResponseResult.buildSuccessResponse(userRedPacketResponse);
            }

            String key = "receive_multi_share_red_packet_concurrent:" + userId+"_"+launchId;
            if (RedisCache.setIfAbsent(key, "1")) {
                // 静默领取红包
                String jsonStr = PmsPoolUtil.popLunchRedPacketQueue(userActivityId);
                log.info("红包池pop队列 jsonStr -> {}", jsonStr);
                if (StringUtils.isNotBlank(jsonStr)) { // 领取成功
                    RedPacketPool redPacketPool = JSONObject.parseObject(jsonStr, RedPacketPool.class);
                    // 更新红包池状态
                    updateRedPacketPool(redPacketPool, userId, nickName, headPortraitUrl);

                    // 用户领取红包
                    DateTime validDate = DateTime.now().plusDays(valid.intValue());
                    Date expireTime = new DateTime(validDate.getYear(), validDate.getMonthOfYear(), validDate.getDayOfMonth(), 23, 59, 59).toDate();
                    UserRedPacket userRedPacket = receiveRedPacket(activityId, userActivityId, activityRedPacket.getName(), redPacketRequest, isShare, redPacketPool.getAmount(), expireTime);
                    if (userRedPacket != null) {
                        UserRedPacketDto receiveRedPacket = BeanMapper.map(userRedPacket, UserRedPacketDto.class);
                        receiveRedPacket.setName(activityRedPacket.getName());
                        userRedPacketResponse.setReceiveRedPacket(receiveRedPacket);
                        userRedPacketResponse.setReceived(true);
                    }
                    ActivityUserRedPacket record = new ActivityUserRedPacket();
                    // 如完成，更新红包活动状态
                    if (Integer.valueOf(players).equals(Integer.valueOf(clickCount.intValue() + 1))) {
                        // 已完成
                        record.setIsSuccess((byte) 1);
                        record.setClickCount(players);
                    } else {
                        // 未完成
                        record.setClickCount((byte) (clickCount.intValue() + 1));
                    }
                    record.setId(userActivityId);
                    record.setUpdateTime(new Date());
                    activityUserRedPacketMapper.updateByPrimaryKeySelective(record);
                    userRedPacketResponse.setReceived(false);
                } else {// 领取失败
                    log.info("多人分享活动领取失败");
                }
            }
            RedisCache.expire(key, 1, TimeUnit.SECONDS);

            // 设置领取状态
            isReceivedRedPacket(userRedPacketResponse,userId,userActivityId,redPacketName,null);

            // 查询该活动领取记录
            List<ActivityUserRedPacketDto> userRedPacketRecordList = multiShareRedPacketRecord(launchType, launchId);
            userRedPacketResponse.setMultiShareRedPacketRecord(userRedPacketRecordList);
            return ResponseResult.buildSuccessResponse(userRedPacketResponse);
        }

        // 活动已结束，查询历史数据
        return fetchHistoryRecord(userRedPacketResponse,launchType,launchId,userId,activityId,redPacketName,clickLimit);
    }

    @Transactional(rollbackFor = Exception.class)
    @AfterReceiveNewRedPacket
    @Override
    @PostMapping(value = "/v1/ActivityUserRedPacketService/recevieLCMultiShareRedPacket")
    public ResponseResult<LCUserRedPacketResponse> recevieLCMultiShareRedPacket(@RequestBody UserRedPacketRequest redPacketRequest) {
        if (redPacketRequest.getLaunchType() == null || redPacketRequest.getLaunchId() == null || redPacketRequest.getIsNewUser() == null
                || redPacketRequest.getOpenid() == null || redPacketRequest.getUserId() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        ResponseResult<UserRedPacketResponse> receviedRedpacketResult = recevieMultiShareRedPacket(redPacketRequest);
        LCUserRedPacketResponse redPacketResponse = new LCUserRedPacketResponse();
        if (receviedRedpacketResult.getData() != null){
            BeanMapper.copy(receviedRedpacketResult.getData(),redPacketResponse);
        }

        UserCouponQueryParam queryParam = new UserCouponQueryParam();
        queryParam.setUserId(redPacketRequest.getUserId());
        queryParam.setBusinessLine(BREAKFAST.getCode());
        Byte isNewUser = redPacketRequest.getIsNewUser();
        if (isNewUser.intValue() == 0){//新用户
            //领取早餐券
            ResponseResult<UserCouponDto> responseResult = breakfastCouponService.receiveNewCoupon(queryParam);
            redPacketResponse.setReceivedCoupon(responseResult.isSuccess() && responseResult.getData() != null);
        }
        return ResponseResult.build(receviedRedpacketResult.isSuccess(),receviedRedpacketResult.getCode(),receviedRedpacketResult.getMessage(),redPacketResponse);
    }

    /**
     * 补偿创建红包
     * @param orderId
     */
    private void compensateCreateMultiShareRedPacket(Long orderId){
        try {
            ResponseResult<OrderDto> simpleOrder = orderService.getSimpleOrderById(orderId);
            if (simpleOrder.isSuccess() && simpleOrder.getData() != null && simpleOrder.getData().getUserId() != null){
                Long userId = simpleOrder.getData().getUserId();
                log.info("补偿生产多人分享红包！！！orderId -> {}, userId -> {}",orderId,userId);
                if (userId != null){
                    UserDto param = new UserDto();
                    param.setId(userId);
                    param.setBusinessLine(OrderType.LUNCH.getValue());
                    ResponseResult<UserDto> result = userService.getUserInfo(param);
                    if (result.isSuccess() && result.getData() != null){
                        UserDto userDto = result.getData();
                        UserRedPacketRequest redPacketRequest = UserRedPacketRequest.builder().userId(userDto.getId()).openid(userDto.getOpenId())
                                .nickName(userDto.getNickName()).headPortraitUrl(userDto.getHeadPortraitUrl()).launchType(ORDER_SHARE).launchId(orderId).build();
                        createMultiShareRedPacket(redPacketRequest);
                    }else {
                        log.info("get user info from ucs  is null !!! orderId -> {}",orderId);
                    }
                }
            }else {
                log.info("get userid from simple order is null !!! orderId -> {}",orderId);
            }

        } catch (Exception e) {
            log.error("",e);
        }
    }

    /**
     * 红包已领完
     * @param userRedPacketResponse
     * @param launchType
     * @param launchId
     * @param userId
     * @param activityId
     * @param userActivityId
     * @param redPacketName
     * @param clickLimit
     * @return
     */
    private ResponseResult<UserRedPacketResponse> redPacketCompleted(UserRedPacketResponse userRedPacketResponse,Byte launchType,Long launchId,Long userId,Long activityId,Long userActivityId,String redPacketName,Integer clickLimit){
        log.info("红包已领完。。。。");
        userRedPacketResponse.setFinished(true);
        // 查询该活动领取记录
        List<ActivityUserRedPacketDto> userRedPacketRecordList = multiShareRedPacketRecord(launchType, launchId);
        // 用户是否领取
        isReceivedRedPacket(userRedPacketResponse,userId,userActivityId,redPacketName,true);

        userRedPacketResponse.setMultiShareRedPacketRecord(userRedPacketRecordList);
        //每天领取次数是否超限
        int count = redPacketPoolMapper.receiveCountToday(activityId, userId);
        if (count >= clickLimit) {
            userRedPacketResponse.setOverLimit(true);
        }
        return ResponseResult.buildSuccessResponse(userRedPacketResponse);
    }

    private ResponseResult<UserRedPacketResponse> fetchHistoryRecord(UserRedPacketResponse userRedPacketResponse,Byte launchType,Long launchId,Long userId,Long activityId,String redPacketName,Integer clickLimit){
        // 活动已结束，查询历史数据
        ActivityUserRedPacketDto activityUserRedPacket = activityUserRedPacketMapper.queryUserRedPacketByLaunch(launchType, launchId);
        if (activityUserRedPacket == null) {
            return ResponseResult.buildFailResponse(ACTIVITY_USER_RED_PACKET_NOT_EXIST.code(), ACTIVITY_USER_RED_PACKET_NOT_EXIST.message());
        }
        Long userActivityId = activityUserRedPacket.getId();
        userRedPacketResponse.setUserActivityId(userActivityId);
        // 用户是否领取
        isReceivedRedPacket(userRedPacketResponse,userId,userActivityId,redPacketName,true);

        //每天领取次数是否超限
        int count = redPacketPoolMapper.receiveCountToday(activityId, userId);
        if (count >= clickLimit) {
            userRedPacketResponse.setOverLimit(true);
        }
        // 活动已结束，查询该活动领取记录
        userRedPacketResponse.setIsOver(true);
        userRedPacketResponse.setMultiShareRedPacketRecord(multiShareRedPacketRecord(launchType, launchId));
        return ResponseResult.buildSuccessResponse(userRedPacketResponse);
    }



    /**
     * 是否已领取红包
     * @param userRedPacketResponse
     * @param userId
     * @param userActivityId
     * @param redPacketName
     * @param received
     */
    private void isReceivedRedPacket(UserRedPacketResponse userRedPacketResponse,Long userId,Long userActivityId,String redPacketName,Boolean received){
        UserRedPacketDto receiveRedPacket = userRedPacketMapper.queryUserRedPacketByUserActivityId(userId, userActivityId, SHARE_REDPACKET.getCode());
        if (receiveRedPacket != null) {
            receiveRedPacket.setName(redPacketName);
            userRedPacketResponse.setReceiveRedPacket(receiveRedPacket);
            userRedPacketResponse.setReceived(received);
        }
    }

    /**
     * 多人分享红包订单入口
     *
     * @param redPacketLaunchQuery
     * @return
     */
    @Override
    @PostMapping(value = "/v1/ActivityUserRedPacketService/multiShareRedPacketLaunchQuery")
    public ResponseResult<RedPacketLaunchResultDto> multiShareRedPacketLaunchQuery(@RequestBody RedPacketLaunchQueryDto redPacketLaunchQuery) {
        if (redPacketLaunchQuery == null || CollectionUtils.isEmpty(redPacketLaunchQuery.getRedPacketLaunchList())) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        try {
            ActivityQueryParam queryParam = new ActivityQueryParam();
            queryParam.setBusinessLine(redPacketLaunchQuery.getBusinessLine());
            queryParam.setType(SHARE_REDPACKET);
            ResponseResult<ActivityRedPacketDto> responseResult = activityService.currentActivityRedPacket(queryParam);
            if (responseResult.isSuccess() && responseResult.getData() != null) {
                ActivityRedPacketDto activityRedPacket = responseResult.getData();

                Date startDate = activityRedPacket.getStartDate();
                Date endDate = activityRedPacket.getEndDate();// 活动结束日期
                Integer showDays = activityRedPacket.getShowDays();// 展示天数
                log.info("activityRedPacket -> {}", activityRedPacket);

                Set<Long> validOrderSet = Sets.newHashSet();

                DateTime now = DateTime.now();
                redPacketLaunchQuery.getRedPacketLaunchList().stream().forEach(lanuch -> {
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

    private void updateRedPacketPool(RedPacketPool redPacketPool, Long userId, String nickName, String headPortraitUrl) {
        redPacketPool.setStatus((byte) 1);
        redPacketPool.setUserId(userId);
        redPacketPool.setNickName(nickName);
        redPacketPool.setHeadPortraitUrl(headPortraitUrl);
        redPacketPool.setUpdateTime(new Date());
        redPacketPoolMapper.updateByPrimaryKey(redPacketPool);
    }

    private UserRedPacket receiveRedPacket(Long activityId, Long userActivityId, String activityName, UserRedPacketRequest redPacketRequest, byte isShare, BigDecimal amount, Date expireTime) {
        UserRedPacket userRedPacket = new UserRedPacket();
        userRedPacket.setId(IdGenerator.nextId());
        userRedPacket.setAcitivtyId(activityId);
        userRedPacket.setUserActivityId(userActivityId);
        userRedPacket.setType(SHARE_REDPACKET.getCode());
        userRedPacket.setName(activityName);
        userRedPacket.setUserId(redPacketRequest.getUserId());
        userRedPacket.setAmount(amount);
        userRedPacket.setExpireTime(expireTime);
        userRedPacket.setIsShare(isShare);
        userRedPacket.setIsValid((byte) 1);
        userRedPacket.setIsUsed((byte) 0);
        userRedPacket.setCreateTime(new Date());
        userRedPacket.setUpdateTime(new Date());
        userRedPacketMapper.insertSelective(userRedPacket);
        return userRedPacket;
    }

    private List<ActivityUserRedPacketDto> multiShareRedPacketRecord(Byte launchType, Long launchId) {
        return activityUserRedPacketMapper.multiShareRedPacketRecord(launchType, launchId);
    }

    private List<String> batchInsertRedPacketPool(List<BigDecimal> redPacketList, Long activityId, Long userActivityId) {
        List<RedPacketPool> redPacketPoolList = Lists.newArrayListWithCapacity(redPacketList.size());
        List<String> redPacketPoolStrList = Lists.newArrayListWithCapacity(redPacketList.size());
        redPacketList.stream().forEach(amount -> {
            RedPacketPool pool = new RedPacketPool();
            pool.setId(IdGenerator.nextId());
            pool.setActivityId(activityId);
            pool.setAmount(amount);
            pool.setStatus((byte) 0);
            pool.setUserActivityId(userActivityId);
            pool.setCreateTime(new Date());
            pool.setUpdateTime(new Date());
            redPacketPoolList.add(pool);
            redPacketPoolStrList.add(JSONObject.toJSONString(pool));
        });
        int row = redPacketPoolMapper.batchInsert(redPacketPoolList);
        if (row > 0) {
            return redPacketPoolStrList;
        }
        return Lists.newArrayList();

    }

    private Long insertActivityUserRedPacket(UserRedPacketRequest redPacketRequest, ActivityRedPacketDto multiShareRedPacket, BigDecimal amount) throws Exception {
        RedPacketLaunchType launchType = redPacketRequest.getLaunchType();
        Long launchId = redPacketRequest.getLaunchId();
        Long userid = redPacketRequest.getUserId();
        String openid = redPacketRequest.getOpenid();
        String key = "multi_share_red_packet:" + launchId;
        Boolean flag = RedisCache.setIfAbsent(key, "1");
        Long id = 0L;
        try {
            if (multiShareRedPacket != null && flag) {
                ActivityUserRedPacket redPacket = new ActivityUserRedPacket();
                id = IdGenerator.nextId();
                redPacket.setId(id);
                redPacket.setActivityId(multiShareRedPacket.getActivityId());
                redPacket.setType(multiShareRedPacket.getType());
                redPacket.setLaunchType(launchType.getCode());
                redPacket.setLaunchId(launchId);
                redPacket.setUserId(userid);
                redPacket.setOpenId(openid);
                redPacket.setNickName(redPacketRequest.getNickName());
                redPacket.setHeadPortraitUrl(redPacketRequest.getHeadPortraitUrl());
                redPacket.setIsSuccess((byte) 0);
                redPacket.setAmount(amount);
                redPacket.setPlayers(multiShareRedPacket.getPlayers());
                redPacket.setValid(multiShareRedPacket.getValid());
                redPacket.setClickLimit(multiShareRedPacket.getClickLimit());
                redPacket.setClickCount((byte) 0);
                redPacket.setIsShare(multiShareRedPacket.getIsShare());
                redPacket.setStartDate(multiShareRedPacket.getStartDate());
                redPacket.setEndDate(multiShareRedPacket.getEndDate());
                redPacket.setCreateTime(new Date());
                redPacket.setUpdateTime(new Date());
                int row = activityUserRedPacketMapper.insertSelective(redPacket);
                if (row > 0) {
                    return id;
                }
            }
        } catch (Exception e) {
            log.error("", e);
            throw new Exception("生产红包池异常");
        } finally {
            RedisCache.expire(key, 5, TimeUnit.SECONDS);
        }
        return id;
    }


    private ActivityRedPacketActionDto hackRedPacket(ActivityUserRedPacketDto activityUserRedPacket, Integer hackTimes) {
        Long activityId = activityUserRedPacket.getId();
        Long userId = activityUserRedPacket.getUserId();
        String key = "hack_redpacket:" + activityId + "_" + userId;
        Boolean success = RedisCache.setIfAbsent(key, "1");
        try {
            if (success) {
                // 每人每天帮别人砍几次
                Integer hackLimit = activityUserRedPacket.getClickLimit().intValue();
                if (hackTimes < hackLimit) {
                    // 砍一刀
                    return activityRedPacketHackService.hackUserRedPacket(activityUserRedPacket).getData();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("砍红包异常");
        } finally {
            RedisCache.expire(key, 1, TimeUnit.MINUTES);
        }
        return null;
    }

    private void finishRedPacket(ActivityUserRedPacketDto activityUserRedPacket) {
        // 砍包活动状态置为完成
        ActivityUserRedPacket record = new ActivityUserRedPacket();
        record.setId(activityUserRedPacket.getId());
        record.setIsSuccess((byte) 1);
        record.setUpdateTime(new Date());
        activityUserRedPacketMapper.updateByPrimaryKeySelective(record);

        // 生成用户可用红包
        UserRedPacket userRedPacket = new UserRedPacket();
        userRedPacket.setId(IdGenerator.nextId());
        userRedPacket.setAcitivtyId(activityUserRedPacket.getActivityId());
        userRedPacket.setUserId(activityUserRedPacket.getUserId());
        userRedPacket.setAmount(activityUserRedPacket.getAmount());
        userRedPacket.setExpireTime(activityUserRedPacket.getEndDate());
        userRedPacket.setIsShare(activityUserRedPacket.getIsShare());
        if (activityUserRedPacket.getEndDate().before(new Date())) {
            userRedPacket.setIsValid(VALID.getCode());//有效
        } else {
            userRedPacket.setIsValid(INVALID.getCode());//无效
        }
        userRedPacket.setIsUsed(UNUSED.getCode());//未使用
        userRedPacketMapper.insertSelective(userRedPacket);
        //TODO 给发起者发送完成消息
    }

    /**
     * 查询有无发起过砍红包
     *
     * @param userId
     * @return
     */
    @Override
    @PostMapping(value = "/v1/ActivityUserRedPacketService/queryUserCutRedPacketByUserId")
    public ResponseResult<ActivityUserRedPacketDto> queryUserCutRedPacketByUserId(@RequestParam Long userId) {
        if (userId == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        try {
            ActivityUserRedPacketDto activityUserRedPacket = activityUserRedPacketMapper.queryUserRedPacket(userId, CUT_REDPACKET.getCode());
            return ResponseResult.buildSuccessResponse(activityUserRedPacket);
        } catch (Exception e) {
            log.error("", e);
            return ResponseResult.buildFailResponse(QUERY_USER_RED_PACKET_EXCEPTION.code(), QUERY_USER_RED_PACKET_EXCEPTION.message());
        }
    }

    /**
     * 创建用户红包
     *
     * @param redPacketRequest
     */
    private ActivityUserRedPacketDto doCreateUserCutRedPacket(UserRedPacketRequest redPacketRequest) {
        // 当前红包
        ActivityQueryParam queryParam = new ActivityQueryParam();
        queryParam.setType(CUT_REDPACKET);
        queryParam.setBusinessLine(redPacketRequest.getBusinessLine());
        ResponseResult<ActivityRedPacketDto> result = activityService.currentActivityRedPacket(queryParam);
        if (result.isSuccess() && result.getData() != null) {
            ActivityRedPacketDto activityRedPacket = result.getData();
            String key = "user_red_packet_concurrent:" + redPacketRequest.getUserId();
            boolean success = RedisCache.setIfAbsent(key, "1");
            try {
                if (success) {
                    ActivityUserRedPacket userRedPacket = new ActivityUserRedPacket();
                    userRedPacket.setId(IdGenerator.nextId());
                    userRedPacket.setActivityId(activityRedPacket.getId());
                    userRedPacket.setUserId(redPacketRequest.getUserId());
                    userRedPacket.setOpenId(redPacketRequest.getOpenid());
                    userRedPacket.setNickName(redPacketRequest.getNickName());
                    userRedPacket.setHeadPortraitUrl(redPacketRequest.getHeadPortraitUrl());
                    userRedPacket.setClickCount((byte) 0);
                    userRedPacket.setIsSuccess((byte) 0);
                    userRedPacket.setAmount(activityRedPacket.getAmount());
                    userRedPacket.setPlayers(activityRedPacket.getPlayers());
                    userRedPacket.setValid(activityRedPacket.getValid());
                    userRedPacket.setClickLimit(activityRedPacket.getClickLimit());
                    DateTime today = DateTime.now();
                    userRedPacket.setStartDate(today.toDate());
                    userRedPacket.setEndDate(today.plusDays(activityRedPacket.getValid()).toDate());
                    userRedPacket.setCreateTime(today.toDate());
                    userRedPacket.setUpdateTime(today.toDate());
                    int row = activityUserRedPacketMapper.insertSelective(userRedPacket);
                    if (row > 0) {
                        return BeanMapper.map(userRedPacket, ActivityUserRedPacketDto.class);
                    }
                }
            } catch (Exception e) {
                log.error("", e);
                throw new RuntimeException("创建红包异常");
            } finally {
                RedisCache.expire(key, 1, TimeUnit.MINUTES);
            }
        } else {
            log.info("不存在生效红包！！！");
        }
        return null;
    }


}