package com.milisong.delay.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.milisong.delay.domain.DelayConsumeEvent;
import com.milisong.delay.mapper.base.BaseDelayConsumeEventMapper;


@Mapper
public interface DelayConsumeEventMapper extends BaseDelayConsumeEventMapper {
	
	List<DelayConsumeEvent> selectFailData();
	
	void updateStatusById(Long id);
	
	void updateSendNumById(Long id);
	
	List<DelayConsumeEvent> selectFailDataByCondition(Map<String,Object> params);
}