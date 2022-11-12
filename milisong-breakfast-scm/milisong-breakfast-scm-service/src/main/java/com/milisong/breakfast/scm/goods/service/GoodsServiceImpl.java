package com.milisong.breakfast.scm.goods.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.distrib.LockProvider;
import com.farmland.core.util.BeanMapper;
import com.milisong.breakfast.scm.common.properties.SystemProperties;
import com.milisong.breakfast.scm.goods.api.GoodsScheduleService;
import com.milisong.breakfast.scm.goods.api.GoodsService;
import com.milisong.breakfast.scm.goods.constant.GoodsStatusEnum;
import com.milisong.breakfast.scm.goods.domain.Goods;
import com.milisong.breakfast.scm.goods.domain.GoodsCategory;
import com.milisong.breakfast.scm.goods.domain.GoodsCategoryExample;
import com.milisong.breakfast.scm.goods.domain.GoodsCombinationRef;
import com.milisong.breakfast.scm.goods.domain.GoodsCombinationRefExample;
import com.milisong.breakfast.scm.goods.domain.GoodsExample;
import com.milisong.breakfast.scm.goods.domain.GoodsExample.Criteria;
import com.milisong.breakfast.scm.goods.dto.GoodsCombinationDto;
import com.milisong.breakfast.scm.goods.dto.GoodsCombinationRefDto;
import com.milisong.breakfast.scm.goods.dto.GoodsCombinationRefMqDto;
import com.milisong.breakfast.scm.goods.dto.GoodsDto;
import com.milisong.breakfast.scm.goods.dto.GoodsInfoMqDto;
import com.milisong.breakfast.scm.goods.dto.GoodsScheduleDetailDto;
import com.milisong.breakfast.scm.goods.mapper.GoodsCombinationRefMapper;
import com.milisong.breakfast.scm.goods.mapper.GoodsMapper;
import com.milisong.breakfast.scm.goods.param.GoodsCombinationParam;
import com.milisong.breakfast.scm.goods.param.GoodsParam;
import com.milisong.breakfast.scm.goods.param.GoodsQueryParam;
import com.milisong.breakfast.scm.goods.utils.GoodsUtil;
import com.milisong.breakfast.scm.goods.utils.MqProducerGoodsUtil;

