package com.milisong.ecm.common.user.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.milisong.ecm.common.dto.CompanyDto;


/**
 * @author zhaozhonghui
 * @date 2018-09-21
 */
public interface CompanyService {

    /**
     * 保存接受MQ的公司信息
     * @param msg
     */
	@PostMapping(value = "/v1/CompanyService/saveMqCompany")
    void saveMqCompany(@RequestParam("msg") String msg);
    
    /**
     * 修改楼层
     * @param company
     */
	@PostMapping(value = "/v1/CompanyService/updateFloor")
    void updateFloor(@RequestBody CompanyDto company);
    
    /**
     * 修改公司名称
     * @param company
     */
	@PostMapping(value = "/v1/CompanyService/updateCompanyName")
    void updateCompanyName(@RequestBody CompanyDto company);
}

