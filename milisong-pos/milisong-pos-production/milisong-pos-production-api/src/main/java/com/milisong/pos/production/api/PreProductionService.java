package com.milisong.pos.production.api;

import java.util.List;

import com.farmland.core.api.ResponseResult;
import com.milisong.pos.production.dto.PreProductionDto;

/**
*@author    created by benny
*@date  2018年10月24日---下午3:57:46
*
*门店预生产管理
*/
public interface PreProductionService {

	
	/**
	 * 生成门店预生产数
	 * @param listPreProductionParam
	 */
	public void createPreProduction(List<PreProductionDto> listPreProductionParam);
	
	
	public ResponseResult<List<PreProductionDto>> getListByShopId(Long shopId);
	
	public ResponseResult<List<PreProductionDto>> getListByAll();
	
	public ResponseResult<String> updateActualProductionCountById(Long id,Integer productionCount,String updateBy);
}
