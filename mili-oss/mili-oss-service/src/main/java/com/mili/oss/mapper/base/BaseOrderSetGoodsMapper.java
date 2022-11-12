package com.mili.oss.mapper.base;

import com.mili.oss.domain.OrderSetGoods;
import com.mili.oss.domain.OrderSetGoodsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseOrderSetGoodsMapper {
    /**
     *
     * @mbg.generated 2019-05-08
     */
    long countByExample(OrderSetGoodsExample example);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    int deleteByExample(OrderSetGoodsExample example);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    int insert(OrderSetGoods record);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    int insertSelective(OrderSetGoods record);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    List<OrderSetGoods> selectByExample(OrderSetGoodsExample example);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    OrderSetGoods selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    int updateByExampleSelective(@Param("record") OrderSetGoods record, @Param("example") OrderSetGoodsExample example);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    int updateByExample(@Param("record") OrderSetGoods record, @Param("example") OrderSetGoodsExample example);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    int updateByPrimaryKeySelective(OrderSetGoods record);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    int updateByPrimaryKey(OrderSetGoods record);
}