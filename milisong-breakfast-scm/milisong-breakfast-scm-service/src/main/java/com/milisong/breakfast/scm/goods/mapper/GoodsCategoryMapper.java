package com.milisong.breakfast.scm.goods.mapper;

import com.milisong.breakfast.scm.goods.domain.GoodsCategory;
import com.milisong.breakfast.scm.goods.domain.GoodsCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GoodsCategoryMapper {
    /**
     *
     * @mbg.generated 2019-01-18
     */
    long countByExample(GoodsCategoryExample example);

    /**
     *
     * @mbg.generated 2019-01-18
     */
    int deleteByExample(GoodsCategoryExample example);

    /**
     *
     * @mbg.generated 2019-01-18
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-01-18
     */
    int insert(GoodsCategory record);

    /**
     *
     * @mbg.generated 2019-01-18
     */
    int insertSelective(GoodsCategory record);

    /**
     *
     * @mbg.generated 2019-01-18
     */
    List<GoodsCategory> selectByExample(GoodsCategoryExample example);

    /**
     *
     * @mbg.generated 2019-01-18
     */
    GoodsCategory selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-01-18
     */
    int updateByExampleSelective(@Param("record") GoodsCategory record, @Param("example") GoodsCategoryExample example);

    /**
     *
     * @mbg.generated 2019-01-18
     */
    int updateByExample(@Param("record") GoodsCategory record, @Param("example") GoodsCategoryExample example);

    /**
     *
     * @mbg.generated 2019-01-18
     */
    int updateByPrimaryKeySelective(GoodsCategory record);

    /**
     *
     * @mbg.generated 2019-01-18
     */
    int updateByPrimaryKey(GoodsCategory record);
}