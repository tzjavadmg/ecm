package com.milisong.ecm.lunch.goods.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;

import com.farmland.core.cache.RedisCache;
import com.milisong.ecm.common.api.ShopConfigService;
import com.milisong.ecm.common.util.DateUtils;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ecm.common.util.WeekDayUtils;
import com.milisong.ecm.lunch.goods.api.GoodsService;
import com.milisong.ecm.lunch.goods.dto.GoodsScheduleMqDto;
import com.milisong.ecm.lunch.goods.dto.WeekDto;
import com.milisong.ecm.lunch.goods.mapper.DateMapper;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
@RestController
@Slf4j
public class GoodsServiceImpl implements GoodsService {
	@Resource
	private DateMapper dateMapper;

	@Resource
	private ShopConfigService shopConfigService;

	@Override
	public ResponseResult<List<WeekDto>> getDate(String shopCode) {
		List<WeekDto> resultList = dateMapper.getDate();
		log.info("可售日期查询结果{},",JSON.toJSONString(resultList));
		for (WeekDto week :resultList) {
			Date date = week.getDate();
			Long dateConver = week.getDateConver();
			Long currentDate = DateUtils.getCurrentTime();
			//判单当天
			if (dateConver.equals(currentDate)) {
				boolean flag = isPreOrder(new Date(),shopCode); //判断截止日期
				log.info("截止日期返回结果{}",flag);
				if (flag) {
					week.setStatus(0);
				}else {
					week.setStatus(1);
				}
			}else {
				week.setStatus(1);
			}
			week.setWeek(WeekDayUtils.getWeekDayString(date));
		}
		return ResponseResult.buildSuccessResponse(resultList);
	}

	@Override
	public Set<String> getOnSaleGoodsCodeSet() {
		return RedisCache.keys(RedisKeyUtils.GOODS_INFO_PREFIX + ":*");
	}

	@Override
	public ResponseResult<List<WeekDto>> getScheduleDate(String shopCode) {
		//获取档期key
		String shopScheduleKey = RedisKeyUtils.getShopScheduleDateKey(shopCode);
		Map<Object, Object> scheduleMap = RedisCache.hGetAll(shopScheduleKey);
		List<GoodsScheduleMqDto> scheduleList = Lists.newArrayList();
		if (scheduleMap != null && scheduleMap.size() > 0) {
			for (Map.Entry<Object, Object> shopSchedule : scheduleMap.entrySet()) {
				Object value = shopSchedule.getValue();
				GoodsScheduleMqDto scheduleDto = JSONObject.parseObject(value.toString(), GoodsScheduleMqDto.class);
				if (BooleanUtils.isTrue(scheduleDto.getStatus())) {
					scheduleList.add(scheduleDto);
				}

			}
		}
		List<WeekDto> weekList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(scheduleList)) {
			scheduleList.sort(Comparator.comparing(GoodsScheduleMqDto::getScheduleDate));
			long currentTime = System.currentTimeMillis();
			for (GoodsScheduleMqDto schedule : scheduleList) {
				long schdeuleMesc = schedule.getScheduleDate().getTime();
				if (schdeuleMesc <= currentTime && !WeekDayUtils.isThisTime(schdeuleMesc, "yyyy-MM-dd")) {
					continue;
				}
				WeekDto week = new WeekDto();
				Date scheduleDate = schedule.getScheduleDate();
				week.setDate(scheduleDate);
				week.setDateConver(schdeuleMesc);
				week.setStatus(1);
				if (WeekDayUtils.isThisTime(schdeuleMesc,"yyyy-MM-dd")) {
					boolean flag = isPreOrder(new Date(),shopCode); //判断截止日期
					if (flag) {
						week.setStatus(0);
					}
				}
				week.setWeek(WeekDayUtils.getWeekDayString(scheduleDate));
				weekList.add(week);
				if (weekList.size() == 5) {
					break;
				}
			}
		}
		return ResponseResult.buildSuccessResponse(weekList);
	}

	private boolean isPreOrder(Date orderDate,String shopCode) {
        Date cutOffOrderTime = shopConfigService.getTodayLastCutOffTime(shopCode);
        log.info("获取截止时间{}",cutOffOrderTime);
        return orderDate.compareTo(cutOffOrderTime) > 0;
    }
}
