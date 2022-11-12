package com.milisong.oms.mapper;

import com.milisong.oms.domain.VirtualOrderDelivery;
import com.milisong.oms.mapper.base.BaseVirtualOrderDeliveryMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/2 15:18
 */
@Mapper
public interface VirtualOrderDeliveryMapper extends BaseVirtualOrderDeliveryMapper {

    /**
     * 批量插入虚拟订单配送信息
     *
     * @param orderDeliverys 订单配送信息
     */
    void batchSave(@Param("collection") Collection<VirtualOrderDelivery> orderDeliverys);

    /**
     * 根虚拟订单ID查找配送日期集合
     *
     * @param orderId 虚拟订单ID
     * @return 配送日期集合
     */
    List<VirtualOrderDelivery> findDeliveryDateListByOrderId(@Param("orderId") Long orderId);

    /**
     * 根虚拟订单ID查找配送表集合
     *
     * @param orderId 虚拟订单ID
     * @return 配送表集合
     */
    List<VirtualOrderDelivery> findListByOrderId(Long orderId);
}
