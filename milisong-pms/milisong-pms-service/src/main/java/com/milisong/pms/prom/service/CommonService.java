package com.milisong.pms.prom.service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.milisong.pms.prom.client.PromClient;
import com.milisong.pms.prom.domain.UserCoupon;
import com.milisong.pms.prom.dto.CouponDto;
import com.milisong.pms.prom.dto.UserCouponDto;
import com.milisong.pms.prom.mapper.UserCouponMapper;
import com.milisong.ucs.api.UserService;
import com.milisong.ucs.dto.Pagenation;
import com.milisong.ucs.dto.UserDto;
import com.milisong.ucs.enums.BusinessLineEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.milisong.ucs.enums.BusinessLineEnum.BREAKFAST;

/**
 * @author sailor wang
 * @date 2018/12/12 1:56 PM
 * @description
 */
@Slf4j
@Service
public class CommonService {

    @Autowired
    private PromClient promClient;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCouponMapper userCouponMapper;

    /**
     * 选折扣券
     *
     * @param userCouponList
     * @param totalDays
     * @return
     */
    public UserCouponDto filterOptimalBreakfastDiscountCouponAmount(List<UserCouponDto> userCouponList, Integer totalDays) {
        if (CollectionUtils.isEmpty(userCouponList)) {
            log.info("没有可用优惠券");
            return null;
        }
        Map<String, List<UserCouponDto>> userCouponMap = new HashMap<>();
        BigDecimal bestDiscount = null;
        for (UserCouponDto userCoupon : userCouponList) {
            BigDecimal price = userCoupon.getDiscount().setScale(1, BigDecimal.ROUND_HALF_UP);

            //找出最优的价格
            if (bestDiscount == null) {
                bestDiscount = price;
            } else {
                if (price.compareTo(bestDiscount) < 0) {
                    bestDiscount = price;
                }
            }

            String discount = price.toPlainString();
            if (userCouponMap.containsKey(discount)) {
                List<UserCouponDto> list = userCouponMap.get(discount);
                list.add(userCoupon);
                userCouponMap.put(discount, list);
            } else {
                userCouponMap.put(discount, Lists.newArrayList(userCoupon));
            }
        }
        // 价格最优列表
        List<UserCouponDto> priceList = userCouponMap.get(bestDiscount.toString());
        if (priceList.size() == 1) {
            return priceList.get(0);
        }

        return filterOptimalSuitableDayAndExpireTime(priceList, totalDays);
    }

    /**
     * 选商品券
     *
     * @param goodsMap
     * @param goodsCouponUsableList
     * @param totalDays
     * @return
     */
    public UserCouponDto filterOptimalBreakfastGoodsCouponAmount(Map<String, BigDecimal> goodsMap, List<UserCouponDto> goodsCouponUsableList, Integer totalDays) {
        if (CollectionUtils.isEmpty(goodsCouponUsableList)) {
            log.info("没有可用优惠券");
            return null;
        }
        log.info("商品价格映射 goodsMap -> {}", JSONObject.toJSONString(goodsMap));
        Map<String, List<UserCouponDto>> userCouponMap = new HashMap<>();
        BigDecimal bestDiscount = null;
        for (UserCouponDto userCoupon : goodsCouponUsableList) {
            String goodsCode = userCoupon.getGoodsCode();
            // 优惠力度 = 商品价格 - x元购
            BigDecimal price = goodsMap.get(goodsCode).subtract(userCoupon.getDiscount());
            log.info("最优价格 price -> {}",price);

            //找出最优的价格
            if (bestDiscount == null) {
                bestDiscount = price;
            } else {
                if (price.compareTo(bestDiscount) > 0) {
                    bestDiscount = price;
                }
            }

            String discount = price.toPlainString();
            if (userCouponMap.containsKey(discount)) {
                List<UserCouponDto> list = userCouponMap.get(discount);
                list.add(userCoupon);
                userCouponMap.put(discount, list);
            } else {
                userCouponMap.put(discount, Lists.newArrayList(userCoupon));
            }
        }
        // 价格最优列表
        List<UserCouponDto> priceList = userCouponMap.get(bestDiscount.toString());
        if (priceList.size() == 1) {
            return priceList.get(0);
        }
        return filterOptimalSuitableDayAndExpireTime(priceList, totalDays);
    }

