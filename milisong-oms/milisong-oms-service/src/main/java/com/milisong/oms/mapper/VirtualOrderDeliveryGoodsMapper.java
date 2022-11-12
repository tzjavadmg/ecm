package com.milisong.oms.mapper;

import com.milisong.oms.domain.VirtualOrderDeliveryGoods;
import com.milisong.oms.mapper.base.BaseVirtualOrderDeliveryGoodsMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/2 15:43
 */
@Mapper
public interface VirtualOrderDeliveryGoodsMapper extends BaseVirtualOrderDeliveryGoodsMapper {

    /**
     * 批量插入配送单商品明细
     *
     * @param collection 商品明细
     */
    void batchSave(@Param("collection") Collection<VirtualOrderDeliveryGoods> collection);

    /**
     * 根据属于同一订单的配送单ID集合查找所有商品明细
     *
     * @param deliveryIds 配送单ID集合
     * @return 商品明细
     */
    List<VirtualOrderDeliveryGoods> batchFindByDeliveryIds(@Param("collection") Collection<Long> deliveryIds);

}
