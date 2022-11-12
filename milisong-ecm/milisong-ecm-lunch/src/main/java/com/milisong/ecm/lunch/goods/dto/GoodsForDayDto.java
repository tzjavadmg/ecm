package com.milisong.ecm.lunch.goods.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GoodsForDayDto {
    private String categoryName;
    private String categoryCode;
    private boolean hasChild;
    private List<GoodsCatalogDto> children;
    private List<GoodsDto> goods;
    private Integer weight;//权重
    private String notifyContent;//通知内容
}
