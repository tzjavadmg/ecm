package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.UserGroupBuy;
import com.milisong.pms.prom.dto.groupbuy.UserGroupBuyDto;
import org.apache.ibatis.annotations.Param;

public interface UserGroupBuyMapper {
    int insert(UserGroupBuy record);

    int insertSelective(UserGroupBuy record);

    UserGroupBuy selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserGroupBuy record);

    int updateByPrimaryKey(UserGroupBuy record);

    UserGroupBuyDto queryUserGroupBuyInfo(@Param("userGroupBuyId")Long userGroupBuyId);
}