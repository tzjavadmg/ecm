package com.milisong.breakfast.scm.goods.mapper;

import com.milisong.breakfast.scm.goods.domain.Goods;
import com.milisong.breakfast.scm.goods.domain.GoodsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GoodsMapper {
    /**
     *
     * @mbg.generated 2019-01-25
     */
    long countByExample(GoodsExample example);

    /**
     *
     * @mbg.generated 2019-01-25
     */
    int deleteByExample(GoodsExample example);

    /**
     *
     * @mbg.generated 2019-01-25
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-01-25
     */
    int insert(Goods record);

    /**
     *
     * @mbg.generated 2019-01-25
     */
    int insertSelective(Goods record);

    /**
     *
     * @mbg.generated 2019-01-25
     */
    List<Goods> selectByExample(GoodsExample example);

    /**
     *
     * @mbg.generated 2019-01-25
     */
    Goods selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-01-25
     */
    int updateByExampleSelective(@Param("record") Goods record, @Param("example") GoodsExample example);

    /**
     *
     * @mbg.generated 2019-01-25
     */
    int updateByExample(@Param("record") Goods record, @Param("example") GoodsExample example);

    /**
     *
     * @mbg.generated 2019-01-25
     */
    int updateByPrimaryKeySelective(Goods record);

    /**
     *
     * @mbg.generated 2019-01-25
     */
    int updateByPrimaryKey(Goods record);
}