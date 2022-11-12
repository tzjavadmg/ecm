package com.milisong.scm.base.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RestController;

import com.farmland.core.api.ResponseResult;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.base.api.AreaService;
import com.milisong.scm.base.domain.Area;
import com.milisong.scm.base.domain.AreaExample;
import com.milisong.scm.base.dto.AreaDto;
import com.milisong.scm.base.mapper.AreaMapper;

/**
 * 区域服务
 * @author yangzhilong
 *
 */
@RestController
public class AreaServiceImpl implements AreaService {
    @Autowired
    private AreaMapper areaMapper;
    
    @Override
    public ResponseResult<List<AreaDto>> queryDistrictByCityCode(String cityCode) {
    	AreaExample areaExample = new AreaExample();
		areaExample.createCriteria().andPCodeEqualTo(cityCode).andStatusEqualTo((byte) 1);
		List<Area> areas = areaMapper.selectByExample(areaExample);
		if(!CollectionUtils.isEmpty(areas)) {
			return ResponseResult.buildSuccessResponse(BeanMapper.mapList(areas, AreaDto.class));
		}
    	
        return ResponseResult.buildSuccessResponse();
    }
}
