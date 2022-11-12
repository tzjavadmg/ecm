package com.mili.oss.dto.param;

import com.farmland.core.api.PageParam;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 预定订单查询条件
 *
 * @author yangzhilong
 */
@Getter
@Setter
public class OrderReserveSearchParamDto extends PageParam implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -7615465054257815171L;
    // 门店id
    private Long shopId;
    // 预定日期
    private String reserveDate;
    // 配送日期
    private String deliveryDate;
}
