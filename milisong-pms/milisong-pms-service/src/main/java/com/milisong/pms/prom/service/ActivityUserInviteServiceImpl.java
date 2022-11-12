package com.milisong.pms.prom.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.milisong.pms.prom.api.ActivityUserInviteService;
import com.milisong.pms.prom.api.ActivityUserRedPacketService;
import com.milisong.pms.prom.api.BreakfastCouponService;
import com.milisong.pms.prom.api.UserInviteRecordService;
import com.milisong.pms.prom.domain.ActivityUserInvite;
import com.milisong.pms.prom.domain.UserInviteRecord;
import com.milisong.pms.prom.dto.UserCouponDto;
import com.milisong.pms.prom.dto.UserCouponQueryParam;
import com.milisong.pms.prom.dto.UserRedPacketDto;
import com.milisong.pms.prom.dto.invite.*;
import com.milisong.pms.prom.enums.UserInviteEnum;
import com.milisong.pms.prom.mapper.ActivityUserInviteMapper;
import com.milisong.pms.prom.mapper.UserCouponMapper;
import com.milisong.pms.prom.mapper.UserInviteRecordMapper;
import com.milisong.pms.prom.util.GlobalConfigCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.milisong.pms.prom.enums.CouponEnum.TYPE.GOODS;
import static com.milisong.pms.prom.enums.PromotionResponseCode.*;
import static com.milisong.pms.prom.enums.UserActivityType.OLD_PULL_NEW;
import static com.milisong.pms.prom.enums.UserInviteEnum.INVITE_RECORD_STATUS.*;
import static com.milisong.pms.prom.enums.UserInviteEnum.INVITE_RECORD_STATUS.SUCCESS;
import static com.milisong.ucs.enums.BusinessLineEnum.BREAKFAST;

/**
 * @author sailor wang
 * @date 2019/3/25 5:10 PM
 * @description
 */
@Slf4j
@Service
@RestController
public class ActivityUserInviteServiceImpl implements ActivityUserInviteService {

    @Autowired
    ActivityUserInviteMapper activityUserInviteMapper;

    @Autowired
    UserInviteRecordService userInviteRecordService;

    @Autowired
    UserInviteRecordMapper userInviteRecordMapper;

    @Autowired
    UserCouponMapper userCouponMapper;

    @Autowired
    BreakfastCouponService breakfastCouponService;

    @Autowired
    ActivityUserRedPacketService activityUserRedPacketService;

    @Autowired
    CommonService commonService;


