package com.milisong.scm.shop.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.base.dto.ShopReqDto;
import com.milisong.scm.shop.api.ShopBannerService;
import com.milisong.scm.shop.constant.ConfigConstant;
import com.milisong.scm.shop.constant.ConfigEnum;
import com.milisong.scm.shop.domain.ShopBanner;
import com.milisong.scm.shop.dto.config.ConfigDto;
import com.milisong.scm.shop.dto.config.ShopBannerDto;
import com.milisong.scm.shop.dto.config.ShopBannerParam;
import com.milisong.scm.shop.mapper.ShopBannerExtMapper;
import com.milisong.scm.shop.mapper.ShopBannerMapper;
import com.milisong.scm.shop.util.MqProducerUtil;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/23   18:09
 *    desc    : 门店轮播图业务实现类
 *    version : v1.0
 * </pre>
 */
@Service("shopBannerService")
public class ShopBannerServiceImpl implements ShopBannerService {

    @Autowired
    private ShopBannerMapper shopBannerMapper;

    @Autowired
    private ShopBannerExtMapper shopBannerExtMapper;

    @Autowired
    private ShopService shopService;

    @Value("${file.oss.viewUrl}")
    private String picViewUrl;

    @Override
    public ResponseResult<ShopBannerDto> saveOrUpdate(ShopBannerDto dto) {
        if(dto.getShopId()!=null){
        	ResponseResult<ShopDto> shopResp = shopService.queryById(dto.getShopId());
        	if(null == shopResp || !shopResp.isSuccess() || null==shopResp.getData()) {
        		return ResponseResult.buildFailResponse(ConfigEnum.SHOP_NOT_FOUND.getCode(),ConfigEnum.SHOP_NOT_FOUND.getDesc());
        	}
        	ShopDto shop = shopResp.getData();
            dto.setShopCode(shop.getCode());
            dto.setShopName(shop.getName());
        }
        ShopBanner shopBanner = BeanMapper.map(dto, ShopBanner.class);
        if(shopBanner.getId() == null){
            ResponseResult<ShopBannerDto> checkResult = checkParamForSave(dto);
            if(!checkResult.isSuccess()){
                return checkResult;
            }
            shopBanner.setId(IdGenerator.nextId());
            shopBannerMapper.insertSelective(shopBanner);
        }else{
            ResponseResult<ShopBannerDto> checkResult = checkParamForUpdate(dto);
            if(!checkResult.isSuccess()){
                return checkResult;
            }
            shopBannerExtMapper.updateByPrimaryKeySelectiveClearLinKUrl(shopBanner);
        }
        ShopBanner banner = shopBannerMapper.selectByPrimaryKey(shopBanner.getId());
        dealFieldSendMq(banner);
        ShopBannerDto map = BeanMapper.map(banner, ShopBannerDto.class);
        dealPicUrl2View(map);
        return ResponseResult.buildSuccessResponse(map);
    }

    @Override
    public ResponseResult<Pagination<ShopBannerDto>> getShopBanner(ShopBannerParam param) {
        Pagination<ShopBannerDto> pagination = new Pagination<>();
        int count = shopBannerExtMapper.getShopIdsCount(param);
        pagination.setTotalCount(count);
        if(count == 0){
            return ResponseResult.buildSuccessResponse(pagination);
        }
        List<Long> list = shopBannerExtMapper.getShopIds(param);
        String ids = "'"+StringUtils.join(list,"','")+"'";
        List<ShopBanner> shopBanners = shopBannerExtMapper.getShopBannerByIds(ids);
        List<ShopBannerDto> bannerDtos = BeanMapper.mapList(shopBanners, ShopBannerDto.class);
        bannerDtos.sort(Comparator.comparingLong(ShopBannerDto::getShopId));
        bannerDtos.stream().forEach(o-> dealPicUrl2View(o));
        pagination.setDataList(bannerDtos);
        return ResponseResult.buildSuccessResponse(pagination);
    }

    @Override
    public ResponseResult<ShopBannerDto> getShopBannerById(Long id) {
        ShopBanner shopBanner = shopBannerMapper.selectByPrimaryKey(id);
        ShopBannerDto map = BeanMapper.map(shopBanner, ShopBannerDto.class);
        dealPicUrl2View(map);
        return ResponseResult.buildSuccessResponse(map);
    }

    @Override
    public List<ShopBannerDto> getShopBannerByShopId(Long shopId) {
        List<ShopBanner> result = shopBannerExtMapper.queryByShopId(shopId);
        if(result == null || result.size() == 0){
            return null;
        }
        return BeanMapper.mapList(result,ShopBannerDto.class);
    }

    @Override
    public ResponseResult<Boolean> copyConfig(Long srcId, ShopReqDto shopReqDto) {
        List<ShopBanner> list = shopBannerExtMapper.queryByShopId(srcId);
        if(list == null || list.size() == 0){
            return ResponseResult.buildSuccessResponse(true);
        }
        list.stream().forEach(o->{
            ShopBanner shopBanner = new ShopBanner();
            shopBanner.setId(IdGenerator.nextId());
            shopBanner.setIsDeleted(o.getIsDeleted());
            shopBanner.setLinkUrl(o.getLinkUrl());
            shopBanner.setPicture(o.getPicture());
            shopBanner.setWeight(o.getWeight());
            shopBanner.setCreateBy(shopReqDto.getCreateBy());
            shopBanner.setShopId(shopReqDto.getId());
            shopBanner.setShopCode(shopReqDto.getCode());
            shopBanner.setShopName(shopReqDto.getName());
            shopBannerMapper.insertSelective(shopBanner);
        });
        return ResponseResult.buildSuccessResponse(true);
    }

