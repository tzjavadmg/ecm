package com.milisong.breakfast.pos.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.milisong.breakfast.pos.domain.Shop;
import com.milisong.breakfast.pos.domain.ShopExample;

public interface BaseShopMapper {
    long countByExample(ShopExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Shop record);

    int insertSelective(Shop record);

    List<Shop> selectByExample(ShopExample example);

    Shop selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Shop record, @Param("example") ShopExample example);

    int updateByExample(@Param("record") Shop record, @Param("example") ShopExample example);

    int updateByPrimaryKeySelective(Shop record);

    int updateByPrimaryKey(Shop record);
}