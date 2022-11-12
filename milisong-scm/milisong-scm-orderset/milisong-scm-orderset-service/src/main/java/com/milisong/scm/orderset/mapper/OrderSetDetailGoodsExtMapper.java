package com.milisong.scm.orderset.mapper;

import java.util.List;

import com.milisong.scm.orderset.domain.OrderSetDetailGoods;
import org.apache.ibatis.annotations.Param;

import com.milisong.scm.orderset.dto.result.OrdersetNoResult;
import com.milisong.scm.orderset.dto.result.PickOrderGoodsSumResult;
import com.milisong.scm.orderset.result.OrderSetDetailStatusDto;

public interface OrderSetDetailGoodsExtMapper {
    List<String> listDetailSetNoByOrderNo(@Param("orderNo")String orderNo);
    
    List<Byte> listGroupStatusByOrderNo(@Param("orderNo")String orderNo);

	List<PickOrderGoodsSumResult> listGoodsCountGroup(List<String> list);
	
	OrdersetNoResult getLastOrderNo(@Param("distributionNo")String distributionNo);
	
	List<OrderSetDetailStatusDto> listAllStatusByOrderNo(@Param("orderNo")String orderNo);

}