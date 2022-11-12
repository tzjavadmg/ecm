package com.milisong.ucs.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.ucs.api.UserDeliveryAddressService;
import com.milisong.ucs.domain.UserDeliveryAddress;
import com.milisong.ucs.dto.Pagenation;
import com.milisong.ucs.dto.SendBreakCouponRequest;
import com.milisong.ucs.dto.UserDeliveryAddressDto;
import com.milisong.ucs.dto.UserDeliveryAddressRequest;
import com.milisong.ucs.mapper.UserDeliveryAddressMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static com.milisong.ucs.enums.UserResponseCode.*;

/**
 * 用户地址管理
 * @author sailor wang
 * @date 2018/9/3 下午4:50
 * @description
 */
@Slf4j
@RestController
public class UserDeliveryAddressServiceImpl implements UserDeliveryAddressService {

    @Autowired
    private UserDeliveryAddressMapper addressMapper;

    /**
     * 保存地址
     * @param userDeliveryAddr
     * @return
     */
    @Override
    public ResponseResult<Boolean> saveDeliveryAddr(@RequestBody UserDeliveryAddressDto userDeliveryAddr) {
        if (userDeliveryAddr == null || userDeliveryAddr.getBusinessLine() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        try {
            //查询用户地址表中是否已经存在这个公司的地址信息，如果是，就更新
            UserDeliveryAddress deliveryAddress = addressMapper.queryDeliveryByCompanyIdAndUserId(userDeliveryAddr.getUserId(), userDeliveryAddr.getDeliveryOfficeBuildingId(), userDeliveryAddr.getBusinessLine());
            if(deliveryAddress == null){
                UserDeliveryAddress address = BeanMapper.map(userDeliveryAddr, UserDeliveryAddress.class);
                address.setId(IdGenerator.nextId());
                address.setCreateTime(new Date());
                address.setUpdateTime(new Date());
                int row = addressMapper.insertSelective(address);
                return ResponseResult.buildSuccessResponse(row > 0);
            }else{
                //更新
                userDeliveryAddr.setId(deliveryAddress.getId());
                return updateDeliveryAddr(userDeliveryAddr);
            }
        } catch (Exception e) {
            log.error("",e);
        }
        return ResponseResult.buildFailResponse(SAVE_USER_ADDR_EXCEPTION.code(),SAVE_USER_ADDR_EXCEPTION.message());
    }


    /**
     * 修改地址
     * @param userDeliveryAddr
     * @return
     */
    @Override
    public ResponseResult<Boolean> updateDeliveryAddr(@RequestBody UserDeliveryAddressDto userDeliveryAddr) {
        if (userDeliveryAddr == null || userDeliveryAddr.getId() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        try {
            UserDeliveryAddress address = BeanMapper.map(userDeliveryAddr, UserDeliveryAddress.class);
            address.setUpdateTime(new Date());
            int row = addressMapper.updateByPrimaryKeySelective(address);
            return ResponseResult.buildSuccessResponse(row>0);
        } catch (Exception e) {
            log.error("",e);
        }
        return ResponseResult.buildFailResponse(UPDATE_USER_ADDR_EXCEPTION.code(),UPDATE_USER_ADDR_EXCEPTION.message());
    }

    /**
     * 新增或更新用户地址信息，验证用户id和公司id，数据唯一
     * @param userDeliveryAddr
     * @return
     */
    @Override
    public ResponseResult<Boolean> saveOrUpdateTempDeliveryAddr(@RequestBody UserDeliveryAddressDto userDeliveryAddr) {
        Long userId = userDeliveryAddr.getUserId();
        Long deliveryOfficeBuildingId = userDeliveryAddr.getDeliveryOfficeBuildingId();
        Byte businessLine = userDeliveryAddr.getBusinessLine();
        if(userId == null || deliveryOfficeBuildingId == null || businessLine == null){
            return ResponseResult.buildFailResponse(SAVE_PARAM_INVALID.code(),SAVE_PARAM_INVALID.message());
        }
        UserDeliveryAddress queryDto = addressMapper.queryDeliveryByCompanyIdAndUserId(userId,deliveryOfficeBuildingId,businessLine);
        if(queryDto!=null){
            return ResponseResult.buildSuccessResponse(true);
        }else{
            //新增
            UserDeliveryAddress address = BeanMapper.map(userDeliveryAddr, UserDeliveryAddress.class);
            address.setId(IdGenerator.nextId());
            int row = addressMapper.insertSelective(address);
            return ResponseResult.buildSuccessResponse(row > 0);
        }
    }

    /**
     * 查询用户地址
     * @param request
     * @return
     */
    @Override
    public ResponseResult<UserDeliveryAddressDto> queryDeliveryAddr(@RequestBody UserDeliveryAddressRequest request) {
        if (request == null || request.getUserId() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        try {
            UserDeliveryAddress address = addressMapper.selectByUserid(request.getUserId(),request.getDeliveryOfficeBuildingId(),request.getBusinessLine());
            if (address != null){
                return ResponseResult.buildSuccessResponse(BeanMapper.map(address,UserDeliveryAddressDto.class));
            }
            return ResponseResult.buildSuccessResponse(null);
        } catch (Exception e) {
            log.error("",e);
        }
        return ResponseResult.buildFailResponse(QUERY_USER_ADDR_EXCEPTION.code(),QUERY_USER_ADDR_EXCEPTION.message());
    }

    /**
     * 根据地址id获取地址信息
     */
    @Override
    public ResponseResult<UserDeliveryAddressDto> queryDeliveryById(Long addressId) {
    	UserDeliveryAddress deliveryAddress =  addressMapper.selectByPrimaryKey(addressId);
    	if (deliveryAddress!=null) {
            return ResponseResult.buildSuccessResponse(BeanMapper.map(deliveryAddress,UserDeliveryAddressDto.class));
    	}
        return ResponseResult.buildFailResponse();
    }

    /**
     * 根据手机号，楼宇楼层公司获取地址信息
     */
    @Override
    public ResponseResult<UserDeliveryAddressDto> queryDeliveryAddrByManyWhere(String mobileNo,
    		Long deliveryOfficeBuildingId, String deliveryFloor, String deliveryCompany) {
    	UserDeliveryAddress deliveryAddress =  addressMapper.selectDeliveryAddrByManyWhere(mobileNo, deliveryOfficeBuildingId, deliveryFloor, deliveryCompany);
    	if (deliveryAddress!=null) {
            return ResponseResult.buildSuccessResponse(BeanMapper.map(deliveryAddress,UserDeliveryAddressDto.class));
    	}
    	 return ResponseResult.buildFailResponse();
    }

    @Override
    public ResponseResult<UserDeliveryAddressDto> queryDeliveryAddrByMobileAndId(Long id, String mobile) {
        UserDeliveryAddress userDeliveryAddress = addressMapper.selectByMobileAndId(id,mobile);
        if(userDeliveryAddress == null){
            return ResponseResult.buildSuccessResponse();
        }else{
            return ResponseResult.buildSuccessResponse(BeanMapper.map(userDeliveryAddress,UserDeliveryAddressDto.class));
        }
    }

    /**
     * 根据公司、保存地址时间、手机号等查询用户
     * @param sendBreakCouponRequest
     * @return
     */
    @Override
    public ResponseResult<Pagenation<UserDeliveryAddressDto>> queryUserByDevlieryAddr(@RequestBody SendBreakCouponRequest sendBreakCouponRequest) {
        log.info("sendBreakCouponRequest -> {}",sendBreakCouponRequest);
        if (sendBreakCouponRequest.getBusinessLine() == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        Pagenation<UserDeliveryAddressDto> pagenation = new Pagenation<>();
        pagenation.setRowCount(0);
        pagenation.setList(Lists.newArrayList());
        if (sendBreakCouponRequest.getPageNo() < 0){
            sendBreakCouponRequest.setPageNo(1);
        }
        if (sendBreakCouponRequest.getPageSize() > 500){
            sendBreakCouponRequest.setPageSize(500);
        }

        int rowCount = addressMapper.countUserByDevlieryAddr(sendBreakCouponRequest);
        if (rowCount > 0){
            List<UserDeliveryAddressDto> list = addressMapper.queryUserByDevlieryAddr(sendBreakCouponRequest);
            pagenation.setRowCount(rowCount);
            pagenation.setPageCount((int) Math.ceil((double)rowCount/sendBreakCouponRequest.getPageSize()));
            pagenation.setList(list);
        }
        return ResponseResult.buildSuccessResponse(pagenation);
    }

    @Override
    public ResponseResult<List<UserDeliveryAddressDto>> queryDeliveryCompany(@RequestParam("companyName") String companyName, @RequestParam("businessLine") Byte buzLine) {
        if (StringUtils.isBlank(companyName) || buzLine == null){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        List<UserDeliveryAddressDto> list = addressMapper.queryDeliveryCompany(companyName.trim(),buzLine);
        return ResponseResult.buildSuccessResponse(list);
    }

    @Override
    public ResponseResult<List<UserDeliveryAddressDto>> queryDeliveryCompanyNames(@RequestParam("deliveryOfficeBuildingIds") List<Long> deliveryOfficeBuildingIdList) {
        if (CollectionUtils.isEmpty(deliveryOfficeBuildingIdList)){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        List<UserDeliveryAddressDto> list = addressMapper.queryDeliveryCompanyNames(deliveryOfficeBuildingIdList);
        return ResponseResult.buildSuccessResponse(list);
    }
}