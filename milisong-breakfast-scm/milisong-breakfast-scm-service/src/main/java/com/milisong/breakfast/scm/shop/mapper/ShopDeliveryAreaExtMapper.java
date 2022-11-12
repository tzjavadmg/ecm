package com.milisong.breakfast.scm.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.milisong.breakfast.scm.shop.domain.ShopDeliveryArea;

public interface ShopDeliveryAreaExtMapper {
    
    /**
     * 根据门店id查询配送范围的楼宇id
     * @param shopId
     * @return
     * youxia 2018年9月3日
     */
    List<Long> getBuildingIdByShopId(@Param("shopId") Long shopId);
    
    /**
     * 根据楼宇id查询门店id
     * @param buildingId
     * @return
     * youxia 2018年9月5日
     */
    Long getShopIdByBuildingId(@Param("buildingId") Long buildingId);

    ShopDeliveryArea getByBuildingId(@Param("buildingId") Long buildingId);
    
}