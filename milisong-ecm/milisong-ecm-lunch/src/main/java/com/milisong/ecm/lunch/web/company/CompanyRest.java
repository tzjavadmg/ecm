package com.milisong.ecm.lunch.web.company;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.google.common.collect.Lists;
import com.milisong.ecm.common.dto.BuildingDto;
import com.milisong.ecm.common.notify.constant.BusinessLineEnum;
import com.milisong.ecm.common.user.api.ApplyCompanyService;
import com.milisong.ecm.common.user.domain.ApplyCompany;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ecm.lunch.dto.CompanyBucketDto;
import com.milisong.ecm.lunch.dto.CompanyDto;
import com.milisong.ecm.lunch.dto.LbsAddressResultDto;
import com.milisong.ecm.lunch.service.CompanyService;
import com.milisong.ucs.api.UserDeliveryAddressService;
import com.milisong.ucs.dto.UserDeliveryAddressDto;
import com.milisong.ucs.dto.UserDeliveryAddressRequest;
import com.milisong.ucs.sdk.security.UserContext;
import com.milisong.ucs.sdk.security.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/2/18   16:48
 *    desc    :  公司业务rest层
 *    version : v1.0
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/v1/company/")
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
    //C端公司搜索展示信息条数
    private static final int COMPANY_LIST_SIZE = 20;

    /**
     * 记录用户地址到redis
     * @param companyId
     * @return
     */
    @GetMapping(value = "record-last-addr")
    ResponseResult<UserDeliveryAddressDto> recordLastAddr(@RequestParam(value = "companyId") Long companyId) {
        log.info("记录用户选择公司信息{}",companyId);
        long userId = UserContext.getCurrentUser().getId();
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
        String latestDeliveryStr = RedisCache.get(latestDeliveryAddressKey);
        log.info("用户是否下过单{}",latestDeliveryStr);
        if (StringUtils.isBlank(latestDeliveryStr)) {
            RedisCache.setEx(latestDeliveryAddressKey, JSON.toJSONString(userDeliveryAddressDto), 90, TimeUnit.DAYS);
        }
        return ResponseResult.buildSuccessResponse(userDeliveryAddressDto);
    }

    /**
     * 用户通过关键字搜索公司信息
     * @param name
     * @return
     */
    @GetMapping(value = "search-company")
    ResponseResult<List<CompanyBucketDto>> searchCompany(@RequestParam(value = "name", required = false) String name) {
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
                    companyList.add(JSONObject.parseObject(value.toString(),CompanyBucketDto.class));
                }
            }
            name = filteChar(name);
            if (StringUtils.isNotBlank(name)) {
                responseResult.setData(matchCompany(name, companyList));
            }else {
                companyList.sort(Comparator.comparingInt(CompanyDto::getOpenStatus).reversed());
                if (companyList.size()>COMPANY_LIST_SIZE) {
                    companyList = companyList.subList(0, COMPANY_LIST_SIZE);
                }
                responseResult.setData(companyList);
            }
        }catch(Exception e) {
            log.error("搜索公司异常{}",e);

        }
        return responseResult;
    }

    /**
     * 查询特定经纬度附近的楼宇
     *
     * @param lon
     * @param lat
     * @param limit
     * @return
     */
    @GetMapping("building-list-by-lbs")
    public ResponseResult<LbsAddressResultDto> getBuildingListByLbs(
            @RequestParam(value = "lon", required = false) Double lon,
            @RequestParam(value = "lat", required = false) Double lat,
            @RequestParam(value = "limit", required = false) Integer limit) {
        log.info("LBS查询公司相关信息,lon:{},lat:{},limit:{}", lon, lat, limit);
        LbsAddressResultDto result = new LbsAddressResultDto();
        if (null != lon && null != lat) {
            ResponseResult<JSONObject> resultData = httpGetLbs(convertServiceUrl,lon,lat);
            log.info("转换坐标系信息的结果：{}", resultData);
            if(!resultData.isSuccess()){
                return ResponseResult.buildFailResponse(resultData.getCode(),resultData.getMessage());
            }
            JSONObject location = resultData.getData();
            if (null != location) {
                lon = location.getDoubleValue("lon");
                lat = location.getDoubleValue("lat");
                ResponseResult<JSONObject> resultData0 = httpGetLbs(addressServiceUrl,lon,lat);
                log.info("获取当前位置的信息：{}", resultData0);
                if(!resultData0.isSuccess()){
                    return ResponseResult.buildFailResponse(resultData0.getCode(),resultData0.getMessage());
                }
                location = resultData0.getData();
                if(location != null){
                    result.setNowAddress(location.getString("sematicDescription"));
                }
            }
            result.setNearbyBuildings(companyService.listNearbyCompany(lon, lat, limit));
        }
        UserDeliveryAddressDto userDeliveryAddressDto = getUserLastDeliveryAddress();
        if (null != userDeliveryAddressDto && null != userDeliveryAddressDto.getId()) {
            userDeliveryAddressDto.setMobileNo(desensitizationPhone(userDeliveryAddressDto.getMobileNo()));
            String buildingStr = RedisCache
                    .get(RedisKeyUtils.getBuildingKey(userDeliveryAddressDto.getDeliveryOfficeBuildingId()));
            log.info("获取当前公司的简称的redis值：{}", buildingStr);
            if (StringUtils.isNotBlank(buildingStr)) {
                BuildingDto buildingDto = JSONObject.parseObject(buildingStr, BuildingDto.class);
                // 楼宇简称
                result.setNearbyBuildingName(buildingDto.getName());
                result.setNearbyBuildingId(buildingDto.getId());
            }
            result.setNearbyDeliveryAddress(userDeliveryAddressDto);
        }
        return ResponseResult.buildSuccessResponse(result);
    }

    /**
     * 用户申请开通公司
     * @param dto
     * @return
     */
    @PostMapping("apply-open-company")
    ResponseResult<Boolean> applyOpenCompany(@RequestBody ApplyCompany dto){
        dto.setUserId(UserContext.getCurrentUser().getId());
        dto.setBusinessLine(BusinessLineEnum.LUNCH.getVal());
        ResponseResult<Boolean> responseResult = applyCompanyService.save(dto);
        return responseResult;
    }

    /**
     * 获取用户最近的配送地址信息
     * @return
     */
    private UserDeliveryAddressDto getUserLastDeliveryAddress() {
        UserSession session = UserContext.getCurrentUser();
        String json = RedisCache.get(RedisKeyUtils.getLatestDeliveryAddressKey(session.getId()));
        log.info("获取我的地址信息:{}", json);
        UserDeliveryAddressDto userDeliveryAddressDto = null;
        if (StringUtils.isBlank(json)) {
            log.info("从redis获取地址信息失败，尝试从数据库获取");
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
        return userDeliveryAddressDto;
    }

    /**
     * 根据输入name，匹配公司
     * @param name
     * @param companyList
     * @return
     */
    private List<CompanyBucketDto> matchCompany(String name, List<CompanyBucketDto> companyList) {
        //从集合模糊搜索
        List<CompanyBucketDto> resultList = Lists.newArrayList();
        List<CompanyBucketDto> matchBuildingList = Lists.newArrayList();
        List<CompanyBucketDto> matchAddressList = Lists.newArrayList();
        Pattern pattern = Pattern.compile(name,Pattern.CASE_INSENSITIVE);
        //根据公司名称搜索,大楼名称搜索,地址搜索
        for (CompanyBucketDto companyDto :companyList) {
            Matcher matcher = pattern.matcher(companyDto.getName());
            if (matcher.find()) {
                resultList.add(companyDto);
                continue;
            }
            Matcher matcherBuilding = pattern.matcher(companyDto.getBuildingName());
            if(matcherBuilding.find()){
                matchBuildingList.add(companyDto);
                continue;
            }
            Matcher matcherAddress = pattern.matcher(companyDto.getDetailAddress());
            if(matcherAddress.find()){
                matchAddressList.add(companyDto);
                continue;
            }
        }
        if(!CollectionUtils.isEmpty(matchBuildingList)){
            resultList.addAll(matchBuildingList);
        }
        if(!CollectionUtils.isEmpty(matchAddressList)){
            resultList.addAll(matchAddressList);
        }
        resultList.sort(Comparator.comparingInt(CompanyBucketDto::getOpenStatus).reversed());
        if (resultList.size()>COMPANY_LIST_SIZE) {
            resultList = resultList.subList(0, COMPANY_LIST_SIZE);
        }
        return resultList;
    }

    /**
     * 自定义搜索过滤规则
     * @param name
     * @return
     */
    private String filteChar(String name) {
        if(name == null){
            name = "";
        }
        if(name.contains("(") || name.contains(")")) {
            name = name.replaceAll("[\\(\\)]", "");
        }
        return name;
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

    /**
     * 滴哦阿勇lbs接口转换坐标和查询位置
     * @param url
     * @param lon
     * @param lat
     * @return
     */
    private ResponseResult<JSONObject> httpGetLbs(String url,Double lon,Double lat){
        String httpUrl = url.concat("?lon=").concat(String.valueOf(lon)).concat("&lat=")
                .concat(String.valueOf(lat));
        String data = restTemplate.getForObject(httpUrl, String.class);
        log.info("调用坐标系转换/获取当前位置的url：{},result:{}", httpUrl,data);
        JSONObject jsonObj = JSONObject.parseObject(data);
        if(!jsonObj.getBooleanValue("success")) {
            return ResponseResult.buildFailResponse(jsonObj.getString("code"), jsonObj.getString("message"));
        }else {
           return ResponseResult.buildSuccessResponse(jsonObj.getJSONObject("data"));
        }
    }
}
