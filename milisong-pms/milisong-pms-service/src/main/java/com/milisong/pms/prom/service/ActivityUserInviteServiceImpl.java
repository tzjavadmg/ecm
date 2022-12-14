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
     * ?????????????????????????????????
     *
     * @param userInviteRequest
     * @return
     */
    @Override
    public ResponseResult<ActivityUserInviteDto> createInviteActivity(@RequestBody UserInviteRequest userInviteRequest) {
        if (userInviteRequest.getOriginateUserId() == null || userInviteRequest.getBusinessLine() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        //???????????????????????????
        Date endTime = GlobalConfigCacheUtil.getInviteEndTime();
        Date now = new Date();
        if (endTime == null || endTime.before(now)) {
            return ResponseResult.buildFailResponse(INVITE_HAS_OVER.code(), INVITE_HAS_OVER.message());
        }
        Date startTime = GlobalConfigCacheUtil.getInviteStartTime();
        if (startTime == null || startTime.after(now)) {
            return ResponseResult.buildFailResponse(INVITE_HAS_NOT_START.code(), INVITE_HAS_NOT_START.message());
        }
        //??????
        ActivityUserInvite activityUserInvite = insertInviteDto(userInviteRequest, endTime, startTime);
        return ResponseResult.buildSuccessResponse(BeanMapper.map(activityUserInvite, ActivityUserInviteDto.class));
    }

    /**
     * ???????????????????????????????????????id
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
     * ??????????????????
     *
     * @return
     */
    @Override
    public ResponseResult<InviteConfigResponse> getConfig() {
        return ResponseResult.buildSuccessResponse(BeanMapper.map(GlobalConfigCacheUtil.getConfig(), InviteConfigResponse.class));
    }

    /**
     * ??????????????????????????????
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
        if (activityUserInvite == null) {//???????????????????????????
            userInviteResponse.setIsCreated(Boolean.FALSE);
            //????????????????????????????????????
            decorateInviteActivityConfig(userInviteResponse, Collections.emptyList());
            Date now = new Date();
            Boolean isOver = now.after(GlobalConfigCacheUtil.getInviteEndTime());
            userInviteResponse.setIsOver(isOver);//????????????????????????
            return ResponseResult.buildSuccessResponse(userInviteResponse);
        }
        // ??????????????????????????????
        return fetchMyInviteRecord(activityUserInvite);
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????????????????
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
            //?????????????????????
            activityUserInvite = activityUserInviteMapper.selectByOriginateUserId(userInviteRequest.getOriginateUserId(), UserInviteEnum.INVITE_TYPE.OLD_PULL_NEW.getValue());
            if (activityUserInvite == null) {
                //??????????????????
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
     * ???????????????????????????
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
            // ?????????????????????????????????
            ResponseResult<ActivityUserInviteDto> responseResult = createInviteActivity(userInviteRequest);
            if (!responseResult.isSuccess() || responseResult.getData() == null) {
                log.info("??????????????????????????????");
                return ResponseResult.buildFailResponse(INVITE_ACTIVITY_NOT_EXIST.code(), INVITE_ACTIVITY_NOT_EXIST.message());
            }
            activityUserInvite = activityUserInviteMapper.selectByOriginateUserId(userInviteRequest.getOriginateUserId(), UserInviteEnum.INVITE_TYPE.OLD_PULL_NEW.getValue());
        }

        if (userInviteRequest.getUserId().equals(activityUserInvite.getOriginateUserId())) {
            // ??????????????????????????????
            return fetchMyInviteRecord(activityUserInvite);
        } else {
            // ?????????????????????????????????
            return fetchOtherInviteRecord(activityUserInvite, userInviteRequest);
        }
    }

    /**
     * ?????????????????????????????????
     *
     * @param activityUserInvite
     * @param userInviteRequest
     * @return
     */
    private ResponseResult<UserInviteResponse> fetchOtherInviteRecord(ActivityUserInvite activityUserInvite, UserInviteRequest userInviteRequest) {
        log.info("????????????????????????????????????");
        UserInviteResponse userInviteResponse = new UserInviteResponse();
        decorateInviteActivityConfig(userInviteResponse, Collections.EMPTY_LIST);

        //??????????????????????????????????????????
        if (StringUtils.isEmpty(userInviteRequest.getHeadPortraitUrl())) {//?????????
            log.info("???????????????????????????");
            return ResponseResult.buildFailResponse(USER_NOT_AUTHORIZATION.code(), USER_NOT_AUTHORIZATION.message());
        }
        Date now = new Date();

        Boolean isOver = now.after(activityUserInvite.getEndDate());//????????????

        userInviteResponse.setIsMy(Boolean.FALSE);
        userInviteResponse.setIsOver(isOver);//??????????????????

        if (isOver) {//???????????????
            log.info("??????????????????????????????");
            UserCouponDto receiveCoupon = queryUserCouponByActivity(activityUserInvite.getId(), userInviteRequest.getUserId());
            userInviteResponse.setReceiveCoupon(receiveCoupon);//?????????????????????
            userInviteResponse.setReceived(receiveCoupon != null);
            return ResponseResult.buildSuccessResponse(userInviteResponse);
        }

        //??????????????????
        Byte isNew = userInviteRequest.getIsNew();
        //????????????????????????
        Byte receivedNewCoupon = userInviteRequest.getReceivedNewCoupon();
        //???????????????????????????
        Byte receivedNewRedpacket = userInviteRequest.getReceivedNewRedpacket();

        if (isNew != null && isNew.intValue() == 0) {// ?????????
            // ????????????????????????
            Boolean receivedFlag = receivedNewRedpacket(userInviteRequest.getUserId(),receivedNewRedpacket);
            userInviteResponse.setReceiveNewRedpacketFlag(receivedFlag);

            if (receivedNewCoupon != null && receivedNewCoupon.intValue() == 1) {//?????????????????????
                log.info("????????????????????????????????????");
                UserCouponDto receiveCoupon = queryUserCouponByActivity(activityUserInvite.getId(), userInviteRequest.getUserId());
                userInviteResponse.setReceiveCoupon(receiveCoupon);//?????????????????????
                userInviteResponse.setReceived(receiveCoupon != null);
                return ResponseResult.buildSuccessResponse(userInviteResponse);
            }
            log.info("????????????????????????????????????");
            //??????????????????????????????
            doInviteRecords(activityUserInvite, userInviteRequest);

            //???????????????
            Boolean receiveNewCouponSuccess = receiveNewCoupon(userInviteRequest.getUserId(), activityUserInvite.getId(), OLD_PULL_NEW.getCode());
            userInviteResponse.setReceiveNewCouponFlag(receiveNewCouponSuccess);

            UserCouponDto receiveCoupon = queryUserCouponByActivity(activityUserInvite.getId(), userInviteRequest.getUserId());
            userInviteResponse.setReceiveCoupon(receiveCoupon);//?????????????????????

            //???????????????????????????????????????
            userInviteResponse.setReceived(Boolean.FALSE);
        } else {
            UserCouponDto receiveCoupon = queryUserCouponByActivity(activityUserInvite.getId(), userInviteRequest.getUserId());
            userInviteResponse.setReceiveCoupon(receiveCoupon);//?????????????????????
            userInviteResponse.setReceived(receiveCoupon != null);
        }

        return ResponseResult.buildSuccessResponse(userInviteResponse);
    }

    /**
     * ????????????
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
            log.info("??????????????????");
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
     * ??????????????????????????????
     *
     * @param activityUserInvite
     * @return
     */
    private ResponseResult<UserInviteResponse> fetchMyInviteRecord(ActivityUserInvite activityUserInvite) {
        log.info("????????????????????????");
        Date now = new Date();
        Boolean isOver = now.after(activityUserInvite.getEndDate());
        UserInviteResponse userInviteResponse = new UserInviteResponse();
        userInviteResponse.setIsMy(Boolean.TRUE);
        userInviteResponse.setIsCreated(Boolean.TRUE);
        userInviteResponse.setIsOver(isOver);//????????????
        ResponseResult<List<UserInviteRecordDto>> responseResult = userInviteRecordService.queryInviteRecords(activityUserInvite.getOriginateUserId());
        List<UserInviteRecordDto> inviteRecordList = Collections.EMPTY_LIST;
        if (responseResult.isSuccess() && !CollectionUtils.isEmpty(responseResult.getData())) {
            inviteRecordList = responseResult.getData();
            userInviteResponse.setInviteRecordList(inviteRecordList);
        }
        //????????????????????????????????????
        decorateInviteActivityConfig(userInviteResponse, inviteRecordList);
        return ResponseResult.buildSuccessResponse(userInviteResponse);
    }

    /**
     * ???????????????
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
        //??????????????????????????????
        userInviteResponse.setMyInvitePic(config.getMyInvitePic());
        Long pv = commonService.getOldPullNewPV();
        log.info("pv -> {}, desc -> {}",pv,config.getMyInviteDesc());
        //?????? XXXX ???????????????????????????
        userInviteResponse.setMyInviteDesc(String.format(config.getMyInviteDesc(),pv));
        //?????????????????????????????? ?????????
        userInviteResponse.setNewbieDisplayPic(config.getNewbieDisplayPic());
        //????????????
        userInviteResponse.setInviteRule(config.getInviteRule());
        //??????????????????
        userInviteResponse.setInviteTransmitPic(config.getInviteTransmitPic());
        //?????????????????????
        userInviteResponse.setInviteTransmitText(config.getInviteTransmitText());
        //????????????????????????????????????????????????
        userInviteResponse.setInviteNewbieCouponUseDesc(config.getInviteNewbieCouponUseDesc());
        //?????????????????????
        userInviteResponse.setInviteToastPic(config.getInviteToastPic());
        //??????????????????
        userInviteResponse.setInviteFirstShareTitle(config.getInviteFirstShareTitle());
        //??????????????????
        userInviteResponse.setInviteFirstShareDesc(config.getInviteFirstShareDesc());
        //??????????????????
        userInviteResponse.setInviteSecondShareTitle(config.getInviteSecondShareTitle());
        //??????????????????
        userInviteResponse.setInviteSecondShareDesc(config.getInviteSecondShareDesc());
        //??????????????????
        userInviteResponse.setInviteRemarkWord(config.getInviteRemarkWord());

        if (CollectionUtils.isEmpty(inviteRecordList)) {
            userInviteResponse.setInviteDataDesc(String.format(config.getInviteDataDesc(), 0, 0));
        } else {
            Integer inviteSuccessCount = 0;//??????????????????
            Integer sendCouponSuccessCount = 0;//??????????????????
            //????????????????????????????????????,???0,1???C????????????????????????2,3???C??????????????????
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