package com.milisong.ecm.common.user.service;

import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.milisong.ecm.common.user.api.ApplyCompanyService;
import com.milisong.ecm.common.user.domain.ApplyCompany;
import com.milisong.ecm.common.user.enums.ApplyCompanyResponseCode;
import com.milisong.ecm.common.user.mapper.ApplyCompanyMapper;
import com.milisong.ecm.common.util.RedisKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/18   14:07
 *    desc    : 申请开通楼宇业务实现类
 *    version : v1.0
 * </pre>
 */
@Slf4j
@RestController
public class ApplyCompanyServiceImpl implements ApplyCompanyService {

    @Autowired
    private ApplyCompanyMapper applyCompanyMapper;

    @Override
    public ResponseResult<Boolean> save(@RequestBody ApplyCompany dto) {
        ResponseResult<Boolean> checkForSave = checkForSave(dto);
        if(!checkForSave.isSuccess()){
            return checkForSave;
        }
        dto.setId(IdGenerator.nextId());
        applyCompanyMapper.insertSelective(dto);
        return ResponseResult.buildSuccessResponse(true);
    }

    /**
     * save操作参数校验
     * @param dto
     * @return
     */
    private ResponseResult<Boolean> checkForSave(ApplyCompany dto){
        if(StringUtils.isBlank(dto.getItemName())){
            return ResponseResult.buildFailResponse(ApplyCompanyResponseCode.INVALID_ITEM_NAME.code(),ApplyCompanyResponseCode.INVALID_ITEM_NAME.message());
        }
        if(dto.getItemName().length() > 100){
            dto.setItemName(dto.getItemName().substring(0,100));
        }
        if(dto.getItemPeople()!= null && dto.getItemPeople().length() > 10){
            dto.setItemPeople(dto.getItemPeople().substring(0,10));
        }
        /*if(dto.getBusinessLine() == null){
            return ResponseResult.buildFailResponse(ApplyCompanyResponseCode.INVALID_BUSINESS_LINE.code(),ApplyCompanyResponseCode.INVALID_BUSINESS_LINE.message());
        }*/
        return ResponseResult.buildSuccessResponse();
    }
}
