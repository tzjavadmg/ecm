package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.RedPacketPool;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RedPacketPoolMapper {
    int insert(RedPacketPool record);

    int insertSelective(RedPacketPool record);

    RedPacketPool selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RedPacketPool record);

    int updateByPrimaryKey(RedPacketPool record);

    int batchInsert(@Param("redPacketPool") List<RedPacketPool> redPacketPoolList);

    int receiveCount(@Param("activityId") Long activityId, @Param("userId") Long userId, @Param("userActivityId") Long userActivityId);

    int receiveCountToday(@Param("activityId") Long activityId, @Param("userId") Long userId);
}