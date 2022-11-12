package com.milisong.dms.api;

import com.milisong.dms.dto.httpdto.BuildingDto;

/**
 * @author zhaozhonghui
 * @Description ${Description}
 * @date 2018-12-25
 */
public interface BuildingService {

    /**
     * 根据id查询楼宇信息
     * @param buildingId
     * @return
     */
    BuildingDto getBuildingById(Long buildingId);

    /**
     * 通过MQ更新redis中的门店信息
     *
     * @param dto
     */
    void syncBuildingInfo(BuildingDto dto);
}
