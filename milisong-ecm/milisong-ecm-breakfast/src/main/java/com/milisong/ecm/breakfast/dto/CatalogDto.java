package com.milisong.ecm.breakfast.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CatalogDto {
	
	//类目编码
	private String categoryCode;
	
	//类目名称
	private String categoryName;
	
	//图片
	private String picture;
	
	//权重
	private Byte weight;

}
