package com.milisong.scm.orderset.mapper;

import java.util.List;

import com.milisong.scm.orderset.param.OrderDetailSearch4OrderSetSqlParam;
import com.milisong.scm.orderset.param.OrderReserveSearchSqlParam;
import com.milisong.scm.orderset.result.OrderDetailSqlResult;
import com.milisong.scm.orderset.result.OrderReserveSearchSqlResult;

public interface OrderDetailExtMapper {   
    /**
     * 查询公司的订单详情
     * @return
     */
    List<OrderDetailSqlResult> getCompanyOrderDetail(OrderDetailSearch4OrderSetSqlParam param);
    
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