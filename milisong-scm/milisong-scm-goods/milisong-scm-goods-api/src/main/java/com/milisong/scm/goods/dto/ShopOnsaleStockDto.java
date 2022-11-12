package com.milisong.scm.goods.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年9月4日---下午6:31:48
*
*/
@Data
public class ShopOnsaleStockDto {

	String shopCode;
    Date saleDate;
    List<GoodsStockDto> goodsStocks;
}
