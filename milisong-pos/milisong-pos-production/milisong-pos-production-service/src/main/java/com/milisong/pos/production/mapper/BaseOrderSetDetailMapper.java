package com.milisong.pos.production.mapper;

import com.milisong.pos.production.domain.OrderSetDetail;
import com.milisong.pos.production.domain.OrderSetDetailExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BaseOrderSetDetailMapper {
    long countByExample(OrderSetDetailExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OrderSetDetail record);

    int insertSelective(OrderSetDetail record);

    List<OrderSetDetail> selectByExample(OrderSetDetailExample example);

    OrderSetDetail selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OrderSetDetail record, @Param("example") OrderSetDetailExample example);

    int updateByExample(@Param("record") OrderSetDetail record, @Param("example") OrderSetDetailExample example);

    int updateByPrimaryKeySelective(OrderSetDetail record);

    int updateByPrimaryKey(OrderSetDetail record);
}