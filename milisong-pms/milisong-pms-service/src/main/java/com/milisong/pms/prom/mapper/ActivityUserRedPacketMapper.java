package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.ActivityUserRedPacket;
import com.milisong.pms.prom.dto.ActivityUserRedPacketDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ActivityUserRedPacketMapper {
    int insert(ActivityUserRedPacket record);

    int insertSelective(ActivityUserRedPacket record);

    ActivityUserRedPacket selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActivityUserRedPacket record);

    int updateByPrimaryKey(ActivityUserRedPacket record);

    ActivityUserRedPacketDto queryUserRedPacket(@Param("userId") Long userId, @Param("type") Byte type);

    ActivityUserRedPacketDto queryUserRedPacketByLaunch(@Param("launchType") Byte launchType, @Param("launchId") Long launchId);

    List<ActivityUserRedPacketDto> multiShareRedPacketRecord(@Param("launchType") Byte launchType, @Param("launchId") Long launchId);
}