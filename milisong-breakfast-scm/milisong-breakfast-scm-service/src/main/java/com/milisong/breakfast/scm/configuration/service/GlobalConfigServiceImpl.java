package com.milisong.breakfast.scm.configuration.service;

import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.breakfast.scm.configuration.api.GlobalConfigService;
import com.milisong.breakfast.scm.configuration.constant.ConfigConstant;
import com.milisong.breakfast.scm.configuration.constant.ConfigEnum;
import com.milisong.breakfast.scm.configuration.constant.GlobalConfigTypeEnum;
import com.milisong.breakfast.scm.configuration.domain.GlobalConfig;
import com.milisong.breakfast.scm.configuration.dto.ConfigDto;
import com.milisong.breakfast.scm.configuration.dto.GlobalConfigDto;
import com.milisong.breakfast.scm.configuration.mapper.GlobalConfigExtMapper;
import com.milisong.breakfast.scm.configuration.mapper.GlobalConfigMapper;
import com.milisong.breakfast.scm.configuration.properties.OssFileProperties;
import com.milisong.breakfast.scm.configuration.util.MqProducerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 *    author  : Tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2018/10/23   17:36
 *    desc    : 全局配置业务实现类
 *    version : v1.0
 * </pre>
 */
@Slf4j
@Service("globalConfigService")
public class GlobalConfigServiceImpl implements GlobalConfigService {

    @Autowired
    private GlobalConfigMapper globalConfigMapper;

    @Autowired
    private GlobalConfigExtMapper globalConfigExtMapper;

    @Resource(name = "configOssFileProperties")
    private OssFileProperties ossFileProperties;

    private static final String SPLIT_POINT = ",";

    @Override
    public ResponseResult<GlobalConfigDto> saveOrUpdate(GlobalConfigDto dto) {
        Long id;
        GlobalConfig globalConfig;
        if(dto.getId() == null){
            ResponseResult<GlobalConfigDto> checkResult = checkParamForSave(dto);
            if(!checkResult.isSuccess()){
                return checkResult;
            }
            dto.setId(IdGenerator.nextId());
            id = dto.getId();
            GlobalConfig config = BeanMapper.map(dto, GlobalConfig.class);
            config.setServiceLine(StringUtils.join(dto.getServiceLines(),SPLIT_POINT));
            globalConfigMapper.insertSelective(config);
            globalConfig = globalConfigMapper.selectByPrimaryKey(id);
            dealFieldAddSendMq(globalConfig);
        }else{
            ResponseResult<GlobalConfigDto> checkResult = checkParamForUpdate(dto);
            if(!checkResult.isSuccess()){
                return checkResult;
            }
            id = dto.getId();
            GlobalConfig config = BeanMapper.map(dto, GlobalConfig.class);
            String oldServiceLine = null;
            if(dto.getServiceLines()!=null && dto.getServiceLines().length > 0){
                GlobalConfig temp = globalConfigMapper.selectByPrimaryKey(id);
                config.setServiceLine(StringUtils.join(dto.getServiceLines(),SPLIT_POINT));
                if(!temp.getServiceLine().equals(config.getServiceLine())){
                    //业务线有做修改
                    oldServiceLine = temp.getServiceLine();
                }
            }
            globalConfigMapper.updateByPrimaryKeySelective(config);
            globalConfig = globalConfigMapper.selectByPrimaryKey(id);
            dealFieldUpdateSendMq(globalConfig,oldServiceLine);
        }
        GlobalConfigDto configDto = BeanMapper.map(globalConfig, GlobalConfigDto.class);
        configDto.setServiceLines(globalConfig.getServiceLine().split(SPLIT_POINT));
        return ResponseResult.buildSuccessResponse();
    }


    @Override
    public ResponseResult<List<GlobalConfigDto>> getGloableConfig() {
        List<GlobalConfig> globalConfigs = globalConfigExtMapper.queryGlobalConfig();
        if(globalConfigs == null || globalConfigs.size() == 0){
            return ResponseResult.buildSuccessResponse();
        }else{
            List<GlobalConfigDto> globalConfigDtos = new ArrayList<>();
            globalConfigs.stream().forEach(o->{
                GlobalConfigDto configDto = BeanMapper.map(o, GlobalConfigDto.class);
                configDto.setServiceLines(o.getServiceLine().split(SPLIT_POINT));
                adjustPicConfig(configDto);
                globalConfigDtos.add(configDto);
            });
            return ResponseResult.buildSuccessResponse(globalConfigDtos);
        }

    }

