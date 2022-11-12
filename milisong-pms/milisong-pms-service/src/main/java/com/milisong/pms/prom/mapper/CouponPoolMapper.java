package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.CouponPool;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CouponPoolMapper {

    int insert(CouponPool record);

    int insertSelective(CouponPool record);

    CouponPool selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponPool record);

    int updateByPrimaryKey(CouponPool record);

    Integer batchInsert(@Param("couponPool")List<CouponPool> redPacketPoolList);

    int receiveCount(@Param("activityId") Long activityId, @Param("userId") Long userId, @Param("userActivityId") Long userActivityId);

    int receiveCountToday(@Param("activityId") Long activityId,@Param("userId") Long userId);

}