    /**
     * 筛选出天数匹配or最早过期的
     *
     * @param priceList
     * @param totalDays
     * @return
     */
    private UserCouponDto filterOptimalSuitableDayAndExpireTime(List<UserCouponDto> priceList, Integer totalDays) {
        Map<Integer, List<UserCouponDto>> matchDaysMap = new HashMap<>();
        Integer matchDays = null;
        // 找出天数最匹配的
        for (UserCouponDto userCoupon : priceList) {
            Integer days = userCoupon.getDays();
            if (totalDays.equals(days)) {
                matchDays = days;
                if (matchDaysMap.containsKey(matchDays)) {
                    List<UserCouponDto> valList = matchDaysMap.get(matchDays);
                    valList.add(userCoupon);
                    matchDaysMap.put(matchDays, valList);
                } else {
                    matchDaysMap.put(matchDays, Lists.newArrayList(userCoupon));
                }
            } else {
                if (matchDays != null) {
                    if (matchDays.equals(days)) {
                        List<UserCouponDto> valList = matchDaysMap.get(matchDays);
                        valList.add(userCoupon);
                        matchDaysMap.put(matchDays, valList);
                    } else if (days > matchDays) {
                        matchDays = days;
                        if (matchDaysMap.containsKey(matchDays)) {
                            List<UserCouponDto> valList = matchDaysMap.get(matchDays);
                            valList.add(userCoupon);
                            matchDaysMap.put(matchDays, valList);
                        } else {
                            matchDaysMap.put(matchDays, Lists.newArrayList(userCoupon));
                        }
                    }
                } else {
                    matchDays = days;
                    matchDaysMap.put(matchDays, Lists.newArrayList(userCoupon));
                }
            }
        }

        // 天数匹配最优列表
        List<UserCouponDto> dayList = matchDaysMap.get(matchDays);
        if (dayList.size() == 1) {
            return dayList.get(0);
        }

        Date nearestDay = null;
        Map<Date, List<UserCouponDto>> nearestDayMap = new HashMap<>();
        // 找出最早过期的券
        for (UserCouponDto userCoupon : dayList) {
            Date expireDate = userCoupon.getExpireTime();
            if (nearestDay == null) {
                nearestDay = expireDate;
                nearestDayMap.put(nearestDay, Lists.newArrayList(userCoupon));
            } else {
                if (expireDate.before(nearestDay) || expireDate.equals(nearestDay)) {
                    nearestDay = expireDate;
                    if (nearestDayMap.containsKey(expireDate)) {
                        List<UserCouponDto> valList = nearestDayMap.get(expireDate);
                        valList.add(userCoupon);
                        nearestDayMap.put(expireDate, valList);
                    } else {
                        nearestDayMap.put(expireDate, Lists.newArrayList(userCoupon));
                    }
                }
            }
        }
        List<UserCouponDto> list = nearestDayMap.get(nearestDay);
        return list.get(0);
    }

    public void sendBreakfastSms(Map<String, Object> sms) {
        try {
            log.info("sms -> {}", sms);
            promClient.sendBreakfastSms(sms);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void sendLunchSms(Map<String, Object> sms) {
        try {
            log.info("sms -> {}", sms);
            promClient.sendAdvertiseSms(sms);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public Map<Long,String> fetchUserMobile(List<Long> userids){
        UserDto userDto = new UserDto();
        userDto.setIds(userids);
        ResponseResult<List<UserDto>> result = userService.fetchUserInfoByIds(userDto);
        if (result.isSuccess() && CollectionUtils.isNotEmpty(result.getData())){
            Map<Long,String> map = Maps.newHashMap();
            result.getData().stream().forEach(user -> {
                map.put(user.getId(),user.getMobileNo());
            });
            return map;
        }
        return Maps.newHashMap();
    }

    public Pagenation<UserDto> fetchUsers(Long stepUserid,Integer fetchSize){
        UserDto user = new UserDto();
        user.setId(stepUserid);
        user.setBusinessLine(BusinessLineEnum.LUNCH.getCode());
        user.setFetchSize(fetchSize);
        ResponseResult<Pagenation<UserDto>> result = userService.fetchUser(user);
        return result.getData();
    }

    /**
     *
     * @param activityId
     * @param userActivityId
     * @param userActivityType
     * @param userId
     * @param couponList
     */
    public Boolean batchReceiveCoupon(Long activityId, Long userActivityId, Byte userActivityType, Long userId, List<CouponDto> couponList){
        if (CollectionUtils.isNotEmpty(couponList)) {
            DateTime now = new DateTime();

            List<UserCoupon> userCouponList = Lists.newArrayList();
            for (CouponDto coupon : couponList) {
                DateTime validDate = now.plusDays(coupon.getValidDays());
                Date expireTime = new DateTime(validDate.getYear(), validDate.getMonthOfYear(), validDate.getDayOfMonth(), 23, 59, 59).toDate();

                UserCoupon userCoupon = new UserCoupon();
                userCoupon.setId(IdGenerator.nextId());
                userCoupon.setActivityId(activityId);
                userCoupon.setUserActivityId(userActivityId);
                userCoupon.setUserActivityType(userActivityType);
                userCoupon.setName(coupon.getName());
                userCoupon.setType(coupon.getType());
                userCoupon.setUserId(userId);
                userCoupon.setDays(coupon.getLimitDays());
                userCoupon.setGoodsCode(coupon.getGoodsCode());
                userCoupon.setDiscount(coupon.getDiscount());
                userCoupon.setExpireTime(expireTime);
                userCoupon.setIsShare((byte) 0);//默认不同享
                userCoupon.setIsValid((byte) 1);
                userCoupon.setIsUsed((byte) 0);
                userCoupon.setRule(coupon.getRule());
                userCoupon.setBusinessLine(BREAKFAST.getCode());
                userCoupon.setCreateTime(now.toDate());
                userCoupon.setUpdateTime(now.toDate());
                userCouponList.add(userCoupon);
            }
            int row = userCouponMapper.insertBatch(userCouponList);
            return row > 0;
        }
        return Boolean.FALSE;
    }


    private static String OLD_PULL_NEW_PV_KEY = "activity_pv_counter:old_pull_new";

    public Long countOldPullNewPV(){
        try {
            Long count = RedisCache.incrBy(OLD_PULL_NEW_PV_KEY,1);
            if (count < 1662){
                count = RedisCache.incrBy(OLD_PULL_NEW_PV_KEY,1662);
            }
            return count;
        } catch (Exception e) {
            log.error("",e);
        }
        return 1662L;//初始值
    }

    public Long getOldPullNewPV(){
        try {
            Long count = Long.valueOf(RedisCache.get(OLD_PULL_NEW_PV_KEY));
            if (count < 1662){
                count = 1662L;
            }
            return count;
        } catch (NumberFormatException e) {
            log.error("",e);
        }
        return 1662L;//初始值
    }

}