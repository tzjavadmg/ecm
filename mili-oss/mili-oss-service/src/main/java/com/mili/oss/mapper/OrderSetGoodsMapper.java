package com.mili.oss.mapper;

import com.mili.oss.domain.OrderSetGoods;
import com.mili.oss.dto.result.OrderSetDetailStatusDto;
import com.mili.oss.dto.DistributionOrdersetInfoResult;
import com.mili.oss.mapper.base.BaseOrderSetGoodsMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author zhaozhonghui
 * @Description 集单商品
 * @date 2019-02-25
 */
public interface OrderSetGoodsMapper extends BaseOrderSetGoodsMapper {

    List<Map<String, Object>> selectCountCustomerOrderBySetNo(List<String> list);

	List<OrderSetDetailStatusDto> listAllStatusByOrderNo(String orderNo);
	
    List<DistributionOrdersetInfoResult> getCustomerOrderByOrderSetNo(@Param("detailSetNo")String detailSetNo);

    void batchSave(@Param("collection")Collection<OrderSetGoods> orderSetGoodss);
}
