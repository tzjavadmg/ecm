package com.mili.oss.mapper;

import com.mili.oss.dto.OrderSetDetailDto;
import com.mili.oss.dto.OrderSetReqDto;
import com.mili.oss.mapper.base.BaseOrderSetMapper;

import java.util.List;

/**
 * @author zhaozhonghui
 * @Description ${Description}
 * @date 2019-02-25
 */
public interface OrderSetMapper extends BaseOrderSetMapper {

    List<OrderSetDetailDto> selectPage(OrderSetReqDto param);

    long selectCount(OrderSetReqDto param);

    List<Long> queryDelayOrderSet();
}
