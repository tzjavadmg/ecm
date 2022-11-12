package com.milisong.scm.shop.mapper;

import com.milisong.scm.shop.dto.shop.ShopOnsaleGoodsDto;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;


public interface ShopOnsaleGoodsExtMapper {

	/**
     * 批量新增可售商品
     * @param list
     * @return
     * youxia 2018年9月4日
     */
    int insertOnsalGoodsByBatch(List<ShopOnsaleGoodsDto> list);
    
    List<ShopOnsaleGoodsDto> getOnsaleGoodsByStatus(@Param("status") Integer status);
    
    int updateOnsaleGoodsByBatch(Map<String, Object> map);
    
}