    @Override
    public ResponseResult<GlobalConfigDto> getGloableConfigById(Long id) {
        GlobalConfig globalConfig = globalConfigMapper.selectByPrimaryKey(id);
        GlobalConfigDto configDto = BeanMapper.map(globalConfig, GlobalConfigDto.class);
        configDto.setServiceLines(globalConfig.getServiceLine().split(SPLIT_POINT));
        adjustPicConfig(configDto);
        return ResponseResult.buildSuccessResponse(configDto);
    }

    @Override
    public String getGloableConfigByKey(String key) {
        GlobalConfig globalConfig = globalConfigExtMapper.queryByKey(key);
        if(globalConfig == null){
            return null;
        }
        return globalConfig.getConfigValue();
    }

    //同步全部配置信息
    @Override
    public ResponseResult<Boolean> syncConfig() {
        dealFieldSyncSendMq();
        return ResponseResult.buildSuccessResponse();
    }

    private void dealFieldSyncSendMq(){
        List<GlobalConfig> globalConfigs = globalConfigExtMapper.queryGlobalConfig();
        HashMap<String,HashMap<String,String>> map = new HashMap<>();
        List<String> lineList = new ArrayList<>();
        globalConfigs.forEach(o->{
            lineList.addAll(Arrays.asList(o.getServiceLine().split(SPLIT_POINT)));
            if(lineList.contains(ConfigConstant.SERVICE_LINE.COMMON.getValue())){
                Arrays.stream(ConfigConstant.SERVICE_LINE.values()).forEach(o1-> lineList.add(o1.getValue()));
                while(lineList.contains(ConfigConstant.SERVICE_LINE.COMMON.getValue())){
                    lineList.remove(ConfigConstant.SERVICE_LINE.COMMON.getValue());
                }
            }
            lineList.stream().forEach(o1->{
                HashMap<String,String> oMap = map.get(o1);
                if(oMap == null){
                    oMap = new HashMap<>();
                    map.put(o1,oMap);
                }
                if(GlobalConfigTypeEnum.PIC.getVal().equals(o.getConfigValueType())){
                    oMap.put(o.getConfigKey(),getViewUrl(o.getConfigValue()));
                }else{
                    oMap.put(o.getConfigKey(),o.getConfigValue());
                }
            });
            lineList.clear();
        });
        ConfigDto<HashMap> configDto = new ConfigDto<>();
        configDto.setType(ConfigConstant.GLOBAL_CONFIG.getValue());
        configDto.setOperation(ConfigConstant.OPERATE.SYNC.getType());
        map.entrySet().stream().forEach(o->{
            configDto.setServiceLine(getServiceLineName(o.getKey()));
            configDto.setData(o.getValue());
            MqProducerUtil.sendConfig(configDto);
        });
    }

