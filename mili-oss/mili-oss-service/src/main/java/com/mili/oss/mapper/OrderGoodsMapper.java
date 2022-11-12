package com.mili.oss.mapper;

import com.mili.oss.algorithm.model.GoodsModel;
import com.mili.oss.domain.OrderGoods;
import com.mili.oss.dto.OrderReserveSearchResultDto;
import com.mili.oss.dto.param.OrderReserveSearchParamDto;
import com.mili.oss.mapper.base.BaseOrderGoodsMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface OrderGoodsMapper extends BaseOrderGoodsMapper {

    Set<GoodsModel> getOrderGoodsByOrderId(@Param("orderId") Long orderId);

    List<GoodsModel> getGoodsModelsByMealAddrIdAndDeliveryTime(@Param("orderType") Byte orderType,@Param("mealAddressId") Long mealAddressId, @Param("deliveryBeginDate") Date deliveryBeginDate);

    long getReserveOrderGroupCount(OrderReserveSearchParamDto param);

    List<OrderReserveSearchResultDto> getReserveOrderGroupList(OrderReserveSearchParamDto param);

    List<OrderGoods> batchSearchByOrderNo(List<String> orderNoList);
}
