package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.ActivityUserInvite;
import com.milisong.pms.prom.dto.invite.UserInviteRecordDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityUserInviteMapper {

    int insert(ActivityUserInvite record);

    int insertSelective(ActivityUserInvite record);

    ActivityUserInvite selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActivityUserInvite record);

    int updateByPrimaryKey(ActivityUserInvite record);

    ActivityUserInvite selectByOriginateUserId(@Param("id") Long id,@Param("type") Byte type);
}