    //针对单条数据更新(包括删除)，发送mq
    private void dealFieldUpdateSendMq(GlobalConfig dto,String oldServiceLine){
        HashMap<String,String> map = new HashMap<>();
        ConfigDto<HashMap<String,String>> configDto = new ConfigDto<>();
        if(GlobalConfigTypeEnum.PIC.getVal().equals(dto.getConfigValueType())){
            map.put(dto.getConfigKey(),getViewUrl(dto.getConfigValue()));
        }else{
            map.put(dto.getConfigKey(),dto.getConfigValue());
        }
        configDto.setData(map);
        configDto.setType(ConfigConstant.GLOBAL_CONFIG.getValue());
        if(Boolean.TRUE.equals(dto.getIsDeleted())){
            configDto.setOperation(ConfigConstant.OPERATE.DELETE.getType());
            configDto.setServiceLine(getServiceLineName(dto.getServiceLine()));
            MqProducerUtil.sendConfig(configDto);
        }else{
            //oldServiceLine作为判断业务线是否改变的标识
            if(StringUtils.isBlank(oldServiceLine)){
                configDto.setOperation(ConfigConstant.OPERATE.UPDATE.getType());
                configDto.setServiceLine(getServiceLineName(dto.getServiceLine()));
                MqProducerUtil.sendConfig(configDto);
            }else{
                List<String> oldLines = new ArrayList<>();//delete
                List<String> newLines = new ArrayList<>();//add
                List<String> commLines = new ArrayList<>();//update
                if(StringUtils.isNotBlank(oldServiceLine)){
                    oldLines.addAll(Arrays.asList(oldServiceLine.split(SPLIT_POINT)));
                }
                if(StringUtils.isNotBlank(dto.getServiceLine())){
                    newLines.addAll(Arrays.asList(dto.getServiceLine().split(SPLIT_POINT)));
                }
                if(newLines.contains(ConfigConstant.SERVICE_LINE.COMMON.getValue())){
                    newLines.clear();
                    Arrays.stream(ConfigConstant.SERVICE_LINE.values()).forEach(o->{
                        newLines.add(o.getValue());
                    });
                    while(newLines.contains(ConfigConstant.SERVICE_LINE.COMMON.getValue())){
                        newLines.remove(ConfigConstant.SERVICE_LINE.COMMON.getValue());
                    }
                }
                if(oldLines.contains(ConfigConstant.SERVICE_LINE.COMMON.getValue())){
                    oldLines.clear();
                    Arrays.stream(ConfigConstant.SERVICE_LINE.values()).forEach(o->{
                        oldLines.add(o.getValue());
                    });
                    while(oldLines.contains(ConfigConstant.SERVICE_LINE.COMMON.getValue())){
                        oldLines.remove(ConfigConstant.SERVICE_LINE.COMMON.getValue());
                    }
                }
                commLines.addAll(oldLines);
                commLines.retainAll(newLines);
                oldLines.removeAll(commLines);
                newLines.removeAll(commLines);
                if(oldLines.size() > 0){
                    configDto.setOperation(ConfigConstant.OPERATE.DELETE.getType());
                    configDto.setServiceLine(getServiceLineName(StringUtils.join(oldLines,SPLIT_POINT)));
                    MqProducerUtil.sendConfig(configDto);
                }
                if(commLines.size() > 0){
                    configDto.setOperation(ConfigConstant.OPERATE.UPDATE.getType());
                    configDto.setServiceLine(getServiceLineName(StringUtils.join(commLines,SPLIT_POINT)));
                    MqProducerUtil.sendConfig(configDto);
                }
                if(newLines.size() > 0){
                    configDto.setOperation(ConfigConstant.OPERATE.ADD.getType());
                    configDto.setServiceLine(getServiceLineName(StringUtils.join(newLines,SPLIT_POINT)));
                    MqProducerUtil.sendConfig(configDto);
                }
            }

        }
        return;
    }

    private void dealFieldAddSendMq(GlobalConfig dto) {
        HashMap<String,String> map = new HashMap<>();
        ConfigDto<HashMap<String,String>> configDto = new ConfigDto<>();
        if(GlobalConfigTypeEnum.PIC.getVal().equals(dto.getConfigValueType())){
            map.put(dto.getConfigKey(),getViewUrl(dto.getConfigValue()));
        }else{
            map.put(dto.getConfigKey(),dto.getConfigValue());
        }
        configDto.setData(map);
        configDto.setOperation(ConfigConstant.OPERATE.ADD.getType());
        configDto.setType(ConfigConstant.GLOBAL_CONFIG.getValue());
        configDto.setServiceLine(getServiceLineName(dto.getServiceLine()));
        MqProducerUtil.sendConfig(configDto);
    }

    private ResponseResult<GlobalConfigDto> checkParamForUpdate(GlobalConfigDto dto) {
        String[] lines = dto.getServiceLines();
        if(lines != null && lines.length > 0){
            for (String s:lines) {
                if(ConfigConstant.SERVICE_LINE.getConstantByValue(s) == null ){
                    return ResponseResult.buildFailResponse(ConfigEnum.SERVICE_LINE_NOT_FOUND.getCode(),ConfigEnum.SERVICE_LINE_NOT_FOUND.getDesc());
                }
            }
        }
        return ResponseResult.buildSuccessResponse();
    }

