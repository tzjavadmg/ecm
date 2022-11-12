package com.milisong.breakfast.scm.configuration.mapper;

import com.milisong.breakfast.scm.configuration.domain.ShopInterceptConfig;

public interface ShopInterceptConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopInterceptConfig record);

    int insertSelective(ShopInterceptConfig record);

    ShopInterceptConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopInterceptConfig record);

    int updateByPrimaryKey(ShopInterceptConfig record);
}