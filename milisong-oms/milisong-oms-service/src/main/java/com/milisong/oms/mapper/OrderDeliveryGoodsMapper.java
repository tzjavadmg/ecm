package com.milisong.oms.mapper;

import com.milisong.oms.domain.OrderDeliveryGoods;
import com.milisong.oms.mapper.base.BaseOrderDeliveryGoodsMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/10/7 8:55
 */
@Mapper
public interface OrderDeliveryGoodsMapper extends BaseOrderDeliveryGoodsMapper {

    /**
     * 批量插入配送单商品明细
     *
     * @param collection 商品明细
     */
    void batchSave(@Param("collection") Collection<OrderDeliveryGoods> collection);

    /**
     * 根据属于同一订单的配送单ID集合查找所有商品明细
     *
     * @param deliveryIds 配送单ID集合
     * @return 商品明细
     */
    List<OrderDeliveryGoods> batchFindByDeliveryIds(@Param("collection") Collection<Long> deliveryIds);


    List<OrderDeliveryGoods> findByDeliveryId(@Param("deliveryId") Long deliveryId);

    List<OrderDeliveryGoods> findAllByDeliveryId(@Param("deliveryId") Long deliveryId);

    List<Map<String, Object>> statLately29DaysSales();
}
