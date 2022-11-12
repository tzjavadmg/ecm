package com.milisong.breakfast.scm.orderset.mapper;

import com.milisong.breakfast.scm.orderset.domain.OrderSetDetailGoods;
import com.milisong.breakfast.scm.orderset.domain.OrderSetDetailGoodsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderSetDetailGoodsMapper {
    /**
     *
     * @mbg.generated 2018-12-19
     */
    long countByExample(OrderSetDetailGoodsExample example);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int deleteByExample(OrderSetDetailGoodsExample example);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int insert(OrderSetDetailGoods record);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int insertSelective(OrderSetDetailGoods record);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    List<OrderSetDetailGoods> selectByExample(OrderSetDetailGoodsExample example);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    OrderSetDetailGoods selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int updateByExampleSelective(@Param("record") OrderSetDetailGoods record, @Param("example") OrderSetDetailGoodsExample example);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int updateByExample(@Param("record") OrderSetDetailGoods record, @Param("example") OrderSetDetailGoodsExample example);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int updateByPrimaryKeySelective(OrderSetDetailGoods record);

    /**
     *
     * @mbg.generated 2018-12-19
     */
    int updateByPrimaryKey(OrderSetDetailGoods record);
}