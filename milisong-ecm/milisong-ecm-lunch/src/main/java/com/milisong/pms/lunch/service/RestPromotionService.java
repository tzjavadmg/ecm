package com.milisong.pms.lunch.service;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.milisong.pms.prom.api.ActivityService;
import com.milisong.pms.prom.api.ActivityUserRedPacketService;
import com.milisong.pms.prom.api.UserRedPacketService;
import com.milisong.pms.prom.dto.*;
import com.milisong.pms.prom.enums.RedPacketType;
import com.milisong.ucs.api.UserService;
import com.milisong.ucs.dto.UserDto;
import com.milisong.ucs.enums.BusinessLineEnum;
import com.milisong.ucs.enums.UserSourceEnum;
import com.milisong.ucs.sdk.security.UserContext;
import com.milisong.ucs.sdk.security.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.milisong.pms.prom.enums.PromotionResponseCode.CREATE_MULTI_SHARE_RED_PACKET_EXCEPTION;
import static com.milisong.pms.prom.enums.RedPacketLaunchType.ORDER_SHARE;

/**
 * @author sailor wang
 * @date 2018/9/14 下午7:47
 * @description
 */
@Slf4j
@Service
public class RestPromotionService {

    @Autowired
    ActivityUserRedPacketService activityUserRedPacketService;

    @Autowired
    private UserRedPacketService userRedPacketService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserService userService;


    /**
     * 创建砍红包实例
     *
     * @param userRequest
     * @return
     */
//    public ResponseResult<UserRedPacketResponse> createUserCutRedPacket(UserRequest userRequest) {
//        if (userRequest != null && StringUtils.isNotBlank(userRequest.getNickName()) && StringUtils.isNotBlank(userRequest.getHeadPortraitUrl())) {
//            updateUser(userRequest);
//        }
//        UserSession userSession = UserContext.getCurrentUser();
//        UserRedPacketRequest redPacketRequest = UserRedPacketRequest.builder().userId(userSession.getId()).openid(userSession.getOpenId()).build();
//        return activityUserRedPacketService.createUserCutRedPacket(redPacketRequest);
//    }

    /**
     * 砍红包
     *
     * @param userRedPacketId
     * @param userRequest
     * @return
     */
//    public ResponseResult<UserRedPacketResponse> hackUserCutRedPacket(Long userRedPacketId, UserRequest userRequest) {
//        if (userRequest != null && StringUtils.isNotBlank(userRequest.getNickName()) && StringUtils.isNotBlank(userRequest.getHeadPortraitUrl())) {
//            updateUser(userRequest);
//        }
//        UserSession userSession = UserContext.getCurrentUser();
//        UserRedPacketRequest redPacketRequest = UserRedPacketRequest.builder().userId(userSession.getId()).openid(userSession.getOpenId()).activityUserRedPacketId(userRedPacketId).build();
//        return activityUserRedPacketService.hackUserCutRedPacket(redPacketRequest);
//    }

