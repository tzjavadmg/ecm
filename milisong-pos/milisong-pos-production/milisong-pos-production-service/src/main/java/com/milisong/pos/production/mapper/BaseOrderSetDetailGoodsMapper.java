package com.milisong.pos.production.mapper;

import com.milisong.pos.production.domain.OrderSetDetailGoods;
import com.milisong.pos.production.domain.OrderSetDetailGoodsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseOrderSetDetailGoodsMapper {
    long countByExample(OrderSetDetailGoodsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OrderSetDetailGoods record);

    int insertSelective(OrderSetDetailGoods record);

    List<OrderSetDetailGoods> selectByExample(OrderSetDetailGoodsExample example);

    OrderSetDetailGoods selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OrderSetDetailGoods record, @Param("example") OrderSetDetailGoodsExample example);

    int updateByExample(@Param("record") OrderSetDetailGoods record, @Param("example") OrderSetDetailGoodsExample example);

    int updateByPrimaryKeySelective(OrderSetDetailGoods record);

    int updateByPrimaryKey(OrderSetDetailGoods record);
}