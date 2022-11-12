package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.UserInviteRecord;
import com.milisong.pms.prom.dto.invite.UserInviteRecordDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInviteRecordMapper {

    int insert(UserInviteRecord record);

    int insertSelective(UserInviteRecord record);

    UserInviteRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserInviteRecord record);

    int updateByPrimaryKey(UserInviteRecord record);

    List<UserInviteRecord> queryByActivityId(Long activityInviteId);

    List<UserInviteRecordDto> queryByOriginateUserId(Long originateUserId);

    UserInviteRecord queryByReceiveUserId(Long receiveUserId);

    Integer sumByActivityInviteIdAndStatus(@Param("activityInviteId") Long activityInviteId, @Param("status") Byte status);

}