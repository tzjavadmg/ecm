package com.milisong.scm.goods.mapper.base;

import com.milisong.scm.goods.domain.GoodsCatalog;
import com.milisong.scm.goods.domain.GoodsCatalogExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseGoodsCatalogMapper {
    /**
     *
     * @mbg.generated 2018-10-26
     */
    long countByExample(GoodsCatalogExample example);

    /**
     *
     * @mbg.generated 2018-10-26
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *
     * @mbg.generated 2018-10-26
     */
    int insert(GoodsCatalog record);

    /**
     *
     * @mbg.generated 2018-10-26
     */
    int insertSelective(GoodsCatalog record);

    /**
     *
     * @mbg.generated 2018-10-26
     */
    List<GoodsCatalog> selectByExample(GoodsCatalogExample example);

    /**
     *
     * @mbg.generated 2018-10-26
     */
    GoodsCatalog selectByPrimaryKey(Integer id);

    /**
     *
     * @mbg.generated 2018-10-26
     */
    int updateByExampleSelective(@Param("record") GoodsCatalog record, @Param("example") GoodsCatalogExample example);

    /**
     *
     * @mbg.generated 2018-10-26
     */
    int updateByExample(@Param("record") GoodsCatalog record, @Param("example") GoodsCatalogExample example);

    /**
     *
     * @mbg.generated 2018-10-26
     */
    int updateByPrimaryKeySelective(GoodsCatalog record);

    /**
     *
     * @mbg.generated 2018-10-26
     */
    int updateByPrimaryKey(GoodsCatalog record);
}