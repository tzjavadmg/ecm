package com.milisong.upms.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.cache.RedisCache;
import com.milisong.upms.api.SsoService;
import com.milisong.upms.common.SsoApplicationConfig;
import com.milisong.upms.common.SsoUrlsConfig;
import com.milisong.upms.constant.SsoErrorConstant;
import com.milisong.upms.dto.MenuDto;
import com.milisong.upms.dto.UserDataPermissionDto;
import com.milisong.upms.dto.UserInfoDto;
import com.milisong.upms.utils.RestClient;
import com.milisong.upms.utils.SignatureUtil;
import com.milisong.upms.utils.SsoRedisKeyUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/auth")
public class SsoServiceImpl implements SsoService {
	@Autowired
	private SsoApplicationConfig ssoApplicationConfig;
	@Autowired
	private SsoUrlsConfig ssoUrlsConfig;
	// GSID相关的缓存时长
	private static final int GSID_CACHE_DAY = 2;
	
	@Override
	@GetMapping("/token")
	public ResponseResult<String> getToken(@RequestParam("ticket")String ticket, @RequestParam("appName")String appName) {
		String gsid = getGsid(ticket, appName);

		if (StringUtils.isNotBlank(gsid)) {
			// 生成本地token
			String token = UUID.randomUUID().toString();

			// 设置sso的gsid与本地token的关系
			RedisCache.setEx(SsoRedisKeyUtil.getToken2GsidKey(token), gsid, GSID_CACHE_DAY, TimeUnit.DAYS);
			RedisCache.setEx(SsoRedisKeyUtil.getToken2AppNameKey(token), appName, GSID_CACHE_DAY, TimeUnit.DAYS);
			
			// 在SSO退出登录的时候清除token信息 
			// TODO 暂时注释，后面接了SSO的MQ再设置
			//RedisCache.sAdd(SsoRedisKeyUtil.getGsid2TokenKey(gsid), token);
			//RedisCache.expire(SsoRedisKeyUtil.getGsid2TokenKey(gsid), GSID_CACHE_DAY, TimeUnit.DAYS);
			
			// 获取用户信息
			UserInfoDto userInfo = getSsoUserInfoByGsId(gsid);
			if (null == userInfo) {
				log.warn("ticket:{}未获取到用户信息", ticket);
				return ResponseResult.buildFailResponse(SsoErrorConstant.SYSTEM.NOT_LOGIN.getCode(), SsoErrorConstant.SYSTEM.NOT_LOGIN.getDesc());
			}
			
			// 缓存数据
			initUserCache(token, userInfo);
			
			return ResponseResult.buildSuccessResponse(token);
		} else {
			log.warn("ticket:{}未获取到GSID信息", ticket);
			return ResponseResult.buildFailResponse(SsoErrorConstant.SYSTEM.NOT_LOGIN.getCode(), SsoErrorConstant.SYSTEM.NOT_LOGIN.getDesc());
		}
	}
	
	@Override
	@GetMapping("/userinfo")
	public ResponseResult<UserInfoDto> getUserInfo(@RequestParam("token") String token) {
		UserInfoDto userInfo = null;
		String value = (String)RedisCache.hGet(SsoRedisKeyUtil.getSsoHashKey(token), SsoRedisKeyUtil.USER_INFO);
		if(StringUtils.isBlank(value)) {
			String gsid = getGsidByToken(token);
			if(StringUtils.isBlank(gsid)) {
				log.warn("token:{}未获取到GSID信息", token);
				return ResponseResult.buildFailResponse(SsoErrorConstant.SYSTEM.NOT_LOGIN.getCode(), SsoErrorConstant.SYSTEM.NOT_LOGIN.getDesc());
			}
			// 获取用户信息
			userInfo = getSsoUserInfoByGsId(gsid);
			if (null == userInfo) {
				log.warn("token:{}未获取到用户信息", token);
				return ResponseResult.buildFailResponse(SsoErrorConstant.SYSTEM.NOT_LOGIN.getCode(), SsoErrorConstant.SYSTEM.NOT_LOGIN.getDesc());
			}
			// 缓存数据
			initUserCache(token, userInfo);
		}  else {
			userInfo = JSONObject.parseObject(value, UserInfoDto.class);
		}
		return ResponseResult.buildSuccessResponse(userInfo);
	}
	
