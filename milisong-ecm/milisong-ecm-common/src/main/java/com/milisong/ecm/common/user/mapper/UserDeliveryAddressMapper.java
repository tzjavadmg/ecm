package com.milisong.ecm.common.user.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.milisong.ecm.common.user.domain.UserDeliveryAddress;

@Mapper
public interface UserDeliveryAddressMapper {

    int insert(UserDeliveryAddress record);

    int insertSelective(UserDeliveryAddress record);

    UserDeliveryAddress selectByPrimaryKey(Long id);

    int updateByPrimaryKey(UserDeliveryAddress record);

    int updateByPrimaryKeySelective(UserDeliveryAddress record);

    UserDeliveryAddress selectByUserid(@Param("userId") Long userId, @Param("deliveryOfficeBuildingId") Long deliveryOfficeBuildingId);

    UserDeliveryAddress selectDeliveryAddrByManyWhere(@Param("mobileNo") String mobileNo,
                                                      @Param("deliveryOfficeBuildingId") Long deliveryOfficeBuildingId,
                                                      @Param("deliveryFloor") String deliveryFloor,
                                                      @Param("deliveryCompany") String deliveryCompany);

    UserDeliveryAddress selectByMobile(String mobile);
    
    /**
     * 根据楼宇+前楼层修改楼层信息
     * @param deliveryFloor
     * @param deliveryOfficeBuildingId
     */
    void updateFloorByCondition(Map<String,Object> params);

    /**
     * 根据楼宇和楼层查询用户信息
     * @param params
     * @return
     */
    List<UserDeliveryAddress> selectUserIdByCondtion (Map<String,Object> params);
}
