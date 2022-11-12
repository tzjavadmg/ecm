package com.milisong.scm.shop.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.scm.shop.dto.config.GlobalConfigDto;

import java.util.List;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/23   17:30
 *    desc    : 全局配置业务接口
 *    version : v1.0
 * </pre>
 */

public interface GlobalConfigService {

    ResponseResult<GlobalConfigDto> saveOrUpdate(GlobalConfigDto dto);

    ResponseResult<List<GlobalConfigDto>> getGloableConfig();

    ResponseResult<GlobalConfigDto> getGloableConfigById(Long id);

    String getGloableConfigByKey(String key);

    ResponseResult<Boolean> syncConfig();
}
