package com.milisong.scm.goods.mapper;

import com.milisong.scm.goods.domain.GoodsScheduleLunch;
import com.milisong.scm.goods.domain.GoodsScheduleLunchExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GoodsScheduleLunchMapper {
    /**
     *
     * @mbg.generated 2018-12-18
     */
    long countByExample(GoodsScheduleLunchExample example);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int insert(GoodsScheduleLunch record);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int insertSelective(GoodsScheduleLunch record);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    List<GoodsScheduleLunch> selectByExample(GoodsScheduleLunchExample example);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    GoodsScheduleLunch selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int updateByExampleSelective(@Param("record") GoodsScheduleLunch record, @Param("example") GoodsScheduleLunchExample example);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int updateByExample(@Param("record") GoodsScheduleLunch record, @Param("example") GoodsScheduleLunchExample example);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int updateByPrimaryKeySelective(GoodsScheduleLunch record);

    /**
     *
     * @mbg.generated 2018-12-18
     */
    int updateByPrimaryKey(GoodsScheduleLunch record);

	int insertBatchSelective(List<GoodsScheduleLunch> listScheduleLunch);
}