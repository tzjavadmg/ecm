package com.milisong.pos.production.mapper;

import com.milisong.pos.production.domain.Shop;
import com.milisong.pos.production.domain.ShopExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

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