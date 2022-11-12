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
 * @date 2018/9/2 下午2:40
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
            //小程序分享图片
            userDto.setPictureUrl(shopConfigService.getSharePicture());
            //小程序分享文案
            userDto.setTitle(shopConfigService.getShareTitle());
        } catch (Exception e) {
            log.error("",e);
        }
    }

    /**
     * 校验签约号
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
        log.info("校验签约号 -> {}", JSON.toJSONString(responseResult));
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
            log.info("签约回调 notityXml ------> {}", notityXml);
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
            log.info("解约回调 notityXml ------> {}", notityXml);
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
     * 新增收货地址
     *
     * @param address
     * @return
     */
    public ResponseResult<Boolean> addDeliveryAddr(UserDeliveryAddressDto address) {
        address.setBusinessLine(OrderType.BREAKFAST.getValue());
        return addressService.saveDeliveryAddr(address);
    }

    /**
     * 获取用户收货地址
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
            String deliveryAddress = RedisCache.get(deliveryAddressKey); //用户进入首页从redis获取最后一次下单收货地址
            UserDeliveryAddressDto deliveryAddressDto = JSONObject.parseObject(deliveryAddress, UserDeliveryAddressDto.class);
            log.info("获取用户最后一次下单地址{}", JSON.toJSONString(deliveryAddressDto));
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
        //根据楼宇跟用户id获取用户地址
        UserDeliveryAddressRequest request = UserDeliveryAddressRequest.builder().userId(userid).deliveryOfficeBuildingId(companyId).build();
        request.setBusinessLine(businessLine);
        log.info("查询用户地址入参{}", JSON.toJSONString(request));
        ResponseResult<UserDeliveryAddressDto> result = addressService.queryDeliveryAddr(request);
        UserDeliveryAddressDto userDeliverAddressDto = result.getData();
        BfUserAddressDto bfUserAddressDto = new BfUserAddressDto();
        if (result.isSuccess() && null != userDeliverAddressDto) {
            BeanMapper.copy(userDeliverAddressDto, bfUserAddressDto);
        }
        if(userDeliverAddressDto == null || StringUtils.isBlank(userDeliverAddressDto.getMobileNo())){
            bfUserAddressDto.setId(null);
            //如果没查到用户的地址信息(如果查询到用户地址信息，且信息中无手机号)，将手机号信息取出，显示
            bfUserAddressDto.setMobileNo(UserContext.getCurrentUser().getMobileNo());
        }
        List<CompanyMealAddressDto> mealsAddressList = getMealsAddress(companyId);
        List<CompanyMealTimeDto> mealsTimeList = getMealsTime(companyId,businessLine);
        bfUserAddressDto.setMealAddressList(mealsAddressList);
        bfUserAddressDto.setDeliveryTimeList(mealsTimeList);
        return ResponseResult.buildSuccessResponse(bfUserAddressDto);
    }
    /**
     * 获取取餐点信息
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
    	log.info("获取公司取餐点信息{}",JSON.toJSONString(mealAddressList));
    	return mealAddressList;
    }

    /**
     * 获取配送时间信息
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
        log.info("获取公司配送时间信息{}",JSON.toJSONString(mealTimeList));
        return mealTimeList;
    }
    /**
     * 签约
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
     * 判断用户公司是否同名(针对老用户)
     *
     * @return
     */
    public ResponseResult<String> isPoint() {
        ResponseResult<String> responseResult = ResponseResult.buildSuccessResponse(new String());
        UserSession userSession = UserContext.loadCurrentUser();
        byte isNew = userSession.getIsNew();
        Long userId = userSession.getId();
        String userCountKey = RedisKeyUtils.getUserCountKey(userId);
        int redisUserCount = RedisCache.get(userCountKey) == null ? 0 : Integer.parseInt(RedisCache.get(userCountKey)); //redis计数
        int hintCount = shopConfigService.getSameCompanyHintCount(); //全局配置次数
        log.info("弹窗提醒redisUserCount={},hintCount={},isNew={}", redisUserCount, hintCount, isNew);
        //如果是老用户 并且未超过弹窗次数限制
        if (isNew == 1 && redisUserCount < hintCount) {
            //获取用户最新的收获地址
            String deliveryAddressKey = RedisKeyUtils.getLatestDeliveryAddressKey(userId);
            String deliveryAddress = RedisCache.get(deliveryAddressKey);//获取用户最后一次下单地址信息
            UserDeliveryAddressDto deliveryAddressDto = JSONObject.parseObject(deliveryAddress, UserDeliveryAddressDto.class);
            log.info("获取用户最后一次下单地址信息{}", JSON.toJSONString(deliveryAddressDto));
            if (deliveryAddressDto != null) {
                Long buildingId = deliveryAddressDto.getDeliveryOfficeBuildingId();
                String floor = deliveryAddressDto.getDeliveryFloor();
                String deliveryCompany = deliveryAddressDto.getDeliveryCompany();
                String companyKey = RedisKeyUtils.getCompanyKey(buildingId, floor, deliveryCompany);
                log.info("判断公司是否同名key{}", companyKey);
                String redisResult = RedisCache.get(companyKey);
                UserCompany company = JSONObject.parseObject(redisResult, UserCompany.class);
                log.info("获取同名公司信息结果{}", JSON.toJSONString(company));
                if (company != null) {
                    //同名
                    if (company.getIsSameName()) {
                        responseResult.setData(company.getSameName() == null ? "" : company.getSameName());
                    }
                }

            }

        }
        log.info("弹窗提醒返回结果:{}", JSON.toJSONString(responseResult));
        return responseResult;
    }

    /**
     * 用户操作弹窗提示是或否
     *
     * @param companyName
     * @return
     */
    public ResponseResult<Boolean> operateYesOrNo(String companyName) {
        ResponseResult<Boolean> responseResult = ResponseResult.buildSuccessResponse(true);
        UserSession userSession = UserContext.loadCurrentUser();
        Long userId = userSession.getId();
        String userCountKey = RedisKeyUtils.getUserCountKey(userId);
        //同名提示修改用户地址表公司名称
        if (StringUtils.isNotEmpty(companyName)) {
            UserDeliveryAddressDto userDeliveryAddr = new UserDeliveryAddressDto();
            String deliveryAddressKey = RedisKeyUtils.getLatestDeliveryAddressKey(userId);
            String deliveryAddress = RedisCache.get(deliveryAddressKey); //从redis获取用户最后一次下单收货地址
            UserDeliveryAddressDto deliveryAddressDto = JSONObject.parseObject(deliveryAddress, UserDeliveryAddressDto.class);
            log.info("用户操作修改公司名称{}", JSON.toJSONString(deliveryAddressDto));
            if (deliveryAddressDto != null) {
                userDeliveryAddr.setId(deliveryAddressDto.getId());
                userDeliveryAddr.setDeliveryCompany(companyName);
                addressService.updateDeliveryAddr(userDeliveryAddr);

                //更新redis最近使用地址
                deliveryAddressDto.setDeliveryCompany(companyName);
                RedisCache.setEx(deliveryAddressKey, JSON.toJSONString(deliveryAddressDto), 90, TimeUnit.DAYS);

            }

/*    		//重写session用户公司信息
    		userSession.setDeliveryCompany(companyName);
    		UserContext.freshUser(userSession);
 */
        } else {
            RedisCache.incrBy(userCountKey, 1); //用户点击否进行计数
        }
        return responseResult;
    }

    /**
     * 根据楼宇楼层获取公司
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
     * 新增或修改收货地址
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
            //修改
            if (address.getId() != null) {
                //更新用户地址表
                addressService.updateDeliveryAddr(address);
                //更新redis用户最近使用地址
                String deliveryAddressKey = RedisKeyUtils.getLatestDeliveryAddressKey(userSession.getId());
                String deliveryAddress = RedisCache.get(deliveryAddressKey); //从redis获取用户最后一次下单收货地址
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
            log.error("编辑地址异常", e);
        }finally {
        	lock.unlock();
        }
        return ResponseResult.buildSuccessResponse(true);
    }

    /**
     * 根据地址id获取用户地址
     *
     * @param addressId
     * @return
     */
    public ResponseResult<UserDeliveryAddressDto> queryDeliveryById(Long addressId) {
        return addressService.queryDeliveryById(addressId);
    }

    /**
     * 根据手机号，楼宇楼层公司获取地址信息
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