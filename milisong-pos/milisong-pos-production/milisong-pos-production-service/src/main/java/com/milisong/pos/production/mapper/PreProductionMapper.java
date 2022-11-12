package com.milisong.pos.production.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.milisong.pos.production.dto.PreProductionDto;

/**
*@author    created by benny
*@date  2018年10月24日---下午3:22:45
*
*/
public interface PreProductionMapper {

	void savePreProductionByList(List<PreProductionDto> list);
	
	List<PreProductionDto> getListByShopId(@Param("shopId")Long shopId);
	
}
