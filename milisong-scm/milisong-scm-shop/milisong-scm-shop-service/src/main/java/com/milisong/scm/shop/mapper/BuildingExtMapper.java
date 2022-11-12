package com.milisong.scm.shop.mapper;

import java.util.List;
import java.util.Map;

import com.milisong.scm.shop.dto.building.BuildingDto;
import com.milisong.scm.shop.param.BuildingParam;
import com.milisong.scm.shop.param.BuildingUpdateParam;

public interface BuildingExtMapper {
    
    /**
     * 统计楼宇数量
     * @param map
     * @return
     * youxia 2018年9月3日
     */
    int getBuildingListCount(Map<String, Object> map);
    
    /**
     * 查询楼宇信息
     * @param map
     * @return
     * youxia 2018年9月3日
     */
    List<BuildingDto> getBuildingList(Map<String, Object> map);

    List<BuildingDto> selectPageByBuildingName(BuildingParam param);

    Long selectCountByBuildingName(BuildingParam param);
    
    List<BuildingDto> selectPageByWeight(BuildingParam param);

    Long selectCountByWeight(BuildingParam param);
    
    int updateBuildingById(List<BuildingUpdateParam> list);
    
}