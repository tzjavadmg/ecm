package com.milisong.pms.prom.service;

import com.alibaba.fastjson.JSON;
import com.farmland.core.api.ResponseResult;
import com.farmland.core.db.IdGenerator;
import com.farmland.core.util.BeanMapper;
import com.milisong.pms.prom.api.CouponService;
import com.milisong.pms.prom.domain.Coupon;
import com.milisong.pms.prom.dto.CouponDto;
import com.milisong.pms.prom.dto.CouponQueryDto;
import com.milisong.pms.prom.dto.Pagenation;
import com.milisong.pms.prom.enums.CouponEnum;
import com.milisong.pms.prom.enums.PromotionResponseCode;
import com.milisong.pms.prom.mapper.CouponMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.milisong.pms.prom.enums.PromotionResponseCode.REQUEST_PARAM_EMPTY;

/**
 * <pre>
 *    author  : tianhaibo
 *    email   : tianhaibo@jshuii.com
 *    time    : 2019/1/11   17:09
 *    desc    : 券业务实现类
 *    version : v1.0
 * </pre>
 */
@Slf4j
@RestController
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public ResponseResult<String> saveOrUpdate(@RequestBody CouponDto dto) {
        log.info("----coupon-info---{}",JSON.toJSONString(dto));
        if(dto.getId() == null){
            //save
            log.info("---save---");
            ResponseResult<String> checkSave = checkForSave(dto);
            if(!checkSave.isSuccess()){
                return checkSave;
            }
            dto.setId(IdGenerator.nextId());
            dto.setStatus(CouponEnum.STATUS.ONLINE.getCode());
            int result = couponMapper.insertSelective(BeanMapper.map(dto,Coupon.class));
            if(result == 0){
                return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_SAVE_FAILED.code(),PromotionResponseCode.COUPON_SAVE_FAILED.message());
            }
        }else{
            //update
            log.info("---update---");
            ResponseResult<String> checkUpdate = checkForUpdate(dto);
            couponMapper.updateByPrimaryKeySelective(BeanMapper.map(dto,Coupon.class));
            if(!checkUpdate.isSuccess()){
                return checkUpdate;
            }
        }
        return ResponseResult.buildSuccessResponse();
    }

    private ResponseResult<String> checkForUpdate(CouponDto dto) {
        if(dto.getBusinessLine() == null){
            return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_NO_BZL.code(),PromotionResponseCode.COUPON_NO_BZL.message());
        }
        if(dto.getLimitDays()!= null && dto.getLimitDays() < 0){
            return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_LIMIT_DAYS_ERROR.code(),PromotionResponseCode.COUPON_LIMIT_DAYS_ERROR.message());
        }
        if(dto.getValidDays()!= null && dto.getValidDays() < 0){
            return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_VALID_DAYS_ERROR.code(),PromotionResponseCode.COUPON_VALID_DAYS_ERROR.message());
        }
        if(dto.getType().equals(CouponEnum.TYPE.DISCOUNT.getCode())){
            if(dto.getDiscount()!= null
                    &&(dto.getDiscount().compareTo(BigDecimal.ZERO) <0
                    ||dto.getDiscount().compareTo(new BigDecimal("10"))>0)){

                return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_DISCOUNT_ERROR.code(),PromotionResponseCode.COUPON_DISCOUNT_ERROR.message());
            }
        }
        if(dto.getType().equals(CouponEnum.TYPE.GOODS.getCode())){
            if(dto.getDiscount()!= null
                    &&(dto.getDiscount().compareTo(BigDecimal.ZERO) <0)){
                return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_DISCOUNT_ERROR.code(),PromotionResponseCode.COUPON_DISCOUNT_ERROR.message());
            }
        }
        //校验code必须为非中文
        if(StringUtils.isNotBlank(dto.getGoodsCode())){
            if(checkCountName(dto.getGoodsCode())){
                return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_GOODS_CODE_HAS_CHINESE.code(),PromotionResponseCode.COUPON_GOODS_CODE_HAS_CHINESE.message());
            }
        }
        return ResponseResult.buildSuccessResponse();
    }

    private ResponseResult<String> checkForSave(CouponDto dto) {
        if(dto.getBusinessLine() == null){
            return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_NO_BZL.code(),PromotionResponseCode.COUPON_NO_BZL.message());
        }
        if(dto.getType() == null){
            return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_NO_TYPE.code(),PromotionResponseCode.COUPON_NO_TYPE.message());
        }
        if(dto.getType().equals(CouponEnum.TYPE.GOODS.getCode()) ){
            if(dto.getLabel() == null){
                ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_GOODS_INVALID_PACKAGE.code(),PromotionResponseCode.COUPON_GOODS_INVALID_PACKAGE.message());
            }
            if(dto.getLabel()!=CouponEnum.LABEL.PACKAGE.getCode()
                    &&dto.getLabel()!=CouponEnum.LABEL.SINGLE.getCode()){
                ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_GOODS_INVALID_PACKAGE.code(),PromotionResponseCode.COUPON_GOODS_INVALID_PACKAGE.message());
            }
        }
        if((dto.getType().equals(CouponEnum.TYPE.GOODS.getCode()))
                && StringUtils.isBlank(dto.getGoodsCode())){
            return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_NO_GOODS.code(),PromotionResponseCode.COUPON_NO_GOODS.message());
        }
        if(dto.getDiscount() == null){
            return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_NO_DISCOUNT.code(),PromotionResponseCode.COUPON_NO_DISCOUNT.message());
        }
        if(StringUtils.isBlank(dto.getRule())){
            return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_NO_RULE.code(),PromotionResponseCode.COUPON_NO_RULE.message());
        }
        if(StringUtils.isBlank(dto.getRemark())){
            return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_NO_REMARK.code(),PromotionResponseCode.COUPON_NO_REMARK.message());
        }
        if(dto.getLimitDays()== null ){
            return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_LIMIT_DAYS_MUST.code(),PromotionResponseCode.COUPON_LIMIT_DAYS_MUST.message());
        }
        if(dto.getLimitDays()!= null && dto.getLimitDays() < 0){
            return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_LIMIT_DAYS_ERROR.code(),PromotionResponseCode.COUPON_LIMIT_DAYS_ERROR.message());
        }
        if(dto.getValidDays()!= null && dto.getValidDays() < 0){
            return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_VALID_DAYS_ERROR.code(),PromotionResponseCode.COUPON_VALID_DAYS_ERROR.message());
        }
        if(dto.getType().equals(CouponEnum.TYPE.DISCOUNT.getCode())){
            if(dto.getDiscount()!= null
                    &&(dto.getDiscount().compareTo(BigDecimal.ZERO) <0
                    ||dto.getDiscount().compareTo(new BigDecimal("10"))>0)){

                return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_DISCOUNT_ERROR.code(),PromotionResponseCode.COUPON_DISCOUNT_ERROR.message());
            }
        }
        if(dto.getType().equals(CouponEnum.TYPE.GOODS.getCode())){
            if(dto.getDiscount()!= null
                    &&(dto.getDiscount().compareTo(BigDecimal.ZERO) <0)){
                return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_DISCOUNT_ERROR.code(),PromotionResponseCode.COUPON_DISCOUNT_ERROR.message());
            }
        }
        //校验code必须为非中文
        if(StringUtils.isNotBlank(dto.getGoodsCode())){
            if(checkCountName(dto.getGoodsCode())){
                return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_GOODS_CODE_HAS_CHINESE.code(),PromotionResponseCode.COUPON_GOODS_CODE_HAS_CHINESE.message());
            }
        }
        return ResponseResult.buildSuccessResponse();
    }

    @Override
    public ResponseResult<CouponDto> query(@RequestParam("id") Long id) {
        Coupon coupon = couponMapper.selectByPrimaryKey(id);
        if(coupon!= null){
            return ResponseResult.buildSuccessResponse(BeanMapper.map(coupon,CouponDto.class));
        }
        return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_NOT_FOUND.code(),PromotionResponseCode.COUPON_NOT_FOUND.message());
    }

    @Override
    public ResponseResult<Pagenation<CouponDto>> queryByCondition(@RequestBody CouponQueryDto dto) {
        ResponseResult<Pagenation<CouponDto>> checkResult = checkForQuery(dto);
        if(!checkResult.isSuccess()){
            return checkResult;
        }
        Pagenation<CouponDto> pagenation = new Pagenation<>();
        List<CouponDto> list = new ArrayList<>();
        int count = couponMapper.queryByTypeNameStatusCount(dto);
        if(count != 0){
            list = couponMapper.queryByTypeNameStatus(dto);
        }
        pagenation.setRowCount(count);
        pagenation.setList(list);
        return ResponseResult.buildSuccessResponse(pagenation);
    }

    @Override
    public ResponseResult<List<CouponDto>> queryByName(@RequestBody CouponQueryDto dto) {
        ResponseResult<Pagenation<CouponDto>> responseResult = checkForQuery(dto);
        if(!responseResult.isSuccess()){
            return ResponseResult.buildFailResponse(responseResult.getCode(),responseResult.getMessage());
        }
        dto.setStatus(CouponEnum.STATUS.ONLINE.getCode());
        List<CouponDto> list = couponMapper.queryByTypeNameStatusForSend(dto);
        return ResponseResult.buildSuccessResponse(list);
    }

    @Override
    public ResponseResult<List<CouponDto>> queryByIds(@RequestParam("couponids") List<Long> couponids) {
        if (CollectionUtils.isEmpty(couponids)){
            return ResponseResult.buildFailResponse(REQUEST_PARAM_EMPTY.code(),REQUEST_PARAM_EMPTY.message());
        }
        List<CouponDto> couponDtos = couponMapper.queryByIds(couponids);
        return ResponseResult.buildSuccessResponse(couponDtos);
    }

    @Override
    public ResponseResult<Boolean> offline(@RequestBody CouponDto dto) {
        Coupon coupon = new Coupon();
        coupon.setId(dto.getId());
        coupon.setStatus(CouponEnum.STATUS.OFFLINE.getCode());
        int result = couponMapper.updateByPrimaryKeySelective(coupon);
        return ResponseResult.buildSuccessResponse(result > 0);
    }


    private ResponseResult<Pagenation<CouponDto>> checkForQuery(CouponQueryDto dto) {
        Byte type = dto.getType();
        if(type == null){
            return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_LACK_PARAM.code(),PromotionResponseCode.COUPON_LACK_PARAM.message());
        }
        if(dto.getBusinessLine() == null){
            return ResponseResult.buildFailResponse(PromotionResponseCode.COUPON_NO_BZL.code(),PromotionResponseCode.COUPON_NO_BZL.message());
        }
        if(dto.getPageSize() > 50){
            dto.setPageSize(50);
        }
        if(dto.getName()!= null){
            dto.setName(StringUtils.trim(dto.getName()));
        }
        return ResponseResult.buildSuccessResponse();
    }

    /**
     * 校验字符串是否含有中文
     * @param countname
     * @return
     */
    private static boolean checkCountName(String countname)
    {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(countname);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
