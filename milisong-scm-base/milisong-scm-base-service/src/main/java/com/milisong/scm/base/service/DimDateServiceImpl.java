package com.milisong.scm.base.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.farmland.core.api.ResponseResult;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.base.api.DimDateService;
import com.milisong.scm.base.domain.DimDate;
import com.milisong.scm.base.domain.DimDateExample;
import com.milisong.scm.base.dto.DimDateDto;
import com.milisong.scm.base.dto.DimDateWeekDto;
import com.milisong.scm.base.dto.WeekInfo;
import com.milisong.scm.base.mapper.DimDateExtMapper;
import com.milisong.scm.base.mapper.DimDateMapper;
import com.milisong.scm.base.util.DateUtil;

/**
*@author    created by benny
*@date  2018年9月12日---下午6:59:03
*
*/
@RestController
public class DimDateServiceImpl implements DimDateService {

	@Autowired
	private DimDateExtMapper dimDateExtMapper;
	
	@Autowired
	private DimDateMapper dimDateMapper;
	@Override
	public ResponseResult<List<String>> getWorkDateByNow(Integer size) {
		List<String> listResult = null;
		size = size == null?5:size;
		listResult = dimDateExtMapper.getWorkDateByNow(size);
		return ResponseResult.buildSuccessResponse(listResult);
	}
	@Override
	public ResponseResult<List<Date>> getWorkDateByBeforeNow(Integer size) {
		List<Date> listResult = null;
		size = size == null?5:size;
		listResult = dimDateExtMapper.getWorkDateByBeforeNow(size);
		return ResponseResult.buildSuccessResponse(listResult);
	}
	
	/**
	 * 查询最大周
	 * @param dimDateWeekDto
	 * @return
	 */
	private DimDate getMaxWeekOfYear(String year){
		DimDateExample dimDateExample = new DimDateExample();
		dimDateExample.createCriteria().andWeekYearEqualTo(year);
		dimDateExample.setOrderByClause(" week_of_year desc ");
		List<DimDate> ListDim =	dimDateMapper.selectByExample(dimDateExample);
		if(CollectionUtils.isNotEmpty(ListDim))
			return ListDim.get(0);
		return null;
	}
	
	private String comboWeek(Integer weekOfYear) {
		if(weekOfYear<=9) {
			return "0" + weekOfYear ;
		}
		return weekOfYear+"";
	}
	 
