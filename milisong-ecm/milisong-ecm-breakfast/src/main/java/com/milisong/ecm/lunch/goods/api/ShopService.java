package com.milisong.ecm.lunch.goods.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.ecm.breakfast.dto.GoodsCatalogDto;
import com.milisong.ecm.common.dto.BuildingDto;
import com.milisong.ecm.lunch.goods.dto.GoodsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/9/3   11:37
 *    desc    : 门店业务接口
 *    version : v1.0
 * </pre>
 */
@FeignClient("milisong-ecm-lunch")
public interface ShopService {

	/**
	 * 门店展示信息详情查询
	 * @param buildId
	 * @return
	 */
	@PostMapping("/v1/ShopService/getShopInfo")
	ResponseResult<ShopDisplayDto> getShopInfo(@RequestParam("buildId") Long buildId);

	/**
	 * 门店展示信息详情查询
	 * @param code
	 * @return
	 */
	@PostMapping("/v1/ShopService/getShopInfoByCode")
	@Deprecated
	ResponseResult<ShopDisplayDto> getShopInfoByCode(@RequestParam("code") String code);

	/**
	 * 门店展示商品信息详情查询
	 * @param shopCode
	 * @return
	 */
	@PostMapping("/v1/ShopService/getShopSaleGoodsDetail")
	ResponseResult<List<GoodsCatalogDto>> getShopSaleGoodsDetail(@RequestParam("shopCode") String shopCode, @RequestParam("setOnTime") String setOnTime, @RequestParam("categoryCode") String categoryCode);

	/**
	 * 接收来自SCM的门店展示商品更新通知，缓存到Redis
	 * @param dto
	 * @return
	 */
	@PostMapping("/v1/ShopService/receiveShopSaleGoodsDetail")
	ResponseResult<Boolean> receiveShopSaleGoodsDetail(@RequestBody Map<String, List<GoodsCatalogDto>> dto);

	/**
	 * 查询门店商品的价格
	 * @param shopCode
	 * @param goodsCode
	 * @return
	 */
	@PostMapping("/v1/ShopService/getGoodsInfo")
	ResponseResult<GoodsDto> getGoodsInfo(@RequestParam("shopCode") String shopCode, @RequestParam("setOnTime") Long setOnTime, @RequestParam("goodsCode") String goodsCode);

	/**
	 * 设置楼宇
	 *
	 * @param buildingDtos
	 * @return
	 */
	@PostMapping("/v1/ShopService/setBuildings")
	ResponseResult<Boolean> setBuildings(@RequestBody List<BuildingDto> buildingDtos);
}
