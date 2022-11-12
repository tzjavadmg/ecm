package com.milisong.scm.stock.mapper.base;

import com.milisong.scm.stock.domain.ShopOnsaleGoodsStock;
import com.milisong.scm.stock.domain.ShopOnsaleGoodsStockExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BaseShopOnsaleGoodsStockMapper {
    /**
     *
     * @mbg.generated 2018-10-29
     */
    long countByExample(ShopOnsaleGoodsStockExample example);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    int insert(ShopOnsaleGoodsStock record);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    int insertSelective(ShopOnsaleGoodsStock record);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    List<ShopOnsaleGoodsStock> selectByExample(ShopOnsaleGoodsStockExample example);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    ShopOnsaleGoodsStock selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    int updateByExampleSelective(@Param("record") ShopOnsaleGoodsStock record, @Param("example") ShopOnsaleGoodsStockExample example);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    int updateByExample(@Param("record") ShopOnsaleGoodsStock record, @Param("example") ShopOnsaleGoodsStockExample example);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    int updateByPrimaryKeySelective(ShopOnsaleGoodsStock record);

    /**
     *
     * @mbg.generated 2018-10-29
     */
    int updateByPrimaryKey(ShopOnsaleGoodsStock record);
}