    /**
     * 创建老拉新邀请活动实例
     *
     * @param userInviteRequest
     * @return
     */
    @Override
    public ResponseResult<ActivityUserInviteDto> createInviteActivity(@RequestBody UserInviteRequest userInviteRequest) {
        if (userInviteRequest.getOriginateUserId() == null || userInviteRequest.getBusinessLine() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        //开始或结束时间校验
        Date endTime = GlobalConfigCacheUtil.getInviteEndTime();
        Date now = new Date();
        if (endTime == null || endTime.before(now)) {
            return ResponseResult.buildFailResponse(INVITE_HAS_OVER.code(), INVITE_HAS_OVER.message());
        }
        Date startTime = GlobalConfigCacheUtil.getInviteStartTime();
        if (startTime == null || startTime.after(now)) {
            return ResponseResult.buildFailResponse(INVITE_HAS_NOT_START.code(), INVITE_HAS_NOT_START.message());
        }
        //创建
        ActivityUserInvite activityUserInvite = insertInviteDto(userInviteRequest, endTime, startTime);
        return ResponseResult.buildSuccessResponse(BeanMapper.map(activityUserInvite, ActivityUserInviteDto.class));
    }

    /**
     * 查询老拉新活动实例通过用户id
     *
     * @param userId
     * @return
     */
    @Override
    public ResponseResult<UserInviteResponse> queryInviteByUserId(@RequestParam("userId") Long userId) {
        UserInviteResponse userInviteResponse = new UserInviteResponse();
        ActivityUserInvite activityUserInvite = activityUserInviteMapper.selectByOriginateUserId(userId, UserInviteEnum.INVITE_TYPE.OLD_PULL_NEW.getValue());
        if (activityUserInvite != null) {
            userInviteResponse.setUserInviteDto(BeanMapper.map(activityUserInvite, ActivityUserInviteDto.class));
        }
        return ResponseResult.buildSuccessResponse(userInviteResponse);
    }

    /**
     * 获取邀新配置
     *
     * @return
     */
    @Override
    public ResponseResult<InviteConfigResponse> getConfig() {
        return ResponseResult.buildSuccessResponse(BeanMapper.map(GlobalConfigCacheUtil.getConfig(), InviteConfigResponse.class));
    }

    /**
     * 查询我的邀请活动实例
     *
     * @param userInviteRequest
     * @return
     */
    @Override
    public ResponseResult<UserInviteResponse> queryMyInviteInfo(@RequestBody UserInviteRequest userInviteRequest) {
        if (userInviteRequest.getUserId() == null || userInviteRequest.getBusinessLine() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        commonService.countOldPullNewPV();
        UserInviteResponse userInviteResponse = new UserInviteResponse();
        ActivityUserInvite activityUserInvite = activityUserInviteMapper.selectByOriginateUserId(userInviteRequest.getUserId(), UserInviteEnum.INVITE_TYPE.OLD_PULL_NEW.getValue());
        if (activityUserInvite == null) {//未创建邀请活动实例
            userInviteResponse.setIsCreated(Boolean.FALSE);
            //配置邀请图片、文案等数据
            decorateInviteActivityConfig(userInviteResponse, Collections.emptyList());
            Date now = new Date();
            Boolean isOver = now.after(GlobalConfigCacheUtil.getInviteEndTime());
            userInviteResponse.setIsOver(isOver);//判断活动是否过期
            return ResponseResult.buildSuccessResponse(userInviteResponse);
        }
        // 是当前邀请活动发起者
        return fetchMyInviteRecord(activityUserInvite);
    }

    /**
     * 先查询是否已存在，没有则执行插入数据库动作，有则直接返回该信息
     *
     * @param userInviteRequest
     * @param endTime
     * @param startTime
     * @return
     */
    private ActivityUserInvite insertInviteDto(UserInviteRequest userInviteRequest, Date endTime, Date startTime) {
        RLock lock = LockProvider.getLock(userInviteRequest.getOriginateUserId().toString());
        ActivityUserInvite activityUserInvite;
        try {
            lock.lock(20, TimeUnit.SECONDS);
            //查询是否已存在
            activityUserInvite = activityUserInviteMapper.selectByOriginateUserId(userInviteRequest.getOriginateUserId(), UserInviteEnum.INVITE_TYPE.OLD_PULL_NEW.getValue());
            if (activityUserInvite == null) {
                //不存在则创建
                activityUserInvite = new ActivityUserInvite();
                activityUserInvite.setId(IdGenerator.nextId());
                activityUserInvite.setName(GlobalConfigCacheUtil.getInviteActivityName());
                activityUserInvite.setOriginateUserId(userInviteRequest.getOriginateUserId());
                activityUserInvite.setStartDate(startTime);
                activityUserInvite.setEndDate(endTime);
                activityUserInvite.setGoodsJson(GlobalConfigCacheUtil.getInviteGoodsInfo());
                activityUserInvite.setMaxCount(GlobalConfigCacheUtil.getInviteMaxCouponCount());
                activityUserInvite.setType(UserInviteEnum.INVITE_TYPE.OLD_PULL_NEW.getValue());
                activityUserInvite.setValidDays(GlobalConfigCacheUtil.getInviteSendCouponValidity());
                activityUserInvite.setCreateTime(new Date());
                activityUserInvite.setUpdateTime(new Date());
                activityUserInviteMapper.insertSelective(activityUserInvite);
            }
        } finally {
            lock.unlock();
        }
        return activityUserInvite;
    }

    /**
     * 查询老拉新活动实例
     *
     * @param userInviteRequest
     * @return
     */
    @Override
    @Transactional
    public ResponseResult<UserInviteResponse> queryInviteActivity(@RequestBody UserInviteRequest userInviteRequest) {
        if (userInviteRequest.getUserId() == null || userInviteRequest.getBusinessLine() == null || userInviteRequest.getOriginateUserId() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        ActivityUserInvite activityUserInvite = activityUserInviteMapper.selectByOriginateUserId(userInviteRequest.getOriginateUserId(), UserInviteEnum.INVITE_TYPE.OLD_PULL_NEW.getValue());

        if (activityUserInvite == null) {
            // 补偿创建老拉新活动实例
            ResponseResult<ActivityUserInviteDto> responseResult = createInviteActivity(userInviteRequest);
            if (!responseResult.isSuccess() || responseResult.getData() == null) {
                log.info("老拉新活动实例不存在");
                return ResponseResult.buildFailResponse(INVITE_ACTIVITY_NOT_EXIST.code(), INVITE_ACTIVITY_NOT_EXIST.message());
            }
            activityUserInvite = activityUserInviteMapper.selectByOriginateUserId(userInviteRequest.getOriginateUserId(), UserInviteEnum.INVITE_TYPE.OLD_PULL_NEW.getValue());
        }

        if (userInviteRequest.getUserId().equals(activityUserInvite.getOriginateUserId())) {
            // 是当前邀请活动发起者
            return fetchMyInviteRecord(activityUserInvite);
        } else {
            // 不是当前邀请活动发起者
            return fetchOtherInviteRecord(activityUserInvite, userInviteRequest);
        }
    }

    /**
     * 获取他人的邀请记录数据
     *
     * @param activityUserInvite
     * @param userInviteRequest
     * @return
     */
    private ResponseResult<UserInviteResponse> fetchOtherInviteRecord(ActivityUserInvite activityUserInvite, UserInviteRequest userInviteRequest) {
        log.info("获取发起者的邀请记录数据");
        UserInviteResponse userInviteResponse = new UserInviteResponse();
        decorateInviteActivityConfig(userInviteResponse, Collections.EMPTY_LIST);

        //有限判断用户是否用户昵称授权
        if (StringUtils.isEmpty(userInviteRequest.getHeadPortraitUrl())) {//未授权
            log.info("用户昵称头像未授权");
            return ResponseResult.buildFailResponse(USER_NOT_AUTHORIZATION.code(), USER_NOT_AUTHORIZATION.message());
        }
        Date now = new Date();

        Boolean isOver = now.after(activityUserInvite.getEndDate());//活动状态

        userInviteResponse.setIsMy(Boolean.FALSE);
        userInviteResponse.setIsOver(isOver);//活动是否结束

        if (isOver) {//活动已结束
            log.info("老拉新邀请活动以结束");
            UserCouponDto receiveCoupon = queryUserCouponByActivity(activityUserInvite.getId(), userInviteRequest.getUserId());
            userInviteResponse.setReceiveCoupon(receiveCoupon);//查询我领取的券
            userInviteResponse.setReceived(receiveCoupon != null);
            return ResponseResult.buildSuccessResponse(userInviteResponse);
        }

        //是否是新用户
        Byte isNew = userInviteRequest.getIsNew();
        //是否领取过新人券
        Byte receivedNewCoupon = userInviteRequest.getReceivedNewCoupon();
        //是否领取过新人红包
        Byte receivedNewRedpacket = userInviteRequest.getReceivedNewRedpacket();

        if (isNew != null && isNew.intValue() == 0) {// 新用户
            // 红包是否领取成功
            Boolean receivedFlag = receivedNewRedpacket(userInviteRequest.getUserId(),receivedNewRedpacket);
            userInviteResponse.setReceiveNewRedpacketFlag(receivedFlag);

            if (receivedNewCoupon != null && receivedNewCoupon.intValue() == 1) {//已领取过新人券
                log.info("新用户，已领取过新人券！");
                UserCouponDto receiveCoupon = queryUserCouponByActivity(activityUserInvite.getId(), userInviteRequest.getUserId());
                userInviteResponse.setReceiveCoupon(receiveCoupon);//查询我领取的券
                userInviteResponse.setReceived(receiveCoupon != null);
                return ResponseResult.buildSuccessResponse(userInviteResponse);
            }
            log.info("新用户，未领取过新人券！");
            //未领取过，助力该实例
            doInviteRecords(activityUserInvite, userInviteRequest);

            //领取新人券
            Boolean receiveNewCouponSuccess = receiveNewCoupon(userInviteRequest.getUserId(), activityUserInvite.getId(), OLD_PULL_NEW.getCode());
            userInviteResponse.setReceiveNewCouponFlag(receiveNewCouponSuccess);

            UserCouponDto receiveCoupon = queryUserCouponByActivity(activityUserInvite.getId(), userInviteRequest.getUserId());
            userInviteResponse.setReceiveCoupon(receiveCoupon);//查询我领取的券

            //第一次领取，表示：未领取过
            userInviteResponse.setReceived(Boolean.FALSE);
        } else {
            UserCouponDto receiveCoupon = queryUserCouponByActivity(activityUserInvite.getId(), userInviteRequest.getUserId());
            userInviteResponse.setReceiveCoupon(receiveCoupon);//查询我领取的券
            userInviteResponse.setReceived(receiveCoupon != null);
        }

        return ResponseResult.buildSuccessResponse(userInviteResponse);
    }

    /**
     * 领取红包
     *
     * @param receivedNewRedpacket
     */
    private Boolean receivedNewRedpacket(Long userId, Byte receivedNewRedpacket){
        if (receivedNewRedpacket == null || receivedNewRedpacket.intValue() == 0){
            ResponseResult<UserRedPacketDto> result = activityUserRedPacketService.receiveNewRedPacket(userId,null);
            return result.isSuccess() && result.getData() != null;
        }
        return Boolean.TRUE;
    }

    private void doInviteRecords(ActivityUserInvite activityUserInvite, UserInviteRequest userInviteRequest) {
        RLock rLock = LockProvider.getLock("user_inivte_record:" + userInviteRequest.getUserId());
        try {
            rLock.lock(5, TimeUnit.SECONDS);
            UserInviteRecord userInviteRecord = userInviteRecordMapper.queryByReceiveUserId(userInviteRequest.getUserId());
            if (userInviteRecord != null) {
                return;
            }
            log.info("生成邀请记录");
            UserInviteRecord inviteRecord = new UserInviteRecord();
            inviteRecord.setId(IdGenerator.nextId());
            inviteRecord.setActivityInviteId(activityUserInvite.getId());
            inviteRecord.setOriginateUserId(activityUserInvite.getOriginateUserId());
            inviteRecord.setReceiveUserId(userInviteRequest.getUserId());
            inviteRecord.setNickName(userInviteRequest.getNickName());
            inviteRecord.setHeadPortraitUrl(userInviteRequest.getHeadPortraitUrl());

            JSONObject goods = JSONObject.parseObject(GlobalConfigCacheUtil.getInviteGoodsInfo());
            inviteRecord.setCouponName(goods.getString("couponName"));
            inviteRecord.setGoodsCode(goods.getString("goodsCode"));
            inviteRecord.setDiscount(goods.getBigDecimal("discount"));
            inviteRecord.setStatus(NOT_PLACE_ORDER.getValue());
            inviteRecord.setRemark(NOT_PLACE_ORDER.getDesc());
            inviteRecord.setCreateTime(new Date());
            inviteRecord.setUpdateTime(new Date());
            userInviteRecordMapper.insertSelective(inviteRecord);
        } catch (Exception ex) {
            log.error("", ex);
            throw ex;
        } finally {
            rLock.unlock();
        }
    }

    private UserCouponDto queryUserCouponByActivity(Long activityUserInviteId, Long userId) {
        List<UserCouponDto> userCouponList = userCouponMapper.queryUserCouponByActivity(activityUserInviteId, OLD_PULL_NEW.getCode(), userId, null);
        if (!CollectionUtils.isEmpty(userCouponList)){
            List<UserCouponDto> goodsCouponList = userCouponList.stream().filter(new Predicate<UserCouponDto>() {
                @Override
                public boolean test(UserCouponDto userCouponDto) {
                    return GOODS.getCode().equals(userCouponDto.getType());
                }
            }).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(goodsCouponList)){
                return goodsCouponList.get(0);
            }
            return userCouponList.get(0);
        }
        return null;
    }

    /**
     * 获取我的邀请记录数据
     *
     * @param activityUserInvite
     * @return
     */
    private ResponseResult<UserInviteResponse> fetchMyInviteRecord(ActivityUserInvite activityUserInvite) {
        log.info("查询我的邀请记录");
        Date now = new Date();
        Boolean isOver = now.after(activityUserInvite.getEndDate());
        UserInviteResponse userInviteResponse = new UserInviteResponse();
        userInviteResponse.setIsMy(Boolean.TRUE);
        userInviteResponse.setIsCreated(Boolean.TRUE);
        userInviteResponse.setIsOver(isOver);//活动状态
        ResponseResult<List<UserInviteRecordDto>> responseResult = userInviteRecordService.queryInviteRecords(activityUserInvite.getOriginateUserId());
        List<UserInviteRecordDto> inviteRecordList = Collections.EMPTY_LIST;
        if (responseResult.isSuccess() && !CollectionUtils.isEmpty(responseResult.getData())) {
            inviteRecordList = responseResult.getData();
            userInviteResponse.setInviteRecordList(inviteRecordList);
        }
        //配置邀请图片、文案等数据
        decorateInviteActivityConfig(userInviteResponse, inviteRecordList);
        return ResponseResult.buildSuccessResponse(userInviteResponse);
    }

    /**
     * 领取新人券
     *
     * @param userId
     * @param userActivityId
     * @param userActivityType
     */
    private Boolean receiveNewCoupon(Long userId, Long userActivityId, Byte userActivityType) {
        UserCouponQueryParam queryParam = new UserCouponQueryParam();
        queryParam.setUserId(userId);
        queryParam.setBusinessLine(BREAKFAST.getCode());
        queryParam.setUserActivityId(userActivityId);
        queryParam.setUserActivityType(userActivityType);
        ResponseResult<UserCouponDto> responseResult = breakfastCouponService.receiveNewCoupon(queryParam);
        return responseResult.getData() != null;
    }

    private void decorateInviteActivityConfig(UserInviteResponse userInviteResponse, List<UserInviteRecordDto> inviteRecordList) {
        InviteConfigDto config = GlobalConfigCacheUtil.getConfig();
        //我的邀请详情页展示图
        userInviteResponse.setMyInvitePic(config.getMyInvitePic());
        Long pv = commonService.getOldPullNewPV();
        log.info("pv -> {}, desc -> {}",pv,config.getMyInviteDesc());
        //已有 XXXX 人次参与邀新活动！
        userInviteResponse.setMyInviteDesc(String.format(config.getMyInviteDesc(),pv));
        //新人点击转发连接进入 展示图
        userInviteResponse.setNewbieDisplayPic(config.getNewbieDisplayPic());
        //邀请规则
        userInviteResponse.setInviteRule(config.getInviteRule());
        //小程序转发图
        userInviteResponse.setInviteTransmitPic(config.getInviteTransmitPic());
        //小程序转发文案
        userInviteResponse.setInviteTransmitText(config.getInviteTransmitText());
        //新用户受邀，领券成功，券使用描述
        userInviteResponse.setInviteNewbieCouponUseDesc(config.getInviteNewbieCouponUseDesc());
        //老带新弹层图片
        userInviteResponse.setInviteToastPic(config.getInviteToastPic());
        //首次分享标题
        userInviteResponse.setInviteFirstShareTitle(config.getInviteFirstShareTitle());
        //首次分享文案
        userInviteResponse.setInviteFirstShareDesc(config.getInviteFirstShareDesc());
        //二次分享标题
        userInviteResponse.setInviteSecondShareTitle(config.getInviteSecondShareTitle());
        //二次分享文案
        userInviteResponse.setInviteSecondShareDesc(config.getInviteSecondShareDesc());
        //提醒用户文案
        userInviteResponse.setInviteRemarkWord(config.getInviteRemarkWord());

        if (CollectionUtils.isEmpty(inviteRecordList)) {
            userInviteResponse.setInviteDataDesc(String.format(config.getInviteDataDesc(), 0, 0));
        } else {
            Integer inviteSuccessCount = 0;//邀请成功数量
            Integer sendCouponSuccessCount = 0;//送券成功数量
            //转换类型，排序，用于显示,将0,1在C端展示为未成功，2,3在C端展示为成功
            for (UserInviteRecordDto inviteRecord : inviteRecordList) {
                if (inviteRecord.getStatus().equals(SUCCESS.getValue())) {
                    sendCouponSuccessCount++;
                    inviteSuccessCount++;
                    inviteRecord.setStatus(C_SUCCESS.getValue());
                } else if (inviteRecord.getStatus().equals(FAILD.getValue())) {
                    inviteSuccessCount++;
                    inviteRecord.setStatus(C_SUCCESS.getValue());
                } else if (inviteRecord.getStatus().equals(ON_DELIVERY.getValue())) {
                    inviteSuccessCount++;
                    inviteRecord.setStatus(C_ON_DELIVERY.getValue());
                } else {
                    inviteRecord.setStatus(C_FAILD.getValue());
                }
            }
            userInviteResponse.setInviteDataDesc(String.format(config.getInviteDataDesc(), inviteSuccessCount, sendCouponSuccessCount));
        }
    }


}