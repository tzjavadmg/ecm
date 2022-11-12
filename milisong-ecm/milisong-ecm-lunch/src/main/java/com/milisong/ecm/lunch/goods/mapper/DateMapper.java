package com.milisong.ecm.lunch.goods.mapper;
import java.util.List;

import com.milisong.ecm.lunch.goods.dto.WeekDto;
import org.apache.ibatis.annotations.Mapper;



@Mapper
public interface DateMapper {
	
	List<WeekDto> getDate();

}
