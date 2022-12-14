package com.milisong.ecm.breakfast.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.milisong.ecm.breakfast.dto.BfUserAddressDto;
import com.milisong.ecm.breakfast.dto.CompanyBucketDto;
import com.milisong.ecm.breakfast.dto.CompanyMealAddressDto;
import com.milisong.ecm.breakfast.dto.CompanyMealTimeDto;
import com.milisong.ecm.common.api.ShopConfigService;
import com.milisong.ecm.common.dto.ShopConfigDto;
import com.milisong.ecm.common.user.dto.UserCompany;
import com.milisong.ecm.common.user.dto.UserCompanySortDto;
import com.milisong.ecm.common.util.RedisKeyUtils;
import com.milisong.ecm.common.util.XMLUtil;
import com.milisong.oms.constant.OrderType;
import com.milisong.ucs.api.UserDeliveryAddressService;
import com.milisong.ucs.api.UserService;
import com.milisong.ucs.dto.*;
import com.milisong.ucs.sdk.security.UserContext;
import com.milisong.ucs.sdk.security.UserSession;
import com.milisong.wechat.miniapp.api.MiniAppService;
import com.milisong.wechat.miniapp.dto.MiniContractDto;
import com.milisong.wechat.miniapp.dto.MiniContractRequest;
import com.milisong.wechat.miniapp.enums.BusinessLine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.stream.Collectors;

import static com.milisong.ucs.enums.UserResponseCode.QUERY_USER_INFO_EXCEPTION;
import static com.milisong.ucs.enums.UserResponseCode.REQUEST_PARAM_EMPTY;
import static com.milisong.ucs.enums.UserSourceEnum.MINI_APP;

/**
 * @author sailor wang
 * @date 2018/9/2 ??????2:40
 * @description
 */
@Slf4j
@Service
public class RestUserService {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDeliveryAddressService addressService;

    @Autowired
    private MiniAppService miniAppService;

    @Autowired
    private ShopConfigService shopConfigService;

 

    public ResponseResult<UserDto> login(UserRequest userRequest) {
        userRequest.setBusinessLine(OrderType.BREAKFAST.getValue());
        ResponseResult<UserDto> result = userService.login(userRequest);
        if (result != null && result.getData() != null){
            fillShareData(result.getData());
        }
        return result;
    }

    private void fillShareData(UserDto userDto){
        try {
            //?????????????????????
            userDto.setPictureUrl(shopConfigService.getSharePicture());
            //?????????????????????
            userDto.setTitle(shopConfigService.getShareTitle());
        } catch (Exception e) {
            log.error("",e);
        }
    }

    /**
     * ???????????????
     *
     * @return
     */
    public ResponseResult<MiniContractDto> validateContractNo(String contractNo) {
        if (contractNo == null || contractNo.trim().length() == 0) {
            contractNo = UserContext.loadCurrentUser().getWechatContractNo();
        }
        MiniContractRequest request = MiniContractRequest.builder().contractNo(contractNo).build();
        request.setBusinessLine(OrderType.BREAKFAST.getValue());
        Byte buzLine = OrderType.BREAKFAST.getValue();
        ResponseResult<MiniContractDto> responseResult = miniAppService.validateContractNo(request);
        if (responseResult.isSuccess() && responseResult.getData() != null && StringUtils.isNotBlank(responseResult.getData().getWechatContractNo())) {
            UserSession userSession = UserContext.loadCurrentUser();
            UserContext.freshUser(userSession.getOpenId(), userSession.getRegisterSource(),buzLine, "wechatContractNo", responseResult.getData().getWechatContractNo());
        }
        log.info("??????????????? -> {}", JSON.toJSONString(responseResult));
        return responseResult;
    }

    public void contractCallBack(HttpServletRequest request) {
        try {
            String inputLine;
            String notityXml = "";
            while ((inputLine = request.getReader().readLine()) != null) {
                notityXml += inputLine;
            }
            request.getReader().close();
            log.info("???????????? notityXml ------> {}", notityXml);
            SortedMap<String, String> map = XMLUtil.doXMLParse(notityXml);
            String openid = map.get("openid");
            String contractNo = map.get("contract_id");
            UserDto userDto = UserDto.builder().openId(openid).registerSource(MINI_APP.getCode()).wechatContractNo(contractNo).build();
            userService.updateByOpenid(userDto);
        } catch (IOException e) {
            log.error("", e);
        }
    }

