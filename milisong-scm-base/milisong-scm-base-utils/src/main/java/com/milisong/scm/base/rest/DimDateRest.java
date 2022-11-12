package com.milisong.scm.base.rest;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.base.api.DimDateService;
import com.milisong.scm.base.dto.DimDateWeekDto;
import com.milisong.scm.base.dto.WeekInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018年12月3日---下午4:16:18
*
*/
@Api(tags = "工作日管理")
@Slf4j
@RestController
@RequestMapping("/dimDate")
public class DimDateRest {

	@Autowired
	private DimDateService dimDateService;
	
	@ApiOperation("查询前后两周的数据")
	@PostMapping("/get-week-info-by-date")
	public ResponseResult<List<WeekInfo>> getWeekInfoByDate(@RequestBody DimDateWeekDto dimDateWeekDto){
		if(null == dimDateWeekDto ) {
			dimDateWeekDto = new DimDateWeekDto();
			
		}
		log.info("获取当前时间的前后两个周{}",JSON.toJSONString(dimDateWeekDto));
		if( null == dimDateWeekDto.getNowDate() ) {
			dimDateWeekDto.setNowDate(new Date());
			
		}
		return dimDateService.getWeekInfoByDate(dimDateWeekDto);
	}
}
