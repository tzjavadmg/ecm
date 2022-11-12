package com.milisong.ecm.breakfast.web.company;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.google.common.collect.Lists;
import com.milisong.ecm.breakfast.company.service.CompanyService;
import com.milisong.ecm.breakfast.dto.CompanyBucketDto;
import com.milisong.ecm.breakfast.dto.CompanyDto;
import com.milisong.ecm.breakfast.dto.CompanyMealTimeDto;
import com.milisong.ecm.breakfast.dto.LbsAddressResultDto;
import com.milisong.ecm.common.dto.BuildingDto;
import com.milisong.ecm.common.enums.RestEnum;
import com.milisong.ecm.common.notify.constant.BusinessLineEnum;
import com.milisong.ecm.common.user.api.ApplyCompanyService;
import com.milisong.ecm.common.user.domain.ApplyCompany;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ucs.api.UserDeliveryAddressService;
import com.milisong.ucs.dto.UserDeliveryAddressDto;
import com.milisong.ucs.dto.UserDeliveryAddressRequest;
import com.milisong.ucs.sdk.security.UserContext;
import com.milisong.ucs.sdk.security.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公司信息接口
 * @author songmulin
 *
 */
@Slf4j
@RestController
public class CompanyRest {

	@Autowired
	private RestTemplate restTemplate;
	@Value("${lbs.address-service-url}")
	private String addressServiceUrl;
	@Value("${lbs.convert-service-url}")
	private String convertServiceUrl;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private UserDeliveryAddressService addressService;

	@Autowired
	private ApplyCompanyService applyCompanyService;

	private static final SimpleDateFormat formatHHmm = new SimpleDateFormat("HH:mm");

	/**
	 * 用户通过关键字搜索公司信息
	 * @param name
	 * @return
	 */
	@GetMapping(value = "/v1/company/search-company")
	ResponseResult<List<CompanyBucketDto>> searchCompany (@RequestParam(value = "name", required = false) String name) {
		log.info("搜索公司信息 ,name={}",name);
		ResponseResult<List<CompanyBucketDto>> responseResult = ResponseResult.buildSuccessResponse();
		try {
			String companyListKey = RedisKeyUtils.getCompanyListKey();
	    	List<CompanyBucketDto> companyList = Lists.newArrayList();
	    	//从redis获取公司集合数据
	    	Map<Object, Object> companyMap = RedisCache.hGetAll(companyListKey);
	    	for (Map.Entry<Object, Object> m : companyMap.entrySet()) {
	    		Object value = m.getValue();
	    		if (value !=null) {
	    			CompanyBucketDto buildingDto = JSONObject.parseObject(value.toString(),CompanyBucketDto.class);
	    			companyList.add(buildingDto);
	     		}
	     	}
	    	if(name.indexOf("(")>=0||name.indexOf(")")>=0) {
	    		name = name.replace("(", "").replace(")", "");
	    	}
	    	if (StringUtils.isNotBlank(name)) {
	        	//从集合模糊搜索
	        	List<CompanyBucketDto> resultList = Lists.newArrayList();
	        	Pattern pattern = Pattern.compile(name,Pattern.CASE_INSENSITIVE);
	        	//根据公司名称搜索
	        	for (CompanyBucketDto companyDto :companyList) {
	        		Matcher matcher = pattern.matcher(companyDto.getName());
	        		if (matcher.find()) {
	        			resultList.add(companyDto);
	        		}
	        	}
	        	//根据大楼名称搜索
	        	for (CompanyBucketDto companyDto :companyList) {
	        		Matcher matcher = pattern.matcher(companyDto.getBuildingName());
	        		if (matcher.find()) {
	        			if (!resultList.contains(companyDto)) {
	        				resultList.add(companyDto);
	        			}
	        			
	        		}
	        	}
	        	//根据地址搜索
	        	for (CompanyBucketDto companyDto :companyList) {
	        		Matcher matcher = pattern.matcher(companyDto.getDetailAddress());
	        		if (matcher.find()) {
	        			if (!resultList.contains(companyDto)) {
	        				resultList.add(companyDto);
	        			}
	        			
	        		}
	        	}
	        	resultList.sort(Comparator.comparingInt(CompanyBucketDto::getOpenStatus).reversed());
	        	if (resultList.size()>20) {
	        		resultList = resultList.subList(0, 20);
	        	}
	        	responseResult.setData(resultList);
	    	}else {
	    		companyList.sort(Comparator.comparingInt(CompanyDto::getOpenStatus).reversed());
	    		if (companyList.size()>20) {
	    			companyList = companyList.subList(0, 20);
	    		}
	    		responseResult.setData(companyList);
	    	}
		}catch(Exception e) {
			log.error("搜索公司异常{}",e);
			
		}
    	return responseResult;
	}
	