    public void cancelContract(HttpServletRequest request) {
        try {
            String inputLine;
            String notityXml = "";
            while ((inputLine = request.getReader().readLine()) != null) {
                notityXml += inputLine;
            }
            request.getReader().close();
            log.info("???????????? notityXml ------> {}", notityXml);
        } catch (IOException e) {
            log.error("", e);
        }
    }

    public ResponseResult<UserDto> getUserInfo() {
        UserDto userDto = new UserDto();
        userDto.setId(UserContext.getCurrentUser().getId());
        userDto.setBusinessLine(OrderType.BREAKFAST.getValue());
        return userService.getUserInfo(userDto);
    }

    public ResponseResult<UserDto> getUserInfo(Long userId, String session) {
        try {
            if (userId != null) {
                UserDto userDto = new UserDto();
                userDto.setId(UserContext.getCurrentUser().getId());
                userDto.setBusinessLine(OrderType.BREAKFAST.getValue());
                return userService.getUserInfo(userDto);
            } else if (StringUtils.isNotBlank(session)) {
                Map<Object, Object> map = UserContext.getUserBySession(session);
                UserDto userDto = BeanMapper.map(map, UserDto.class);
                return ResponseResult.buildSuccessResponse(userDto);
            }
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        } catch (Exception e) {
            log.error("", e);
            return ResponseResult.buildFailResponse(QUERY_USER_INFO_EXCEPTION.code(), QUERY_USER_INFO_EXCEPTION.message());
        }
    }

    public ResponseResult<UserDto> updateUser(UserRequest userRequest) {
        UserDto userDto = BeanMapper.map(userRequest, UserDto.class);
        userDto.setBusinessLine(OrderType.BREAKFAST.getValue());
        userDto.setId(UserContext.getCurrentUser().getId());
        userDto.setOpenId(UserContext.getCurrentUser().getOpenId());
        userDto.setWxSessionKey(UserContext.getCurrentUser().getWxSessionKey());
        return userService.updateUser(userDto);
    }

