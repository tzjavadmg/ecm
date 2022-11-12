package com.milisong.pms.prom.service;

import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.milisong.pms.prom.api.ActivityScanQrcodeService;
import com.milisong.pms.prom.api.CouponService;
import com.milisong.pms.prom.api.MarketingQrcodeService;
import com.milisong.pms.prom.domain.ActivityScanQrcode;
import com.milisong.pms.prom.domain.ActivityScanQrcodeDetail;
import com.milisong.pms.prom.dto.CouponDto;
import com.milisong.pms.prom.dto.MarketingQrcodeDto;
import com.milisong.pms.prom.dto.UserCouponDto;
import com.milisong.pms.prom.dto.scanqrcode.ActivityScanQrcodeDto;
import com.milisong.pms.prom.dto.scanqrcode.ScanQrcodeRequest;
import com.milisong.pms.prom.dto.scanqrcode.ScanQrcodeResponse;
import com.milisong.pms.prom.enums.ScanQrcodeActivityEnum;
import com.milisong.pms.prom.mapper.ActivityScanQrcodeDetailMapper;
import com.milisong.pms.prom.mapper.ActivityScanQrcodeMapper;
import com.milisong.pms.prom.mapper.UserCouponMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.milisong.pms.prom.enums.PromotionResponseCode.*;
import static com.milisong.pms.prom.enums.UserActivityType.SCAN_QRCODE;

/**
 * 用户扫码送礼品活动
 *
 * @author sailor wang
 * @date 2019/5/9 9:03 PM
 * @description
 */
@Slf4j
@Service
@RestController
public class ActivityScanQrcodeServiceImpl implements ActivityScanQrcodeService {

    @Autowired
    private MarketingQrcodeService marketingQrcodeService;

    @Autowired
    private ActivityScanQrcodeMapper activityScanQrcodeMapper;

    @Autowired
    private ActivityScanQrcodeDetailMapper activityScanQrcodeDetailMapper;

    @Autowired
    private CouponService couponService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private UserCouponMapper userCouponMapper;

    /**
     * 创建扫码送礼物活动
     *
     * @param scanQrcodeRequest
     * @return
     */
    @Override
    public ResponseResult<Boolean> createScanQrcodeActivity(@RequestBody ScanQrcodeRequest scanQrcodeRequest) {
        log.info("创建扫码送礼物活动 入参: scanQrcodeRequest -> {}", scanQrcodeRequest);
        if (invalidScanQrcodeRequestParam(scanQrcodeRequest)) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        //创建二维码活动
        Boolean insertSuccess = doCreateScanQrcodeActivity(scanQrcodeRequest);
        return ResponseResult.buildSuccessResponse(insertSuccess);
    }

