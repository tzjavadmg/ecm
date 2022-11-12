package com.milisong.breakfast.scm.goods.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.milisong.breakfast.scm.goods.api.GoodsCategoryService;
import com.milisong.breakfast.scm.goods.constant.GoodsCategoryStatusEnum;
import com.milisong.breakfast.scm.goods.constant.GoodsCategoryTypeEnum;
import com.milisong.breakfast.scm.goods.constant.GoodsStatusEnum;
import com.milisong.breakfast.scm.goods.domain.GoodsCategory;
import com.milisong.breakfast.scm.goods.domain.GoodsCategoryExample;
import com.milisong.breakfast.scm.goods.domain.GoodsExample.Criteria;
import com.milisong.breakfast.scm.goods.dto.GoodsCategoryInfoDto;
import com.milisong.breakfast.scm.goods.mapper.GoodsCategoryMapper;
import com.milisong.breakfast.scm.goods.param.GoodsCategoryParam;
import com.milisong.breakfast.scm.goods.param.GoodsCategoryQueryParam;
import com.milisong.breakfast.scm.goods.task.GoodsTask;
import com.milisong.breakfast.scm.goods.utils.GoodsUtil;

import lombok.extern.slf4j.Slf4j;

/**
*@author    created by benny
*@date  2018年12月7日---上午12:30:50
*
*/
@Service
@Slf4j
@RefreshScope
public class GoodsCategoryServiceImpl implements GoodsCategoryService {

	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;
	
	@Resource
    private SystemProperties systemProperties;
	
	@Autowired
	private GoodsTask goodsTask;
	@Override
	public ResponseResult<List<GoodsCategoryInfoDto>> getAll(byte type) {
		log.info("查询所有类目信息,类目类型{}",type);
		String picViewUrl = systemProperties.getFileOss().getViewUrl();
		GoodsCategoryExample goodsCategoryExample = new GoodsCategoryExample();
		goodsCategoryExample.setOrderByClause(" weight asc ");
		List<GoodsCategoryInfoDto> listGoodsCategoryInfoDto = new ArrayList<GoodsCategoryInfoDto>();
		goodsCategoryExample.createCriteria().andIsDeletedEqualTo(false).andTypeEqualTo(type).andStatusEqualTo((byte)1);
		List<GoodsCategory> listGoodsCategory = goodsCategoryMapper.selectByExample(goodsCategoryExample);
		if(CollectionUtils.isNotEmpty(listGoodsCategory)) {
			listGoodsCategoryInfoDto = BeanMapper.mapList(listGoodsCategory, GoodsCategoryInfoDto.class);
			for (GoodsCategory goodsCategory : listGoodsCategory) {
				goodsCategory.setPicture(picViewUrl.concat("/6/").concat(goodsCategory.getPicture()));
			}
		}
		return ResponseResult.buildSuccessResponse(listGoodsCategoryInfoDto);
	}

