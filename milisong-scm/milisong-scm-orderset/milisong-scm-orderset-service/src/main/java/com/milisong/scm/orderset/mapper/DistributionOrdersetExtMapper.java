package com.milisong.scm.orderset.mapper;

import java.util.List;
import java.util.Map;

import com.milisong.scm.orderset.dto.result.DistributionOrdersetInfoResult;
import com.milisong.scm.orderset.result.BuildingBaseInfoSqlResult;

import io.lettuce.core.dynamic.annotation.Param;

public interface DistributionOrdersetExtMapper {

    List<BuildingBaseInfoSqlResult> selectBuildingByDistributionNo(@Param("distributionNo")String distributionNo);

    List<Map<String,Object>> customerCountByDistributionNo(List<String> list);
    
    List<DistributionOrdersetInfoResult> getCustomerOrderByDistributionNo(@Param("distributionNo")String distributionNo );

    List<String> getOrdersetNoByDistributionNo(@Param("distributionNo")String distributionNo);

	List<Long> getOrdersetIdByDistributionNo(@Param("distributionNo") String distributionNo);

	List<DistributionOrdersetInfoResult> getCustomerOrderByOrderSetNo(@Param("setNo")String setNo);
}