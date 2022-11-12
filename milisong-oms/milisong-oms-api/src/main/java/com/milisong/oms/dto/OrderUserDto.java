package com.milisong.oms.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderUserDto {
	
    private Long id;
    
    //订单号
    private String orderNo;
    
    //门店code
    private String shopCode;
   
    //订单日期(格式化)
    private String orderDate;
    
    //订单日期
    private Date orderDateF;
    
    //实付金额
    private BigDecimal actualPayAmount;
    
    private Short orderType;
    
    //预定天数
    private Integer preOrderDays; 
    
    //预计配送时间
    private Long deliveryStartDate;
    
    private Short orderStatus;
    
    //总商品数量
    private Integer totalGoodsCount;
    
    //订单状态描述
    private String orderStatusName;
    
    //预计配送时间
    private Date deliveryDate;
    
    //周几
    private String weekDay;
    
    //支付状态
    private Short payStatus;
    
    //配送费原价总金额
    private BigDecimal totalDeliveryCostAmount;
    
    //配送费优惠后总金额
    private BigDecimal totalDeliveryCostDiscountAmount;
    
    //楼宇id
    private Long deliveryOfficeBuildingId;
    
    //楼宇名称
    private String buildName;
    
    //楼层
    private String deliveryFloor;
    
    //公司
    private String deliveryCompany;
    
    //联系人
    private String realName;

    //联系电话
    private String mobileNo;
    
    //包装费总金额
    private BigDecimal totalPackageAmount;
    
    //包装费优惠后总金额
    private BigDecimal totalPackageDiscountAmount;
    
    //商品优惠
    private BigDecimal discountDiffAmount;
    
    //未支付倒计时
    private long countDown;
    
    //父单状态描述
    private String parentStatusName;
    
    //红包表示(0:否 1：是)
    private Integer redPacketFlag;
    
    //红包金额
    private BigDecimal redPacketAmount;
    
    //红包Id
    private Long redPacketId;

    
    //按天维度list
    private List<OrderUserDto> multiDayOrders;
    
    //订单明细list
    private List<OrderUserDetailDto> orderUserDetails;



}
