package com.mili.oss.mapper;

import java.util.Date;
import java.util.List;

import com.mili.oss.dto.result.ShopGoodsCount;
import org.apache.ibatis.annotations.Param;

import com.mili.oss.algorithm.model.OrderModel;
import com.mili.oss.dto.OrderSearchResult;
import com.mili.oss.dto.OrderSearchResultPos;
import com.mili.oss.dto.begin.UserAmount;
import com.mili.oss.dto.param.OrderGoodsCompanySqlParam;
import com.mili.oss.dto.param.OrderSearchParam;
import com.mili.oss.dto.param.OrderSearchParamPos;
import com.mili.oss.mapper.base.BaseOrderMapper;

public interface OrderMapper extends BaseOrderMapper {

    List<UserAmount> groupByUserId(@Param("shopId") Long shopId, @Param("mealAddressId") Long mealAddressId, @Param("deliveryBeginDate") Date deliveryBeginDate);

    List<OrderModel> getOrderByUserIdAndTime(@Param("shopId") Long shopId, @Param("mealAddressId") Long mealAddressId, @Param("deliveryBeginDate") Date deliveryBeginDate, @Param("userId") Long userId);

    List<OrderSearchResult> selectOrderPage(OrderSearchParam param);

    Long selectOrderCount(OrderSearchParam param);

    List<OrderSearchResultPos> selectOrderPagePos(OrderSearchParamPos param);

    Long selectOrderCountPos(OrderSearchParamPos param);
    
    Integer selectOrderGoodsCountCompany(OrderGoodsCompanySqlParam param);

    long batchUpdateStockStatus(List<String> list);

    long updateOrderShop(@Param("companyId")Long companyId,@Param("shopId")Long shopId,@Param("shopCode")String shopCode);

    List<ShopGoodsCount> getShopGoodsCount(@Param("shopCode")String shopCode, @Param("companyId") Long companyId);
}
