package com.milisong.breakfast.scm.configuration.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.breakfast.scm.configuration.api.ShopSingleConfigService;
import com.milisong.breakfast.scm.configuration.constant.ConfigConstant;
import com.milisong.breakfast.scm.configuration.constant.ConfigEnum;
import com.milisong.breakfast.scm.configuration.domain.ShopSingleConfig;
import com.milisong.breakfast.scm.configuration.dto.ConfigDto;
import com.milisong.breakfast.scm.configuration.dto.MaxOutputDto;
import com.milisong.breakfast.scm.configuration.dto.ShopSingleConfigDto;
import com.milisong.breakfast.scm.configuration.dto.ShopSingleConfigMQDto;
import com.milisong.breakfast.scm.configuration.dto.ShopSingleConfigParam;
import com.milisong.breakfast.scm.configuration.mapper.ShopSingleConfigExtMapper;
import com.milisong.breakfast.scm.configuration.mapper.ShopSingleConfigMapper;
import com.milisong.breakfast.scm.configuration.util.MqProducerUtil;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.base.dto.ShopReqDto;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/23   18:44
 *    desc    : 单个门店配置业务实现
 *    version : v1.0
 * </pre>
 */
@Slf4j
@Service("shopSingleConfigService")
public class ShopSingleConfigServiceImpl implements ShopSingleConfigService {

    @Autowired
    private ShopSingleConfigMapper shopSingleConfigMapper;

    @Autowired
    private ShopSingleConfigExtMapper shopSingleConfigExtMapper;

    @Autowired
    private ShopService shopService;
    private static final String SPLIT_POINT = ",";

    @Override
    public ResponseResult<ShopSingleConfigDto> saveOrUpdate(ShopSingleConfigDto dto) {
        ShopSingleConfig shopSingleConfig1;
        if ("maxProduction".equals(dto.getConfigKey())) {
            int parseInt = Integer.parseInt(dto.getConfigValue());
            if (parseInt <= 0) {
                return ResponseResult.buildFailResponse(ConfigEnum.SHOP_SINGLE_MAXPRODUCTION_NOT_ZERO.getCode(), ConfigEnum.SHOP_SINGLE_MAXPRODUCTION_NOT_ZERO.getDesc());
            }
        }
        if (dto.getShopId() != null) {
        	ResponseResult<ShopDto> shopResp = shopService.queryById(dto.getShopId());
        	if(null == shopResp || !shopResp.isSuccess() || null==shopResp.getData()) {
        		throw new RuntimeException("根据shopId查询门店信息为空");
        	}
    		
    		// 处理门店名称和code
    		ShopDto shop = shopResp.getData();
    		if(null == shop) {
    			throw new RuntimeException("根据shopId查询门店信息为空");
    		}
            dto.setShopCode(shop.getCode());
            dto.setShopName(shop.getName());
        }
        ShopSingleConfig shopSingleConfig = BeanMapper.map(dto, ShopSingleConfig.class);
        if (shopSingleConfig.getId() == null) {
            ResponseResult<ShopSingleConfigDto> checkResult = checkParamForSave(dto);
            if (!checkResult.isSuccess()) {
                return checkResult;
            }
            shopSingleConfig.setServiceLine(StringUtils.join(dto.getServiceLines(), SPLIT_POINT));
            shopSingleConfig.setId(IdGenerator.nextId());
            shopSingleConfigMapper.insertSelective(shopSingleConfig);
            shopSingleConfig1 = shopSingleConfigMapper.selectByPrimaryKey(shopSingleConfig.getId());
            dealFieldAddSendMq(shopSingleConfig1);
        } else {
            ResponseResult<ShopSingleConfigDto> checkResult = checkParamForUpdate(dto);
            if (!checkResult.isSuccess()) {
                return checkResult;
            }
            String oldServiceLine = null;
            if (dto.getServiceLines() != null && dto.getServiceLines().length > 0) {
                ShopSingleConfig temp = shopSingleConfigMapper.selectByPrimaryKey(dto.getId());
                shopSingleConfig.setServiceLine(StringUtils.join(dto.getServiceLines(), SPLIT_POINT));
                if (!temp.getServiceLine().equals(shopSingleConfig.getServiceLine())) {
                    //业务线有做修改
                    oldServiceLine = temp.getServiceLine();
                }
            }
            shopSingleConfigMapper.updateByPrimaryKeySelective(shopSingleConfig);
            shopSingleConfig1 = shopSingleConfigMapper.selectByPrimaryKey(shopSingleConfig.getId());
            dealFieldUpdateSendMq(shopSingleConfig1, oldServiceLine);
        }
        ShopSingleConfigDto configDto = BeanMapper.map(shopSingleConfig1, ShopSingleConfigDto.class);
        configDto.setServiceLines(shopSingleConfig1.getServiceLine().split(SPLIT_POINT));
        return ResponseResult.buildSuccessResponse(configDto);
    }

