package com.milisong.oms.dto.shunfeng;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zhaozhonghui
 * @date 2018-10-23
 */
@Data
@JsonInclude
public class SfOrderDetail {
    /** 订单总金额 */
    private BigDecimal totalPrice;
    /** 商品类型 1送餐; 2送药; 3百货; 4脏衣收; 5干净衣派; 6生鲜; 7保单;8饮品；9现场勘查；99其他 */
    private Byte productType;
    /** 实收用户金额 分*/
    private BigDecimal userMoney;
    /** 实付商户金额 分*/
    private BigDecimal shopMoney;
    /** 商户收取的配送费 分*/
    private BigDecimal deliveryMoney;
    /** 商品重量 */
    private BigDecimal weightGram;
    /** 商品个数 */
    private Integer productNum;
    /** 商品类型数量 */
    private Integer productTypeNum;
    /** 商品详情 */
    private List<SfORderProductDetail> productDetails;
}
