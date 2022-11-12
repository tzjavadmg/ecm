package com.milisong.oms.dto.shunfeng;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zhaozhonghui
 * @date 2018-10-23
 */
@Data
public class SfORderProductDetail {
    /** 商品名称 */
    private String productName;
    /** 商品id */
    private Long productId;
    /** 商品数量 */
    private Integer productNum;
    /** 商品价格 */
    private BigDecimal productPrice;
    /** 备注 */
    private String productRemark;
}
