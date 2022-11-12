package com.milisong.ecm.common.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.util.BeanMapper;
import com.google.common.collect.Maps;
import com.milisong.ecm.common.dto.CompanyDto;
import com.milisong.ecm.common.user.api.CompanyService;
import com.milisong.ecm.common.user.domain.Company;
import com.milisong.ecm.common.user.domain.UserDeliveryAddress;
import com.milisong.ecm.common.user.dto.UserCompany;
import com.milisong.ecm.common.user.mapper.CompanyMapper;
import com.milisong.ecm.common.user.mapper.UserDeliveryAddressMapper;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ucs.dto.UserDeliveryAddressDto;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaozhonghui
 * @date 2018-09-21
 */
@Service
@RestController
@Slf4j
public class CompanyServiceImpl implements CompanyService {
	@Autowired
	private CompanyMapper companyMapper;
	
	@Autowired
	private UserDeliveryAddressMapper deliveryAddressMapper;

	@Override
	public void saveMqCompany(String msg) {
		UserCompany userCompany;
		try {
			userCompany = JSONObject.parseObject(msg, UserCompany.class);
			if (null == userCompany.getBuildingId() || StringUtils.isBlank(userCompany.getFloor())
					|| StringUtils.isBlank(userCompany.getAbbreviation())) {
				log.error("公司信息缺失");
				return;
			}
			_saveDBCompany(userCompany);
			_saveRedisCompany(userCompany);
		} catch (Exception e) {
			log.error("处理公司信息报错:", e);
		}
	}

	@Override
	public void updateFloor(@RequestBody CompanyDto company) {
		if (StringUtils.isBlank(company.getReviseAfter())||
			StringUtils.isBlank(company.getReviseFront())||
			null == company.getBuildingId()) {
			return;
		}
		//更新用户最后一次下单地址楼层信息
		Map<String,Object> selectMap = Maps.newHashMap();
		selectMap.put("frontFloor",company.getReviseFront());
		selectMap.put("deliveryOfficeBuildingId", company.getBuildingId());
		List<UserDeliveryAddress> userAddressList = deliveryAddressMapper.selectUserIdByCondtion(selectMap);
		if (CollectionUtils.isNotEmpty(userAddressList)) {
			for (UserDeliveryAddress userAddress : userAddressList) {
				String latestDeliveryAddressKey = RedisKeyUtils.getLatestDeliveryAddressKey(userAddress.getUserId());
				String latestAddressMsg = RedisCache.get(latestDeliveryAddressKey);
				UserDeliveryAddressDto userDeliverAddressDto = JSONObject.parseObject(latestAddressMsg, UserDeliveryAddressDto.class);
				if (userDeliverAddressDto != null) {
					//修改后楼层
	                userDeliverAddressDto.setDeliveryFloor(company.getReviseAfter());
	                //重写最后一次用户收货地址
					RedisCache.setEx(latestDeliveryAddressKey, JSON.toJSONString(userDeliverAddressDto), 90, TimeUnit.DAYS);
				}

			}
		}
		
		//更新数据库用户收货地址表楼层信息
		Map<String,Object> updateMap = Maps.newHashMap();
		updateMap.put("afterFloor", company.getReviseAfter());
		updateMap.put("frontFloor", company.getReviseFront());
		updateMap.put("deliveryOfficeBuildingId", company.getBuildingId());
		deliveryAddressMapper.updateFloorByCondition(updateMap);
	}

	@Override
	public void updateCompanyName(@RequestBody CompanyDto company) {
		// TODO Auto-generated method stub

	}

	private void _saveDBCompany(UserCompany userCompany) {
		Company company = BeanMapper.map(userCompany, Company.class);
		Company findCompany = companyMapper.selectByPrimaryKey(company.getId());
		if (null == findCompany) {
			companyMapper.insertSelective(company);
		} else {
			company.setUpdateTime(null);
			companyMapper.updateByPrimaryKeySelective(company);
		}
	}

	private void _saveRedisCompany(UserCompany userCompany) {
		String companyKey = RedisKeyUtils.getCompanyKey(userCompany.getBuildingId(), userCompany.getFloor(),
				userCompany.getAbbreviation());
		Map<String, Object> map = new HashMap<>();
		map.put("isCertification", userCompany.getIsCertification());
		map.put("isSameName", userCompany.getIsSameName());
		map.put("sameName", userCompany.getSameName());
		map.put("abbreviation", userCompany.getAbbreviation());
		RedisCache.set(companyKey, JSONObject.toJSONString(map));
	}
}