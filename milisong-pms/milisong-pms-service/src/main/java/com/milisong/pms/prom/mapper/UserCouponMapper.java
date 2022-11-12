package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.UserCoupon;
import com.milisong.pms.prom.dto.UserCouponDto;
import com.milisong.pms.prom.dto.UserCouponQueryParam;
import com.milisong.pms.prom.dto.UserRedPacketDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface UserCouponMapper {
    int insert(UserCoupon record);

    int insertSelective(UserCoupon record);

    UserCoupon selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserCoupon record);

    int updateByPrimaryKey(UserCoupon record);

    int insertBatch(List<UserCoupon> userCouponList);

    List<UserCouponDto> queryUserCoupon(@Param("queryParam")UserCouponQueryParam queryParam);

    Integer hasReceivedCoupon(@Param("userId")Long userId,@Param("buzline")Byte buzline,@Param("couponType")Byte couponType);

    Integer countUserCoupon(@Param("queryParam")UserCouponQueryParam queryParam);

    int scanBreakfastCoupon(@Param("currentDate") Date currentDate);

    List<UserCouponDto> queryUsefulCouponUnlimit(@Param("queryParam")UserCouponQueryParam queryParam);

    List<Long> usableCouponsCount(@Param("queryParam")UserCouponQueryParam queryParam);

    UserCouponDto queryUserOptimalCouponByUserIdActivityId(@Param("userId") Long userId,@Param("userActivityId") Long userActivityId,@Param("type") Byte type);

    List<UserCouponDto> queryUserCouponByActivity(@Param("userActivityId")Long userActivityId,@Param("userActivityType") Byte userActivityType,@Param("userId") Long userId,@Param("couponType") Byte couponType);

    int countUserCouponByActivity(@Param("userActivityId")Long userActivityId,@Param("userActivityType") Byte userActivityType,@Param("userId") Long userId,@Param("couponType") Byte couponType);
}