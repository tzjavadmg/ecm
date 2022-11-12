package com.milisong.scm.orderset.mapper;

import com.milisong.scm.orderset.domain.OrderSetDetailGoods;
import com.milisong.scm.orderset.domain.OrderSetDetailGoodsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderSetDetailGoodsMapper {
    long countByExample(OrderSetDetailGoodsExample example);

    int deleteByExample(OrderSetDetailGoodsExample example);

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