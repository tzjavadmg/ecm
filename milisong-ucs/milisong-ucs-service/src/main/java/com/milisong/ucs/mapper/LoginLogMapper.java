package com.milisong.ucs.mapper;

import com.milisong.ucs.domain.LoginLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoginLogMapper {
    int insert(LoginLog record);

    int insertSelective(LoginLog record);

    LoginLog selectByPrimaryKey(Long id);

    int updateByPrimaryKey(LoginLog record);

    List<LoginLog> selectAll();
}