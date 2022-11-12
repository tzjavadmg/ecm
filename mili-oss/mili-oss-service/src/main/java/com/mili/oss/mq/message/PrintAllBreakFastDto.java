package com.mili.oss.mq.message;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PrintAllBreakFastDto {

	private List<PrintBreakFastDto>  comboCountDate;
	
	private List<PrintBreakFastDto>  singleCountDate;
	
	private String coupletNo;
}
