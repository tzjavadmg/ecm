package com.milisong.oms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/2 16:35
 */
@Getter
@Setter
@NoArgsConstructor
public class GoodsStockDto {

    String goodsCode;
    int goodsCount;
    String goodsName;
    
    public GoodsStockDto(String goodsCode,int goodsCount) {
    	this.goodsCode = goodsCode;
    	this.goodsCount = goodsCount;
    }
    
    public GoodsStockDto(String goodsCode, int goodsCount, String goodsName) {
    	this.goodsCode = goodsCode;
    	this.goodsCount = goodsCount;
    	this.goodsName = goodsName;
    	
    }
   
}