    private ResponseResult<GlobalConfigDto> checkParamForSave(GlobalConfigDto dto) {
        if(StringUtils.isBlank(dto.getConfigKey())){
            return ResponseResult.buildFailResponse(ConfigEnum.GLOBAL_KEY_BLANK.getCode(),ConfigEnum.GLOBAL_KEY_BLANK.getDesc());
        }
        if(dto.getServiceLines() == null || dto.getServiceLines().length == 0){
            return ResponseResult.buildFailResponse(ConfigEnum.SERVICE_LINE_BLANK.getCode(),ConfigEnum.SERVICE_LINE_BLANK.getDesc());
        }else{
            String[] lines = dto.getServiceLines();
            for (String s:lines) {
                if(ConfigConstant.SERVICE_LINE.getConstantByValue(s) == null ){
                    return ResponseResult.buildFailResponse(ConfigEnum.SERVICE_LINE_NOT_FOUND.getCode(),ConfigEnum.SERVICE_LINE_NOT_FOUND.getDesc());
                }
            }
        }
        if(dto.getConfigValueType() == null){
            return ResponseResult.buildFailResponse(ConfigEnum.GLOBAL_TYPE_BLANK.getCode(),ConfigEnum.GLOBAL_TYPE_BLANK.getDesc());
        }
        GlobalConfig globalConfig = globalConfigExtMapper.queryByKey(dto.getConfigKey());
        if(globalConfig!=null){
            return ResponseResult.buildFailResponse(ConfigEnum.GLOBAL_CONFIG_EXITS.getCode(),ConfigEnum.GLOBAL_CONFIG_EXITS.getDesc());
        }
        return ResponseResult.buildSuccessResponse();
    }

    private void testConfigMQ(String serviceLine){
        /*ConfigDto<HashMap<String, String>> configDto = new ConfigDto<>();
        configDto.setServiceLine(getServiceLineName(serviceLine));
        configDto.setType(ConfigConstant.GLOBAL_CONFIG.getValue());
        configDto.setOperation(ConfigConstant.OPERATE.DELETE.getType());
        HashMap<String, String> map = new HashMap<>();
        map.put("test_config1","aaaa");
        configDto.setData(map);
        MqProducerUtil.sendConfigMsg(configDto);*/
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setServiceLine("1,2");
        globalConfig.setConfigKey("tttt");
        globalConfig.setConfigValue("344434");
        String oldServiceLine = "2,3";
        dealFieldUpdateSendMq(globalConfig,oldServiceLine);
    }

    private static String[] getServiceLineName(String val){
        final List<String> strings = new ArrayList<>();
        if(StringUtils.isBlank(val)){
            strings.add(ConfigConstant.SERVICE_LINE.COMMON.getLineName());
        }else{
            List<String> split = Arrays.asList(val.split(SPLIT_POINT));
            if(split.size() == 0 || split.contains(ConfigConstant.SERVICE_LINE.COMMON.getValue())){
                strings.add(ConfigConstant.SERVICE_LINE.COMMON.getLineName());
            }else{
                split.stream().forEach(o-> {
                    ConfigConstant.SERVICE_LINE constant = ConfigConstant.SERVICE_LINE.getConstantByValue(o);
                    if(constant != null){
                        strings.add(constant.getLineName());
                    }else{
                       log.error("全局配置信息同步出现未知业务线配置---{}",val);
                    }
                });
                if(strings.size() == 0){
                    strings.add(ConfigConstant.SERVICE_LINE.COMMON.getLineName());
                }
            }
        }
        if(strings.contains(ConfigConstant.SERVICE_LINE.COMMON.getLineName())){
            strings.clear();
            Arrays.stream(ConfigConstant.SERVICE_LINE.values()).forEach(o-> strings.add(o.getLineName()));
            while(strings.contains(ConfigConstant.SERVICE_LINE.COMMON.getLineName())){
                strings.remove(ConfigConstant.SERVICE_LINE.COMMON.getLineName());
            }
        }
        String[] result = new String[strings.size()];
        return strings.toArray(result);
    }

    private void adjustPicConfig(GlobalConfigDto configDto){
        if(GlobalConfigTypeEnum.PIC.getVal().equals(configDto.getConfigValueType())){
            configDto.setActualPicUrl(getViewUrl(configDto.getConfigValue()));
        }else{
            return;
        }
    }

    private String getViewUrl(String relativePath) {
        String returnUrl = ossFileProperties.getViewUrl();
        if(!returnUrl.endsWith("/")) {
            returnUrl = returnUrl.concat("/");
        }
        returnUrl = returnUrl.concat(Byte.toString(ossFileProperties.getBizType()));
        if(!relativePath.startsWith("/")){
            returnUrl = returnUrl.concat("/");
        }
        returnUrl = returnUrl.concat(relativePath);
        return returnUrl;
    }

}
