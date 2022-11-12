package com.milisong.oms.mapper;

import com.milisong.oms.domain.RefundOrder;
import com.milisong.oms.mapper.base.BaseRefundOrderMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/9 9:50
 */
@Mapper
public interface RefundOrderMapper extends BaseRefundOrderMapper {

    BigDecimal getOrderTotalRefundAmount(@Param("orderId") Long orderId);
    
    /**
     * 查询退款中订单
     * @return
     */
    List<RefundOrder> getRefundingOrder(@Param("status") Byte status);

    List<RefundOrder> getRefundOrderByOrderId(@Param("orderId") Long orderId);
}
