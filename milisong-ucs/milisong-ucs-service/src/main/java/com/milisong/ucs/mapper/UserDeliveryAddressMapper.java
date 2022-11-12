package com.milisong.ucs.mapper;

import com.milisong.ucs.domain.UserDeliveryAddress;
import com.milisong.ucs.dto.SendBreakCouponRequest;
import com.milisong.ucs.dto.UserDeliveryAddressDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserDeliveryAddressMapper {

    int insert(UserDeliveryAddress record);

    int insertSelective(UserDeliveryAddress record);

    UserDeliveryAddress selectByPrimaryKey(Long id);

    int updateByPrimaryKey(UserDeliveryAddress record);

    int updateByPrimaryKeySelective(UserDeliveryAddress record);

    UserDeliveryAddress selectByUserid(@Param("userId") Long userId, @Param("deliveryOfficeBuildingId") Long deliveryOfficeBuildingId,@Param("businessLine")Byte businessLine);

    UserDeliveryAddress selectDeliveryAddrByManyWhere(@Param("mobileNo") String mobileNo,
                                                      @Param("deliveryOfficeBuildingId") Long deliveryOfficeBuildingId,
                                                      @Param("deliveryFloor") String deliveryFloor,
                                                      @Param("deliveryCompany") String deliveryCompany);

    UserDeliveryAddress selectByMobileAndId(Long id,String mobile);
    
    /**
     * 根据楼宇+前楼层修改楼层信息
     * @param params
     */
    void updateFloorByCondition(Map<String,Object> params);

    /**
     * 根据楼宇和楼层查询用户信息
     * @param params
     * @return
     */
    List<UserDeliveryAddress> selectUserIdByCondtion (Map<String,Object> params);

    int countUserByDevlieryAddr(SendBreakCouponRequest sendBreakCouponRequest);

    List<UserDeliveryAddressDto> queryUserByDevlieryAddr(SendBreakCouponRequest sendBreakCouponRequest);

    List<UserDeliveryAddressDto> queryDeliveryCompany(@Param("companyName") String companyName,@Param("buzLine")Byte buzLine);

    List<UserDeliveryAddressDto> queryDeliveryCompanyNames(@Param("deliveryOfficeBuildingIdList")List<Long> deliveryOfficeBuildingIdList);

    UserDeliveryAddress queryDeliveryByCompanyIdAndUserId(@Param("userId") Long userId, @Param("deliveryOfficeBuildingId") Long deliveryOfficeBuildingId, @Param("businessLine") Byte businessLine)
            ;
}