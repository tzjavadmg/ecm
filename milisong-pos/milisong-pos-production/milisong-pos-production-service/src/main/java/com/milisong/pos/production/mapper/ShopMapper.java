package com.milisong.pos.production.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.milisong.pos.production.api.ShopService;
import com.milisong.pos.production.dto.ShopDto;

public interface ShopMapper extends BaseShopMapper{
    
    /**
     * 统计门店数量
     * @param map
     * @return
     * youxia 2018年9月3日
     */
    int getShopListCount(Map<String, Object> map);
    
    /**
     * 查询门店信息
     * @param map
     * @return
     * youxia 2018年9月3日
     */
    List<ShopDto> getShopList(Map<String, Object> map);

    ShopDto getShopByCode(@Param("shopCode")String shopCode);
}