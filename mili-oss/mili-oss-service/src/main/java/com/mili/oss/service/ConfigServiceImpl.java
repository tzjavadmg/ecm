package com.mili.oss.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.mili.oss.api.ConfigService;
import com.mili.oss.dto.config.ShopInterceptConfigDto;
import com.mili.oss.properties.ScmBFResdisProperties;
import com.mili.oss.util.RedisConfigUtil;
import com.mili.oss.util.ScmBFRedisHttpUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/29   11:44
 *    desc    :
 *    version : v1.0
 * </pre>
 */
@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {
    @Override
    public List<ShopInterceptConfigDto> queryInterceptConfigByShopId(Long shopId) {
        List<ShopInterceptConfigDto> list = RedisConfigUtil.getShopIncercept(shopId);
        return list;
    }
    @Autowired
    ScmBFResdisProperties scmBFResdisProperties;
    @Autowired 
    ScmBFRedisHttpUtils  scmBFRedisHttpUtils;
    
    private static final String SHOP_INTERCEPT_CONFIG_KEY = "config:intercept_config:scm:%s";
    
	@Override
	public List<ShopInterceptConfigDto> queryHttpInterceptConfigByShopId(Long shopId) {
		
		
		Object result = scmBFRedisHttpUtils.redisGet(scmBFResdisProperties.getScmBfRedisUrl(),String.format(SHOP_INTERCEPT_CONFIG_KEY, shopId.toString()));
		if(null == result) return null;
		List<ShopInterceptConfigDto> list = JSON.parseArray(result.toString(), ShopInterceptConfigDto.class);
        return list;
	}

    
}
