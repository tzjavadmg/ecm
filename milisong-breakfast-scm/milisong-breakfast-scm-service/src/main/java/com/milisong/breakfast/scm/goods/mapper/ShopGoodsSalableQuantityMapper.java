package com.milisong.breakfast.scm.goods.mapper;

import com.milisong.breakfast.scm.goods.domain.ShopGoodsSalableQuantity;
import com.milisong.breakfast.scm.goods.domain.ShopGoodsSalableQuantityExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ShopGoodsSalableQuantityMapper {
    /**
     *
     * @mbg.generated 2018-12-04
     */
    long countByExample(ShopGoodsSalableQuantityExample example);

    /**
     *
     * @mbg.generated 2018-12-04
     */
    int deleteByExample(ShopGoodsSalableQuantityExample example);

    /**
     *
     * @mbg.generated 2018-12-04
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-04
     */
    int insert(ShopGoodsSalableQuantity record);

    /**
     *
     * @mbg.generated 2018-12-04
     */
    int insertSelective(ShopGoodsSalableQuantity record);

    /**
     *
     * @mbg.generated 2018-12-04
     */
    List<ShopGoodsSalableQuantity> selectByExample(ShopGoodsSalableQuantityExample example);

    /**
     *
     * @mbg.generated 2018-12-04
     */
    ShopGoodsSalableQuantity selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-04
     */
    int updateByExampleSelective(@Param("record") ShopGoodsSalableQuantity record, @Param("example") ShopGoodsSalableQuantityExample example);

    /**
     *
     * @mbg.generated 2018-12-04
     */
    int updateByExample(@Param("record") ShopGoodsSalableQuantity record, @Param("example") ShopGoodsSalableQuantityExample example);

    /**
     *
     * @mbg.generated 2018-12-04
     */
    int updateByPrimaryKeySelective(ShopGoodsSalableQuantity record);

    /**
     *
     * @mbg.generated 2018-12-04
     */
    int updateByPrimaryKey(ShopGoodsSalableQuantity record);

    int insertBatchSelective(List<ShopGoodsSalableQuantity> listParam);
}