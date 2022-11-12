package com.milisong.breakfast.scm.configuration.mapper;

import com.milisong.breakfast.scm.configuration.domain.ShopBanner;
import com.milisong.breakfast.scm.configuration.dto.ShopBannerParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopBannerExtMapper {

    List<ShopBanner> queryShopBanner();

    List<ShopBanner> queryByShopCode(@Param("shopCode") String shopCode);

    int getShopIdsCount(ShopBannerParam param);

    List<Long> getShopIds(ShopBannerParam param);

    List<ShopBanner> getShopBannerByIds(@Param("ids") String ids);

    int updateByPrimaryKeySelectiveClearLinKUrl(ShopBanner record);

    List<ShopBanner> queryByShopId(@Param("shopId") Long shopId);

}