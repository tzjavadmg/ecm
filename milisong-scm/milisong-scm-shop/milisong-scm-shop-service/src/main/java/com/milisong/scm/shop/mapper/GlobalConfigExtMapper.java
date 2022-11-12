package com.milisong.scm.shop.mapper;

import com.milisong.scm.shop.domain.GlobalConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GlobalConfigExtMapper {

    List<GlobalConfig> queryGlobalConfig();

    GlobalConfig queryByKey(@Param("configKey") String configKey);
}