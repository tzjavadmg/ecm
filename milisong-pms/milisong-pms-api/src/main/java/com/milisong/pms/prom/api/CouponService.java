package com.milisong.pms.prom.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.CouponDto;
import com.milisong.pms.prom.dto.CouponQueryDto;
import com.milisong.pms.prom.dto.Pagenation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/11   17:02
 *    desc    : 券业务接口
 *    version : v1.0
 * </pre>
 */
@FeignClient("milisong-pms-service")
public interface CouponService {

    /**
     * 保存和更新 通过id
     * @param dto
     * @return
     */
    @PostMapping("/v1/CouponService/saveOrUpdate")
    ResponseResult<String> saveOrUpdate(@RequestBody CouponDto dto);

    /**
     * 查询by id
     * @param id
     * @return
     */
    @PostMapping("/v1/CouponService/query")
    ResponseResult<CouponDto> query(@RequestParam("id")Long id);


    /**
     * 条件查询券
     * @param dto
     * @return
     */
    @PostMapping("/v1/CouponService/queryByCondition")
    ResponseResult<Pagenation<CouponDto>> queryByCondition(@RequestBody CouponQueryDto dto);

    /**
     * 模糊名字查询券
     * @param dto
     * @return
     */
    @PostMapping("/v1/CouponService/queryByName")
    ResponseResult<List<CouponDto>> queryByName(@RequestBody CouponQueryDto dto);


    /**
     * 查询券列表
     * @param couponids
     * @return
     */
    @PostMapping("/v1/CouponService/queryByIds")
    ResponseResult<List<CouponDto>> queryByIds(@RequestParam("couponids") List<Long> couponids);

    /**
     * 下线券 ,入参：id
     * @param dto
     * @return
     */
    @PostMapping("/v1/CouponService/offline")
    ResponseResult<Boolean> offline(@RequestBody CouponDto dto);

}
