package com.milisong.scm.orderset.dto.mq;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发送给SCM的MQ消息类:配送商品明细
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/9/12 15:37
 */
@Getter
@Setter
public class OrderDeliveryGoodsMessage implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
    /**
     * 配送ID
     */
    private Long deliveryId;
    /**
     * 配送编号
     */
    private String deliveryNo;
    /**
     * 商品编号
     */
    private String goodsCode;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品数量
     */
    private Integer goodsCount;
    /**
     * 商品实价
     */
    private BigDecimal goodsActualPrice;
    /**
     * 包装费实价
     */
    private BigDecimal packageActualPrice;
    /**
     * 总费用
     */
    private BigDecimal totalAmount;

    /**
     * 商品原价和
     */
    private BigDecimal goodsOriginalPrice;
}
