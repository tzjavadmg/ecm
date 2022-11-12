package com.milisong.scm.goods.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

 @Getter
 @Setter
public class GoodsScheduleLunchInfoDto {

	 private List<GoodsScheduleLunchDto> goodsScheduleLunch;
	
	private List<GoodsScheduleDetailLunchDto> goodsScheduleDetailLunch;
}
