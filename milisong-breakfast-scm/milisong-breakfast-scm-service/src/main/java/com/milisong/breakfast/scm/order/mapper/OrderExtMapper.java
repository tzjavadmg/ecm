package com.milisong.breakfast.scm.order.mapper;

import com.milisong.breakfast.scm.order.dto.param.OrderSearchParam;
import com.milisong.breakfast.scm.order.dto.result.OrderSearchResult;
import com.milisong.breakfast.scm.order.param.OrderGoodsCompanySqlParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderExtMapper {
    Integer selectOrderGoodsCountCompany(OrderGoodsCompanySqlParam param);

    List<OrderSearchResult> selectOrderByPage(OrderSearchParam param);

    Long countOrderByPage(OrderSearchParam param);

    List<String> selectMealAddressByCompany(@Param("companyId") Long companyId);

}