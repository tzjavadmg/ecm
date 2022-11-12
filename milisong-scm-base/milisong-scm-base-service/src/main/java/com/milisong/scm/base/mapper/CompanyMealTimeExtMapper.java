package com.milisong.scm.base.mapper;

import com.milisong.scm.base.dto.CompanyMealTimeDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyMealTimeExtMapper {

    List<CompanyMealTimeDto> selectByCompanyId(@Param("companyId") Long companyId,@Param("businessLine")Byte businessLine , @Param("flag") Boolean flag);

    int updateByCompanyId(@Param("companyId") Long companyId,@Param("businessLine")Byte businessLine, @Param("flag") Boolean flag);
}