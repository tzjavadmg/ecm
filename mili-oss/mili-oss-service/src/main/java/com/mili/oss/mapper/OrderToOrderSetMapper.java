package com.mili.oss.mapper;

import com.mili.oss.domain.OrderToOrderSet;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

public interface OrderToOrderSetMapper {

    void batchSave(@Param("collection") Collection<OrderToOrderSet> orderSetGoodss);
}
