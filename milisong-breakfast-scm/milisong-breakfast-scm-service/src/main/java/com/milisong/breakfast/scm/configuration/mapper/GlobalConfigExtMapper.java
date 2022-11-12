package com.milisong.breakfast.scm.configuration.mapper;

import com.milisong.breakfast.scm.configuration.domain.GlobalConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GlobalConfigExtMapper {

    List<GlobalConfig> queryGlobalConfig();

    GlobalConfig queryByKey(@Param("configKey") String configKey);

}