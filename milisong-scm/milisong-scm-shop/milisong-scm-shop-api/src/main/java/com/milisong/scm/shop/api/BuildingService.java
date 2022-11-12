package com.milisong.scm.shop.api;


import java.util.List;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.shop.dto.building.BuildingDto;
import com.milisong.scm.shop.dto.building.BuildingSaveDto;
import com.milisong.scm.shop.param.BuildingParam;
import com.milisong.scm.shop.param.BulidingUpdateListParam;

public interface BuildingService {
	
	/**
	 * 根据门店id分页查询楼宇
	 * @param param
	 * @return
	 * youxia 2018年9月2日
	 */
	public Pagination<BuildingDto> getBuildingPageByShopId(BuildingParam param);
	
	/**
	 * 根据经纬度查询楼宇信息
	 * @param lat
	 * @param lon
	 * @return
	 * youxia 2018年9月3日
	 */
	public List<BuildingDto> getBuildingInfoByLatAndLon(String lat, String lon);
	
	/**
	 * 根据楼宇id查询楼宇信息
	 * @param buildingId
	 * @return
	 * youxia 2018年9月7日
	 */
	public BuildingDto getBuildingInfoById(Long buildingId);
	
	/**
	 * 根据楼宇id集合查询楼宇信息
	 * @param buildingId
	 * @return
	 * youxia 2018年9月7日
	 */
	public List<BuildingDto> getBuildingInfoByIdSet(List<Long> buildingId);

	/**
	 * 分页查询楼宇信息
	 * @param param
	 * @return
	 */
	ResponseResult<Pagination<BuildingDto>> pageSearch(BuildingParam param);
	
	/**
	 * 分页查询楼宇信息-排序
	 * @param param
	 * @return
	 */
	ResponseResult<Pagination<BuildingDto>> pageSearchWeight(BuildingParam param);
	/**
	 * 不分页查询楼宇信息
	 * @param param
	 * @return
	 */
	List<BuildingDto> search(BuildingParam param);

	/**
	 * 保存修改楼宇信息
	 * @param dto
	 * @return
	 */
	ResponseResult<Object> saveBuilding(BuildingSaveDto dto);

	/**
	 * 发送楼宇信息到ecm
	 * @return
	 */
	ResponseResult<Object> sendBuildingToEcm();
	
	/**
	 * 根据楼宇ID批量更新排序
	 * @param buildingUpdateParam
	 * @return
	 */
	ResponseResult<Object> updateBuildingByIds(BulidingUpdateListParam buildingUpdateParam);
}
