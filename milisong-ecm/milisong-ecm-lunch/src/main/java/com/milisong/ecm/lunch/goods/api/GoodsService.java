package com.milisong.ecm.lunch.goods.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.lunch.goods.dto.WeekDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

//@FeignClient("milisong-ecm-lunch")
public interface GoodsService {

	@PostMapping(value = "/v1/GoodsService/getDate")
	ResponseResult<List<WeekDto>> getDate(@RequestParam("shopCode") String shopCode);

	@PostMapping(value = "/v1/GoodsService/getOnSaleGoodsCodeSet")
	Set<String> getOnSaleGoodsCodeSet();

	@PostMapping(value = "/v1/GoodsService/get-schedule-date")
	ResponseResult<List<WeekDto>> getScheduleDate(@RequestParam("shopCode") String shopCode);
}
