package com.milisong.scm.shop.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.base.dto.ShopReqDto;
import com.milisong.scm.shop.api.GlobalConfigService;
import com.milisong.scm.shop.api.ShopInterceptService;
import com.milisong.scm.shop.constant.ConfigConstant;
import com.milisong.scm.shop.constant.ConfigEnum;
import com.milisong.scm.shop.domain.ShopInterceptConfig;
import com.milisong.scm.shop.dto.config.ConfigDto;
import com.milisong.scm.shop.dto.config.ShopInterceptConfigDto;
import com.milisong.scm.shop.dto.config.ShopInterceptConfigMQDto;
import com.milisong.scm.shop.dto.config.ShopInterceptConfigParam;
import com.milisong.scm.shop.mapper.ShopInterceptConfigExtMapper;
import com.milisong.scm.shop.mapper.ShopInterceptConfigMapper;
import com.milisong.scm.shop.util.MqProducerUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/23   18:26
 *    desc    : 门店截单配置实现类
 *    version : v1.0
 * </pre>
 */
@Slf4j
@Service("shopInterceptService")
public class ShopInterceptServiceImpl implements ShopInterceptService {

    @Autowired
    private ShopInterceptConfigMapper shopInterceptConfigMapper;

    @Autowired
    private ShopInterceptConfigExtMapper shopInterceptConfigExtMapper;

    @Autowired
    private ShopService shopService;

    @Autowired
    private GlobalConfigService globalConfigService;

    private static final String format = "HH:mm";
//    private static final SimpleDateFormat format = new SimpleDateFormat("HH:mm");