    /**
     * 创建多人分享红包实例
     *
     * @param orderid
     * @return
     */
    public ResponseResult<UserRedPacketResponse> createMultiShareRedPacket(Long orderid) {
        try {
            UserSession userSession = UserContext.getCurrentUser();
            UserRedPacketRequest redPacketRequest = UserRedPacketRequest.builder().userId(userSession.getId()).openid(userSession.getOpenId())
                    .nickName(userSession.getNickName()).headPortraitUrl(userSession.getHeadPortraitUrl()).launchType(ORDER_SHARE).launchId(orderid).build();
            return activityUserRedPacketService.createMultiShareRedPacket(redPacketRequest);
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildFailResponse(CREATE_MULTI_SHARE_RED_PACKET_EXCEPTION.code(), CREATE_MULTI_SHARE_RED_PACKET_EXCEPTION.message());
    }

    /**
     * 领取多人分享红包
     *
     * @param orderid
     * @return
     */
    public ResponseResult<UserRedPacketResponse> recevieMultiShareRedPacket(Long orderid) {
        UserSession userSession = UserContext.getCurrentUser();
        UserRedPacketRequest redPacketRequest = UserRedPacketRequest.builder().userId(userSession.getId()).openid(userSession.getOpenId()).isNewUser(userSession.getIsNew()).nickName(userSession.getNickName()).
                headPortraitUrl(userSession.getHeadPortraitUrl()).launchType(ORDER_SHARE).launchId(orderid).build();
        ResponseResult<UserRedPacketResponse> response = activityUserRedPacketService.recevieMultiShareRedPacket(redPacketRequest);
        UserRedPacketResponse userRedPacket = response.getData();
        Byte isNew = userSession.getIsNew();
        Byte receivedNewRedPacket = userSession.getReceivedNewRedPacket();
        if (userRedPacket != null && (isNew == null || isNew.intValue() == 0) && (receivedNewRedPacket == null || receivedNewRedPacket.intValue() == 0)) {//新用户领取新人红包
            if (userRedPacket.getNewRedPacket() != null) {
                updateReceivedNewRedPacketStatus(userSession.getId(), userSession.getOpenId());
            }
        }
        return response;
    }

    // 修改用户领取红包状态
    private void updateReceivedNewRedPacketStatus(Long userId, String openid) {
        UserDto user = new UserDto();
        user.setId(userId);
        user.setOpenId(openid);
        user.setReceivedNewRedPacket((byte) 1);
        user.setUpdateTime(new Date());
        user.setBusinessLine(BusinessLineEnum.LUNCH.getCode());
        userService.updateUser(user);
        UserContext.freshUser(openid, UserSourceEnum.MINI_APP.getCode(),BusinessLineEnum.LUNCH.getCode(), "receivedNewRedPacket", "1");
    }

    /**
     * 可用红包数量
     *
     * @return
     */
    public ResponseResult<Integer> usableRedpacketCount() {
        return userRedPacketService.usableUserRedPacketCount(UserContext.getCurrentUser().getId());
    }

    /**
     * 可用红包列表
     *
     * @return
     */
    public ResponseResult<UserRedPacketResponse> usableRedpackets() {
        log.info("usableRedpackets -> {}",UserContext.getCurrentUser().getId());
        return userRedPacketService.usableUserRedPackets(UserContext.getCurrentUser().getId());
    }

    public ResponseResult<ActivityRedPacketDto> multiRedpacketInfo() {
        ActivityRedPacketQueryParam queryParam = new ActivityRedPacketQueryParam();
        queryParam.setType(RedPacketType.SHARE_REDPACKET);
        queryParam.setBusinessLine(BusinessLineEnum.LUNCH.getCode());
        return activityService.currentActivityRedPacket(queryParam);
    }

//    public ResponseResult<UserDto> updateUser(UserRequest userRequest) {
////        UserDto userDto = BeanMapper.map(userRequest, UserDto.class);
////        userDto.setId(UserContext.getCurrentUser().getId());
////        userDto.setOpenId(UserContext.getCurrentUser().getOpenId());
////        userDto.setWxSessionKey(UserContext.getCurrentUser().getWxSessionKey());
////        userClient.updateUser(userDto);
////        return null;
////    }

    /**
     * 我的红包列表
     * @return
     */
    public ResponseResult<Pagination<UserRedPacketDto>> myRedpacket(ActivityRedPacketQueryParam queryParam) {
        UserSession userSession = UserContext.loadCurrentUser();
        queryParam.setUserId(userSession.getId());
        return userRedPacketService.userRedPacketList(queryParam);
    }

    /**
     * 登录页弹层
     *
     * @return
     */
    public ResponseResult<UserRedPacketGuide> redPacketToast() {
        ResponseResult<ToastRedPacketConfig> result = userRedPacketService.toastRedPacketConfig();
        log.info("红包弹层 result -> {}",result);
        try {
            if (result.isSuccess()){
                ToastRedPacketConfig config = result.getData();
                Long userId = UserContext.loadCurrentUser().getId();
                Integer toastTimes = config.getToastPerDay();
                // 弹层次数
                String key = "red_packet_toast:" + userId;
                String timesStr = RedisCache.get(key);
                log.info("弹层次数 timesStr -> {}",timesStr);
                if (StringUtils.isBlank(timesStr)){
                    DateTime nowTime = new DateTime();
                    DateTime endOfDay =  nowTime.millisOfDay().withMaximumValue();
                    int seconds = Seconds.secondsBetween(DateTime.now(), endOfDay).getSeconds();
                    RedisCache.setEx(key, "1", Long.valueOf(seconds), TimeUnit.SECONDS);
                    return ResponseResult.buildSuccessResponse(initUserRedPacketGuide(config));
                }else {
                    Integer times = Integer.valueOf(timesStr);
                    if (toastTimes > times){
                        DateTime nowTime = new DateTime();
                        DateTime endOfDay =  nowTime.millisOfDay().withMaximumValue();
                        int seconds = Seconds.secondsBetween(DateTime.now(), endOfDay).getSeconds();
                        RedisCache.setEx(key, Integer.toString(times+1), Long.valueOf(seconds), TimeUnit.SECONDS);
                        return ResponseResult.buildSuccessResponse(initUserRedPacketGuide(config));
                    }
                }
            }
        } catch (Exception e) {
            log.error("",e);
        }
        return ResponseResult.buildSuccessResponse();
    }

    /**
     * 活动任务，每分钟执行一次
     */
    public void scanRedPacketActivityStatus() {
        log.info("活动红包任务");
        activityService.scanActivityStatus(BusinessLineEnum.LUNCH.getCode());
    }

    /**
     * 用户活动状态，每分钟执行一次
     */
    public void scanUserRedPacket() {
        log.info("用户活动任务");
        userRedPacketService.scanUserRedPacket();
    }

    private UserRedPacketGuide initUserRedPacketGuide(ToastRedPacketConfig config){
        UserSession userSession = UserContext.loadCurrentUser();
        ResponseResult<List<UserRedPacketDto>> result = userRedPacketService.toastUserRedPacket(userSession.getId(),config.getShowNum());
        log.info("用户红包个数 result -> {}",result.getData().size());
        if (result.isSuccess() && CollectionUtils.isNotEmpty(result.getData())){
            UserRedPacketGuide guide = new UserRedPacketGuide();
            guide.setBgImg(config.getBgImg());
            guide.setSubTitle(config.getSubTitle());
            guide.setRedPacketList(result.getData());
            return guide;
        }
        return null;
    }

    public ResponseResult<Boolean> sendRedPacket() {
        return userRedPacketService.sendRedPacket();
    }

    public ResponseResult<String> batchSendRedPacket(BatchSendRedPacketParam sendRedPacketParam){
        return userRedPacketService.batchSendRedPacket(sendRedPacketParam);
    }

    public ResponseResult<FullReduce> fullReduce() {
        return userRedPacketService.fullReduceConfig();
    }
}