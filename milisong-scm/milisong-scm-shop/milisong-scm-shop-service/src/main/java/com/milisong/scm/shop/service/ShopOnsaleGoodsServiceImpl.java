package com.milisong.scm.shop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.shop.api.ShopOnsaleGoodsService;
import com.milisong.scm.shop.constant.ShopOnsaleGoodsStatusEnums;
import com.milisong.scm.shop.domain.ShopOnsaleGoods;
import com.milisong.scm.shop.domain.ShopOnsaleGoodsExample;
import com.milisong.scm.shop.domain.ShopOnsaleGoodsExample.Criteria;
import com.milisong.scm.shop.dto.goods.GoodsCatalogDto;
import com.milisong.scm.shop.dto.goods.GoodsInfoDto;
import com.milisong.scm.shop.dto.shop.ShopOnsaleGoodsDto;
import com.milisong.scm.shop.mapper.ShopOnsaleGoodsExtMapper;
import com.milisong.scm.shop.mapper.ShopOnsaleGoodsMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class ShopOnsaleGoodsServiceImpl implements ShopOnsaleGoodsService{

	@Autowired
	private ShopOnsaleGoodsMapper shopOnsaleGoodsMapper;
	
	@Autowired
	private ShopOnsaleGoodsExtMapper shopOnsaleGoodsExtMapper;

	@Autowired
	private ShopService shopService;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${ecm.shoponsale.goods-url}")
	private String ecmShopOnsaleGoodsUrl;
	
	@Value("${ecm.shoponsale.goods-default-count}")
	private int goodsDefaultCount;

	@Override
	public List<ShopOnsaleGoodsDto> getByCondition() {
		//查询状态为可售的商品ID
		List<ShopOnsaleGoodsDto> listResult = shopOnsaleGoodsExtMapper.getOnsaleGoodsByStatus(ShopOnsaleGoodsStatusEnums.AVAILABLE.getStatus());
		return listResult;
	}

	@Override
	public int insert(ShopOnsaleGoodsDto shopOnsaleGoodsDto, List<GoodsInfoDto> goodsInfos) {
		log.info("更新可售商品：shopOnsaleGoodsDto={}", JSONObject.toJSONString(shopOnsaleGoodsDto));
		ShopOnsaleGoods onsaleGoods = BeanMapper.map(shopOnsaleGoodsDto, ShopOnsaleGoods.class);
		ShopOnsaleGoodsExample example = new ShopOnsaleGoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andGoodsCodeEqualTo(onsaleGoods.getGoodsCode());
		criteria.andIsDeletedEqualTo(false);
		List<ShopOnsaleGoods> list = shopOnsaleGoodsMapper.selectByExample(example);
		int res = 0;
		
		ResponseResult<List<ShopDto>> shopResp = shopService.getShopList();
		if(null == shopResp || !shopResp.isSuccess()) {
    		throw new RuntimeException("查询门店列表失败");
    	}
		List<ShopDto> shops = shopResp.getData();
		if (CollectionUtils.isNotEmpty(list)) {
			 for (ShopOnsaleGoods shopOnsaleGoods : list) {
					onsaleGoods.setId(shopOnsaleGoods.getId());
					onsaleGoods.setGoodsName(shopOnsaleGoodsDto.getGoodsName());
					log.info("修改可售商品信息:{}",JSON.toJSONString(onsaleGoods));
					res = shopOnsaleGoodsMapper.updateByPrimaryKeySelective(onsaleGoods);
				 }
		} else {
			for (ShopDto shopDto : shops) {
				onsaleGoods.setShopId(shopDto.getId());
				onsaleGoods.setShopCode(shopDto.getCode());
				onsaleGoods.setId(IdGenerator.nextId());
				onsaleGoods.setDefaultAvailableVolume(goodsDefaultCount); // 商品默认数量
				res = shopOnsaleGoodsMapper.insertSelective(onsaleGoods);
			}
			
		}
		if (CollectionUtils.isNotEmpty(goodsInfos)) {
			// 需要通知ecm更新商品信息
			example.clear();
			criteria = example.createCriteria();
			criteria.andIsDeletedEqualTo(false);
			criteria.andStatusEqualTo((byte) ShopOnsaleGoodsStatusEnums.AVAILABLE.getStatus());
			List<ShopOnsaleGoods> shopOnsaleGoodsList = shopOnsaleGoodsMapper.selectByExample(example);
			Map<String, ShopOnsaleGoods> map = new HashMap<>();
			for (ShopOnsaleGoods shopOnsaleGoods : shopOnsaleGoodsList) {
				map.put(shopOnsaleGoods.getGoodsCode(), shopOnsaleGoods);
			}
			List<GoodsCatalogDto> catalogDtos = getGoodsCatInfos(goodsInfos);
			if (CollectionUtils.isNotEmpty(catalogDtos)) {
				Map<String, List<GoodsCatalogDto>> catMap = new HashMap<>();
				for (ShopDto shopDto : shops) {
					catMap.put(shopDto.getCode(), catalogDtos);
				}
				setOnsaleGoods(catMap);
			}
		}
		return res;
	}

	@Override
	public int update(List<ShopOnsaleGoodsDto> shopOnsaleGoodsDtoList, List<GoodsInfoDto> goodsInfos) {
		if (CollectionUtils.isEmpty(shopOnsaleGoodsDtoList)) {
			log.info("可售商品为空");
			return 0;
		}
		ShopOnsaleGoodsExample example = new ShopOnsaleGoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo(false);
		List<ShopOnsaleGoods> list = shopOnsaleGoodsMapper.selectByExample(example);
		int sumRes = 0;
		
		
		ResponseResult<List<ShopDto>> shopResp = shopService.getShopList();
		if(null == shopResp || !shopResp.isSuccess()) {
    		throw new RuntimeException("查询门店列表失败");
    	}
		List<ShopDto> shops = shopResp.getData();

		if (CollectionUtils.isEmpty(list)) {
			// 所有为空批量新增
			for (ShopOnsaleGoodsDto dto : shopOnsaleGoodsDtoList) {
				dto.setDefaultAvailableVolume(goodsDefaultCount); // 默认可售量
			}
			int res = shopOnsaleGoodsExtMapper.insertOnsalGoodsByBatch(shopOnsaleGoodsDtoList);
			log.info("新增可售商品信息：res={}", res);
			sumRes += res;
		} else {
			Map<String, ShopOnsaleGoodsDto> map = new HashMap<>(); // 记录新的可售商品
			Set<String> allCodeSet = new HashSet<>(); // 所有已有code
			Set<String> codeSet = new HashSet<>(); // 可售code
			for (ShopOnsaleGoods goods : list) {
				allCodeSet.add(goods.getGoodsCode()+goods.getShopId()); // 记录原有的可售商品code
			}
			for (ShopOnsaleGoodsDto dto : shopOnsaleGoodsDtoList) {
				for (int i = 0; i < list.size(); i++) {
					ShopOnsaleGoods goods = list.get(i);
					if ((dto.getGoodsCode()+dto.getShopId()).equals(goods.getGoodsCode()+goods.getShopId())) {
						// 存在，不需要变更状态
						codeSet.add(dto.getGoodsCode()+dto.getShopId());
						break;
					}
					if (i == list.size() - 1) {
						// code不在list中，可售商品
						map.put(dto.getGoodsCode()+dto.getShopId(), dto); // 记录新的可售商品
						codeSet.add(dto.getGoodsCode()+dto.getShopId());
					}
				}
			}
			allCodeSet.removeAll(codeSet); // 剩余code为不可售code
			if (CollectionUtils.isNotEmpty(map.values())) {
				List<ShopOnsaleGoodsDto> dtos = new ArrayList<>();
				dtos.addAll(map.values());
				dtos.forEach(dto -> {
					dto.setDefaultAvailableVolume(goodsDefaultCount);
				});
				int res = shopOnsaleGoodsExtMapper.insertOnsalGoodsByBatch(dtos);
				log.info("新增可售商品信息：res={}", res);
				sumRes += res;
			}
			if (!allCodeSet.isEmpty()) {
				// 更新不可售
				List<String> goodsCodes = new ArrayList<>();
				goodsCodes.addAll(allCodeSet);
				Map<String, Object> upMap = new HashMap<>();
				upMap.put("status", ShopOnsaleGoodsStatusEnums.NOTAVAILABLE.getStatus());
				upMap.put("goodsCodes", goodsCodes);
				int res = shopOnsaleGoodsExtMapper.updateOnsaleGoodsByBatch(upMap);
				log.info("更新不可售商品信息：res={}", res);
				sumRes += res;
			}
		}
		List<GoodsCatalogDto> catalogDtos = getGoodsCatInfos(goodsInfos);
		if (CollectionUtils.isNotEmpty(catalogDtos)) {
			// 需要通知ecm更新商品信息
			Map<String, List<GoodsCatalogDto>> catMap = new HashMap<>();
			for (ShopDto shopDto : shops) {
				catMap.put(shopDto.getCode(), catalogDtos);
			}
			setOnsaleGoods(catMap);
		}
		return sumRes;
	}

	/**
	 * 调用ecm更新商品信息
	 * @param catMap
	 * @return
	 * youxia 2018年9月5日
	 */
	@SuppressWarnings("unchecked")
	public boolean setOnsaleGoods(Map<String, List<GoodsCatalogDto>> catMap) {
		ResponseResult<Object> obj = ResponseResult.buildFailResponse();
		try {
			log.info("通知ecm更新可售商品信息请求信息：catMap={}", JSONObject.toJSONString(catMap));
			obj = restTemplate.postForObject(ecmShopOnsaleGoodsUrl, catMap, ResponseResult.class);
			log.info("通知ecm更新可售商品信息返回结果：obj={}", JSONObject.toJSONString(obj));
			log.info("通知结果：obj.success={}", obj.isSuccess());
		} catch (RestClientException e) {
			log.error("调用ecm设置可售商品,{}{}", e.getMessage(), e);
		}
		return obj.isSuccess();
	}

	/**
	 * 组织通知ecm的更新商品信息
	 * @param goods
	 * @return
	 * youxia 2018年9月6日
	 */
	public List<GoodsCatalogDto> getGoodsCatInfos(List<GoodsInfoDto> goods) {
		log.info("通知ecm：goods={}", JSONObject.toJSONString(goods));
		Map<String, List<GoodsInfoDto>> map = new HashMap<>();
		Map<String,String> categoryMap = new HashMap<String,String>();
		for (GoodsInfoDto dto : goods) {
			String categoryName = dto.getCategoryName();
			String categoryCode = dto.getCategoryCode();
			if (StringUtils.isNotBlank(categoryCode)) {
				List<GoodsInfoDto> list = map.get(categoryCode);
				if (CollectionUtils.isEmpty(list)) {
					list = new ArrayList<>();
				}
				list.add(dto);
				map.put(categoryCode, list);
			}
			categoryMap.put(categoryCode, categoryName);
		}
		List<GoodsCatalogDto> catalogDtos = new ArrayList<>();
		if (map != null && !map.isEmpty()) {
			map.forEach((k, v) -> {
				GoodsCatalogDto catalogDto = new GoodsCatalogDto();
				catalogDto.setCategoryCode(k);
				catalogDto.setHasChild(false);
				catalogDto.setGoods(v);
				catalogDto.setCategoryName(categoryMap.get(k));
				catalogDtos.add(catalogDto);
			});
		}
		return catalogDtos;
	}

}
