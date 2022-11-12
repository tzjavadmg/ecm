package com.mili.oss.mq.message;

import com.mili.oss.domain.OrderSetGoods;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PrintBreakFastDto {

	private List<OrderSetGoods> listOrderSetDetail;
	
	private Map<String,Object> orderSetPrintMap;
	
	private Integer goodsCount;
	
	private List<OrderSetGoods> surplusSingle;
}
