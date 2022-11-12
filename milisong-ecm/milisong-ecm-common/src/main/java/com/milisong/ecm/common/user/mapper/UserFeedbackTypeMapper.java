package com.milisong.ecm.common.user.mapper;


import com.milisong.ecm.common.user.domain.UserFeedbackType;
import com.milisong.ecm.common.user.dto.UserFeedbackTypeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFeedbackTypeMapper {

    int insert(UserFeedbackType record);

    int insertSelective(UserFeedbackType record);

    UserFeedbackType selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserFeedbackType record);

    int updateByPrimaryKey(UserFeedbackType record);
    List<UserFeedbackTypeDto> feedbackType(@Param("buzLine") Byte buzLine);

    List<UserFeedbackTypeDto> feedbackTypeTransit(@Param("buzLine") Byte buzLine);
}