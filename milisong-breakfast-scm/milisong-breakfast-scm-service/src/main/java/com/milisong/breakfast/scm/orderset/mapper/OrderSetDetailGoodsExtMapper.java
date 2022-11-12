package com.milisong.breakfast.scm.orderset.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.milisong.breakfast.scm.orderset.dto.result.OrdersetNoResult;
import com.milisong.breakfast.scm.orderset.result.OrderSetDetailStatusDto;

public interface OrderSetDetailGoodsExtMapper {
    List<String> listDetailSetNoByOrderNo(@Param("orderNo")String orderNo);
    
    List<Byte> listGroupStatusByOrderNo(@Param("orderNo")String orderNo);
	
	List<OrderSetDetailStatusDto> listAllStatusByOrderNo(@Param("orderNo")String orderNo);

}