	/**
	 * 获取公司信息
	 * @param companyId
	 * @return
	 */
	@GetMapping(value = "/v1/company/company-info")
	ResponseResult<CompanyBucketDto> companyInfo (@RequestParam(value = "companyId" , required = true) Long companyId) {
		log.info("获取公司信息,companyId=",companyId);
		try {
			String companyKey = RedisKeyUtils.getCompanyKey(companyId);
			String companyStr = RedisCache.get(companyKey);
			CompanyBucketDto companyBucketDto = JSONObject.parseObject(companyStr, CompanyBucketDto.class);
			//查询用户最后一次下单，如果是这个公司的话，那就取它的配送时间
			String deliveryAddressKey = RedisKeyUtils.getLatestDeliveryAddressKey(UserContext.getCurrentUser().getId());
			//用户进入首页从redis获取最后一次下单收货地址
			String deliveryAddress = RedisCache.get(deliveryAddressKey);
			if(StringUtils.isBlank(deliveryAddress)){
				return ResponseResult.buildSuccessResponse(companyBucketDto);
			}
			UserDeliveryAddressDto deliveryAddressDto = JSONObject.parseObject(deliveryAddress, UserDeliveryAddressDto.class);
			log.info("获取用户最后一次下单地址{}", JSON.toJSONString(deliveryAddressDto));
			if(deliveryAddressDto!=null ){
				Date deliveryTimeBegin = deliveryAddressDto.getDeliveryTimeBegin();
				Date deliveryTimeEnd = deliveryAddressDto.getDeliveryTimeEnd();
				if(deliveryTimeBegin!=null && deliveryTimeEnd!=null){
					companyBucketDto.setDeliveryTimeBegin(formatHHmm.format(deliveryTimeBegin));
					companyBucketDto.setDeliveryTimeEnd(formatHHmm.format(deliveryTimeEnd));
				}
			}
			if(companyBucketDto.getDeliveryTimeBegin() ==null
					|| companyBucketDto.getDeliveryTimeEnd() == null){
				List<CompanyMealTimeDto> mealTimeList = companyBucketDto.getMealTimeList();
				CompanyMealTimeDto mealTimeDto = mealTimeList.get(0);
				if(mealTimeDto!=null
						&& mealTimeDto.getDeliveryTimeBegin() !=null
						&& mealTimeDto.getDeliveryTimeEnd()!=null){
					companyBucketDto.setDeliveryTimeBegin(formatHHmm.format(mealTimeDto.getDeliveryTimeBegin()));
					companyBucketDto.setDeliveryTimeEnd(formatHHmm.format(mealTimeDto.getDeliveryTimeEnd()));
				}
			}
			return ResponseResult.buildSuccessResponse(companyBucketDto);
		}catch(Exception e) {
			log.error("获取公司信息异常");
			return ResponseResult.buildFailResponse();
		}
	}

	/**
	 * 获取待开通楼宇
	 * @param companyId
	 * @return
	 */
	@GetMapping(value = "/v1/company/toopen-company")
	ResponseResult<String> toOpenCompany(@RequestParam(value = "companyId" , required = true) Long companyId) {
		return null;
	}