    /**
     * ??????????????????
     *
     * @param address
     * @return
     */
    public ResponseResult<Boolean> addDeliveryAddr(UserDeliveryAddressDto address) {
        address.setBusinessLine(OrderType.BREAKFAST.getValue());
        return addressService.saveDeliveryAddr(address);
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    @Deprecated
    public ResponseResult<BfUserAddressDto> getDeliveryAddr(Long companyId) {
        return getDeliveryAddr(companyId,BusinessLine.BREAKFAST.getCode());
    }


    public ResponseResult<BfUserAddressDto> getDeliveryAddr(Long companyId,Byte businessLine) {
        Long userid = UserContext.getCurrentUser().getId();
        if (null == companyId) {
            String deliveryAddressKey = RedisKeyUtils.getLatestDeliveryAddressKey(userid);
            String deliveryAddress = RedisCache.get(deliveryAddressKey); //?????????????????????redis????????????????????????????????????
            UserDeliveryAddressDto deliveryAddressDto = JSONObject.parseObject(deliveryAddress, UserDeliveryAddressDto.class);
            log.info("????????????????????????????????????{}", JSON.toJSONString(deliveryAddressDto));
            if (deliveryAddressDto != null) {
                companyId = deliveryAddressDto.getDeliveryOfficeBuildingId();
                BfUserAddressDto bfUserAddressDto = new BfUserAddressDto();
                BeanMapper.copy(deliveryAddressDto, bfUserAddressDto);
                List<CompanyMealAddressDto> mealsAddressList = getMealsAddress(companyId);
                List<CompanyMealTimeDto> mealsTimeList = getMealsTime(companyId,businessLine);
                bfUserAddressDto.setMealAddressList(mealsAddressList);
                bfUserAddressDto.setDeliveryTimeList(mealsTimeList);
                return ResponseResult.buildSuccessResponse(bfUserAddressDto);
            }
        }
        //?????????????????????id??????????????????
        UserDeliveryAddressRequest request = UserDeliveryAddressRequest.builder().userId(userid).deliveryOfficeBuildingId(companyId).build();
        request.setBusinessLine(businessLine);
        log.info("????????????????????????{}", JSON.toJSONString(request));
        ResponseResult<UserDeliveryAddressDto> result = addressService.queryDeliveryAddr(request);
        UserDeliveryAddressDto userDeliverAddressDto = result.getData();
        BfUserAddressDto bfUserAddressDto = new BfUserAddressDto();
        if (result.isSuccess() && null != userDeliverAddressDto) {
            BeanMapper.copy(userDeliverAddressDto, bfUserAddressDto);
        }
        if(userDeliverAddressDto == null || StringUtils.isBlank(userDeliverAddressDto.getMobileNo())){
            bfUserAddressDto.setId(null);
            //????????????????????????????????????(????????????????????????????????????????????????????????????)????????????????????????????????????
            bfUserAddressDto.setMobileNo(UserContext.getCurrentUser().getMobileNo());
        }
        List<CompanyMealAddressDto> mealsAddressList = getMealsAddress(companyId);
        List<CompanyMealTimeDto> mealsTimeList = getMealsTime(companyId,businessLine);
        bfUserAddressDto.setMealAddressList(mealsAddressList);
        bfUserAddressDto.setDeliveryTimeList(mealsTimeList);
        return ResponseResult.buildSuccessResponse(bfUserAddressDto);
    }
    /**
     * ?????????????????????
     * @param companyId
     * @return
     */
    private List<CompanyMealAddressDto> getMealsAddress(Long companyId) {
    	String companyKey = RedisKeyUtils.getCompanyKey(companyId);
    	String companyStr = RedisCache.get(companyKey);
    	List<CompanyMealAddressDto> mealAddressList = Lists.newArrayList();
    	if (StringUtils.isNotBlank(companyStr)) {
			CompanyBucketDto companyBucketDto = JSONObject.parseObject(companyStr, CompanyBucketDto.class);
			mealAddressList =  companyBucketDto.getMealAddressList();
    	}
    	log.info("???????????????????????????{}",JSON.toJSONString(mealAddressList));
    	return mealAddressList;
    }

    /**
     * ????????????????????????
     * @param companyId
     * @return
     */
    private List<CompanyMealTimeDto> getMealsTime(Long companyId,Byte businessLine){
        String companyKey = RedisKeyUtils.getCompanyKey(companyId);
        String companyStr = RedisCache.get(companyKey);
        List<CompanyMealTimeDto> mealTimeList = Lists.newArrayList();
        if (StringUtils.isNotBlank(companyStr)) {
            CompanyBucketDto companyBucketDto = JSONObject.parseObject(companyStr, CompanyBucketDto.class);
            if(businessLine.equals(BusinessLine.BREAKFAST.getCode())){
                mealTimeList =  companyBucketDto.getMealTimeList();
            }else if(businessLine.equals(BusinessLine.LUNCH.getCode())){
                mealTimeList =  companyBucketDto.getLunchMealTimeList();
            }
        }
        log.info("??????????????????????????????{}",JSON.toJSONString(mealTimeList));
        return mealTimeList;
    }
    /**
     * ??????
     *
     * @return
     */
    public ResponseResult<Map<String, Object>> signContract() {
        String mobileNo = UserContext.getCurrentUser().getMobileNo();
        MiniContractRequest contractRequest = MiniContractRequest.builder().mobileNo(mobileNo).build();
        contractRequest.setBusinessLine(OrderType.BREAKFAST.getValue());
        ResponseResult<Map<String, Object>> miniContract = miniAppService.signContract(contractRequest);
        return miniContract;
    }

    /**
     * ??????????????????????????????(???????????????)
     *
     * @return
     */
    public ResponseResult<String> isPoint() {
        ResponseResult<String> responseResult = ResponseResult.buildSuccessResponse(new String());
        UserSession userSession = UserContext.loadCurrentUser();
        byte isNew = userSession.getIsNew();
        Long userId = userSession.getId();
        String userCountKey = RedisKeyUtils.getUserCountKey(userId);
        int redisUserCount = RedisCache.get(userCountKey) == null ? 0 : Integer.parseInt(RedisCache.get(userCountKey)); //redis??????
        int hintCount = shopConfigService.getSameCompanyHintCount(); //??????????????????
        log.info("????????????redisUserCount={},hintCount={},isNew={}", redisUserCount, hintCount, isNew);
        //?????????????????? ?????????????????????????????????
        if (isNew == 1 && redisUserCount < hintCount) {
            //?????????????????????????????????
            String deliveryAddressKey = RedisKeyUtils.getLatestDeliveryAddressKey(userId);
            String deliveryAddress = RedisCache.get(deliveryAddressKey);//??????????????????????????????????????????
            UserDeliveryAddressDto deliveryAddressDto = JSONObject.parseObject(deliveryAddress, UserDeliveryAddressDto.class);
            log.info("??????????????????????????????????????????{}", JSON.toJSONString(deliveryAddressDto));
            if (deliveryAddressDto != null) {
                Long buildingId = deliveryAddressDto.getDeliveryOfficeBuildingId();
                String floor = deliveryAddressDto.getDeliveryFloor();
                String deliveryCompany = deliveryAddressDto.getDeliveryCompany();
                String companyKey = RedisKeyUtils.getCompanyKey(buildingId, floor, deliveryCompany);
                log.info("????????????????????????key{}", companyKey);
                String redisResult = RedisCache.get(companyKey);
                UserCompany company = JSONObject.parseObject(redisResult, UserCompany.class);
                log.info("??????????????????????????????{}", JSON.toJSONString(company));
                if (company != null) {
                    //??????
                    if (company.getIsSameName()) {
                        responseResult.setData(company.getSameName() == null ? "" : company.getSameName());
                    }
                }

            }

        }
        log.info("????????????????????????:{}", JSON.toJSONString(responseResult));
        return responseResult;
    }

    /**
     * ?????????????????????????????????
     *
     * @param companyName
     * @return
     */
    public ResponseResult<Boolean> operateYesOrNo(String companyName) {
        ResponseResult<Boolean> responseResult = ResponseResult.buildSuccessResponse(true);
        UserSession userSession = UserContext.loadCurrentUser();
        Long userId = userSession.getId();
        String userCountKey = RedisKeyUtils.getUserCountKey(userId);
        //?????????????????????????????????????????????
        if (StringUtils.isNotEmpty(companyName)) {
            UserDeliveryAddressDto userDeliveryAddr = new UserDeliveryAddressDto();
            String deliveryAddressKey = RedisKeyUtils.getLatestDeliveryAddressKey(userId);
            String deliveryAddress = RedisCache.get(deliveryAddressKey); //???redis??????????????????????????????????????????
            UserDeliveryAddressDto deliveryAddressDto = JSONObject.parseObject(deliveryAddress, UserDeliveryAddressDto.class);
            log.info("??????????????????????????????{}", JSON.toJSONString(deliveryAddressDto));
            if (deliveryAddressDto != null) {
                userDeliveryAddr.setId(deliveryAddressDto.getId());
                userDeliveryAddr.setDeliveryCompany(companyName);
                addressService.updateDeliveryAddr(userDeliveryAddr);

                //??????redis??????????????????
                deliveryAddressDto.setDeliveryCompany(companyName);
                RedisCache.setEx(deliveryAddressKey, JSON.toJSONString(deliveryAddressDto), 90, TimeUnit.DAYS);

            }

/*    		//??????session??????????????????
    		userSession.setDeliveryCompany(companyName);
    		UserContext.freshUser(userSession);
 */
        } else {
            RedisCache.incrBy(userCountKey, 1); //???????????????????????????
        }
        return responseResult;
    }

    /**
     * ??????????????????????????????
     *
     * @param companyId
     * @param deliveryFloor
     * @return
     */
    public ResponseResult<List<String>> getDeliveryCompany(Long companyId, String deliveryFloor) {
        ResponseResult<List<String>> responseList = ResponseResult.buildSuccessResponse(new ArrayList<>());
        String companySearchKey = RedisKeyUtils.getCompanySearchKey(companyId, deliveryFloor);
        Set<String> keys = RedisCache.keys(companySearchKey);
        List<UserCompanySortDto> companyList = Lists.newArrayList();
        for (String key : keys) {
            UserCompanySortDto sortDto = new UserCompanySortDto();
            String companyResult = RedisCache.get(key);
            JSONObject jsonObj = JSONObject.parseObject(companyResult);
            String isCertification = jsonObj.get("isCertification").toString();
            String abbreviation = jsonObj.get("abbreviation").toString();
            if (isCertification.equals("true")) {
                sortDto.setCompanyName(abbreviation);
                companyList.add(sortDto);
            }
        }
        List<String> resultList = companyList.stream().map(item -> item.getCompanyName()).collect(Collectors.toList());
        responseList.setData(resultList);
        return responseList;

    }

    /**
     * ???????????????????????????
     *
     * @param address
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Boolean> addOrUpdateAddr(@RequestBody UserDeliveryAddressDto address) {
        if (address == null || address.getRealName().trim().isEmpty() || address.getMobileNo().trim().isEmpty()
                || address.getDeliveryFloor().trim().isEmpty() || address.getDeliveryCompany().trim().isEmpty()) {
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(), REQUEST_PARAM_EMPTY.message());
        }
        UserSession userSession = UserContext.loadCurrentUser();
        Long userId = userSession.getId();
    	String lockKey = RedisKeyUtils.getDeliveryAddressKeyLockKey(userId, address.getDeliveryOfficeBuildingId());
    	Lock lock = LockProvider.getLock(lockKey);
    	lock.lock();
        try {
            //??????
            if (address.getId() != null) {
                //?????????????????????
                addressService.updateDeliveryAddr(address);
                //??????redis????????????????????????
                String deliveryAddressKey = RedisKeyUtils.getLatestDeliveryAddressKey(userSession.getId());
                String deliveryAddress = RedisCache.get(deliveryAddressKey); //???redis??????????????????????????????????????????
                UserDeliveryAddressDto deliveryAddressDto = JSONObject.parseObject(deliveryAddress, UserDeliveryAddressDto.class);
                if (deliveryAddressDto != null) {
                    deliveryAddressDto.setId(address.getId());
                    deliveryAddressDto.setDeliveryOfficeBuildingId(address.getDeliveryOfficeBuildingId());
                    deliveryAddressDto.setRealName(address.getRealName());
                    deliveryAddressDto.setSex(address.getSex());
                    deliveryAddressDto.setMobileNo(address.getMobileNo());
                    deliveryAddressDto.setDeliveryFloor(address.getDeliveryFloor());
                    deliveryAddressDto.setDeliveryAddress(address.getDeliveryAddress());
                    deliveryAddressDto.setDeliveryCompany(address.getDeliveryCompany());
                    deliveryAddressDto.setNotifyType(address.getNotifyType());
                    deliveryAddressDto.setTakeMealsAddrId(address.getTakeMealsAddrId());
                    deliveryAddressDto.setTakeMealsAddrName(address.getTakeMealsAddrName());
                    deliveryAddressDto.setDeliveryTimeId(address.getDeliveryTimeId());
                    deliveryAddressDto.setDeliveryTimeBegin(address.getDeliveryTimeBegin());
                    deliveryAddressDto.setDeliveryTimeEnd(address.getDeliveryTimeEnd());
                    RedisCache.setEx(deliveryAddressKey, JSON.toJSONString(deliveryAddressDto), 90, TimeUnit.DAYS);
                }

            } else {
                address.setUserId(userId);
                addressService.saveDeliveryAddr(address);
            }
        } catch (Exception e) {
            log.error("??????????????????", e);
        }finally {
        	lock.unlock();
        }
        return ResponseResult.buildSuccessResponse(true);
    }

    /**
     * ????????????id??????????????????
     *
     * @param addressId
     * @return
     */
    public ResponseResult<UserDeliveryAddressDto> queryDeliveryById(Long addressId) {
        return addressService.queryDeliveryById(addressId);
    }

    /**
     * ??????????????????????????????????????????????????????
     *
     * @param mobileNo
     * @param deliveryOfficeBuildingId
     * @param deliveryFloor
     * @param deliveryCompany
     * @return
     */
    public ResponseResult<UserDeliveryAddressDto> queryDeliveryAddrByManyWhere(String mobileNo, Long deliveryOfficeBuildingId,
                                                                               String deliveryFloor, String deliveryCompany) {
        return addressService.queryDeliveryAddrByManyWhere(mobileNo, deliveryOfficeBuildingId, deliveryFloor, deliveryCompany);
    }

    public ResponseResult<UserDto> updateUser(UserDto userDto) {
        userDto.setBusinessLine(OrderType.BREAKFAST.getValue());
        return userService.updateUser(userDto);
    }

    public ResponseResult<Pagenation<UserDto>> fetchUser(Long userId, Integer fetchSize) {
        UserDto userDto = new UserDto();
        userDto.setId(userId);
        userDto.setFetchSize(fetchSize);
        userDto.setBusinessLine(OrderType.BREAKFAST.getValue());
        return userService.fetchUser(userDto);
    }

    public ResponseResult<AddMiniAppTips> addMiniAppTips() {
        UserSession userSession = UserContext.loadCurrentUser();
        UserDto userDto = new UserDto();
        userDto.setId(userSession.getId());
        userDto.setBusinessLine(OrderType.BREAKFAST.getValue());
        ShopConfigDto.AddMiniAppTips addMiniAppTips = shopConfigService.getAddMiniAppTips();
        log.info("get breakfast addMiniAppTips data from config service -> {}",JSONObject.toJSONString(addMiniAppTips));
        AddMiniAppTips tips = BeanMapper.map(addMiniAppTips,AddMiniAppTips.class);
        userDto.setAddMiniAppTips(tips);
        return userService.addMiniAppTips(userDto);
    }

    public ResponseResult<String> queryAccessToken() {
        return miniAppService.queryAccessToken(BusinessLine.BREAKFAST.getCode());
    }

    public ResponseResult<String> shareFriendBanner() {
        String bannerUrl = shopConfigService.getShareFriendBanner();
        return ResponseResult.buildSuccessResponse(bannerUrl);
    }

}