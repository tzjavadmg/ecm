package com.milisong.scm.base.dto.param;

import com.farmland.core.api.PageParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 早餐-公司信息
 */
@Getter
@Setter
public class CompanySearchDto extends PageParam implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2365597986511930558L;

	/**
     * 
     */
    private Long id;

    /**
     * 公司名字
     */
    @ApiModelProperty("公司名字")
    private String name;

    /**
     * 楼宇名字
     */
    @ApiModelProperty("楼宇名字")
    private String buildingName;

    /**
     * 门店id
     */
    @ApiModelProperty("门店id")
    private Long shopId;

    /**
     * 开通状态 1开通 0关闭
     */
    @ApiModelProperty("开通状态 1开通 0关闭")
    private Byte openStatus;
}