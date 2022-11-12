package com.milisong.scm.printer.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrintAllBreakFastDto {

	private List<PrintBreakFastDto>  comboCountDate;
	
	private List<PrintBreakFastDto>  singleCountDate;
	
	private String coupletNo;
}
