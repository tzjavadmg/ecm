package com.milisong.scm.goods.param;

import java.util.Date;

import com.farmland.core.api.PageParam;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoosSalableLunchParam extends PageParam{

	private Long shopId;
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
	private Date saleDate;
}
