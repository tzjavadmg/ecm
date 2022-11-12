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
 * 早餐拼团
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
     * 获取拼团详情
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
            //查询存在有效拼团
            UserGroupBuyDto userGroupBuy = queryJoinedGroupBuy(activityGroupBuy.getId(), groupBuyRequest.getUserId(),new Date());
            Integer expireTime = null;
            Integer leftNum = null;
            if (userGroupBuy != null){
                expireTime = userGroupBuy.getExpireTime();
                //已参团用户列表
                List<JoinUser> joinUserList = fetchGroupBuyJoinedUsers(userGroupBuy.getId());
                leftNum = userGroupBuy.getMinJoinNum() - joinUserList.size();
            }
            return ResponseResult.buildSuccessResponse(GroupBuyEntryResponse.builder().joined(leftNum!=null).userGroupBuyExpireTime(expireTime).leftNum(leftNum).groupBuyDetail(activityGroupBuy).build());
        }
        log.info("groupBuyEntry 拼团活动不存在");
        return ResponseResult.buildSuccessResponse(ACTIVITY_GROUP_BUY_NOT_EXIST.code(), ACTIVITY_GROUP_BUY_NOT_EXIST.message());
    }

    /**
     * 点击分享的的参团信息
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
            //开团or开团成功，等待支付状态，count_down_latch 加锁3秒
            GroupBuyUtil.waitBeforePayCallback(orderId);
        }

        UserGroupBuyDto userGroupBuyDto = userGroupBuyMapper.queryUserGroupBuyInfo(userGroupBuyId);
        if (userGroupBuyDto == null){
            return ResponseResult.buildFailResponse(ACTIVITY_GROUP_BUY_NOT_EXIST.code(),ACTIVITY_GROUP_BUY_NOT_EXIST.message());
        }

        //1.拼团活动数据
        ActivityGroupBuy activityGroupBuy = getActivityGroupBuy(userGroupBuyDto.getActivityGroupBuyId());
        if (activityGroupBuy==null || activityGroupBuy.getId() == null){
            return ResponseResult.buildFailResponse(ACTIVITY_GROUP_BUY_NOT_EXIST.code(),ACTIVITY_GROUP_BUY_NOT_EXIST.message());
        }

        Date now = new Date();

        //2.拼团活动是否结束
        Boolean isRunning = now.after(activityGroupBuy.getStartDate()) && now.before(activityGroupBuy.getEndDate());
        response.setActivityGroupBuyisOver(!isRunning);
        response.setActivityGroupBuyId(activityGroupBuy.getId());

        //3.已参团用户列表
        List<JoinUser> joinUserList = fetchGroupBuyJoinedUsers(userGroupBuyDto.getId());
        Integer leftNum = userGroupBuyDto.getMinJoinNum() - joinUserList.size();
        //参团用户列表
        response.setJoinUserList(joinUserList);
        //剩余人数
        response.setLeftNum(leftNum <= 0 ? 0 : leftNum);
        //公司id
        response.setCompanyId(userGroupBuyDto.getCompanyId());

        //4.是否已参该团
        UserGroupBuyRecord userGroupBuyRecord = userGroupBuyRecordMapper.hasJoinedThisGroup(userGroupBuyId,userId);
        userGroupBuyDto.setOrderId(userGroupBuyRecord != null?userGroupBuyRecord.getOrderId():null);
        response.setJoined(userGroupBuyRecord != null);
        response.setUserGroupBuyDetail(userGroupBuyDto);

        return ResponseResult.buildSuccessResponse(response);
    }

    /**
     * 查询参团用户
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
     * 预支付订单，创建开团
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public ResponseResult<GroupBuyResponse> createGroupBuyForPay(@RequestBody GroupBuyRequest request) {
        log.info("预支付订单，创建开团入参 request -> {}", request);
        if (request.getActivityGroupBuyId() == null || request.getUserId() == null || request.getOrderId() == null
               || StringUtils.isBlank(request.getOpenId()) || request.getCompanyId() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        Long userId = request.getUserId();
        Long activityGroupBuyId = request.getActivityGroupBuyId();

        Date now = new Date();
        //1.查询存在有效拼团
        UserGroupBuyDto userGroupBuyDto = queryJoinedGroupBuy(activityGroupBuyId, userId,now);
        if (userGroupBuyDto != null) {
            //已参过该团
            return ResponseResult.buildFailResponse(HAS_JOINED_ACTIVITY_GROUP_BUY.code(), HAS_JOINED_ACTIVITY_GROUP_BUY.message());
        }

        //2.判断当前活动是否有效
        ActivityGroupBuy activityGroupBuy = getActivityGroupBuy(activityGroupBuyId);
        if (activityGroupBuy == null || activityGroupBuy.getId() == null){
            return ResponseResult.buildFailResponse(ACTIVITY_GROUP_BUY_NOT_EXIST.code(),ACTIVITY_GROUP_BUY_NOT_EXIST.message());
        }

        Boolean isRunning = now.after(activityGroupBuy.getStartDate()) && now.before(activityGroupBuy.getEndDate());
        if (!isRunning) {//活动已结束
            log.info("创建拼团，但是活动已结束，拼团活动id -> {}", activityGroupBuyId);
            return ResponseResult.buildFailResponse(ACTIVITY_GROUP_BUY_OVER.code(),ACTIVITY_GROUP_BUY_OVER.message());
        }


        Long userGroupBuyRecordId = IdGenerator.nextId();
        //3.创建拼团实例
        UserGroupBuy userGroupBuy = doCreateGroupBuy(activityGroupBuy, request, userGroupBuyRecordId);
        if (userGroupBuy != null) {//开团成功
            //开团成功，加上全局锁等待支付成功回调，支付回调成功后解锁，页面获取拼团信息方可正常获取数据
            GroupBuyUtil.lockBeforePayCallback(request.getOrderId());
            return ResponseResult.buildSuccessResponse(GroupBuyResponse.builder().userGroupBuyId(userGroupBuy.getId()).userGroupBuyRecordId(userGroupBuyRecordId).companyId(request.getCompanyId()).build());
        }
        return ResponseResult.buildFailResponse(CREATE_GROUP_BUY_FAILED.code(), CREATE_GROUP_BUY_FAILED.message());
    }

    /**
     * 拼团中的活动，到期未成团时，急速取消并退款
     */
    private void registerCanelGroupBuyDelayQueue(Long userGroupBuyId,Date endDate){
        try {
            //未成团取消时间，单位：秒
            long seconds = Seconds.secondsBetween(DateTime.now(),new DateTime(endDate)).getSeconds();
            groupBuyDelayQueue.refundGroupBuy(userGroupBuyId,seconds);
        } catch (Exception e) {
            log.error("",e);
        }
    }

    /**
     * 拼团中的活动，30分钟倒计时提醒
     */
    private void leftTimeNotifyGroupBuy(Long userGroupBuyId,Date endDate){
        try {
            int coutDownMinutes = 30;//倒计时30分钟
            DateTime now = DateTime.now();
            DateTime notifyTime = new DateTime(endDate).minusMinutes(coutDownMinutes);
            if (notifyTime.isAfter(now)){
                //未成团取消时间，单位：秒
                long seconds = Seconds.secondsBetween(now,notifyTime).getSeconds();
                //groupBuyDelayQueue.leftTimeNotifyGroupBuy(userGroupBuyId,seconds);
            }
        } catch (Exception e) {
            log.error("",e);
        }
    }

    /**
     * 释放拼团锁
     *
     * @param request
     * @return
     */
    @Override
    public ResponseResult<Boolean> releaseGroupBuyLock(@RequestBody GroupBuyRequest request) {
        log.info("主动释放拼团锁，request -> {}",request);
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
     * 定时取消超时的拼团
     *
     * @param message
     * @return
     */
    @Override
    public ResponseResult<Boolean> refundSigleGroupBuy(@RequestBody DelayQueueMessage message) {
        if (message.getBizId() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        //退款
        doRefundGroupBuy(message.getBizId());
        return ResponseResult.buildSuccessResponse();
    }

    /**
     * 定时取消超时的拼团
     *
     * @return
     */
    @Override
    public ResponseResult<Boolean> refundGroupBuy() {
        Date now = new Date();
        //查询拼团中的订单信息
        List<UserGroupBuyRecordDto> userGroupBuyRecordList = userGroupBuyRecordMapper.queryJoinedGroupBuyList(now);
        log.info("定时取消超时的拼团");
        if (CollectionUtils.isNotEmpty(userGroupBuyRecordList)){
            for (UserGroupBuyRecordDto record : userGroupBuyRecordList){
                //退款
                doRefundGroupBuy(record.getUserGroupBuyId());
            }
        }
        return ResponseResult.buildSuccessResponse();
    }

    /**
     * 剩余xx时间，拼团待成团提醒
     * @return
     */
    @Override
    public ResponseResult<Boolean> leftTimeNotifyGroupBuy(@RequestBody DelayQueueMessage message) {
        if (message.getBizId() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        log.info("超时通知提醒。。。。userGroupBuyId -> {}",message.getBizId());
        String key = "left_time_notify:"+message.getBizId();
        Boolean lockSuccess = false;
        try {
            lockSuccess = RedisCache.setIfAbsent(key,"1");
            log.info(" 加锁 lockSuccess -> {}",lockSuccess);
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
                        msgMessage.setWarnTips("赶紧邀请同事参与吧！");
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

    //退款
    private void doRefundGroupBuy(Long userGroupBuyId){
        String key = "refund_group_buy:"+userGroupBuyId;
        RLock lock = LockProvider.getLock(key);
        try {
            lock.lock();
            List<JoinUser> joinUsers = userGroupBuyRecordMapper.fetchGroupBuyJoinedUsers(userGroupBuyId);
            log.info("延迟队列定时触发退款");
            int row = userGroupBuyRecordMapper.refundGroupBuy(userGroupBuyId);
            if (row > 0){
                //发送mq通知订单退款
                groupBuyProducer.cancelGroupBuyMQ(GroupBugCancelMessage.builder().userGroupBuyId(userGroupBuyId).build());
                //发送退款微信消息
                sendRefundGroupBuyWechatMsg(userGroupBuyId,joinUsers);
            }
        } catch (Exception e) {
            log.error("",e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 发送退款微信消息
     *
     * @param userGroupBuyId
     */
    private void sendRefundGroupBuyWechatMsg(Long userGroupBuyId,List<JoinUser> joinUsers){
        if (CollectionUtils.isNotEmpty(joinUsers)){

            //超时提醒
            ActivityGroupBuy activityGroupBuy = activityGroupBuyMapper.selectByUserGroupBuyId(userGroupBuyId);
            GroupBuyWechatMsgMessage msgMessage = GroupBuyWechatMsgMessage.builder()
                    .deliveryDate(activityGroupBuy.getDeliveryDate()).userGroupBuyId(userGroupBuyId)
                    .goodsCode(activityGroupBuy.getGoodsCode()).price(activityGroupBuy.getBuyPrice()).build();

            Integer minJoinNum = activityGroupBuy.getMinJoinNum();
            msgMessage.setScene(TIMEOUT_NOTIFY);
            msgMessage.setJoinUserList(joinUsers);
            msgMessage.setFailReason("规定时间未凑满"+minJoinNum+"人参团");
            msgMessage.setWarnTips("本次未成团，退款将原路返回至你的账户，注意查收哦~");

            groupBuyProducer.wechatMsgNofitMQ(msgMessage);
        }
    }

    /**
     * 查询拼团活动详情
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
     * 预支付订单，参团
     *
     * @param groupBuyRequest
     * @return
     */
    @Override
    public ResponseResult<GroupBuyResponse> joinGroupBuyForPay(@RequestBody GroupBuyRequest groupBuyRequest) {
        log.info("预支付订单，参团入参 groupBuyRequest -> {}", groupBuyRequest);
        if (groupBuyRequest.getUserGroupBuyId() == null || groupBuyRequest.getUserId() == null || StringUtils.isBlank(groupBuyRequest.getOpenId()) || groupBuyRequest.getOrderId() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        Long userGroupBuyId = groupBuyRequest.getUserGroupBuyId();
        Long userId = groupBuyRequest.getUserId();

        //1.判断是否已参与该团
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
        //2.判断是否已参与该团同一个拼团活动的拼团
        UserGroupBuyDto userGroupBuyDto = queryJoinedGroupBuy(groupBuyId, userId,now);
        if (userGroupBuyDto != null) {
            //已参过该团
            return ResponseResult.buildFailResponse(HAS_JOINED_ACTIVITY_GROUP_BUY.code(), HAS_JOINED_ACTIVITY_GROUP_BUY.message());
        }
        //3.1 判断此团是否：已过期。预参团提前30秒结束，防止预支付和及时退款同时发生，导致团退款了但支付成功了。
        Boolean isRunning = now.after(userGroupBuy.getStartDate()) && now.before(new DateTime(userGroupBuy.getEndDate()).minusSeconds(6).toDate());
        if (!isRunning){
            //已过期
            return ResponseResult.buildFailResponse(USER_GROUP_BUY_EXPIRED.code(),USER_GROUP_BUY_EXPIRED.message());
        }
        String lockKey = "join_group_buy_lock:"+userGroupBuyId;
        RLock rLock = LockProvider.getLock(lockKey);
        try {
            rLock.lock();
            //3.2 拼团名额已满
            Integer joinNum = userGroupBuy.getMinJoinNum();
            int lockedNum = userGroupBuyRecordMapper.countLockedNumExcludeMeIfPresent(userGroupBuyId,userId);

            if (lockedNum >= joinNum){
                return ResponseResult.buildFailResponse(BEYOND_GROUP_BUY_JOINED_NUM.code(),BEYOND_GROUP_BUY_JOINED_NUM.message());
            }
            UserGroupBuyRecord myJoinedRecord = userGroupBuyRecordMapper.queryReentrentUser(userGroupBuyId,userId);
            Long groupBuyRecordId = null;
            if (myJoinedRecord != null){
                log.info("。。。重入参团。。。");
                groupBuyRecordId = updateJoinGroupBuyStatus(myJoinedRecord,groupBuyRequest);
            }else {
                //4.参团
                groupBuyRecordId = doJoinGroupBuy(groupBuyRequest);
            }
            if (groupBuyRecordId != null){
                //参团成功，加上全局锁等待支付成功回调，支付回调成功后解锁，页面获取拼团信息方可正常获取数据
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
     * 参团支付成功
     *
     * @param payRequest
     * @return
     */
    @Override
    public ResponseResult<Boolean> groupBuyPaySuccess(@RequestBody GroupBuyPayRequest payRequest) {
        log.info("拼团支付成功 payRequest -> {}", payRequest);
        if (payRequest.getOrderId() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        Long orderId = payRequest.getOrderId();//订单id
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
            if (JOINED_UNPAY.getCode().equals(userGroupBuyRecord.getStatus())) {//已参团未支付
                UserGroupBuy userGroupBuy = userGroupBuyMapper.selectByPrimaryKey(userGroupBuyRecord.getUserGroupBuyId());
                if (userGroupBuy != null) {
                    ActivityGroupBuy activityGroupBuy = getActivityGroupBuy(userGroupBuy.getActivityGroupBuyId());
                    if (activityGroupBuy != null && activityGroupBuy.getId() != null){
                        //支付成功修改状态
                        return updateStatusAfterPaySuccess(activityGroupBuy,userGroupBuy,userGroupBuyRecord);
                    }
                }
            } else {
                log.info("参团状态已改为成功，无需再次修改, userGroupBuyId -> {}", userGroupBuyRecord.getUserGroupBuyId());
                return ResponseResult.buildSuccessResponse();
            }
        } catch (Exception e) {
            log.error("", e);
            throw e;
        } finally {
            lock.unlock();
            //支付成功，释放等待锁
            GroupBuyUtil.unlockAfterPayCallback(orderId);
        }
        return ResponseResult.buildFailResponse();
    }

    /**
     * 支付成功修改状态
     *
     * @param userGroupBuy
     * @param userGroupBuyRecord
     * @return
     */
    private ResponseResult<Boolean> updateStatusAfterPaySuccess(ActivityGroupBuy activityGroupBuy,UserGroupBuy userGroupBuy, UserGroupBuyRecord userGroupBuyRecord) {
        Integer minJoinNum = userGroupBuy.getMinJoinNum();
        int count = userGroupBuyRecordMapper.countJoinedTimesExcludeCurrentUser(userGroupBuy.getId(),userGroupBuyRecord.getUserId());
        if (count >= minJoinNum) {
            log.info("拼团人数超限 userGroupBuyRecordId -> {}", userGroupBuyRecord.getId());
            return ResponseResult.buildFailResponse(BEYOND_GROUP_BUY_JOINED_NUM.code(), BEYOND_GROUP_BUY_JOINED_NUM.message());
        }
        int row = 0;
        Boolean createGroupBuy = false;//创建开团
        Boolean completeGroupBuy = false;//成团
        if (CREATE_UNPAY.getCode().equals(userGroupBuy.getStatus())) {
            if (minJoinNum == 1){
                //1.0 成团人数只有一人，拼团成功
                row = userGroupBuyRecordMapper.completeGroupBuy(userGroupBuyRecord.getId());
                completeGroupBuy = row > 0;
            }else {
                //1.1 首次开团成功，标记：开团成功&参团记录状态
                log.info("首次开团成功，更新开团成功和参团记录状态 userGroupBuyRecordId -> {}", userGroupBuyRecord.getId());
                row = userGroupBuyRecordMapper.updateStatusAfterCreateGroupBuy(userGroupBuyRecord.getId());
                if (row>0){
                    createGroupBuy = true;
                    //拼团中的活动，到期未成团时，急速取消并退款
                    registerCanelGroupBuyDelayQueue(userGroupBuy.getId(),userGroupBuy.getEndDate());
                    //拼团中的活动，30分钟倒计时提醒。PS:该功能暂时去掉，拼团有效时长如果改为30分钟会和30分钟的成团提醒有冲突
                    //leftTimeNotifyGroupBuy(userGroupBuy.getId(),userGroupBuy.getEndDate());
                }
            }
        } else if (minJoinNum == (count+1)){
            //2.成团成功
            row = userGroupBuyRecordMapper.completeGroupBuy(userGroupBuyRecord.getId());
            completeGroupBuy = row > 0;
        }else{
            //3.参团成功
            UserGroupBuyRecord record = new UserGroupBuyRecord();
            record.setId(userGroupBuyRecord.getId());
            record.setStatus(JOINED_PAY.getCode());//支付成功
            record.setLockStatus(LOCK.getCode());//加锁中
            record.setRemark(JOINED_PAY.getMsg()+"_"+LOCK.getMsg());//支付成功
            record.setUpdateTime(new Date());
            row = userGroupBuyRecordMapper.updateByPrimaryKeySelective(record);
        }
        if (row > 0){
            int hasJoinedNum = userGroupBuyRecordMapper.countJoinedTimesExcludeCurrentUser(userGroupBuy.getId(),null);
            //发送微信消息
            sendWechatMsg(activityGroupBuy,userGroupBuy,userGroupBuyRecord,hasJoinedNum,createGroupBuy);
            GroupBuyUtil.clearGroupBuyJoinedUsers(userGroupBuy.getId());
            GroupBuyUtil.clearUserJoinedGroupBuy(userGroupBuy.getActivityGroupBuyId(),userGroupBuyRecord.getUserId());
            //拼团成功后的触发操作
            handleTrigerAfterCompleteGroupBuy(completeGroupBuy,userGroupBuy.getId());
            return ResponseResult.buildSuccessResponse(completeGroupBuy);
        }
        log.error("updateStatusAfterPaySuccess 支付成功状态修改失败，userGroupBuyRecordId -> {}, userId -> {}",userGroupBuyRecord.getId(),userGroupBuyRecord.getUserId());
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
                    //1.将参团用户状态置为"老用户"
                    List<Long> userIdList = Lists.transform(joinUserList, new Function<JoinUser, Long>() {
                        @Nullable
                        @Override
                        public Long apply(@Nullable JoinUser input) {
                            return input.getUserId();
                        }
                    });
                    groupBuyProducer.updateUserStatusAfterCompleteGroupBuy(GroupBuyUpdateUserMessage.builder().userIdList(userIdList).build());

                    //2.创建订单分享券活动
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
            if (createGroupBuy){//开团成功
                msgMessage.setJoinUserList(Lists.newArrayList(joinUsers.get(0)));
                msgMessage.setScene(GrouyBuyEnum.SEND_MSG_SCENE.CREATE_GROUPBUY_NOTIFY);
                groupBuyProducer.wechatMsgNofitMQ(msgMessage);
            }else if (minJoinNum > 1){
                if (minJoinNum == hasJoinedNum){//成团
                    msgMessage.setJoinUserList(joinUsers);
                    //成团
                    msgMessage.setScene(GrouyBuyEnum.SEND_MSG_SCENE.COMPLETE_NOTIFY);
                    groupBuyProducer.wechatMsgNofitMQ(msgMessage);
                }else if (minJoinNum == 3 && minJoinNum == hasJoinedNum + 1){//发给团长
                    //3人团，提示倒数第二个参团进度，发给团长
                    msgMessage.setJoinUserList(Lists.newArrayList(joinUsers.get(0)));
                    msgMessage.setScene(GrouyBuyEnum.SEND_MSG_SCENE.PROCESS_NOTIFY);
                    msgMessage.setJoinedNum(hasJoinedNum-1);
                    msgMessage.setSeconds(Seconds.secondsBetween(DateTime.now(),new DateTime(userGroupBuy.getEndDate())).getSeconds());
                    groupBuyProducer.wechatMsgNofitMQ(msgMessage);
                    //参团通知
                    JoinUser joinUser = JoinUser.builder().userId(record.getUserId()).openId(record.getOpenId()).orderId(record.getOrderId()).build();
                    msgMessage.setJoinUserList(Lists.newArrayList(joinUser));
                    msgMessage.setScene(GrouyBuyEnum.SEND_MSG_SCENE.CREATE_GROUPBUY_NOTIFY);
                    groupBuyProducer.wechatMsgNofitMQ(msgMessage);
                }else if (minJoinNum > 3){
                    if (hasJoinedNum == 2 || (minJoinNum == hasJoinedNum + 1)){//发给团长
                        //3人以上团，提示第二个or倒数第二个参团进度，发给团长
                        msgMessage.setJoinUserList(Lists.newArrayList(joinUsers.get(0)));
                        msgMessage.setScene(GrouyBuyEnum.SEND_MSG_SCENE.PROCESS_NOTIFY);
                        msgMessage.setJoinedNum(hasJoinedNum-1);
                        msgMessage.setSeconds(Seconds.secondsBetween(DateTime.now(),new DateTime(userGroupBuy.getEndDate())).getSeconds());
                        groupBuyProducer.wechatMsgNofitMQ(msgMessage);
                    }
                    //参团通知
                    JoinUser joinUser = JoinUser.builder().userId(record.getUserId()).openId(record.getOpenId()).orderId(record.getOrderId()).build();
                    msgMessage.setJoinUserList(Lists.newArrayList(joinUser));
                    msgMessage.setScene(GrouyBuyEnum.SEND_MSG_SCENE.CREATE_GROUPBUY_NOTIFY);
                    groupBuyProducer.wechatMsgNofitMQ(msgMessage);
                }else {//参团通知
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
     * 创建拼团实例
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
                log.info("参团人数不能小于1");
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
                    userGroupBuyRecord.setJoinTime(now.toDate());//参团时间
                    userGroupBuyRecord.setLockTime(now.toDate());//加锁时间
                    userGroupBuyRecord.setLockStatus(LOCK.getCode());//加锁
                    userGroupBuyRecord.setOrderId(groupBuyRequest.getOrderId());//订单id
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
     * 更新参团拼团实例
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
            userGroupBuyRecord.setLockTime(now);//加锁时间
            userGroupBuyRecord.setLockStatus(LOCK.getCode());//加锁
            userGroupBuyRecord.setOrderId(groupBuyRequest.getOrderId());//订单id
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
     * 参团拼团实例
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
            userGroupBuyRecord.setJoinTime(now);//参团时间
            userGroupBuyRecord.setLockTime(now);//加锁时间
            userGroupBuyRecord.setLockStatus(LOCK.getCode());//加锁
            userGroupBuyRecord.setOrderId(groupBuyRequest.getOrderId());//订单id
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
     * 根据id查询拼团活动
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
                GroupBuyUtil.setActivityGroupBuy(activityGroupBuyId, activityGroupBuy, seconds - 30, TimeUnit.SECONDS);//缓存提前30s失效，防止边界定制器操作导致缓存和db数据不一致
            }
            return activityGroupBuy;
        }
        activityGroupBuy = new ActivityGroupBuy();
        //防止缓存穿透
        GroupBuyUtil.setActivityGroupBuy(activityGroupBuyId, activityGroupBuy, 5, TimeUnit.SECONDS);
        return activityGroupBuy;
    }


    /**
     * 活动id
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
//        //查询是否参加当前拼团
//        userGroupBuy = userGroupBuyRecordMapper.queryJoinedGroupBuy(groupBuyId, userId);
//        if (userGroupBuy != null) {
//            GroupBuyUtil.cacheUserJoinedGroupBuy(userGroupBuy, groupBuyId, userId, 24 * 60 * 60, TimeUnit.SECONDS);
//            return userGroupBuy;
//        }
//        return userGroupBuy;
    }


    /**
     * 查询当前生效的拼团活动
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
        //防止缓存穿透，减少DB鸭梨
        GroupBuyUtil.setCurrentActivityGroupBuy(activityGroupBuy, 5, TimeUnit.SECONDS);
        return activityGroupBuy;
    }

    /**
     * 查询自己参与的拼团信息
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
            //开团or开团成功，等待支付状态，count_down_latch 加锁3秒
            GroupBuyUtil.waitBeforePayCallback(orderId);
        }

        //查询当前的拼团活动
        ActivityGroupBuy activityGroupBuy = getActivityGroupBuy(activityGroupBuyId);
        if (activityGroupBuy == null || activityGroupBuy.getId() == null){
            return ResponseResult.buildFailResponse(ACTIVITY_GROUP_BUY_NOT_EXIST.code(),ACTIVITY_GROUP_BUY_NOT_EXIST.message());
        }

        Date now = new Date();
        //查询参加过该团
        UserGroupBuyDto userGroupBuy = queryJoinedGroupBuy(activityGroupBuyId, userId,now);

        //拼团活动倒计时时间
        long seconds = Seconds.secondsBetween(new DateTime(now), new DateTime(activityGroupBuy.getEndDate())).getSeconds();
        seconds = seconds - 1;//防止临界值，提前一秒结束，seconds小于等于1s时，活动状态置为结束
        if (userGroupBuy == null) {//未参加拼团
            if (seconds > 0) {
                //拼团活动进行中
                log.info("拼团活动进行中，activityGroupBuyId -> {}", activityGroupBuyId);
            } else {
                //拼团活动已结束
                log.info("拼团活动已结束，activityGroupBuyId -> {}", activityGroupBuyId);
            }
            userGroupBuy = BeanMapper.map(activityGroupBuy, UserGroupBuyDto.class);
            return ResponseResult.buildSuccessResponse(GroupBuyResponse.builder().activityGroupBuyExpireTime((int)seconds).joined(false).activityGroupBuyisOver(seconds<=0).companyId(userGroupBuy.getCompanyId()).userGroupBuyDetail(userGroupBuy).build());
        }
        //已参团用户列表
        List<JoinUser> joinUserList = fetchGroupBuyJoinedUsers(userGroupBuy.getId());
        Integer leftNum = userGroupBuy.getMinJoinNum() - joinUserList.size();
        return ResponseResult.buildSuccessResponse(GroupBuyResponse.builder().activityGroupBuyExpireTime(seconds>0?(int)seconds:0).joined(userGroupBuy != null).leftNum(leftNum < 0 ? 0 : leftNum).activityGroupBuyisOver(seconds<=0).companyId(userGroupBuy.getCompanyId()).userGroupBuyDetail(userGroupBuy).joinUserList(joinUserList).build());
    }


}