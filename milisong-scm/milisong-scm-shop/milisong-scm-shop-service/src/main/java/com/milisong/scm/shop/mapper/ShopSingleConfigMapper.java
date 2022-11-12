package com.milisong.scm.shop.mapper;

import com.milisong.scm.shop.domain.ShopSingleConfig;

public interface ShopSingleConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopSingleConfig record);

    int insertSelective(ShopSingleConfig record);

    ShopSingleConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopSingleConfig record);

    int updateByPrimaryKey(ShopSingleConfig record);
}