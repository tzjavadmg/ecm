package com.milisong.scm.base.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmland.core.api.ResponseResult;
import com.milisong.scm.base.api.AreaService;
import com.milisong.scm.base.dto.AreaDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhaozhonghui
 * @date 2018-09-12
 */
@Slf4j
@RestController
@RequestMapping("/area")
@Api(tags = "区域管理")
public class AreaRest {

    @Autowired
    private AreaService areaService;

    @GetMapping("/get-district-by-city-code")
    @ApiOperation("根据父编码查询下级行政区域信息")
    public ResponseResult<List<AreaDto>> getDistrictByCityCode(@RequestParam("cityCode")String cityCode){
        log.info("根据城市编码查询区域，城市编码:{}",cityCode);
        return areaService.queryDistrictByCityCode(cityCode);
    }
}
