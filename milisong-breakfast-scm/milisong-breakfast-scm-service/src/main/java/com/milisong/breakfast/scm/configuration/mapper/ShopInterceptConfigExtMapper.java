package com.milisong.breakfast.scm.configuration.mapper;

import com.milisong.breakfast.scm.configuration.domain.ShopInterceptConfig;
import com.milisong.breakfast.scm.configuration.dto.ShopInterceptConfigParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopInterceptConfigExtMapper {

    List<ShopInterceptConfig> queryByShopId(@Param("shopId")Long shopId);

    List<ShopInterceptConfig> getInterceptorConfig(ShopInterceptConfigParam param);

    int getInterceptorConfigCount(ShopInterceptConfigParam param);

}