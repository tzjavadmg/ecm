package com.milisong.scm.goods.dto;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsScheduleDetailLunchDto {

	@ApiModelProperty("id")
    private Long id;

	@ApiModelProperty("门店d")
    private Long shopId;

	@ApiModelProperty("门店code")
    private String shopCode;

	@ApiModelProperty("门店名称")
    private String shopName;

	@ApiModelProperty("年份")
    private Integer year;

	@ApiModelProperty("W多少周")
    private Integer weekOfYear;

	@ApiModelProperty("商品编码")
    private String goodsCode;

	@ApiModelProperty("商品名字")
    private String goodsName;

	@ApiModelProperty("第1天是否可售 1可售 0不可售")
    private Boolean flag1;

	@ApiModelProperty("第2天是否可售 1可售 0不可售")
    private Boolean flag2;

	@ApiModelProperty("第3天是否可售 1可售 0不可售")
    private Boolean flag3;

	@ApiModelProperty("第4天是否可售 1可售 0不可售")
    private Boolean flag4;

	@ApiModelProperty("第5天是否可售 1可售 0不可售")
    private Boolean flag5;

	@ApiModelProperty("第6天是否可售 1可售 0不可售")
    private Boolean flag6;

	@ApiModelProperty("第7天是否可售 1可售 0不可售")
    private Boolean flag7;

    private Boolean isDeleted;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private Date updateTime;
}
