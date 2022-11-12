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
		log.info("分页查询商品列表：param={}", JSONObject.toJSONString(param));
		int count = goodsExtMapper.getGoodsListCount(param);
		Pagination<GoodsDto> pagination = new Pagination<>();
		pagination.setPageNo(param.getPageNo());
		pagination.setPageSize(param.getPageSize());
		pagination.setTotalCount(count);
		log.info("统计商品列表数量：count={}", count);
		if (count > 0) {
			List<GoodsDto> list = goodsExtMapper.getGoodsList(param);
			if (CollectionUtils.isNotEmpty(list)) {
				list.forEach(goodsDto -> {
					String detailsStatus = "DS" + goodsDto.getDetailStatus();
					if (detailsStatus.equals(GoodsStatusEnum.DETAIL_STATUS_FORTHCOMING.getValue())) {
						goodsDto.setStatusStr(GoodsStatusEnum.DETAIL_STATUS_FORTHCOMING.getName()); // 即将上线
					} else if (detailsStatus.equals(GoodsStatusEnum.DETAIL_STATUS_DOWNLINE.getValue())) {
						goodsDto.setStatusStr(GoodsStatusEnum.DETAIL_STATUS_DOWNLINE.getName()); // 已下线
					} else {
						String statusStr = GoodsStatusEnum.getNameByStatus("S"+goodsDto.getStatus());
						goodsDto.setStatusStr(statusStr+"-"+GoodsStatusEnum.getNameByStatus(detailsStatus)); // 使用中/今日下线/明日下线
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
		log.info("查询商品明细：goodsId={}", goodsId);
		if (goodsId == null) {
			log.info("goodsId为空");
			return ResponseResult.buildFailResponse("20001", "参数校验错误");
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
			log.info("保存信息为空");
			return ResponseResult.buildFailResponse("20001", "参数校验错误");
		}
		RLock lock = LockProvider.getLock("SAVE_GOODS_" + goodsParam.getCode());
		try {
			lock.lock(3, TimeUnit.SECONDS);
			Goods goods = BeanMapper.map(goodsParam, Goods.class);
			Date now = new Date();
			GoodsUtil.buildStatus(now, Arrays.asList(goods)); // 构建明细状态
			if (goods.getId() == null) {
				goods.setCreateBy(goods.getUpdateBy());
				
				GoodsExample example = new GoodsExample();
				Criteria criteria = example.createCriteria();
				criteria.andIsDeletedEqualTo(false); // false 为 0
				criteria.andNameEqualTo(goods.getName());
				log.info("{}", JSONObject.toJSONString(example.getOredCriteria()));
				List<Goods> tempList = goodsExtMapper.selectByExample(example);
				if (CollectionUtils.isNotEmpty(tempList)) {
					log.info("商品名称已存在");
					return ResponseResult.buildFailResponse("10001", "商品名称已存在");
				}
				goods.setId(IdGenerator.nextId());
				String goodsCode = "";
				do {
					example.clear();
					criteria = example.createCriteria();
					goodsCode = GoodsUtil.generateGoodsCode();
					criteria.andCodeEqualTo(goodsCode);
					criteria.andIsDeletedEqualTo(false); // false 为 0
					tempList = goodsExtMapper.selectByExample(example);
				} while (CollectionUtils.isNotEmpty(tempList));
				goods.setCode(goodsCode);
				// 查询类目信息
				GoodsCatalog catalog = goodsCatalogExtMapper.getGoodsCatalog();
				if (catalog != null) {
					goods.setCategoryCode(catalog.getCode());
					goods.setCategoryName(catalog.getName());
				}
				goodsExtMapper.insertSelective(goods);
			} else {
				// 由于折扣金额可以修改为空，故要先将数据库里部分字段信息先查询出来
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
			log.info("查询商品状态不为已停用的信息：list.size={}", list.size());
			GoodsUtil.buildStatus(new Date(), list);
			int upRes = goodsExtMapper.updateGoodsStatusByBatch(list);
			log.info("批量更新商品状态结果：upRes={}", upRes);
			List<ShopOnsaleGoodsDto> onsaleGoodsDtos = new ArrayList<>();
			List<Goods> goodsList = new ArrayList<>();
			ResponseResult<List<ShopDto>> shopsResponse = shopService.getShopList();
			if (null == shopsResponse || !shopsResponse.isSuccess() || null == shopsResponse.getData()) {
				log.error("未能查询到门店信息");
                throw new RuntimeException("未能查询到门店信息");
			}
			List<ShopDto> shops = shopsResponse.getData();
			if (CollectionUtils.isEmpty(shops)) {
				// 默认取第一个门店的信息v1.0
				log.error("没有找到可用的门店");
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
							onsaleGoodsDto.setStatus(Byte.parseByte(String.valueOf(ShopOnsaleGoodsStatusEnums.AVAILABLE.getStatus()))); // 使用中/今日下线/明日下线：可售
							onsaleGoodsDtos.add(onsaleGoodsDto);
						}
					goodsList.add(goods);
				}
			}
			shopOnsaleGoodsService.update(onsaleGoodsDtos, getGoosdInfo(goodsList));
		} else {
			log.info("查询商品状态不为已停用的商品信息为空");
		}
		return ResponseResult.buildSuccessResponse();
	}
	
	/**
	 * 保存可售商品信息
	 * @param goodsParam
	 * youxia 2018年9月4日
	 */
	private void doSaveShopOnsaleGoods(Goods goods) {
		// 保存可售商品
		ShopOnsaleGoodsDto onsaleGoodsDto = new ShopOnsaleGoodsDto();
		onsaleGoodsDto.setGoodsCode(goods.getCode());
		onsaleGoodsDto.setGoodsName(goods.getName());
		String status = String.valueOf(goods.getDetailStatus());
		if (GoodsStatusEnum.DETAIL_STATUS_DOWNLINE.getValue().replaceAll("DS", "").equals(status)
				//|| GoodsStatusEnum.DETAIL_STATUS_FORTHCOMING.getValue().replaceAll("DS", "").equals(status)
				) {
			onsaleGoodsDto.setStatus(Byte.parseByte(String.valueOf(ShopOnsaleGoodsStatusEnums.NOTAVAILABLE.getStatus()))); // 已下线/即将上线：不可售	
		} else {
			onsaleGoodsDto.setStatus(Byte.parseByte(String.valueOf(ShopOnsaleGoodsStatusEnums.AVAILABLE.getStatus()))); // 使用中/今日下线/明日下线：可售 
		}
		if (onsaleGoodsDto.getStatus() == ShopOnsaleGoodsStatusEnums.AVAILABLE.getStatus() || GoodsStatusEnum.DETAIL_STATUS_DOWNLINE.getValue().replaceAll("DS", "").equals(status)) {
			// 可售或已下线通知ecm
			shopOnsaleGoodsService.insert(onsaleGoodsDto, getGoosdInfo(null));
		} else {
			shopOnsaleGoodsService.insert(onsaleGoodsDto, null);
		}
	}
	
	/**
	 * 转换商品信息
	 * @return
	 * youxia 2018年9月4日
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