	private List<GoodsCategory> checkCategoryName(String name) {
		GoodsCategoryExample example = new GoodsCategoryExample();
		com.milisong.breakfast.scm.goods.domain.GoodsCategoryExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name).andIsDeletedEqualTo(false); // false 为 0;
		log.info("{}", JSONObject.toJSONString(example.getOredCriteria()));
		return goodsCategoryMapper.selectByExample(example);
	}
	
	@Override
	public ResponseResult<String> saveGoodsCategory(GoodsCategoryParam goodsCategoryParam) {
		log.info("类目信息变更:{}",JSON.toJSONString(goodsCategoryParam));
		if (goodsCategoryParam == null) {
			log.info("保存信息为空");
			return ResponseResult.buildFailResponse("20001", "参数校验错误");
		}
		RLock lock = LockProvider.getLock("SAVE_GOODSCATEGORY_" + goodsCategoryParam.getCode());
		try {
			lock.lock(3, TimeUnit.SECONDS);
			GoodsCategory goodsCategory = BeanMapper.map(goodsCategoryParam, GoodsCategory.class);
			if(goodsCategory.getId() == null) {
				//新增类目
				GoodsCategoryExample example = new GoodsCategoryExample();
				com.milisong.breakfast.scm.goods.domain.GoodsCategoryExample.Criteria criteria = example.createCriteria();
//				criteria.andNameEqualTo(goodsCategory.getName()).andIsDeletedEqualTo(false); // false 为 0;
//				log.info("{}", JSONObject.toJSONString(example.getOredCriteria()));
//				List<GoodsCategory> goodsCategoryList =  goodsCategoryMapper.selectByExample(example);
				List<GoodsCategory> goodsCategoryList = checkCategoryName(goodsCategory.getName());
				if (CollectionUtils.isNotEmpty(goodsCategoryList)) {
					log.info("类目名称已存在:{}",JSON.toJSONString(goodsCategoryList));
					return ResponseResult.buildFailResponse("10002", "类目名称已存在");
				}
				
				String goodsCategoryCode = "";
				do {
					example.clear();
					criteria = example.createCriteria();
					goodsCategoryCode = GoodsUtil.generateGoodsCategroyCode();
					criteria.andCodeEqualTo(goodsCategoryCode);
					criteria.andIsDeletedEqualTo(false); // false 为 0
					goodsCategoryList = goodsCategoryMapper.selectByExample(example);
				} while (CollectionUtils.isNotEmpty(goodsCategoryList));
				goodsCategory.setCreateBy(goodsCategory.getUpdateBy());
				goodsCategory.setId(IdGenerator.nextId());
				goodsCategory.setCode(goodsCategoryCode);
				log.info("新增类目：{}",JSON.toJSONString(goodsCategory));
				goodsCategoryMapper.insertSelective(goodsCategory);
			}else {
				//修改类目
				GoodsCategory goodsCategoryDb = goodsCategoryMapper.selectByPrimaryKey(goodsCategory.getId());
				log.info("变更前的类目信息{}",JSON.toJSONString(goodsCategoryDb));
				List<GoodsCategory> goodsCategoryList = checkCategoryName(goodsCategory.getName());
				if (CollectionUtils.isNotEmpty(goodsCategoryList)) {
					if(!goodsCategoryList.get(0).getId().equals(goodsCategory.getId())) {
						log.info("类目名称已存在:{}",JSON.toJSONString(goodsCategoryList));
						return ResponseResult.buildFailResponse("10002", "类目名称已存在");
					}
				}
				goodsCategoryDb.setPicture(goodsCategory.getPicture());
				goodsCategoryDb.setName(goodsCategory.getName());
				goodsCategoryDb.setStatus(goodsCategory.getStatus());
				goodsCategoryDb.setDescribe(goodsCategory.getDescribe());
				goodsCategoryDb.setWeight(goodsCategory.getWeight());
				log.info("变更后的类目信息{}",JSON.toJSONString(goodsCategoryDb));
				int row = goodsCategoryMapper.updateByPrimaryKeySelective(goodsCategoryDb);
				log.info("类目信息变更结果{}",row);
			}
			//发送MQ
			goodsTask.categoryMqSync();
			return ResponseResult.buildSuccessResponse("");
		} catch (Exception e) {
			log.error("新增/编辑 类目信息出现异常{}",e);
		}finally {
			lock.unlock();
		}
		return ResponseResult.buildFailResponse();
	}

	@Override
	public ResponseResult<Pagination<GoodsCategoryInfoDto>> getGoodsCategoryPageInfo(GoodsCategoryQueryParam param) {
		Pagination<GoodsCategoryInfoDto> pagination = new Pagination<GoodsCategoryInfoDto>();
		GoodsCategoryExample example = new GoodsCategoryExample();
		example.setPageSize(param.getPageSize());
		example.setStartRow(param.getStartRow());
		com.milisong.breakfast.scm.goods.domain.GoodsCategoryExample.Criteria goodsCategoryCriteria = example.createCriteria();
		if(StringUtils.isNotBlank(param.getName())) {
			goodsCategoryCriteria.andNameLike("%"+param.getName()+"%");
		}
		if(null != param.getStatus()) {
			goodsCategoryCriteria.andStatusEqualTo(param.getStatus());
		}
		Long count = goodsCategoryMapper.countByExample(example);
		pagination.setTotalCount(count);
		if(count > 0) {
			example.setOrderByClause(" weight desc ");
			List<GoodsCategory> list = goodsCategoryMapper.selectByExample(example);
			if(CollectionUtils.isNotEmpty(list)) {
				List<GoodsCategoryInfoDto> listInfo = BeanMapper.mapList(list, GoodsCategoryInfoDto.class);
				listInfo.forEach(item -> {
					item.setStatusStr(GoodsCategoryStatusEnum.getNameByStatus(item.getStatus()));
					item.setPicture(systemProperties.getFileOss().getViewUrl() + "/6/" +item.getPicture());
					item.setTypeStr(GoodsCategoryTypeEnum.getNameByStatus(item.getType()));
				});
				pagination.setDataList(listInfo);
			}
		}
		return ResponseResult.buildSuccessResponse(pagination);
	}

	@Override
	public ResponseResult<GoodsCategoryInfoDto> getGoodsCategoryById(Long id) {
		GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(id);
		GoodsCategoryInfoDto goodsCategroyInfo = new GoodsCategoryInfoDto();
		if(goodsCategory != null) {
			goodsCategroyInfo = BeanMapper.map(goodsCategory, GoodsCategoryInfoDto.class);
			goodsCategroyInfo.setPictureView(systemProperties.getFileOss().getViewUrl() + "/6/" +goodsCategroyInfo.getPicture());
		}
		return ResponseResult.buildSuccessResponse(goodsCategroyInfo);
	}

}
