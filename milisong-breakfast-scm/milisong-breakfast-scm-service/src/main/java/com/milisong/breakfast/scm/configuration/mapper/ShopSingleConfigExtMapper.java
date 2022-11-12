package com.milisong.breakfast.scm.configuration.mapper;

import com.milisong.breakfast.scm.configuration.domain.ShopSingleConfig;
import com.milisong.breakfast.scm.configuration.dto.ShopSingleConfigParam;
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