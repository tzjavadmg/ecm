package com.milisong.pms.prom.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.milisong.oms.api.OrderService;
import com.milisong.oms.api.PromotionLockerService;
import com.milisong.oms.dto.OrderDto;
import com.milisong.pms.prom.annotation.AfterReceiveNewCoupon;
import com.milisong.pms.prom.api.ActivityService;
import com.milisong.pms.prom.api.BreakfastCouponService;
import com.milisong.pms.prom.api.CouponService;
import com.milisong.pms.prom.api.SendCouponRecordService;
import com.milisong.pms.prom.domain.*;
import com.milisong.pms.prom.dto.*;
import com.milisong.pms.prom.enums.CouponEnum;
import com.milisong.pms.prom.enums.RedPacketLaunchType;
import com.milisong.pms.prom.enums.SendCouponStatus;
import com.milisong.pms.prom.mapper.*;
import com.milisong.pms.prom.param.OrderDeliveryGoodsParam;
import com.milisong.pms.prom.util.ActivityCacheUtil;
import com.milisong.pms.prom.util.ConfigRedisUtils;
import com.milisong.pms.prom.util.PmsPoolUtil;
import com.milisong.ucs.api.UserDeliveryAddressService;
import com.milisong.ucs.api.UserService;
import com.milisong.ucs.dto.UserDeliveryAddressDto;
import com.milisong.ucs.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.milisong.pms.prom.enums.CouponEnum.TYPE.GOODS;
import static com.milisong.pms.prom.enums.CouponEnum.TYPE.NEW_DISCOUNT;
import static com.milisong.pms.prom.enums.PromotionResponseCode.*;
import static com.milisong.pms.prom.enums.RedPacketLaunchType.BREAKFAST_COUPON_ORDER_SHARE;
import static com.milisong.pms.prom.enums.SendCouponStatus.SEND_FAILED;
import static com.milisong.pms.prom.enums.SendCouponStatus.SEND_SUCCESS;
import static com.milisong.ucs.enums.BusinessLineEnum.BREAKFAST;

/**
 * ?????????
 *
 * @author sailor wang
 * @date 2018/12/11 9:57 AM
 * @description
 */
@Slf4j
@RestController
public class BreakfastCouponServiceImpl implements BreakfastCouponService {

    @Autowired
    UserCouponMapper userCouponMapper;

    @Autowired
    UserService userService;

    @Autowired
    CommonService commonService;

    @Autowired
    PromotionLockerService promotionLockerService;

    @Autowired
    CouponService couponService;

    @Autowired
    SendCouponRecordService sendCouponRecordService;

    @Autowired
    SendCouponRecordMapper sendCouponRecordMapper;

    @Autowired
    SendCouponWaterMapper sendCouponWaterMapper;

    @Autowired
    UserDeliveryAddressService userDeliveryAddressService;

    @Autowired
    ActivityUserCouponMapper actUserCouponMapper;

    @Autowired
    ActivityService activityService;

    @Autowired
    CouponPoolMapper couponPoolMapper;

    @Autowired
    OrderService orderService;


