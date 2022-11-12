/*
package com.milisong.ecm.lunch.goods.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.lunch.goods.dto.BuildingDto;
import com.milisong.ecm.lunch.goods.param.BuildingParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//@FeignClient("milisong-ecm-rest")
public interface BuildingService {
	
	*/
/**
	 * 根据楼宇id获取楼宇详细信息
	 * @param buildingId
	 * @return
	 *//*

	@PostMapping(value = "/v1/BuildingService/getBuildList")
	List<BuildingDto> getBuildList(@RequestBody List<Long> buildingId);
	

    */
/**
     * 获取楼宇信息
     *
     * @return
     *//*

	@PostMapping(value = "/v1/BuildingService/getBuildingList")
    ResponseResult<List<BuildingDto>> getBuildingList();
    
    */
/**
     * 获取待开通楼宇
     * @return
     *//*

	@PostMapping(value = "/v1/BuildingService/toOpenBuilding")
    ResponseResult<String> toOpenBuilding(@RequestBody BuildingParam buildingParam);
    
    */
/**
     * 根据楼宇名称模糊搜索楼宇
     * @param name
     * @return
     *//*

	@PostMapping(value = "/v1/BuildingService/searchBuilding")
    ResponseResult<List<BuildingDto>> searchBuilding(@RequestParam("name") String name);

}
*/