	@Override
	@GetMapping("/menu")
	public ResponseResult<List<MenuDto>> getUserMenu(@RequestParam("token") String token) {
		List<MenuDto> list = null;
		String value = (String)RedisCache.hGet(SsoRedisKeyUtil.getSsoHashKey(token), SsoRedisKeyUtil.MENU);
		if(StringUtils.isBlank(value)) {
			ResponseResult<UserInfoDto> userInfo = getUserInfo(token);
			if(userInfo.isSuccess()) {
				list = this.getSsoMenuTree(userInfo.getData().getUserNo(), RedisCache.get(SsoRedisKeyUtil.getToken2AppNameKey(token)));
				if(!CollectionUtils.isEmpty(list)) {
					initMenuCache(token, list);
				} else {
					// 本地缓存sso的信息
					RedisCache.hPut(SsoRedisKeyUtil.getSsoHashKey(token), SsoRedisKeyUtil.MENU, SsoRedisKeyUtil.EMPTY);
					this.setTtl(token);
				}
			} else {
				log.warn("token:{}获取菜单信息失败,结果：{}", token, JSONObject.toJSONString(userInfo));
				return ResponseResult.buildFailResponse(userInfo.getCode(), userInfo.getMessage());
			}
		} else {
			if(!SsoRedisKeyUtil.EMPTY.equals(value)) {
				list = JSONObject.parseArray(value, MenuDto.class);
			}
		}
		if(null == list) {
			list = Collections.emptyList();
		}
		return ResponseResult.buildSuccessResponse(list);
	}
	
	@Override
	@GetMapping("/check-login")
	public ResponseResult<Boolean> checkLogin(@RequestParam("token") String token) {
		ResponseResult<UserInfoDto> userInfo = this.getUserInfo(token);
		if(userInfo.isSuccess() && userInfo.getData()!=null) {
			return ResponseResult.buildSuccessResponse(Boolean.TRUE);
		}
		log.warn("token:{}校验登陆失败", token);
		return ResponseResult.buildSuccessResponse(Boolean.FALSE);
	}
		
	@Override
	@GetMapping("/check-permission")
	public ResponseResult<Boolean> checkUserPermission(@RequestParam("token") String token, @RequestParam("url")String url) {
		List<String> list = getUserPermissionList(token, RedisCache.get(SsoRedisKeyUtil.getToken2AppNameKey(token)));
		if(CollectionUtils.isNotEmpty(list) && list.contains(url)) {
			return ResponseResult.buildSuccessResponse(Boolean.TRUE);
		}
		log.warn("token:{}校验url：{}无权限", token, url);
		return ResponseResult.buildSuccessResponse(Boolean.FALSE);
	}
	
	@Override
	@GetMapping("/data-permission")
	public ResponseResult<List<UserDataPermissionDto>> getShopByPermission(@RequestParam("token") String token, @RequestParam("dataType") String dataType) {
		List<UserDataPermissionDto> list = null;
		String value = (String)RedisCache.hGet(SsoRedisKeyUtil.getSsoHashKey(token), SsoRedisKeyUtil.DATA_PERMISSION);
		if(StringUtils.isBlank(value)) {
			ResponseResult<UserInfoDto> userInfo = getUserInfo(token);
			if(userInfo.isSuccess()) {
				list = this.getSsoUserDataPermission(userInfo.getData().getUserNo(), dataType);
				if(!CollectionUtils.isEmpty(list)) {
					initDatPermissionList(token, list);
				} else {
					// 本地缓存sso的信息
					RedisCache.hPut(SsoRedisKeyUtil.getSsoHashKey(token), SsoRedisKeyUtil.DATA_PERMISSION, SsoRedisKeyUtil.EMPTY);
					// 设置TTL
					this.setTtl(token);
				}
			} else {
				log.warn("token:{}获取数据权限信息失败,结果：{}", token, JSONObject.toJSONString(userInfo));
				return ResponseResult.buildFailResponse(userInfo.getCode(), userInfo.getMessage());
			}
		} else {
			if(!SsoRedisKeyUtil.EMPTY.equals(value)) {
				list = JSONObject.parseArray(value, UserDataPermissionDto.class);
			}
		}
		if(null == list) {
			list = Collections.emptyList();
		}
		return ResponseResult.buildSuccessResponse(list);
	}
	
