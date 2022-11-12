package com.milisong.scm.shop.mapper;

import com.milisong.scm.shop.domain.ShopOnsaleGoods;
import com.milisong.scm.shop.domain.ShopOnsaleGoodsExample;
import com.milisong.scm.shop.dto.shop.ShopOnsaleGoodsDto;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ShopOnsaleGoodsMapper {
    long countByExample(ShopOnsaleGoodsExample example);

    int deleteByExample(ShopOnsaleGoodsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ShopOnsaleGoods record);

    int insertSelective(ShopOnsaleGoods record);

    List<ShopOnsaleGoods> selectByExample(ShopOnsaleGoodsExample example);

    ShopOnsaleGoods selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ShopOnsaleGoods record, @Param("example") ShopOnsaleGoodsExample example);

    int updateByExample(@Param("record") ShopOnsaleGoods record, @Param("example") ShopOnsaleGoodsExample example);

    int updateByPrimaryKeySelective(ShopOnsaleGoods record);

    int updateByPrimaryKey(ShopOnsaleGoods record);
    
}