package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.UserGroupBuyRecord;
import com.milisong.pms.prom.dto.groupbuy.GroupBuyResponse;
import com.milisong.pms.prom.dto.groupbuy.JoinUser;
import com.milisong.pms.prom.dto.groupbuy.UserGroupBuyDto;
import com.milisong.pms.prom.dto.groupbuy.UserGroupBuyRecordDto;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserGroupBuyRecordMapper {
    int insert(UserGroupBuyRecord record);

    int insertSelective(UserGroupBuyRecord record);

    UserGroupBuyRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserGroupBuyRecord record);

    int updateByPrimaryKey(UserGroupBuyRecord record);

    UserGroupBuyDto queryJoinedGroupBuy(@Param("groupBuyId") Long groupBuyId, @Param("userId") Long userId, @Param("curDate") Date now);

    List<JoinUser> fetchGroupBuyJoinedUsers(@Param("userGroupBuyId")Long userGroupBuyId);

    int countJoinedTimesExcludeCurrentUser(@Param("userGroupBuyId") Long userGroupBuyId,@Param("userId") Long userId);

    int updateStatusAfterCreateGroupBuy(@Param("recordId") Long userGroupBuyRecordId);

    UserGroupBuyDto queryJoinedGroupBuyByIdUid(@Param("id") Long userGroupBuyId,@Param("userId") Long userId);

    int completeGroupBuy(@Param("recordId") Long userGroupBuyRecordId);

    UserGroupBuyRecord hasJoinedThisGroup(@Param("userGroupBuyId") Long userGroupBuyId,@Param("userId") Long userId);

    UserGroupBuyRecord selectByOrderId(@Param("orderId") Long orderId);

    int releaseGroupBuyLock(@Param("orderId") Long orderId);

    List<UserGroupBuyRecordDto> queryJoinedGroupBuyList(@Param("curDate") Date curDate);

    int batchRefundGroupBuy(@Param("records") List<UserGroupBuyRecordDto> userGroupBuyRecordList);

    int refundGroupBuy(@Param("userGroupBuyId") Long userGroupBuyId);

    int countLockedNumExcludeMeIfPresent(@Param("userGroupBuyId") Long userGroupBuyId,@Param("userId") Long userId);

    UserGroupBuyRecord queryReentrentUser(@Param("userGroupBuyId") Long userGroupBuyId,@Param("userId") Long userId);
}