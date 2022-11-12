package com.milisong.breakfast.scm.goods.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
*@author    created by benny
*@date  2018年12月4日---下午9:34:51
*
*/
@Getter
@Setter
public class GoodsScheduleMqDto {

	private Date date;
	private List<GoodsCatalogListMqDto> goodsCatalogList;
}
