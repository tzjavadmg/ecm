package com.milisong.scm.shop.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.base.api.ShopService;
import com.milisong.scm.base.dto.ShopDto;
import com.milisong.scm.shop.api.BuildingService;
import com.milisong.scm.shop.constant.BuildingConstant;
import com.milisong.scm.shop.domain.Building;
import com.milisong.scm.shop.domain.BuildingExample;
import com.milisong.scm.shop.domain.BuildingExample.Criteria;
import com.milisong.scm.shop.dto.building.BuildingDto;
import com.milisong.scm.shop.dto.building.BuildingSaveDto;
import com.milisong.scm.shop.mapper.BuildingExtMapper;
import com.milisong.scm.shop.mapper.BuildingMapper;
import com.milisong.scm.shop.param.BuildingParam;
import com.milisong.scm.shop.param.BulidingUpdateListParam;
import com.milisong.scm.shop.util.MqProducerUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;


@Slf4j
@Service
public class BuildingServiceImpl implements BuildingService {
    @Autowired
    private BuildingExtMapper buildingExtMapper;
    @Autowired
    private BuildingMapper buildingMapper;
    @Autowired
    private ShopService shopService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${ecm.shoponsale.buildingList-url}")
    private String buildingListUrl;
    @Value("${lbs.pointServiceUrl}")
    private String lbsPointServiceUrl;

    @Override
    public Pagination<BuildingDto> getBuildingPageByShopId(BuildingParam param) {
        log.info("分页根据门店id查询楼宇信息：param={}", JSONObject.toJSONString(param));
        if (param == null || param.getShopId() == null) {
            log.info("查询条件为空");
            return new Pagination<>();
        }
        BuildingExample example = new BuildingExample();
        Criteria criteria = example.createCriteria();
        if (null != param.getBuildingId()) {
            criteria.andIdEqualTo(param.getBuildingId());
        }
        if (StringUtils.isNotBlank(param.getBuildingName())) {
            criteria.andAbbreviationLike("%".concat(param.getBuildingName()).concat("%"));
        }
        if (null != param.getShopId()) {
            criteria.andShopIdEqualTo(param.getShopId());
        }
        long ct = buildingMapper.countByExample(example);
        Pagination<BuildingDto> pagination = new Pagination<>();
        pagination.setPageNo(param.getPageNo());
        pagination.setPageSize(param.getPageSize());
        pagination.setTotalCount(ct);
        if (ct > 0) {
            example.setPageSize(param.getPageSize());
            example.setStartRow(param.getStartRow());
            example.setOrderByClause(" -weight desc ");
            List<Building> list = buildingMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(list)) {
                pagination.setDataList(Collections.emptyList());
            } else {
                pagination.setDataList(BeanMapper.mapList(list, BuildingDto.class));
            }
        }