	/**
	 * 从sso通过ticket换取gsid
	 *
	 * @param ticket
	 * @return
	 */
	private String getGsid(String ticket, String appName) {
		Map<String, String> param = new HashMap<>();
		param.put("ticket", ticket);
		param.put("appKey", ssoApplicationConfig.getApp().get(appName).getAppKey());
		param.put("appName", appName);
		param.put("sign", SignatureUtil.getSign(ticket, ssoApplicationConfig.getApp().get(appName).getAppSercet()));

		String result = RestClient.postJson(ssoUrlsConfig.getValidateTicket(), param);
		log.info("根据ticket：{},appName：{}获取到的gsid的结果为：{}", ticket, appName ,result);

		if (StringUtils.isNotBlank(result)) {
			JSONObject json = JSONObject.parseObject(result);
			if (json.getBooleanValue("success")) {
				return json.getString("data");
			}
			return null;
		}
		return null;
	}
	
	/**
	 * 从sso获取用户信息 结果例子：HttpResponse(success=true,
	 * data={"success":true,"data":{"userNo":1,"accountName":"zhufen","userName":"超级管理员","mobile":"15168353412","email":"zhufen@mili.shop","gender":1,"joinTime":"2017-11-14
	 * 18:51:20","createTime":"2017-11-14 18:51:20","updateTime":"2017-11-20
	 * 14:37:17","status":1,"isLeader":false,"isDelete":false}})
	 *
	 * @param gsid
	 * @return
	 */
	private UserInfoDto getSsoUserInfoByGsId(String gsid) {
		String result = RestClient.get(ssoUrlsConfig.getLoginUserInfo().concat("?gsid=").concat(gsid));
		//log.info("根据gsid：{}获取用户信息结果：{}", gsid, result);
		return getSsoUserInfo(result);
	}
	
	/**
	 * 根据token获取gisd
	 * @param token
	 * @return
	 */
	private String getGsidByToken(String token) {
		return RedisCache.get(SsoRedisKeyUtil.getToken2GsidKey(token));
	}
	
	/**
	 * 初始化用户缓存
	 * @param token
	 * @param userInfo
	 */
	private void initUserCache(String token, UserInfoDto userInfo) {
		// 本地缓存sso的信息
		RedisCache.hPut(SsoRedisKeyUtil.getSsoHashKey(token), SsoRedisKeyUtil.USER_INFO, JSONObject.toJSONString(userInfo));
		// 设置TTL
		this.setTtl(token);
	}
	
	/**
	 * 初始化用户菜单树
	 * @param token
	 * @param list
	 */
	private void initMenuCache(String token, List<MenuDto> list) {
		// 本地缓存sso的信息
		RedisCache.hPut(SsoRedisKeyUtil.getSsoHashKey(token), SsoRedisKeyUtil.MENU, JSONObject.toJSONString(list));
		// 设置TTL
		this.setTtl(token);
	}
	
	/**
	 * 初始化权限缓存
	 * @param token
	 * @param list
	 */
	private void initPermissionList(String token, List<String> list) {
		// 本地缓存sso的信息
		RedisCache.hPut(SsoRedisKeyUtil.getSsoHashKey(token), SsoRedisKeyUtil.PERMISSION, JSONObject.toJSONString(list));
		// 设置TTL
		this.setTtl(token);
	}
	
	/**
	 * 初始化数据权限缓存
	 * @param token
	 * @param list
	 */
	private void initDatPermissionList(String token, List<UserDataPermissionDto> list) {
		// 本地缓存sso的信息
		RedisCache.hPut(SsoRedisKeyUtil.getSsoHashKey(token), SsoRedisKeyUtil.DATA_PERMISSION, JSONObject.toJSONString(list));
		// 设置TTL
		this.setTtl(token);
	}
	
	/**
	 * 设置过期时间
	 * @param token
	 */
	private void setTtl(String token) {
		// 设置过期时间
		RedisCache.expire(SsoRedisKeyUtil.getSsoHashKey(token), ssoApplicationConfig.getCacheTimeOutWithMinute(), TimeUnit.MINUTES);
	}
	
