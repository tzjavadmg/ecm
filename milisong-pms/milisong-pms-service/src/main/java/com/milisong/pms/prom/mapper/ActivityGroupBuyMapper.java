package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.ActivityGroupBuy;
import com.milisong.pms.prom.dto.groupbuy.ActivityGroupBuyDto;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface ActivityGroupBuyMapper {
    int insert(ActivityGroupBuy record);

    int insertSelective(ActivityGroupBuy record);

    ActivityGroupBuy selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ActivityGroupBuy record);

    int updateByPrimaryKey(ActivityGroupBuy record);

    ActivityGroupBuyDto queryActiveGroupBuyDetail(@Param("currentDate") Date now);

    ActivityGroupBuy selectByUserGroupBuyId(@Param("userGroupBuyId")Long userGroupBuyId);
}