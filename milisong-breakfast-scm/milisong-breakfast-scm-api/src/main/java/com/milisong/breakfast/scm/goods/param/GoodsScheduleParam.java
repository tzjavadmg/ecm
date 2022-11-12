package com.milisong.breakfast.scm.goods.param;

import java.util.List;

import com.milisong.breakfast.scm.goods.dto.GoodsScheduleDetailDto;
import com.milisong.breakfast.scm.goods.dto.GoodsScheduleDto;

import lombok.Getter;
import lombok.Setter;

/**
*@author    created by benny
*@date  2018年12月3日---下午8:25:05
*
*/
@Getter
@Setter
public class GoodsScheduleParam {

	private List<GoodsScheduleDto> goodsSchedule;
	
	private List<GoodsScheduleDetailDto> goodsScheduleDetail;
	
	private String updateBy;
}
