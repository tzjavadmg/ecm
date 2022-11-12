package com.milisong.breakfast.scm.orderset.mapper;

import java.util.List;
import java.util.Map;

import com.milisong.breakfast.scm.orderset.domain.OrderSetDetail;
import com.milisong.breakfast.scm.orderset.dto.result.OrdersetInfoResult;
import com.milisong.breakfast.scm.orderset.param.OrderSetBuildingSqlResult;

import io.lettuce.core.dynamic.annotation.Param;

public interface OrderSetDetailExtMapper {
    List<OrderSetBuildingSqlResult> selectBuildingList(OrderSetDetail domain);
	
	List<Map<String,Object>> selectCountCustomerOrderBySetNo(List<String> list);
	
	List<OrdersetInfoResult> getCustomerOrderByOrderSetNo(@Param("setNo")String setNo);
}