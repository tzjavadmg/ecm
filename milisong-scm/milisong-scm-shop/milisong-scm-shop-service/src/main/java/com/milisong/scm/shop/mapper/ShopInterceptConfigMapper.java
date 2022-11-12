package com.milisong.scm.shop.mapper;

import com.milisong.scm.shop.domain.ShopInterceptConfig;

public interface ShopInterceptConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopInterceptConfig record);

    int insertSelective(ShopInterceptConfig record);

    ShopInterceptConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopInterceptConfig record);

    int updateByPrimaryKey(ShopInterceptConfig record);
}