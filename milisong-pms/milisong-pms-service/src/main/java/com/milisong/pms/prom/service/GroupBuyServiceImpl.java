package com.milisong.pms.prom.service;

import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.milisong.pms.prom.api.BreakfastCouponService;
import com.milisong.pms.prom.api.GroupBuyService;
import com.milisong.pms.prom.domain.ActivityGroupBuy;
import com.milisong.pms.prom.domain.UserGroupBuy;
import com.milisong.pms.prom.domain.UserGroupBuyRecord;
import com.milisong.pms.prom.dto.DelayQueueMessage;
import com.milisong.pms.prom.dto.UserCouponRequest;
import com.milisong.pms.prom.dto.groupbuy.*;
import com.milisong.pms.prom.enums.GrouyBuyEnum;
import com.milisong.pms.prom.mapper.ActivityGroupBuyMapper;
import com.milisong.pms.prom.mapper.UserGroupBuyMapper;
import com.milisong.pms.prom.mapper.UserGroupBuyRecordMapper;
import com.milisong.pms.prom.mq.GroupBuyProducer;
import com.milisong.pms.prom.util.GroupBuyDelayQueueUtil;
import com.milisong.pms.prom.util.GroupBuyUtil;
import com.milisong.ucs.enums.BusinessLineEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.milisong.pms.prom.enums.GrouyBuyEnum.GROUP_BUY_LOCK_STATUS.LOCK;
import static com.milisong.pms.prom.enums.GrouyBuyEnum.GROUP_BUY_STATUS.CREATE_UNPAY;
import static com.milisong.pms.prom.enums.GrouyBuyEnum.SEND_MSG_SCENE.LEFT_TIME_NOTIFY;
import static com.milisong.pms.prom.enums.GrouyBuyEnum.SEND_MSG_SCENE.TIMEOUT_NOTIFY;
import static com.milisong.pms.prom.enums.GrouyBuyEnum.USER_GROUP_BUY_STATUS.JOINED_PAY;
import static com.milisong.pms.prom.enums.GrouyBuyEnum.USER_GROUP_BUY_STATUS.JOINED_UNPAY;
import static com.milisong.pms.prom.enums.PromotionResponseCode.*;
import static com.milisong.pms.prom.enums.RedPacketLaunchType.COMPLETE_GROUPBUY_ORDER_SHARE;

/**
 * ????????????
 *
 * @author sailor wang
 * @date 2019/5/17 3:28 PM
 * @description
 */
@Slf4j
@RestController
public class GroupBuyServiceImpl implements GroupBuyService {

    @Autowired
    private UserGroupBuyRecordMapper userGroupBuyRecordMapper;

    @Autowired
    private ActivityGroupBuyMapper activityGroupBuyMapper;

    @Autowired
    private UserGroupBuyMapper userGroupBuyMapper;

    @Autowired
    private GroupBuyDelayQueueUtil groupBuyDelayQueue;

    @Autowired
    private GroupBuyProducer groupBuyProducer;

    @Autowired
    private BreakfastCouponService breakfastCouponService;

