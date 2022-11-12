package com.milisong.oms.mapper;

import com.milisong.oms.domain.GroupBuyOrderDelivery;
import com.milisong.oms.mapper.base.BaseGroupBuyOrderDeliveryMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/20 16:20
 */
@Mapper
public interface GroupBuyOrderDeliveryMapper extends BaseGroupBuyOrderDeliveryMapper {

    @Select({"SELECT *",
            "FROM group_buy_order_delivery",
            "WHERE order_id = #{orderId}"})
    List<GroupBuyOrderDelivery> findDeliveryListByOrderId(Long orderId);
}
