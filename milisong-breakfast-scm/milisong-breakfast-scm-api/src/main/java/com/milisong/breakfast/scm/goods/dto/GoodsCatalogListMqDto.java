package com.milisong.breakfast.scm.goods.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
*@author    created by benny
*@date  2018年12月4日---下午9:36:00
*
*/ 
@Getter
@Setter
public class GoodsCatalogListMqDto {

	private String categoryCode;
	private String categoryName;
	private String picture;  //类目图片
	private List<GoodsMqDto> goods;
}
