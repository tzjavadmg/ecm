package com.milisong.scm.printer.dto;

import java.util.List;
import java.util.Map;

import com.milisong.scm.printer.request.mq.OrderSetDetailGoodsDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrintBreakFastDto {

	private List<OrderSetDetailGoodsDto> listOrderSetDetail;
	
	private Map<String,Object> orderSetPrintMap;
	
	private Integer goodsCount;
	
	private List<OrderSetDetailGoodsDto> surplusSingle;
}
