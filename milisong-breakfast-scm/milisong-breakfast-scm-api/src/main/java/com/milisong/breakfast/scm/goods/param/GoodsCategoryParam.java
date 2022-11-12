package com.milisong.breakfast.scm.goods.param;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodsCategoryParam {

	  /**
     * 主键
     */
    private Long id;

    /**
     * 类目code
     */
    private String code;

    /**
     * 类目名称
     */
    private String name;

    /**
     * 类目图片
     */
    @ApiModelProperty("类目图片")
    private String picture;

    /**
     * 类目简介
     */
    @ApiModelProperty("类目简介")
    private String describe;

    /**
     * 排序号(权重)
     */
    @ApiModelProperty("排序号(权重)")
    private Byte weight;

    /**
     * 父类目id
     */
    private Long pid;

    /**
     * 状态1有效 0无效
     */
    @ApiModelProperty("状态1有效 0无效")
    private Byte status;

    /**
     * 分类类型(1单品，2套餐)
     */
    @ApiModelProperty("分类类型(1单品，2套餐)")
    private Byte type;

    /**
     * 是否删除 1是 0否
     */
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
