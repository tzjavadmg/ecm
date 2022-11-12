package com.milisong.scm.printer.dto;

import java.util.List;
import java.util.Map;

import com.milisong.scm.printer.request.mq.OrderSetDetailGoodsDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrintLunchDto {

	private List<OrderSetDetailGoodsDto> customerInfo;
	
	private List<OrderSetDetailGoodsDto> picklesInfo;
	
	private Map<String,Object> orderSetPrintMap;
	
	private String coupletNo;
}
