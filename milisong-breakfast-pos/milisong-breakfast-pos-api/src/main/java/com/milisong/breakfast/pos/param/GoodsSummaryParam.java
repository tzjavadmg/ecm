package com.milisong.breakfast.pos.param;

import com.farmland.core.api.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhaozhonghui
 * @Description 商品汇总请求数据封装
 * @date 2018-12-04
 */
@Data
public class GoodsSummaryParam extends PageParam implements Serializable {
    private static final long serialVersionUID = -5534548064391693212L;
    @ApiModelProperty("门店id")
    private String shopId;
    @ApiModelProperty("配送日期 yyyy-MM-dd")
    private String deliveryDate;
    @ApiModelProperty("订单日期")
    private String orderDate;
    @ApiModelProperty("配送时间 HH:mm:ss")
    private String deliveryTime;
}