    private Boolean invalidScanQrcodeRequestParam(ScanQrcodeRequest scanQrcodeRequest) {
        if (scanQrcodeRequest.getBusinessLine() == null || StringUtils.isBlank(scanQrcodeRequest.getActivityName())
                || StringUtils.isBlank(scanQrcodeRequest.getGiftIds()) || StringUtils.isBlank(scanQrcodeRequest.getBgImg())
                || scanQrcodeRequest.getStartDate() == null || scanQrcodeRequest.getEndDate() == null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private Boolean doCreateScanQrcodeActivity(ScanQrcodeRequest scanQrcodeRequest) {
        try {
            Long actId = IdGenerator.nextId();
            ActivityScanQrcode activityScanQrcode = new ActivityScanQrcode();
            activityScanQrcode.setId(actId);
            activityScanQrcode.setName(scanQrcodeRequest.getActivityName());
            activityScanQrcode.setType(SCAN_QRCODE.getCode());
            activityScanQrcode.setGiftType(ScanQrcodeActivityEnum.GIFT_TYPE.BF_COUPON.getValue());
            activityScanQrcode.setGiftIds(scanQrcodeRequest.getGiftIds());
            activityScanQrcode.setBgImg(scanQrcodeRequest.getBgImg());
            activityScanQrcode.setStartDate(scanQrcodeRequest.getStartDate());
            activityScanQrcode.setEndDate(scanQrcodeRequest.getEndDate());
            Date now = new Date();
            activityScanQrcode.setCreateTime(now);
            activityScanQrcode.setUpdateTime(now);
            int row = activityScanQrcodeMapper.insertSelective(activityScanQrcode);
            return row > 0;
        } catch (Exception e) {
            log.error("", e);
        }
        return Boolean.FALSE;
    }

    /**
     * 二维码绑定扫码活动
     *
     * @param scanQrcodeRequest
     * @return
     */
    @Override
    public ResponseResult<Boolean> bindQrcodeAndScanQrcodeActivity(@RequestBody ScanQrcodeRequest scanQrcodeRequest) {
        log.info("二维码绑定扫码活动 入参: scanQrcodeRequest -> {}", scanQrcodeRequest);
        if (scanQrcodeRequest.getBusinessLine() == null || scanQrcodeRequest.getActivityId() == null
                || StringUtils.isBlank(scanQrcodeRequest.getQrcode())) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        Long activityId = scanQrcodeRequest.getActivityId();
        String qrcode = scanQrcodeRequest.getQrcode();

        //校验活动状态
        ResponseResult<Boolean> result = checkScanQrcodeActivityStatus(activityId);
        if (result != null) {
            return result;
        }

        //绑定二维码和活动的关系
        return bindActivityAndQrcode(activityId, qrcode);
    }

    /**
     * 校验活动状态
     *
     * @param activityId
     * @return
     */
    private ResponseResult<Boolean> checkScanQrcodeActivityStatus(Long activityId) {
        ActivityScanQrcode activityScanQrcode = activityScanQrcodeMapper.selectByPrimaryKey(activityId);
        Date now = new Date();
        Date startDate = activityScanQrcode.getStartDate();
        Date endDate = activityScanQrcode.getEndDate();
        if (startDate != null) {
            if (startDate.after(now)) {
                log.info("活动还未开始~，activityId -> {}", activityId);
                return ResponseResult.buildFailResponse(SCAN_QRCODE_ACTIVITY_HAS_NOT_START.code(), SCAN_QRCODE_ACTIVITY_HAS_NOT_START.message());
            }
        }
        if (endDate != null) {
            if (now.after(endDate)) {
                log.info("活动已结束~，activityId -> {}", activityId);
                return ResponseResult.buildFailResponse(SCAN_QRCODE_ACTIVITY_HAS_OVER.code(), SCAN_QRCODE_ACTIVITY_HAS_OVER.message());
            }
        }
        return null;
    }

    /**
     * 绑定活动和二维码
     *
     * @param activityId
     * @param qrcode
     * @return
     */
    private ResponseResult<Boolean> bindActivityAndQrcode(Long activityId, String qrcode) {
        //根据二维码code查询二维码具体信息
        ResponseResult<MarketingQrcodeDto> responseResult = marketingQrcodeService.getDetailByCode(qrcode);
        if (!responseResult.isSuccess()) {
            log.info("oops~ ,二维码不存在");
            return ResponseResult.buildFailResponse(responseResult.getCode(), responseResult.getMessage(), false);
        }

        //check该二维码是否已绑定活动，一个二维码只能绑定一个活动
        int count = activityScanQrcodeMapper.qrcodeHasBindActivity(qrcode);
        if (count > 0) {
            log.info("该二维码已绑定领券活动，请重新生成二维码");
            return ResponseResult.buildFailResponse(QRCODE_HAS_BIND_ACTIVITY.code(), QRCODE_HAS_BIND_ACTIVITY.message());
        }

        //绑定活动和二维码
        ActivityScanQrcodeDetail activityScanQrcodeDetail = new ActivityScanQrcodeDetail();
        activityScanQrcodeDetail.setId(IdGenerator.nextId());
        activityScanQrcodeDetail.setActScanQrcodeId(activityId);
        activityScanQrcodeDetail.setQrcode(qrcode);
        Date now = new Date();
        activityScanQrcodeDetail.setUpdateTime(now);
        activityScanQrcodeDetail.setCreateTime(now);
        int row = activityScanQrcodeDetailMapper.insertSelective(activityScanQrcodeDetail);
        return ResponseResult.buildSuccessResponse(row > 0);
    }

    /**
     * 根据二维码code获取绑定的活动
     *
     * @param scanQrcodeRequest
     * @return
     */
    @Override
    public ResponseResult<ScanQrcodeResponse> queryScanQrcodeActivityByQrcode(@RequestBody ScanQrcodeRequest scanQrcodeRequest) {
        log.info("根据二维码code获取绑定的活动, qrcode -> {} ", scanQrcodeRequest.getQrcode());
        if (StringUtils.isBlank(scanQrcodeRequest.getQrcode())) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        String qrcode = scanQrcodeRequest.getQrcode();
        ActivityScanQrcodeDto activityScanQrcode = activityScanQrcodeMapper.getScanQrcodeActivityDetialByQrcode(qrcode.trim());
        if (activityScanQrcode == null) {
            return ResponseResult.buildSuccessResponse(QRCODE_HAS_NOT_BIND_ACTIVITY.code(), QRCODE_HAS_NOT_BIND_ACTIVITY.message());
        }
        activityScanQrcode.setGiftIds(null);
        activityScanQrcode.setGiftType(null);
        return ResponseResult.buildSuccessResponse(ScanQrcodeResponse.builder().activityScanQrcode(activityScanQrcode).build());
    }

    /**
     * 根据二维码code领取券
     *
     * @param scanQrcodeRequest
     * @return
     */
    @Override
    public ResponseResult<ScanQrcodeResponse> receiveCouponByQrcode(@RequestBody ScanQrcodeRequest scanQrcodeRequest) {
        log.info("根据二维码code领取券, qrcode -> {} ", scanQrcodeRequest.getQrcode());
        if (StringUtils.isBlank(scanQrcodeRequest.getQrcode()) || scanQrcodeRequest.getUserId() == null || scanQrcodeRequest.getIsNew() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        String qrcode = scanQrcodeRequest.getQrcode().trim();
        Long userId = scanQrcodeRequest.getUserId();

        //查询二维码绑定的活动
        ActivityScanQrcodeDto activityScanQrcode = activityScanQrcodeMapper.getScanQrcodeActivityDetialByQrcode(qrcode);
        if (activityScanQrcode == null) {
            return ResponseResult.buildFailResponse(QRCODE_HAS_NOT_BIND_ACTIVITY.code(), QRCODE_HAS_NOT_BIND_ACTIVITY.message());
        }
        Long userActivityId = activityScanQrcode.getId();
        Byte userActivityType = activityScanQrcode.getType();

        ScanQrcodeResponse scanQrcodeResponse = new ScanQrcodeResponse();
        scanQrcodeResponse.setActivityScanQrcode(activityScanQrcode);//活动信息

        Date now = new Date();
        Date startDate = activityScanQrcode.getStartDate();

        //活动未开始
        if (startDate != null && startDate.after(now)){
            return ResponseResult.buildSuccessResponse(SCAN_QRCODE_ACTIVITY_HAS_NOT_START.code(), SCAN_QRCODE_ACTIVITY_HAS_NOT_START.message(),scanQrcodeResponse);
        }

        //判断是否领取过该活动
        List<UserCouponDto> userCouponList = userCouponMapper.queryUserCouponByActivity(userActivityId, userActivityType, userId, null);
        if (CollectionUtils.isNotEmpty(userCouponList)) {
            scanQrcodeResponse.setReceived(Boolean.TRUE);//已领取过
            scanQrcodeResponse.setReceiveCoupon(userCouponList.get(0));//领取的券
            return ResponseResult.buildSuccessResponse(SCAN_QRCODE_ACTIVITY_HAS_RECEIVED.code(), SCAN_QRCODE_ACTIVITY_HAS_RECEIVED.message(),scanQrcodeResponse);
        }


        Date endDate = activityScanQrcode.getEndDate();
        //活动已结束
        if (endDate != null && now.after(endDate)){
            scanQrcodeResponse.setIsOver(Boolean.TRUE);
            return ResponseResult.buildSuccessResponse(SCAN_QRCODE_ACTIVITY_HAS_OVER.code(), SCAN_QRCODE_ACTIVITY_HAS_OVER.message(),scanQrcodeResponse);
        }

        //领取券
        if (activityScanQrcode.getIsActive()) {
            return doReceiveScanQrcodeCoupon(scanQrcodeRequest,scanQrcodeResponse,activityScanQrcode);
        }
        return ResponseResult.buildSuccessResponse(RECEIVE_SCANQRCODE_COUPON_FAIL.code(), RECEIVE_SCANQRCODE_COUPON_FAIL.message(),scanQrcodeResponse);
    }

    /**
     * 领券
     * @param scanQrcodeRequest
     * @param scanQrcodeResponse
     * @param activityScanQrcode
     * @return
     */
    private ResponseResult<ScanQrcodeResponse> doReceiveScanQrcodeCoupon(ScanQrcodeRequest scanQrcodeRequest,ScanQrcodeResponse scanQrcodeResponse,ActivityScanQrcodeDto activityScanQrcode){
        Long userId = scanQrcodeRequest.getUserId();
        Byte isNew = scanQrcodeRequest.getIsNew();

        Long userActivityId = activityScanQrcode.getId();
        Byte userActivityType = activityScanQrcode.getType();

        String key = "scan_coupon_activity_received:" + userId;
        try {
            if (isNew.intValue() != 0){//判断是否是新用户
                scanQrcodeResponse.setReceiveNotAllowed(Boolean.TRUE);//不允许领取
                scanQrcodeResponse.setRemark(ONLY_NEW_USER_RECEIVE.message());//提示语
                return ResponseResult.buildSuccessResponse(ONLY_NEW_USER_RECEIVE.code(), ONLY_NEW_USER_RECEIVE.message(),scanQrcodeResponse);
            }
            Boolean flag = RedisCache.setIfAbsent(key, "1");
            if (flag) {
                String giftids = activityScanQrcode.getGiftIds();
                List<CouponDto> couponList = queryCouponList(giftids);
                Long activityId = 6L;
                //领券
                Boolean receiveSuccess = commonService.batchReceiveCoupon(activityId, userActivityId, userActivityType, userId, couponList);
                if (receiveSuccess) {
                    List<UserCouponDto> userCouponList = userCouponMapper.queryUserCouponByActivity(userActivityId, userActivityType, userId, null);
                    if (CollectionUtils.isNotEmpty(userCouponList)){
                        scanQrcodeResponse.setReceived(Boolean.FALSE);//已领取过
                        scanQrcodeResponse.setReceiveCoupon(userCouponList.get(0));//领取的券
                    }
                    return ResponseResult.buildSuccessResponse(SCAN_QRCODE_ACTIVITY_RECEIVE_SUCCESS.code(), SCAN_QRCODE_ACTIVITY_RECEIVE_SUCCESS.message(),scanQrcodeResponse);
                } else {
                    //券已下线
                    return ResponseResult.buildFailResponse(RECEIVE_BREAKFAST_COUPON_FAIL.code(), RECEIVE_BREAKFAST_COUPON_FAIL.message(),scanQrcodeResponse);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            RedisCache.expire(key, 3, TimeUnit.SECONDS);
        }
        //加锁失败
        return ResponseResult.buildFailResponse(RECEIVEING_BREAKFAST_COUPON.code(), RECEIVEING_BREAKFAST_COUPON.message(),scanQrcodeResponse);
    }

    private List<CouponDto> queryCouponList(String giftids) {
        List<Long> giftIdList = Lists.transform(Lists.newArrayList(giftids.split(",")), new Function<String, Long>() {
            @Nullable
            @Override
            public Long apply(@Nullable String input) {
                return Long.valueOf(input);
            }
        });
        ResponseResult<List<CouponDto>> couponListResult = couponService.queryByIds(giftIdList);
        if (couponListResult.isSuccess()) {
            return couponListResult.getData();
        }
        return Collections.EMPTY_LIST;
    }
}