package com.milisong.ucs.mapper;

import com.milisong.ucs.domain.User;
import com.milisong.ucs.dto.SendLunchRedPacketRequest;
import com.milisong.ucs.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByOpenid(@Param("openid") String openid, @Param("registerSource") Integer registerSource, @Param("businessLine") Byte businessLine);

    int updateByOpenid(UserDto userDto);

    List<UserDto> fetchUser(@Param("userId")Long userId, @Param("fetchSize") Integer fetchSize,@Param("businessLine") Byte businessLine);

    Long countWithMobile(@Param("businessLine") Byte businessLine);

    List<UserDto> fetchUserInfoByIds(@Param("ids") List<Long> ids);

    int calcPoint(@Param("id") Long id,@Param("action") Byte action,@Param("point") Integer point);

    List<UserDto> fetchUserInfoByMobileNos(@Param("mobileNos")List<String> mobileNos,@Param("businessLine") Byte businessLine);

    List<UserDto> queryUserWithMobile(SendLunchRedPacketRequest redPacketRequest);

    int batchUpdateUserStatus(@Param("ids")List<Long> userIdList);
}