/*
package com.milisong.ecm.lunch.goods.api;

import com.milisong.ecm.lunch.goods.dto.BuildingDto;
import com.milisong.ecm.lunch.goods.dto.BuildingLbsDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

*/
/**
 * LBS相关的服务
 * @author yangzhilong
 *
 *//*

//@FeignClient("milisong-ecm-rest")
public interface LbsService {
	*/
/**
	 * 新增或者修改楼宇的经纬度
	 * @param buildDto
	 *//*

	@PostMapping(value = "/v1/LbsService/saveBuildingLonAndLat")
	void saveBuildingLonAndLat(@RequestBody BuildingDto buildDto);
	
	*/
/**
	 * 查询指定经纬度附近的楼宇
	 * @param lon 经度
	 * @param lat 纬度
	 * @param limit 记录数
	 * @return
	 *//*

	@PostMapping(value = "/v1/LbsService/listNearbyBuilding")
	List<BuildingLbsDto> listNearbyBuilding(@RequestParam("lon") double lon, @RequestParam("lat") double lat, @RequestParam("limit") int limit);
	
}
*/
