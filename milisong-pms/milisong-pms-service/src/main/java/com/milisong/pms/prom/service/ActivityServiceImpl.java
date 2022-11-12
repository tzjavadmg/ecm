package com.milisong.pms.prom.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.google.common.collect.Lists;
import com.milisong.pms.prom.api.ActivityService;
import com.milisong.pms.prom.domain.Activity;
import com.milisong.pms.prom.domain.ActivityRedPacket;
import com.milisong.pms.prom.dto.*;
import com.milisong.pms.prom.enums.ActivityStatus;
import com.milisong.pms.prom.enums.CouponEnum;
import com.milisong.pms.prom.enums.RedPacketType;
import com.milisong.pms.prom.mapper.ActivityMapper;
import com.milisong.pms.prom.mapper.ActivityRedPacketMapper;
import com.milisong.pms.prom.util.ActivityCacheUtil;
import com.milisong.ucs.enums.BusinessLineEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static com.milisong.pms.prom.enums.PromotionResponseCode.*;
import static com.milisong.pms.prom.enums.RedPacketType.*;

/**
 * 活动接口实现
 * @author sailor wang
 * @date 2018/9/13 下午1:45
 * @description
 */
@Slf4j
@Service
@RestController
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    ActivityRedPacketMapper activityRedPacketMapper;

    /**
     * 定时扫描活动状态：未生效、生效
     *
     * @return
     */
    @Override
    @PostMapping(value = "/v1/ActivityService/scanActivityStatus")
    public ResponseResult scanActivityStatus(@RequestParam("businessLine")Byte businessLine) {
        try {
            Date currentDate = new Date();
            ActivityQueryParam queryParam = new ActivityQueryParam();
            queryParam.setType(RedPacketType.CUT_REDPACKET);
            queryParam.setBusinessLine(businessLine);
            ResponseResult<ActivityRedPacketDto> currentActivityRedPacket = currentActivityRedPacket(queryParam);
            if (currentActivityRedPacket.getData() == null){
                activityMapper.onlineRedPacketActivity(currentDate);
            }
            activityMapper.offlineRedPacketActivity(currentDate);
            return ResponseResult.buildSuccessResponse();
        } catch (Exception e) {
            log.error("定时扫描活动状态异常",e);
            return ResponseResult.buildFailResponse();
        }
    }

    /**
     * 当前红包
     * @return
     */
    @Override
    @PostMapping(value = "/v1/ActivityService/currentActivityRedPacket")
    public ResponseResult<ActivityRedPacketDto> currentActivityRedPacket(@RequestBody ActivityQueryParam queryParam){
        try {
            if (BusinessLineEnum.BREAKFAST.getCode().equals(queryParam.getBusinessLine())){
                return ResponseResult.buildFailResponse();//TODO 早餐还未开通
            }else{
                return lunchActivityRedPacket(queryParam);
            }
        } catch (Exception e) {
            log.error("当前红包",e);
        }
        return ResponseResult.buildFailResponse();
    }

    /**
     * 保存红包活动
     * @param activityRedPacket
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @PostMapping(value = "/v1/ActivityService/saveOrUpdateActivityRedPacket")
    public ResponseResult<Boolean> saveOrUpdateActivityRedPacket(@RequestBody ActivityRedPacketDto activityRedPacket) {
        ResponseResult<Boolean> result = checkActivityRedPacketFields(activityRedPacket);
        if (result != null ){
            return result;
        }
        Long activityId = activityRedPacket.getId();
        if (activityId == null){
            return saveActivityRedPacket(activityRedPacket);
        }
        return updateActivityRedPacket(activityRedPacket);
    }

    private ResponseResult<ActivityRedPacketDto> lunchActivityRedPacket(ActivityQueryParam queryParam){
        if (NEW_REDPACKET.equals(queryParam.getType())){
            // 新人红包，从redis获取
            String jsonStr = RedisCache.get("activity_red_packet:2");
            log.info("新人红包数据 jsonStr -> {}",jsonStr);
            if (StringUtils.isEmpty(jsonStr)){
                log.info("新人红包缓存数据为空！！！");
                return ResponseResult.buildFailResponse(NEW_RED_PACKET_REDIS_EMPTY.code(),NEW_RED_PACKET_REDIS_EMPTY.message());
            }
            NewRedPacketConfig newRedPacket = JSONObject.parseObject(jsonStr, NewRedPacketConfig.class);
            Date now = new Date();
            ActivityRedPacketDto activityRedPacketDto = BeanMapper.map(newRedPacket,ActivityRedPacketDto.class);
            if (now.after(newRedPacket.getStartDate()) && now.before(newRedPacket.getEndDate())){
                return ResponseResult.buildSuccessResponse(activityRedPacketDto);
            }
            return ResponseResult.buildFailResponse(RED_PACKET_EXPIRED.code(),RED_PACKET_EXPIRED.message(),activityRedPacketDto);
        }else if (SHARE_REDPACKET.equals(queryParam.getType())){
            // 多人分享红包
            String jsonStr = RedisCache.get("activity_red_packet:1");
            log.info("多人分享红包数据 jsonStr -> {}",jsonStr);
            if (StringUtils.isEmpty(jsonStr)){
                log.info("多人分享红包数据为空！！！");
                return ResponseResult.buildFailResponse(MULTI_SHARE_RED_PACKET_REDIS_EMPTY.code(),MULTI_SHARE_RED_PACKET_REDIS_EMPTY.message());
            }
            MultiShareRedPacketConfig multiShareRedPacket = JSONObject.parseObject(jsonStr, MultiShareRedPacketConfig.class);
            log.info("multiShareRedPacket -> {}",multiShareRedPacket);
            Date now = new Date();
            ActivityRedPacketDto activityRedPacketDto = BeanMapper.map(multiShareRedPacket,ActivityRedPacketDto.class);
            log.info("activityRedPacketDto -> {}",activityRedPacketDto);
            if (now.after(multiShareRedPacket.getStartDate()) && now.before(multiShareRedPacket.getEndDate())){
                return ResponseResult.buildSuccessResponse(activityRedPacketDto);
            }
            return ResponseResult.buildFailResponse(RED_PACKET_EXPIRED.code(),RED_PACKET_EXPIRED.message(),activityRedPacketDto);
        }else if (ACTIVIE_REDPACKET.equals(queryParam.getType())){
            // 促活红包
            String jsonStr = RedisCache.get("activity_red_packet:4");
            log.info("促活红包数据 jsonStr -> {}",jsonStr);
            if (StringUtils.isEmpty(jsonStr)){
                log.info("促活红包数据为空！！！");
                return ResponseResult.buildFailResponse(ACTIVE_RED_PACKET_NOT_EXIST.code(),ACTIVE_RED_PACKET_NOT_EXIST.message());
            }
            SendRedPacketConfig sendRedPacketConfig = JSONObject.parseObject(jsonStr, SendRedPacketConfig.class);
            Date now = new Date();

            ActivityRedPacketDto activityRedPacketDto = BeanMapper.map(sendRedPacketConfig,ActivityRedPacketDto.class);
            if (now.after(sendRedPacketConfig.getStartDate()) && now.before(sendRedPacketConfig.getEndDate())){
                return ResponseResult.buildSuccessResponse(activityRedPacketDto);
            }
            return ResponseResult.buildFailResponse(RED_PACKET_EXPIRED.code(),RED_PACKET_EXPIRED.message(),activityRedPacketDto);
        }
        ActivityRedPacketDto activityRedPacket = ActivityCacheUtil.getCurrentRedPacket();
        if (activityRedPacket != null){
            return ResponseResult.buildSuccessResponse(activityRedPacket);
        }
        activityRedPacket = activityMapper.currentActivityRedPacket(new Date(),queryParam.getType().getCode());
        if (activityRedPacket != null){
            ActivityCacheUtil.cacheCurrentRedPacket(activityRedPacket);
        }
        return ResponseResult.buildSuccessResponse(activityRedPacket);
    }

    private ResponseResult<Boolean> updateActivityRedPacket(ActivityRedPacketDto activityRedPacket){
        Long activityId = activityRedPacket.getId();
        Date startDate = activityRedPacket.getStartDate();
        Date endDate = activityRedPacket.getEndDate();
        int count = activityMapper.conflictActivityRedPacket(activityId,startDate);
        if (count > 0){
            return ResponseResult.buildFailResponse(EXIST_CONFLICT_ACTIVITY_RED_PACKET.code(),EXIST_CONFLICT_ACTIVITY_RED_PACKET.message());
        }
        Byte activityStatus = activityRedPacket.getStatus();// 0 保存、1 保存并发布
        if (ActivityStatus.NO_EFFECTIVE.getCode().equals(Integer.valueOf(activityStatus))){//保存并发布
            Date now = new Date();
            if (now.after(startDate) && now.before(endDate)){// 时间已生效
                activityRedPacket.setStatus(ActivityStatus.EFFECTIVE.getCode().byteValue());
            }
        }
        Activity activity = BeanMapper.map(activityRedPacket,Activity.class);
        activity.setUpdateTime(new Date());
        int row = activityMapper.updateByPrimaryKey(activity);
        if (row>0){
            ActivityRedPacket actRedPacket = BeanMapper.map(activityRedPacket,ActivityRedPacket.class);
            actRedPacket.setActivityId(activityRedPacket.getId());
            actRedPacket.setUpdateTime(new Date());
            row = activityRedPacketMapper.updateByActivityId(actRedPacket);
            return ResponseResult.buildSuccessResponse(row>0);
        }
        return ResponseResult.buildFailResponse();
    }

    private ResponseResult<Boolean> saveActivityRedPacket(ActivityRedPacketDto activityRedPacket){
        Long activityId = activityRedPacket.getId();
        Date startDate = activityRedPacket.getStartDate();
        Date endDate = activityRedPacket.getEndDate();
        int count = activityMapper.conflictActivityRedPacket(activityId,startDate);
        if (count > 0){
            return ResponseResult.buildFailResponse(EXIST_CONFLICT_ACTIVITY_RED_PACKET.code(),EXIST_CONFLICT_ACTIVITY_RED_PACKET.message());
        }

        Byte activityStatus = activityRedPacket.getStatus();// 0 保存、1 保存并发布
        if (ActivityStatus.NO_EFFECTIVE.getCode().equals(Integer.valueOf(activityStatus))){//保存并发布
            Date now = new Date();
            if (now.after(startDate) && now.before(endDate)){// 时间已生效
                activityRedPacket.setStatus(ActivityStatus.EFFECTIVE.getCode().byteValue());
            }
        }

        // 保存活动
        activityId = saveActivity(activityRedPacket);
        if (activityId > 0){
            // 保存活动红包数据
            int row = saveActivityRedPacket(activityId,activityRedPacket);
            return ResponseResult.buildSuccessResponse(row>0);
        }
        return ResponseResult.buildFailResponse();
    }

    /**
     * 查询红包活动列表
     * @param queryParam
     * @return
     */
    @Override
    @PostMapping(value = "/v1/ActivityService/queryActivityRedPacketList")
    public ResponseResult<Pagination<ActivityRedPacketDto>> queryActivityRedPacketList(@RequestBody ActivityQueryParam queryParam) {
        int startRow = 1;
        int pageSize = 10;
        Byte buzLine = queryParam.getBusinessLine();
        if (queryParam != null){
            pageSize = queryParam.getPageSize()!=null?queryParam.getPageSize():10;
            startRow = queryParam.getPageNo()==null?0:(queryParam.getPageNo() - 1)*pageSize;
        }
        try {
            int totalCount = activityMapper.countActivityRedPacket(buzLine);
            List<ActivityRedPacketDto> list = Lists.newArrayList();
            if (totalCount > 0){
                list = activityMapper.queryActivityRedPacketList(startRow,pageSize,buzLine);
            }
            Pagination pagination = new Pagination(queryParam.getPageNo(),pageSize,totalCount,list);
            return ResponseResult.buildSuccessResponse(pagination);
        } catch (Exception e) {
            log.error("",e);
            return ResponseResult.buildFailResponse(QUERY_ACTIVITY_RED_PACKET_EXCEPTION.code(),QUERY_ACTIVITY_RED_PACKET_EXCEPTION.message());
        }
    }

    @Override
    public ResponseResult<MultiBreakfastCouponConfig> currentActivityCouponConfig(@RequestBody ActivityQueryParam queryParam) {
        if (CouponEnum.TYPE.DISCOUNT.equals(queryParam.getCouponType())){
            // 早餐多人分享券
            String jsonStr = RedisCache.get("breakfast_coupon:multi");
            if (StringUtils.isEmpty(jsonStr)){
                log.info("多人分享券数据为空！！！");
                return ResponseResult.buildFailResponse(MULTI_BREAKFAST_COUPON_CONFIG_NOT_EXISTS.code(), MULTI_BREAKFAST_COUPON_CONFIG_NOT_EXISTS.message());
            }
            MultiBreakfastCouponConfig couponConfig = JSONObject.parseObject(jsonStr, MultiBreakfastCouponConfig.class);
            Date now = new Date();
            if (now.after(couponConfig.getStartDate()) && now.before(couponConfig.getEndDate())){
                return ResponseResult.buildSuccessResponse(couponConfig);
            }
            return ResponseResult.buildFailResponse(BREAKFAST_COUPON_EXPIRE.code(),BREAKFAST_COUPON_EXPIRE.message(),couponConfig);
        }
        return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
    }

    private Long saveActivity(ActivityRedPacketDto activityRedPacket){
        Activity activity = BeanMapper.map(activityRedPacket,Activity.class);
        activity.setId(IdGenerator.nextId());
        activity.setUserLimit(1);// 用户限制次数默认为1
//        activity.setBusinessLine(activityRedPacket.getBusinessLine());
        activity.setIsDelete((byte) 0);
        activity.setCreateTime(new Date());
        activity.setUpdateTime(new Date());
        int row = activityMapper.insertSelective(activity);
        if (row > 0){
            return activity.getId();
        }
        return 0L;
    }

    private int saveActivityRedPacket(Long activityId,ActivityRedPacketDto activityRedPacket){
        ActivityRedPacket actRedPacket = BeanMapper.map(activityRedPacket,ActivityRedPacket.class);
        actRedPacket.setId(IdGenerator.nextId());
        actRedPacket.setActivityId(activityId);
        actRedPacket.setIsDelete((byte) 0);
        actRedPacket.setBusinessLine(activityRedPacket.getBusinessLine());
        actRedPacket.setCreateTime(new Date());
        actRedPacket.setUpdateTime(new Date());

        return activityRedPacketMapper.insertSelective(actRedPacket);
    }

    private ResponseResult<Boolean> checkActivityRedPacketFields(ActivityRedPacketDto activity){
        if (activity == null){
            log.info("创建红包活动参数为空 activity -> {}",activity);
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }

        if (activity.getType() == null){// 活动类型为空
            log.info("创建红包活动参数为空 activity -> {}",activity);
            return ResponseResult.buildFailResponse(ACTIVITY_TYPE_IS_EMPTY.code(),ACTIVITY_TYPE_IS_EMPTY.message());
        }

        if (StringUtils.isEmpty(activity.getName()) || activity.getStartDate()== null || activity.getEndDate() == null
                || activity.getAmount() == null || activity.getPlayers() == null || activity.getIsShare() == null
                || activity.getClickLimit() == null){
            log.info("创建红包活动参数为空 activity -> {}",activity);
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }

        if (!ActivityStatus.DRAFT.getCode().equals(Integer.valueOf(activity.getStatus())) && !ActivityStatus.NO_EFFECTIVE.getCode().equals(Integer.valueOf(activity.getStatus()))){
            return ResponseResult.buildFailResponse(ACTIVITY_STATUS_INVALID.code(),ACTIVITY_STATUS_INVALID.message());
        }

        return null;
    }
}