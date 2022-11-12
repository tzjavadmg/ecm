package com.milisong.breakfast.scm.order.mapper;

import java.util.List;

import com.milisong.breakfast.scm.order.param.OrderReserveSearchSqlParam;
import com.milisong.breakfast.scm.order.result.OrderReserveSearchSqlResult;

public interface OrderDetailExtMapper {   
    /**
    * 查询预定订单的商品维度汇总信息的总记录数
    * @param param
    * @return
    */
   int getReserveOrderGroupCount(OrderReserveSearchSqlParam param);
   
   /**
    * 查询预定订单的商品维度汇总信息
    * @param param
    * @return
    */
   List<OrderReserveSearchSqlResult> getReserveOrderGroupList(OrderReserveSearchSqlParam param);
}