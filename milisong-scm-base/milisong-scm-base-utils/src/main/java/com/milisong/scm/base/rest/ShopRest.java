package com.milisong.scm.base.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.ShopBossDto;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.base.dto.ShopParam;
import com.milisong.scm.base.dto.ShopReqDto;
import com.milisong.upms.constant.SsoErrorConstant;
import com.milisong.upms.utils.UserInfoUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 门店信息rest
 * @author youxia 2018年9月4日
 */
@Slf4j
@RestController
@RequestMapping("/shop")
public class ShopRest {

	@Autowired
	private ShopService shopService;
	
	/**
	 * 获取营业中的门店
	 * @return
	 */
	@PostMapping("/get-shop-list")
	public ResponseResult<List<ShopDto>> getShopList(){
		try {
			return shopService.getShopList();
		} catch (Exception e) {
			log.error("查询门店列表异常", e);
			throw e;
		}
	}

	/**
	 * 保存门店
	 * @param dto
	 * @return
	 */
	@PostMapping("/save-or-update")
	public ResponseResult<ShopDto> saveOrUpdate(@RequestBody ShopReqDto dto){
		try {
			String updateBy = UserInfoUtil.buildUpdateBy();
			if(dto.getId() == null){
				dto.setCreateBy(updateBy);
			}else{
				ResponseResult<String> checkResult = checkShopPermission(dto.getId());
				if(!checkResult.isSuccess()){
					return ResponseResult.buildFailResponse(checkResult.getCode(),checkResult.getMessage());
				}
				dto.setUpdateBy(updateBy);
			}
			return shopService.saveOrUpdate(dto);
		} catch (Exception e) {
			log.error("新增或更新门店异常", e);
			throw e;
		}
	}

	/**
	 * 分页查询门店信息
	 * @param dto
	 * @return
	 */
	@PostMapping("/query-by-condition")
	public ResponseResult<Pagination<ShopDto>> queryByCondition(@RequestBody ShopParam dto){
		return shopService.queryByCondition(dto);
	}

	/**
	 * 根据门店id查询门店信息
	 * @param id
	 * @return
	 */
	@GetMapping("/query-by-id")
	public ResponseResult<ShopDto> queryById(@RequestParam("id")Long id){
		return shopService.queryById(id);
	}


	/**
	 * 获取所有的门店（不过滤状态）
	 * @return
	 */
	@PostMapping("/get-all-shop-list")
	public ResponseResult<List<ShopBossDto>> getAllShopList(){
		try {
			return shopService.getAllShopList();
		} catch (Exception e) {
			log.error("查询门店列表异常", e);
			throw e;
		}
	}

	private ResponseResult<String> checkShopPermission(Long shopId){
		if(!UserInfoUtil.checkShopPermission(shopId)){
			return ResponseResult.buildFailResponse(SsoErrorConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getCode(),SsoErrorConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getDesc());
		}else{
			return ResponseResult.buildSuccessResponse();
		}
	}
}
