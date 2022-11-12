package com.milisong.scm.base.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.base.dto.ShopBossDto;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.base.dto.ShopParam;
import com.milisong.scm.base.dto.ShopReqDto;

@FeignClient("milisong-scm-base-service")
public interface ShopService {

	/**
	 * 查询门店信息
	 * @return
	 * youxia 2018年9月2日
	 */
	@PostMapping("/base/shop/list")
	public ResponseResult<List<ShopDto>> getShopList();
	
	/**
	 * 获取所有的门店（不过滤状态）
	 * @return
	 */
	@PostMapping("/base/shop/list-all")
	public ResponseResult<List<ShopBossDto>> getAllShopList();

	/**
	 * 新增或更新门店信息
	 * @param dto
	 * @return
	 */
	@PostMapping("/base/shop/save")
	ResponseResult<ShopDto> saveOrUpdate(@RequestBody ShopReqDto dto);

	/**
	 * 门店分页条件查询
	 * @param dto
	 * @return
	 */
	@PostMapping("/base/shop/query-by-condition")
	ResponseResult<Pagination<ShopDto>> queryByCondition(@RequestBody ShopParam dto);

	/**
	 * 通过id查询门店信息
	 * @param id
	 * @return
	 */
	@PostMapping("/base/shop/query-by-id")
	ResponseResult<ShopDto> queryById(@RequestParam("id") Long id);
	
	/**
	 * 通过code查询门店信息
	 * @param id
	 * @return
	 */
	@PostMapping("/base/shop/query-by-code")
	ResponseResult<ShopDto> queryByCode(@RequestParam("code")String code);
}
