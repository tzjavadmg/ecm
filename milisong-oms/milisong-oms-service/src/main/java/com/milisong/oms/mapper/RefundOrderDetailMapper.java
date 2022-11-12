package com.milisong.oms.mapper;

import com.milisong.oms.domain.RefundOrderDetail;
import com.milisong.oms.dto.RefundOrderDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/10 14:12
 */
@Mapper
public interface RefundOrderDetailMapper extends RefundOrderMapper {

    void batchSave(@Param("collection") Collection<RefundOrderDetail> refundOrderDetails);

    List<RefundOrderDetail> getListByRefundId(@Param("refundId") Long refundId);
    
    RefundOrderDetailDto getRefundOrderDetail(@Param("deliveryId") Long deliveryId);
}
