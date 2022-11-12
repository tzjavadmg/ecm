package com.milisong.ecm.breakfast.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;


/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/2 17:04
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDto {

    private Long id;//deliveryId
    private String code;//code
    private String name;//名称
    private String picture;//图片
    private String describe;//描述
    private Integer weight;//权重
    private BigDecimal advisePrice;//原价
    private BigDecimal preferentialPrice;//优惠价
    private Integer detailStatus;//状态
    private Integer stock;//库存余量(10点半之后是明日的库存量)
    private Integer monthSale;//月销数量
    private String bigPicture; //大图
    private String details;//详细描述
    private String taste;//口味
    private Integer spicy;//辣度 0不辣 1微辣 2中辣 3重辣 4变态辣
    private String categoryCode;//类目code
    private Integer stockFlag;
    private String newUserPrice;//新用户价格，根据最优折扣券计算出
    private Boolean isCombo;//是否是组合商品
    /**
     * 是否新品
     */
    private Boolean isNewGoods;
    
    /**
     * 组合商品  子商品信息
     */
   	private List<GoodsCombinationRefDto> listGoodsCombinationRefMqDto;
}