    @Override
    public ResponseResult<Boolean> syncShopConfig(Long shopId) {
        ShopBanner shopBanner = new ShopBanner();
        shopBanner.setShopId(shopId);
        dealFieldSendMq(shopBanner);
        return ResponseResult.buildSuccessResponse();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void dealFieldSendMq(ShopBanner config){
        List<ShopBanner> list = shopBannerExtMapper.queryByShopId(config.getShopId());
        if(list == null || list.size() == 0){
            return;
        }
        List<Map<String, Object>> arrayList = new ArrayList<>();
        list.stream().forEach(o->{
            HashMap map = new HashMap<String,Object>();
            map.put("shopCode",o.getShopCode());
            map.put("picture",dealPicUrl2View(o.getPicture()));
            map.put("linkUrl",o.getLinkUrl());
            map.put("weight",o.getWeight());
            arrayList.add(map);
        });
        ConfigDto<List<Map<String, Object>>> configDto = new ConfigDto<>();
        configDto.setType(ConfigConstant.BANNER_CONFIG.getValue());
        configDto.setData(arrayList);
        MqProducerUtil.sendConfigBannerMsg(configDto);
    }

    private ResponseResult<ShopBannerDto> checkParamForUpdate(ShopBannerDto dto) {
        if(dto.getShopId() == null){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_BANNER_SHOP_MSG_BLANK.getCode(),ConfigEnum.SHOP_BANNER_SHOP_MSG_BLANK.getDesc());
        }
        if(dto.getPicture()!=null && dto.getPicture().length() == 0){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_BANNER_PICTURE_BLANK.getCode(),ConfigEnum.SHOP_BANNER_PICTURE_BLANK.getDesc());
        }
        /*if(dto.getLinkUrl()!=null && dto.getLinkUrl().length() == 0){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_BANNER_LINK_URL_BLANK.getCode(),ConfigEnum.SHOP_BANNER_LINK_URL_BLANK.getDesc());
        }*/
        Byte weight = dto.getWeight();
        if(weight!=null){
            List<ShopBanner> list = shopBannerExtMapper.queryByShopCode(dto.getShopCode());
            for (ShopBanner s:list) {
                if(s.getWeight().equals(weight) && !s.getId().equals(dto.getId())){
                    return ResponseResult.buildFailResponse(ConfigEnum.SHOP_BANNER_WEIGHT_EXIST.getCode(),ConfigEnum.SHOP_BANNER_WEIGHT_EXIST.getDesc());
                }
            }
        }
        return ResponseResult.buildSuccessResponse();
    }

    private ResponseResult<ShopBannerDto> checkParamForSave(ShopBannerDto dto) {
        if(dto.getShopId() == null){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_BANNER_SHOP_MSG_BLANK.getCode(),ConfigEnum.SHOP_BANNER_SHOP_MSG_BLANK.getDesc());
        }
        if(dto.getShopId() == null){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_BANNER_SHOP_MSG_BLANK.getCode(),ConfigEnum.SHOP_BANNER_SHOP_MSG_BLANK.getDesc());
        }
        if(StringUtils.isBlank(dto.getPicture())){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_BANNER_PICTURE_BLANK.getCode(),ConfigEnum.SHOP_BANNER_PICTURE_BLANK.getDesc());
        }
        /*if(StringUtils.isBlank(dto.getLinkUrl())){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_BANNER_LINK_URL_BLANK.getCode(),ConfigEnum.SHOP_BANNER_LINK_URL_BLANK.getDesc());
        }*/
        if(dto.getWeight() == null){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_BANNER_WEIGHT_BLANK.getCode(),ConfigEnum.SHOP_BANNER_WEIGHT_BLANK.getDesc());
        }
        Byte weight = dto.getWeight();
        List<ShopBanner> list = shopBannerExtMapper.queryByShopCode(dto.getShopCode());
        for (ShopBanner s:list) {
            if(s.getWeight().equals(weight)){
                return ResponseResult.buildFailResponse(ConfigEnum.SHOP_BANNER_WEIGHT_EXIST.getCode(),ConfigEnum.SHOP_BANNER_WEIGHT_EXIST.getDesc());
            }
        }
        return ResponseResult.buildSuccessResponse();
    }

    private void dealPicUrl2View(ShopBannerDto dto) {
        if(StringUtils.isBlank(dto.getPicture())){
            return;
        }
        String view = dealPicUrl2View(dto.getPicture());
        dto.setActualPicUrl(view);
    }

    private String dealPicUrl2View(String url){
        if(StringUtils.isBlank(url)){
            return null;
        }
        String httpUrl = picViewUrl;
        if(picViewUrl.endsWith("/")){
            httpUrl = picViewUrl.concat("5/");
        }else{
            httpUrl = picViewUrl.concat("/5/");
        }
        if(url.startsWith("/")){
            url = url.substring(1);
        }
        return httpUrl.concat(url);
    }
}
