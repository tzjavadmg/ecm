package com.milisong.scm.base.service;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.constant.ShopResponseEnum;
import com.milisong.scm.base.constant.ShopStatusEnum;
import com.milisong.scm.base.domain.Shop;
import com.milisong.scm.base.domain.ShopExample;
import com.milisong.scm.base.dto.ShopBossDto;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.base.dto.ShopParam;
import com.milisong.scm.base.dto.ShopReqDto;
import com.milisong.scm.base.dto.mq.ShopCreateMqDto;
import com.milisong.scm.base.dto.mq.ShopMqDto;
import com.milisong.scm.base.mapper.ShopMapper;
import com.milisong.scm.base.mq.MqProducerUtil;

@Service
@RestController
public class ShopServiceImpl implements ShopService {

	@Autowired
	private ShopMapper shopMapper;
	
	@Override
	public ResponseResult<List<ShopDto>> getShopList() {
		// 查询营业中的门店信息
		ShopExample example = new ShopExample();
		example.createCriteria().andStatusEqualTo(ShopStatusEnum.SHOP_STATUS_OPEN.getValue().byteValue());
		List<Shop> list = shopMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(list)) {
			return ResponseResult.buildSuccessResponse(Collections.emptyList());
		} else {
			return ResponseResult.buildSuccessResponse(BeanMapper.mapList(list, ShopDto.class));
		}
	}

	@Override
	public ResponseResult<List<ShopBossDto>> getAllShopList() {
		ShopExample example = new ShopExample();
		List<Shop> list = shopMapper.selectByExample(example);
		if(!CollectionUtils.isEmpty(list)) {
			return ResponseResult.buildSuccessResponse(BeanMapper.mapList(list, ShopBossDto.class));
		}
		return ResponseResult.buildSuccessResponse(Collections.emptyList());
	}

	@Override
	@Transactional
	public ResponseResult<ShopDto> saveOrUpdate(@RequestBody ShopReqDto dto) {
		ResponseResult<ShopDto> resp = ResponseResult.buildSuccessResponse();
		boolean add = false;
		if(dto.getId() == null){
			//add
			dto.setId(IdGenerator.nextId());
			add = true;
			
			resp = this.add(dto);
			if(!resp.isSuccess()) {
				return resp;
			}
		}else{
			//update
			resp = this.update(dto);
			if(!resp.isSuccess()) {
				return resp;
			}
		}
		Shop shop = shopMapper.selectByPrimaryKey(dto.getId());
		ShopDto result = BeanMapper.map(shop, ShopDto.class);
		MqProducerUtil.sendShopInfoMsg(BeanMapper.map(result, ShopMqDto.class));
		if(add) {
			ShopCreateMqDto createEvent =  ShopCreateMqDto.builder()
					.id(shop.getId())
					.code(shop.getCode())
					.name(shop.getName())
					.srcId(dto.getSrcId())
					.build();
			MqProducerUtil.sendShopCreateMsg(createEvent);;
		}
		
		return ResponseResult.buildSuccessResponse(result);
	}
	
	@Override
	public ResponseResult<Pagination<ShopDto>> queryByCondition(@RequestBody ShopParam dto) {
		Pagination<ShopDto> pagination = new Pagination<>();
		ShopExample shopExample = new ShopExample();
		ShopExample.Criteria criteria = shopExample.createCriteria();
		if(dto.getIds() != null && dto.getIds().size() > 0){
			criteria.andIdIn(dto.getIds());
		}
		if(dto.getStatus()!=null){
			criteria.andStatusEqualTo(dto.getStatus());
		}
		if(!StringUtils.isBlank(dto.getName())){
			criteria.andNameLike("%".concat(dto.getName()).concat("%"));
		}
		criteria.andIsDeleteEqualTo(false);
		long count = shopMapper.countByExample(shopExample);
		if(count == 0){
			pagination.setTotalCount(0);
			return ResponseResult.buildSuccessResponse(pagination);
		}
		shopExample.setOrderByClause("create_time desc limit ".concat(String.valueOf(dto.getStartRow())).concat(",").concat(String.valueOf(dto.getPageSize())));
		List<Shop> shops = shopMapper.selectByExample(shopExample);
		pagination.setTotalCount(count);
		pagination.setDataList(BeanMapper.mapList(shops,ShopDto.class));
		return ResponseResult.buildSuccessResponse(pagination);
	}

	@Override
	public ResponseResult<ShopDto> queryById(Long id) {
		Shop shop = shopMapper.selectByPrimaryKey(id);
		if(shop == null ){
			return ResponseResult.buildSuccessResponse();
		}else{
			return ResponseResult.buildSuccessResponse(BeanMapper.map(shop,ShopDto.class));
		}
	}
	
	@Override
	public ResponseResult<ShopDto> queryByCode(String code) {
		ShopExample shopExample = new ShopExample();
		shopExample.createCriteria().andCodeEqualTo(code);
		List<Shop> shops = shopMapper.selectByExample(shopExample);
		if(CollectionUtils.isEmpty(shops)){
			return ResponseResult.buildSuccessResponse();
		}else{
			return ResponseResult.buildSuccessResponse(BeanMapper.map(shops.get(0),ShopDto.class));
		}
	}

	private String generateCode(){
		ShopExample shopExample = new ShopExample();
		shopExample.setOrderByClause("code desc limit 0,1");
		List<Shop> shops = shopMapper.selectByExample(shopExample);
		Shop shop = shops.get(0);
		String code = shop.getCode();
		Integer integer = new Integer(code);
		return Integer.toString(integer+1);
	}

	private Shop checkShopName(String name){
		ShopExample shopExample = new ShopExample();
		ShopExample.Criteria criteria = shopExample.createCriteria();
		criteria.andNameEqualTo(name);
		criteria.andIsDeleteEqualTo(false);
		List<Shop> shops = shopMapper.selectByExample(shopExample);
		if(shops!=null && shops.size() >0){
			return shops.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 新增门店
	 * @param dto
	 * @return
	 */
	private ResponseResult<ShopDto> add(ShopReqDto dto) {
		Shop shop = BeanMapper.map(dto,Shop.class);
		if(StringUtils.isBlank(shop.getName())){
			return ResponseResult.buildFailResponse(ShopResponseEnum.SHOP_NAME_BLANK.getCode(),ShopResponseEnum.SHOP_NAME_BLANK.getDesc());
		}
		if(StringUtils.isBlank(shop.getAddress())){
			return ResponseResult.buildFailResponse(ShopResponseEnum.SHOP_ADDRESS_BLANK.getCode(),ShopResponseEnum.SHOP_ADDRESS_BLANK.getDesc());
		}
		if(shop.getStatus() == null){
			return ResponseResult.buildFailResponse(ShopResponseEnum.SHOP_STATUS_BLANK.getCode(),ShopResponseEnum.SHOP_STATUS_BLANK.getDesc());
		}
		if(checkShopName(shop.getName()) !=null){
			return ResponseResult.buildFailResponse(ShopResponseEnum.SHOP_NAME_EXISTS.getCode(),ShopResponseEnum.SHOP_NAME_EXISTS.getDesc());
		}
		shop.setCode(generateCode());
		shop.setIsDelete(false);
		shopMapper.insertSelective(shop);
		return ResponseResult.buildSuccessResponse();
	}
	
	/**
	 * 修改门店
	 * @param dto
	 * @return
	 */
	private ResponseResult<ShopDto> update(ShopReqDto dto) {
		Shop shop = BeanMapper.map(dto,Shop.class);
		shop.setCode(null);
		shop.setIsDelete(null);
		if(shop.getName()!=null && shop.getName().length() == 0){
			return ResponseResult.buildFailResponse(ShopResponseEnum.SHOP_NAME_BLANK.getCode(),ShopResponseEnum.SHOP_NAME_BLANK.getDesc());
		}
		if(shop.getAddress()!=null && shop.getAddress().length() == 0){
			return ResponseResult.buildFailResponse(ShopResponseEnum.SHOP_ADDRESS_BLANK.getCode(),ShopResponseEnum.SHOP_ADDRESS_BLANK.getDesc());
		}
		Shop checkShop = checkShopName(shop.getName());
		if( checkShop!= null && !checkShop.getId().equals(dto.getId())){
			return ResponseResult.buildFailResponse(ShopResponseEnum.SHOP_NAME_EXISTS.getCode(),ShopResponseEnum.SHOP_NAME_EXISTS.getDesc());
		}
		shopMapper.updateByPrimaryKeySelective(shop);
		return ResponseResult.buildSuccessResponse();
	}
}
