package com.milisong.scm.shop.param;

import java.io.Serializable;

import com.farmland.core.api.PageParam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildingParam extends PageParam implements Serializable {

	private static final long serialVersionUID = 4948998773117994985L;

	private Long shopId; // 门店id
	private Long buildingId; // 楼宇id
	private String buildingName; // 楼宇名称
	
}
