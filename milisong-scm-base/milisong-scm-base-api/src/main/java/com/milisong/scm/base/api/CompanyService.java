package com.milisong.scm.base.api;

import com.farmland.core.api.Pagination;
import com.farmland.core.api.ResponseResult;
import com.milisong.scm.base.dto.CompanyDto;
import com.milisong.scm.base.dto.CompanyMealAddressDto;
import com.milisong.scm.base.dto.CompanyPolymerizationDto;
import com.milisong.scm.base.dto.param.CompanySearchDto;
import com.milisong.scm.base.dto.param.CompanyUpdateListParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("milisong-scm-base-service")
public interface CompanyService {
    /**
     * 分页查询公司信息
     *
     * @param param
     * @return
     */
    @PostMapping("/base/company/page")
    ResponseResult<Pagination<CompanyDto>> pageSearch(@RequestBody CompanySearchDto param);

    /**
     * 配送顺序管理用分页查询
     *
     * @param param
     * @return
     */
    @PostMapping("/base/company/page-weight")
    ResponseResult<Pagination<CompanyDto>> pageSearchWithWeigth(@RequestBody CompanySearchDto param);

    /**
     * 根据公司id查询公司详情（查询有效的）
     *
     * @param id
     * @return
     */
    @GetMapping("/base/company/query-by-id")
    ResponseResult<CompanyPolymerizationDto> queryById(@RequestParam("id") Long id);

    /**
     * 根据公司id查询公司详情（包含删除的取餐点）
     *
     * @param id
     * @return
     */
    @GetMapping("/base/company/inside-query-by-id")
    ResponseResult<CompanyPolymerizationDto> queryAllById(@RequestParam("id") Long id);

    /**
     * 根据公司id查询基本的公司信息
     *
     * @param id
     * @return
     */
    @GetMapping("/base/company/simple-query-by-id")
    ResponseResult<CompanyDto> querySimpleById(@RequestParam("id") Long id);

    /**
     * 新增或者编辑
     *
     * @param dto
     * @return
     */
    @PostMapping("/base/company/save-or-update")
    ResponseResult<String> saveOrUpdate(@RequestBody CompanyPolymerizationDto dto);

    /**
     * 修改公司权重
     *
     * @param param
     * @return
     */
    @PostMapping("/base/company/update-weight")
    ResponseResult<String> updateWeight(CompanyUpdateListParam param);

    /**
     * 根据公司id查询取餐点list
     *
     * @param id
     * @return
     */
    @GetMapping("/base/company/query-meal-address")
    ResponseResult<List<CompanyMealAddressDto>> queryMealAddressList(@RequestParam("id") Long id);

    /**
     * 同步所有公司
     */
    @GetMapping("/base/company/sync")
    ResponseResult syncAllCompany();

    /**
     * 保存/更新配送时段
     * @param dto
     * @return
     */
    @PostMapping("/base/company/update/meal-time")
    ResponseResult<String> saveOrUpdateMealTime(@RequestBody CompanyDto dto);

    /**
     * 根据门店ID查公司
     * @param shopId
     * @return
     */
    @GetMapping("/base/company/query-by-shop-id")
    ResponseResult<List<CompanyPolymerizationDto>> queryByShopId(@RequestParam("shopId") Long shopId);

    @GetMapping("/base/company/query-all-meal-address")
    List<CompanyMealAddressDto> queryMealAddress();

    @PostMapping("/base/company/update-company-meal-address-pic")
    void updateCompanyMealAddressPic(CompanyMealAddressDto dto);
}
