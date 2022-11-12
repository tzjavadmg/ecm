package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.MarketingQrcode;
import com.milisong.pms.prom.domain.UserRedPacket;
import com.milisong.pms.prom.dto.UserRedPacketDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Mapper
public interface UserRedPacketMapper {
    int insert(UserRedPacket record);

    int insertSelective(UserRedPacket record);

    UserRedPacket selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserRedPacket record);

    int updateByPrimaryKey(UserRedPacket record);

    List<UserRedPacketDto> queryUserRedPacket(@Param("userId") Long userId, @Param("type") Byte type, @Param("usable") Boolean usable);

    int usableUserRedPacketCount(@Param("userId") Long userId);

    int totalCount(@Param("userId") Long userId, @Param("usable") Boolean usable);

    List<UserRedPacketDto> userRedPacketList(@Param("userId") Long userId, @Param("startRow") int startRow, @Param("pageSize") int pageSize, @Param("usable") Boolean usable);

    UserRedPacketDto queryUserRedPacketByOrderId(@Param("deliveryId") Long orderId);

    int scanUserRedPacket(@Param("currentDate") Date currentDate);

    UserRedPacketDto queryUserRedPacketByUserActivityId(@Param("userId") Long userId, @Param("userActivityId") Long userActivityId, @Param("type") Byte type);

    int insertBatch(List<UserRedPacket> list);

    List<UserRedPacketDto> toastUserRedPacket(@Param("userId") Long userId,@Param("limit") Integer num,@Param("currentDate") Date currentDate);

    Set<Long> filterUserRedPacketWithAmount(@Param("ids")List<Long> ids,@Param("conditionAmount") BigDecimal conditionAmount);
}