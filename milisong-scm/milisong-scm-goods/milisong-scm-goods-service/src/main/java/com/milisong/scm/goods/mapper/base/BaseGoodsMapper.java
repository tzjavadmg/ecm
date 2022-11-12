package com.milisong.scm.goods.mapper.base;

import com.milisong.scm.goods.domain.Goods;
import com.milisong.scm.goods.domain.GoodsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseGoodsMapper {
    /**
     *
     * @mbg.generated 2019-05-09
     */
    long countByExample(GoodsExample example);

    /**
     *
     * @mbg.generated 2019-05-09
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-05-09
     */
    int insert(Goods record);

    /**
     *
     * @mbg.generated 2019-05-09
     */
    int insertSelective(Goods record);

    /**
     *
     * @mbg.generated 2019-05-09
     */
    List<Goods> selectByExample(GoodsExample example);

    /**
     *
     * @mbg.generated 2019-05-09
     */
    Goods selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-05-09
     */
    int updateByExampleSelective(@Param("record") Goods record, @Param("example") GoodsExample example);

    /**
     *
     * @mbg.generated 2019-05-09
     */
    int updateByExample(@Param("record") Goods record, @Param("example") GoodsExample example);

    /**
     *
     * @mbg.generated 2019-05-09
     */
    int updateByPrimaryKeySelective(Goods record);

    /**
     *
     * @mbg.generated 2019-05-09
     */
    int updateByPrimaryKey(Goods record);
}