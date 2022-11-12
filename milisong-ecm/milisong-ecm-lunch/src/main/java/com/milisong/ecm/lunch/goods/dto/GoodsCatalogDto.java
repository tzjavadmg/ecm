package com.milisong.ecm.lunch.goods.dto;

import lombok.Getter;
import lombok.Setter;

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
public class GoodsCatalogDto {

    private String categoryName;
    private String categoryCode;
    private boolean hasChild;
    private List<GoodsCatalogDto> children;
    private List<GoodsDto> goods;
    private Integer weight;//权重
}
