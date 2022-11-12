package com.milisong.pms.prom.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.milisong.oms.api.PromotionLockerService;
import com.milisong.oms.constant.OrderType;
import com.milisong.pms.prom.api.ActivityService;
import com.milisong.pms.prom.api.UserRedPacketService;
import com.milisong.pms.prom.domain.UserRedPacket;
import com.milisong.pms.prom.dto.*;
import com.milisong.pms.prom.enums.RedPacketType;
import com.milisong.pms.prom.mapper.UserCouponMapper;
import com.milisong.pms.prom.mapper.UserRedPacketMapper;
import com.milisong.ucs.api.UserService;
import com.milisong.ucs.dto.Pagenation;
import com.milisong.ucs.dto.UserDto;
import com.milisong.ucs.enums.BusinessLineEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.milisong.pms.prom.enums.PromotionResponseCode.*;

/**
 * @author sailor wang
 * @date 2018/9/14 下午7:07
 * @description
 */
@Slf4j
@Service
@RestController
public class UserRedPacketServiceImpl implements UserRedPacketService {

    @Autowired
    UserRedPacketMapper userRedPacketMapper;

    @Autowired
    ActivityService activityService;

    @Autowired
    UserService userService;

    @Autowired
    PromotionLockerService promotionLockerService;

    @Autowired
    CommonService commonService;

    @Autowired
    UserCouponMapper userCouponMapper;

    Integer fetchSize = 500;