	@Override
	public ResponseResult<List<WeekInfo>> getWeekInfoByDate(@RequestBody DimDateWeekDto dimDateWeekDto) {
		List<WeekInfo> listResutl = new ArrayList<WeekInfo>();
		Integer nowYear = DateUtil.getyearByDate(dimDateWeekDto.getNowDate());
		DimDate dimDate = getWeekOfYearByDate(dimDateWeekDto.getNowDate(), nowYear+"");
		DimDate maxDimDate = getMaxWeekOfYear(nowYear+"");
		DimDateExample dimDateExample = new DimDateExample();
		List<Integer> listWeekOfYear = new ArrayList<Integer>();
		listWeekOfYear.add(1);
		listWeekOfYear.add(7);
		String startWeek ="";
		if(dimDate.getWeekOfYear().equals("01")) {
			startWeek = "01";
		} else if(dimDate.getWeekOfYear().equals("02")) {
			startWeek = "01";
		} else {
			if(Integer.parseInt(dimDate.getWeekOfYear())<=9) {
				startWeek =comboWeek(Integer.parseInt(dimDate.getWeekOfYear())-2);
			}  else {
				startWeek = comboWeek(Integer.parseInt(dimDate.getWeekOfYear())-2);
				
			}
		}
		String endWeek = comboWeek(Integer.parseInt(dimDate.getWeekOfYear())+2);
		if(Integer.parseInt(dimDate.getWeekOfYear())<=9) {
			endWeek = comboWeek(Integer.parseInt(dimDate.getWeekOfYear())+2);
		}
		dimDateExample.createCriteria().andWeekOfYearBetween(startWeek, endWeek).andWeekYearEqualTo(nowYear+"").andDayOfWeekIn(listWeekOfYear);
		List<DimDate> ListDimWeek =dimDateMapper.selectByExample(dimDateExample);
		if(dimDate.getWeekOfYear().equals(maxDimDate.getWeekOfYear())) {
			dimDateExample.clear();
			dimDateExample.createCriteria().andWeekOfYearBetween("01","02").andWeekYearEqualTo((nowYear+1)+"").andDayOfWeekIn(listWeekOfYear);
			ListDimWeek.addAll(dimDateMapper.selectByExample(dimDateExample));
		}else if(dimDate.getWeekOfYear().equals((Integer.parseInt(maxDimDate.getWeekOfYear())-1)+"")) {
			dimDateExample.clear();
			dimDateExample.createCriteria().andWeekOfYearBetween("01","01").andWeekYearEqualTo((nowYear+1)+"").andDayOfWeekIn(listWeekOfYear);
			ListDimWeek.addAll(dimDateMapper.selectByExample(dimDateExample));
		}else if(dimDate.getWeekOfYear().equals("01")) {
			maxDimDate = getMaxWeekOfYear((nowYear-1)+"");
			dimDateExample.clear();
			Integer weekOfYear = Integer.parseInt(maxDimDate.getWeekOfYear());
			dimDateExample.createCriteria().andWeekOfYearBetween((weekOfYear-1)+"",weekOfYear+"").andWeekYearEqualTo((nowYear-1)+"").andDayOfWeekIn(listWeekOfYear);
			ListDimWeek.addAll(dimDateMapper.selectByExample(dimDateExample));
		}else if(dimDate.getWeekOfYear().equals("02")) {
			maxDimDate = getMaxWeekOfYear((nowYear)-1+"");
			dimDateExample.clear();
			Integer weekOfYear = Integer.parseInt(maxDimDate.getWeekOfYear());
			dimDateExample.createCriteria().andWeekOfYearBetween(weekOfYear+"",weekOfYear+"").andWeekYearEqualTo((nowYear-1)+"").andDayOfWeekIn(listWeekOfYear);
			ListDimWeek.addAll(dimDateMapper.selectByExample(dimDateExample));
		}
		
		Map<String,WeekInfo> maps = new TreeMap<String,WeekInfo>();
		for (DimDate dimDate2 : ListDimWeek) {
			WeekInfo weekInfo =	maps.get(dimDate2.getWeekOfYear());
			if(weekInfo == null) {
				weekInfo = new WeekInfo();
			}
			 
			weekInfo.setWeek(dimDate2.getWeekOfYear());
			if(dimDate2.getDayOfWeek() == 1) {
				weekInfo.setStartDate(dimDate2.getDate());
			}
			if(dimDate2.getDayOfWeek() == 7) {
				weekInfo.setEndDate(dimDate2.getDate());
			}
			weekInfo.setYear(dimDate2.getWeekYear());
			maps.put(dimDate2.getWeekOfYear(), weekInfo);
		}
		
		 listResutl.addAll(maps.values());
		 Collections.sort(listResutl,new Comparator<WeekInfo>() {

			@Override
			public int compare(WeekInfo o1, WeekInfo o2) {
				Integer o1Year = Integer.parseInt(o1.getYear());
				Integer o2Year = Integer.parseInt(o2.getYear());
				if(o1Year > o2Year) {
					return 1;
				} else if(o1Year < o2Year){
					return -1;
				}
				return 0;
			} 
			 
		 });
		 return ResponseResult.buildSuccessResponse(listResutl);
	}
	
	private DimDate getWeekOfYearByDate(Date nowDate, String nowYear) {
		DimDateExample dimDateExample = new DimDateExample();
		dimDateExample.createCriteria().andDateEqualTo(nowDate).andWeekYearEqualTo(nowYear);
		List<DimDate> ListDim =	dimDateMapper.selectByExample(dimDateExample);
		if(!CollectionUtils.isEmpty(ListDim)) {
			return ListDim.get(0);
		}
		return null;
	}
	@Override
	public ResponseResult<List<DimDateDto>> getWeekDateInfoByWeek(String nowWeek, String nowYear) {
		DimDateExample dimDateExample = new DimDateExample();
		dimDateExample.createCriteria().andWeekOfYearEqualTo(nowWeek).andWeekYearEqualTo(nowYear);
		List<DimDate> ListDim =	dimDateMapper.selectByExample(dimDateExample);
		if(CollectionUtils.isNotEmpty(ListDim)) {
			List<DimDateDto> listDimDateDto = BeanMapper.mapList(ListDim, DimDateDto.class);
			return ResponseResult.buildSuccessResponse(listDimDateDto);
		}
		return null;
	}
	
}
