package com.milisong.scm.stock.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.milisong.scm.stock.domain.ShopOnsaleGoodsStock;
import com.milisong.scm.stock.dto.OnsaleGoodsStockDto;
import com.milisong.scm.stock.dto.ShopOnsaleGoodsStockDto;
import com.milisong.scm.stock.mapper.base.BaseShopOnsaleGoodsStockMapper;

@Mapper
public interface ShopOnsaleGoodsStockExtMapper extends BaseShopOnsaleGoodsStockMapper{
    
    int getCountByCondition(Map<String,Object> paramMap);
    
    List<ShopOnsaleGoodsStockDto> getListByCondition(Map<String,Object> paramMap);
    
    int insertList(List<ShopOnsaleGoodsStock> list);
    
    List<Long> getListRelationIdByNow(@Param("saleDate")Date date);
    
    List<OnsaleGoodsStockDto> getNowListByCondition(Map<String,Object> paramMap);
}