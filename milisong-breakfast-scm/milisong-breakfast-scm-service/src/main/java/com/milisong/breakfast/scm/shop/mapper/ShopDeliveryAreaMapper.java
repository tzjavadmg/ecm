package com.milisong.breakfast.scm.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.milisong.breakfast.scm.shop.domain.ShopDeliveryArea;
import com.milisong.breakfast.scm.shop.domain.ShopDeliveryAreaExample;

public interface ShopDeliveryAreaMapper {
    long countByExample(ShopDeliveryAreaExample example);

    int deleteByExample(ShopDeliveryAreaExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ShopDeliveryArea record);

    int insertSelective(ShopDeliveryArea record);

    List<ShopDeliveryArea> selectByExample(ShopDeliveryAreaExample example);

    ShopDeliveryArea selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ShopDeliveryArea record, @Param("example") ShopDeliveryAreaExample example);

    int updateByExample(@Param("record") ShopDeliveryArea record, @Param("example") ShopDeliveryAreaExample example);

    int updateByPrimaryKeySelective(ShopDeliveryArea record);

    int updateByPrimaryKey(ShopDeliveryArea record);
    
}