package com.milisong.scm.shop.mapper;

import com.milisong.scm.shop.domain.ShopInterceptConfig;
import com.milisong.scm.shop.dto.config.ShopInterceptConfigParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopInterceptConfigExtMapper {

    List<ShopInterceptConfig> queryByShopId(@Param("shopId")Long shopId);

    List<ShopInterceptConfig> getInterceptorConfig(ShopInterceptConfigParam param);

    int getInterceptorConfigCount(ShopInterceptConfigParam param);
}