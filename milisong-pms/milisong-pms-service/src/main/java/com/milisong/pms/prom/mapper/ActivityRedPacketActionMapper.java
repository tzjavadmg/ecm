package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.ActivityRedPacketAction;
import com.milisong.pms.prom.dto.ActivityRedPacketActionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ActivityRedPacketActionMapper {

    int insert(ActivityRedPacketAction record);

    int insertSelective(ActivityRedPacketAction record);

    ActivityRedPacketAction selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActivityRedPacketAction record);

    int updateByPrimaryKey(ActivityRedPacketAction record);

    int hackRedPacketTimes(@Param("userId") Long userId);

    Integer hackActivityRedPacketTimes(@Param("userId") Long userId, @Param("activityUserRedPacketId") Long activityUserRedPacketId);

    List<ActivityRedPacketActionDto> hackRedPacketRecord(@Param("activityUserRedPacketId") Long userRedPacketId);
}