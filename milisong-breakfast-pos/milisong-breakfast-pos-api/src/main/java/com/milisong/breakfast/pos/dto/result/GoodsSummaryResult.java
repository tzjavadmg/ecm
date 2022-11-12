package com.milisong.breakfast.pos.dto.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhaozhonghui
 * @Description 商品汇总数据返回
 * @date 2018-12-04
 */
@Data
public class GoodsSummaryResult implements Serializable {
    private static final long serialVersionUID = 3474479531356012551L;
    /** 预定日期 */
    @ApiModelProperty("预定日期")
    private Date orderDate;
    /** 配送日期 */
    @ApiModelProperty("配送日期")
    private Date deliveryDate;
    /** 商品名称 */
    @ApiModelProperty("商品名称")
    private String goodsName;
    /** 顾客订购数量 */
    @ApiModelProperty("商品总数")
    private String totalCount;

}
