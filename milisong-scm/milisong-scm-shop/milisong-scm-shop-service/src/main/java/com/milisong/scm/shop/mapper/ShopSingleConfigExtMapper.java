package com.milisong.scm.shop.mapper;

import com.milisong.scm.shop.domain.ShopSingleConfig;
import com.milisong.scm.shop.dto.config.ShopSingleConfigParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopSingleConfigExtMapper {

    List<ShopSingleConfig> queryByShopId(@Param("shopId")Long shopId);

    List<ShopSingleConfig> queryAllShop();

    ShopSingleConfig queryByShopIdAndKey(@Param("shopId") Long shopId, @Param("configKey")String configKey);

    int getShopIdsCount(ShopSingleConfigParam param);

    List<Long> getShopIds(ShopSingleConfigParam param);

    List<ShopSingleConfig> getShopSingleConfigByIds(@Param("ids") String ids);
}