package com.milisong.scm.shop.dto.goods;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 商品类目信息
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/2 17:04
 */
@Getter
@Setter
public class GoodsCatalogDto {

    private String categoryName;
    
    private String categoryCode;
    
    private boolean hasChild;
    
    private List<GoodsCatalogDto> children;
    
    private List<GoodsInfoDto> goods;
    
    private Integer weight;//权重
}
