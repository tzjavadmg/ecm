package com.milisong.scm.goods.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.goods.api.GoodsLunchService;
import com.milisong.scm.goods.api.GoodsScheduleLunchService;
import com.milisong.scm.goods.constant.GoodsLunchStatusEnum;
import com.milisong.scm.goods.domain.GoodsCatalog;
import com.milisong.scm.goods.domain.GoodsLunch;
import com.milisong.scm.goods.domain.GoodsLunchExample;
import com.milisong.scm.goods.domain.GoodsLunchExample.Criteria;
import com.milisong.scm.goods.dto.GoodsLunchDto;
import com.milisong.scm.goods.dto.GoodsScheduleDetailLunchDto;
import com.milisong.scm.goods.mapper.GoodsCatalogExtMapper;
import com.milisong.scm.goods.mapper.GoodsLunchMapper;
import com.milisong.scm.goods.mq.MqProducerGoodsUtil;
import com.milisong.scm.goods.param.GoodsLunchParam;
import com.milisong.scm.goods.param.GoodsLunchQueryParam;
import com.milisong.scm.goods.util.GoodsUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
@RefreshScope
public class GoodsLunchServiceImpl implements GoodsLunchService {

	@Autowired
	private GoodsLunchMapper goodsLunchMapper;
	
	@Autowired
	private GoodsScheduleLunchService goodsScheduleService;
	
	@Autowired
	private GoodsCatalogExtMapper goodsCatalogExtMapper;
	
	@Value("${file.oss.viewUrl}")
	private String picViewUrl;
	
 
	@Override
	public ResponseResult<Pagination<GoodsLunchDto>> getGoodsLunchPageInfo(GoodsLunchQueryParam param) {
		Pagination<GoodsLunchDto> pagination = new Pagination<GoodsLunchDto>();
		GoodsLunchExample goodsExample = new GoodsLunchExample();
		goodsExample.setPageSize(param.getPageSize());
		goodsExample.setStartRow(param.getStartRow());
		Criteria goodsCriteria = goodsExample.createCriteria();
		if(StringUtils.isNotBlank(param.getCategoryCode())) {
			goodsCriteria.andCategoryCodeEqualTo(param.getCategoryCode());
		}
		if(StringUtils.isNotBlank(param.getCode())) {
			goodsCriteria.andCodeLike("%"+param.getCode()+"%");
		}
		 
		if(StringUtils.isNotBlank(param.getName())) {
			goodsCriteria.andNameLike("%"+param.getName()+"%");
		}
		if(null != param.getStatus()) {
			goodsCriteria.andStatusEqualTo(param.getStatus());
		}
		long count = goodsLunchMapper.countByExample(goodsExample);
		pagination.setTotalCount(count);
		if(count>0) {
			goodsExample.setPageSize(param.getPageSize());
			goodsExample.setStartRow(param.getStartRow());
			goodsExample.setOrderByClause(" weight desc ");
			List<GoodsLunch> listGoods = goodsLunchMapper.selectByExample(goodsExample);
			if(CollectionUtils.isNotEmpty(listGoods)) {
				List<GoodsLunchDto> listGoodsDto = BeanMapper.mapList(listGoods, GoodsLunchDto.class);
				for (GoodsLunchDto goodsDto : listGoodsDto) {
					goodsDto.setStatusStr(GoodsLunchStatusEnum.getNameByStatus(goodsDto.getStatus()));
					goodsDto.setPicture(picViewUrl + "/5/" +goodsDto.getPicture());
					goodsDto.setBigPicture(picViewUrl + "/5/" +goodsDto.getBigPicture());
				}
				pagination.setDataList(listGoodsDto);
			}
		}
		return ResponseResult.buildSuccessResponse(pagination);
	}

	@Override
	public ResponseResult<GoodsLunchDto> getGoodsLunchDetailsById(Long goodsId) {
		GoodsLunch goodslunch = goodsLunchMapper.selectByPrimaryKey(goodsId);
		GoodsLunchDto goodsLunchDto = BeanMapper.map(goodslunch, GoodsLunchDto.class);
		goodsLunchDto.setBigPictureShow(picViewUrl.concat("/5/").concat(goodsLunchDto.getBigPicture()));
		goodsLunchDto.setPictureShow(picViewUrl.concat("/5/").concat(goodsLunchDto.getPicture()));
		return ResponseResult.buildSuccessResponse(goodsLunchDto);
	}

