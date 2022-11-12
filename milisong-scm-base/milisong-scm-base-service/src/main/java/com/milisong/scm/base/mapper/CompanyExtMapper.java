package com.milisong.scm.base.mapper;

import com.milisong.scm.base.dto.CompanyDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CompanyExtMapper {

    List<Long> selectAllCompanyId();

    List<Long> selectCompanyIdByParam(CompanyDto dto);
}