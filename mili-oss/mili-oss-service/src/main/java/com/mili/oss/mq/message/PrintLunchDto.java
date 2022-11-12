package com.mili.oss.mq.message;


import com.mili.oss.domain.OrderSetGoods;
import com.mili.oss.dto.OrderSetDetailGoodsDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PrintLunchDto {

	private List<OrderSetGoods> customerInfo;
	
	private List<OrderSetGoods> picklesInfo;
	
	private Map<String,Object> orderSetPrintMap;
	
	private String coupletNo;
}
