package com.milisong.scm.base.api;

import java.util.Date;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmland.core.api.ResponseResult;
import com.milisong.scm.base.dto.DimDateDto;
import com.milisong.scm.base.dto.DimDateWeekDto;
import com.milisong.scm.base.dto.WeekInfo;

/**
*@author    created by benny
*@date  2018年9月12日---下午6:57:17
*
*/
@FeignClient("milisong-scm-base-service")
public interface DimDateService {

	/**
	 * 查询近五个工作日日期
	 * @param size
	 * @return
	 */
	@PostMapping("/base/dim-date/get-work-date-by-now")
	public ResponseResult<List<String>> getWorkDateByNow(@RequestParam(name = "size")Integer size);
	
	/**
	 * 查询当前时间之前的工作日
	 * @param size
	 * @return
	 */
	@PostMapping("/base/dim-date/get-work-date-by-before-now")
	public ResponseResult<List<Date>> getWorkDateByBeforeNow(@RequestParam(name = "size")Integer size);
	
	/**
	 * 
	 * 查询星期  前后各两周
	 */ 
	@PostMapping("/base/dim-date/get-week-info-by-date")
	public ResponseResult<List<WeekInfo>> getWeekInfoByDate(@RequestBody DimDateWeekDto dimDateWeekDto);
	
	/**
	 * 查詢周日期
	 * @param nowWeek
	 * @param nowYear
	 * @return
	 */
	@PostMapping("/base/dim-date/get-week-date-info-by-week")
	public ResponseResult<List<DimDateDto>> getWeekDateInfoByWeek(@RequestParam(name = "nowWeek")String nowWeek,@RequestParam(name = "nowYear")String nowYear);
}
