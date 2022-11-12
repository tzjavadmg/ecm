package com.milisong.pms.prom.api;

import com.farmland.core.api.ResponseResult;
import com.milisong.pms.prom.dto.Pagenation;
import com.milisong.pms.prom.dto.UserPointWaterDto;
import com.milisong.pms.prom.dto.UserPointWaterQueryDto;
import com.milisong.pms.prom.dto.UserPointWaterResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/4   10:28
 *    desc    : 用户积分业务接口
 *    version : v1.0
 * </pre>
 */
@FeignClient("milisong-pms-service")
public interface UserPointService {

    /**查询用户积分流水
     * 入参字段:businessLine,userId,pageNo,pageSize
     * @param reqDto
     * @return
     */
    @PostMapping("/v1/UserPointService/queryByUserId")
    ResponseResult<Pagenation<UserPointWaterResDto>> queryByUserId(@RequestBody UserPointWaterQueryDto reqDto);

    /**插入用户积分流水记录
     * 入参字段:userId,action,incomeType,refType,refId,point,expireTime,remark,businessLine
     * @param dto
     * @return
     */
    @PostMapping("/v1/UserPointService/saveUserPointWater")
    ResponseResult<Boolean> saveUserPointWater(@RequestBody UserPointWaterDto dto);

    /**
     * 获取积分规则图片
     * @return
     */
    @PostMapping("/v1/UserPointService/getPointRulePic")
    ResponseResult<String> getPointRulePic();
}
