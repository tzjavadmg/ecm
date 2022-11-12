package com.milisong.breakfast.scm.configuration.mapper;

import com.milisong.breakfast.scm.configuration.domain.ShopSingleConfig;

public interface ShopSingleConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopSingleConfig record);

    int insertSelective(ShopSingleConfig record);

    ShopSingleConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopSingleConfig record);

    int updateByPrimaryKey(ShopSingleConfig record);
}