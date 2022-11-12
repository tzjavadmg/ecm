package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.Activity;
import com.milisong.pms.prom.dto.ActivityDto;
import com.milisong.pms.prom.dto.ActivityRedPacketDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ActivityMapper {

    int insert(Activity record);

    int insertSelective(Activity record);

    Activity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Activity record);

    int updateByPrimaryKey(Activity record);

    List<ActivityDto> queryActivityByStatus(@Param("activityStatus") List<Integer> status);

    int offlineRedPacketActivity(@Param("currentDate") Date currentDate);

    int onlineRedPacketActivity(@Param("currentDate") Date currentDate);

    ActivityRedPacketDto currentActivityRedPacket(@Param("currentDate") Date currentDate, @Param("redPacketType") Byte redPacketType);

    int conflictActivityRedPacket(@Param("activityId") Long activityId, @Param("startDate") Date startDate);

    int countActivityRedPacket(@Param("buzLine") Byte buzLine);

    List<ActivityRedPacketDto> queryActivityRedPacketList(@Param("startRow") int startRow,@Param("pageSize") int pageSize,@Param("buzLine") Byte buzLine);
}