        return pagination;
    }

    @Override
    public List<BuildingDto> getBuildingInfoByLatAndLon(String lat, String lon) {
        log.info("根据经纬度查询楼宇：lat={},lon={}", lat, lon);
        Map<String, Object> map = new HashMap<>();
        map.put("lat", lat);
        map.put("lon", lon);
        List<BuildingDto> list = buildingExtMapper.getBuildingList(map);
        return list;
    }

    @Override
    public BuildingDto getBuildingInfoById(Long buildingId) {
        log.info("根据楼宇id查询楼宇：buildingId={}", buildingId);
        if (buildingId == null) {
            log.info("楼宇id为空");
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("buildingId", buildingId);
        List<BuildingDto> list = buildingExtMapper.getBuildingList(map);
        if (CollectionUtils.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public ResponseResult<Pagination<BuildingDto>> pageSearch(BuildingParam param) {
        Pagination<BuildingDto> pagination = new Pagination<>();
        List<BuildingDto> list = new ArrayList<>();
        Long count = buildingExtMapper.selectCountByBuildingName(param);
        if (count > 0) {
            list = buildingExtMapper.selectPageByBuildingName(param);
        }
        pagination.setDataList(list);
        pagination.setTotalCount(count);
        return ResponseResult.buildSuccessResponse(pagination);
    }

    @Override
    public List<BuildingDto> search(BuildingParam param) {
        return buildingExtMapper.selectPageByBuildingName(param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult<Object> saveBuilding(BuildingSaveDto dto) {
        ResponseResult<Object> result = ResponseResult.buildSuccessResponse();
        // 检查楼宇名称
        if (_checkBuliding(dto)) {
            return ResponseResult.buildFailResponse("9999", "楼宇名称已存在！");
        }
        if (null == dto.getId()) {
            _addBuliding(dto, result);
        } else {
            _updateBuilding(dto, result);
        }
        return result;
    }

    @Override
    public ResponseResult<Object> sendBuildingToEcm() {
        BuildingExample example = new BuildingExample();
        example.createCriteria().andStatusIn(Arrays.asList((byte) 2, (byte) 9));
        List<Building> buildings = buildingMapper.selectByExample(example);
        List<BuildingDto> list = BeanMapper.mapList(buildings, BuildingDto.class);
        if (!list.isEmpty()) {
            log.info("给ECM同步楼宇信息:{}", JSONObject.toJSONString(list));
            list.stream().forEach(item -> {
                // 发MQ
                MqProducerUtil.sendBuildingMsg(item);
            });
        }
        return ResponseResult.buildSuccessResponse();
    }

    @Override
    public List<BuildingDto> getBuildingInfoByIdSet(List<Long> buildingId) {
        log.info("根据楼宇ID集合查询楼宇信息{}", JSONObject.toJSONString(buildingId));
        if (CollectionUtils.isEmpty(buildingId)) {
            return null;
        }
        BuildingExample buildingExample = new BuildingExample();
        buildingExample.createCriteria().andIdIn(buildingId);
        List<Building> listBuding = buildingMapper.selectByExample(buildingExample);
        if (CollectionUtils.isEmpty(listBuding)) {
            return null;
        }
        List<BuildingDto> buildingDto = BeanMapper.mapList(listBuding, BuildingDto.class);
        return buildingDto;
    }

    private void _addBuliding(BuildingSaveDto dto, ResponseResult<Object> result) {
        log.info("进行楼宇的新增操作，入参：{}", JSONObject.toJSONString(dto));
        ResponseResult<ShopDto> shopResp = shopService.queryById(dto.getShopId());
        if (null == shopResp || !shopResp.isSuccess() || null == shopResp.getData()) {
            throw new RuntimeException("根据shopId查询门店信息为空");
        }
        ShopDto shop = shopResp.getData();
        dto.setShopCode(shop.getCode());
        dto.setShopName(shop.getName());

        Building building = BeanMapper.map(dto, Building.class);
        building.setId(IdGenerator.nextId());
        building.setCreateBy(dto.getUpdateUser());
        if (BuildingConstant.BuildingStatusEnum.OPENED.getValue() == Integer.valueOf(dto.getStatus())) {
            building.setOpenTime(new Date());
        }

        if (!setLonAndLatInfo(building, result)) {
            return;
        }

        buildingMapper.insertSelective(building);

        // 发MQ
        MqProducerUtil.sendBuildingMsg(BeanMapper.map(building, BuildingDto.class));
    }

    private void _updateBuilding(BuildingSaveDto dto, ResponseResult<Object> result) {
        log.info("进行楼宇的更新操作，入参：{}", JSONObject.toJSONString(dto));
        ResponseResult<ShopDto> shopResp = shopService.queryById(dto.getShopId());
        if (null == shopResp || !shopResp.isSuccess() || null == shopResp.getData()) {
            throw new RuntimeException("根据shopId查询门店信息为空");
        }
        ShopDto shop = shopResp.getData();
        dto.setShopCode(shop.getCode());
        dto.setShopName(shop.getName());

        Building building = BeanMapper.map(dto, Building.class);
        if (BuildingConstant.BuildingStatusEnum.OPENED.getValue() == Integer.valueOf(dto.getStatus())) {
            building.setOpenTime(new Date());
        }
        building.setUpdateBy(dto.getUpdateUser());

        if (!setLonAndLatInfo(building, result)) {
            return;
        }

        buildingMapper.updateByPrimaryKeySelective(building);

        // 发MQ
        MqProducerUtil.sendBuildingMsg(BeanMapper.map(building, BuildingDto.class));
    }

    /**
     * 设置经纬度信息
     *
     * @param building
     */
    private boolean setLonAndLatInfo(Building building, ResponseResult<Object> result) {
        String url = lbsPointServiceUrl.concat("?address=").concat(building.getRegionName()).concat(building.getDetailAddress());
        try {
            String rt = restTemplate.getForObject(url, String.class);
            log.info("调用lbs服务获取的结果为：{}", rt);
            if (StringUtils.isBlank(rt)) {
                result.setSuccess(false);
                result.setCode("500");
                result.setMessage("内部LBS未返回任何数据");
            } else {
                JSONObject json = JSONObject.parseObject(rt);
                if (json.getBooleanValue("success")) {
                    building.setLon(new BigDecimal(json.getJSONObject("data").getDoubleValue("lon")));
                    building.setLat(new BigDecimal(json.getJSONObject("data").getDoubleValue("lat")));
                } else {
                    result.setSuccess(false);
                    result.setCode(json.getString("code"));
                    result.setMessage(json.getString("message"));
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("调用内部LBS服务出错", e);
            result.setSuccess(false);
            result.setCode("500");
            result.setMessage("内部LBS服务繁忙");
            return false;
        }
        return true;
    }

    private boolean _checkBuliding(BuildingDto dto) {
        if (dto.getId() == null) {
            BuildingExample example = new BuildingExample();
            example.createCriteria().andNameEqualTo(dto.getName());
            long count = buildingMapper.countByExample(example);
            return count > 0;
        } else {
            Building building = buildingMapper.selectByPrimaryKey(dto.getId());
            if (building.getName().equals(dto.getName())) {
                return false;
            } else {
                BuildingExample example = new BuildingExample();
                example.createCriteria().andNameEqualTo(dto.getName());
                long count = buildingMapper.countByExample(example);
                return count > 0;
            }
        }
    }

    @Override
    public ResponseResult<Pagination<BuildingDto>> pageSearchWeight(BuildingParam param) {
        Pagination<BuildingDto> pagination = new Pagination<>();
        List<BuildingDto> list = new ArrayList<>();
        Long count = buildingExtMapper.selectCountByWeight(param);
        if (count > 0) {
            list = buildingExtMapper.selectPageByWeight(param);
        }
        pagination.setDataList(list);
        pagination.setTotalCount(count);
        return ResponseResult.buildSuccessResponse(pagination);
    }

    @Override
    public ResponseResult<Object> updateBuildingByIds(BulidingUpdateListParam buildingUpdateParam) {
        log.info("批量修改楼宇排序{}", JSON.toJSONString(buildingUpdateParam));
        if (null == buildingUpdateParam) {
            return ResponseResult.buildFailResponse("9999", "参数错误");
        }
        if (CollectionUtils.isEmpty(buildingUpdateParam.getList()) || buildingUpdateParam.getList().size() <= 0) {
            return ResponseResult.buildFailResponse("9999", "参数错误");
        }
        int row = buildingExtMapper.updateBuildingById(buildingUpdateParam.getList());
        return ResponseResult.buildSuccessResponse(row);
    }
}
