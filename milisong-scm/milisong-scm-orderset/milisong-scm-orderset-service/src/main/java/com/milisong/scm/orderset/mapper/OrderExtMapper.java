package com.milisong.scm.orderset.mapper;

import java.util.List;

import com.milisong.scm.orderset.dto.param.OrderSearchParam;
import com.milisong.scm.orderset.dto.result.OrderSearchResult;
import com.milisong.scm.orderset.param.OrderCompanySqlParam;
import com.milisong.scm.orderset.param.OrderGoodsCompanySqlParam;
import com.milisong.scm.orderset.result.OrderCompanySqlResult;

public interface OrderExtMapper {
    List<OrderCompanySqlResult> selectOrderCompany( OrderCompanySqlParam param);
    
    String selectLastDeliveryAddressByCompanyInfo( OrderCompanySqlParam param);
    
    Long countCompany( OrderCompanySqlParam param);
    
    Integer selectOrderGoodsCountCompany( OrderGoodsCompanySqlParam param);

    List<OrderSearchResult> selectOrderByPage(OrderSearchParam param);

    Long  countOrderByPage(OrderSearchParam param);
}