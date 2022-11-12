package com.milisong.breakfast.scm.goods.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
*@author    created by benny
*@date  2018年12月3日---下午7:47:31
*
*/
@Getter
@Setter
public class GoodsScheduleInfoDto {

	private List<GoodsScheduleDto> goodsSchedule;
	
	private List<GoodsScheduleDetailDto> goodsScheduleDetail;
}
