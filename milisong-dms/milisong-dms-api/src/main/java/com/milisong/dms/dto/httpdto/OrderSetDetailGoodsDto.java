package com.milisong.dms.dto.httpdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class OrderSetDetailGoodsDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7954539608521895432L;

	private Long id;

    private String detailSetNo;

    private String orderNo;
    
    private String coupletNo;

    private Long userId;

    private String userName;

    private String userPhone;

    private String goodsCode;

    private String goodsName;

    private Integer goodsNumber;

    private Boolean isDeleted;

    private String createBy;

    private Date createTime;

    private String updateBy;

    private Date updateTime;
    
    private BigDecimal goodsPrice;
    
    private BigDecimal goodsDiscountPrice;
    
    /**
     * 单件商品实付金额
     */
    private BigDecimal actualPayAmount;
    
    private Date orderTime;
}