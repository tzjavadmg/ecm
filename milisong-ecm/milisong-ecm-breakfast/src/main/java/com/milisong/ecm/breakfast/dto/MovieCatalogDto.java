package com.milisong.ecm.breakfast.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieCatalogDto {
	
	//本周档期集合
	private List<MovieDto> currentWeek;
	
	//下周档期集合
	private List<MovieDto> nextWeek;
	
	//类目集合
	private List<CatalogDto> catalogList;
	

}
