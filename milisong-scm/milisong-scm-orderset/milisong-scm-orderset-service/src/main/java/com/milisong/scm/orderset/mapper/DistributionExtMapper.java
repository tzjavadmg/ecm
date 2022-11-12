package com.milisong.scm.orderset.mapper;

import java.util.List;
import java.util.Map;

import com.milisong.scm.orderset.domain.Distribution;

public interface DistributionExtMapper {
     
	List<Distribution> selectByPage(Map<String,Object> paramMap);
	
	Integer selectByCount(Map<String,Object> paramMap);
}