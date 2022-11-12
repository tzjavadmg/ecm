package com.milisong.pos.production.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO〈一句话功能简述〉<br>
 *
 * @author zengyuekang
 * @version 1.0.0
 * @date 2018/8/31 14:56
 */
@Getter
@Setter
public class OrderDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3401722471274842596L;

	private Long id;
	// 用户ID
	private Long userId;
	// 订单编号
    private String orderNo;
    // 顾客名字
    private String realName;
    // 手机号
    private String mobileNo;
    // 预计配送开始时间
    private Date deliveryStartDate;
    // 预计配送结束时间
    private Date deliveryEndDate;
    // 楼宇id
    private Long deliveryOfficeBuildingId;
    // 配送公司
    private String deliveryCompany;
    // 配送详细地址
    private String deliveryAddress;
    // 楼层
    private String deliveryFloor;
    // 房间号（暂无值）
    private String deliveryRoom;
    // 订单总金额
    private BigDecimal totalAmount;
    // 实际支付金额
    private BigDecimal actualPayAmount;
    // 折扣后金额
    private BigDecimal discountAmount;
    // 配送费金额
    private BigDecimal deliveryCostAmount;
    
    // 打包费
    private BigDecimal packageAmount;
    // 红包金额
    private BigDecimal redPacketAmount;
    
    // 订单类型0:普通;1:预定
    private Short orderType;
    // 门店编号
    private String shopCode;
    // 订单状态  0:配送中 1:已打包-待配送 2:已支付-备餐中 3:已申请退款 4:已退款 9:已完成
    private Short orderStatus;
    // 订单明细数据
    private List<OrderDetailDto> orderDetails;
}
