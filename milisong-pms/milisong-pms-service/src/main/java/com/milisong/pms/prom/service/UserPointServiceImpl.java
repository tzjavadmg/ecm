package com.milisong.pms.prom.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.pms.prom.api.UserPointService;
import com.milisong.pms.prom.domain.UserPointWater;
import com.milisong.pms.prom.dto.Pagenation;
import com.milisong.pms.prom.dto.UserPointWaterDto;
import com.milisong.pms.prom.dto.UserPointWaterQueryDto;
import com.milisong.pms.prom.dto.UserPointWaterResDto;
import com.milisong.pms.prom.enums.PromotionResponseCode;
import com.milisong.pms.prom.mapper.UserPointWaterMapper;
import com.milisong.pms.prom.util.ConfigRedisUtils;
import com.milisong.pms.prom.util.UserPointUtil;
import com.milisong.ucs.api.UserService;
import com.milisong.ucs.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/4   10:34
 *    desc    : 用户积分业务实现类
 *    version : v1.0
 * </pre>
 */
@Slf4j
@RestController
public class UserPointServiceImpl implements UserPointService {


    @Autowired
    private UserPointWaterMapper userPointWaterMapper;

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult<Pagenation<UserPointWaterResDto>> queryByUserId(@RequestBody UserPointWaterQueryDto reqDto) {
        ResponseResult<Pagenation<UserPointWaterResDto>> checkParam = checkQueryParam(reqDto);
        if(!checkParam.isSuccess()){
            return checkParam;
        }
        Pagenation<UserPointWaterResDto> pagenation = new Pagenation<>();
        Integer count = userPointWaterMapper.queryPointWaterCount(reqDto);
        List<UserPointWaterResDto> list = new ArrayList<>();
        if(count > 0){
            List<UserPointWater> resultList = userPointWaterMapper.queryPointWater(reqDto);
            if(resultList!=null){
                list = BeanMapper.mapList(resultList, UserPointWaterResDto.class);
            }
        }
        pagenation.setRowCount(count);
        pagenation.setList(list);
        return ResponseResult.buildSuccessResponse(pagenation);
    }

    private ResponseResult<Pagenation<UserPointWaterResDto>> checkQueryParam(UserPointWaterQueryDto reqDto) {
        if(reqDto.getUserId() == null){
            return ResponseResult.buildFailResponse(PromotionResponseCode.POINT_QUERY_ILLEGAL.code(), PromotionResponseCode.POINT_QUERY_ILLEGAL.message());
        }
        if(reqDto.getBusinessLine() == null){
            return ResponseResult.buildFailResponse(PromotionResponseCode.POINT_QUERY_NO_BZL.code(), PromotionResponseCode.POINT_QUERY_NO_BZL.message());
        }
        if(reqDto.getPageSize() < 0){
            reqDto.setPageSize(10);
        }
        if(reqDto.getPageSize() >50){
            reqDto.setPageSize(50);
        }
        if(reqDto.getPageNo() < 0){
            reqDto.setPageNo(1);
        }
        return ResponseResult.buildSuccessResponse();
    }

    @Override
    @Transactional
    public ResponseResult<Boolean> saveUserPointWater(@RequestBody UserPointWaterDto dto) {
        ResponseResult<Boolean> responseResult = saveUserPointWater(dto, () -> {
                log.info("---插入积分流水信息---{}", JSON.toJSONString(dto));
                ResponseResult<Boolean> checkParamResult = checkParam(dto);
                if(!checkParamResult.isSuccess()){
                    return checkParamResult;
                }
            ResponseResult<Boolean> checkPowerHash = checkPowerHash(dto);
            if(!checkPowerHash.isSuccess()){
                return checkPowerHash;
            }
            dto.setId(IdGenerator.nextId());
            dto.setExpireTime(UserPointUtil.POINT_EXPIRE_DATE);
            int result = userPointWaterMapper.insertSelective(BeanMapper.map(dto, UserPointWater.class));
            if (result > 0) {
                UserDto userDto = new UserDto();
                userDto.setId(dto.getUserId());
                userDto.setUsefulPoint(dto.getPoint());
                userDto.setUserPointAction(dto.getAction());
                userDto.setIdempotent(UserPointUtil.generatePowerHash(dto));
                ResponseResult<Boolean> updateUser = userService.calcPoint(userDto);
                if (!updateUser.isSuccess()) {
                    log.info("插入用户积分流水成功，更新用户总积分失败，主动抛出异常，回滚积分记录信息");
                    return ResponseResult.buildFailResponse(PromotionResponseCode.POINT_SAVE_FAIL.code(), PromotionResponseCode.POINT_SAVE_FAIL.message());
                }
                return ResponseResult.buildSuccessResponse(true);
            } else {
                log.info("----插入积分流水信息失败---");
                return ResponseResult.buildFailResponse(PromotionResponseCode.POINT_SAVE_FAIL.code(), PromotionResponseCode.POINT_SAVE_FAIL.message());
            }
            });
        return responseResult;
    }

