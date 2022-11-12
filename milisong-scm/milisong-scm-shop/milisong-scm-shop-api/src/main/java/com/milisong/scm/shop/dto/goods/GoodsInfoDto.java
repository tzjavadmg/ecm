package com.milisong.scm.shop.dto.goods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/2 17:04
 */
@Getter
@Setter
public class GoodsInfoDto implements Serializable{

    private static final long serialVersionUID = -6941439920443085647L;
    private Long id;//id
    
    private String code;//code
    
    private String name;//名称
    
    private String picture;//图片
    
    private String describe;//描述
    
    private Integer weight;//权重
    
    private String categoryCode; // 类目code

    private String categoryName; // 类目名称
    
    private BigDecimal advisePrice;//原价
    
    private BigDecimal preferentialPrice;//优惠价
    
    private Integer detailStatus;//状态
    
    private Integer stockCount;//库存
    
    private Date beginDate;//开始时间
    /**
     * 0=主餐、1、小菜
     */
    private Byte type;
    
    private Date endDate;//结束时间
    
    private Integer status; //主状态
    
    private String bigPicture; // 大图片
	
	private Byte spicy; //辣度 0不辣 1微辣 2中辣 3重辣

    private String taste; //口味
}
