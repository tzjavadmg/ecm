package com.milisong.scm.base.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmland.core.api.ResponseResult;
import com.milisong.scm.base.dto.AreaDto;

/**
 * @author zhaozhonghui
 * @date 2018-09-12
 */
@FeignClient("milisong-scm-base-service")
public interface AreaService {

    /**
     * 根据城市编码查询区域
     * @param cityCode
     * @return
     */
	@PostMapping("/base/area/query-by-city-code")
    ResponseResult<List<AreaDto>> queryDistrictByCityCode(@RequestParam("cityCode") String cityCode);
}
