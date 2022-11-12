package com.milisong.scm.orderset.mapper;

import com.milisong.scm.orderset.domain.OrderSet;
import com.milisong.scm.orderset.domain.OrderSetExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderSetMapper {
    long countByExample(OrderSetExample example);

    int deleteByExample(OrderSetExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OrderSet record);

    int insertSelective(OrderSet record);

    List<OrderSet> selectByExample(OrderSetExample example);

    OrderSet selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OrderSet record, @Param("example") OrderSetExample example);

    int updateByExample(@Param("record") OrderSet record, @Param("example") OrderSetExample example);

    int updateByPrimaryKeySelective(OrderSet record);

    int updateByPrimaryKey(OrderSet record);
}