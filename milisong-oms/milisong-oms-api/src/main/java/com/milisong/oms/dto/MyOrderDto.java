package com.milisong.oms.dto;


import com.milisong.oms.constant.GoodsType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class MyOrderDto {
	
    private Long id;
    
    //订单号
    private String orderNo;
    
    //门店code
    private String shopCode;
    
    //订单日期
    private Date orderDate;
    
    //支付总金额
    private BigDecimal totalPayAmount;
    
    //总订单天数
    private Integer totalOrderDays;
    
    //订单状态：0:待支付;1:支付失败;2:已取消;3:已支付;9:已完成;
    private Byte status;
    
    //红包表示(0:否 1：是)
    private Integer redPacketFlag;
    
    /**
     * 订单详情属性
     */
    
    //配送费原价总金额
    private BigDecimal totalDeliveryOriginalAmount;
    
    //配送费优惠价总金额
    private BigDecimal totalDeliveryActualAmount;
    
    //包装费原价总金额
    private BigDecimal totalPackageOriginalAmount;

    //包装费优惠价总金额
    private BigDecimal totalPackageActualAmount;
    
    //商品原价总金额
    private BigDecimal totalGoodsOriginalAmount;
    
    //商品实际总金额
    private BigDecimal totalGoodsActualAmount;

    //配送楼宇ID
    private Long deliveryBuildingId;
    
    //配送公司名称
    private String deliveryCompany;
    
    //配送楼层
    private String deliveryFloor;
    
    //联系人姓名
    private String realName;

    private Byte sex;

    private String takeMealsAddr;
    
    //联系手机号
    private String mobileNo;
    
    //红包ID
    private Long redPacketId;

    //红包金额
    private BigDecimal redPacketAmount;

    //红包ID
    private Long couponId;

    //红包金额
    private BigDecimal couponAmount;
    
    //订单日期
    private String orderDateStr;
    
    //状态值名称
    private String statusName;
    
    //楼宇名称
    private String buildName;

    //满减金额
    private BigDecimal fullReduceAmount;
    
    //抵扣使用积分
    private Long deductionPoints;

    //订单获得积分
    private Long acquirePoints;

    private Byte orderMode;
    
    //未支付倒计时
    private long countDown;

    //配送单信息集合
    private List<MyOrderDeliveryDto> orderDlivery;

    
    public BigDecimal getTotalGoodsDiscountAmount() {
    	if (totalGoodsOriginalAmount!=null && totalGoodsActualAmount!=null) {
            return totalGoodsOriginalAmount.subtract(totalGoodsActualAmount);
    	}
    	return new BigDecimal(0);
    }

    public Integer getPackageCount() {
        return orderDlivery.stream().flatMap(delivery -> delivery.getOrderDeliveryGoods().stream())
                .filter(g -> g.getType() == null || GoodsType.MAIN_MEAL.getValue() == g.getType())
                .mapToInt(MyOrderDeliveryGoodsDto::getGoodsCount).sum();
    }
}
