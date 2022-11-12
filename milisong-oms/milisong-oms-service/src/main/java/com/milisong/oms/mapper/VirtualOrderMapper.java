package com.milisong.oms.mapper;

import com.milisong.oms.domain.VirtualOrder;
import com.milisong.oms.mapper.base.BaseVirtualOrderMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/8 16:28
 */
@Mapper
public interface VirtualOrderMapper extends BaseVirtualOrderMapper {
	
    /**
     * 根据状态查询虚拟订单
     * @param payStatus
     * @return
     */
    List<VirtualOrder> getVirtualOrderByStatus(@Param("status") Byte status,@Param("unPayExpiredTime") int unPayExpiredTime);
}
