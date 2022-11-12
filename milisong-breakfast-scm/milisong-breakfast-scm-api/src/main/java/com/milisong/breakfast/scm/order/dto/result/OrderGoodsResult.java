package com.milisong.breakfast.scm.order.dto.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhaozhonghui
 * @date 2018-11-21
 */
@Data
public class OrderGoodsResult implements Serializable {
    private static final long serialVersionUID = 6339716096741138702L;
    /** 商品名称 */
    private String goodsName;
    /** 商品数量 */
    private Integer goodsNumber;
    /** 商品编码 */
    private String orderNo;
}
