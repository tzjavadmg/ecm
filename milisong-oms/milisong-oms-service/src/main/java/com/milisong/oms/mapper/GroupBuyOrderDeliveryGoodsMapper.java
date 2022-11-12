package com.milisong.oms.mapper;

import com.milisong.oms.domain.GroupBuyOrderDelivery;
import com.milisong.oms.domain.GroupBuyOrderDeliveryGoods;
import com.milisong.oms.domain.OrderDeliveryGoods;
import com.milisong.oms.mapper.base.BaseGroupBuyOrderDeliveryGoodsMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

/**
 * @description: TODO
 * @program: milisong-workspace
 * @author: codyzeng@163.com
 * @date: 2019/5/20 16:20
 */
@Mapper
public interface GroupBuyOrderDeliveryGoodsMapper extends BaseGroupBuyOrderDeliveryGoodsMapper {

    @Select({"SELECT *",
            "FROM group_buy_order_delivery_goods",
            "WHERE delivery_id = #{deliveryId}"})
    List<GroupBuyOrderDeliveryGoods> findGoodsListByDeliveryId(Long deliveryId);

    @Select({"<script>SELECT * FROM group_buy_order_delivery_goods WHERE delivery_id IN\n" +
            "        <foreach collection=\"collection\" item=\"id\" open=\"(\" separator=\",\" close=\")\">\n" +
            "            #{id}\n" +
            "        </foreach> </script>"})
    List<GroupBuyOrderDeliveryGoods> batchFindByDeliveryIds(@Param("collection") Collection<Long> deliveryIds);
}
