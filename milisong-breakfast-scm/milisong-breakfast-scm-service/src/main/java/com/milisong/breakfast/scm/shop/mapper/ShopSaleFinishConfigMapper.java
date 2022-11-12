package com.milisong.breakfast.scm.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.milisong.breakfast.scm.shop.domain.ShopSaleFinishConfig;
import com.milisong.breakfast.scm.shop.domain.ShopSaleFinishConfigExample;

public interface ShopSaleFinishConfigMapper {
    long countByExample(ShopSaleFinishConfigExample example);

    int deleteByExample(ShopSaleFinishConfigExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ShopSaleFinishConfig record);

    int insertSelective(ShopSaleFinishConfig record);

    List<ShopSaleFinishConfig> selectByExample(ShopSaleFinishConfigExample example);

    ShopSaleFinishConfig selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ShopSaleFinishConfig record, @Param("example") ShopSaleFinishConfigExample example);

    int updateByExample(@Param("record") ShopSaleFinishConfig record, @Param("example") ShopSaleFinishConfigExample example);

    int updateByPrimaryKeySelective(ShopSaleFinishConfig record);

    int updateByPrimaryKey(ShopSaleFinishConfig record);
}