package com.mili.oss.mapper.base;

import com.mili.oss.domain.OrderGoods;
import com.mili.oss.domain.OrderGoodsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseOrderGoodsMapper {
    /**
     *
     * @mbg.generated 2019-05-08
     */
    long countByExample(OrderGoodsExample example);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    int deleteByExample(OrderGoodsExample example);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    int insert(OrderGoods record);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    int insertSelective(OrderGoods record);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    List<OrderGoods> selectByExample(OrderGoodsExample example);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    OrderGoods selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    int updateByExampleSelective(@Param("record") OrderGoods record, @Param("example") OrderGoodsExample example);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    int updateByExample(@Param("record") OrderGoods record, @Param("example") OrderGoodsExample example);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    int updateByPrimaryKeySelective(OrderGoods record);

    /**
     *
     * @mbg.generated 2019-05-08
     */
    int updateByPrimaryKey(OrderGoods record);
    
    int insertBatchSelective(List<OrderGoods> list);
}