    @Override
    @Transactional
    public ResponseResult<UserCouponDto> receiveNewCoupon(@RequestBody UserCouponQueryParam queryParam) {
        log.info("??????????????? queryParam -> {}, buzLine -> {}", queryParam, queryParam.getBusinessLine());
        if (queryParam.getUserId() == null || queryParam.getBusinessLine() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        String RECEIVE_NEW_COUPON_KEY = "receive_new_coupon:" + queryParam.getUserId();
        String couponJson = RedisCache.get(RECEIVE_NEW_COUPON_KEY);
        if (StringUtils.isNotBlank(couponJson)) {
            return ResponseResult.buildSuccessResponse(JSON.parseObject(couponJson, UserCouponDto.class));
        }

        BreakfastCouponConfig couponConfig = ActivityCacheUtil.getCurrentBreakfastCoupon();
        if (couponConfig != null && CollectionUtils.isNotEmpty(couponConfig.getCoupons())) {
            Long userId = queryParam.getUserId();
            Long activityId = couponConfig.getActivityId();
            List<BreakfastCoupon> coupons = couponConfig.getCoupons();

            DateTime now = new DateTime();
            // ??????????????????
            List<BreakfastCoupon> unexpiredGoodsCoupons = Lists.newArrayListWithCapacity(coupons.size());
            // ?????????
            List<BreakfastCoupon> unexpiredCoupons = coupons.stream().filter(new Predicate<BreakfastCoupon>() {
                @Override
                public boolean test(BreakfastCoupon coupon) {
                    Boolean isUnExpire = now.toDate().after(coupon.getStartDate()) && now.toDate().before(coupon.getEndDate());
                    if(GOODS.getCode().equals(coupon.getType())){
                        if (isUnExpire){
                            unexpiredGoodsCoupons.add(coupon);
                        }
                        return false;
                    }
                    return isUnExpire;
                }
            }).collect(Collectors.toList());
            log.info("?????????????????? -> {}", unexpiredCoupons);

            String key = "receive_breakfast_new_coupon:" + userId + "_" + activityId;

            if (CollectionUtils.isNotEmpty(unexpiredCoupons)) {
                Integer count = userCouponMapper.hasReceivedCoupon(userId,queryParam.getBusinessLine(),NEW_DISCOUNT.getCode());
                if (count > 0){
                    log.info("????????????????????? count -> {}",count);
                    return ResponseResult.buildFailResponse(HAS_RECEIVED_BREAKFAST_COUPON.code(),HAS_RECEIVED_BREAKFAST_COUPON.message());
                }
                boolean flag = RedisCache.setIfAbsent(key, "1");
                if (flag) {
                    Random random = new Random();
                    if (CollectionUtils.isNotEmpty(unexpiredGoodsCoupons)){
                        //???????????????????????????
                        unexpiredCoupons.add(unexpiredGoodsCoupons.get(random.nextInt(unexpiredGoodsCoupons.size())));
                    }

                    UserCoupon maxUserCoupon = null;
                    List<UserCoupon> userCouponList = Lists.newArrayList();
                    for (BreakfastCoupon unexpiredCoupon : unexpiredCoupons) {
                        DateTime validDate = now.plusDays(unexpiredCoupon.getValid());
                        Date expireTime = new DateTime(validDate.getYear(), validDate.getMonthOfYear(), validDate.getDayOfMonth(), 23, 59, 59).toDate();
                        // ?????????
                        Byte type = unexpiredCoupon.getType()!=null?unexpiredCoupon.getType():couponConfig.getType();

                        UserCoupon userCoupon = new UserCoupon();
                        userCoupon.setId(IdGenerator.nextId());
                        userCoupon.setActivityId(couponConfig.getActivityId());
                        userCoupon.setUserActivityId(queryParam.getUserActivityId());
                        userCoupon.setUserActivityType(queryParam.getUserActivityType());
                        userCoupon.setGoodsCode(unexpiredCoupon.getGoodsCode());
                        userCoupon.setName(unexpiredCoupon.getName());
                        userCoupon.setType(type);
                        userCoupon.setUserId(userId);
                        userCoupon.setDays(unexpiredCoupon.getDays());
                        userCoupon.setDiscount(unexpiredCoupon.getDiscount());
                        userCoupon.setExpireTime(expireTime);
                        userCoupon.setIsShare((byte) 1);//????????????
                        userCoupon.setIsValid((byte) 1);
                        userCoupon.setIsUsed((byte) 0);
                        userCoupon.setRule(unexpiredCoupon.getRule());
                        userCoupon.setBusinessLine(queryParam.getBusinessLine());
                        userCoupon.setCreateTime(now.toDate());
                        userCoupon.setUpdateTime(now.toDate());

                        if (NEW_DISCOUNT.getCode().equals(type)){//???????????????
                            if (maxUserCoupon != null){
                                if (maxUserCoupon.getDiscount().compareTo(userCoupon.getDiscount()) > 0){//???????????????
                                    maxUserCoupon = userCoupon;
                                }else if (maxUserCoupon.getDiscount().compareTo(userCoupon.getDiscount()) == 0){
                                    if (maxUserCoupon.getExpireTime().compareTo(userCoupon.getExpireTime()) > 0){//????????????????????????????????????
                                        maxUserCoupon = userCoupon;
                                    }
                                }
                            }else {
                                maxUserCoupon = userCoupon;
                            }
                        }

                        userCouponList.add(userCoupon);
                    }
                    int row = userCouponMapper.insertBatch(userCouponList);
                    RedisCache.expire(key, 5, TimeUnit.SECONDS);
                    if (row > 0) {
                        UserCouponDto userCoupon = BeanMapper.map(maxUserCoupon, UserCouponDto.class);
                        RedisCache.set(RECEIVE_NEW_COUPON_KEY, JSON.toJSONString(userCoupon));
                        return ResponseResult.buildSuccessResponse(userCoupon);
                    }
                }
                return ResponseResult.buildFailResponse(RECEIVE_BREAKFAST_COUPON_FAIL.code(), RECEIVE_BREAKFAST_COUPON_FAIL.message());
            } else {
                return ResponseResult.buildSuccessResponse(BREAKFAST_COUPON_EXPIRE.code(), BREAKFAST_COUPON_EXPIRE.message());
            }
        } else {
            log.info("????????????????????????????????????");
        }
        return ResponseResult.buildFailResponse(RECEIVE_BREAKFAST_COUPON_FAIL.code(), RECEIVE_BREAKFAST_COUPON_FAIL.message());
    }

    @Override
    public ResponseResult<UserBreakfastCouponTips> breakfastCouponTips(@RequestBody UserCouponQueryParam queryParam) {
        log.info("???????????????????????? -> {}", queryParam);
        if (queryParam.getUserId() == null || queryParam.getBusinessLine() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        Long userId = queryParam.getUserId();
        queryParam.setUsable(Boolean.TRUE);
        // ????????????
        String key = "breakfast_coupon_toast:" + userId;
        String timesStr = RedisCache.get(key);
        log.info("?????????????????? timesStr -> {}", timesStr);

//        DateTime nowTime = new DateTime();
//        DateTime endOfDay = nowTime.millisOfDay().withMaximumValue();
//        int seconds = Seconds.secondsBetween(DateTime.now(), endOfDay).getSeconds();
//        List<UserCouponDto> couponList = userCouponMapper.queryUsefulCouponUnlimit(queryParam);
//        if (CollectionUtils.isNotEmpty(couponList)) {
//            RedisCache.setEx(key, "1", Long.valueOf(seconds), TimeUnit.SECONDS);
//            UserBreakfastCouponTips couponTips = UserBreakfastCouponTips.builder().coupons(couponList).count(couponList.size()).build();
//            return ResponseResult.buildSuccessResponse(couponTips);
//        }
//        return ResponseResult.buildSuccessResponse();

        if (StringUtils.isBlank(timesStr)) {//??????
            DateTime nowTime = new DateTime();
            DateTime endOfDay = nowTime.millisOfDay().withMaximumValue();
            int seconds = Seconds.secondsBetween(DateTime.now(), endOfDay).getSeconds();
            List<UserCouponDto> couponList = userCouponMapper.queryUsefulCouponUnlimit(queryParam);
            if (CollectionUtils.isNotEmpty(couponList)) {
                RedisCache.setEx(key, "1", Long.valueOf(seconds), TimeUnit.SECONDS);
                UserBreakfastCouponTips couponTips = UserBreakfastCouponTips.builder().coupons(couponList).count(couponList.size()).build();
                return ResponseResult.buildSuccessResponse(couponTips);
            }
            return ResponseResult.buildSuccessResponse();
        } else {//??????????????????tips
            Integer count = userCouponMapper.countUserCoupon(queryParam);
            UserBreakfastCouponTips couponTips = UserBreakfastCouponTips.builder().count(count).build();
            return ResponseResult.buildSuccessResponse(couponTips);
        }
    }

    /**
     * ??????????????????+??????+???????????????
     *
     * @param queryParam
     * @return
     */
    @Override
    public ResponseResult<MyUsableBreakfastCouponDto> settleAccountsDiscount(@RequestBody UserCouponQueryParam queryParam) {
        UserDto userParam = new UserDto();
        userParam.setId(queryParam.getUserId());
        ResponseResult<UserDto> userResult = userService.getUserInfo(userParam);
        Integer point = 0;
        if (userResult.isSuccess() && userResult.getData() != null) {
            point = userResult.getData().getUsefulPoint();
        }
        queryParam.setTotalPoints(point);
        return usableCoupons(queryParam);
    }

    /**
     * ????????? ?????????????????????
     *
     * @param queryParam
     * @return
     */
    @Override
    public ResponseResult<MyUsableBreakfastCouponDto> usableCoupons(@RequestBody UserCouponQueryParam queryParam) {
        log.info("????????????????????????????????? -> {}", queryParam);
        if (queryParam.getUserId() == null || queryParam.getBusinessLine() == null || queryParam.getDays() == null || queryParam.getPayAmount() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        Integer days = queryParam.getDays();
        List<UserCouponDto> couponList = userCouponMapper.queryUsefulCouponUnlimit(queryParam);

        MyUsableBreakfastCouponDto usableBreakfastCouponDto = new MyUsableBreakfastCouponDto();
        if (queryParam.getMerge() == null) {
            Map<String, List<UserCouponDto>> couponMap = selectedCoupon(couponList, queryParam.getOrderGoods(), days);
            // ????????????????????????
            usableBreakfastCouponDto.setUsableCoupons(couponMap.get("usable"));
            // ???????????????????????????
            usableBreakfastCouponDto.setUnusableCoupons(couponMap.get("unUsable"));
        } else if (queryParam.getMerge()) {
            usableBreakfastCouponDto.setUsableCoupons(couponList);
        }
        usableBreakfastCouponDto.setTotalPoints(queryParam.getTotalPoints());
        //?????????,?????????????????????,??????????????????
        usableBreakfastCouponDto.setUnusableLabel(ConfigRedisUtils.getUnusableCouponLabel());
        return ResponseResult.buildSuccessResponse(usableBreakfastCouponDto);
    }


    private Map<String, List<UserCouponDto>> selectedCoupon(List<UserCouponDto> couponList, List<OrderDeliveryGoodsParam> orderGoods, Integer days) {
        List<UserCouponDto> discountCouponUsableList = Lists.newArrayList();
        List<UserCouponDto> goodsCouponUsableList = Lists.newArrayList();
        List<UserCouponDto> unUsableList = Lists.newArrayList();

        Map<String, List<UserCouponDto>> selectedCouponMap = Maps.newHashMap();

        // ??????????????????????????????
        Map<String, BigDecimal> goodsMap = Maps.newHashMap();
        //???????????????
        BigDecimal goodsActualAmount = new BigDecimal(0);

        if (CollectionUtils.isNotEmpty(couponList)) {
            if (CollectionUtils.isNotEmpty(orderGoods)) {
                for (OrderDeliveryGoodsParam goods : orderGoods) {
                    goodsMap.put(goods.getGoodsCode(), goods.getGoodsActualPrice());
                    goodsActualAmount = goodsActualAmount.add(goods.getGoodsActualPrice());
                }
            }
            // ????????????????????????????????????????????????
            List<Long> lockedCouponids = queryCouponLocksByList(couponList);
            for (UserCouponDto userCouponDto : couponList) {
                // ?????????
                Byte couponType = userCouponDto.getType();
                if (GOODS.getCode().equals(couponType)) {
                    // ?????????????????????????????????????????????????????????????????????~(>_<)~
                    if (goodsMap.size() > 0) {
                        String goodsCode = userCouponDto.getGoodsCode();
                        if (goodsMap.containsKey(goodsCode)) {
                            if (userCouponDto.getDays() == null || userCouponDto.getDays() <= days) {
                                if (!lockedCouponids.contains(userCouponDto.getId())) {
                                    goodsCouponUsableList.add(userCouponDto);
                                }
                            } else {
                                // ?????????
                                unUsableList.add(userCouponDto);
                            }
                        } else {
                            // ?????????
                            unUsableList.add(userCouponDto);
                        }
                    } else {
                        // ???????????????????????????
                        unUsableList.add(userCouponDto);
                    }
                } else {
                    // ????????????????????????????????????
                    if (userCouponDto.getDays() == null || userCouponDto.getDays() <= days) {
                        if (!lockedCouponids.contains(userCouponDto.getId())) {
                            discountCouponUsableList.add(userCouponDto);
                        }
                    } else {
                        // ?????????
                        if (!lockedCouponids.contains(userCouponDto.getId())) {
                            unUsableList.add(userCouponDto);
                        }
                    }
                }
            }
        }
        List<UserCouponDto> usableList = Lists.newArrayList();

        UserCouponDto discountCoupon = null;
        UserCouponDto goodsCoupon = null;

        if (CollectionUtils.isNotEmpty(discountCouponUsableList)) {
            usableList.addAll(discountCouponUsableList);
            discountCoupon = commonService.filterOptimalBreakfastDiscountCouponAmount(discountCouponUsableList, days);
            log.info("??????????????? discountCoupon -> {}", JSONObject.toJSONString(discountCoupon));
        }
        if (CollectionUtils.isNotEmpty(goodsCouponUsableList)) {
            usableList.addAll(goodsCouponUsableList);
            goodsCoupon = commonService.filterOptimalBreakfastGoodsCouponAmount(goodsMap, goodsCouponUsableList, days);
            log.info("??????????????? goodsCoupon -> {}", JSONObject.toJSONString(goodsCoupon));
        }

        UserCouponDto selectedCoupon = null;
        // ?????????????????????
        if (discountCoupon != null && goodsCoupon != null) {
            // ??????????????????????????????????????????????????? x ??????
            BigDecimal discountCouponCalcAmount = (goodsActualAmount.multiply(discountCoupon.getDiscount()).
                    divide(new BigDecimal(10))).setScale(2, BigDecimal.ROUND_HALF_UP);
            log.info("??????????????? -> {}, ????????? -> {}, ??????????????????????????? -> {}", goodsActualAmount, discountCoupon.getDiscount(), discountCouponCalcAmount);

            // ???????????????????????????????????????????????????-??????????????????+????????????x??????
            BigDecimal goodsPrice = goodsMap.get(goodsCoupon.getGoodsCode());
            BigDecimal goodsCouponCalcAmount = goodsActualAmount.subtract(goodsPrice).add(goodsCoupon.getDiscount());
            log.info("??????????????? -> {}, x????????? -> {} y?????? -> {}, ??????????????????????????? -> {}", goodsActualAmount, goodsPrice, goodsCoupon.getDiscount(), goodsCouponCalcAmount);
            if (goodsCouponCalcAmount.compareTo(discountCouponCalcAmount) < 0) {
                selectedCoupon = goodsCoupon;
            } else {
                selectedCoupon = discountCoupon;
            }
        } else {
            if (discountCoupon != null) {
                selectedCoupon = discountCoupon;
            } else {
                selectedCoupon = goodsCoupon;
            }
        }

        if (selectedCoupon != null) {
            for (UserCouponDto couponDto : usableList) {
                if (selectedCoupon.getId().equals(couponDto.getId())) {
                    couponDto.setSelected(Boolean.TRUE);
                    if (GOODS.getCode().equals(couponDto.getType())) {
                        couponDto.setDiscountAmount(goodsMap.get(selectedCoupon.getGoodsCode()).subtract(selectedCoupon.getDiscount()));
                    }
                    break;
                }
            }
        } else {
            log.info("??????????????????????????????");
        }
        selectedCouponMap.put("usable", usableList);
        selectedCouponMap.put("unUsable", unUsableList);
        return selectedCouponMap;
    }

    @Override
    public ResponseResult<Integer> usableCouponsCount(@RequestBody UserCouponQueryParam queryParam) {
        log.info("????????????????????????????????? -> {}", queryParam);
        if (queryParam.getUserId() == null || queryParam.getBusinessLine() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        List<Long> couponList = userCouponMapper.usableCouponsCount(queryParam);
        Integer count = 0;
        if (CollectionUtils.isNotEmpty(couponList)) {
            // ????????????????????????????????????????????????
            List<Long> lockedCouponids = queryCouponLocksByIds(couponList);

            for (Long couponId : couponList) {
                if (!lockedCouponids.contains(couponId)) {
                    count++;
                }
            }
        }
        return ResponseResult.buildSuccessResponse(count);
    }

    @Override
    public ResponseResult<Pagination<UserCouponDto>> myCoupon(@RequestBody UserCouponQueryParam queryParam) {
        log.info("??????????????????????????? -> {}, buzline -> {}", queryParam, queryParam.getBusinessLine());
        if (queryParam.getUserId() == null || queryParam.getBusinessLine() == null || queryParam.getUsable() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        int startRow = 0;
        int pageSize = 10;
        if (queryParam.getPageSize() == null || queryParam.getPageSize() <= 0) {
            queryParam.setPageSize(10);
        }
        if (queryParam.getPageNo() == null || queryParam.getPageNo() <= 0) {
            queryParam.setPageNo(1);
        }
        pageSize = queryParam.getPageSize();
        if (pageSize > 100) {
            pageSize = 100;
        }
        startRow = (queryParam.getPageNo() - 1) * pageSize;
        Integer totalCount = userCouponMapper.countUserCoupon(queryParam);
        Integer count = 0;
        if (totalCount > 0) {
            queryParam.setStartRow(startRow);
            List<UserCouponDto> couponList = userCouponMapper.queryUserCoupon(queryParam);

            List<UserCouponDto> list = Lists.newArrayList();
            if (CollectionUtils.isNotEmpty(couponList)) {
                // ????????????????????????????????????????????????
                List<Long> lockedCouponids = queryCouponLocksByList(couponList);

                for (UserCouponDto coupon : couponList) {
                    if (!lockedCouponids.contains(coupon.getId())) {
                        list.add(coupon);
                    } else {
                        count++;
                    }
                }
            }

            Pagination pagination = new Pagination(queryParam.getPageNo(), pageSize, totalCount - count, list);
            return ResponseResult.buildSuccessResponse(pagination);
        }
        return ResponseResult.buildSuccessResponse();
    }

    @Override
    public ResponseResult<Boolean> scanBreakfastCoupon() {
        try {
            Date currentDate = new Date();
            userCouponMapper.scanBreakfastCoupon(currentDate);
            return ResponseResult.buildSuccessResponse();
        } catch (Exception e) {
            log.error("?????????????????????????????????", e);
            return ResponseResult.buildFailResponse();
        }
    }

    @Deprecated
    @Override
    public ResponseResult<Boolean> sendBreakfastCoupon(@RequestBody UserCouponQueryParam queryParam) {
        if (CollectionUtils.isEmpty(queryParam.getUserids())) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        Byte couponType = queryParam.getCouponType();
        log.info("??????????????? couponType -> {}", couponType);
        if (GOODS.getCode().equals(couponType)) {
            return sendBreakfastGoodsCoupon(queryParam);
        }
        return sendBreakfastDiscountCoupon(queryParam);
    }

    @Override
    @Transactional
    @Async("asyncServiceExecutor")
    public ResponseResult<Boolean> batchSendBreakfastCoupon(@RequestBody SendBreakCouponRequest sendCouponRequest) {
        log.info("??????????????? sendCouponRequest -> {}", sendCouponRequest);
        if (CollectionUtils.isEmpty(sendCouponRequest.getCouponids()) || sendCouponRequest.getOperatorId() == null
                || StringUtils.isBlank(sendCouponRequest.getOperatorName()) || sendCouponRequest.getBusinessLine() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        Date now = new Date();
        if (sendCouponRequest.getSendTime() == null){
            sendCouponRequest.setSendTime(now);
        }
        // ????????????????????????????????????
        sendCouponRequest.setPageNo(1);
        sendCouponRequest.setPageSize(500);

        // ?????????ids???????????????
        List<Long> couponids = sendCouponRequest.getCouponids();
        ResponseResult<List<CouponDto>> couponResponseResult = couponService.queryByIds(couponids);
        if (couponResponseResult.isSuccess() && CollectionUtils.isNotEmpty(couponResponseResult.getData())) {
            List<CouponDto> couponList = couponResponseResult.getData();

            // ???????????????????????????
            ResponseResult<Boolean> result = isExistCoupons(sendCouponRequest, couponList);
            if (!result.isSuccess()) {
                return result;
            }

            Long sendCouponRecordId = sendCouponRequest.getId() == null?IdGenerator.nextId():sendCouponRequest.getId();
            // ??????????????????????????????
            if (sendCouponRequest.getSendTime().compareTo(now) > 0){
                log.info("?????????????????????????????????");
                saveSendCouponRecord(couponList,sendCouponRecordId,sendCouponRequest,0,0, SendCouponStatus.WAIT_FOR_SEND.getCode());
                return ResponseResult.buildSuccessResponse();
            }
            String key = "send_coupon:"+sendCouponRecordId;
            Boolean lockSuccess = RedisCache.setIfAbsent(key, "1");
            // ?????????
            if (lockSuccess){
                try {
                    ResponseResult<com.milisong.ucs.dto.Pagenation<UserDeliveryAddressDto>> userResult =
                            userDeliveryAddressService.queryUserByDevlieryAddr(BeanMapper.map(sendCouponRequest, com.milisong.ucs.dto.SendBreakCouponRequest.class));
                    if (userResult.isSuccess() && userResult.getData() != null && CollectionUtils.isNotEmpty(userResult.getData().getList())) {
                        com.milisong.ucs.dto.Pagenation<UserDeliveryAddressDto> pagenation = userResult.getData();
                        int pageCount = pagenation.getPageCount();
                        int rowCount = (int) pagenation.getRowCount();
                        List<UserDeliveryAddressDto> userList = pagenation.getList();

                        //??????????????? & ?????????
                        int sendRow = userList.size();
                        sendAndRecordCouponWater(sendCouponRecordId,couponList, userList,sendCouponRequest.getSendTime(),sendCouponRequest.getSmsMsg());
                        if (pageCount > 1) {
                            for (Integer pageNum = 2; pageNum <= pageCount; pageNum++) {
                                List<UserDeliveryAddressDto> users = fetchUser(sendCouponRequest, pageNum);
                                //?????????
                                int row = sendAndRecordCouponWater(sendCouponRecordId,couponList, users,sendCouponRequest.getSendTime(),sendCouponRequest.getSmsMsg());
                                sendRow += users.size();
                            }
                        }
                        saveSendCouponRecord(couponList,sendCouponRecordId,sendCouponRequest,rowCount,sendRow,SEND_SUCCESS.getCode());
                        return ResponseResult.buildSuccessResponse();
                    }
                }catch (Exception ex){
                    log.error("",ex);
                    throw ex;
                }finally {
                    RedisCache.expire(key,5,TimeUnit.MINUTES);
                }
            }else {
                log.info("????????????????????????????????????");
            }
            return ResponseResult.buildFailResponse(NOT_FOUND_USERS.code(), NOT_FOUND_USERS.message());
        }
        // ??????????????????
        return notFindCoupons(sendCouponRequest, couponids);
    }

    private void saveSendCouponRecord(List<CouponDto> couponList,Long sendCouponRecordId,SendBreakCouponRequest sendCouponRequest,
                                      Integer shouldSendNum,Integer actualSendNum,Byte status){
        String content = Joiner.on(",").join(Lists.transform(couponList, new Function<CouponDto, String>() {
            @Nullable
            @Override
            public String apply(@Nullable CouponDto input) {
                return input.getName()+"_"+input.getRemark();
            }
        }));
        SendCouponRecord sendCouponRecord = new SendCouponRecord();
        sendCouponRecord.setId(sendCouponRecordId);
        sendCouponRecord.setOperatorId(sendCouponRequest.getOperatorId());
        sendCouponRecord.setOperatorName(sendCouponRequest.getOperatorName());
        sendCouponRecord.setContent(content);
        sendCouponRecord.setFilterCondition(JSONObject.toJSONString(sendCouponRequest));
        sendCouponRecord.setStatus(status);
        sendCouponRecord.setSendTime(sendCouponRequest.getSendTime());
        sendCouponRecord.setCouponids(Joiner.on(",").join(couponList.stream().map(CouponDto::getId).collect(Collectors.toList())));
        sendCouponRecord.setShouldSendNum(shouldSendNum);
        sendCouponRecord.setActualSendNum(actualSendNum);
        sendCouponRecord.setRemark("");
        sendCouponRecord.setUpdateTime(new Date());

        if (sendCouponRequest.getId() != null){
            sendCouponRecord.setId(sendCouponRequest.getId());
            sendCouponRecordMapper.updateByPrimaryKeySelective(sendCouponRecord);
        }else {
            sendCouponRecord.setStatus(status);
            sendCouponRecord.setBusinessLine(BREAKFAST.getCode());
            sendCouponRecord.setCreateTime(new Date());
            sendCouponRecordMapper.insertSelective(sendCouponRecord);
        }
    }

    private List<UserDeliveryAddressDto> fetchUser(SendBreakCouponRequest sendCouponRequest,int pageNo) {
        // ????????????????????????????????????
        sendCouponRequest.setPageNo(pageNo);
        sendCouponRequest.setPageSize(500);
        ResponseResult<com.milisong.ucs.dto.Pagenation<UserDeliveryAddressDto>> userResult =
                userDeliveryAddressService.queryUserByDevlieryAddr(BeanMapper.map(sendCouponRequest, com.milisong.ucs.dto.SendBreakCouponRequest.class));
        if (userResult.isSuccess() && userResult.getData() != null && CollectionUtils.isNotEmpty(userResult.getData().getList())) {
            com.milisong.ucs.dto.Pagenation<UserDeliveryAddressDto> pagenation = userResult.getData();
            return pagenation.getList();
        }
        return Lists.newArrayList();
    }

    /**
     * ?????????????????????
     *
     * @param couponList
     * @return
     */
    private ResponseResult<Boolean> isExistCoupons(SendBreakCouponRequest sendCouponRequest, List<CouponDto> couponList) {
        List<String> remarkList = Lists.newArrayList();
        List<String> nameList = Lists.newArrayList();
        for (CouponDto coupon : couponList) {
            String name = coupon.getName() + "_" + coupon.getRemark();
            if (CouponEnum.STATUS.OFFLINE.getCode().equals(coupon.getStatus())) {
                remarkList.add(name + " (?????????)");
            }
            nameList.add(name);
        }
        if (CollectionUtils.isNotEmpty(remarkList)){
            SendCouponRecord sendCouponRecord = new SendCouponRecord();
            sendCouponRecord.setOperatorId(sendCouponRequest.getOperatorId());
            sendCouponRecord.setOperatorName(sendCouponRequest.getOperatorName());
            sendCouponRecord.setContent(Joiner.on(",").join(nameList));
            sendCouponRecord.setSendTime(sendCouponRequest.getSendTime());
            sendCouponRecord.setCouponids(Joiner.on(",").join(couponList.stream().map(CouponDto::getId).collect(Collectors.toList())));
            sendCouponRecord.setFilterCondition(JSONObject.toJSONString(sendCouponRequest));
            sendCouponRecord.setShouldSendNum(0);
            sendCouponRecord.setActualSendNum(0);
            sendCouponRecord.setRemark(Joiner.on(",").join(remarkList));
            sendCouponRecord.setStatus(SEND_FAILED.getCode());
            sendCouponRecord.setUpdateTime(new Date());

            if (sendCouponRequest.getId() != null){
                sendCouponRecord.setId(sendCouponRequest.getId());
                sendCouponRecordMapper.updateByPrimaryKeySelective(sendCouponRecord);
            }else {
                sendCouponRecord.setId(IdGenerator.nextId());
                sendCouponRecord.setBusinessLine(BREAKFAST.getCode());
                sendCouponRecord.setCreateTime(new Date());
                sendCouponRecordMapper.insertSelective(sendCouponRecord);
            }
            return ResponseResult.buildFailResponse(BREAKFAST_COUPON_OFFLINE.code(), BREAKFAST_COUPON_OFFLINE.message());
        }
        return ResponseResult.buildSuccessResponse();
    }

    /**
     * ??????????????????
     *
     * @param sendCouponRequest
     * @param couponids
     * @return
     */
    private ResponseResult<Boolean> notFindCoupons(SendBreakCouponRequest sendCouponRequest, List<Long> couponids) {
        SendCouponRecord sendCouponRecord = new SendCouponRecord();
        sendCouponRecord.setContent("");
        if (sendCouponRecord.getSendTime() == null) {
            sendCouponRecord.setSendTime(new Date());
        }
        sendCouponRecord.setOperatorId(sendCouponRequest.getOperatorId());
        sendCouponRecord.setOperatorName(sendCouponRequest.getOperatorName());
        sendCouponRecord.setContent("");
        sendCouponRecord.setCouponids(Joiner.on(",").join(couponids));
        sendCouponRecord.setFilterCondition(JSONObject.toJSONString(sendCouponRequest));
        sendCouponRecord.setShouldSendNum(0);
        sendCouponRecord.setActualSendNum(0);
        sendCouponRecord.setRemark("??????????????????");
        sendCouponRecord.setStatus(SEND_FAILED.getCode());
        sendCouponRecord.setUpdateTime(new Date());

        if (sendCouponRequest.getId() != null){
            sendCouponRecord.setId(sendCouponRequest.getId());
            sendCouponRecordMapper.updateByPrimaryKeySelective(sendCouponRecord);
        }else {
            sendCouponRecord.setId(IdGenerator.nextId());
            sendCouponRecord.setBusinessLine(BREAKFAST.getCode());
            sendCouponRecord.setCreateTime(new Date());
            sendCouponRecordMapper.insertSelective(sendCouponRecord);
        }
        return ResponseResult.buildFailResponse(BREAKFAST_COUPON_NOT_EXIST.code(), BREAKFAST_COUPON_NOT_EXIST.message());
    }

    @Override
    public ResponseResult<Boolean> useUserCoupon(@RequestBody UserCouponQueryParam queryParam) {
        log.info("??????????????????????????? queryParam -> {}", queryParam);
        if (queryParam.getOrderId() == null || queryParam.getUserCouponId() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }

        try {
            UserCoupon userCoupon = new UserCoupon();
            userCoupon.setId(queryParam.getUserCouponId());
            userCoupon.setIsUsed((byte) 1);
            userCoupon.setUpdateTime(new Date());
            userCoupon.setOrderId(queryParam.getOrderId());
            int row = userCouponMapper.updateByPrimaryKeySelective(userCoupon);
            return ResponseResult.buildSuccessResponse(row > 0);
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildFailResponse(UPDATE_USER_COUPON_EXCEPTION.code(), UPDATE_USER_COUPON_EXCEPTION.message());
    }

    /**
     * ?????????????????????
     *
     * @param queryParam
     * @return
     */
    @Override
    public ResponseResult<Boolean> updateUserCouponUseful(@RequestBody UserCouponQueryParam queryParam) {
        if (queryParam.getUserCouponId() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        try {
            UserCoupon record = new UserCoupon();
            record.setId(queryParam.getUserCouponId());
            record.setIsUsed((byte) 0);
            record.setUpdateTime(new Date());
            int row = userCouponMapper.updateByPrimaryKeySelective(record);
            return ResponseResult.buildSuccessResponse(row > 0);
        } catch (Exception e) {
            log.error("", e);
        }
        return ResponseResult.buildFailResponse(UPDATE_USER_COUPON_EXCEPTION.code(), UPDATE_USER_COUPON_EXCEPTION.message());
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param couponRequest
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult createMultiShareCoupon(@RequestBody UserCouponRequest couponRequest) throws Exception {
        log.info(" ?????????????????????????????? couponRequest -> {}",couponRequest);
        if (couponRequest == null || couponRequest.getLaunchType() == null
                || couponRequest.getLaunchId() == null || couponRequest.getBusinessLine() == null
                || couponRequest.getOpenid() == null || couponRequest.getUserId() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }

        RedPacketLaunchType launchType = couponRequest.getLaunchType();
        Long launchId = couponRequest.getLaunchId();
        Byte buzLine = couponRequest.getBusinessLine();
        // ??????????????????????????????
        int count = actUserCouponMapper.countActivityUserCouponByLaunch(launchType.getCode(),launchId,buzLine);
        if (count == 0) {
            ActivityQueryParam queryParam = new ActivityQueryParam();
            queryParam.setBusinessLine(buzLine);
            queryParam.setCouponType(CouponEnum.TYPE.DISCOUNT);
            ResponseResult<MultiBreakfastCouponConfig> responseResult = activityService.currentActivityCouponConfig(queryParam);
            if (!responseResult.isSuccess()) {
                log.info("?????????????????????");
                return ResponseResult.buildFailResponse(responseResult.getCode(), responseResult.getMessage());
            }
            String lockKey = "breakfast_coupon_pool_lock:" + launchId;
            RLock rLock = null;
            try {
                rLock = LockProvider.getLock(lockKey);
                rLock.lock(1,TimeUnit.MINUTES);
                MultiBreakfastCouponConfig couponConfig = responseResult.getData();
                //?????????????????????
                Long userActivityId = insertActivityUserCoupon(couponRequest, couponConfig);
                Long activityId = couponConfig.getActivityId();
                if (userActivityId > 0) {
                    // ????????????????????????
                    List<String> strList = batchInsertCouponPool(couponConfig,buzLine, activityId, userActivityId);
                    if (strList.size() > 0) {
                        // ????????????????????????
                        Long row = PmsPoolUtil.createBreakFastCouponQueue(userActivityId, strList);
                        if (row <= 0) {
                            log.error("??????????????????????????????");
                            throw new Exception();
                        }
                    }
                }
                return ResponseResult.buildSuccessResponse();
            } catch (Exception e) {
                log.error("",e);
                throw e;
            }finally {
                if (rLock != null){
                    rLock.unlock();
                }
            }
        }
        return ResponseResult.buildSuccessResponse(USER_COUPON_ACTIVITY_EXISTED.code(), USER_COUPON_ACTIVITY_EXISTED.message());
    }

    /**
     * ???????????????????????????
     *
     * @param couponRequest
     * @return
     */
    @Override
    @AfterReceiveNewCoupon
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<UserCouponResponse> recevieMultiShareCoupon(@RequestBody UserCouponRequest couponRequest) {
        if (couponRequest.getLaunchId() == null
                || couponRequest.getIsNewUser() == null || couponRequest.getBusinessLine() == null
                || couponRequest.getOpenid() == null || couponRequest.getUserId() == null) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }

        UserCouponResponse userCouponResponse = new UserCouponResponse();
        Long userId = couponRequest.getUserId();

        Long launchId = couponRequest.getLaunchId();

        String nickName = couponRequest.getNickName();
        String headPortraitUrl = couponRequest.getHeadPortraitUrl();
        Byte buzLine = couponRequest.getBusinessLine();

        // ?????????????????????
        ActivityQueryParam queryParam = new ActivityQueryParam();
        queryParam.setBusinessLine(buzLine);
        queryParam.setCouponType(CouponEnum.TYPE.DISCOUNT);
        ResponseResult<MultiBreakfastCouponConfig> responseResult = activityService.currentActivityCouponConfig(queryParam);
        if (responseResult.getData() == null){
            return ResponseResult.buildFailResponse(MULTI_BREAKFAST_COUPON_CONFIG_NOT_EXISTS.code(), MULTI_BREAKFAST_COUPON_CONFIG_NOT_EXISTS.message());
        }

        MultiBreakfastCouponConfig breakfastCouponConfig = responseResult.getData();
        userCouponResponse.setDescript(breakfastCouponConfig.getDescript());
        userCouponResponse.setMasterPic(breakfastCouponConfig.getMasterPic());
        String couponName = breakfastCouponConfig.getName();
        Long activityId = breakfastCouponConfig.getActivityId();
        Integer clickLimit = breakfastCouponConfig.getClickLimit().intValue();//??????????????????????????????

        if (responseResult.isSuccess()) {
            userCouponResponse.setIsOver(false);// ???????????????
            // ??????????????????????????????(?????????????????????????????????????????????????????????????????????)
            ActivityUserCouponDto activityUserCoupon = actUserCouponMapper.queryActivityUserCouponByLaunch(null,launchId,buzLine);
            if (activityUserCoupon == null) {
                //?????????????????????????????????
                compensateCreateMultiShareCoupon(launchId);
                activityUserCoupon = actUserCouponMapper.queryActivityUserCouponByLaunch(null,launchId,buzLine);
                if (activityUserCoupon == null){
                    return ResponseResult.buildFailResponse(ACTIVITY_USER_COUPON_NOT_EXIST.code(), ACTIVITY_USER_COUPON_NOT_EXIST.message());
                }
            }

            Byte launchType = activityUserCoupon.getLaunchType();

            Long userActivityId = activityUserCoupon.getId();
            userCouponResponse.setUserActivityId(userActivityId);
            if (activityUserCoupon.getIsSuccess().intValue() == 1) { //?????????
                log.info("??????????????????????????????");
                return couponCompleted(userCouponResponse,launchType,launchId,userId,activityId,userActivityId,couponName,clickLimit);
            }
            // ????????????????????????
            if (StringUtils.isEmpty(nickName) && StringUtils.isEmpty(headPortraitUrl)) {
                log.info("???????????????????????????");
                return ResponseResult.buildFailResponse(USER_NOT_AUTHORIZATION.code(), USER_NOT_AUTHORIZATION.message());
            }

            Byte clickCount = activityUserCoupon.getClickCount() == null ? 0 : activityUserCoupon.getClickCount();// ????????????
            Byte players = activityUserCoupon.getPlayers();
            Byte isShare = activityUserCoupon.getIsShare();
            Byte valid = activityUserCoupon.getValid();
            //??????????????????????????????
            int count = couponPoolMapper.receiveCountToday(activityId, couponRequest.getUserId());
            if (count >= clickLimit) {
                log.info("??????????????????????????????");
                userCouponResponse.setOverLimit(true);
                // ???????????????????????????
                List<ActivityUserCouponDto> userCouponRecordList = actUserCouponMapper.multiShareCouponRecord(launchType, launchId);
                // ??????????????????
                isReceivedCoupon(userCouponResponse,userId,userActivityId,couponName,true);

                userCouponResponse.setMultiShareCouponRecord(userCouponRecordList);
                return ResponseResult.buildSuccessResponse(userCouponResponse);
            }

            // ?????????????????????
            int received = couponPoolMapper.receiveCount(null, couponRequest.getUserId(), userActivityId);
            if (received > 0) {
                // ??????????????????
                isReceivedCoupon(userCouponResponse,userId,userActivityId,couponName,true);

                // ???????????????????????????
                List<ActivityUserCouponDto> userCouponRecordList = actUserCouponMapper.multiShareCouponRecord(launchType, launchId);
                userCouponResponse.setMultiShareCouponRecord(userCouponRecordList);
                return ResponseResult.buildSuccessResponse(userCouponResponse);
            }

            String key = "receive_multi_share_coupon_concurrent:" + userId +"_"+launchId;
            if (RedisCache.setIfAbsent(key, "1")) {
                // ?????????????????????
                String jsonStr = PmsPoolUtil.popBreakFastCouponQueue(userActivityId);
                log.info("????????????pop?????? jsonStr -> {}", jsonStr);
                if (StringUtils.isNotBlank(jsonStr)) { // ????????????
                    CouponPool couponPool = JSONObject.parseObject(jsonStr, CouponPool.class);
                    // ??????????????????
                    updateCouponPool(couponPool, userId, nickName, headPortraitUrl);

                    // ???????????????
                    DateTime validDate = DateTime.now().plusDays(valid.intValue());
                    Date expireTime = new DateTime(validDate.getYear(), validDate.getMonthOfYear(), validDate.getDayOfMonth(), 23, 59, 59).toDate();
                    UserCoupon userCoupon = receiveCoupon(couponPool, couponRequest, expireTime);
                    if (userCoupon != null) {
                        UserCouponDto userCouponDto = BeanMapper.map(userCoupon, UserCouponDto.class);
                        userCouponDto.setName(couponPool.getCouponName());
                        userCouponResponse.setReceiveCoupon(userCouponDto);
                        userCouponResponse.setReceived(true);
                    }
                    ActivityUserCoupon record = new ActivityUserCoupon();
                    // ?????????????????????????????????
                    if (Integer.valueOf(players).equals(Integer.valueOf(clickCount.intValue() + 1))) {
                        // ?????????
                        record.setIsSuccess((byte) 1);
                        record.setClickCount(players);
                    } else {
                        // ?????????
                        record.setClickCount((byte) (clickCount.intValue() + 1));
                    }
                    record.setId(userActivityId);
                    record.setUpdateTime(new Date());
                    actUserCouponMapper.updateByPrimaryKeySelective(record);
                    userCouponResponse.setReceived(false);
                } else {// ????????????
                    log.info("??????????????????????????????");
                }
            }
            RedisCache.expire(key, 1, TimeUnit.SECONDS);

            // ??????????????????
            isReceivedCoupon(userCouponResponse,userId,userActivityId,couponName,null);

            // ???????????????????????????
            List<ActivityUserCouponDto> userCouponRecordList = actUserCouponMapper.multiShareCouponRecord(launchType, launchId);
            userCouponResponse.setMultiShareCouponRecord(userCouponRecordList);
            return ResponseResult.buildSuccessResponse(userCouponResponse);
        }

        // ????????????????????????????????????
        return fetchHistoryRecord(userCouponResponse,null,launchId,userId,activityId,couponName,clickLimit);
    }

    /**
     * ???????????????
     *
     * @return
     */
    @Override
    public ResponseResult<BigDecimal> bestDiscount() {
        BreakfastCoupon bestDicountCoupon = bestDiscountCoupon();
        if (bestDicountCoupon != null){
            return ResponseResult.buildSuccessResponse(bestDicountCoupon.getDiscount());
        }
        return ResponseResult.buildFailResponse();
    }

    /**
     * ??????????????????????????????????????????
     *
     * @return
     */
    private BreakfastCoupon bestDiscountCoupon() {
        BreakfastCouponConfig couponConfig = ActivityCacheUtil.getCurrentBreakfastCoupon();
        BreakfastCoupon bestDiscountCoupon = null;
        if (couponConfig != null && CollectionUtils.isNotEmpty(couponConfig.getCoupons())) {
            List<BreakfastCoupon> coupons = couponConfig.getCoupons();

            DateTime now = new DateTime();
            for (BreakfastCoupon coupon : coupons){
                Boolean isUnExpire = now.toDate().after(coupon.getStartDate()) && now.toDate().before(coupon.getEndDate());
                Byte couponType = coupon.getType();
                if (isUnExpire && NEW_DISCOUNT.getCode().equals(couponType)){
                    if (bestDiscountCoupon != null){
                        if (bestDiscountCoupon.getDiscount().compareTo(coupon.getDiscount()) > 0){//???????????????
                            bestDiscountCoupon = coupon;
                        }
                    }else {
                        bestDiscountCoupon = coupon;
                    }
                }
            }
        }
        return bestDiscountCoupon;
    }
    private void updateCouponPool(CouponPool couponPool, Long userId, String nickName, String headPortraitUrl) {
        couponPool.setStatus((byte) 1);
        couponPool.setUserId(userId);
        couponPool.setNickName(nickName);
        couponPool.setHeadPortraitUrl(headPortraitUrl);
        couponPool.setUpdateTime(new Date());
        couponPoolMapper.updateByPrimaryKey(couponPool);
    }

    private UserCoupon receiveCoupon(CouponPool couponPool,UserCouponRequest couponRequest, Date expireTime) {
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setId(IdGenerator.nextId());
        userCoupon.setActivityId(couponPool.getActivityId());
        userCoupon.setUserActivityId(couponPool.getUserActivityId());
        userCoupon.setName(couponPool.getCouponName());
        userCoupon.setType(CouponEnum.TYPE.DISCOUNT.getCode());
        userCoupon.setUserId(couponRequest.getUserId());
        userCoupon.setDays(couponPool.getLimitDays());
        userCoupon.setDiscount(couponPool.getDiscount());
        userCoupon.setExpireTime(expireTime);
        userCoupon.setIsShare(couponPool.getIsShare());
        userCoupon.setIsValid((byte) 1);
        userCoupon.setIsUsed((byte) 0);
        userCoupon.setRule(couponPool.getRule());
        userCoupon.setBusinessLine(couponPool.getBusinessLine());
        userCoupon.setCreateTime(new Date());
        userCoupon.setUpdateTime(new Date());
        userCouponMapper.insertSelective(userCoupon);
        return userCoupon;
    }

    private ResponseResult<UserCouponResponse> fetchHistoryRecord(UserCouponResponse userCouponResponse,Byte launchType,Long launchId,Long userId,Long activityId,String couponName,Integer clickLimit){
        // ????????????????????????????????????
        ActivityUserCouponDto activityUserCoupon = actUserCouponMapper.queryActivityUserCouponByLaunch(launchType, launchId,BREAKFAST.getCode());
        if (activityUserCoupon == null) {
            //return ResponseResult.buildFailResponse(ACTIVITY_USER_COUPON_NOT_EXIST.code(), ACTIVITY_USER_COUPON_NOT_EXIST.message());
            // ??????????????????????????????????????????????????????????????????????????????
            userCouponResponse.setIsOver(true);
            return ResponseResult.buildSuccessResponse(userCouponResponse);
        }
        Long userActivityId = activityUserCoupon.getId();
        userCouponResponse.setUserActivityId(userActivityId);
        // ??????????????????
        isReceivedCoupon(userCouponResponse,userId,userActivityId,couponName,true);

        //??????????????????????????????
        int count = couponPoolMapper.receiveCountToday(activityId, userId);
        if (count >= clickLimit) {
            userCouponResponse.setOverLimit(true);
        }
        // ?????????????????????????????????????????????
        userCouponResponse.setIsOver(true);
        userCouponResponse.setMultiShareCouponRecord(actUserCouponMapper.multiShareCouponRecord(launchType, launchId));
        return ResponseResult.buildSuccessResponse(userCouponResponse);
    }

    /**
     * ???????????????
     *
     * @param orderId
     */
    private void compensateCreateMultiShareCoupon(Long orderId){
        try {
            ResponseResult<OrderDto> simpleOrder = orderService.getSimpleOrderById(orderId);
            if (simpleOrder.isSuccess() && simpleOrder.getData() != null && simpleOrder.getData().getUserId() != null){
                Long userId = simpleOrder.getData().getUserId();
                log.info("??????????????????????????????????????????orderId -> {}, userId -> {}",orderId,userId);
                if (userId != null){
                    UserDto param = new UserDto();
                    param.setId(userId);
                    param.setBusinessLine(BREAKFAST.getCode());
                    ResponseResult<UserDto> result = userService.getUserInfo(param);
                    if (result.isSuccess() && result.getData() != null){
                        UserDto userDto = result.getData();
                        UserCouponRequest couponRequest = UserCouponRequest.builder().userId(userDto.getId()).openid(userDto.getOpenId())
                                .nickName(userDto.getNickName()).headPortraitUrl(userDto.getHeadPortraitUrl())
                                .launchType(BREAKFAST_COUPON_ORDER_SHARE).launchId(orderId).build();
                        couponRequest.setBusinessLine(BREAKFAST.getCode());
                        createMultiShareCoupon(couponRequest);
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
     * ??????????????????
     * @param userCouponResponse
     * @param launchType
     * @param launchId
     * @param userId
     * @param activityId
     * @param userActivityId
     * @param couponName
     * @param clickLimit
     * @return
     */
    private ResponseResult<UserCouponResponse> couponCompleted(UserCouponResponse userCouponResponse,Byte launchType,Long launchId,Long userId,Long activityId,Long userActivityId,String couponName,Integer clickLimit){
        userCouponResponse.setFinished(true);
        // ???????????????????????????
        List<ActivityUserCouponDto> userCouponRecordList = actUserCouponMapper.multiShareCouponRecord(launchType, launchId);
        // ??????????????????
        isReceivedCoupon(userCouponResponse,userId,userActivityId,couponName,true);

        userCouponResponse.setMultiShareCouponRecord(userCouponRecordList);
        //??????????????????????????????
        int count = couponPoolMapper.receiveCountToday(activityId, userId);
        if (count >= clickLimit) {
            userCouponResponse.setOverLimit(true);
        }
        return ResponseResult.buildSuccessResponse(userCouponResponse);
    }

    /**
     * ??????????????????
     * @param userCouponResponse
     * @param userId
     * @param userActivityId
     * @param couponName
     * @param received
     */
    private void isReceivedCoupon(UserCouponResponse userCouponResponse,Long userId,Long userActivityId,String couponName,Boolean received){
        UserCouponDto couponDto = userCouponMapper.queryUserOptimalCouponByUserIdActivityId(userId, userActivityId, CouponEnum.TYPE.DISCOUNT.getCode());
        if (couponDto != null) {
            couponDto.setName(couponName);
            userCouponResponse.setReceiveCoupon(couponDto);
            userCouponResponse.setReceived(received);
        }
    }

    private List<String> batchInsertCouponPool(MultiBreakfastCouponConfig couponConfig,Byte buzLine, Long activityId, Long userActivityId) {
        List<CouponPool> couponPoolList = Lists.newArrayList();
        List<String> couponPoolStrList = Lists.newArrayList();
        couponConfig.getCoupons().stream().forEach(coupon -> {
            Integer quantity = coupon.getQuantity();
            if (quantity != null){
                for (int i=1;i<=quantity;i++){
                    CouponPool pool = new CouponPool();
                    pool.setId(IdGenerator.nextId());
                    pool.setActivityId(activityId);
                    pool.setUserActivityId(userActivityId);
                    pool.setCouponName(coupon.getName());
                    pool.setCouponType(couponConfig.getType());
                    pool.setLimitDays(coupon.getLimitDays());
                    pool.setDiscount(coupon.getDiscount());
                    pool.setIsShare(couponConfig.getIsShare());
                    pool.setValidDays(couponConfig.getValid().intValue());
                    pool.setRule(coupon.getRule());
                    pool.setStatus((byte) 0);
                    pool.setBusinessLine(buzLine);
                    pool.setCreateTime(new Date());
                    pool.setUpdateTime(new Date());
                    couponPoolList.add(pool);
                    couponPoolStrList.add(JSONObject.toJSONString(pool));
                }
            }
        });
        log.info("????????? couponPoolList -> {}",JSONObject.toJSONString(couponPoolList));
        Collections.shuffle(couponPoolList);//????????????
        log.info("????????? couponPoolList -> {}",JSONObject.toJSONString(couponPoolList));
        Collections.shuffle(couponPoolStrList);//????????????
        couponPoolMapper.batchInsert(couponPoolList);
        return couponPoolStrList;
    }

    private Long insertActivityUserCoupon(UserCouponRequest couponRequest, MultiBreakfastCouponConfig couponConfig) throws Exception {
        RedPacketLaunchType launchType = couponRequest.getLaunchType();
        Long launchId = couponRequest.getLaunchId();
        Long userid = couponRequest.getUserId();
        String openid = couponRequest.getOpenid();
        String key = "multi_share_coupon:" + launchId;
        Boolean flag = RedisCache.setIfAbsent(key, "1");
        Long id = 0L;
        try {
            if (couponConfig != null && flag) {
                ActivityUserCoupon activityUserCoupon = new ActivityUserCoupon();
                id = IdGenerator.nextId();
                activityUserCoupon.setId(id);
                activityUserCoupon.setActivityId(couponConfig.getActivityId());
                activityUserCoupon.setType(couponConfig.getType());
                activityUserCoupon.setLaunchType(launchType.getCode());
                activityUserCoupon.setLaunchId(launchId);
                activityUserCoupon.setUserId(userid);
                activityUserCoupon.setOpenId(openid);
                activityUserCoupon.setNickName(couponRequest.getNickName());
                activityUserCoupon.setHeadPortraitUrl(couponRequest.getHeadPortraitUrl());
                activityUserCoupon.setIsSuccess((byte) 0);
                activityUserCoupon.setPlayers(couponConfig.getPlayers());
                activityUserCoupon.setValid(couponConfig.getValid());
                activityUserCoupon.setClickLimit(couponConfig.getClickLimit());
                activityUserCoupon.setClickCount((byte) 0);
                activityUserCoupon.setIsShare(couponConfig.getIsShare());
                activityUserCoupon.setBusinessLine(couponRequest.getBusinessLine());
                activityUserCoupon.setStartDate(couponConfig.getStartDate());
                activityUserCoupon.setEndDate(couponConfig.getEndDate());
                activityUserCoupon.setCreateTime(new Date());
                activityUserCoupon.setUpdateTime(new Date());
                int row = actUserCouponMapper.insertSelective(activityUserCoupon);
                if (row > 0) {
                    return id;
                }
            }
        } catch (Exception e) {
            log.error("", e);
            throw new Exception("??????????????????????????????");
        } finally {
            RedisCache.expire(key, 5, TimeUnit.SECONDS);
        }
        return id;
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param couponList
     * @return
     */
    private List<Long> queryCouponLocksByList(List<UserCouponDto> couponList) {
        try {
            List<Long> couponIds = Lists.transform(couponList, new Function<UserCouponDto, Long>() {
                @Nullable
                @Override
                public Long apply(@Nullable UserCouponDto input) {
                    return input.getId();
                }
            });
            log.info("??????????????????,?????? couponIds -> {} ", couponIds);
            ResponseResult<List<Long>> result = promotionLockerService.getLockerCoupon(couponIds);
            log.info("??????????????????,?????? couponIds -> {} ", JSONObject.toJSONString(result));
            if (result.isSuccess() && CollectionUtils.isNotEmpty(result.getData())) {
                return result.getData();
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return Lists.newArrayList();
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @param couponIds
     * @return
     */
    private List<Long> queryCouponLocksByIds(List<Long> couponIds) {
        try {
            log.info("??????????????????,?????? couponIds -> {} ", couponIds);
            ResponseResult<List<Long>> result = promotionLockerService.getLockerCoupon(couponIds);
            log.info("??????????????????,?????? couponIds -> {} ", JSONObject.toJSONString(result));
            if (result.isSuccess() && CollectionUtils.isNotEmpty(result.getData())) {
                return result.getData();
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return Lists.newArrayList();
    }

    private ResponseResult<Boolean> sendBreakfastGoodsCoupon(UserCouponQueryParam queryParam) {
        List<Long> userids = queryParam.getUserids();
        List<BreakfastCoupon> coupons = null;

        String key = "batch_send_breakfast_goods_coupon_flag";

        // ????????????????????????????????????
        BreakfastCouponConfig couponConfig = ActivityCacheUtil.getBreakfastGoodsCouponConfig();
        if (couponConfig != null) {
            coupons = couponConfig.getCoupons();
        } else {
            log.info("??????????????????????????????????????????");
            return ResponseResult.buildFailResponse(BREAKFAST_COUPON_NOT_EXIST.code(), BREAKFAST_COUPON_NOT_EXIST.message());
        }

        Boolean flag = RedisCache.setIfAbsent(key, "1");
        try {
            if (flag) {
                batchSendCoupon(coupons, couponConfig, userids);
            } else {
                return ResponseResult.buildFailResponse(SENDING_BREAKFAST_COUPON.code(), SENDING_BREAKFAST_COUPON.message());
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            RedisCache.expire(key, 1, TimeUnit.MINUTES);
        }
        return ResponseResult.buildSuccessResponse(SENDING_BREAKFAST_DONE.code(), SENDING_BREAKFAST_DONE.message());
    }

    /**
     * ???????????????
     *
     * @param queryParam
     * @return
     */
    private ResponseResult<Boolean> sendBreakfastDiscountCoupon(UserCouponQueryParam queryParam) {
        Long companyId = queryParam.getCompanyId();
        List<Long> userids = queryParam.getUserids();
        List<BreakfastCoupon> coupons = null;
        BreakfastCouponConfig couponConfig = null;

        String key = "batch_send_breakfast_coupon_flag";

        if (companyId != null) {
            key = key + ":" + companyId;
            // ???????????????????????????????????????
            couponConfig = ActivityCacheUtil.getBreafastCouponByCompanyId(companyId);
            if (couponConfig != null) {
                coupons = couponConfig.getCoupons();
            } else {
                log.info("????????????????????????????????????");
                return ResponseResult.buildFailResponse(BREAKFAST_COUPON_NOT_EXIST.code(), BREAKFAST_COUPON_NOT_EXIST.message());
            }
        } else {
            // ??????????????????????????????
            couponConfig = ActivityCacheUtil.getDefaultBreakfastConfig();
            if (couponConfig != null) {
                coupons = couponConfig.getCoupons();
            } else {
                log.info("????????????????????????????????????");
                return ResponseResult.buildFailResponse(BREAKFAST_COUPON_NOT_EXIST.code(), BREAKFAST_COUPON_NOT_EXIST.message());
            }
        }

        Boolean flag = RedisCache.setIfAbsent(key, "1");
        try {
            if (flag) {
                batchSendCoupon(coupons, couponConfig, userids);
            } else {
                return ResponseResult.buildFailResponse(SENDING_BREAKFAST_COUPON.code(), SENDING_BREAKFAST_COUPON.message());
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            RedisCache.expire(key, 1, TimeUnit.MINUTES);
        }
        return ResponseResult.buildSuccessResponse(SENDING_BREAKFAST_DONE.code(), SENDING_BREAKFAST_DONE.message());
    }

    private Boolean batchSendCoupon(List<BreakfastCoupon> coupons, BreakfastCouponConfig couponConfig, List<Long> userids) {
        List<UserCoupon> userCouponList = Lists.newArrayList();
        Map<String, Object> sms = Maps.newHashMap();

        Map<Long, String> mobileMap = commonService.fetchUserMobile(userids);
        for (Long userId : userids) {
            String mobileNo = mobileMap.get(userId);
            if (StringUtils.isNotBlank(mobileNo)) {
                sms.put(mobileMap.get(userId), couponConfig.getSmsMsg());
            }
            if (CollectionUtils.isNotEmpty(coupons)) {
                DateTime now = new DateTime();
                for (BreakfastCoupon coupon : coupons) {
                    DateTime validDate = now.plusDays(coupon.getValid());
                    Date expireTime = new DateTime(validDate.getYear(), validDate.getMonthOfYear(), validDate.getDayOfMonth(), 23, 59, 59).toDate();

                    UserCoupon userCoupon = new UserCoupon();
                    userCoupon.setId(IdGenerator.nextId());
                    userCoupon.setActivityId(couponConfig.getActivityId());
                    userCoupon.setName(coupon.getName());
                    userCoupon.setType(couponConfig.getType());
                    userCoupon.setUserId(userId);
                    userCoupon.setDays(coupon.getDays());
                    userCoupon.setGoodsCode(coupon.getGoodsCode());
                    userCoupon.setDiscount(coupon.getDiscount());
                    userCoupon.setExpireTime(expireTime);
                    userCoupon.setIsShare((byte) 0);//???????????????
                    userCoupon.setIsValid((byte) 1);
                    userCoupon.setIsUsed((byte) 0);
                    userCoupon.setRule(coupon.getRule());
                    userCoupon.setBusinessLine(BREAKFAST.getCode());
                    userCoupon.setCreateTime(new Date());
                    userCoupon.setUpdateTime(new Date());

                    userCouponList.add(userCoupon);
                }
            }
        }
        int row = userCouponMapper.insertBatch(userCouponList);
        if (row > 0) {
            if (StringUtils.isNotBlank(couponConfig.getSmsMsg())) {
                //????????????
                commonService.sendBreakfastSms(sms);
            }
        }
        return Boolean.TRUE;
    }

    private Integer sendAndRecordCouponWater(Long sendCouponRecordId,List<CouponDto> coupons, List<UserDeliveryAddressDto> userList, Date sendTime,String smsMsg) {
        if (CollectionUtils.isEmpty(userList)) {
            return 0;
        }
        List<UserCoupon> userCouponList = Lists.newArrayList();
        Map<String, Object> sms = Maps.newHashMap();

        List<SendCouponWater> sendCouponWaters = Lists.newArrayList();

        for (UserDeliveryAddressDto userDeliveryAddress : userList) {
            if (StringUtils.isNotBlank(userDeliveryAddress.getMobileNo())){
                sms.put(userDeliveryAddress.getMobileNo(), smsMsg);
            }

            if (CollectionUtils.isNotEmpty(coupons)) {
                DateTime now = new DateTime();
                for (CouponDto coupon : coupons) {
                    DateTime validDate = now.plusDays(coupon.getValidDays());
                    Date expireTime = new DateTime(validDate.getYear(), validDate.getMonthOfYear(), validDate.getDayOfMonth(), 23, 59, 59).toDate();

                    UserCoupon userCoupon = new UserCoupon();
                    userCoupon.setId(IdGenerator.nextId());
                    userCoupon.setName(coupon.getName());
                    userCoupon.setType(coupon.getType());
                    userCoupon.setUserId(userDeliveryAddress.getUserId());
                    userCoupon.setDays(coupon.getLimitDays());
                    userCoupon.setGoodsCode(coupon.getGoodsCode());
                    userCoupon.setDiscount(coupon.getDiscount());
                    userCoupon.setExpireTime(expireTime);
                    userCoupon.setIsShare((byte) 0);//???????????????
                    userCoupon.setIsValid((byte) 1);
                    userCoupon.setIsUsed((byte) 0);
                    userCoupon.setRule(coupon.getRule());
                    userCoupon.setBusinessLine(BREAKFAST.getCode());
                    userCoupon.setCreateTime(new Date());
                    userCoupon.setUpdateTime(new Date());

                    userCouponList.add(userCoupon);
                }
                sendCouponWaters.add(initSendCouponWater(sendCouponRecordId,userDeliveryAddress,sendTime));
            }
        }
        int row = userCouponMapper.insertBatch(userCouponList);
        if (row > 0) {
            row = sendCouponWaterMapper.insertBatch(sendCouponWaters);
            if (row > 0){
                if (StringUtils.isNotBlank(smsMsg)) {
                    //????????????
                    commonService.sendBreakfastSms(sms);
                }
            }
        }
        return row;
    }

    private SendCouponWater initSendCouponWater(Long sendCouponRecordId,UserDeliveryAddressDto userDeliveryAddress,Date sendTime){
        SendCouponWater sendCouponWater = new SendCouponWater();
        sendCouponWater.setId(IdGenerator.nextId());
        sendCouponWater.setSendCouponRecordId(sendCouponRecordId);
        sendCouponWater.setUserId(userDeliveryAddress.getUserId());
        sendCouponWater.setUserName(userDeliveryAddress.getRealName());
        sendCouponWater.setMobileNo(userDeliveryAddress.getMobileNo());
        sendCouponWater.setSex(userDeliveryAddress.getSex()==null?null:userDeliveryAddress.getSex().byteValue());
        sendCouponWater.setDeliveryCompany(userDeliveryAddress.getDeliveryCompany());
        sendCouponWater.setSendTime(sendTime);
        sendCouponWater.setBusinessLine(BREAKFAST.getCode());
        sendCouponWater.setCreateTime(new Date());
        sendCouponWater.setUpdateTime(new Date());
        return sendCouponWater;
    }

}