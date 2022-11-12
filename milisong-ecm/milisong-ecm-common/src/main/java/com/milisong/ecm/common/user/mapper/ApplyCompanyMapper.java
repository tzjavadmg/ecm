package com.milisong.ecm.common.user.mapper;

import com.milisong.ecm.common.user.domain.ApplyCompany;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplyCompanyMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ApplyCompany record);

    int insertSelective(ApplyCompany record);

    ApplyCompany selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ApplyCompany record);

    int updateByPrimaryKey(ApplyCompany record);
}