    /**
     * ??????????????????
     *
     * @param groupBuyRequest
     * @return
     */
    @Override
    public ResponseResult<GroupBuyEntryResponse> groupBuyEntry(@RequestBody GroupBuyRequest groupBuyRequest) {
        if (groupBuyRequest.getUserId() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }

        ActivityGroupBuyDto activityGroupBuy = queryActiveGroupBuyDetail();
        if (activityGroupBuy != null && activityGroupBuy.getId() != null) {
            //????????????????????????
            UserGroupBuyDto userGroupBuy = queryJoinedGroupBuy(activityGroupBuy.getId(), groupBuyRequest.getUserId(),new Date());
            Integer expireTime = null;
            Integer leftNum = null;
            if (userGroupBuy != null){
                expireTime = userGroupBuy.getExpireTime();
                //?????????????????????
                List<JoinUser> joinUserList = fetchGroupBuyJoinedUsers(userGroupBuy.getId());
                leftNum = userGroupBuy.getMinJoinNum() - joinUserList.size();
            }
            return ResponseResult.buildSuccessResponse(GroupBuyEntryResponse.builder().joined(leftNum!=null).userGroupBuyExpireTime(expireTime).leftNum(leftNum).groupBuyDetail(activityGroupBuy).build());
        }
        log.info("groupBuyEntry ?????????????????????");
        return ResponseResult.buildSuccessResponse(ACTIVITY_GROUP_BUY_NOT_EXIST.code(), ACTIVITY_GROUP_BUY_NOT_EXIST.message());
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    @Override
    public ResponseResult<GroupBuyResponse> queryOtherJoinedGroupBuy(@RequestBody GroupBuyRequest groupBuyRequest) {
        if (groupBuyRequest.getUserGroupBuyId() == null || groupBuyRequest.getUserId() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        Long userId = groupBuyRequest.getUserId();
        Long userGroupBuyId = groupBuyRequest.getUserGroupBuyId();
        GroupBuyResponse response = GroupBuyResponse.builder().build();

        Long orderId = groupBuyRequest.getOrderId();
        if (orderId != null){
            //??????or????????????????????????????????????count_down_latch ??????3???
            GroupBuyUtil.waitBeforePayCallback(orderId);
        }

        UserGroupBuyDto userGroupBuyDto = userGroupBuyMapper.queryUserGroupBuyInfo(userGroupBuyId);
        if (userGroupBuyDto == null){
            return ResponseResult.buildFailResponse(ACTIVITY_GROUP_BUY_NOT_EXIST.code(),ACTIVITY_GROUP_BUY_NOT_EXIST.message());
        }

        //1.??????????????????
        ActivityGroupBuy activityGroupBuy = getActivityGroupBuy(userGroupBuyDto.getActivityGroupBuyId());
        if (activityGroupBuy==null || activityGroupBuy.getId() == null){
            return ResponseResult.buildFailResponse(ACTIVITY_GROUP_BUY_NOT_EXIST.code(),ACTIVITY_GROUP_BUY_NOT_EXIST.message());
        }

        Date now = new Date();

        //2.????????????????????????
        Boolean isRunning = now.after(activityGroupBuy.getStartDate()) && now.before(activityGroupBuy.getEndDate());
        response.setActivityGroupBuyisOver(!isRunning);
        response.setActivityGroupBuyId(activityGroupBuy.getId());

        //3.?????????????????????
        List<JoinUser> joinUserList = fetchGroupBuyJoinedUsers(userGroupBuyDto.getId());
        Integer leftNum = userGroupBuyDto.getMinJoinNum() - joinUserList.size();
        //??????????????????
        response.setJoinUserList(joinUserList);
        //????????????
        response.setLeftNum(leftNum <= 0 ? 0 : leftNum);
        //??????id
        response.setCompanyId(userGroupBuyDto.getCompanyId());

        //4.??????????????????
        UserGroupBuyRecord userGroupBuyRecord = userGroupBuyRecordMapper.hasJoinedThisGroup(userGroupBuyId,userId);
        userGroupBuyDto.setOrderId(userGroupBuyRecord != null?userGroupBuyRecord.getOrderId():null);
        response.setJoined(userGroupBuyRecord != null);
        response.setUserGroupBuyDetail(userGroupBuyDto);

        return ResponseResult.buildSuccessResponse(response);
    }

    /**
     * ??????????????????
     *
     * @param userGroupBuyId
     * @return
     */
    public List<JoinUser> fetchGroupBuyJoinedUsers(Long userGroupBuyId) {
        List<JoinUser> joinUserList = GroupBuyUtil.fetchGroupBuyJoinedUsers(userGroupBuyId);
        if (CollectionUtils.isNotEmpty(joinUserList)) {
            return joinUserList;
        }
        joinUserList = userGroupBuyRecordMapper.fetchGroupBuyJoinedUsers(userGroupBuyId);
        if (CollectionUtils.isNotEmpty(joinUserList)) {
            GroupBuyUtil.cacheGroupBuyJoinedUsers(userGroupBuyId, joinUserList, 24 * 60 * 60, TimeUnit.SECONDS);
            return joinUserList;
        }
        return Lists.newArrayList();
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public ResponseResult<GroupBuyResponse> createGroupBuyForPay(@RequestBody GroupBuyRequest request) {
        log.info("???????????????????????????????????? request -> {}", request);
        if (request.getActivityGroupBuyId() == null || request.getUserId() == null || request.getOrderId() == null
               || StringUtils.isBlank(request.getOpenId()) || request.getCompanyId() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        Long userId = request.getUserId();
        Long activityGroupBuyId = request.getActivityGroupBuyId();

        Date now = new Date();
        //1.????????????????????????
        UserGroupBuyDto userGroupBuyDto = queryJoinedGroupBuy(activityGroupBuyId, userId,now);
        if (userGroupBuyDto != null) {
            //???????????????
            return ResponseResult.buildFailResponse(HAS_JOINED_ACTIVITY_GROUP_BUY.code(), HAS_JOINED_ACTIVITY_GROUP_BUY.message());
        }

        //2.??????????????????????????????
        ActivityGroupBuy activityGroupBuy = getActivityGroupBuy(activityGroupBuyId);
        if (activityGroupBuy == null || activityGroupBuy.getId() == null){
            return ResponseResult.buildFailResponse(ACTIVITY_GROUP_BUY_NOT_EXIST.code(),ACTIVITY_GROUP_BUY_NOT_EXIST.message());
        }

        Boolean isRunning = now.after(activityGroupBuy.getStartDate()) && now.before(activityGroupBuy.getEndDate());
        if (!isRunning) {//???????????????
            log.info("???????????????????????????????????????????????????id -> {}", activityGroupBuyId);
            return ResponseResult.buildFailResponse(ACTIVITY_GROUP_BUY_OVER.code(),ACTIVITY_GROUP_BUY_OVER.message());
        }


        Long userGroupBuyRecordId = IdGenerator.nextId();
        //3.??????????????????
        UserGroupBuy userGroupBuy = doCreateGroupBuy(activityGroupBuy, request, userGroupBuyRecordId);
        if (userGroupBuy != null) {//????????????
            //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            GroupBuyUtil.lockBeforePayCallback(request.getOrderId());
            return ResponseResult.buildSuccessResponse(GroupBuyResponse.builder().userGroupBuyId(userGroupBuy.getId()).userGroupBuyRecordId(userGroupBuyRecordId).companyId(request.getCompanyId()).build());
        }
        return ResponseResult.buildFailResponse(CREATE_GROUP_BUY_FAILED.code(), CREATE_GROUP_BUY_FAILED.message());
    }

    /**
     * ???????????????????????????????????????????????????????????????
     */
    private void registerCanelGroupBuyDelayQueue(Long userGroupBuyId,Date endDate){
        try {
            //????????????????????????????????????
            long seconds = Seconds.secondsBetween(DateTime.now(),new DateTime(endDate)).getSeconds();
            groupBuyDelayQueue.refundGroupBuy(userGroupBuyId,seconds);
        } catch (Exception e) {
            log.error("",e);
        }
    }

    /**
     * ?????????????????????30?????????????????????
     */
    private void leftTimeNotifyGroupBuy(Long userGroupBuyId,Date endDate){
        try {
            int coutDownMinutes = 30;//?????????30??????
            DateTime now = DateTime.now();
            DateTime notifyTime = new DateTime(endDate).minusMinutes(coutDownMinutes);
            if (notifyTime.isAfter(now)){
                //????????????????????????????????????
                long seconds = Seconds.secondsBetween(now,notifyTime).getSeconds();
                //groupBuyDelayQueue.leftTimeNotifyGroupBuy(userGroupBuyId,seconds);
            }
        } catch (Exception e) {
            log.error("",e);
        }
    }

    /**
     * ???????????????
     *
     * @param request
     * @return
     */
    @Override
    public ResponseResult<Boolean> releaseGroupBuyLock(@RequestBody GroupBuyRequest request) {
        log.info("????????????????????????request -> {}",request);
        if (request.getOrderId() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        Long orderId = request.getOrderId();
        int row = userGroupBuyRecordMapper.releaseGroupBuyLock(orderId);
        if (row > 0){
            return ResponseResult.buildSuccessResponse(Boolean.TRUE);
        }
        return ResponseResult.buildFailResponse();
    }

    /**
     * ???????????????????????????
     *
     * @param message
     * @return
     */
    @Override
    public ResponseResult<Boolean> refundSigleGroupBuy(@RequestBody DelayQueueMessage message) {
        if (message.getBizId() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        //??????
        doRefundGroupBuy(message.getBizId());
        return ResponseResult.buildSuccessResponse();
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    @Override
    public ResponseResult<Boolean> refundGroupBuy() {
        Date now = new Date();
        //??????????????????????????????
        List<UserGroupBuyRecordDto> userGroupBuyRecordList = userGroupBuyRecordMapper.queryJoinedGroupBuyList(now);
        log.info("???????????????????????????");
        if (CollectionUtils.isNotEmpty(userGroupBuyRecordList)){
            for (UserGroupBuyRecordDto record : userGroupBuyRecordList){
                //??????
                doRefundGroupBuy(record.getUserGroupBuyId());
            }
        }
        return ResponseResult.buildSuccessResponse();
    }

    /**
     * ??????xx??????????????????????????????
     * @return
     */
    @Override
    public ResponseResult<Boolean> leftTimeNotifyGroupBuy(@RequestBody DelayQueueMessage message) {
        if (message.getBizId() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        log.info("??????????????????????????????userGroupBuyId -> {}",message.getBizId());
        String key = "left_time_notify:"+message.getBizId();
        Boolean lockSuccess = false;
        try {
            lockSuccess = RedisCache.setIfAbsent(key,"1");
            log.info(" ?????? lockSuccess -> {}",lockSuccess);
            if (lockSuccess){
                UserGroupBuy userGroupBuy = userGroupBuyMapper.selectByPrimaryKey(message.getBizId());
                if (userGroupBuy != null){
                    ActivityGroupBuy activityGroupBuy = activityGroupBuyMapper.selectByUserGroupBuyId(userGroupBuy.getId());
                    GroupBuyWechatMsgMessage msgMessage = GroupBuyWechatMsgMessage.builder().userGroupBuyId(userGroupBuy.getId())
                            .companyId(userGroupBuy.getCompanyId()).deliveryDate(activityGroupBuy.getDeliveryDate()).scene(LEFT_TIME_NOTIFY)
                            .goodsCode(activityGroupBuy.getGoodsCode()).price(activityGroupBuy.getBuyPrice()).build();
                    Integer minJoinNum = userGroupBuy.getMinJoinNum();
                    int hasJoinedNum = userGroupBuyRecordMapper.countJoinedTimesExcludeCurrentUser(userGroupBuy.getId(),null);
                    List<JoinUser> joinUserList = userGroupBuyRecordMapper.fetchGroupBuyJoinedUsers(message.getBizId());
                    msgMessage.setJoinUserList(joinUserList);
                    if (minJoinNum > hasJoinedNum){
                        msgMessage.setLeftNum(minJoinNum - hasJoinedNum);
                        msgMessage.setSeconds(Seconds.secondsBetween(DateTime.now(),new DateTime(userGroupBuy.getEndDate())).getSeconds());
                        msgMessage.setWarnTips("??????????????????????????????");
                        groupBuyProducer.wechatMsgNofitMQ(msgMessage);
                    }
                }
            }
        } catch (Exception e) {
            log.error("",e);
        }finally {
            if (lockSuccess){
                RedisCache.expire(key,1,TimeUnit.MINUTES);
            }
        }
        return ResponseResult.buildSuccessResponse();
    }

    //??????
    private void doRefundGroupBuy(Long userGroupBuyId){
        String key = "refund_group_buy:"+userGroupBuyId;
        RLock lock = LockProvider.getLock(key);
        try {
            lock.lock();
            List<JoinUser> joinUsers = userGroupBuyRecordMapper.fetchGroupBuyJoinedUsers(userGroupBuyId);
            log.info("??????????????????????????????");
            int row = userGroupBuyRecordMapper.refundGroupBuy(userGroupBuyId);
            if (row > 0){
                //??????mq??????????????????
                groupBuyProducer.cancelGroupBuyMQ(GroupBugCancelMessage.builder().userGroupBuyId(userGroupBuyId).build());
                //????????????????????????
                sendRefundGroupBuyWechatMsg(userGroupBuyId,joinUsers);
            }
        } catch (Exception e) {
            log.error("",e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * ????????????????????????
     *
     * @param userGroupBuyId
     */
    private void sendRefundGroupBuyWechatMsg(Long userGroupBuyId,List<JoinUser> joinUsers){
        if (CollectionUtils.isNotEmpty(joinUsers)){

            //????????????
            ActivityGroupBuy activityGroupBuy = activityGroupBuyMapper.selectByUserGroupBuyId(userGroupBuyId);
            GroupBuyWechatMsgMessage msgMessage = GroupBuyWechatMsgMessage.builder()
                    .deliveryDate(activityGroupBuy.getDeliveryDate()).userGroupBuyId(userGroupBuyId)
                    .goodsCode(activityGroupBuy.getGoodsCode()).price(activityGroupBuy.getBuyPrice()).build();

            Integer minJoinNum = activityGroupBuy.getMinJoinNum();
            msgMessage.setScene(TIMEOUT_NOTIFY);
            msgMessage.setJoinUserList(joinUsers);
            msgMessage.setFailReason("?????????????????????"+minJoinNum+"?????????");
            msgMessage.setWarnTips("????????????????????????????????????????????????????????????????????????~");

            groupBuyProducer.wechatMsgNofitMQ(msgMessage);
        }
    }

    /**
     * ????????????????????????
     *
     * @param groupBuyRequest
     * @return
     */
    @Override
    public ResponseResult<ActivityGroupBuyDto> queryActivityGroupBuyInfo(@RequestBody GroupBuyRequest groupBuyRequest) {
        if (groupBuyRequest.getActivityGroupBuyId() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        ActivityGroupBuy actGroupBuy = activityGroupBuyMapper.selectByPrimaryKey(groupBuyRequest.getActivityGroupBuyId());
        if (actGroupBuy == null){
            return ResponseResult.buildFailResponse(ACTIVITY_GROUP_BUY_NOT_EXIST.code(), ACTIVITY_GROUP_BUY_NOT_EXIST.message());
        }
        return ResponseResult.buildSuccessResponse(ActivityGroupBuyDto.builder().goodsCode(actGroupBuy.getGoodsCode()).buyPrice(actGroupBuy.getBuyPrice()).deliveryDate(actGroupBuy.getDeliveryDate()).build());
    }

    /**
     * ????????????????????????
     *
     * @param groupBuyRequest
     * @return
     */
    @Override
    public ResponseResult<GroupBuyResponse> joinGroupBuyForPay(@RequestBody GroupBuyRequest groupBuyRequest) {
        log.info("?????????????????????????????? groupBuyRequest -> {}", groupBuyRequest);
        if (groupBuyRequest.getUserGroupBuyId() == null || groupBuyRequest.getUserId() == null || StringUtils.isBlank(groupBuyRequest.getOpenId()) || groupBuyRequest.getOrderId() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        Long userGroupBuyId = groupBuyRequest.getUserGroupBuyId();
        Long userId = groupBuyRequest.getUserId();

        //1.???????????????????????????
        UserGroupBuyRecord userGroupBuyRecord = userGroupBuyRecordMapper.hasJoinedThisGroup(userGroupBuyId,userId);
        if (userGroupBuyRecord != null){
            return ResponseResult.buildFailResponse(REPEAT_JOINED_USER_GROUP_BUY.code(),REPEAT_JOINED_USER_GROUP_BUY.message());
        }
        UserGroupBuy userGroupBuy = userGroupBuyMapper.selectByPrimaryKey(userGroupBuyId);
        if (userGroupBuy == null){
            return ResponseResult.buildFailResponse(ACTIVITY_GROUP_BUY_NOT_EXIST.code(),ACTIVITY_GROUP_BUY_NOT_EXIST.message());
        }

        Long groupBuyId = userGroupBuy.getActivityGroupBuyId();
        Date now = new Date();
        //2.?????????????????????????????????????????????????????????
        UserGroupBuyDto userGroupBuyDto = queryJoinedGroupBuy(groupBuyId, userId,now);
        if (userGroupBuyDto != null) {
            //???????????????
            return ResponseResult.buildFailResponse(HAS_JOINED_ACTIVITY_GROUP_BUY.code(), HAS_JOINED_ACTIVITY_GROUP_BUY.message());
        }
        //3.1 ????????????????????????????????????????????????30????????????????????????????????????????????????????????????????????????????????????????????????
        Boolean isRunning = now.after(userGroupBuy.getStartDate()) && now.before(new DateTime(userGroupBuy.getEndDate()).minusSeconds(6).toDate());
        if (!isRunning){
            //?????????
            return ResponseResult.buildFailResponse(USER_GROUP_BUY_EXPIRED.code(),USER_GROUP_BUY_EXPIRED.message());
        }
        String lockKey = "join_group_buy_lock:"+userGroupBuyId;
        RLock rLock = LockProvider.getLock(lockKey);
        try {
            rLock.lock();
            //3.2 ??????????????????
            Integer joinNum = userGroupBuy.getMinJoinNum();
            int lockedNum = userGroupBuyRecordMapper.countLockedNumExcludeMeIfPresent(userGroupBuyId,userId);

            if (lockedNum >= joinNum){
                return ResponseResult.buildFailResponse(BEYOND_GROUP_BUY_JOINED_NUM.code(),BEYOND_GROUP_BUY_JOINED_NUM.message());
            }
            UserGroupBuyRecord myJoinedRecord = userGroupBuyRecordMapper.queryReentrentUser(userGroupBuyId,userId);
            Long groupBuyRecordId = null;
            if (myJoinedRecord != null){
                log.info("??????????????????????????????");
                groupBuyRecordId = updateJoinGroupBuyStatus(myJoinedRecord,groupBuyRequest);
            }else {
                //4.??????
                groupBuyRecordId = doJoinGroupBuy(groupBuyRequest);
            }
            if (groupBuyRecordId != null){
                //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                GroupBuyUtil.lockBeforePayCallback(groupBuyRequest.getOrderId());
                return ResponseResult.buildSuccessResponse(GroupBuyResponse.builder().userGroupBuyId(userGroupBuyId).userGroupBuyRecordId(groupBuyRecordId).build());
            }

        } catch (Exception e) {
            log.error("",e);
        }finally {
            rLock.unlock();
        }
        return ResponseResult.buildFailResponse(JOIN_USER_GROUP_BUY_FAILED.code(),JOIN_USER_GROUP_BUY_FAILED.message());
    }

    /**
     * ??????????????????
     *
     * @param payRequest
     * @return
     */
    @Override
    public ResponseResult<Boolean> groupBuyPaySuccess(@RequestBody GroupBuyPayRequest payRequest) {
        log.info("?????????????????? payRequest -> {}", payRequest);
        if (payRequest.getOrderId() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        Long orderId = payRequest.getOrderId();//??????id
        UserGroupBuyRecord userGroupBuyRecord = userGroupBuyRecordMapper.selectByOrderId(orderId);

        if (userGroupBuyRecord == null){
            return ResponseResult.buildFailResponse(USER_GROUP_BUY_RECORD_NOT_EXIST.code(), USER_GROUP_BUY_RECORD_NOT_EXIST.message());
        }

        String lockKey = "group_buy_pay_lock_key" + userGroupBuyRecord.getUserGroupBuyId();
        RLock lock = LockProvider.getLock(lockKey);
        try {
            lock.lock();
            //DCL
            userGroupBuyRecord = userGroupBuyRecordMapper.selectByOrderId(orderId);
            if (JOINED_UNPAY.getCode().equals(userGroupBuyRecord.getStatus())) {//??????????????????
                UserGroupBuy userGroupBuy = userGroupBuyMapper.selectByPrimaryKey(userGroupBuyRecord.getUserGroupBuyId());
                if (userGroupBuy != null) {
                    ActivityGroupBuy activityGroupBuy = getActivityGroupBuy(userGroupBuy.getActivityGroupBuyId());
                    if (activityGroupBuy != null && activityGroupBuy.getId() != null){
                        //????????????????????????
                        return updateStatusAfterPaySuccess(activityGroupBuy,userGroupBuy,userGroupBuyRecord);
                    }
                }
            } else {
                log.info("????????????????????????????????????????????????, userGroupBuyId -> {}", userGroupBuyRecord.getUserGroupBuyId());
                return ResponseResult.buildSuccessResponse();
            }
        } catch (Exception e) {
            log.error("", e);
            throw e;
        } finally {
            lock.unlock();
            //??????????????????????????????
            GroupBuyUtil.unlockAfterPayCallback(orderId);
        }
        return ResponseResult.buildFailResponse();
    }

    /**
     * ????????????????????????
     *
     * @param userGroupBuy
     * @param userGroupBuyRecord
     * @return
     */
    private ResponseResult<Boolean> updateStatusAfterPaySuccess(ActivityGroupBuy activityGroupBuy,UserGroupBuy userGroupBuy, UserGroupBuyRecord userGroupBuyRecord) {
        Integer minJoinNum = userGroupBuy.getMinJoinNum();
        int count = userGroupBuyRecordMapper.countJoinedTimesExcludeCurrentUser(userGroupBuy.getId(),userGroupBuyRecord.getUserId());
        if (count >= minJoinNum) {
            log.info("?????????????????? userGroupBuyRecordId -> {}", userGroupBuyRecord.getId());
            return ResponseResult.buildFailResponse(BEYOND_GROUP_BUY_JOINED_NUM.code(), BEYOND_GROUP_BUY_JOINED_NUM.message());
        }
        int row = 0;
        Boolean createGroupBuy = false;//????????????
        Boolean completeGroupBuy = false;//??????
        if (CREATE_UNPAY.getCode().equals(userGroupBuy.getStatus())) {
            if (minJoinNum == 1){
                //1.0 ???????????????????????????????????????
                row = userGroupBuyRecordMapper.completeGroupBuy(userGroupBuyRecord.getId());
                completeGroupBuy = row > 0;
            }else {
                //1.1 ??????????????????????????????????????????&??????????????????
                log.info("???????????????????????????????????????????????????????????? userGroupBuyRecordId -> {}", userGroupBuyRecord.getId());
                row = userGroupBuyRecordMapper.updateStatusAfterCreateGroupBuy(userGroupBuyRecord.getId());
                if (row>0){
                    createGroupBuy = true;
                    //???????????????????????????????????????????????????????????????
                    registerCanelGroupBuyDelayQueue(userGroupBuy.getId(),userGroupBuy.getEndDate());
                    //?????????????????????30????????????????????????PS:??????????????????????????????????????????????????????30????????????30??????????????????????????????
                    //leftTimeNotifyGroupBuy(userGroupBuy.getId(),userGroupBuy.getEndDate());
                }
            }
        } else if (minJoinNum == (count+1)){
            //2.????????????
            row = userGroupBuyRecordMapper.completeGroupBuy(userGroupBuyRecord.getId());
            completeGroupBuy = row > 0;
        }else{
            //3.????????????
            UserGroupBuyRecord record = new UserGroupBuyRecord();
            record.setId(userGroupBuyRecord.getId());
            record.setStatus(JOINED_PAY.getCode());//????????????
            record.setLockStatus(LOCK.getCode());//?????????
            record.setRemark(JOINED_PAY.getMsg()+"_"+LOCK.getMsg());//????????????
            record.setUpdateTime(new Date());
            row = userGroupBuyRecordMapper.updateByPrimaryKeySelective(record);
        }
        if (row > 0){
            int hasJoinedNum = userGroupBuyRecordMapper.countJoinedTimesExcludeCurrentUser(userGroupBuy.getId(),null);
            //??????????????????
            sendWechatMsg(activityGroupBuy,userGroupBuy,userGroupBuyRecord,hasJoinedNum,createGroupBuy);
            GroupBuyUtil.clearGroupBuyJoinedUsers(userGroupBuy.getId());
            GroupBuyUtil.clearUserJoinedGroupBuy(userGroupBuy.getActivityGroupBuyId(),userGroupBuyRecord.getUserId());
            //??????????????????????????????
            handleTrigerAfterCompleteGroupBuy(completeGroupBuy,userGroupBuy.getId());
            return ResponseResult.buildSuccessResponse(completeGroupBuy);
        }
        log.error("updateStatusAfterPaySuccess ?????????????????????????????????userGroupBuyRecordId -> {}, userId -> {}",userGroupBuyRecord.getId(),userGroupBuyRecord.getUserId());
        return ResponseResult.buildFailResponse(UPDATE_STATUS_FAIL_AFTER_PAY_SUCCESS.code(),UPDATE_STATUS_FAIL_AFTER_PAY_SUCCESS.message());
    }

    /**
     *
     * @param completeGroupBuy
     * @param userGroupBuyId
     */
    private void handleTrigerAfterCompleteGroupBuy(Boolean completeGroupBuy,Long userGroupBuyId){
        if (completeGroupBuy){
            try {
                List<JoinUser> joinUserList = userGroupBuyRecordMapper.fetchGroupBuyJoinedUsers(userGroupBuyId);
                if (CollectionUtils.isNotEmpty(joinUserList)){
                    //1.???????????????????????????"?????????"
                    List<Long> userIdList = Lists.transform(joinUserList, new Function<JoinUser, Long>() {
                        @Nullable
                        @Override
                        public Long apply(@Nullable JoinUser input) {
                            return input.getUserId();
                        }
                    });
                    groupBuyProducer.updateUserStatusAfterCompleteGroupBuy(GroupBuyUpdateUserMessage.builder().userIdList(userIdList).build());

                    //2.???????????????????????????
                    for (JoinUser joinUser : joinUserList){
                        UserCouponRequest couponRequest = UserCouponRequest.builder().launchType(COMPLETE_GROUPBUY_ORDER_SHARE).launchId(joinUser.getOrderId())
                                .userId(joinUser.getUserId()).openid(joinUser.getOpenId()).build();
                        couponRequest.setBusinessLine(BusinessLineEnum.BREAKFAST.getCode());
                        breakfastCouponService.createMultiShareCoupon(couponRequest);
                    }

                }
            } catch (Exception e) {
                log.error("",e);
            }
        }

    }

    private void sendWechatMsg(ActivityGroupBuy activityGroupBuy,UserGroupBuy userGroupBuy,UserGroupBuyRecord record,int hasJoinedNum,Boolean createGroupBuy){
        try {
            Integer minJoinNum = userGroupBuy.getMinJoinNum();

            GroupBuyWechatMsgMessage msgMessage = GroupBuyWechatMsgMessage.builder()
                    .companyId(userGroupBuy.getCompanyId()).deliveryDate(activityGroupBuy.getDeliveryDate()).userGroupBuyId(userGroupBuy.getId())
                    .leftNum(minJoinNum - hasJoinedNum).seconds(Seconds.secondsBetween(DateTime.now(),new DateTime(userGroupBuy.getEndDate())).getSeconds())
                    .goodsCode(activityGroupBuy.getGoodsCode()).price(activityGroupBuy.getBuyPrice()).build();

            List<JoinUser> joinUsers = userGroupBuyRecordMapper.fetchGroupBuyJoinedUsers(userGroupBuy.getId());
            if (CollectionUtils.isEmpty(joinUsers)){
                return;
            }
            if (createGroupBuy){//????????????
                msgMessage.setJoinUserList(Lists.newArrayList(joinUsers.get(0)));
                msgMessage.setScene(GrouyBuyEnum.SEND_MSG_SCENE.CREATE_GROUPBUY_NOTIFY);
                groupBuyProducer.wechatMsgNofitMQ(msgMessage);
            }else if (minJoinNum > 1){
                if (minJoinNum == hasJoinedNum){//??????
                    msgMessage.setJoinUserList(joinUsers);
                    //??????
                    msgMessage.setScene(GrouyBuyEnum.SEND_MSG_SCENE.COMPLETE_NOTIFY);
                    groupBuyProducer.wechatMsgNofitMQ(msgMessage);
                }else if (minJoinNum == 3 && minJoinNum == hasJoinedNum + 1){//????????????
                    //3?????????????????????????????????????????????????????????
                    msgMessage.setJoinUserList(Lists.newArrayList(joinUsers.get(0)));
                    msgMessage.setScene(GrouyBuyEnum.SEND_MSG_SCENE.PROCESS_NOTIFY);
                    msgMessage.setJoinedNum(hasJoinedNum-1);
                    msgMessage.setSeconds(Seconds.secondsBetween(DateTime.now(),new DateTime(userGroupBuy.getEndDate())).getSeconds());
                    groupBuyProducer.wechatMsgNofitMQ(msgMessage);
                    //????????????
                    JoinUser joinUser = JoinUser.builder().userId(record.getUserId()).openId(record.getOpenId()).orderId(record.getOrderId()).build();
                    msgMessage.setJoinUserList(Lists.newArrayList(joinUser));
                    msgMessage.setScene(GrouyBuyEnum.SEND_MSG_SCENE.CREATE_GROUPBUY_NOTIFY);
                    groupBuyProducer.wechatMsgNofitMQ(msgMessage);
                }else if (minJoinNum > 3){
                    if (hasJoinedNum == 2 || (minJoinNum == hasJoinedNum + 1)){//????????????
                        //3??????????????????????????????or??????????????????????????????????????????
                        msgMessage.setJoinUserList(Lists.newArrayList(joinUsers.get(0)));
                        msgMessage.setScene(GrouyBuyEnum.SEND_MSG_SCENE.PROCESS_NOTIFY);
                        msgMessage.setJoinedNum(hasJoinedNum-1);
                        msgMessage.setSeconds(Seconds.secondsBetween(DateTime.now(),new DateTime(userGroupBuy.getEndDate())).getSeconds());
                        groupBuyProducer.wechatMsgNofitMQ(msgMessage);
                    }
                    //????????????
                    JoinUser joinUser = JoinUser.builder().userId(record.getUserId()).openId(record.getOpenId()).orderId(record.getOrderId()).build();
                    msgMessage.setJoinUserList(Lists.newArrayList(joinUser));
                    msgMessage.setScene(GrouyBuyEnum.SEND_MSG_SCENE.CREATE_GROUPBUY_NOTIFY);
                    groupBuyProducer.wechatMsgNofitMQ(msgMessage);
                }else {//????????????
                    JoinUser joinUser = JoinUser.builder().userId(record.getUserId()).openId(record.getOpenId()).orderId(record.getOrderId()).build();
                    msgMessage.setJoinUserList(Lists.newArrayList(joinUser));
                    msgMessage.setScene(GrouyBuyEnum.SEND_MSG_SCENE.CREATE_GROUPBUY_NOTIFY);
                    groupBuyProducer.wechatMsgNofitMQ(msgMessage);
                }
            }
        } catch (Exception e) {
            log.error("",e);
        }
    }

    /**
     * ??????????????????
     *
     * @param activityGroupBuy
     * @param groupBuyRequest
     */
    private UserGroupBuy doCreateGroupBuy(ActivityGroupBuy activityGroupBuy, GroupBuyRequest groupBuyRequest,Long userGroupBuyRecordId) {
        Long activityGroupBuyId = activityGroupBuy.getId();
        Long userId = groupBuyRequest.getUserId();
        String lockKey = "group_buy_lock_key:" + groupBuyRequest.getOrderId();
        Boolean lockSuccess = RedisCache.setIfAbsent(lockKey, "1");
        try {
            Integer minJoinNum = activityGroupBuy.getMinJoinNum();
            if (minJoinNum <= 0) {
                log.info("????????????????????????1");
                return null;
            }

            if (lockSuccess) {
                Long userGroupBuyId = IdGenerator.nextId();
                UserGroupBuy userGroupBuy = new UserGroupBuy();
                userGroupBuy.setId(userGroupBuyId);
                userGroupBuy.setActivityGroupBuyId(activityGroupBuyId);
                userGroupBuy.setOriginUserId(userId);
                userGroupBuy.setCompanyId(groupBuyRequest.getCompanyId());
                userGroupBuy.setStatus(CREATE_UNPAY.getCode());
                userGroupBuy.setRemark(CREATE_UNPAY.getMsg());
                userGroupBuy.setMinJoinNum(minJoinNum);
                userGroupBuy.setValidPeriod(activityGroupBuy.getValidPeriod());
                DateTime now = DateTime.now();
                Date endDate = now.plusMinutes(activityGroupBuy.getValidPeriod()).toDate();
                userGroupBuy.setStartDate(now.toDate());
                userGroupBuy.setEndDate(endDate.after(activityGroupBuy.getEndDate())?activityGroupBuy.getEndDate():endDate);
                userGroupBuy.setCreateTime(now.toDate());
                userGroupBuy.setUpdateTime(now.toDate());
                int row = userGroupBuyMapper.insertSelective(userGroupBuy);
                if (row > 0) {
                    UserGroupBuyRecord userGroupBuyRecord = new UserGroupBuyRecord();
                    userGroupBuyRecord.setId(userGroupBuyRecordId);
                    userGroupBuyRecord.setUserGroupBuyId(userGroupBuyId);
                    userGroupBuyRecord.setUserId(userId);
                    userGroupBuyRecord.setOpenId(groupBuyRequest.getOpenId());
                    userGroupBuyRecord.setNickName(groupBuyRequest.getNickName());
                    userGroupBuyRecord.setHeadPortraitUrl(groupBuyRequest.getHeadPortraitUrl());
                    userGroupBuyRecord.setJoinTime(now.toDate());//????????????
                    userGroupBuyRecord.setLockTime(now.toDate());//????????????
                    userGroupBuyRecord.setLockStatus(LOCK.getCode());//??????
                    userGroupBuyRecord.setOrderId(groupBuyRequest.getOrderId());//??????id
                    userGroupBuyRecord.setStatus(JOINED_UNPAY.getCode());
                    userGroupBuyRecord.setRemark(JOINED_UNPAY.getMsg() + "_" + LOCK.getMsg());
                    userGroupBuyRecord.setCreateTime(now.toDate());
                    userGroupBuyRecord.setUpdateTime(now.toDate());
                    row = userGroupBuyRecordMapper.insertSelective(userGroupBuyRecord);
                    return row > 0 ? userGroupBuy : null;
                }
            }
        } catch (Exception e) {
            log.error("", e);
            throw e;
        } finally {
            RedisCache.expire(lockKey, 2, TimeUnit.SECONDS);
        }
        return null;
    }

    /**
     * ????????????????????????
     *
     * @param joinedRecord
     * @param groupBuyRequest
     */
    private Long updateJoinGroupBuyStatus(UserGroupBuyRecord joinedRecord,GroupBuyRequest groupBuyRequest) {
        try {
            Date now = new Date();
            Long userGroupBuyRecordId = joinedRecord.getId();
            UserGroupBuyRecord userGroupBuyRecord = new UserGroupBuyRecord();
            userGroupBuyRecord.setId(userGroupBuyRecordId);
            userGroupBuyRecord.setNickName(groupBuyRequest.getNickName());
            userGroupBuyRecord.setHeadPortraitUrl(groupBuyRequest.getHeadPortraitUrl());
            userGroupBuyRecord.setLockTime(now);//????????????
            userGroupBuyRecord.setLockStatus(LOCK.getCode());//??????
            userGroupBuyRecord.setOrderId(groupBuyRequest.getOrderId());//??????id
            userGroupBuyRecord.setStatus(JOINED_UNPAY.getCode());
            userGroupBuyRecord.setRemark(JOINED_UNPAY.getMsg() + "_" + LOCK.getMsg());
            userGroupBuyRecord.setUpdateTime(now);
            int row = userGroupBuyRecordMapper.updateByPrimaryKeySelective(userGroupBuyRecord);
            return row > 0 ? userGroupBuyRecordId : null;
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }

    /**
     * ??????????????????
     *
     * @param groupBuyRequest
     */
    private Long doJoinGroupBuy(GroupBuyRequest groupBuyRequest) {
        Long userGroupBuyId = groupBuyRequest.getUserGroupBuyId();
        Long userId = groupBuyRequest.getUserId();
        try {
            Date now = new Date();
            Long userGroupBuyRecordId = IdGenerator.nextId();
            UserGroupBuyRecord userGroupBuyRecord = new UserGroupBuyRecord();
            userGroupBuyRecord.setId(userGroupBuyRecordId);
            userGroupBuyRecord.setUserGroupBuyId(userGroupBuyId);
            userGroupBuyRecord.setUserId(userId);
            userGroupBuyRecord.setOpenId(groupBuyRequest.getOpenId());
            userGroupBuyRecord.setNickName(groupBuyRequest.getNickName());
            userGroupBuyRecord.setHeadPortraitUrl(groupBuyRequest.getHeadPortraitUrl());
            userGroupBuyRecord.setJoinTime(now);//????????????
            userGroupBuyRecord.setLockTime(now);//????????????
            userGroupBuyRecord.setLockStatus(LOCK.getCode());//??????
            userGroupBuyRecord.setOrderId(groupBuyRequest.getOrderId());//??????id
            userGroupBuyRecord.setStatus(JOINED_UNPAY.getCode());
            userGroupBuyRecord.setRemark(JOINED_UNPAY.getMsg() + "_" + LOCK.getMsg());
            userGroupBuyRecord.setCreateTime(now);
            userGroupBuyRecord.setUpdateTime(now);
            int row = userGroupBuyRecordMapper.insertSelective(userGroupBuyRecord);
            return row > 0 ? userGroupBuyRecordId : null;
        } catch (Exception e) {
            log.error("", e);
            throw e;
        }
    }

    /**
     * ??????id??????????????????
     *
     * @param activityGroupBuyId
     * @return
     */
    private ActivityGroupBuy getActivityGroupBuy(Long activityGroupBuyId) {
        ActivityGroupBuy activityGroupBuy = GroupBuyUtil.getActivityGroupBuy(activityGroupBuyId);
        if (activityGroupBuy != null) {
            return activityGroupBuy;
        }
        activityGroupBuy = activityGroupBuyMapper.selectByPrimaryKey(activityGroupBuyId);
        if (activityGroupBuy != null) {
            int seconds = Seconds.secondsBetween(DateTime.now(),new DateTime(activityGroupBuy.getEndDate())).getSeconds();
            if ((seconds - 30) > 0){
                GroupBuyUtil.setActivityGroupBuy(activityGroupBuyId, activityGroupBuy, seconds - 30, TimeUnit.SECONDS);//????????????30s???????????????????????????????????????????????????db???????????????
            }
            return activityGroupBuy;
        }
        activityGroupBuy = new ActivityGroupBuy();
        //??????????????????
        GroupBuyUtil.setActivityGroupBuy(activityGroupBuyId, activityGroupBuy, 5, TimeUnit.SECONDS);
        return activityGroupBuy;
    }


    /**
     * ??????id
     *
     * @param groupBuyId
     * @param userId
     * @return
     */
    private UserGroupBuyDto queryJoinedGroupBuy(Long groupBuyId, Long userId,Date now) {
        return userGroupBuyRecordMapper.queryJoinedGroupBuy(groupBuyId, userId,now);
//        UserGroupBuyDto userGroupBuy = GroupBuyUtil.userJoinedGroupBuy(groupBuyId, userId);
//        if (userGroupBuy != null) {
//            return userGroupBuy;
//        }
//        //??????????????????????????????
//        userGroupBuy = userGroupBuyRecordMapper.queryJoinedGroupBuy(groupBuyId, userId);
//        if (userGroupBuy != null) {
//            GroupBuyUtil.cacheUserJoinedGroupBuy(userGroupBuy, groupBuyId, userId, 24 * 60 * 60, TimeUnit.SECONDS);
//            return userGroupBuy;
//        }
//        return userGroupBuy;
    }


    /**
     * ?????????????????????????????????
     *
     * @return
     */
    private ActivityGroupBuyDto queryActiveGroupBuyDetail() {
        ActivityGroupBuyDto activityGroupBuy = GroupBuyUtil.currentActivityGroupBuy();
        if (activityGroupBuy != null) {
            return activityGroupBuy;
        }
        Date now = new Date();
        activityGroupBuy = activityGroupBuyMapper.queryActiveGroupBuyDetail(now);
        if (activityGroupBuy != null) {
            Date endDate = activityGroupBuy.getEndDate();
            long seconds = Seconds.secondsBetween(new DateTime(now), new DateTime(endDate)).getSeconds();
            if (seconds > 0) {
                GroupBuyUtil.setCurrentActivityGroupBuy(activityGroupBuy, seconds, TimeUnit.SECONDS);
                return activityGroupBuy;
            }
        }
        activityGroupBuy = new ActivityGroupBuyDto();
        //???????????????????????????DB??????
        GroupBuyUtil.setCurrentActivityGroupBuy(activityGroupBuy, 5, TimeUnit.SECONDS);
        return activityGroupBuy;
    }

    /**
     * ?????????????????????????????????
     *
     * @param groupBuyRequest
     * @return
     */
    @Override
    public ResponseResult<GroupBuyResponse> querySelfJoinedGroupBuy(@RequestBody GroupBuyRequest groupBuyRequest) {
        log.info("querySelfJoinedGroupBuy groupBuyRequest -> {}",groupBuyRequest);
        if (groupBuyRequest.getActivityGroupBuyId() == null || groupBuyRequest.getUserId() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        Long userId = groupBuyRequest.getUserId();
        Long activityGroupBuyId = groupBuyRequest.getActivityGroupBuyId();

        Long orderId = groupBuyRequest.getOrderId();
        if (orderId != null){
            //??????or????????????????????????????????????count_down_latch ??????3???
            GroupBuyUtil.waitBeforePayCallback(orderId);
        }

        //???????????????????????????
        ActivityGroupBuy activityGroupBuy = getActivityGroupBuy(activityGroupBuyId);
        if (activityGroupBuy == null || activityGroupBuy.getId() == null){
            return ResponseResult.buildFailResponse(ACTIVITY_GROUP_BUY_NOT_EXIST.code(),ACTIVITY_GROUP_BUY_NOT_EXIST.message());
        }

        Date now = new Date();
        //?????????????????????
        UserGroupBuyDto userGroupBuy = queryJoinedGroupBuy(activityGroupBuyId, userId,now);

        //???????????????????????????
        long seconds = Seconds.secondsBetween(new DateTime(now), new DateTime(activityGroupBuy.getEndDate())).getSeconds();
        seconds = seconds - 1;//???????????????????????????????????????seconds????????????1s??????????????????????????????
        if (userGroupBuy == null) {//???????????????
            if (seconds > 0) {
                //?????????????????????
                log.info("????????????????????????activityGroupBuyId -> {}", activityGroupBuyId);
            } else {
                //?????????????????????
                log.info("????????????????????????activityGroupBuyId -> {}", activityGroupBuyId);
            }
            userGroupBuy = BeanMapper.map(activityGroupBuy, UserGroupBuyDto.class);
            return ResponseResult.buildSuccessResponse(GroupBuyResponse.builder().activityGroupBuyExpireTime((int)seconds).joined(false).activityGroupBuyisOver(seconds<=0).companyId(userGroupBuy.getCompanyId()).userGroupBuyDetail(userGroupBuy).build());
        }
        //?????????????????????
        List<JoinUser> joinUserList = fetchGroupBuyJoinedUsers(userGroupBuy.getId());
        Integer leftNum = userGroupBuy.getMinJoinNum() - joinUserList.size();
        return ResponseResult.buildSuccessResponse(GroupBuyResponse.builder().activityGroupBuyExpireTime(seconds>0?(int)seconds:0).joined(userGroupBuy != null).leftNum(leftNum < 0 ? 0 : leftNum).activityGroupBuyisOver(seconds<=0).companyId(userGroupBuy.getCompanyId()).userGroupBuyDetail(userGroupBuy).joinUserList(joinUserList).build());
    }


}