package com.milisong.breakfast.scm.configuration.mapper;

import com.milisong.breakfast.scm.configuration.domain.ShopBanner;

public interface ShopBannerMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ShopBanner record);

    int insertSelective(ShopBanner record);

    ShopBanner selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopBanner record);

    int updateByPrimaryKey(ShopBanner record);
}