import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018年12月3日---下午2:30:25
*
*/
@Slf4j
@Service
@RefreshScope
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private GoodsMapper goodsMapper;
	
	@Autowired
	private GoodsScheduleService goodsScheduleService;
	
	@Autowired
	private GoodsCombinationRefMapper goodsCombinationRefMapper;
	
	@Resource
    private SystemProperties systemProperties;
	
	@Override
	public ResponseResult<Pagination<GoodsDto>> getGoodsPageInfo(GoodsQueryParam param) {
		Pagination<GoodsDto> pagination = new Pagination<GoodsDto>();
		GoodsExample goodsExample = new GoodsExample();
		goodsExample.setPageSize(param.getPageSize());
		goodsExample.setStartRow(param.getStartRow());
		Criteria goodsCriteria = goodsExample.createCriteria();
		if(StringUtils.isNotBlank(param.getCategoryCode())) {
			goodsCriteria.andCategoryCodeEqualTo(param.getCategoryCode());
		}
		if(StringUtils.isNotBlank(param.getCode())) {
			goodsCriteria.andCodeLike("%"+param.getCode()+"%");
		}
		if(null != param.getIsCombo()) {
			goodsCriteria.andIsComboEqualTo(param.getIsCombo());
		}
		if(StringUtils.isNotBlank(param.getName())) {
			goodsCriteria.andNameLike("%"+param.getName()+"%");
		}
		if(null != param.getStatus()) {
			goodsCriteria.andStatusEqualTo(param.getStatus());
		}
		long count = goodsMapper.countByExample(goodsExample);
		pagination.setTotalCount(count);
		if(count>0) {
			goodsExample.setPageSize(param.getPageSize());
			goodsExample.setStartRow(param.getStartRow());
			goodsExample.setOrderByClause(" weight desc ");
			List<Goods> listGoods = goodsMapper.selectByExample(goodsExample);
			if(CollectionUtils.isNotEmpty(listGoods)) {
				List<GoodsDto> listGoodsDto = BeanMapper.mapList(listGoods, GoodsDto.class);
				for (GoodsDto goodsDto : listGoodsDto) {
					goodsDto.setStatusStr(GoodsStatusEnum.getNameByStatus(goodsDto.getStatus()));
					goodsDto.setPicture(systemProperties.getFileOss().getViewUrl() + "/6/" +goodsDto.getPicture());
					goodsDto.setBigPicture(systemProperties.getFileOss().getViewUrl() + "/6/" +goodsDto.getBigPicture());
				}
				pagination.setDataList(listGoodsDto);
			}
		}
		return ResponseResult.buildSuccessResponse(pagination);
	}

	@Override
	public ResponseResult<GoodsDto> getGoodsDetailsById(Long goodsId) {
		Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
		GoodsDto goodsDto = BeanMapper.map(goods, GoodsDto.class);
		goodsDto.setBigPictureShow(systemProperties.getFileOss().getViewUrl().concat("/6/").concat(goodsDto.getBigPicture()));
		goodsDto.setPictureShow(systemProperties.getFileOss().getViewUrl().concat("/6/").concat(goodsDto.getPicture()));
		//组合商品查询组合商品信息
		if(BooleanUtils.isNotFalse(goodsDto.getIsCombo())) {
			List<GoodsCombinationRef> listGoodsCombinationRef = getGoodsCombinationRefByGoodsCode(goodsDto.getCode());
			List<String> goodsCode = listGoodsCombinationRef.stream().map(GoodsCombinationRef::getSingleCode).collect(Collectors.toList());
			List<GoodsDto> listGoodsInfoDto = getGoodsInfoByGoodsCodes(goodsCode);
			List<GoodsCombinationDto> listGoodsCombinationDto = comboGoodsInfo(listGoodsCombinationRef, listGoodsInfoDto);
			goodsDto.setListGoodsCombination(listGoodsCombinationDto);
		}
		return ResponseResult.buildSuccessResponse(goodsDto);
	}

	private List<GoodsCombinationDto> comboGoodsInfo(List<GoodsCombinationRef> listGoodsCombinationRef,List<GoodsDto> listGoodsInfoDto){
		List<GoodsCombinationDto> listGoodsCombinationDto = new ArrayList<GoodsCombinationDto>();
		if(CollectionUtils.isNotEmpty(listGoodsInfoDto)) {
			listGoodsCombinationDto = BeanMapper.mapList(listGoodsInfoDto, GoodsCombinationDto.class);
		}
		Map<String,GoodsCombinationRef> mapGoodsCombination = listGoodsCombinationRef.stream().collect(Collectors.toMap(GoodsCombinationRef::getSingleCode, a -> a,(k1,k2)->k1));
		for (GoodsCombinationDto goodsCombinationDto : listGoodsCombinationDto) {
			GoodsCombinationRef goodsCombinationRef
			= mapGoodsCombination.get(goodsCombinationDto.getCode());
			goodsCombinationDto.setGoodsCount(goodsCombinationRef.getGoodsCount());
		}
		return listGoodsCombinationDto;
	}
		
	private List<Goods> checkGoodsName(String name) {
		GoodsExample example = new GoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo(false).andStatusEqualTo(GoodsStatusEnum.STATUS_USEING.getValue()); // false 为 0
		criteria.andNameEqualTo(name);
		log.info("{}", JSONObject.toJSONString(example.getOredCriteria()));
		return goodsMapper.selectByExample(example);
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
			if(null == goods.getStatus() || 0 == goods.getStatus() ){
				log.info("状态必填");
				return ResponseResult.buildFailResponse("20001", "参数校验错误,状态不能为空");

			}
			if(StringUtils.isBlank(goods.getPicture())  || StringUtils.isBlank( goods.getBigPicture())){
				log.info("图片必传");
				return ResponseResult.buildFailResponse("20002", "参数校验错误,图片必传");
			}
			if (goods.getId() == null) {
				if(StringUtils.isBlank(goods.getCategoryCode())) {
					log.info("保存信息为空");
					return ResponseResult.buildFailResponse("20001", "参数校验错误,分类名称不能为空");
				}
				if(StringUtils.isBlank(goods.getCategoryName())) {
					log.info("保存信息为空");
					return ResponseResult.buildFailResponse("20001", "参数校验错误,分类名称不能为空");
				}
				
				
				goods.setCreateBy(goods.getUpdateBy());
				GoodsExample example = new GoodsExample();
				Criteria criteria = example.createCriteria();
//				criteria.andIsDeletedEqualTo(false); // false 为 0
//				criteria.andNameEqualTo(goods.getName());
//				log.info("{}", JSONObject.toJSONString(example.getOredCriteria()));
				List<Goods> tempList = checkGoodsName(goods.getName());
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
					tempList = goodsMapper.selectByExample(example);
				} while (CollectionUtils.isNotEmpty(tempList));
				goods.setCode(goodsCode);
				// 查询类目信息
				goodsMapper.insertSelective(goods);
				// 判断商品类型是组合还是套餐
				insertGoodsCombination(goodsParam, goods);
			} else {
				// 由于折扣金额可以修改为空，故要先将数据库里部分字段信息先查询出来
				List<Goods> tempList = checkGoodsName(goods.getName());
				if (CollectionUtils.isNotEmpty(tempList)) {
					if (!tempList.get(0).getId().equals(goods.getId())) {
						log.info("商品名称已存在");
						return ResponseResult.buildFailResponse("10001", "商品名称已存在");
					}
				}
				
				Goods dbGoods = goodsMapper.selectByPrimaryKey(goods.getId());
				log.info("变更前商品信息{}",JSON.toJSONString(dbGoods));
				goods.setCreateTime(dbGoods.getCreateTime());
				goods.setCreateBy(dbGoods.getCreateBy());
//				goods.setCategoryCode(dbGoods.getCategoryCode());
//				goods.setCategoryName(dbGoods.getCategoryName());
				goods.setIsDeleted(dbGoods.getIsDeleted());
				goods.setUpdateTime(new Date());
				goods.setIsCombo(dbGoods.getIsCombo());
				log.info("变更后商品信息{}",JSON.toJSONString(goods));
				goodsMapper.updateByPrimaryKey(goods);
			}
			//发送商品MQ
			senGoodsInfoMq(goods.getId());
			return ResponseResult.buildSuccessResponse(goods.getCode());
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 批量新增组合商品下面的子类商品
	 * @param goodsParam
	 * @param goods
	 */
	private void insertGoodsCombination(GoodsParam goodsParam, Goods goods) {
		if(BooleanUtils.isNotFalse(goods.getIsCombo())) {
			List<GoodsCombinationRef> listGoodsCombo = new ArrayList<GoodsCombinationRef>();
			List<GoodsCombinationParam> combo = goodsParam.getComboGoods();
			
			combo.forEach(item -> {
				GoodsCombinationRef goodsCombinationRef = BeanMapper.map(item, GoodsCombinationRef.class);
				goodsCombinationRef.setCombinationCode(goods.getCode());
				goodsCombinationRef.setId(IdGenerator.nextId());
				goodsCombinationRef.setCreateBy(goodsParam.getUpdateBy());
				goodsCombinationRef.setUpdateBy(goodsParam.getUpdateBy());
				listGoodsCombo.add(goodsCombinationRef);
			});
			goodsCombinationRefMapper.insertBatchSelective(listGoodsCombo);
		}
	}
	
	/**
	 * 发送MQ
	 * @param id
	 */
	@Override
	public void senGoodsInfoMq(Long id) {
		log.info("发送MQ,商品ID{}",id);
		Goods  goods = goodsMapper.selectByPrimaryKey(id);
		goods.setPicture(systemProperties.getFileOss().getViewUrl() + "/6/" +goods.getPicture());
		goods.setBigPicture(systemProperties.getFileOss().getViewUrl() + "/6/" +goods.getBigPicture());
		GoodsInfoMqDto goodsInfoMqDto = BeanMapper.map(goods, GoodsInfoMqDto.class);
		log.info("需要发送的商品信息{}",JSON.toJSONString(goodsInfoMqDto));
		if(null != goodsInfoMqDto) {
			if(BooleanUtils.isNotFalse(goodsInfoMqDto.getIsCombo())) {
				List<GoodsCombinationRef> listGoodsCombinationRef = getGoodsCombinationRefByGoodsCode(goodsInfoMqDto.getCode());
				if(!CollectionUtils.isEmpty(listGoodsCombinationRef)) {
					List<GoodsCombinationRefMqDto>  listGoodsCombinationRefMqDto = BeanMapper.mapList(listGoodsCombinationRef, GoodsCombinationRefMqDto.class);
					List<String> listSingleCode = listGoodsCombinationRefMqDto.stream().map(GoodsCombinationRefMqDto::getSingleCode).collect(Collectors.toList());
					List<GoodsDto> ListGoodsDtoInfo = getGoodsInfoByGoodsCodes(listSingleCode);
					Map<String,GoodsDto> mapGoodsInfo = ListGoodsDtoInfo.stream().collect(Collectors.toMap(GoodsDto::getCode, goodsDto -> goodsDto));
					for (GoodsCombinationRefMqDto goodsCombinationRefMqDto : listGoodsCombinationRefMqDto) {
						GoodsDto goodsDto = mapGoodsInfo.get(goodsCombinationRefMqDto.getSingleCode());
						goodsCombinationRefMqDto.setGoodsName(goodsDto.getName());
						goodsCombinationRefMqDto.setAdvisePrice(goodsDto.getAdvisePrice());
						goodsCombinationRefMqDto.setBigPicture(goodsDto.getBigPicture());
						goodsCombinationRefMqDto.setBigPictureShow(systemProperties.getFileOss().getViewUrl() + "/6/" +goods.getBigPicture());
						goodsCombinationRefMqDto.setDescribe(goodsDto.getDescribe());
						goodsCombinationRefMqDto.setDiscount(goodsDto.getDiscount());
						goodsCombinationRefMqDto.setPicture(goodsDto.getPicture());
						goodsCombinationRefMqDto.setPictureShow(systemProperties.getFileOss().getViewUrl() + "/6/" +goodsDto.getPicture());
						goodsCombinationRefMqDto.setPreferentialPrice(goodsDto.getPreferentialPrice());
						goodsCombinationRefMqDto.setSpicy(goodsDto.getSpicy());
						goodsCombinationRefMqDto.setStatus(goodsDto.getStatus());
						goodsCombinationRefMqDto.setWeight(goodsDto.getWeight());
					}
					goodsInfoMqDto.setListGoodsCombinationRefMqDto(listGoodsCombinationRefMqDto);
				}
			}
		MqProducerGoodsUtil.sendGoodsBasicMsg(goodsInfoMqDto);
		}
	}
	
	private List<GoodsCombinationRef> getGoodsCombinationRefByGoodsCode(String goodsCode){
		GoodsCombinationRefExample goodsCombinationRefExample = new GoodsCombinationRefExample();
		goodsCombinationRefExample.createCriteria().andCombinationCodeEqualTo(goodsCode);
		return goodsCombinationRefMapper.selectByExample(goodsCombinationRefExample);
	}

	@Override
	public ResponseResult<List<GoodsDto>> getGoodsBySchedule(Integer year, Integer weekOfYear, Long shopId) {
		//获取档期中的商品信息
		List<GoodsScheduleDetailDto>  listGoodsScheduleDetailsDto = goodsScheduleService.getGoodsScheduleDetailByWeekOfYear(year, weekOfYear, shopId);
		List<String> goodsCode = null;
		if(CollectionUtils.isNotEmpty(listGoodsScheduleDetailsDto)) {
			goodsCode = listGoodsScheduleDetailsDto.stream().map(GoodsScheduleDetailDto::getGoodsCode).collect(Collectors.toList());
		}
		GoodsExample goodsExample = new GoodsExample();
		Criteria  goodsCriteria = goodsExample.createCriteria().andIsDeletedEqualTo(false).
		andStatusEqualTo(GoodsStatusEnum.STATUS_USEING.getValue());
		if(CollectionUtils.isNotEmpty(goodsCode)) {
			goodsCriteria.andCodeNotIn(goodsCode);
		}
		List<Goods> listGoods = goodsMapper.selectByExample(goodsExample);
		if(CollectionUtils.isNotEmpty(listGoods)) {
			List<GoodsDto> listGoodsDto = BeanMapper.mapList(listGoods, GoodsDto.class);
			for (GoodsDto goodsDto : listGoodsDto) {
				goodsDto.setStatusStr(GoodsStatusEnum.getNameByStatus(goodsDto.getStatus()));
			}
			return ResponseResult.buildSuccessResponse(listGoodsDto);
		}
		return null;
	}
 
	@Override
	public List<GoodsDto> getGoodsInfoByGoodsCodes(List<String> goodsCode) {
		if(CollectionUtils.isEmpty(goodsCode)) {
			return null;
		}
		GoodsExample goodsExample = new GoodsExample();
		goodsExample.createCriteria().andCodeIn(goodsCode);
		List<Goods> listGoods = goodsMapper.selectByExample(goodsExample);
		if(CollectionUtils.isNotEmpty(listGoods)) {
			return BeanMapper.mapList(listGoods, GoodsDto.class);
		}
		return null;
	}

	@Override
	public ResponseResult<List<GoodsDto>> getInfoByName(String name) {
		List<GoodsDto> list = new ArrayList<GoodsDto>();
		if(StringUtils.isBlank(name)) {
			return ResponseResult.buildSuccessResponse(list);
		}
		GoodsExample goodsExample = new GoodsExample();
		goodsExample.createCriteria().andNameLike("%"+name+"%")
		.andIsComboEqualTo(false)
		.andStatusEqualTo(GoodsStatusEnum.STATUS_USEING.getValue());
		List<Goods> goodsInfo = goodsMapper.selectByExample(goodsExample);
		if(CollectionUtils.isNotEmpty(goodsInfo)) {
			list = BeanMapper.mapList(goodsInfo, GoodsDto.class);
		}
		return ResponseResult.buildSuccessResponse(list);
	}

	@Override
	public List<GoodsCombinationRefDto> getGoodsCombinationDtoByGoodsCode(Set<String> goodsCode) {
		List<GoodsCombinationRefDto> listResult = new ArrayList<GoodsCombinationRefDto>();
		if(CollectionUtils.isEmpty(goodsCode)) {
			return listResult;
		}
		List<String> listGoodsCode = new ArrayList<String>();
		listGoodsCode.addAll(goodsCode);
		GoodsCombinationRefExample goodsCombinationRefExample = new GoodsCombinationRefExample();
		goodsCombinationRefExample.createCriteria().andCombinationCodeIn(listGoodsCode);
		List<GoodsCombinationRef> listGoodsCombinationRef =	goodsCombinationRefMapper.selectByExample(goodsCombinationRefExample);
		if(CollectionUtils.isNotEmpty(listGoodsCombinationRef)) {
			listResult = BeanMapper.mapList(listGoodsCombinationRef, GoodsCombinationRefDto.class);
		}
		return listResult;
	}

}