    /**
     * 查询用户红包
     *
     * @param userId
     * @return
     */
    @Override
    @PostMapping(value = "/v1/UserRedPacketService/queryUserRedPacket")
    public ResponseResult<List<UserRedPacketDto>> queryUserRedPacket(@RequestParam("userId") Long userId, @RequestParam("type") RedPacketType type) {
        if (userId == null || type == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        try {
            List<UserRedPacketDto> userRedPacketList = userRedPacketMapper.queryUserRedPacket(userId, type.getCode(), null);
            return ResponseResult.buildSuccessResponse(userRedPacketList);
        } catch (Exception e) {
            log.error("", e);
            return ResponseResult.buildFailResponse();
        }
    }

    @Override
    @PostMapping(value = "/v1/UserRedPacketService/queryByUserRedPacketId")
    public ResponseResult<UserRedPacketDto> queryByUserRedPacketId(@RequestParam("userRedPacketId") Long userRedPacketId) {
        try {
            UserRedPacket userRedPacket = userRedPacketMapper.selectByPrimaryKey(userRedPacketId);
            if (userRedPacket != null) {
                if (userRedPacket.getIsValid().intValue() == 0) {
                    return ResponseResult.buildFailResponse(USER_RED_PACKET_EXPIRED.code(), USER_RED_PACKET_EXPIRED.message());
                }
                return ResponseResult.buildSuccessResponse(BeanMapper.map(userRedPacket, UserRedPacketDto.class));
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildFailResponse(USER_RED_PACKET_NOT_EXIST.code(), USER_RED_PACKET_NOT_EXIST.message());
    }

    /**
     * 根据订单id查询该订单所用的红包
     *
     * @param orderId
     * @return
     */
    @Override
    @PostMapping(value = "/v1/UserRedPacketService/queryUserRedPacketByOrderId")
    public ResponseResult<UserRedPacketDto> queryUserRedPacketByOrderId(@RequestParam("orderId") Long orderId) {
        if (orderId == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        try {
            UserRedPacketDto userRedPacketDto = userRedPacketMapper.queryUserRedPacketByOrderId(orderId);
            return ResponseResult.buildSuccessResponse(userRedPacketDto);
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildFailResponse(QUERY_USER_RED_PACKET_EXCEPTION.code(), QUERY_USER_RED_PACKET_EXCEPTION.message());
    }

    @Override
    @PostMapping(value = "/v1/UserRedPacketService/useUserRedPacket")
    public ResponseResult<Boolean> useUserRedPacket(@RequestParam("userRedPacketId") Long userRedPacketId, @RequestParam("orderId") Long orderId) {
        try {
            UserRedPacket record = new UserRedPacket();
            record.setId(userRedPacketId);
            record.setIsUsed((byte) 1);
            record.setUpdateTime(new Date());
            record.setOrderId(orderId);
            int row = userRedPacketMapper.updateByPrimaryKeySelective(record);
            return ResponseResult.buildSuccessResponse(row > 0);
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildFailResponse(UPDATE_USER_RED_PACKET_EXCEPTION.code(), UPDATE_USER_RED_PACKET_EXCEPTION.message());
    }

    @Override
    @PostMapping(value = "/v1/UserRedPacketService/updateUserRedPacketUseful")
    public ResponseResult<Boolean> updateUserRedPacketUseful(@RequestParam("userRedPacketId") Long userRedPacketId) {
        if (userRedPacketId == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        try {
            UserRedPacket record = new UserRedPacket();
            record.setId(userRedPacketId);
            record.setIsUsed((byte) 0);
            record.setUpdateTime(new Date());
            int row = userRedPacketMapper.updateByPrimaryKeySelective(record);
            return ResponseResult.buildSuccessResponse(row > 0);
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildFailResponse(UPDATE_USER_RED_PACKET_EXCEPTION.code(), UPDATE_USER_RED_PACKET_EXCEPTION.message());
    }

    @Override
    @PostMapping(value = "/v1/UserRedPacketService/usableUserRedPacketCount")
    public ResponseResult<Integer> usableUserRedPacketCount(@RequestParam("userId") Long userId) {
        if (userId == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        try {
            List<UserRedPacketDto> userRedPacketList = userRedPacketMapper.queryUserRedPacket(userId, null, true);
            if (CollectionUtils.isNotEmpty(userRedPacketList)) {
                List<UserRedPacketDto> usableList = Lists.newArrayList();
                List<Long> locks = redpacketLocks(userRedPacketList);

                for (UserRedPacketDto userRedPacket : userRedPacketList) {
                    if (!locks.contains(userRedPacket.getId())) {
                        usableList.add(userRedPacket);
                    }
                }
                return ResponseResult.buildSuccessResponse(usableList.size());
            }
            return ResponseResult.buildSuccessResponse(0);
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildFailResponse(QUERY_ACTIVITY_RED_PACKET_EXCEPTION.code(), QUERY_ACTIVITY_RED_PACKET_EXCEPTION.message());
    }

    @Override
    @PostMapping(value = "/v1/UserRedPacketService/usableUserRedPackets")
    public ResponseResult<UserRedPacketResponse> usableUserRedPackets(@RequestParam("userId") Long userId) {
        if (userId == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        try {
            List<UserRedPacketDto> userRedPacketList = userRedPacketMapper.queryUserRedPacket(userId, null, true);
            if (CollectionUtils.isNotEmpty(userRedPacketList)) {
                List<UserRedPacketDto> usableList = Lists.newArrayList();
                Map<Integer, List<UserRedPacketDto>> weightRedPacketMap = Maps.newHashMap();
                // 用于筛选最大金额红包，用于默认选择
                Integer maxAmount = 0;
                //红包锁集合
                List<Long> locks = redpacketLocks(userRedPacketList);

                for (UserRedPacketDto userRedPacket : userRedPacketList) {
                    if (!locks.contains(userRedPacket.getId())) { //红包未锁定
                        Integer amount = userRedPacket.getAmount().intValue();
                        if (amount > maxAmount) {
                            maxAmount = amount;
                        }
                        List<UserRedPacketDto> vals = weightRedPacketMap.get(amount);
                        if (CollectionUtils.isEmpty(vals)) {
                            weightRedPacketMap.put(amount, Lists.newArrayList(userRedPacket));
                        } else {
                            vals.add(userRedPacket);
                            weightRedPacketMap.put(amount, vals);
                        }
                        usableList.add(userRedPacket);
                    }
                }

                List<UserRedPacketDto> maxRedPacketList = weightRedPacketMap.get(maxAmount);
                if (CollectionUtils.isNotEmpty(maxRedPacketList)) {
                    maxRedPacketList.get(0).setSelected(true);
                }
                return ResponseResult.buildSuccessResponse(UserRedPacketResponse.builder().usableUserRedPacket(usableList).build());
            }
            return ResponseResult.buildSuccessResponse(UserRedPacketResponse.builder().usableUserRedPacket(Lists.newArrayList()).build());
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildFailResponse(QUERY_USER_RED_PACKET_EXCEPTION.code(), QUERY_USER_RED_PACKET_EXCEPTION.message());
    }

    @Override
    @PostMapping(value = "/v1/UserRedPacketService/userRedPacketList")
    public ResponseResult<Pagination<UserRedPacketDto>> userRedPacketList(@RequestBody ActivityQueryParam queryParam) {
        if (queryParam == null || queryParam.getUserId() == null || queryParam.getUsable() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        int startRow = 0;
        int pageSize = 10;
        try {
            if (queryParam != null) {
                if (queryParam.getPageSize() == null) {
                    queryParam.setPageSize(10);
                }
                if (queryParam.getPageNo() == null) {
                    queryParam.setPageNo(0);
                }
                pageSize = queryParam.getPageSize();
                if (pageSize > 100) {
                    pageSize = 100;
                }
                startRow = (queryParam.getPageNo() - 1) * pageSize;
            }
            int totalCount = userRedPacketMapper.totalCount(queryParam.getUserId(), queryParam.getUsable());
            if (totalCount > 0) {
                // 红包列表
                List<UserRedPacketDto> list = userRedPacketMapper.userRedPacketList(queryParam.getUserId(), startRow, pageSize, queryParam.getUsable());
                if (CollectionUtils.isNotEmpty(list)) {
                    List<UserRedPacketDto> usableList = Lists.newArrayList();
                    //红包锁集合
                    List<Long> locks = redpacketLocks(list);

                    for (UserRedPacketDto userRedPacket : list) {
                        if (!locks.contains(userRedPacket.getId())) {
                            usableList.add(userRedPacket);
                        }
                    }
                    Pagination pagination = new Pagination(queryParam.getPageNo(), pageSize, totalCount, usableList);
                    return ResponseResult.buildSuccessResponse(pagination);
                }
                Pagination pagination = new Pagination(queryParam.getPageNo(), pageSize, totalCount, list);
                return ResponseResult.buildSuccessResponse(pagination);
            }
            return ResponseResult.buildSuccessResponse();
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildFailResponse(QUERY_USER_RED_PACKET_EXCEPTION.code(), QUERY_USER_RED_PACKET_EXCEPTION.message());
    }


    @Override
    @PostMapping(value = "/v1/UserRedPacketService/scanUserRedPacket")
    public ResponseResult scanUserRedPacket() {
        try {
            Date currentDate = new Date();
            userRedPacketMapper.scanUserRedPacket(currentDate);
            return ResponseResult.buildSuccessResponse();
        } catch (Exception e) {
            log.error("定时扫描用户活动异常", e);
            return ResponseResult.buildFailResponse();
        }
    }

    /**
     * 主动推送用户红包
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/v1/UserRedPacketService/sendRedPacket")
    public ResponseResult<Boolean> sendRedPacket() {
        // 促活红包
        String jsonStr = RedisCache.get("activity_red_packet:4");
        log.info("促活红包数据 jsonStr -> {}", jsonStr);
        if (StringUtils.isEmpty(jsonStr)) {
            log.info("促活红包数据为空！！！");
            return ResponseResult.buildFailResponse(ACTIVE_RED_PACKET_NOT_EXIST.code(), ACTIVE_RED_PACKET_NOT_EXIST.message());
        }
        try {
            SendRedPacketConfig sendRedPacketConfig = JSONObject.parseObject(jsonStr, SendRedPacketConfig.class);
            Interval interval = new Interval(new DateTime(sendRedPacketConfig.getStartDate()), new DateTime(sendRedPacketConfig.getEndDate()));
            DateTime now = DateTime.now();

            String sendRedPacketKey = "batch_send_red_packet_flag";
            String sendRedPacketFlag = RedisCache.get(sendRedPacketKey);
            if (StringUtils.isBlank(sendRedPacketFlag)) {
                if (interval.contains(DateTime.now())) {
                    Integer fetchSize = 200;
                    // 查询符合条件的用户 -> 生成用户红包 -> 发送短信
                    UserDto userDto = new UserDto();
                    userDto.setFetchSize(fetchSize);
                    userDto.setBusinessLine(OrderType.LUNCH.getValue());
                    ResponseResult<com.milisong.ucs.dto.Pagenation<UserDto>> responseResult = userService.fetchUser(userDto);
                    com.milisong.ucs.dto.Pagenation<UserDto> pagination = responseResult.getData();
                    if (responseResult.isSuccess() && pagination != null && CollectionUtils.isNotEmpty(pagination.getList())) {
                        log.info("用户数量 -> {}", pagination.getList().size());
                        int totalPage = (int) (Math.ceil(Double.valueOf(pagination.getRowCount()) / fetchSize));

                        Long lastUserId = doSendRedPacket(pagination.getList(), sendRedPacketConfig, now.toDate());
                        log.info("总页数 -> {}, lastUserId -> {}", totalPage, lastUserId);

                        for (int i = 2; i <= totalPage; i++) {
                            try {
                                if (lastUserId != null) {
                                    log.info("lastUserId -> {}, fetchSize -> {}", lastUserId, fetchSize);
                                    userDto.setId(lastUserId);
                                    userDto.setFetchSize(fetchSize);
                                    userDto.setBusinessLine(OrderType.LUNCH.getValue());
                                    responseResult = userService.fetchUser(userDto);
                                }
                                if (responseResult.isSuccess() && pagination != null && CollectionUtils.isNotEmpty(pagination.getList())) {
                                    lastUserId = doSendRedPacket(pagination.getList(), sendRedPacketConfig, now.toDate());
                                }
                            } catch (Exception e) {
                                log.error("", e);
                            }
                        }
                        RedisCache.setEx(sendRedPacketKey, "1", 5, TimeUnit.MINUTES);//5分钟
                        return ResponseResult.buildSuccessResponse();
                    }
                }
            } else {
                return ResponseResult.buildFailResponse(REPEAT_EXECUTIVE.code(), REPEAT_EXECUTIVE.message());
            }

            return ResponseResult.buildFailResponse(ACTIVE_RED_PACKET_EXPIRED.code(), ACTIVE_RED_PACKET_EXPIRED.message());
        } catch (Exception e) {
            log.error("", e);
            return ResponseResult.buildFailResponse(ACTIVE_RED_PACKET_EXCEPTION.code(), ACTIVE_RED_PACKET_EXCEPTION.message());
        }
    }

    @Override
    public ResponseResult<String> batchSendRedPacket(@RequestBody BatchSendRedPacketParam sendRedPacketParam) {
        if(sendRedPacketParam.getSendAll() && CollectionUtils.isNotEmpty(sendRedPacketParam.getUserids())){
            return ResponseResult.buildFailResponse(SEND_RED_PACKET_CONFLICT.code(), SEND_RED_PACKET_CONFLICT.message());
        }
        if(!sendRedPacketParam.getSendAll() && CollectionUtils.isEmpty(sendRedPacketParam.getUserids())){
            return ResponseResult.buildFailResponse(SEND_RED_PACKET_CONFLICT.code(), SEND_RED_PACKET_CONFLICT.message());
        }

        if (sendRedPacketParam.getSendAll()){
            String msg = String.format("发放完毕，共发送%s个用户",0);
            // 全量发送
            Pagenation<UserDto> pagenation = commonService.fetchUsers(null,fetchSize);
            if (pagenation != null && CollectionUtils.isNotEmpty(pagenation.getList())){
                msg = String.format("发放完毕，共发送%s个用户",pagenation.getRowCount());
                fullSendRedpacket(sendRedPacketParam.getRedPackets(),pagenation.getList(),sendRedPacketParam.getSmsMsg());
            }
            return ResponseResult.buildSuccessResponse(msg);
        }else {
            String msg = String.format("发放完毕，共发送%s个用户",sendRedPacketParam.getUserids().size());
            // 指定人员发送
            List<Long> userids = sendRedPacketParam.getUserids();
            batchSendRedpacket(sendRedPacketParam.getRedPackets(),userids,sendRedPacketParam.getSmsMsg());
            return ResponseResult.buildSuccessResponse(msg);
        }
    }

    private Boolean batchSendRedpacket(List<SendRedPacketConfig> redPackets,List<Long> userids,String msg){
        List<UserRedPacket> userRedPacketList = Lists.newArrayList();
        Map<String, Object> sms = Maps.newHashMap();

        Map<Long,String> mobileMap = commonService.fetchUserMobile(userids);
        for (Long userId : userids) {
            String mobileNo = mobileMap.get(userId);
            if (StringUtils.isNotBlank(mobileNo)){
                sms.put(mobileMap.get(userId), msg);
            }
            if (CollectionUtils.isNotEmpty(redPackets)) {
                DateTime now = new DateTime();
                for (SendRedPacketConfig redpacket : redPackets) {
                    DateTime validDate = now.plusDays(redpacket.getValid());
                    Date expireTime = new DateTime(validDate.getYear(), validDate.getMonthOfYear(), validDate.getDayOfMonth(), 23, 59, 59).toDate();

                    List<BigDecimal> amounts = redpacket.getAmount();
                    for (BigDecimal amount : amounts){
                        UserRedPacket userRedPacket = new UserRedPacket();
                        userRedPacket.setId(IdGenerator.nextId());
                        userRedPacket.setAcitivtyId(redpacket.getAcitivtyId());
                        userRedPacket.setName(redpacket.getName());
                        userRedPacket.setType(redpacket.getType());
                        userRedPacket.setUserId(userId);
                        userRedPacket.setAmount(amount);
                        userRedPacket.setExpireTime(expireTime);
                        userRedPacket.setIsShare(redpacket.getIsShare());
                        userRedPacket.setIsValid((byte) 1);
                        userRedPacket.setIsUsed((byte) 0);
                        userRedPacket.setBusinessLine(BusinessLineEnum.LUNCH.getCode());
                        userRedPacket.setCreateTime(now.toDate());
                        userRedPacket.setUpdateTime(now.toDate());

                        userRedPacketList.add(userRedPacket);
                    }
                }
            }
        }
        int row = userRedPacketMapper.insertBatch(userRedPacketList);
        if (row > 0) {
            if (StringUtils.isNotBlank(msg)){
                //发送短信
                commonService.sendLunchSms(sms);
            }
        }
        return Boolean.TRUE;
    }


    private Boolean fullSendRedpacket(List<SendRedPacketConfig> redPackets, List<UserDto> userList, String msg){
        List<UserRedPacket> userRedPacketList = Lists.newArrayList();
        Map<String, Object> sms = Maps.newHashMap();

        for (UserDto user : userList) {
            sms.put(user.getMobileNo(), msg);
            if (CollectionUtils.isNotEmpty(redPackets)) {
                DateTime now = new DateTime();
                for (SendRedPacketConfig redpacket : redPackets) {
                    DateTime validDate = now.plusDays(redpacket.getValid());
                    Date expireTime = new DateTime(validDate.getYear(), validDate.getMonthOfYear(), validDate.getDayOfMonth(), 23, 59, 59).toDate();

                    List<BigDecimal> amounts = redpacket.getAmount();
                    for (BigDecimal amount : amounts){
                        UserRedPacket userRedPacket = new UserRedPacket();
                        userRedPacket.setId(IdGenerator.nextId());
                        userRedPacket.setAcitivtyId(redpacket.getAcitivtyId());
                        userRedPacket.setName(redpacket.getName());
                        userRedPacket.setType(redpacket.getType());
                        userRedPacket.setUserId(user.getId());
                        userRedPacket.setAmount(amount);
                        userRedPacket.setExpireTime(expireTime);
                        userRedPacket.setIsShare(redpacket.getIsShare());
                        userRedPacket.setIsValid((byte) 1);
                        userRedPacket.setIsUsed((byte) 0);
                        userRedPacket.setBusinessLine(BusinessLineEnum.LUNCH.getCode());
                        userRedPacket.setCreateTime(now.toDate());
                        userRedPacket.setUpdateTime(now.toDate());

                        userRedPacketList.add(userRedPacket);
                    }
                }
            }
        }
        int row = userRedPacketMapper.insertBatch(userRedPacketList);
        if (row > 0) {
            if (StringUtils.isNotBlank(msg)){
                //发送短信
                commonService.sendLunchSms(sms);
            }
        }
        if (userList.size() == fetchSize){
            Long stepUserid = userList.get(fetchSize-1).getId();
            Pagenation<UserDto> pagenation = commonService.fetchUsers(stepUserid,fetchSize);
            if (pagenation != null && CollectionUtils.isNotEmpty(pagenation.getList())){
                fullSendRedpacket(redPackets, pagenation.getList(), msg);
            }
        }
        return Boolean.TRUE;
    }



    @Override
    @PostMapping(value = "/v1/UserRedPacketService/toastRedPacketConfig")
    public ResponseResult<ToastRedPacketConfig> toastRedPacketConfig() {
        String configStr = RedisCache.get("red_packet_toast_config");
        log.info("red_packet_toast_config -> {}", configStr);
        if (StringUtils.isBlank(configStr)) {
            return ResponseResult.buildFailResponse(REDIS_DATA_NOT_EXIST.code(), REDIS_DATA_NOT_EXIST.message());
        }
        ToastRedPacketConfig config = JSONObject.parseObject(configStr, ToastRedPacketConfig.class);
        if (!config.getToastSwitch()) {
            return ResponseResult.buildFailResponse(SWITCH_OFF.code(), SWITCH_OFF.message());
        }
        return ResponseResult.buildSuccessResponse(config);
    }

    /**
     * 弹层展示的红包个数
     *
     * @param num
     * @return
     */
    @Override
    @PostMapping(value = "/v1/UserRedPacketService/toastUserRedPacket")
    public ResponseResult<List<UserRedPacketDto>> toastUserRedPacket(@RequestParam("userId") Long userId, @RequestParam("num") Integer num) {
        if (num == null) {
            num = 2;
        } else if (num > 5) {
            num = 5;
        }
        try {
            Date currentDate = new Date();
            List<UserRedPacketDto> userRedPacketList = userRedPacketMapper.toastUserRedPacket(userId, num, currentDate);
            return ResponseResult.buildSuccessResponse(userRedPacketList);
        } catch (Exception e) {
            log.error("", e);
            return ResponseResult.buildFailResponse();
        }
    }

    @Override
    @PostMapping(value = "/v1/UserRedPacketService/fullReduceConfig")
    public ResponseResult<FullReduce> fullReduceConfig() {
        try {
            String configStr = RedisCache.get("full_reduce_config");
            if (StringUtils.isBlank(configStr)) {
                return ResponseResult.buildFailResponse(REDIS_DATA_NOT_EXIST.code(), REDIS_DATA_NOT_EXIST.message());
            }
            FullReduce fullReduce = JSONObject.parseObject(configStr, FullReduce.class);
            DateTime now = DateTime.now();
            Interval interval = new Interval(new DateTime(fullReduce.getStartDate()), new DateTime(fullReduce.getEndDate()));
            if (interval.contains(now)) {
                return ResponseResult.buildSuccessResponse(fullReduce);
            }
            return ResponseResult.buildSuccessResponse(FULL_REDUCE_ACTIVITY_EXPIRED.code(), FULL_REDUCE_ACTIVITY_EXPIRED.message());
        } catch (Exception e) {
            log.error("", e);
            return ResponseResult.buildFailResponse(FULL_REDUCE_CONFIG_EXCEPTION.code(), FULL_REDUCE_CONFIG_EXCEPTION.message());
        }
    }

    private List<Long> redpacketLocks(List<UserRedPacketDto> userRedPacketList){
        try {
            List<Long> redpacketIds = Lists.transform(userRedPacketList, new Function<UserRedPacketDto, Long>() {
                @Nullable
                @Override
                public Long apply(@Nullable UserRedPacketDto input) {
                    return input.getId();
                }
            });
            log.info("红包锁请求，入参 redpacketIds -> {}",redpacketIds);
            ResponseResult<List<Long>> result = promotionLockerService.getLockerRedPacket(redpacketIds);
            log.info("红包锁请求，出参 result -> {}",JSONObject.toJSONString(result));
            if (result.isSuccess() && CollectionUtils.isNotEmpty(result.getData())){
                return result.getData();
            }
        } catch (Exception e) {
            log.error("",e);
        }
        return Lists.newArrayList();
    }



    private List<UserRedPacket> initUserRedPacket(SendRedPacketConfig sendRedPacketConfig, Long userId, Date now) {
        DateTime validDate = DateTime.now().plusDays(sendRedPacketConfig.getValid().intValue());
        Date expireTime = new DateTime(validDate.getYear(), validDate.getMonthOfYear(), validDate.getDayOfMonth(), 23, 59, 59).toDate();

        List<UserRedPacket> list = new ArrayList<>(sendRedPacketConfig.getAmount().size());
        for (BigDecimal amount : sendRedPacketConfig.getAmount()) {
            UserRedPacket userRedPacket = new UserRedPacket();
            userRedPacket.setId(IdGenerator.nextId());
            userRedPacket.setAcitivtyId(sendRedPacketConfig.getAcitivtyId());
            userRedPacket.setName(sendRedPacketConfig.getName());
            userRedPacket.setType(sendRedPacketConfig.getType());
            userRedPacket.setUserId(userId);
            userRedPacket.setAmount(amount);
            userRedPacket.setExpireTime(expireTime);
            userRedPacket.setIsShare(sendRedPacketConfig.getIsShare());
            userRedPacket.setIsValid((byte) 1);
            userRedPacket.setIsUsed((byte) 0);
            userRedPacket.setCreateTime(now);
            userRedPacket.setUpdateTime(now);

            list.add(userRedPacket);
        }
        return list;
    }

    // 发送红包
    private Long doSendRedPacket(List<UserDto> userDtos, SendRedPacketConfig sendRedPacketConfig, Date now) {
        List<UserRedPacket> list = Lists.newArrayList();
        Map<String, Object> sms = Maps.newHashMap();
        // 过滤不符合条件用户
        Set<Long> blackSet = filterWithCondition(userDtos, sendRedPacketConfig.getConditionAmount());

        if (CollectionUtils.isNotEmpty(userDtos)) {
            Long lastUserId = 0L;
            for (int i = 0; i < userDtos.size(); i++) {
                if (blackSet == null || !blackSet.contains(userDtos.get(i).getId())) {
                    if (StringUtils.isNotBlank(userDtos.get(i).getMobileNo())) {
                        sms.put(userDtos.get(i).getMobileNo(), sendRedPacketConfig.getSmsMsg());
                    }
                    list.addAll(initUserRedPacket(sendRedPacketConfig, userDtos.get(i).getId(), now));
                }

                if (i == userDtos.size() - 1) {
                    lastUserId = userDtos.get(i).getId();
                }
            }

            if (CollectionUtils.isNotEmpty(list)) {
                userRedPacketMapper.insertBatch(list);
                //发送短信
                commonService.sendLunchSms(sms);
            }
            return lastUserId;
        }
        return null;
    }

    // 过滤不符合条件的用户
    private Set<Long> filterWithCondition(List<UserDto> userDtos, BigDecimal conditionAmount) {
        List<Long> ids = Lists.transform(userDtos, new Function<UserDto, Long>() {
            @Nullable
            @Override
            public Long apply(@Nullable UserDto userDto) {
                return userDto.getId();
            }
        });
        return userRedPacketMapper.filterUserRedPacketWithAmount(ids, conditionAmount);
    }

}