	/**
	 * 查询特定经纬度附近的楼宇
	 *
	 * @param lon
	 * @param lat
	 * @param limit
	 * @return
	 */
	@GetMapping("/v1/company/building-list-by-lbs")
	public ResponseResult<LbsAddressResultDto> getBuildingListByLbs(
			@RequestParam(value = "lon", required = false) Double lon,
			@RequestParam(value = "lat", required = false) Double lat,
			@RequestParam(value = "limit", required = false) Integer limit) {
		log.info("LBS查询公司相关信息,lon:{},lat:{},limit:{}", lon, lat, limit);
		LbsAddressResultDto result = new LbsAddressResultDto();

		if (null != lon && null != lat) {
			String url = convertServiceUrl.concat("?lon=").concat(String.valueOf(lon)).concat("&lat=")
					.concat(String.valueOf(lat));
			log.info("调用坐标系转换的url：{}", url);
			String data = restTemplate.getForObject(url, String.class);
			log.info("转换坐标系信息的结果：{}", data);
			if (StringUtils.isNotBlank(data)) {
				JSONObject jsonObj = JSONObject.parseObject(data);
				if (!jsonObj.getBooleanValue("success")) {
					return ResponseResult.buildFailResponse(jsonObj.getString("code"), jsonObj.getString("message"));
				}
				JSONObject location = jsonObj.getJSONObject("data");
				if (null != location) {
					lon = location.getDoubleValue("lon");
					lat = location.getDoubleValue("lat");

					url = addressServiceUrl.concat("?lon=").concat(String.valueOf(lon)).concat("&lat=")
							.concat(String.valueOf(lat));
					log.info("调用获取当前位置的url：{}", url);

					data = restTemplate.getForObject(url, String.class);
					log.info("获取当前位置的信息：{}", data);
					if (StringUtils.isNotBlank(data)) {
						jsonObj = JSONObject.parseObject(data);
						if (!jsonObj.getBooleanValue("success")) {
							return ResponseResult.buildFailResponse(jsonObj.getString("code"),
									jsonObj.getString("message"));
						}
						location = jsonObj.getJSONObject("data");
						if (null != location) {
							result.setNowAddress(location.getString("sematicDescription"));
						}
					}
				}
			}
			result.setNearbyBuildings(companyService.listNearbyCompany(lon, lat, limit));
		}

		UserSession session = UserContext.getCurrentUser();
		String json = RedisCache.get(RedisKeyUtils.getLatestDeliveryAddressKey(session.getId()));
		log.info("获取我的地址信息:{}", json);

		UserDeliveryAddressDto userDeliveryAddressDto = null;

		if (StringUtils.isBlank(json)) {
			log.info("从reidis获取地址信息失败，尝试从数据库获取");
			UserDeliveryAddressRequest request = new UserDeliveryAddressRequest();
			request.setUserId(session.getId());
			ResponseResult<UserDeliveryAddressDto> data = addressService.queryDeliveryAddr(request);
			log.info("从数据库获取到的地址信息：{}", JSONObject.toJSONString(data));
			if (data.isSuccess() && data.getData() != null) {
				userDeliveryAddressDto = data.getData();
			}
		} else {
			userDeliveryAddressDto = JSONObject.parseObject(json, UserDeliveryAddressDto.class);
		}

		if (null != userDeliveryAddressDto) {
			if (null != userDeliveryAddressDto.getId()) {
				userDeliveryAddressDto.setMobileNo(desensitizationPhone(userDeliveryAddressDto.getMobileNo()));
				String buildingStr = RedisCache
						.get(RedisKeyUtils.getBuildingKey(userDeliveryAddressDto.getDeliveryOfficeBuildingId()));
				log.info("获取当前公司的简称的reidis值：{}", buildingStr);
				if (StringUtils.isNotBlank(buildingStr)) {
					BuildingDto buildingDto = JSONObject.parseObject(buildingStr, BuildingDto.class);
					// 楼宇简称
					result.setNearbyBuildingName(buildingDto.getName());
					result.setNearbyBuildingId(buildingDto.getId());
				}
				result.setNearbyDeliveryAddress(userDeliveryAddressDto);
			}

		}
		return ResponseResult.buildSuccessResponse(result);
	}

	
	/**
	 * 暂存用户最近一次选择公司信息
	 * @param companyId
	 * @return
	 */
    @GetMapping(value = "/v1/company/record-last-addr")
    ResponseResult<UserDeliveryAddressDto> recordLastAddr(@RequestParam(value = "companyId" , required = true) Long companyId) {
    	log.info("记录用户选择公司信息{}",companyId);
    	UserSession session = UserContext.getCurrentUser();
    	long userId = session.getId();
        String companyKey  = RedisKeyUtils.getCompanyKey(companyId);
        String companyStr = RedisCache.get(companyKey);
		CompanyBucketDto companyBucketDto = JSONObject.parseObject(companyStr, CompanyBucketDto.class);
        UserDeliveryAddressDto userDeliveryAddressDto = new UserDeliveryAddressDto();
        if (companyBucketDto != null) {
        	//公司名称
        	userDeliveryAddressDto.setDeliveryCompany(companyBucketDto.getName()); 
        	//楼宇名称
        	userDeliveryAddressDto.setDeliveryAddress(companyBucketDto.getBuildingName());
        	//楼层
        	userDeliveryAddressDto.setDeliveryFloor(companyBucketDto.getFloor());
        	//公司id
        	userDeliveryAddressDto.setDeliveryOfficeBuildingId(companyBucketDto.getId());
        }
        String latestDeliveryAddressKey = RedisKeyUtils.getLatestDeliveryAddressKey(userId);
        //如果是新人，就记录数据库
        if(UserContext.getCurrentUser().getIsNew().equals((byte)0)){
			String jsonString = JSON.toJSONString(userDeliveryAddressDto);
			RedisCache.setEx(latestDeliveryAddressKey, JSON.toJSONString(userDeliveryAddressDto), 90, TimeUnit.DAYS);
			userDeliveryAddressDto.setUserId(UserContext.getCurrentUser().getId());
			userDeliveryAddressDto.setBusinessLine(BusinessLineEnum.BREAKFAST.getVal());
			log.info("记录用户选择的公司信息到数据库:{}",jsonString);
			addressService.saveOrUpdateTempDeliveryAddr(userDeliveryAddressDto);
		}else{
        	//如果是老人，就不记录
			String latestDeliveryStr = RedisCache.get(latestDeliveryAddressKey);
			log.info("用户是否下过单{}",latestDeliveryStr);
			if (StringUtils.isBlank(latestDeliveryStr)) {
				RedisCache.setEx(latestDeliveryAddressKey, JSON.toJSONString(userDeliveryAddressDto), 90, TimeUnit.DAYS);
			}
		}
		return ResponseResult.buildSuccessResponse(userDeliveryAddressDto);
    }

	/**
	 * 用户申请开通公司
	 * @param dto
	 * @return
	 */
	@PostMapping("/v1/company/apply-open-company")
	ResponseResult<Boolean> applyOpenCompany(@RequestBody ApplyCompany dto){
	    dto.setUserId(UserContext.getCurrentUser().getId());
	    dto.setBusinessLine(BusinessLineEnum.BREAKFAST.getVal());
        ResponseResult<Boolean> responseResult = applyCompanyService.save(dto);
        return responseResult;
	}


	/**
	 * 脱敏手机号
	 *
	 * @param phone
	 * @return
	 */
	private String desensitizationPhone(String phone) {
		if (StringUtils.isNotBlank(phone)) {
			StringBuilder sb = new StringBuilder(phone.substring(0, 3));
			sb.append("****").append(phone.substring(7));
			return sb.toString();
		}
		return phone;
	}
}
