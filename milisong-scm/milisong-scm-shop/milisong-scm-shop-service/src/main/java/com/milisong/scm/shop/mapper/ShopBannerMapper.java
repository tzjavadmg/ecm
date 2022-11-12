package com.milisong.scm.shop.mapper;

import com.milisong.scm.shop.domain.ShopBanner;
import com.milisong.scm.shop.domain.ShopBannerExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ShopBannerMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ShopBanner record);

    int insertSelective(ShopBanner record);

    List<ShopBanner> selectByExample(ShopBannerExample example);

    ShopBanner selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ShopBanner record);

    int updateByPrimaryKey(ShopBanner record);
}