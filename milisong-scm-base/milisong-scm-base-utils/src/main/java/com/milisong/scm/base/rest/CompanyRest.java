package com.milisong.scm.base.rest;

import com.alibaba.fastjson.JSONObject;
import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.base.api.CompanyService;
import com.milisong.scm.base.dto.CompanyDto;
import com.milisong.scm.base.dto.CompanyPolymerizationDto;
import com.milisong.scm.base.dto.param.CompanySearchDto;
import com.milisong.scm.base.dto.param.CompanyUpdateListParam;
import com.milisong.upms.utils.UserInfoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

@Api(tags = "公司管理")
@Slf4j
@RestController
@RequestMapping("/company")
public class CompanyRest {
    @Autowired
    private CompanyService companyService;

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    @ApiOperation("分页查询公司信息")
    @PostMapping("/page-search")
    public ResponseResult<Pagination<CompanyDto>> pageSearch(@RequestBody CompanySearchDto param) {
        return companyService.pageSearch(param);
    }

    /**
     * 配送顺序管理用分页查询
     *
     * @param param
     * @return
     */
    @ApiOperation("配送顺序管理用分页查询")
    @PostMapping("/page-search-with-weight")
    public ResponseResult<Pagination<CompanyDto>> pageSearchWithWeigth(@RequestBody CompanySearchDto param) {
        return companyService.pageSearch(param);
    }

    /**
     * 根据id查询公司详情
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id查询公司详情")
    @GetMapping("/query-by-id")
    public ResponseResult<CompanyPolymerizationDto> queryById(@RequestParam(value = "id") Long id) {
        return companyService.queryById(id);
    }

    /**
     * 根据id查询公司详情（内部调用）
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id查询公司详情", hidden = true)
    @GetMapping("/inside-query-by-id")
    public ResponseResult<CompanyPolymerizationDto> insideQueryById(@RequestParam(value = "id") Long id) {
        return companyService.queryAllById(id);
    }

    /**
     * 新增
     *
     * @param dto
     * @return
     */
    @ApiOperation("新增公司")
    @PostMapping("/add")
    public ResponseResult<String> add(@RequestBody CompanyPolymerizationDto dto) {
        String updateUser = UserInfoUtil.buildUpdateBy();
        log.info("新增公司信息：{},操作人：{}", JSONObject.toJSONString(dto), updateUser);
        dto.setCreateBy(updateUser);
        return companyService.saveOrUpdate(dto);
    }

    /**
     * 编辑
     *
     * @param dto
     * @return
     */
    @ApiOperation("编辑公司")
    @PostMapping("/update")
    public ResponseResult<String> update(@RequestBody CompanyPolymerizationDto dto) {
        String updateUser = UserInfoUtil.buildUpdateBy();
        log.info("编辑公司信息：{},操作人：{}", JSONObject.toJSONString(dto), updateUser);
        dto.setUpdateBy(updateUser);
        return companyService.saveOrUpdate(dto);
    }

    /**
     * 修改公司排序
     *
     * @param param
     * @return
     */
    @ApiOperation("保存公司的配送顺序")
    @PostMapping("/update-weight")
    public ResponseResult<String> updateBuildingByIdWeight(@RequestBody CompanyUpdateListParam param) {
        log.info("修改公司排序{}", JSONObject.toJSONString(param));
        if (null == param) {
            return ResponseResult.buildFailResponse("400", "入参对象不能为空");
        }
        if (CollectionUtils.isEmpty(param.getList()) || param.getList().size() <= 0) {
            return ResponseResult.buildFailResponse("400", "配送顺序list必须有值");
        }
        param.setUpdateBy(UserInfoUtil.buildUpdateBy());
        return companyService.updateWeight(param);
    }

    @GetMapping("/sync")
    public ResponseResult syncCompany() {
        return companyService.syncAllCompany();
    }

    @PostMapping("/set-meal-time")
    public ResponseResult<String> updateMealTime(@RequestBody CompanyDto dto){
        String updateUser = UserInfoUtil.buildUpdateBy();
        log.info("配置公司配送时间信息：{},操作人：{}", JSONObject.toJSONString(dto), updateUser);
        if (dto.getId() == null) {
            dto.setCreateBy(updateUser);
        } else {
            dto.setUpdateBy(updateUser);
        }
        return companyService.saveOrUpdateMealTime(dto);
    }

//    @PostMapping("/update/meal-time")
//    public ResponseResult<String> updateMealTime(@RequestBody CompanyPolymerizationDto dto){
//        String updateUser = UserInfoUtil.buildUpdateBy();
//        log.info("编辑公司配送时间信息：{},操作人：{}", JSONObject.toJSONString(dto), updateUser);
//        dto.setUpdateBy(updateUser);
//        return companyService.saveOrUpdate(dto);
//    }
}
