package com.milisong.scm.shop.api;

import com.milisong.scm.shop.dto.building.BuildingApplyDto;

public interface BuildingApplyService {
	/**
	 * 楼宇申请数据保存
	 * @param dto
	 */
	void save(BuildingApplyDto dto);
}
