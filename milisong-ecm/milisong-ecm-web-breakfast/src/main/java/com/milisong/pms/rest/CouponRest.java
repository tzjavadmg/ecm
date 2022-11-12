package com.milisong.pms.rest;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.breakfast.enums.BusinessLine;
import com.milisong.pms.prom.api.CouponService;
import com.milisong.pms.prom.dto.CouponDto;
import com.milisong.pms.prom.dto.CouponQueryDto;
import com.milisong.pms.prom.dto.Pagenation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/13   13:39
 *    desc    : 券业务rest层
 *    version : v1.0
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping("/v1/coupon/")
public class CouponRest {

    @Autowired
    private CouponService couponService;

    /**
     * 新增或更新
     * @param dto
     * @return
     */
    @PostMapping("save-or-update")
    ResponseResult<String> saveOrUpdate(@RequestBody CouponDto dto){
        dto.setBusinessLine(BusinessLine.BREAKFAST.getCode());
        ResponseResult<String> responseResult = couponService.saveOrUpdate(dto);
        return responseResult;
    }

    /**
     * 查询，通过id
     * @param id
     * @return
     */
    @GetMapping("query")
    ResponseResult<CouponDto> query(@RequestParam("id")Long id){
        ResponseResult<CouponDto> responseResult = couponService.query(id);
        return responseResult;
    }



    /**
     * 条件查询，分页
     * @param dto
     * @return
     */
    @PostMapping("query-by-condition")
    ResponseResult<Pagenation<CouponDto>> queryByCondition(@RequestBody CouponQueryDto dto){
        dto.setBusinessLine(BusinessLine.BREAKFAST.getCode());
        ResponseResult<Pagenation<CouponDto>> responseResult = couponService.queryByCondition(dto);
        return responseResult;
    }

    /**
     * name模糊查询
     * @param name
     * @param type
     * @return
     */
    @GetMapping("query-by-name")
    ResponseResult<List<CouponDto>> queryByName(@RequestParam("name") String name,@RequestParam("type")Byte type,@RequestParam(value = "pageNo",required = false)Integer pageNo, @RequestParam(value = "pageSize", required = false)Integer pageSize ){
        CouponQueryDto dto = new CouponQueryDto();
        dto.setName(name);
        dto.setType(type);
        dto.setPageNo(pageNo);
        dto.setPageSize(pageSize);
        dto.setBusinessLine(BusinessLine.BREAKFAST.getCode());
        return couponService.queryByName(dto);
    }

    /**
     * 下线券
     * @param dto
     * @return
     */
    @PostMapping("offline")
    ResponseResult<Boolean> offline(@RequestBody CouponDto dto){
        return couponService.offline(dto);
    }
}