	@Override
	public ResponseResult<String> saveGoodsLunchInfo(GoodsLunchParam goodsParam) {
		if (goodsParam == null) {
			log.info("保存信息为空");
			return ResponseResult.buildFailResponse("20001", "参数校验错误");
		}
		RLock lock = LockProvider.getLock("SAVE_GOODS_" + goodsParam.getCode());
		try {
			lock.lock(3, TimeUnit.SECONDS);
			GoodsLunch goods = BeanMapper.map(goodsParam, GoodsLunch.class);
			if(null == goods.getStatus() || 0 == goods.getStatus() ){
				log.info("状态必填");
				return ResponseResult.buildFailResponse("20001", "参数校验错误,状态不能为空");

			}
			if (goods.getId() == null) {
				goods.setCreateBy(goods.getUpdateBy());
				GoodsLunchExample example = new GoodsLunchExample();
				Criteria criteria = example.createCriteria();
				criteria.andIsDeletedEqualTo(false); // false 为 0
				criteria.andNameEqualTo(goods.getName());
				log.info("{}", JSONObject.toJSONString(example.getOredCriteria()));
				List<GoodsLunch> tempList = goodsLunchMapper.selectByExample(example);
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
					tempList = goodsLunchMapper.selectByExample(example);
				} while (CollectionUtils.isNotEmpty(tempList));
				goods.setCode(goodsCode);
				// 查询类目信息
				// 查询类目信息
				GoodsCatalog catalog = goodsCatalogExtMapper.getGoodsCatalog();
				if (catalog != null) {
					goods.setCategoryCode(catalog.getCode());
					goods.setCategoryName(catalog.getName());
				}
				goodsLunchMapper.insertSelective(goods);
			} else {
				// 由于折扣金额可以修改为空，故要先将数据库里部分字段信息先查询出来
				GoodsLunch dbGoods = goodsLunchMapper.selectByPrimaryKey(goods.getId());
				goods.setCreateTime(dbGoods.getCreateTime());
				goods.setCreateBy(dbGoods.getCreateBy());
				goods.setCategoryCode(dbGoods.getCategoryCode());
				goods.setCategoryName(dbGoods.getCategoryName());
				goods.setIsDeleted(dbGoods.getIsDeleted());
				goods.setUpdateTime(new Date());
				goodsLunchMapper.updateByPrimaryKey(goods);
			}
			//发送商品MQ
			senGoodsInfoMq(goods.getId());
			return ResponseResult.buildSuccessResponse(goods.getCode());
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 发送MQ
	 * @param id
	 */
	private void senGoodsInfoMq(Long id) {
		log.info("发送MQ,商品ID{}",id);
		GoodsLunch  goods = goodsLunchMapper.selectByPrimaryKey(id);
		if(null != goods) {
			goods.setPicture(picViewUrl + "/5/" +goods.getPicture());
			goods.setBigPicture(picViewUrl + "/5/" +goods.getBigPicture());
			GoodsLunchDto goodsInfoMqDto = BeanMapper.map(goods, GoodsLunchDto.class);
			log.info("需要发送的商品信息{}",JSON.toJSONString(goodsInfoMqDto));
			MqProducerGoodsUtil.sendGoodsBasicMsg(goodsInfoMqDto);
		}
	}
	
	@Override
	public ResponseResult<List<GoodsLunchDto>> getGoodsLunchBySchedule(Integer year, Integer weekOfYear, Long shopId) {
		//获取档期中的商品信息
		List<GoodsScheduleDetailLunchDto>  listGoodsScheduleDetailsDto = goodsScheduleService.getGoodsScheduleDetailByWeekOfYear(year, weekOfYear, shopId);
		List<String> goodsCode = null;
		if(CollectionUtils.isNotEmpty(listGoodsScheduleDetailsDto)) {
			goodsCode = listGoodsScheduleDetailsDto.stream().map(GoodsScheduleDetailLunchDto::getGoodsCode).collect(Collectors.toList());
		}
		GoodsLunchExample goodsExample = new GoodsLunchExample();
		Criteria  goodsCriteria = goodsExample.createCriteria().andIsDeletedEqualTo(false).
		andStatusEqualTo(GoodsLunchStatusEnum.STATUS_USEING.getValue());
		if(CollectionUtils.isNotEmpty(goodsCode)) {
			goodsCriteria.andCodeNotIn(goodsCode);
		}
		List<GoodsLunch> listGoods = goodsLunchMapper.selectByExample(goodsExample);
		if(CollectionUtils.isNotEmpty(listGoods)) {
			List<GoodsLunchDto> listGoodsDto = BeanMapper.mapList(listGoods, GoodsLunchDto.class);
			for (GoodsLunchDto goodsDto : listGoodsDto) {
				goodsDto.setStatusStr(GoodsLunchStatusEnum.getNameByStatus(goodsDto.getStatus()));
			}
			return ResponseResult.buildSuccessResponse(listGoodsDto);
		}
		return null;
	}

	@Override
	public List<GoodsLunchDto> getGoodsLunchInfoByGoodsCodes(List<String> goodsCode) {
		if(CollectionUtils.isEmpty(goodsCode)) {
			return null;
		}
		GoodsLunchExample goodsExample = new GoodsLunchExample();
		goodsExample.createCriteria().andCodeIn(goodsCode);
		List<GoodsLunch> listGoods = goodsLunchMapper.selectByExample(goodsExample);
		if(CollectionUtils.isNotEmpty(listGoods)) {
			return BeanMapper.mapList(listGoods, GoodsLunchDto.class);
		}
		return null;
	}

	@Override
	public ResponseResult<List<GoodsLunchDto>> getInfoByName(String name) {
		List<GoodsLunchDto> list = new ArrayList<GoodsLunchDto>();
		if(StringUtils.isBlank(name)) {
			return ResponseResult.buildSuccessResponse(list);
		}
		GoodsLunchExample goodsExample = new GoodsLunchExample();
		goodsExample.createCriteria().andNameLike("%"+name+"%")
		.andStatusEqualTo(GoodsLunchStatusEnum.STATUS_USEING.getValue());
		List<GoodsLunch> goodsInfo = goodsLunchMapper.selectByExample(goodsExample);
		if(CollectionUtils.isNotEmpty(goodsInfo)) {
			list = BeanMapper.mapList(goodsInfo, GoodsLunchDto.class);
		}
		return ResponseResult.buildSuccessResponse(list);
	}

}