	/**
	 * 获取用户的权限list
	 * @param token
	 * @return
	 */
	private List<String> getUserPermissionList(String token, String appName) {
		List<String> list = null;
		String value = (String)RedisCache.hGet(SsoRedisKeyUtil.getSsoHashKey(token), SsoRedisKeyUtil.PERMISSION);
		if(StringUtils.isBlank(value)) {
			ResponseResult<UserInfoDto> userInfo = getUserInfo(token);
			if(userInfo.isSuccess()) {
				list = this.getSsoPermissionList(userInfo.getData().getUserNo(), appName);
				if(!CollectionUtils.isEmpty(list)) {
					initPermissionList(token, list);
				} else {
					// 本地缓存sso的信息
					RedisCache.hPut(SsoRedisKeyUtil.getSsoHashKey(token), SsoRedisKeyUtil.PERMISSION, SsoRedisKeyUtil.EMPTY);
					this.setTtl(token);
				}
			}
		} else {
			if(!SsoRedisKeyUtil.EMPTY.equals(value)) {
				list = JSONObject.parseArray(value, String.class);
			}
		}
		return list;
	}
	
	/**
	 * 解析BOSS的user到内部的user
	 * @param result
	 * @return
	 */
	private UserInfoDto getSsoUserInfo(String result){
		if (StringUtils.isNotBlank(result)) {
			JSONObject json = JSONObject.parseObject(result);
			if (json.getBooleanValue("success")) {
				return json.getObject("data", UserInfoDto.class);
			} else {
				log.warn("调用sso服务获取用户信息失败：" + result);
			}
		}
		return null;
	}
	
	/**
	 * 从sso获取用户菜单tree
	 * @param userNo
	 * @return
	 */
	private List<MenuDto> getSsoMenuTree(Integer userNo, String appName) {
		String result = RestClient.get(ssoUrlsConfig.getMenuTree().concat("?")
				.concat("userNo=").concat(String.valueOf(userNo)).concat("&appName=").concat(appName));
		if (StringUtils.isNotBlank(result)) {
			//log.info("根据userNo:{}从sso获取到菜单tree的结果为：{}", userNo, result);
			JSONObject json = JSONObject.parseObject(result);
			if (json.getBooleanValue("success")) {
				JSONArray array = json.getJSONArray("data");
				if (null != array && !array.isEmpty()) {
					return JSONObject.parseArray(array.toJSONString(), MenuDto.class);
				}
			} else {
				log.warn("调用sso服务获取菜单树失败：" + result);
			}
		}
		return new ArrayList<>();
	}
	
	/**
	 * 获取有权限的菜单的url的list
	 * 
	 * @param userNo
	 * @return
	 */
	private List<String> getSsoPermissionList(Integer userNo, String appName) {
		String result = RestClient.get(ssoUrlsConfig.getUserPermission().concat("?")
				.concat("userNo=").concat(String.valueOf(userNo)).concat("&appName=").concat(appName));
		if (StringUtils.isNotBlank(result)) {
			//log.info("根据userNo:{}从sso获取到功能权限的结果为：{}", userNo, result);
			JSONObject json = JSONObject.parseObject(result);
			if (json.getBooleanValue("success")) {
				JSONArray array = json.getJSONArray("data");
				if (null != array && !array.isEmpty()) {
					List<String> list = new ArrayList<>(array.size());
					for (int i = 0; i < array.size(); i++) {
						list.add(array.getString(i));
					}
					return list;
				}
			} else {
				log.warn("调用sso服务获取用户权限失败：" + result);
			}
		}
		return new ArrayList<>();
	}
	
	/**
	 * 查询BOSS该用户有权限的城市
	 * @param userId
	 * @return
	 */
	private List<UserDataPermissionDto> getSsoUserDataPermission(Integer userNo, String type) {
		Map<String, Object> param = new HashMap<>();
		param.put("userNo", userNo);
		param.put("type", type);

		String result = RestClient.postJson(ssoUrlsConfig.getUserDataPermission() , param);
		//log.info("调用boss查询用户数据权限结果：{}", result);
		JSONObject json = JSONObject.parseObject(result);
		if(json.getBooleanValue("success")) {
			JSONArray list = json.getJSONArray("data");
			if(null!=list && !list.isEmpty()) {
				List<UserDataPermissionDto> resp = new ArrayList<>(list.size());
				UserDataPermissionDto dto = null;
				JSONObject obj = null;
				for(int i=0; i<list.size(); i++) {
					obj = list.getJSONObject(i);
					dto = new UserDataPermissionDto();
					dto.setType(type);
					dto.setDataValue(obj.getString("dataValue"));
					dto.setDataDesc(obj.getString("dataDesc"));;
					resp.add(dto);
				}
				return resp;
			} else {
				return new ArrayList<>();
			}
		} else {
			log.warn("调用sso服务获取用户数据权限失败：" + result);
		}
		return null;
	}
}