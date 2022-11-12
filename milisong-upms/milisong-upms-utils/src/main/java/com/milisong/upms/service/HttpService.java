package com.milisong.upms.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.milisong.upms.config.SsoConfig;
import com.milisong.upms.constant.SsoConstant;
import com.milisong.upms.constant.SsoErrorConstant.SYSTEM_INFO;
import com.milisong.upms.dto.MenuDto;
import com.milisong.upms.dto.ShopDto;
import com.milisong.upms.dto.UserDataPermissionDto;
import com.milisong.upms.dto.UserInfoDto;
import com.milisong.upms.utils.RestClient;

@Service
public class HttpService {
	@Autowired
	private SsoConfig config;

	// 获取token
	public ResponseResult<String> getMilisongToken(String ticket) {
		String json =  RestClient.get(config.getUpmsTokenUrl() + "?ticket=" + ticket + "&appName=" + config.getAppName());
		return convertTokenResult(json);
	}
	
	/**
	 * 根据token获取用户信息
	 * 
	 * @param token
	 * @return
	 */
	public ResponseResult<UserInfoDto> getUserInfo(String token) {
		String result = RestClient.get(config.getUpmsUserInfoUrl() + "?token=" + token);
		return convertUserInfoResult(result);
	}
	
	/**
	 * 根据token获取用户菜单
	 * 
	 * @param token
	 * @return
	 */
	public ResponseResult<List<MenuDto>> getUserMenu(String token) {
		String json = RestClient.get(config.getUpmsGetMenuListUrl() + "?token=" + token);
		return convertMenuResult(json);
	}
	
	/**
	 * 根据token获取用户门店权限
	 * 
	 * @param token
	 * @return
	 */
	public ResponseResult<List<UserDataPermissionDto>> getShopPermissionList(String token) {
		String url = config.getUpmsGetDataPermissionUrl() + "?token=" + token + "&dataType="
				+ SsoConstant.DataType.MLS_SHOP;
		String json = RestClient.get(url);
		
		return convertShopPermissionResult(json);
	}

	/**
	 * 根据token获取到门店数据权限的加工版本
	 * 
	 * @param token
	 * @return
	 */
	public ResponseResult<List<ShopDto>> getCurrentUserShopList(String token) {
		List<ShopDto> result = new ArrayList<>();

		ResponseResult<List<UserDataPermissionDto>> list = getShopPermissionList(token);
		if (null != list && list.isSuccess()
				&& org.apache.commons.collections.CollectionUtils.isNotEmpty(list.getData())) {
			list.getData().stream().forEach(item -> {
				ShopDto dto = new ShopDto();
				dto.setShopId(Long.valueOf(item.getDataValue().split("_")[0]));
				dto.setShopCode(item.getDataValue().split("_")[1]);
				dto.setShopName(item.getDataDesc());
				result.add(dto);
			});
		}
		return ResponseResult.buildSuccessResponse(result);
	}

	/**
	 * 检查是否登录
	 * 
	 * @param token
	 * @return
	 */
	public Boolean checkLogin(String token) {
		String url = config.getUpmsCheckLoginUrl() + "?token=" + token;
		return checkBoolean(url);
	}
	
	/**
	 * 检查是否有权限
	 * @param token
	 * @param strUrl
	 * @return
	 */
	public Boolean checkUserPermission(String token ,String strUrl){
        String url = config.getUpmsCheckPermissionUrl()+"?token="+token+"&url="+strUrl;
        return checkBoolean(url);
    }
	
	/**
	 * 检查boolean
	 * @param url
	 * @return
	 */
	private Boolean checkBoolean(String url) {
		String result = RestClient.get(url);
        if(StringUtils.isNotBlank(result)) {
			JSONObject json = JSONObject.parseObject(result);
			if(json.getBooleanValue("success")) {
				return json.getBooleanValue("data");
			}
		}
        return false;
	}
	
	/**
	 * 转换token结果
	 * @param result
	 * @return
	 */
	private ResponseResult<String> convertTokenResult(String result) {
		if(StringUtils.isNotBlank(result)) {
			JSONObject json = JSONObject.parseObject(result);
			if(json.getBooleanValue("success")) {
				return ResponseResult.buildSuccessResponse(json.getString("data"));
			} else {
				return ResponseResult.buildFailResponse(json.getString("code"), json.getString("message"));
			}
		}
		
		return ResponseResult.buildFailResponse(SYSTEM_INFO.FAIL.getCode(), SYSTEM_INFO.FAIL.getDesc());
	}
	
	/**
	 * 转换userInfo结果
	 * @param result
	 * @return
	 */
	private ResponseResult<UserInfoDto> convertUserInfoResult(String result) {
		if(StringUtils.isNotBlank(result)) {
			JSONObject json = JSONObject.parseObject(result);
			if(json.getBooleanValue("success")) {
				JSONObject data = json.getJSONObject("data");
				if(null != data) {
					return ResponseResult.buildSuccessResponse(JSONObject.toJavaObject(data, UserInfoDto.class));
				}
			} else {
				return ResponseResult.buildFailResponse(json.getString("code"), json.getString("message"));
			}
		}
		
		return ResponseResult.buildFailResponse(SYSTEM_INFO.FAIL.getCode(), SYSTEM_INFO.FAIL.getDesc());
	}
	
	/**
	 * 转换userInfo结果
	 * @param result
	 * @return
	 */
	private ResponseResult<List<MenuDto>> convertMenuResult(String result) {
		if(StringUtils.isNotBlank(result)) {
			JSONObject json = JSONObject.parseObject(result);
			if(json.getBooleanValue("success")) {
				JSONArray data = json.getJSONArray("data");
				if(null != data) {
					return ResponseResult.buildSuccessResponse(JSONArray.parseArray(data.toJSONString(), MenuDto.class));
				}
			} else {
				return ResponseResult.buildFailResponse(json.getString("code"), json.getString("message"));
			}
		}
		
		return ResponseResult.buildFailResponse(SYSTEM_INFO.FAIL.getCode(), SYSTEM_INFO.FAIL.getDesc());
	}
	
	/**
	 * 转换ShopPermission结果
	 * @param result
	 * @return
	 */
	private ResponseResult<List<UserDataPermissionDto>> convertShopPermissionResult(String result) {
		if(StringUtils.isNotBlank(result)) {
			JSONObject json = JSONObject.parseObject(result);
			if(json.getBooleanValue("success")) {
				JSONArray data = json.getJSONArray("data");
				if(null != data) {
					return ResponseResult.buildSuccessResponse(JSONArray.parseArray(data.toJSONString(), UserDataPermissionDto.class));
				}
			} else {
				return ResponseResult.buildFailResponse(json.getString("code"), json.getString("message"));
			}
		}
		
		return ResponseResult.buildFailResponse(SYSTEM_INFO.FAIL.getCode(), SYSTEM_INFO.FAIL.getDesc());
	}
}
