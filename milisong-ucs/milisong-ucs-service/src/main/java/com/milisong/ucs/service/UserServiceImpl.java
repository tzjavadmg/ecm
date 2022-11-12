package com.milisong.ucs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.google.common.base.Splitter;
import com.milisong.ecm.miniapp.api.MiniAppService;
import com.milisong.ecm.miniapp.dto.MiniLoginRequest;
import com.milisong.ecm.miniapp.dto.MiniSessionDto;
import com.milisong.ucs.annotation.LoginLog;
import com.milisong.ucs.annotation.ReceiveNewRedPacketAfterLogin;
import com.milisong.ucs.api.UserService;
import com.milisong.ucs.domain.User;
import com.milisong.ucs.dto.*;
import com.milisong.ucs.enums.UserSourceEnum;
import com.milisong.ucs.mapper.UserMapper;
import com.milisong.ucs.sdk.security.UserContext;
import com.milisong.ucs.sdk.security.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.milisong.ucs.enums.UserResponseCode.*;
import static com.milisong.ucs.enums.UserSourceEnum.MINI_APP;

/**
 * 用户接口
 * @author sailor wang
 * @date 2018/8/31 下午5:40
 * @description
 */
@Slf4j
@RestController
public class UserServiceImpl implements UserService {

    @Autowired
    private MiniAppService miniAppService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户登录
     * @param userRequest
     * @return
     */
    @LoginLog
    @ReceiveNewRedPacketAfterLogin
    @Override
    public ResponseResult<UserDto> login(@RequestBody UserRequest userRequest) {
        if (userRequest == null || StringUtils.isEmpty(userRequest.getJsCode()) || userRequest.getBusinessLine() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        try {
            MiniLoginRequest loginRequest = MiniLoginRequest.builder().jsCode(userRequest.getJsCode()).build();
            loginRequest.setBusinessLine(userRequest.getBusinessLine());
            ResponseResult<MiniSessionDto> loginResult = miniAppService.login(loginRequest);
            log.info("loginResult -> {}",JSON.toJSONString(loginResult));
            if (loginResult.isSuccess() && loginResult.getData() != null){
                String openid = loginResult.getData().getOpenid();
                // 会话密钥(用于解密手机号)
                String wxSessionKey = loginResult.getData().getSessionKey();
                Integer registerSource = MINI_APP.getCode();
                log.info("12登录日志打印 openid -> {}, registerSource -> {}, buzline -> {}",openid,registerSource,userRequest.getBusinessLine());
                User user = userMapper.selectByOpenid(openid,registerSource,userRequest.getBusinessLine());
                log.info("user -> {}",JSON.toJSONString(user));
                if (user == null){
                    //新用户，生产用户数据
                    user = saveUser(openid,registerSource,userRequest);
                }
                user.setBusinessLine(userRequest.getBusinessLine());
                //分布式session
                UserSession userSession = BeanMapper.map(user,UserSession.class);
                userSession.setWxSessionKey(wxSessionKey);
                String sessionKey = UserContext.loginSession(userSession);
                String traceId = user.getOpenId()+"_"+ DateTime.now().getMillis();
                UserDto userDto = UserDto.builder().id(user.getId()).openId(user.getOpenId()).nickName(user.getNickName())
                        .headPortraitUrl(user.getHeadPortraitUrl()).sessionKey(sessionKey).wechatContractNo(user.getWechatContractNo())
                        .receivedNewRedPacket(user.getReceivedNewRedPacket()).registerSource(registerSource)
                        .receivedNewCoupon(user.getReceivedNewCoupon()).mobileNo(user.getMobileNo()).isNew(userSession.getIsNew())
                        .traceId(traceId).build();
                userDto.setBusinessLine(userRequest.getBusinessLine());
                return ResponseResult.buildSuccessResponse(userDto);
            }
        } catch (Exception e) {
            log.error("用户登录异常",e);
        }
        return ResponseResult.buildFailResponse(USER_LOGIN_EXCEPTION.code(), USER_LOGIN_EXCEPTION.message());
    }

    /**
     * 根据openid更新用户信息
     * @param userDto
     * @return
     */
    @Override
    public ResponseResult<Boolean> updateByOpenid(@RequestBody UserDto userDto) {
        if (userDto == null || StringUtils.isEmpty(userDto.getOpenId()) || userDto.getBusinessLine() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        int row = 0;
        userDto.setRegisterSource(UserSourceEnum.MINI_APP.getCode());
        Byte buzLine = userDto.getBusinessLine();
        try {
            if (StringUtils.isNotBlank(userDto.getWechatContractNo())){
                //UserContext更新签约号
                UserContext.freshUser(userDto.getOpenId(),MINI_APP.getCode(),buzLine,"wechatContractNo",userDto.getWechatContractNo());
            }else if (userDto.getIsNew() != null){
                UserContext.freshUser(userDto.getOpenId(),MINI_APP.getCode(),buzLine,"isNew",userDto.getIsNew().toString());
            }
            row = userMapper.updateByOpenid(userDto);
        } catch (Exception e) {
            log.error("",e);
        }
        return ResponseResult.buildSuccessResponse(row>0);
    }

    /**
     * 根据userid查询用户数据
     * @param userDto
     * @return
     */
    @Override
    public ResponseResult<UserDto> getUserInfo(@RequestBody UserDto userDto) {
        try {
            if (userDto.getId() != null){
                User user = userMapper.selectByPrimaryKey(userDto.getId());
                if (user != null){
                    userDto = BeanMapper.map(user,UserDto.class);
                    return ResponseResult.buildSuccessResponse(userDto);
                }
            }else if (StringUtils.isNotBlank(userDto.getSessionKey())){
                Map<Object, Object> map = UserContext.getUserBySession(userDto.getSessionKey());
                log.info("getUserInfo sessionKey -> {}, map -> {}",userDto.getSessionKey(),JSONObject.toJSONString(map));
                if (map != null){
                    UserDto user = BeanMapper.map(map, UserDto.class);
                    return ResponseResult.buildSuccessResponse(user);
                }
            }

        } catch (Exception e) {
            log.error("",e);
        }
        return ResponseResult.buildFailResponse(QUERY_USER_INFO_EXCEPTION.code(),QUERY_USER_INFO_EXCEPTION.message());
    }

    /**
     * 更新用户信息
     * @param userDto
     * @return
     */
    @Override
    public ResponseResult<UserDto> updateUser(@RequestBody UserDto userDto) {
        if (userDto == null || userDto.getId() == null || userDto.getOpenId() == null || userDto.getBusinessLine() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        log.info("更新用户信息 userDto -> {}",userDto);
        User record = BeanMapper.map(userDto,User.class);
        String wxSessionKey = userDto.getWxSessionKey();// 会话密钥
        String encryptedData = userDto.getEncryptedData();// 包括敏感数据在内的完整用户信息的加密数据
        String iv = userDto.getIv();// 加密算法的初始向量
        Byte buzLine = userDto.getBusinessLine();

        if (StringUtils.isNotBlank(wxSessionKey) && StringUtils.isNotBlank(encryptedData) && StringUtils.isNotBlank(iv)) {

            //获取手机号
            ResponseResult<String> mobileResult = miniAppService.getMobile(wxSessionKey,encryptedData,iv);
            log.info("获取手机号--- mobileResult -》 {}", JSONObject.toJSONString(mobileResult));
            if (mobileResult.isSuccess() && StringUtils.isNotBlank(mobileResult.getData())){
                String mobile = mobileResult.getData();
                record.setMobileNo(mobile);
                record.setUpdateTime(new Date());

                // 更新手机号
                UserContext.freshUser(userDto.getOpenId(),MINI_APP.getCode(),buzLine,"mobileNo",mobile);
            }
        }else if (StringUtils.isNotBlank(record.getRealName()) && record.getSex() != null){
            // 真实姓名和性别
            UserContext.freshUser(userDto.getOpenId(),MINI_APP.getCode(),buzLine,"realName",record.getRealName());
            UserContext.freshUser(userDto.getOpenId(),MINI_APP.getCode(),buzLine,"sex",record.getSex().toString());
        }else if (StringUtils.isNotBlank(record.getNickName()) || record.getHeadPortraitUrl() != null){
            // 昵称和头像
            UserContext.freshUser(userDto.getOpenId(),MINI_APP.getCode(),buzLine,"nickName",record.getNickName());
            UserContext.freshUser(userDto.getOpenId(),MINI_APP.getCode(),buzLine,"headPortraitUrl",record.getHeadPortraitUrl());
        }else if (record.getReceivedNewRedPacket() != null){
            UserContext.freshUser(userDto.getOpenId(),MINI_APP.getCode(),buzLine,"receivedNewRedPacket",record.getReceivedNewRedPacket().toString());
        }else if (record.getReceivedNewCoupon() != null){
            UserContext.freshUser(userDto.getOpenId(),MINI_APP.getCode(),buzLine,"receivedNewCoupon",record.getReceivedNewCoupon().toString());
        }
        record.setOpenId(null);
        userMapper.updateByPrimaryKeySelective(record);
        UserDto resultDto = new UserDto();
        String mobileNo = record.getMobileNo();
        if(mobileNo == null ){
            UserSession currentUser = UserContext.getCurrentUser();
            if(currentUser!=null){
                mobileNo = currentUser.getMobileNo();
            }
        }
        resultDto.setMobileNo(mobileNo);
        return ResponseResult.buildSuccessResponse(resultDto);
    }

    @Override
    public ResponseResult<Pagenation<UserDto>> fetchUser(@RequestBody UserDto userDto) {
        if (userDto.getBusinessLine() == null){
            return ResponseResult.buildFailResponse(BUSINESS_LINE_IS_EMPTY.code(),BUSINESS_LINE_IS_EMPTY.message());
        }
        if (userDto.getFetchSize() == 0 || userDto.getFetchSize() < 0 || userDto.getFetchSize() > 500){
            userDto.setFetchSize(100);
        }

        Long total = 0L;
        Pagenation pagenation = new Pagenation();
        pagenation.setPageCount(0);
        if (userDto.getId() == null){
            total = userMapper.countWithMobile(userDto.getBusinessLine());
            pagenation.setPageCount((int) Math.ceil((double) total / userDto.getFetchSize()));
        }
        List<UserDto> userList = userMapper.fetchUser(userDto.getId(),userDto.getFetchSize(),userDto.getBusinessLine());
        pagenation.setList(userList);
        pagenation.setRowCount(total);

        return ResponseResult.buildSuccessResponse(pagenation);
    }

    @Override
    public ResponseResult<Pagenation<UserDto>> queryUser(@RequestBody SendLunchRedPacketRequest redPacketRequest) {
        if (redPacketRequest.getBusinessLine() == null){
            return ResponseResult.buildFailResponse(BUSINESS_LINE_IS_EMPTY.code(),BUSINESS_LINE_IS_EMPTY.message());
        }
        if (redPacketRequest.getPageNo() < 0) {
            redPacketRequest.setPageNo(1);
        }
        if (redPacketRequest.getPageSize() < 0) {
            redPacketRequest.setPageSize(10);
        }
        if (redPacketRequest.getPageSize() > 500) {
            redPacketRequest.setPageSize(500);
        }
        Pagenation<UserDto> pagenation = new Pagenation();
        pagenation.setRowCount(0);
        Long count = userMapper.countWithMobile(redPacketRequest.getBusinessLine());
        if (count > 0){
            List<UserDto> userDtos = userMapper.queryUserWithMobile(redPacketRequest);
            pagenation.setRowCount(count);
            pagenation.setList(userDtos);
        }
        return ResponseResult.buildSuccessResponse(pagenation);
    }

    @Override
    @PostMapping(value = "/v1/UserService/fetchUserInfoByIds")
    public ResponseResult<List<UserDto>> fetchUserInfoByIds(@RequestBody UserDto userDto) {
        if (CollectionUtils.isEmpty(userDto.getIds())){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        List<UserDto> list = userMapper.fetchUserInfoByIds(userDto.getIds());
        log.info("根据ids批量获取用户信息 list size -> {}",list==null?0:list.size());
        return ResponseResult.buildSuccessResponse(list);
    }

    @Override
    @PostMapping(value = "/v1/UserService/fetchUserInfoByMobiles")
    public ResponseResult<List<UserDto>> fetchUserInfoByMobiles(@RequestBody UserDto userDto) {
        if (CollectionUtils.isEmpty(userDto.getMobileNos()) || userDto.getBusinessLine() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        List<UserDto> list = userMapper.fetchUserInfoByMobileNos(userDto.getMobileNos(),userDto.getBusinessLine());
        log.info("根据手机号批量获取用户信息 list size -> {}",list==null?0:list.size());
        return ResponseResult.buildSuccessResponse(list);
    }

    /**
     * 计算积分
     *
     * @param userDto
     * @return
     */
    @Override
    public ResponseResult<Boolean> calcPoint(@RequestBody UserDto userDto) {
        if (userDto.getId() == null || userDto.getUserPointAction() == null || userDto.getUsefulPoint() == null || StringUtils.isBlank(userDto.getIdempotent())){
            log.info("计算积分入参 userDto -> {}",userDto);
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        int row = 0;
        String lockKey = "user_point_lock:"+userDto.getId();
        RLock rLock = LockProvider.getLock(lockKey);
        String idempotent = userDto.getIdempotent();
        // 幂等
        String idempotentKey = userDto.getId()+"_"+idempotent;
        try {
            rLock.lock();
            Long r = RedisCache.incrBy(idempotentKey,1);
            if (r > 0){
                log.info("用户计算积分 uid -> {} action -> {} point -> {} ",
                        userDto.getId(),userDto.getUserPointAction(),userDto.getUsefulPoint());
                row = userMapper.calcPoint(userDto.getId(),userDto.getUserPointAction(),userDto.getUsefulPoint());
            }else {
                log.error("用户积分计算异常 idempotent -> {}",userDto.getId()+"_"+idempotent);
            }
            if (row > 0){
                return ResponseResult.buildSuccessResponse(true);
            }
        } catch (Exception e) {
            log.error("",e);
        }finally {
            RedisCache.expire(idempotentKey,30, TimeUnit.MINUTES);
            rLock.unlock();
        }
        return ResponseResult.buildFailResponse();
    }

    @Override
    public ResponseResult<AddMiniAppTips> addMiniAppTips(@RequestBody UserDto userDto) {
        if (userDto == null || userDto.getAddMiniAppTips() == null || userDto.getBusinessLine() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        try {
            Long userId = userDto.getId();
            AddMiniAppTips addMiniAppTips = userDto.getAddMiniAppTips();
            log.info("addMiniAppTips -> {}",JSONObject.toJSONString(addMiniAppTips));
            if (addMiniAppTips != null){
                AddMiniAppTips tips = BeanMapper.map(addMiniAppTips,AddMiniAppTips.class);
                Integer showTimes = tips.getShowTimes()==null?1:tips.getShowTimes();
                String tips_key = "add_miniapp_tips_toast:"+userId;
                String timesStr = RedisCache.get(tips_key);
                if (StringUtils.isNotBlank(timesStr)){
                    Integer times = Integer.valueOf(timesStr);
                    if (times < showTimes){
                        // 已弹过，还可以弹
                        RedisCache.set(tips_key,String.valueOf(times+1));
                        return ResponseResult.buildSuccessResponse(tips);
                    }
                }else {
                    // 未弹过，弹
                    RedisCache.set(tips_key,String.valueOf(1));
                    return ResponseResult.buildSuccessResponse(tips);
                }
                return ResponseResult.buildSuccessResponse(ALREADY_EXECUTE.code(),ALREADY_EXECUTE.message());
            }
            return ResponseResult.buildSuccessResponse(REDIS_CONFIG_DATA_NOT_EXISTS.code(),REDIS_CONFIG_DATA_NOT_EXISTS.message());
        } catch (Exception e) {
            log.error("",e);
            return ResponseResult.buildFailResponse(ADD_MINIAPP_TIPS_EXCEPTION.code(),ADD_MINIAPP_TIPS_EXCEPTION.message());
        }
    }

    private User saveUser(String openid,Integer registerSource,UserRequest userRequest){
        User user = new User();
        user.setOpenId(openid);
        user.setRegisterSource(registerSource);
        user.setRegisterShopId(userRequest.getShopId() == null?"":userRequest.getShopId().toString());
        user.setRegisterDate(new Date());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setId(IdGenerator.nextId());
        user.setIsNew((byte)0);
        try {
            String qrcode = userRequest.getRegisterQrcode();
            if (StringUtils.isNotBlank(qrcode)){
                log.info("qrcode -> {}",qrcode);
                if (qrcode.contains("%")){// 防止qrcode编码
                    log.info("after decoder qrcode -> {}",qrcode);
                    qrcode = URLDecoder.decode(qrcode,"UTF-8");
                }
                if (qrcode.contains("&")){
                    Splitter.MapSplitter mapSplitter = Splitter.on("&").withKeyValueSeparator("=");
                    Map<String, String> map = mapSplitter.split(qrcode);
                    user.setRegisterQrcode(map.get("q"));
                }else {
                    user.setRegisterQrcode(qrcode.replace("q=",""));
                }
            }
        } catch (Exception e) {
            log.error("",e);
        }
        user.setUsefulPoint(0);
        user.setUsedPoint(0);
        user.setBusinessLine(userRequest.getBusinessLine());
        userMapper.insertSelective(user);
        return user;
    }
}