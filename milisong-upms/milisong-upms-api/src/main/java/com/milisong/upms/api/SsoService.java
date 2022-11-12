package com.milisong.upms.api;

import java.util.List;

import com.farmland.core.api.ResponseResult;
import com.milisong.upms.dto.MenuDto;
import com.milisong.upms.dto.UserDataPermissionDto;
import com.milisong.upms.dto.UserInfoDto;

public interface SsoService {
	/**
	 * 通过ticket 交换token
	 * 
	 * @param ticket
	 * @return
	 */
	ResponseResult<String> getToken(String ticket, String appName);

	/**
	 * 获取用户信息
	 * 
	 * @return
	 */
	ResponseResult<UserInfoDto> getUserInfo(String token);

	/**
	 * 获取用户菜单tree
	 * 
	 * @param userNo
	 * @return
	 */
	ResponseResult<List<MenuDto>> getUserMenu(String token);

	/**
	 * 检查用户是否登录
	 * 
	 * @param token
	 * @return
	 */
	ResponseResult<Boolean> checkLogin(String token);

	/**
	 * 检查用户权限
	 * 
	 * @param token
	 * @param url
	 * @return
	 */
	ResponseResult<Boolean> checkUserPermission(String token, String url);

	/**
	 * 获取用户的数据权限
	 * 
	 * @param token
	 * @param dataType
	 *            MLS_SHOP代表门店权限
	 * @return
	 */
	ResponseResult<List<UserDataPermissionDto>> getShopByPermission(String token, String dataType);
}
