package com.milisong.scm.rest;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.util.BeanMapper;
import com.milisong.scm.constant.SysConstant;
import com.milisong.scm.shop.api.BuildingService;
import com.milisong.scm.shop.dto.building.BuildingDto;
import com.milisong.scm.shop.dto.building.BuildingPageResutlDto;
import com.milisong.scm.shop.dto.building.BuildingSaveDto;
import com.milisong.scm.shop.param.BuildingParam;
import com.milisong.scm.shop.param.BulidingUpdateListParam;
import com.milisong.upms.utils.UserInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 门店信息rest
 *
 * @author youxia 2018年9月4日
 */
@Slf4j
@RestController
@RequestMapping("/building")
public class BuildingRest {
    @Autowired
    private BuildingService buildingService;

    /**
     * 根据楼宇id查询详情
     *
     * @param id
     * @return
     */
    @GetMapping("/get-building-by-id")
    public ResponseResult<BuildingDto> getById(@RequestParam("id") Long id) {
        BuildingDto building = buildingService.getBuildingInfoById(id);
        if (null != building) {
            return ResponseResult.buildSuccessResponse(building);
        } else {
            return ResponseResult.buildFailResponse(SysConstant.SYSTEM_INFO.FALSE.getCode(),
                    SysConstant.SYSTEM_INFO.FALSE.getDesc());
        }
    }

    /**
     * 楼宇管理页面分页查询
     *
     * @param param
     * @return
     */
    @PostMapping("/get-building-by-page")
    public ResponseResult<Pagination<BuildingPageResutlDto>> pageSearch(@RequestBody BuildingParam param) {
        ResponseResult<String> checkResult = checkShopPermission(param.getShopId());
        if (!checkResult.isSuccess()) {
            return ResponseResult.buildFailResponse(checkResult.getCode(), checkResult.getMessage());
        }
        ResponseResult<Pagination<BuildingPageResutlDto>> result = ResponseResult.buildSuccessResponse();
        ResponseResult<Pagination<BuildingDto>> data = buildingService.pageSearch(param);
        if (!data.isSuccess()) {
            result.setSuccess(false);
            result.setCode(data.getCode());
            result.setMessage(data.getMessage());
            result.setDetailMessage(data.getMessage());
            return result;
        }

        Pagination<BuildingPageResutlDto> page = new Pagination<>();
        page.setPageNo(data.getData().getPageNo());
        page.setPageSize(data.getData().getPageSize());
        page.setTotalCount(data.getData().getTotalCount());

        if (null != data.getData()
                && !CollectionUtils.isEmpty(data.getData().getDataList())) {
            List<BuildingDto> dataList = data.getData().getDataList();

            List<BuildingPageResutlDto> rt = new ArrayList<>(dataList.size());

            // 得到楼宇id的set
            Set<Long> buildingIds = dataList.stream().map(item -> item.getId()).collect(Collectors.toSet());
//            Map<Long, Integer> map = companyService.queryCompayCountByBuildingIds(buildingIds);
            Map<Long, Integer> map = new HashMap<>();
            log.info("计算楼宇的公司数量结果：{}", JSONObject.toJSONString(map));

            dataList.stream().forEach(item -> {
                BuildingPageResutlDto dto = BeanMapper.map(item, BuildingPageResutlDto.class);
                dto.setCompanyCount(getCompanyCount(dto.getId(), map));
                rt.add(dto);
            });
            page.setDataList(rt);
        }
        result.setData(page);
        return result;
    }

    /**
     * 不分页查询楼宇信息
     *
     * @param param
     * @return
     */
    @PostMapping("/search")
    public ResponseResult<List<BuildingDto>> search(@RequestBody BuildingParam param) {
        return ResponseResult.buildSuccessResponse(buildingService.search(param));
    }

    /**
     * 新增或者修改楼宇
     *
     * @param dto
     * @return
     */
    @PostMapping("/save-or-update")
    public ResponseResult<Object> saveOrUpdate(@RequestBody BuildingDto dto) {
        BuildingSaveDto param = BeanMapper.map(dto, BuildingSaveDto.class);
        param.setUpdateUser(UserInfoUtil.buildUpdateBy());
        return buildingService.saveBuilding(param);
    }

    /**
     * @return
     */
    @PostMapping("/send-buildings")
    public ResponseResult<Object> sendBuildingToEcm() {
        return buildingService.sendBuildingToEcm();
    }

    /**
     * 得到公司的数量
     *
     * @param buildingId
     * @param map
     * @return
     */
    private Integer getCompanyCount(Long buildingId, Map<Long, Integer> map) {
        if (null == map || map.isEmpty() || null == map.get(buildingId)) {
            return 0;
        }
        return map.get(buildingId);
    }

    /**
     * 楼宇管理页面分页查询-排序
     *
     * @param param
     * @return
     */
    @PostMapping("/get-building-by-page-weight")
    public ResponseResult<Pagination<BuildingDto>> pageSearchByWeight(@RequestBody BuildingParam param) {
        ResponseResult<String> checkResult = checkShopPermission(param.getShopId());
        if (!checkResult.isSuccess()) {
            return ResponseResult.buildFailResponse(checkResult.getCode(), checkResult.getMessage());
        }
        ResponseResult<Pagination<BuildingDto>> data = buildingService.pageSearchWeight(param);
        return data;
    }

    /**
     * 修改楼宇排序
     *
     * @param buildingUpdateParam
     * @return
     */
    @PostMapping("/update-building-by-id-weight")
    public ResponseResult<Object> updateBuildingByIdWeight(@RequestBody BulidingUpdateListParam buildingUpdateParam) {
        log.info("修改楼宇排序{}", JSONObject.toJSONString(buildingUpdateParam));
        if (null == buildingUpdateParam) {
            return ResponseResult.buildFailResponse("9999", "参数错误");
        }
        if (CollectionUtils.isEmpty(buildingUpdateParam.getList()) || buildingUpdateParam.getList().size() <= 0) {
            return ResponseResult.buildFailResponse("9999", "参数错误");
        }
        return buildingService.updateBuildingByIds(buildingUpdateParam);
    }

    private ResponseResult<String> checkShopPermission(Long shopId) {
        if (!UserInfoUtil.checkShopPermission(shopId)) {
            return ResponseResult.buildFailResponse(SysConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getCode(), SysConstant.SYSTEM_INFO.NO_SHOP_PERMISSION.getDesc());
        } else {
            return ResponseResult.buildSuccessResponse();
        }
    }
}
