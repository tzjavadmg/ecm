package com.milisong.ecm.common.user.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.milisong.ecm.common.user.domain.Company;


@Mapper
public interface CompanyMapper {
    int insert(Company record);

    int insertSelective(Company record);

    Company selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Company record);

    int updateByPrimaryKey(Company record);
}