    @Override
    public ResponseResult<Pagination<ShopSingleConfigDto>> getShopSingleConfig(ShopSingleConfigParam param) {
        Pagination<ShopSingleConfigDto> pagination = new Pagination<>();
        int count = shopSingleConfigExtMapper.getShopIdsCount(param);
        pagination.setTotalCount(count);
        if (count == 0) {
            return ResponseResult.buildSuccessResponse(pagination);
        }
        List<Long> list = shopSingleConfigExtMapper.getShopIds(param);
        String ids = "'" + StringUtils.join(list, "','") + "'";
        List<ShopSingleConfig> shopSingleConfigs = shopSingleConfigExtMapper.getShopSingleConfigByIds(ids);
        List<ShopSingleConfigDto> configDtos = new ArrayList<>();
        shopSingleConfigs.stream().forEach(o -> {
            ShopSingleConfigDto dto = BeanMapper.map(o, ShopSingleConfigDto.class);
            dto.setServiceLines(o.getServiceLine().split(SPLIT_POINT));
            configDtos.add(dto);
        });
        configDtos.sort(Comparator.comparingLong(ShopSingleConfigDto::getShopId));
        pagination.setDataList(configDtos);
        return ResponseResult.buildSuccessResponse(pagination);
    }

    @Override
    public ResponseResult<ShopSingleConfigDto> getByShopSingleConfigById(Long id) {
        ShopSingleConfig shopSingleConfig = shopSingleConfigMapper.selectByPrimaryKey(id);
        ShopSingleConfigDto map = BeanMapper.map(shopSingleConfig, ShopSingleConfigDto.class);
        map.setServiceLines(shopSingleConfig.getServiceLine().split(SPLIT_POINT));
        return ResponseResult.buildSuccessResponse(map);
    }