    @Override
    public ResponseResult<ShopInterceptConfigDto> saveOrUpdate(ShopInterceptConfigDto dto) {
    	log.info("保存截单配置信息：{}", JSONObject.toJSONString(dto));
    	if(null == dto.getIsDeleted()) {
    		dto.setIsDeleted(Boolean.FALSE);
    	}
        if(dto.getIsDeleted()){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_INTERCEPT_NOT_DELETE.getCode(),ConfigEnum.SHOP_INTERCEPT_NOT_DELETE.getDesc());
        }
        if(dto.getShopId()!=null){
        	ResponseResult<ShopDto> shopResp = shopService.queryById(dto.getShopId());
        	if(null == shopResp || !shopResp.isSuccess() || null==shopResp.getData()) {
        		return ResponseResult.buildFailResponse(ConfigEnum.SHOP_NOT_FOUND.getCode(),ConfigEnum.SHOP_NOT_FOUND.getDesc());
        	}
        	ShopDto shop = shopResp.getData();
            dto.setShopCode(shop.getCode());
            dto.setShopName(shop.getName());
        }
        if(dto.getFirstOrdersetTime() != null){
            dto.setSecondOrdersetTime(DateUtils.addMinutes(dto.getFirstOrdersetTime(),getSpaceTime()));
        }
        ShopInterceptConfig shopInterceptConfig = BeanMapper.map(dto, ShopInterceptConfig.class);
        if(shopInterceptConfig.getId() == null){
            ResponseResult<ShopInterceptConfigDto> checkResult = checkParamForSave(dto);
            if(!checkResult.isSuccess()){
                return checkResult;
            }
            shopInterceptConfig.setId(IdGenerator.nextId());
            dealChangeIsDefault(dto);
            if(!checkInterceptTime(dto)){
                return ResponseResult.buildFailResponse(ConfigEnum.SHOP_INTERCEPT_EXISTS.getCode(),ConfigEnum.SHOP_INTERCEPT_EXISTS.getDesc());
            }
            shopInterceptConfigMapper.insertSelective(shopInterceptConfig);
        }else if(dto.getIsDeleted()!=null && dto.getIsDeleted()){
            ShopInterceptConfig configDto = new ShopInterceptConfig();
            configDto.setId(dto.getId());
            configDto.setIsDeleted(true);
            shopInterceptConfigMapper.updateByPrimaryKeySelective(configDto);
        }else{
            ResponseResult<ShopInterceptConfigDto> checkResult = checkParamForUpdate(dto);
            if(!checkResult.isSuccess()){
                return checkResult;
            }
            dealChangeIsDefault(dto);
            if(!checkInterceptTime(dto)){
                return ResponseResult.buildFailResponse(ConfigEnum.SHOP_INTERCEPT_EXISTS.getCode(),ConfigEnum.SHOP_INTERCEPT_EXISTS.getDesc());
            }
            shopInterceptConfigMapper.updateByPrimaryKeySelective(shopInterceptConfig);
        }
        ShopInterceptConfig config = shopInterceptConfigMapper.selectByPrimaryKey(shopInterceptConfig.getId());
        ShopInterceptConfigDto shopInterceptConfigDto = BeanMapper.map(config, ShopInterceptConfigDto.class);
        dealFieldSendMq(config);
        return ResponseResult.buildSuccessResponse(shopInterceptConfigDto);
    }

    private Integer getSpaceTime() {
        String spaceTime = globalConfigService.getGloableConfigByKey("spaceTime");
        if(StringUtils.isBlank(spaceTime)){
            return 5;
        }else{
            return Integer.parseInt(spaceTime);
        }
    }
    private boolean checkInterceptTime(ShopInterceptConfigDto dto){
        if(dto.getOrderInterceptTime() == null){
            return true;
        }
        List<ShopInterceptConfig> list = shopInterceptConfigExtMapper.queryByShopId(dto.getShopId());
        for (ShopInterceptConfig o:list) {
            if(o.getOrderInterceptTime().equals(dto.getOrderInterceptTime())){
                if(dto.getId() == null){
                    return false;
                }else if(!dto.getId().equals(o.getId())){
                    return false;
                }
            }
        }
        return true;

    }
    private void dealChangeIsDefault(ShopInterceptConfigDto dto) {
        Boolean isDefault = dto.getIsDefault();
        if(isDefault==null || !isDefault){
            return;
        }
        ShopInterceptConfig aDefault = getDefault(dto.getShopId());
        if(aDefault == null){
            return;
        }
        if(dto.getId() == null || !aDefault.getId().equals(dto.getId())){
            //新增时，设置为默认，自动将其他处理为非默认
            ShopInterceptConfig shopInterceptConfig = new ShopInterceptConfig();
            shopInterceptConfig.setId(aDefault.getId());
            shopInterceptConfig.setIsDefault(false);
            shopInterceptConfigMapper.updateByPrimaryKeySelective(shopInterceptConfig);
        }
    }

    @Override
    public ResponseResult<Pagination<ShopInterceptConfigDto>> getInterceptorConfig(ShopInterceptConfigParam param) {
        Pagination<ShopInterceptConfigDto> listPagination = new Pagination<>();
        int count = shopInterceptConfigExtMapper.getInterceptorConfigCount(param);
        if(count == 0){
            return ResponseResult.buildSuccessResponse(listPagination);
        }
        List<ShopInterceptConfig> listDtos = shopInterceptConfigExtMapper.getInterceptorConfig(param);
        List<ShopInterceptConfigDto> interceptConfigDtos = BeanMapper.mapList(listDtos, ShopInterceptConfigDto.class);

        listPagination.setTotalCount(count);
        listPagination.setDataList(interceptConfigDtos);
        return ResponseResult.buildSuccessResponse(listPagination);
    }

    @Override
    public ResponseResult<ShopInterceptConfigDto> getShopInterceptorById(Long id) {
        ShopInterceptConfig interceptConfig = shopInterceptConfigMapper.selectByPrimaryKey(id);
        ShopInterceptConfigDto map = BeanMapper.map(interceptConfig, ShopInterceptConfigDto.class);
        return ResponseResult.buildSuccessResponse(map);
    }

    @Override
    public List<ShopInterceptConfigDto> getInterceptorConfigByShopId(Long shopId) {
        List<ShopInterceptConfig> list = shopInterceptConfigExtMapper.queryByShopId(shopId);
        return BeanMapper.mapList(list,ShopInterceptConfigDto.class);
    }

    @Override
    public ResponseResult<Boolean> copyConfig(Long srcId,  ShopReqDto shopReqDto) {
        List<ShopInterceptConfig> list = shopInterceptConfigExtMapper.queryByShopId(srcId);
        if(list == null || list.size() == 0){
            return ResponseResult.buildSuccessResponse(true);
        }
        list.stream().forEach(o->{
            ShopInterceptConfig shopInterceptConfig = new ShopInterceptConfig();
            shopInterceptConfig.setId(IdGenerator.nextId());
            shopInterceptConfig.setOrderInterceptTime(o.getOrderInterceptTime());
            shopInterceptConfig.setFirstOrdersetTime(o.getFirstOrdersetTime());
            shopInterceptConfig.setSecondOrdersetTime(o.getSecondOrdersetTime());
            shopInterceptConfig.setDeliveryTimeBegin(o.getDeliveryTimeBegin());
            shopInterceptConfig.setDeliveryTimeEnd(o.getDeliveryTimeEnd());
            shopInterceptConfig.setMaxOutput(o.getMaxOutput());
            shopInterceptConfig.setIsDefault(o.getIsDefault());
            shopInterceptConfig.setIsDeleted(o.getIsDeleted());
            shopInterceptConfig.setStatus(o.getStatus());
            shopInterceptConfig.setShopId(shopReqDto.getId());
            shopInterceptConfig.setShopCode(shopReqDto.getCode());
            shopInterceptConfig.setShopName(shopReqDto.getName());
            shopInterceptConfig.setCreateBy(shopReqDto.getCreateBy());
            shopInterceptConfigMapper.insertSelective(shopInterceptConfig);
        });
        return ResponseResult.buildSuccessResponse(true);
    }

    @Override
    public ResponseResult<Boolean> syncShopConfig(Long shopId) {
        ShopInterceptConfig shopInterceptConfig = new ShopInterceptConfig();
        shopInterceptConfig.setShopId(shopId);
        dealFieldSendMq(shopInterceptConfig);
        return ResponseResult.buildSuccessResponse();
    }

    private List<ShopInterceptConfigDto> getInterceptorByShopId(Long shopId) {
        List<ShopInterceptConfig> shopInterceptConfig = shopInterceptConfigExtMapper.queryByShopId(shopId);
        if(shopInterceptConfig == null ){
            return null;
        }
        List<ShopInterceptConfigDto> shopInterceptConfigDtos = BeanMapper.mapList(shopInterceptConfig, ShopInterceptConfigDto.class);

        return shopInterceptConfigDtos;
    }

	private void dealFieldSendMq(ShopInterceptConfig config){
        Long shopId = config.getShopId();
        List<ShopInterceptConfigDto> list = getInterceptorByShopId(shopId);
        if( list==null || list.size() == 0){
            return;
        }
        list.sort(Comparator.comparing(ShopInterceptConfigDto::getOrderInterceptTime));
        ArrayList<ShopInterceptConfigMQDto> arrayList = new ArrayList<>();
        list.stream().forEach(o->{
            ShopInterceptConfigMQDto dto = new ShopInterceptConfigMQDto();
            dto.setId(o.getId());
            dto.setShopId(o.getShopId());
            dto.setShopCode(o.getShopCode());
            dto.setDispatchTime(DateFormatUtils.format(o.getDispatchTime(),format));
            dto.setStartTime(DateFormatUtils.format(o.getDeliveryTimeBegin(),format));
            dto.setEndTime(DateFormatUtils.format(o.getDeliveryTimeEnd(),format));
            dto.setCutOffTime(DateFormatUtils.format(o.getOrderInterceptTime(),format));
            dto.setMaxCapacity(o.getMaxOutput());
            dto.setIsDefault(o.getIsDefault());
            dto.setShopName(o.getShopName());
            dto.setFirstOrdersetTime(DateFormatUtils.format(o.getFirstOrdersetTime(),format));
            dto.setSecondOrdersetTime(DateFormatUtils.format(o.getSecondOrdersetTime(),format));
            arrayList.add(dto);
        });

        ConfigDto<List<ShopInterceptConfigMQDto>> configDto = new ConfigDto<>();
        configDto.setType(ConfigConstant.INTERCEPT_CONFIG.getValue());
        configDto.setData(arrayList);
        MqProducerUtil.sendConfigInterceptMsg(configDto);
    }

    private ResponseResult<ShopInterceptConfigDto> checkParamForUpdate(ShopInterceptConfigDto dto) {
        if(dto.getShopId() == null){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_INTERCEPT_SHOPID_BLANK.getCode(),ConfigEnum.SHOP_INTERCEPT_SHOPID_BLANK.getDesc());
        }
        if(dto.getMaxOutput()!=null && dto.getMaxOutput() <=0){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_MAX_OUTPUT_NOT_ZERO.getCode(),ConfigEnum.SHOP_MAX_OUTPUT_NOT_ZERO.getDesc());
        }
        if(dto.getOrderInterceptTime()!=null
                &&dto.getFirstOrdersetTime()!=null
                &&dto.getOrderInterceptTime().after(dto.getFirstOrdersetTime())){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_INTERCEPT_FIRST_ORDERSET_ERROR.getCode(),ConfigEnum.SHOP_INTERCEPT_FIRST_ORDERSET_ERROR.getDesc());
        }
        if(dto.getSecondOrdersetTime()!=null
                &&dto.getFirstOrdersetTime()!=null
                && !dto.getSecondOrdersetTime().after(dto.getFirstOrdersetTime())){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_FIRST_SECOND_ORDERSET_ERROR.getCode(),ConfigEnum.SHOP_FIRST_SECOND_ORDERSET_ERROR.getDesc());
        }
        if(dto.getDeliveryTimeEnd()!=null
                &&dto.getDeliveryTimeBegin()!=null
                &&!dto.getDeliveryTimeEnd().after(dto.getDeliveryTimeBegin())){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_BEGIN_END_ERROR.getCode(),ConfigEnum.SHOP_BEGIN_END_ERROR.getDesc());
        }
        if(!checkDeliveryTimeDouble(dto)){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_BEGIN_END_CONFLICT.getCode(),ConfigEnum.SHOP_BEGIN_END_CONFLICT.getDesc());
        }
        return ResponseResult.buildSuccessResponse();
    }

    private ResponseResult<ShopInterceptConfigDto> checkParamForSave(ShopInterceptConfigDto dto) {
        if(dto.getShopId() == null){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_INTERCEPT_SHOPID_BLANK.getCode(),ConfigEnum.SHOP_INTERCEPT_SHOPID_BLANK.getDesc());
        }
        if(dto.getOrderInterceptTime() == null){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_INTERCEPT_SHOPID_BLANK.getCode(),ConfigEnum.SHOP_INTERCEPT_SHOPID_BLANK.getDesc());
        }
        if(dto.getFirstOrdersetTime() == null){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_ORDERSET_FIRST_TIME_BLANK.getCode(),ConfigEnum.SHOP_ORDERSET_FIRST_TIME_BLANK.getDesc());
        }
        if(dto.getDispatchTime() == null){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_DISPATCH_TIME_INVALID.getCode(),ConfigEnum.SHOP_DISPATCH_TIME_INVALID.getDesc());
        }
        if(dto.getDeliveryTimeBegin() == null){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_DELIVERY_TIME_BEGIN_BLANK.getCode(),ConfigEnum.SHOP_DELIVERY_TIME_BEGIN_BLANK.getDesc());
        }
        if(dto.getDeliveryTimeEnd() == null){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_DELIVERY_TIME_END_BLANK.getCode(),ConfigEnum.SHOP_DELIVERY_TIME_END_BLANK.getDesc());
        }
        if(dto.getMaxOutput() == null){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_MAX_OUTPUT_BLANK.getCode(),ConfigEnum.SHOP_MAX_OUTPUT_BLANK.getDesc());
        }
        if(dto.getMaxOutput()!=null && dto.getMaxOutput() <=0){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_MAX_OUTPUT_NOT_ZERO.getCode(),ConfigEnum.SHOP_MAX_OUTPUT_NOT_ZERO.getDesc());
        }
        /*if(dto.getIsDefault() == null){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_IS_DEFAULT_BLANK.getCode(),ConfigEnum.SHOP_IS_DEFAULT_BLANK.getDesc());
        }*/
        if(dto.getOrderInterceptTime().after(dto.getFirstOrdersetTime())){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_INTERCEPT_FIRST_ORDERSET_ERROR.getCode(),ConfigEnum.SHOP_INTERCEPT_FIRST_ORDERSET_ERROR.getDesc());
        }
        if(dto.getSecondOrdersetTime()!=null && !dto.getSecondOrdersetTime().after(dto.getFirstOrdersetTime())){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_FIRST_SECOND_ORDERSET_ERROR.getCode(),ConfigEnum.SHOP_FIRST_SECOND_ORDERSET_ERROR.getDesc());
        }
        if(!dto.getDeliveryTimeEnd().after(dto.getDeliveryTimeBegin())){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_BEGIN_END_ERROR.getCode(),ConfigEnum.SHOP_BEGIN_END_ERROR.getDesc());
        }
        if(!checkDeliveryTimeDouble(dto)){
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_BEGIN_END_CONFLICT.getCode(),ConfigEnum.SHOP_BEGIN_END_CONFLICT.getDesc());
        }
        return ResponseResult.buildSuccessResponse();
    }

    private ShopInterceptConfig getDefault(Long shopId) {
            List<ShopInterceptConfig> list = shopInterceptConfigExtMapper.queryByShopId(shopId);
            for (ShopInterceptConfig s:list) {
                if(s.getIsDefault()){
                    return s;
                }
            }
        return null;
    }

    /**
     * 校验配送时间是否冲突，完全一样
     * @param dto
     * @return true-合法，false-非法
     */
    private boolean checkDeliveryTimeDouble(ShopInterceptConfigDto dto){
        String strOld = DateFormatUtils.format(dto.getDeliveryTimeBegin(),format).concat("-").concat(DateFormatUtils.format(dto.getDeliveryTimeEnd(),format));
        List<ShopInterceptConfig> list = shopInterceptConfigExtMapper.queryByShopId(dto.getShopId());
        for (ShopInterceptConfig config:list) {
            String strNew = DateFormatUtils.format(config.getDeliveryTimeBegin(),format).concat("-").concat(DateFormatUtils.format(config.getDeliveryTimeEnd(),format));
            if(strNew.equals(strOld)){
                if(dto.getId() == null){
                    return false;
                }else if(!dto.getId().equals(config.getId())){
                    return false;
                }else{
                    return true;
                }
            }
        }
        return true;
    }
}
