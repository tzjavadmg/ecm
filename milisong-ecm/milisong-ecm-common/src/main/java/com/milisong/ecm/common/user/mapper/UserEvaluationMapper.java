package com.milisong.ecm.common.user.mapper;


import com.milisong.ecm.common.user.domain.UserEvaluation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserEvaluationMapper {
    int insert(UserEvaluation record);

    int insertSelective(UserEvaluation record);

    UserEvaluation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserEvaluation record);

    int updateByPrimaryKey(UserEvaluation record);

    int selectByDeliveryId(Long deliveryId);
}