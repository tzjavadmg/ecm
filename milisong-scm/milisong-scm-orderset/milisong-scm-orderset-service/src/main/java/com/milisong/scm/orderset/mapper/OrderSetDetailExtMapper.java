package com.milisong.scm.orderset.mapper;

import java.util.List;
import java.util.Map;

import com.milisong.scm.orderset.domain.OrderSetDetail;
import com.milisong.scm.orderset.dto.result.OrderSetDetailDto;
import com.milisong.scm.orderset.param.OrderSetBuildingSqlResult;

public interface OrderSetDetailExtMapper {
    List<OrderSetBuildingSqlResult> selectBuildingList(OrderSetDetail domain);

	List<OrderSetDetailDto> selectListOrderSetByPage(Map<String, Object> mapParam);

	Long selectCountOrderSetByPage(Map<String, Object> mapParam);
	
	List<Map<String,Object>> selectCountCustomerOrderBySetNo(List<String> list);
}