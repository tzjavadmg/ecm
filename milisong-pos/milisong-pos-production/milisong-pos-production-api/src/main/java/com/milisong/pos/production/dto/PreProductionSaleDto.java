package com.milisong.pos.production.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
*@author    created by benny
*@date  2018年10月24日---下午7:43:06
*
*/
@Data
public class PreProductionSaleDto extends PreProductionDto{

	List<SaleVolumes> list = new ArrayList<SaleVolumes>();
}