    private ResponseResult<Boolean> checkPowerHash(UserPointWaterDto dto) {
        UserPointWater userPointWater = userPointWaterMapper.queryForPowerHashBeforeSave(dto);
        if(userPointWater == null){
            return ResponseResult.buildSuccessResponse();
        }else{
            return ResponseResult.buildFailResponse(PromotionResponseCode.POINT_POWER_CHECK_DB_FAIL.code(), PromotionResponseCode.POINT_POWER_CHECK_DB_FAIL.message());
        }
    }

    @Override
    public ResponseResult<String> getPointRulePic() {
        String pointRulePic = ConfigRedisUtils.getPointRulePic();
        if(StringUtils.isBlank(pointRulePic)){
            return ResponseResult.buildFailResponse(PromotionResponseCode.POINT_RULE_NULL.code(),PromotionResponseCode.POINT_RULE_NULL.message());
        }
        return ResponseResult.buildSuccessResponse(pointRulePic);
    }

    private ResponseResult<Boolean> checkParam(UserPointWaterDto dto) {
        if (dto.getPoint() == null || dto.getPoint() < 1) {
            return ResponseResult.buildFailResponse(PromotionResponseCode.POINT_INVALID_POINT.code(), PromotionResponseCode.POINT_INVALID_POINT.message());
        }
        //用户id
        if(dto.getUserId() == null ){
            return ResponseResult.buildFailResponse(PromotionResponseCode.POINT_USER_ID_NULL.code(),PromotionResponseCode.POINT_USER_ID_NULL.message());
        }
        //流水收支类型
        if(dto.getIncomeType() == null ){
            return ResponseResult.buildFailResponse(PromotionResponseCode.POINT_INCOME_TYPE_NULL.code(),PromotionResponseCode.POINT_INCOME_TYPE_NULL.message());
        }
        if(dto.getRefId() == null ){
            return ResponseResult.buildFailResponse(PromotionResponseCode.POINT_REF_ID_NULL.code(),PromotionResponseCode.POINT_REF_ID_NULL.message());
        }
        if(dto.getRefType() == null ){
            return ResponseResult.buildFailResponse(PromotionResponseCode.POINT_REF_TYPE_ID_NULL.code(),PromotionResponseCode.POINT_REF_TYPE_ID_NULL.message());
        }
        if(dto.getAction() == null ){
            return ResponseResult.buildFailResponse(PromotionResponseCode.POINT_ACTION_NULL.code(),PromotionResponseCode.POINT_ACTION_NULL.message());
        }
        if(dto.getBusinessLine() == null ){
            return ResponseResult.buildFailResponse(PromotionResponseCode.POINT_BUSINESS_LINE_NULL.code(),PromotionResponseCode.POINT_BUSINESS_LINE_NULL.message());
        }
        return ResponseResult.buildSuccessResponse();
    }

    /**
     * 将事务和并发控制等安全措施和业务代码分开
     * @param dto
     * @param supplier
     * @return
     */
    private ResponseResult<Boolean> saveUserPointWater(UserPointWaterDto dto, Supplier<ResponseResult<Boolean>> supplier){
        ResponseResult<Boolean> responseResult = ResponseResult.buildSuccessResponse();
        try{
            if (!UserPointUtil.checkPowerHash(dto)) {
                log.info("----积分流水记录前，幂等校验，近期已经执行过记录动作-->{}", JSON.toJSONString(dto));
                return ResponseResult.buildFailResponse(PromotionResponseCode.POINT_POWER_CHECK_FAIL.code(), PromotionResponseCode.POINT_POWER_CHECK_FAIL.message());
            }
            responseResult = supplier.get();
        }catch (Exception e){
            responseResult = ResponseResult.buildFailResponse(PromotionResponseCode.POINT_SAVE_FAIL.code(), PromotionResponseCode.POINT_SAVE_FAIL.message());
            log.error("保存积分信息异常",e);
        }finally {
            if(!responseResult.isSuccess()){
                log.info("----回滚，释放幂等锁---{}",JSON.toJSONString(responseResult));
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                UserPointUtil.releasePowerHash(dto);
            }
            return responseResult;
        }
    }
}
