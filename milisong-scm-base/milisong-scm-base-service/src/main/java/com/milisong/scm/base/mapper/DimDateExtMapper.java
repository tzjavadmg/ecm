package com.milisong.scm.base.mapper;

import java.util.Date;
import java.util.List;

public interface DimDateExtMapper {
	
	List<String> getWorkDateByNow(Integer size);

	List<Date> getWorkDateByBeforeNow(Integer size);
}