    @Override
    public ResponseResult<MaxOutputDto> queryMaxOutput(Long shopId) {
        ShopSingleConfig maxOutput = shopSingleConfigExtMapper.queryByShopIdAndKey(shopId, "maxProduction");
        if (maxOutput == null) {
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_SINGLE_KEY_NOT_FOUND.getCode(), ConfigEnum.SHOP_SINGLE_KEY_NOT_FOUND.getDesc());
        }
        MaxOutputDto maxOutputDto = new MaxOutputDto();
        maxOutputDto.setId(maxOutput.getId());
        maxOutputDto.setShopId(shopId);
        maxOutputDto.setShopCode(maxOutput.getShopCode());
        maxOutputDto.setShopName(maxOutput.getShopName());
        maxOutputDto.setMaxOutput(Integer.parseInt(maxOutput.getConfigValue()));
        return ResponseResult.buildSuccessResponse(maxOutputDto);
    }

    @Override
    public ResponseResult<MaxOutputDto> updateMaxOutput(MaxOutputDto dto) {
        if (dto.getMaxOutput() != null && dto.getMaxOutput().compareTo(0) <= 0) {
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_MAX_OUTPUT_NOT_ZERO.getCode(), ConfigEnum.SHOP_MAX_OUTPUT_NOT_ZERO.getDesc());
        }
        ShopSingleConfig shopSingleConfig = new ShopSingleConfig();
        shopSingleConfig.setId(dto.getId());
        shopSingleConfig.setConfigKey("maxProduction");
        shopSingleConfig.setConfigValue(dto.getMaxOutput().toString());
        shopSingleConfig.setUpdateBy(dto.getUpdateBy());
        shopSingleConfigMapper.updateByPrimaryKeySelective(shopSingleConfig);
        ShopSingleConfig result = shopSingleConfigMapper.selectByPrimaryKey(dto.getId());
        MaxOutputDto maxOutputDto = new MaxOutputDto();
        maxOutputDto.setId(dto.getId());
        maxOutputDto.setShopId(result.getShopId());
        maxOutputDto.setShopCode(result.getShopCode());
        maxOutputDto.setShopName(result.getShopName());
        maxOutputDto.setMaxOutput(Integer.parseInt(result.getConfigValue()));
        dealFieldUpdateSendMq(result, null);
        return ResponseResult.buildSuccessResponse(maxOutputDto);
    }

    @Override
    public List<ShopSingleConfigDto> getShopSingleConfigByShopId(Long shopId) {
        List<ShopSingleConfig> shopSingleConfigs = shopSingleConfigExtMapper.queryByShopId(shopId);
        if (shopSingleConfigs == null || shopSingleConfigs.size() == 0) {
            return null;
        }
        List<ShopSingleConfigDto> list = new ArrayList<>();
        shopSingleConfigs.stream().forEach(o -> {
            ShopSingleConfigDto dto = BeanMapper.map(o, ShopSingleConfigDto.class);
            dto.setServiceLines(o.getServiceLine().split(SPLIT_POINT));
            list.add(dto);
        });
        return list;
    }

    @Override
    public ResponseResult<Boolean> copyConfig(Long srcId, ShopReqDto shopReqDto) {
        List<ShopSingleConfig> list = shopSingleConfigExtMapper.queryByShopId(srcId);
        if (list == null || list.size() == 0) {
            return ResponseResult.buildSuccessResponse(true);
        }
        list.stream().forEach(o -> {
            ShopSingleConfig shopSingleConfig = new ShopSingleConfig();
            shopSingleConfig.setId(IdGenerator.nextId());
            shopSingleConfig.setConfigKey(o.getConfigKey());
            shopSingleConfig.setConfigValue(o.getConfigValue());
            shopSingleConfig.setConfigDescribe(o.getConfigDescribe());
            shopSingleConfig.setConfigValueType(o.getConfigValueType());
            shopSingleConfig.setCreateBy(shopReqDto.getCreateBy());
            shopSingleConfig.setShopId(shopReqDto.getId());
            shopSingleConfig.setShopCode(shopReqDto.getCode());
            shopSingleConfig.setShopName(shopReqDto.getName());
            shopSingleConfigMapper.insertSelective(shopSingleConfig);
        });
        return ResponseResult.buildSuccessResponse(true);
    }

    @Override
    public ResponseResult<Boolean> syncShopConfig(Long shopId) {
        dealFieldSyncSendMq(shopId);
        return ResponseResult.buildSuccessResponse();
    }

    @Override
    public ResponseResult<Boolean> syncConfig(Long shopId) {
        if (shopId != null) {
            dealFieldSyncSendMq(shopId);
            return ResponseResult.buildSuccessResponse();
        }
        List<ShopSingleConfig> singleConfigs = shopSingleConfigExtMapper.queryAllShop();
        singleConfigs.stream().forEach(o -> dealFieldSyncSendMq(o.getShopId()));
        return ResponseResult.buildSuccessResponse();
    }

    private void dealFieldSyncSendMq(Long shopId) {
        List<ShopSingleConfig> shopSingleConfig = shopSingleConfigExtMapper.queryByShopId(shopId);
        HashMap<String, List<ShopSingleConfigMQDto>> map = new HashMap<>();
        List<String> lineList = new ArrayList<>();
        shopSingleConfig.forEach(o -> {
            lineList.addAll(Arrays.asList(o.getServiceLine().split(SPLIT_POINT)));
            if (lineList.contains(ConfigConstant.SERVICE_LINE.COMMON.getValue())) {
                Arrays.stream(ConfigConstant.SERVICE_LINE.values()).forEach(o1 -> lineList.add(o1.getValue()));
                while (lineList.contains(ConfigConstant.SERVICE_LINE.COMMON.getValue())) {
                    lineList.remove(ConfigConstant.SERVICE_LINE.COMMON.getValue());
                }
            }
            ShopSingleConfigMQDto s = new ShopSingleConfigMQDto();
            s.setShopId(o.getShopId());
            s.setShopCode(o.getShopCode());
            s.setConfigKey(o.getConfigKey());
            s.setConfigValue(o.getConfigValue());
            s.setConfigValueType(o.getConfigValueType());
            s.setConfigDescribe(o.getConfigDescribe());
            lineList.stream().forEach(o1 -> {
                List<ShopSingleConfigMQDto> dtoList = map.get(o1);
                if (dtoList == null) {
                    dtoList = new ArrayList<>();
                    map.put(o1, dtoList);
                }
                dtoList.add(s);
            });
            lineList.clear();
        });
        ConfigDto<List<ShopSingleConfigMQDto>> configDto = new ConfigDto<>();
        configDto.setType(ConfigConstant.SHOP_SINGLE_CONFIG.getValue());
        configDto.setOperation(ConfigConstant.OPERATE.SYNC.getType());
        map.entrySet().stream().forEach(o -> {
            configDto.setServiceLine(getServiceLineName(o.getKey()));
            configDto.setData(o.getValue());
            MqProducerUtil.sendConfig(configDto);
        });
    }

    private void dealFieldAddSendMq(ShopSingleConfig config) {
        ConfigDto<List<ShopSingleConfigMQDto>> configDto = new ConfigDto<>();
        ArrayList<ShopSingleConfigMQDto> mqDtos = new ArrayList<>();
        ShopSingleConfigMQDto shopSingleMQDto = new ShopSingleConfigMQDto();
        shopSingleMQDto.setShopId(config.getShopId());
        shopSingleMQDto.setShopCode(config.getShopCode());
        shopSingleMQDto.setConfigKey(config.getConfigKey());
        shopSingleMQDto.setConfigValue(config.getConfigValue());
        shopSingleMQDto.setConfigDescribe(config.getConfigDescribe());
        shopSingleMQDto.setConfigValueType(config.getConfigValueType());
        mqDtos.add(shopSingleMQDto);
        configDto.setData(mqDtos);
        configDto.setOperation(ConfigConstant.OPERATE.ADD.getType());
        configDto.setType(ConfigConstant.SHOP_SINGLE_CONFIG.getValue());
        configDto.setServiceLine(getServiceLineName(config.getServiceLine()));
        MqProducerUtil.sendConfig(configDto);
    }

    private void dealFieldUpdateSendMq(ShopSingleConfig config, String oldServiceLine) {
        ConfigDto<List<ShopSingleConfigMQDto>> configDto = new ConfigDto<>();
        ArrayList<ShopSingleConfigMQDto> mqDtos = new ArrayList<>();
        ShopSingleConfigMQDto shopSingleMQDto = new ShopSingleConfigMQDto();
        shopSingleMQDto.setShopId(config.getShopId());
        shopSingleMQDto.setShopCode(config.getShopCode());
        shopSingleMQDto.setConfigKey(config.getConfigKey());
        shopSingleMQDto.setConfigValue(config.getConfigValue());
        shopSingleMQDto.setConfigDescribe(config.getConfigDescribe());
        shopSingleMQDto.setConfigValueType(config.getConfigValueType());
        mqDtos.add(shopSingleMQDto);
        configDto.setData(mqDtos);
        configDto.setType(ConfigConstant.SHOP_SINGLE_CONFIG.getValue());
        if (Boolean.TRUE.equals(config.getIsDeleted())) {
            configDto.setOperation(ConfigConstant.OPERATE.DELETE.getType());
            configDto.setServiceLine(getServiceLineName(config.getServiceLine()));
            MqProducerUtil.sendConfig(configDto);
        } else {
            if (StringUtils.isBlank(oldServiceLine)) {
                configDto.setOperation(ConfigConstant.OPERATE.UPDATE.getType());
                configDto.setServiceLine(getServiceLineName(config.getServiceLine()));
                MqProducerUtil.sendConfig(configDto);
            } else {
                List<String> oldLines = new ArrayList<>();//delete
                List<String> newLines = new ArrayList<>();//add
                List<String> commLines = new ArrayList<>();//update
                if (StringUtils.isNotBlank(oldServiceLine)) {
                    oldLines.addAll(Arrays.asList(oldServiceLine.split(SPLIT_POINT)));
                }
                if (StringUtils.isNotBlank(config.getServiceLine())) {
                    newLines.addAll(Arrays.asList(config.getServiceLine().split(SPLIT_POINT)));
                }
                if (newLines.contains(ConfigConstant.SERVICE_LINE.COMMON.getValue())) {
                    newLines.clear();
                    Arrays.stream(ConfigConstant.SERVICE_LINE.values()).forEach(o -> {
                        newLines.add(o.getValue());
                    });
                    while (newLines.contains(ConfigConstant.SERVICE_LINE.COMMON.getValue())) {
                        newLines.remove(ConfigConstant.SERVICE_LINE.COMMON.getValue());
                    }
                }
                if (oldLines.contains(ConfigConstant.SERVICE_LINE.COMMON.getValue())) {
                    oldLines.clear();
                    Arrays.stream(ConfigConstant.SERVICE_LINE.values()).forEach(o -> {
                        oldLines.add(o.getValue());
                    });
                    while (oldLines.contains(ConfigConstant.SERVICE_LINE.COMMON.getValue())) {
                        oldLines.remove(ConfigConstant.SERVICE_LINE.COMMON.getValue());
                    }
                }
                commLines.addAll(oldLines);
                commLines.retainAll(newLines);
                oldLines.removeAll(commLines);
                newLines.removeAll(commLines);
                if (oldLines.size() > 0) {
                    configDto.setOperation(ConfigConstant.OPERATE.DELETE.getType());
                    configDto.setServiceLine(getServiceLineName(StringUtils.join(oldLines, SPLIT_POINT)));
                    MqProducerUtil.sendConfig(configDto);
                }
                if (commLines.size() > 0) {
                    configDto.setOperation(ConfigConstant.OPERATE.UPDATE.getType());
                    configDto.setServiceLine(getServiceLineName(StringUtils.join(commLines, SPLIT_POINT)));
                    MqProducerUtil.sendConfig(configDto);
                }
                if (newLines.size() > 0) {
                    configDto.setOperation(ConfigConstant.OPERATE.ADD.getType());
                    configDto.setServiceLine(getServiceLineName(StringUtils.join(newLines, SPLIT_POINT)));
                    MqProducerUtil.sendConfig(configDto);
                }
            }
        }
    }

    private ResponseResult<ShopSingleConfigDto> checkParamForUpdate(ShopSingleConfigDto dto) {
        //更新时检查业务线
        String[] lines = dto.getServiceLines();
        if (lines != null && lines.length > 0) {
            for (String s : lines) {
                if (ConfigConstant.SERVICE_LINE.getConstantByValue(s) == null) {
                    return ResponseResult.buildFailResponse(ConfigEnum.SERVICE_LINE_NOT_FOUND.getCode(), ConfigEnum.SERVICE_LINE_NOT_FOUND.getDesc());
                }
            }
        }
        if (dto.getConfigValue() != null && dto.getConfigValue().length() == 0) {
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_SINGLE_VAL_BLANK.getCode(), ConfigEnum.SHOP_SINGLE_VAL_BLANK.getDesc());
        }
        if (dto.getConfigValue() != null && dto.getConfigValue().length() == 0) {
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_SINGLE_VAL_BLANK.getCode(), ConfigEnum.SHOP_SINGLE_VAL_BLANK.getDesc());
        }
        if (dto.getConfigDescribe() != null && dto.getConfigDescribe().length() == 0) {
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_SINGLE_DESC_BLANK.getCode(), ConfigEnum.SHOP_SINGLE_DESC_BLANK.getDesc());
        }
        ShopSingleConfig shopSingleConfig = shopSingleConfigExtMapper.queryByShopIdAndKey(dto.getShopId(), dto.getConfigKey());
        if (shopSingleConfig == null) {
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_SINGLE_KEY_NOT_CHANGE.getCode(), ConfigEnum.SHOP_SINGLE_KEY_NOT_CHANGE.getDesc());
        }
        return ResponseResult.buildSuccessResponse();
    }

    private ResponseResult<ShopSingleConfigDto> checkParamForSave(ShopSingleConfigDto dto) {
        if (StringUtils.isBlank(dto.getConfigKey())) {
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_SINGLE_KEY_BLANK.getCode(), ConfigEnum.SHOP_SINGLE_KEY_BLANK.getDesc());
        }
        if (StringUtils.isBlank(dto.getConfigValue())) {
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_SINGLE_VAL_BLANK.getCode(), ConfigEnum.SHOP_SINGLE_VAL_BLANK.getDesc());
        }
        if (dto.getServiceLines() == null || dto.getServiceLines().length == 0) {
            return ResponseResult.buildFailResponse(ConfigEnum.SERVICE_LINE_BLANK.getCode(), ConfigEnum.SERVICE_LINE_BLANK.getDesc());
        } else {
            for (String s : dto.getServiceLines()) {
                if (ConfigConstant.SERVICE_LINE.getConstantByValue(s) == null) {
                    return ResponseResult.buildFailResponse(ConfigEnum.SERVICE_LINE_NOT_FOUND.getCode(), ConfigEnum.SERVICE_LINE_NOT_FOUND.getDesc());
                }
            }
        }
        if (dto.getConfigValueType() == null) {
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_SINGLE_TYPE_BLANK.getCode(), ConfigEnum.SHOP_SINGLE_TYPE_BLANK.getDesc());
        }
        if (dto.getShopId() == null) {
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_SINGLE_SHOP_BLANK.getCode(), ConfigEnum.SHOP_SINGLE_SHOP_BLANK.getDesc());
        }
        ShopSingleConfig shopSingleConfig = shopSingleConfigExtMapper.queryByShopIdAndKey(dto.getShopId(), dto.getConfigKey());
        if (shopSingleConfig != null) {
            return ResponseResult.buildFailResponse(ConfigEnum.SHOP_SINGLE_KEY_EXISTS.getCode(), ConfigEnum.SHOP_SINGLE_KEY_EXISTS.getDesc());
        }
        return ResponseResult.buildSuccessResponse();
    }

    private static String[] getServiceLineName(String val) {
        final List<String> strings = new ArrayList<>();
        if (StringUtils.isBlank(val)) {
            strings.add(ConfigConstant.SERVICE_LINE.COMMON.getLineName());
        } else {
            List<String> split = Arrays.asList(val.split(SPLIT_POINT));
            if (split.size() == 0 || split.contains(ConfigConstant.SERVICE_LINE.COMMON.getValue())) {
                strings.add(ConfigConstant.SERVICE_LINE.COMMON.getLineName());
            } else {
                split.stream().forEach(o -> {
                    ConfigConstant.SERVICE_LINE constant = ConfigConstant.SERVICE_LINE.getConstantByValue(o);
                    if (constant != null) {
                        strings.add(constant.getLineName());
                    } else {
                        log.error("门店属性信息同步出现未知业务线配置---{}", val);
                    }
                });
                if (strings.size() == 0) {
                    strings.add(ConfigConstant.SERVICE_LINE.COMMON.getLineName());
                }
            }
        }
        if (strings.contains(ConfigConstant.SERVICE_LINE.COMMON.getLineName())) {
            strings.clear();
            Arrays.stream(ConfigConstant.SERVICE_LINE.values()).forEach(o -> strings.add(o.getLineName()));
            while (strings.contains(ConfigConstant.SERVICE_LINE.COMMON.getLineName())) {
                strings.remove(ConfigConstant.SERVICE_LINE.COMMON.getLineName());
            }
        }
        String[] result = new String[strings.size()];
        return strings.toArray(result);
    }
}
