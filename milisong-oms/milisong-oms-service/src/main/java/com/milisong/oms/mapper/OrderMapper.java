package com.milisong.oms.mapper;

import com.milisong.oms.domain.Order;
import com.milisong.oms.dto.MyOrderDto;
import com.milisong.oms.dto.OrderDto;
import com.milisong.oms.dto.OrderPlanDeliveryDto;
import com.milisong.oms.mapper.base.BaseOrderMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseOrderMapper {

    /**
     * 我的订单总数量
     *
     * @param userId
     * @return
     */
    int selectOrderForCount(@Param("userId") long userId, @Param("orderType") Byte orderType);

    List<MyOrderDto> selectOrderDetail(@Param("userId") long userId, @Param("orderType") Byte orderType, @Param("startRow") int startRow,
                                       @Param("pageSize") int pageSize);

    /**
     * 我的订单详情
     *
     * @param userId
     * @param orderId
     * @return
     */
    MyOrderDto selectOrderDetailById(@Param("userId") long userId, @Param("orderId") Long orderId);

    /**
     * 根据订单编号查询订单信息
     *
     * @param orderNo
     * @return
     */
    Order selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据状态查询订单
     *
     * @param payStatus
     * @return
     */
    List<Order> selectOrderByStatus(@Param("status") Byte status, @Param("unPayExpiredTime") int unPayExpiredTime);

    /**
     * 我的订单计划
     *
     * @param userId
     * @param orderStatusList
     * @param deliveryStatusList
     * @return
     */
    List<OrderPlanDeliveryDto> selectOrderPlan(@Param("userId") long userId,
                                               @Param("orderStatusList") List<Byte> orderStatusList,
                                               @Param("deliveryStatusList") List<Byte> deliveryStatusList);

    /**
     * 我的订单计划
     *
     * @param userId
     * @param orderStatusList
     * @param deliveryStatusList
     * @return
     */
    List<OrderPlanDeliveryDto> selectOrderPlan2(@Param("userId") long userId,
                                               @Param("orderStatusList") List<Byte> orderStatusList,
                                               @Param("deliveryStatusList") List<Byte> deliveryStatusList,@Param("orderType") Byte orderType);

    List<Order> selectByOrderNoList(@Param("orderNoList") List<String> orderNoList);

    OrderDto selectOrderPoints(@Param("orderId") Long orderId);

    /**
     * 根据订单状态和类型查询订单
     * @param status
     * @param orderType
     */
	List<Order> selectOrderByStatusAndOrderType(@Param("status")byte status);

	/**
	 * 修改订单状态为已完成
	 * @param orderId
	 * @return
	 */
	int updateOrderStatusByOrderId(@Param("orderId")List<Long> orderId);
}