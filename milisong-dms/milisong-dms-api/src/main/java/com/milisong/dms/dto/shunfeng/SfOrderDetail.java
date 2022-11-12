package com.milisong.dms.dto.shunfeng;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhaozhonghui
 * @date 2018-10-23
 */
@Data
public class SfOrderDetail implements Serializable {
    private static final long serialVersionUID = 8454158583218075384L;
    /** 订单总金额 分*/
    private Long totalPrice;
    /** 商品类型 1送餐; 2送药; 3百货; 4脏衣收; 5干净衣派; 6生鲜; 7保单;8饮品；9现场勘查；99其他 */
    private Byte productType;
    /** 商品重量 克*/
    private Integer weightGram;
    /** 商品个数 */
    private Integer productNum;
    /** 商品类型数量 */
    private Integer productTypeNum;
    /** 商品详情 */
    private List<SfORderProductDetail> productDetail;
}
