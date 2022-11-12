package com.milisong.scm.shop.mapper;

import com.milisong.scm.shop.domain.GlobalConfig;

public interface GlobalConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(GlobalConfig record);

    int insertSelective(GlobalConfig record);

    GlobalConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GlobalConfig record);

    int updateByPrimaryKey(GlobalConfig record);
}