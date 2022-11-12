package com.milisong.oms.mapper;

import com.milisong.oms.domain.OrderDelivery;
import com.milisong.oms.dto.OrderDeliveryDto;
import com.milisong.oms.dto.OrderDeliveryMqDto;
import com.milisong.oms.mapper.base.BaseOrderDeliveryMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/7 8:40
 */
@Mapper
public interface OrderDeliveryMapper extends BaseOrderDeliveryMapper {

    /**
     * 批量插入订单配送信息
     *
     * @param orderDeliverys 订单配送信息
     */
    void batchSave(@Param("collection") Collection<OrderDelivery> orderDeliverys);

    List<OrderDelivery> findUncompletedDeliveriesByOrderId(@Param("orderId") Long orderId);

    List<OrderDeliveryDto> getOrderListByDeliveryDate(@Param("businessLine") Byte businessLine, @Param("shopCode") String shopCode, @Param("deliveryDate") Date deliveryDate);

    /**
     * 根订单ID查找配送单
     *
     * @param orderId 虚拟订单ID
     * @return 配送单集合
     */
    List<OrderDelivery> findDeliveryDateListByOrderId(@Param("orderId") Long orderId);

    /**
     * 根订单ID查找配送单
     *
     * @param orderId 虚拟订单ID
     * @return 配送单集合
     */
    List<OrderDelivery> findListByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据配送单号查询已完成的配送单
     *
     * @param deliveryNo
     * @param statusList
     * @return
     */
    List<OrderDelivery> findFinishOrder(@Param("deliveryNo") String deliveryNo, @Param("statusList") List<Byte> statusList);

    /**
     * 根据配送单号更新时间及状态
     *
     * @param deliveryMqDto
     * @return
     */
    int updateDeliveryByOrderNo(OrderDeliveryMqDto deliveryMqDto);

    /**
     * @param ids
     * @return
     */
    List<OrderDelivery> batchFind(@Param("collection") Collection<Long> ids);

    /**
     * @param deliveryDate
     * @param status
     * @return
     */
    List<OrderDeliveryDto> getDeliveryListByDeliveryDateAndStatus(@Param("deliveryDate") Date deliveryDate, @Param("status") Byte status, byte orderType);
    
    
    /**
     * 根据业务线、配送时间查询配送单号集合
     * @param businessLine
     * @param deliveryDate
     * @return
     */
    List<String> getDeliveryNos(@Param("businessLine") Byte businessLine,@Param("deliveryDate") String deliveryDate);
    
    /**
     * 根据配送单号查询配送单
     * @param deliveryNo
     * @return
     */
    OrderDelivery findDeliveryByNo(@Param("deliveryNo") String deliveryNo);
    
    /**
     * 修改配送单在状态为已完成
     * task任务
     */
    int updateOrderDeliveryStatus(@Param("nodeHour") Integer nodeHour);

    /**
     * 根据订单id 检查有多少备餐中的配送单
     * @param orderId
     */
	List<Long> checkOrderDeliveryStatusCount(@Param("orderId")List<Long> orderId);

    /**
     * 根据配送单号查询配送预期开始和结束时间
     * @param deliveryNo
     * @return
     */
    OrderDelivery getDeliveryTimezoneByNo(@Param("deliveryNo") String deliveryNo);
    
}
