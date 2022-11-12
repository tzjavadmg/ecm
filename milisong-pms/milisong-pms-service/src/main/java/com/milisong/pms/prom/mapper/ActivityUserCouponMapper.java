package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.ActivityUserCoupon;
import com.milisong.pms.prom.dto.ActivityUserCouponDto;
import com.milisong.pms.prom.dto.ActivityUserRedPacketDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityUserCouponMapper {

    int insert(ActivityUserCoupon record);

    int insertSelective(ActivityUserCoupon record);

    ActivityUserCoupon selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActivityUserCoupon record);

    int updateByPrimaryKey(ActivityUserCoupon record);

    int countActivityUserCouponByLaunch(@Param("launchType") Byte launchType,@Param("launchId") Long launchId,@Param("buzLine") Byte buzLine);

    ActivityUserCouponDto queryActivityUserCouponByLaunch(@Param("launchType") Byte launchType,@Param("launchId") Long launchId,@Param("buzLine") Byte buzLine);

    List<ActivityUserCouponDto> multiShareCouponRecord(@Param("launchType") Byte launchType, @Param("launchId") Long launchId);
}