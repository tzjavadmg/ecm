package com.milisong.scm.goods.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.goods.api.GoodsService;
import com.milisong.scm.goods.constant.GoodsStatusEnum;
import com.milisong.scm.goods.domain.Goods;
import com.milisong.scm.goods.domain.GoodsCatalog;
import com.milisong.scm.goods.domain.GoodsExample;
import com.milisong.scm.goods.domain.GoodsExample.Criteria;
import com.milisong.scm.goods.dto.GoodsDto;
import com.milisong.scm.goods.mapper.GoodsCatalogExtMapper;
import com.milisong.scm.goods.mapper.GoodsExtMapper;
import com.milisong.scm.goods.param.GoodsParam;
import com.milisong.scm.goods.param.GoodsQueryParam;
import com.milisong.scm.goods.util.GoodsUtil;
import com.milisong.scm.shop.api.ShopOnsaleGoodsService;
import com.milisong.scm.shop.constant.ShopOnsaleGoodsStatusEnums;
import com.milisong.scm.shop.dto.goods.GoodsInfoDto;
import com.milisong.scm.shop.dto.shop.ShopOnsaleGoodsDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class GoodsServiceImpl implements GoodsService {
	
	@Autowired
	private GoodsExtMapper goodsExtMapper;

	@Autowired(required = false)
	private ShopOnsaleGoodsService shopOnsaleGoodsService;
	
	@Autowired
	private GoodsCatalogExtMapper goodsCatalogExtMapper;
	
	@Autowired
	private ShopService shopService;
	
	@Value("${file.oss.viewUrl}")
	private String picViewUrl;
	
	@Override
	public ResponseResult<Pagination<GoodsDto>> getGoodsPageInfo(GoodsQueryParam param) {
		log.info("???????????????????????????param={}", JSONObject.toJSONString(param));
		int count = goodsExtMapper.getGoodsListCount(param);
		Pagination<GoodsDto> pagination = new Pagination<>();
		pagination.setPageNo(param.getPageNo());
		pagination.setPageSize(param.getPageSize());
		pagination.setTotalCount(count);
		log.info("???????????????????????????count={}", count);
		if (count > 0) {
			List<GoodsDto> list = goodsExtMapper.getGoodsList(param);
			if (CollectionUtils.isNotEmpty(list)) {
				list.forEach(goodsDto -> {
					String detailsStatus = "DS" + goodsDto.getDetailStatus();
					if (detailsStatus.equals(GoodsStatusEnum.DETAIL_STATUS_FORTHCOMING.getValue())) {
						goodsDto.setStatusStr(GoodsStatusEnum.DETAIL_STATUS_FORTHCOMING.getName()); // ????????????
					} else if (detailsStatus.equals(GoodsStatusEnum.DETAIL_STATUS_DOWNLINE.getValue())) {
						goodsDto.setStatusStr(GoodsStatusEnum.DETAIL_STATUS_DOWNLINE.getName()); // ?????????
					} else {
						String statusStr = GoodsStatusEnum.getNameByStatus("S"+goodsDto.getStatus());
						goodsDto.setStatusStr(statusStr+"-"+GoodsStatusEnum.getNameByStatus(detailsStatus)); // ?????????/????????????/????????????
					}
					if (StringUtils.isNotBlank(goodsDto.getPicture())) {
						goodsDto.setPictureUrl(picViewUrl + "/5/" + goodsDto.getPicture());
					}
					if (StringUtils.isNotBlank(goodsDto.getBigPicture())) {
						goodsDto.setBigPictureUrl(picViewUrl + "/5/" + goodsDto.getBigPicture());
					}
				});
			}
			pagination.setDataList(list);
		} else {
			pagination.setDataList(new ArrayList<>());
		}
		return ResponseResult.buildSuccessResponse(pagination);
	}

	@Override
	public ResponseResult<GoodsDto> getGoodsDetailsById(Long goodsId) {
		log.info("?????????????????????goodsId={}", goodsId);
		if (goodsId == null) {
			log.info("goodsId??????");
			return ResponseResult.buildFailResponse("20001", "??????????????????");
		}
		Goods goods = goodsExtMapper.selectByPrimaryKey(goodsId);
		GoodsDto goodsDto = new GoodsDto();
		if (goods != null) {
			BeanMapper.copy(goods, goodsDto);
			goodsDto.setId(goods.getId());
			goodsDto.setCode(goods.getCode());
			goodsDto.setName(goods.getName());
			goodsDto.setPicture(goods.getPicture());
			if (StringUtils.isNotBlank(goodsDto.getPicture())) {
				goodsDto.setPictureUrl(picViewUrl + "/5/" + goodsDto.getPicture());
			}
			if (StringUtils.isNotBlank(goodsDto.getBigPicture())) {
				goodsDto.setBigPictureUrl(picViewUrl + "/5/" + goodsDto.getBigPicture());
			}
		}
		return ResponseResult.buildSuccessResponse(goodsDto);
	}

	@Override
	public ResponseResult<String> saveGoodsInfo(GoodsParam goodsParam) {
		if (goodsParam == null) {
			log.info("??????????????????");
			return ResponseResult.buildFailResponse("20001", "??????????????????");
		}
		RLock lock = LockProvider.getLock("SAVE_GOODS_" + goodsParam.getCode());
		try {
			lock.lock(3, TimeUnit.SECONDS);
			Goods goods = BeanMapper.map(goodsParam, Goods.class);
			Date now = new Date();
			GoodsUtil.buildStatus(now, Arrays.asList(goods)); // ??????????????????
			if (goods.getId() == null) {
				goods.setCreateBy(goods.getUpdateBy());
				
				GoodsExample example = new GoodsExample();
				Criteria criteria = example.createCriteria();
				criteria.andIsDeletedEqualTo(false); // false ??? 0
				criteria.andNameEqualTo(goods.getName());
				log.info("{}", JSONObject.toJSONString(example.getOredCriteria()));
				List<Goods> tempList = goodsExtMapper.selectByExample(example);
				if (CollectionUtils.isNotEmpty(tempList)) {
					log.info("?????????????????????");
					return ResponseResult.buildFailResponse("10001", "?????????????????????");
				}
				goods.setId(IdGenerator.nextId());
				String goodsCode = "";
				do {
					example.clear();
					criteria = example.createCriteria();
					goodsCode = GoodsUtil.generateGoodsCode();
					criteria.andCodeEqualTo(goodsCode);
					criteria.andIsDeletedEqualTo(false); // false ??? 0
					tempList = goodsExtMapper.selectByExample(example);
				} while (CollectionUtils.isNotEmpty(tempList));
				goods.setCode(goodsCode);
				// ??????????????????
				GoodsCatalog catalog = goodsCatalogExtMapper.getGoodsCatalog();
				if (catalog != null) {
					goods.setCategoryCode(catalog.getCode());
					goods.setCategoryName(catalog.getName());
				}
				goodsExtMapper.insertSelective(goods);
			} else {
				// ????????????????????????????????????????????????????????????????????????????????????????????????
				Goods dbGoods = goodsExtMapper.selectByPrimaryKey(goods.getId());
				goods.setCreateTime(dbGoods.getCreateTime());
				goods.setCreateBy(dbGoods.getCreateBy());
				goods.setCategoryCode(dbGoods.getCategoryCode());
				goods.setCategoryName(dbGoods.getCategoryName());
				goods.setIsDeleted(dbGoods.getIsDeleted());
				goods.setUpdateTime(new Date());
				
				goodsExtMapper.updateByPrimaryKey(goods);
			}
			doSaveShopOnsaleGoods(goods);
			return ResponseResult.buildSuccessResponse(goods.getCode());
		} finally {
			lock.unlock();
		}
	}
	
	@Override
	public ResponseResult<String> updateGoodsStatus() {
		GoodsExample example = new GoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo(false);
		criteria.andStatusNotEqualTo(Byte.parseByte(GoodsStatusEnum.STATUS_CLOSE.getValue().replace("S", "")));
		List<Goods> list = goodsExtMapper.selectByExample(example);
		if (CollectionUtils.isNotEmpty(list)) {
			log.info("?????????????????????????????????????????????list.size={}", list.size());
			GoodsUtil.buildStatus(new Date(), list);
			int upRes = goodsExtMapper.updateGoodsStatusByBatch(list);
			log.info("?????????????????????????????????upRes={}", upRes);
			List<ShopOnsaleGoodsDto> onsaleGoodsDtos = new ArrayList<>();
			List<Goods> goodsList = new ArrayList<>();
			ResponseResult<List<ShopDto>> shopsResponse = shopService.getShopList();
			if (null == shopsResponse || !shopsResponse.isSuccess() || null == shopsResponse.getData()) {
				log.error("???????????????????????????");
                throw new RuntimeException("???????????????????????????");
			}
			List<ShopDto> shops = shopsResponse.getData();
			if (CollectionUtils.isEmpty(shops)) {
				// ?????????????????????????????????v1.0
				log.error("???????????????????????????");
				return ResponseResult.buildFailResponse();
			}
				for (Goods goods : list) {
					String status = String.valueOf(goods.getDetailStatus());
					if (!GoodsStatusEnum.DETAIL_STATUS_DOWNLINE.getValue().replaceAll("DS", "").equals(status)) {
						for (ShopDto shopDto : shops) {
							ShopOnsaleGoodsDto onsaleGoodsDto = new ShopOnsaleGoodsDto();
							onsaleGoodsDto.setId(IdGenerator.nextId());
							onsaleGoodsDto.setShopId(shopDto.getId());
							onsaleGoodsDto.setShopCode(shopDto.getCode());
							onsaleGoodsDto.setGoodsCode(goods.getCode());
							onsaleGoodsDto.setGoodsName(goods.getName());
							onsaleGoodsDto.setStatus(Byte.parseByte(String.valueOf(ShopOnsaleGoodsStatusEnums.AVAILABLE.getStatus()))); // ?????????/????????????/?????????????????????
							onsaleGoodsDtos.add(onsaleGoodsDto);
						}
					goodsList.add(goods);
				}
			}
			shopOnsaleGoodsService.update(onsaleGoodsDtos, getGoosdInfo(goodsList));
		} else {
			log.info("??????????????????????????????????????????????????????");
		}
		return ResponseResult.buildSuccessResponse();
	}
	
	/**
	 * ????????????????????????
	 * @param goodsParam
	 * youxia 2018???9???4???
	 */
	private void doSaveShopOnsaleGoods(Goods goods) {
		// ??????????????????
		ShopOnsaleGoodsDto onsaleGoodsDto = new ShopOnsaleGoodsDto();
		onsaleGoodsDto.setGoodsCode(goods.getCode());
		onsaleGoodsDto.setGoodsName(goods.getName());
		String status = String.valueOf(goods.getDetailStatus());
		if (GoodsStatusEnum.DETAIL_STATUS_DOWNLINE.getValue().replaceAll("DS", "").equals(status)
				//|| GoodsStatusEnum.DETAIL_STATUS_FORTHCOMING.getValue().replaceAll("DS", "").equals(status)
				) {
			onsaleGoodsDto.setStatus(Byte.parseByte(String.valueOf(ShopOnsaleGoodsStatusEnums.NOTAVAILABLE.getStatus()))); // ?????????/????????????????????????	
		} else {
			onsaleGoodsDto.setStatus(Byte.parseByte(String.valueOf(ShopOnsaleGoodsStatusEnums.AVAILABLE.getStatus()))); // ?????????/????????????/????????????????????? 
		}
		if (onsaleGoodsDto.getStatus() == ShopOnsaleGoodsStatusEnums.AVAILABLE.getStatus() || GoodsStatusEnum.DETAIL_STATUS_DOWNLINE.getValue().replaceAll("DS", "").equals(status)) {
			// ????????????????????????ecm
			shopOnsaleGoodsService.insert(onsaleGoodsDto, getGoosdInfo(null));
		} else {
			shopOnsaleGoodsService.insert(onsaleGoodsDto, null);
		}
	}
	
	/**
	 * ??????????????????
	 * @return
	 * youxia 2018???9???4???
	 */
	private List<GoodsInfoDto> getGoosdInfo(List<Goods> list) {
		if (CollectionUtils.isEmpty(list)) {
			GoodsExample example = new GoodsExample();
			Criteria criteria = example.createCriteria();
			criteria.andIsDeletedEqualTo(false);
			list = goodsExtMapper.selectByExample(example);
		}
		if (CollectionUtils.isNotEmpty(list)) {
			List<GoodsInfoDto> goods = BeanMapper.mapList(list, GoodsInfoDto.class);
			for (GoodsInfoDto goodsInfoDto : goods) {
				String status = String.valueOf(goodsInfoDto.getStatus());
				if(GoodsStatusEnum.STATUS_USEING.getValue().replaceAll("S", "").equals(status)){
					goodsInfoDto.setDetailStatus(2);
				}else {
					goodsInfoDto.setDetailStatus(5);
				}
				if (StringUtils.isNotBlank(goodsInfoDto.getPicture())) {
					goodsInfoDto.setPicture(picViewUrl + "/5/" + goodsInfoDto.getPicture());
				}
				if (StringUtils.isNotBlank(goodsInfoDto.getBigPicture())) {
					goodsInfoDto.setBigPicture(picViewUrl + "/5/" + goodsInfoDto.getBigPicture());
				}
			}
			return goods;
		}
		return null;
	}
	
	
}
