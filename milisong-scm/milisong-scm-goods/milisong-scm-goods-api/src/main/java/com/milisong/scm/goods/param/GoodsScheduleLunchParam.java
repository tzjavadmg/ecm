package com.milisong.scm.goods.param;

import java.util.List;

import com.milisong.scm.goods.dto.GoodsScheduleDetailLunchDto;
import com.milisong.scm.goods.dto.GoodsScheduleLunchDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsScheduleLunchParam {

	private List<GoodsScheduleLunchDto> goodsScheduleLunch;
	
	private List<GoodsScheduleDetailLunchDto> goodsScheduleDetailLunch;
	
	private String updateBy;
}
