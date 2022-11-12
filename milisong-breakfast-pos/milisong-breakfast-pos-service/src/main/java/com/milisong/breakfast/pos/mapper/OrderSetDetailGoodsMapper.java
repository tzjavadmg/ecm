package com.milisong.breakfast.pos.mapper;

import com.milisong.breakfast.pos.domain.OrderSetDetailGoods;
import com.milisong.breakfast.pos.domain.OrderSetDetailGoodsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderSetDetailGoodsMapper {
    /**
     *
     * @mbg.generated 2018-12-13
     */
    long countByExample(OrderSetDetailGoodsExample example);

    /**
     *
     * @mbg.generated 2018-12-13
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-13
     */
    int insert(OrderSetDetailGoods record);

    /**
     *
     * @mbg.generated 2018-12-13
     */
    int insertSelective(OrderSetDetailGoods record);

    /**
     *
     * @mbg.generated 2018-12-13
     */
    List<OrderSetDetailGoods> selectByExample(OrderSetDetailGoodsExample example);

    /**
     *
     * @mbg.generated 2018-12-13
     */
    OrderSetDetailGoods selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2018-12-13
     */
    int updateByExampleSelective(@Param("record") OrderSetDetailGoods record, @Param("example") OrderSetDetailGoodsExample example);

    /**
     *
     * @mbg.generated 2018-12-13
     */
    int updateByExample(@Param("record") OrderSetDetailGoods record, @Param("example") OrderSetDetailGoodsExample example);

    /**
     *
     * @mbg.generated 2018-12-13
     */
    int updateByPrimaryKeySelective(OrderSetDetailGoods record);

    /**
     *
     * @mbg.generated 2018-12-13
     */
    int updateByPrimaryKey(OrderSetDetailGoods record);

	int insertBatchSelective(List<OrderSetDetailGoods> goods);
}