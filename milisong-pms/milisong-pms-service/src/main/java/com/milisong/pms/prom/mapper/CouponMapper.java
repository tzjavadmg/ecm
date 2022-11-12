package com.milisong.pms.prom.mapper;

import com.milisong.pms.prom.domain.Coupon;
import com.milisong.pms.prom.dto.CouponDto;
import com.milisong.pms.prom.dto.CouponQueryDto;

import java.util.List;

public interface CouponMapper {

    int insert(Coupon record);

    int insertSelective(Coupon record);

    Coupon selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Coupon record);

    int updateByPrimaryKey(Coupon record);

    int queryByTypeNameStatusCount(CouponQueryDto dto);

    List<CouponDto> queryByTypeNameStatus(CouponQueryDto dto);

    List<CouponDto> queryByIds(List<Long> couponids);

    List<CouponDto> queryByTypeNameStatusForSend(CouponQueryDto dto);
}