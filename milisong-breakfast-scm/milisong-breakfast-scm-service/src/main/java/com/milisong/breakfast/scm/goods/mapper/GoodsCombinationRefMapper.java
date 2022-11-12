package com.milisong.breakfast.scm.goods.mapper;

import com.milisong.breakfast.scm.goods.domain.GoodsCombinationRef;
import com.milisong.breakfast.scm.goods.domain.GoodsCombinationRefExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GoodsCombinationRefMapper {
    /**
     *
     * @mbg.generated 2018-12-11
     */
    long countByExample(GoodsCombinationRefExample example);

    /**
     *
     * @mbg.generated 2018-12-11
     */
    int deleteByExample(GoodsCombinationRefExample example);

    /**
     *
     * @mbg.generated 2018-12-11
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-11
     */
    int insert(GoodsCombinationRef record);

    /**
     *
     * @mbg.generated 2018-12-11
     */
    int insertSelective(GoodsCombinationRef record);

    /**
     *
     * @mbg.generated 2018-12-11
     */
    List<GoodsCombinationRef> selectByExample(GoodsCombinationRefExample example);

    /**
     *
     * @mbg.generated 2018-12-11
     */
    GoodsCombinationRef selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-11
     */
    int updateByExampleSelective(@Param("record") GoodsCombinationRef record, @Param("example") GoodsCombinationRefExample example);

    /**
     *
     * @mbg.generated 2018-12-11
     */
    int updateByExample(@Param("record") GoodsCombinationRef record, @Param("example") GoodsCombinationRefExample example);

    /**
     *
     * @mbg.generated 2018-12-11
     */
    int updateByPrimaryKeySelective(GoodsCombinationRef record);

    /**
     *
     * @mbg.generated 2018-12-11
     */
    int updateByPrimaryKey(GoodsCombinationRef record);
    
    /**
     * 批量新增
     * @param listGoodsCombo
     * @return
     */
	int insertBatchSelective(List<GoodsCombinationRef> listGoodsCombo);
}