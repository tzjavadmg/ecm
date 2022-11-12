package com.milisong.ecm.lunch.goods.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuildingParam {
	
	//楼宇id
	private Long deliveryBuildingId;
	
	//楼宇名称
	private String deliveryBuildingName;
	

	//用户id
    private Long userId;

    //用户昵称
    private String nikeName;

    //手机号码
    private String mobile;

}
